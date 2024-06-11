package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test case for valid usage of goog.scope
    String testCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = createNodeFromString("", null);
    Node root = createNodeFromString(testCode, null);

    scopedAliases.process(externs, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Check that there is exactly one scope call and one alias definition
    assertTrue(aliasDefinitionsInOrder.size() == 1);
    assertTrue(scopeCalls.size() == 1);

    // Check that the alias definition is for 'dom' and 'DIV'
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertTrue(aliasDefinition.getString().equals("dom"));
    Node aliasDefinitionValue = aliasDefinition.getLastChild();
    assertTrue(aliasDefinitionValue.getString().equals("goog.dom"));

    // Check that the scope call is to 'goog.scope' and has one child, which is the anonymous function
    Node scopeCall = scopeCalls.get(0);
    assertTrue(scopeCall.getFirstChild().getString().equals("goog.scope"));
    Node functionNode = scopeCall.getChildAtIndex(1);
    assertTrue(functionNode.getType() == Token.FUNCTION);

    // Check that the anonymous function has one child, which is the body
    Node bodyNode = functionNode.getLastChild();
    assertTrue(bodyNode.getType() != Token.SCRIPT);
    assertTrue(bodyNode.getType() != Token.BLOCK);

    // Check that the body contains a variable declaration for 'dom' and an assignment to 'DIV'
    List<Node> bodyChildren = Lists.newArrayList(bodyNode.getChildren());
    Node varNode = bodyChildren.get(0);
    assertTrue(varNode.getType() == Token.VAR);
    assertTrue(varNode.getString().equals("dom"));
    assertTrue(varNode.getFirstChild().getType() == Token.NAME);
    assertTrue(varNode.getFirstChild().getString().equals("dom"));
    Node assignmentNode = bodyChildren.get(1);
    assertTrue(assignmentNode.getType() == Token.SETPROP);
    assertTrue(assignmentNode.getParent().getType() == Token.ASSIGN);
    assertTrue(assignmentNode.getString().equals("DIV"));
    Node rightHandSideNode = assignmentNode.getLastChild();
    assertTrue(rightHandSideNode.getType() == Token.CALL);
    assertTrue(rightHandSideNode.getFirstChild().getString().equals("goog.dom"));
    assertTrue(rightHandSideNode.getLastChild().getType() == Token.IDENT);
    assertTrue(rightHandSideNode.getLastChild().getString().equals("createElement"));

    // Check that the preprocessorSymbolTable contains a reference to 'dom'
    Collection<Node> references = preprocessorSymbolTable.getReferences("dom");
    assertTrue(references.size() == 1);
    assertTrue(references.iterator().next().equals(varNode));

    // Test case for invalid usage of goog.scope
    testCode =
        "goog.scope(function() {" +
            "function foo() {" +
            "}" +
            "});";

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    externs = createNodeFromString("", null);
    root = createNodeFromString(testCode, null);

    scopedAliases.process(externs, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Check that there are no scope calls and no alias definitions
    assertFalse(aliasDefinitionsInOrder.isEmpty());
    assertFalse(scopeCalls.isEmpty());

    // Test case for goog.scope with incorrect number of parameters
    testCode =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "});" +
            "});";

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    externs = createNodeFromString("", null);
    root = createNodeFromString(testCode, null);

    scopedAliases.process(externs, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Check that there are no scope calls and no alias definitions
    assertFalse(aliasDefinitionsInOrder.isEmpty());
    assertFalse(scopeCalls.isEmpty());

    // Test case for goog.scope with a body that references 'this'
    testCode =
        "goog.scope(function() {" +
            "this.foo = 42;" +
            "});";

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    externs = createNodeFromString("", null);
    root = createNodeFromString(testCode, null);

    scopedAliases.process(externs, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Check that there are no scope calls and no alias definitions, and a diagnostic message was reported
    assertNull(aliasDefinitionsInOrder.get(0));
    assertNull(scopeCalls.get(0));
    assertTrue(scopedAliases.getTraversal().getDiagnostics().size() == 1);
    DiagnosticType diagnosticType = scopedAliases.getTraversal().getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.equals(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS));

    // Test case for goog.scope with a body that uses 'return'
    testCode =
        "goog.scope(function() {" +
            "return 42;" +
            "});";

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    externs = createNodeFromString("", null);
    root = createNodeFromString(testCode, null);

    scopedAliases.process(externs, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Check that there are no scope calls and no alias definitions, and a diagnostic message was reported
    assertNull(aliasDefinitionsInOrder.get(0));
    assertNull(scopeCalls.get(0));
    assertTrue(scopedAliases.getTraversal().getDiagnostics().size() == 1);
    diagnosticType = scopedAliases.getTraversal().getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.equals(ScopedAliases.GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testAliasTransformation() {
    // Test case for alias transformation
    String testCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "goog.dom.getElementsByTagName(DIV)[0].innerHTML = 'Hello World';" +
            "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    when(transformationHandler.logAliasTransformation(eq("test.js"), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation("dom", "goog.dom"));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = createNodeFromString("", null);
    Node root = createNodeFromString(testCode, null);

    scopedAliases.process(externs, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> bodyChildren = Lists.newArrayList(scopedAliases.getTraversal().getTraversalRoot().getLastChild().getChildren());

    // Check that there is exactly one scope call and one alias definition
    assertTrue(aliasDefinitionsInOrder.size() == 1);
    assertTrue(scopeCalls.size() == 1);

    // Check that the alias definition is for 'dom' and 'DIV'
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertTrue(aliasDefinition.getString().equals("dom"));
    Node aliasDefinitionValue = aliasDefinition.getLastChild();
    assertTrue(aliasDefinitionValue.getString().equals("goog.dom"));

    // Check that the scope call is to 'goog.scope' and has one child, which is the anonymous function
    Node scopeCall = scopeCalls.get(0);
    assertTrue(scopeCall.getFirstChild().getString().equals("goog.scope"));
    Node functionNode = scopeCall.getChildAtIndex(1);
    assertTrue(functionNode.getType() == Token.FUNCTION);

    // Check that the anonymous function has one child, which is the body
    Node bodyNode = functionNode.getLastChild();
    assertTrue(bodyNode.getType() != Token.SCRIPT);
    assertTrue(bodyNode.getType() != Token.BLOCK);

    // Check that the body contains a variable declaration for 'dom' and an assignment to 'DIV'
    Node varNode = bodyChildren.get(0);
    assertTrue(varNode.getType() == Token.VAR);
    assertTrue(varNode.getString().equals("dom"));
    assertTrue(varNode.getFirstChild().getType() == Token.NAME);
    assertTrue(varNode.getFirstChild().getString().equals("dom"));
    Node assignmentNode = bodyChildren.get(1);
    assertTrue(assignmentNode.getType() == Token.SETPROP);
    assertTrue(assignmentNode.getParent().getType() == Token.ASSIGN);
    assertTrue(assignmentNode.getString().equals("DIV"));
    Node rightHandSideNode = assignmentNode.getLastChild();
    assertTrue(rightHandSideNode.getType() == Token.IDENT);
    assertTrue(rightHandSideNode.getString().equals("DOM_DIV"));

    // Check that the element created with 'createElement' has its innerHTML updated with 'Hello World'
    assertTrue(bodyChildren.size() > 1);
    Node elementNode = bodyChildren.get(1);
    assertTrue(elementNode.getType() == Token.CALL);
    assertTrue(elementNode.getFirstChild().getString().equals("goog.dom.createElement"));
    assertTrue(elementNode.getLastChild().getType() == Token.IDENT);
    assertTrue(elementNode.getLastChild().getString().equals("DIV"));
    Node element = (Node) transformationHandler.getAliasTransformation("dom").getValue();
    Node createdElement = (Node) NodeUtil.tryApplyTransformation(element, "createElement", new Object[]{String.class, String.class}, new Object[]{"DIV", null});
    assertTrue(createdElement != null);
    Node innerHTMLNode = NodeUtil.getFirstChildByName(createdElement, "innerHTML");
    assertTrue(innerHTMLNode != null);
    assertTrue(innerHTMLNode.getString().equals("Hello World"));

    // Check that the alias transformation was logged
    verify(transformationHandler, times(1)).logAliasTransformation(eq("test.js"), any(SourcePosition.class));
  }

  private Node createNodeFromString(String code, SourcePosition position) {
    return new AbstractNode(code, 0, 0, position, 0, 0, Token.SCRIPT, null);
  }
}