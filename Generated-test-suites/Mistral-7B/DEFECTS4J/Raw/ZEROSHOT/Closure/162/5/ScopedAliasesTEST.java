package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTableMock;
  private AliasTransformationHandler transformationHandlerMock;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    preprocessorSymbolTableMock = mock(PreprocessorSymbolTable.class);
    transformationHandlerMock = mock(AliasTransformationHandler.class);
    compiler = mock(AbstractCompiler.class);
    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTableMock,
        transformationHandlerMock);
  }

  @Test
  public void testProcess_NoGoogScopeCalls_DoesNotApplyAliases() {
    Node root = Node.makeString("var x = 123;");
    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());
  }

  @Test
  public void testProcess_ValidGoogScopeCalls_AppliesAliases() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});");

    scopedAliases.process(null, root);

    List<Node> expectedAliasDefinitions = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    assertEquals(expectedAliasDefinitions.size(),
        scopedAliases.getAliasDefinitionsInOrder().size());

    List<Node> expectedAliasUsages = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    List<Node> actualAliasUsages = new ArrayList<>(
        scopedAliases.getAliasUsages());

    for (int i = 0; i < expectedAliasUsages.size(); ++i) {
      assertEquals(expectedAliasUsages.get(i), actualAliasUsages.get(i));
    }

    // Only the goog.scope call was modified.
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node expectedScopeCall = Node.makeCall(
        Node.makeName("goog.scope"),
        Node.makeFunction(
            Node.makeBlock(
                Collections.singletonList(
                    Node.makeVar(
                        Node.makeName("dom"),
                        Node.makeName("goog.dom"))
                ),
                Node.makeStatement(
                    Node.makeCall(
                        Node.makeName("goog.dom.createElement"),
                        Node.makeName("DIV"))
                )
            )
        )
    );

    assertEquals(expectedScopeCall, scopedAliases.getScopeCalls().get(0));
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_DoesNotApplyAliases() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var x = 123;\n" +
        "});");

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithNonFunctionParam_Error() {
    Node root = Node.makeString(
        "goog.scope(123);");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // A diagnostic should have been reported.
    assertEquals(1, scopedAliases.getDiagnostics().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithReturnStatement_Error() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  return 123;\n" +
        "});");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // A diagnostic should have been reported.
    assertEquals(1, scopedAliases.getDiagnostics().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithThrowStatement_Error() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  throw new Error();\n" +
        "});");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // A diagnostic should have been reported.
    assertEquals(1, scopedAliases.getDiagnostics().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithAliasRedefinition_Error() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var a = 123;\n" +
        "  var a = goog.dom.createElement('DIV');\n" +
        "});");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // A diagnostic should have been reported.
    assertEquals(1, scopedAliases.getDiagnostics().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithNonAliasLocal_Error() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var x = 123;\n" +
        "});");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // A diagnostic should have been reported.
    assertEquals(1, scopedAliases.getDiagnostics().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithAliasDefinitionWithoutInitialValue_Error() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var a;\n" +
        "});");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(Node.class),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    // No aliases should have been applied.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // A diagnostic should have been reported.
    assertEquals(1, scopedAliases.getDiagnostics().size());
  }

  @Test
  public void testProcess_GoogScopeCallWithAliasDefinitionWithInitialValue_AppliesAlias() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "});");

    scopedAliases.process(null, root);

    List<Node> expectedAliasDefinitions = Arrays.asList(
        Node.makeName("dom")
    );

    assertEquals(expectedAliasDefinitions.size(),
        scopedAliases.getAliasDefinitionsInOrder().size());

    List<Node> expectedAliasUsages = Arrays.asList(
        Node.makeName("dom")
    );

    List<Node> actualAliasUsages = new ArrayList<>(
        scopedAliases.getAliasUsages());

    for (int i = 0; i < expectedAliasUsages.size(); ++i) {
      assertEquals(expectedAliasUsages.get(i), actualAliasUsages.get(i));
    }
  }

  @Test
  public void testProcess_GoogScopeCallWithMultipleAliases_AppliesAliases() {
    Node root = Node.makeString(
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var foo = 'bar';\n" +
        "});");

    scopedAliases.process(null, root);

    List<Node> expectedAliasDefinitions = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    assertEquals(expectedAliasDefinitions.size(),
        scopedAliases.getAliasDefinitionsInOrder().size());

    List<Node> expectedAliasUsages = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    List<Node> actualAliasUsages = new ArrayList<>(
        scopedAliases.getAliasUsages());

    for (int i = 0; i < expectedAliasUsages.size(); ++i) {
      assertEquals(expectedAliasUsages.get(i), actualAliasUsages.get(i));
    }
  }

  @Test
  public void testProcess_HotSwap_GoogScopeCallWithMultipleAliases_AppliesAliases() {
    Node oldRoot = Node.makeString(
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var foo = 'bar';\n" +
        "});");

    Node newRoot = Node.makeString(
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = goog.dom.createElement;\n" +
        "  var foo = 'qux';\n" +
        "});");

    scopedAliases.process(null, oldRoot);

    List<Node> expectedOldAliasDefinitions = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    assertEquals(expectedOldAliasDefinitions.size(),
        scopedAliases.getAliasDefinitionsInOrder().size());

    List<Node> expectedOldAliasUsages = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    List<Node> actualOldAliasUsages = new ArrayList<>(
        scopedAliases.getAliasUsages());

    List<Node> expectedNewAliasDefinitions = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    for (int i = 0; i < expectedOldAliasUsages.size(); ++i) {
      assertEquals(expectedOldAliasUsages.get(i), actualOldAliasUsages.get(i));
    }

    scopedAliases.hotSwapScript(newRoot, oldRoot);

    List<Node> expectedNewAliasUsages = Arrays.asList(
        Node.makeName("dom"),
        Node.makeName("DIV")
    );

    List<Node> actualNewAliasUsages = new ArrayList<>(
        scopedAliases.getAliasUsages());

    for (int i = 0; i < expectedNewAliasUsages.size(); ++i) {
      assertEquals(expectedNewAliasUsages.get(i), actualNewAliasUsages.get(i));
    }

    // The new alias definitions should replace the old ones.
    assertEquals(expectedNewAliasDefinitions.size(),
        scopedAliases.getAliasDefinitionsInOrder().size());

    for (int i = 0; i < expectedNewAliasDefinitions.size(); ++i) {
      assertEquals(expectedNewAliasDefinitions.get(i),
          scopedAliases.getAliasDefinitionsInOrder().get(i));
    }
  }

  @Test
  public void testProcess_GoogScopeCallWithJSDocInfo_AliasesAreNotChanged() {
    Node root = Node.makeString(
        "/**\n" +
        " * @namespace goog.dom\n" +
        " */\n" +
        "goog.scope(function() {\n" +
        "  var tagName = goog.dom.TagName;\n" +
        "});");

    scopedAliases.process(null, root);

    List<Node> expectedAliasDefinitions = Arrays.asList(
        Node.makeName("goog.dom"),
        Node.makeName("TagName")
    );

    assertEquals(expectedAliasDefinitions.size(),
        scopedAliases.getAliasDefinitionsInOrder().size());

    List<Node> expectedAliasUsages = Arrays.asList(
        Node.makeName("goog.dom"),
        Node.makeName("TagName")
    );

    List<Node> actualAliasUsages = new ArrayList<>(
        scopedAliases.getAliasUsages());

    for (int i = 0; i < expectedAliasUsages.size(); ++i) {
      assertEquals(expectedAliasUsages.get(i), actualAliasUsages.get(i));
    }

    JSDocInfo expectedJSDocInfo = Node.makeJSDocInfo(
        Arrays.asList(
            Node.makeTag("namespace", "goog.dom")
        ),
        null
    );

    JSDocInfo actualJSDocInfo = scopedAliases.getScopeCalls().get(0)
        .getFirstChild()
        .getJSDocInfo();

    assertEquals(expectedJSDocInfo, actualJSDocInfo);
  }
}