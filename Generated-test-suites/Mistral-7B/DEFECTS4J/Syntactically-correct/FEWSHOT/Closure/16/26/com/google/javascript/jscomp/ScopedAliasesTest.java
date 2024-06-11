package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.aliastransformation.AliasTransformation;
import com.google.javascript.jscomp.aliastransformation.AliasTransformationHandler;
import com.google.javascript.jscomp.util.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private Node rootNode;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.ALIASES);
    compiler = new Compiler();
    compiler.initialize(compilerOptions, null, null);

    rootNode = Node.parse("goog.scope(function() {goog.dom.createElement(goog.dom.TagName.DIV);});", null);
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler(compiler, null, preprocessorSymbolTable);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertTrue(preprocessorSymbolTable.getAliases().isEmpty());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node aliasDefinitionNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    Node aliasUsageNode = scopedAliases.getAliasUsages().get(0);
    Node scopeCallNode = scopedAliases.getScopeCalls().get(0);

    assertEquals("goog.dom.createElement", NodeUtil.getFunctionName(aliasDefinitionNode));
    assertEquals("goog.dom.TagName.DIV", NodeUtil.getIdentifier(aliasUsageNode.getFirstChild()));
    assertEquals("goog.scope", NodeUtil.getIdentifier(scopeCallNode.getFirstChild().getFirstChild()));

    assertEquals("goog.dom.createElement", transformationHandler.getTransformedName(aliasDefinitionNode));
    assertNull(transformationHandler.getTransformedName(aliasUsageNode));

    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(new ScopedAliases.Traversal() {});
    nodeTraversal.traverse(compiler, rootNode, null);

    assertFalse(nodeTraversal.getAliasDefinitionsInOrder().isEmpty());
    assertFalse(nodeTraversal.getAliasUsages().isEmpty());
    assertFalse(nodeTraversal.getScopeCalls().isEmpty());
  }

  @Test
  public void testImproperUsageOfGoogScope() {
    Node rootNode = Node.parse("goog.scope(function() {goog.scope(function() {});});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnosticType.getName());
  }

  @Test
  public void testBadParametersToGoogScope() {
    Node rootNode = Node.parse("goog.scope(function(x) {});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnosticType.getName());
  }

  @Test
  public void testGoogScopeReferencesThis() {
    Node rootNode = Node.parse("goog.scope(function() {this.x = 42;});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnosticType.getName());
  }

  @Test
  public void testGoogScopeUsesReturn() {
    Node rootNode = Node.parse("goog.scope(function() {return 42;});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnosticType.getName());
  }

  @Test
  public void testGoogScopeUsesThrow() {
    Node rootNode = Node.parse("goog.scope(function() {throw new Error();});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnosticType.getName());
  }

  @Test
  public void testAliasRedefined() {
    Node rootNode = Node.parse("goog.scope(function() {" +
        "var x = goog.x;" +
        "var x = 42;" +
        "});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnosticType.getName());
  }

  @Test
  public void testNonAliasLocalVariable() {
    Node rootNode = Node.parse("goog.scope(function() {" +
        "var x = 42;" +
        "goog.x = 0;" +
        "});", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType diagnosticType = scopedAliases.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnosticType.getName());
  }
}