package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.addPass(HotSwapCompilerPass.class);
    rootNode = parseTestFile("TestScopedAliases.js");
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(Paths.get("TestScopedAliases.js"));
  }

  @Test
  public void testNormalUsage() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    // There should be no errors during compilation.
    assertThat(compiler.getDiagnostics().getSeverities()).isEmpty();

    // There should be no alias definitions.
    assertThat(aliasDefinitions).isEmpty();

    // There should be one scope call.
    Node scopeCall = scopeCalls.get(0);
    assertThat(scopeCall.getFirstChild()).isInstanceOf(Token.CallExpr);
    Node scopeMethod = scopeCall.getFirstChild().getFirstChild();
    assertThat(scopeMethod.getString()).isEqualTo("goog.scope");

    // There should be one alias usage.
    Node aliasUsage = aliasUsages.get(0);
    Node aliasReference = aliasUsage.getNode();
    Node aliasDefinition = aliasUsage.getAliasDefinition();
    Node aliasNode = aliasDefinition.getLastChild();

    // The alias reference should be replaced with the alias definition.
    assertThat(aliasReference.getParent()).isSameInstance(aliasDefinition.getParent());
    assertThat(aliasDefinition.getParent()).isSameInstance(aliasUsage.getNode().getParent());

    // The alias definition and the alias node should have the same name.
    assertThat(aliasDefinition.getString()).isEqualTo(aliasUsage.getNode().getString());
    assertThat(aliasNode.getString()).isEqualTo("goog.dom");
  }

  @Test
  public void testImproperUsage() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestImproperUsage.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_USED_IMPROPERLY");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  @Test
  public void testBadParameters() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestBadParameters.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  @Test
  public void testRefersThis() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestRefersThis.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  @Test
  public void testUsesReturn() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestUsesReturn.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  @Test
  public void testUsesThrow() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestUsesThrow.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_USES_THROW");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  @Test
  public void testAliasesRedefined() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestAliasesRedefined.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_ALIAS_REDEFINED");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  @Test
  public void testNonAliasLocal() {
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node rootNode = parseTestFile("TestNonAliasLocal.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // There should be an error during compilation.
    DiagnosticType errorType = DiagnosticType.error("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
    Node errorNode = rootNode.getChildAtIndex(1);
    List<DiagnosticType> severities = compiler.getDiagnostics().getSeverities();
    assertThat(severities).contains(errorType);
    assertThat(errorNode.getParent()).isSameInstance(rootNode);
    assertThat(errorNode.getNext()).isNull();
  }

  private Node parseTestFile(String fileName) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(fileName));
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(join(lines, System.lineSeparator()), options);

    Files.deleteIfExists(Paths.get(fileName));
    return root;
  }

  private String join(Iterable<String> iterable, String separator) {
    ImmutableList<String> list = ImmutableList.copyOf(iterable);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      if (i > 0) {
        result.append(separator);
      }
      result.append(list.get(i));
    }
    return result.toString();
  }
}