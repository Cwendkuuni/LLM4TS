package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.NodeType.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Warning;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {

  private static AbstractCompiler createCompiler() {
    return mock(AbstractCompiler.class);
  }

  private static CheckGlobalThis createCheckGlobalThis(AbstractCompiler compiler, CheckLevel level) {
    return spy(new CheckGlobalThis(compiler, level));
  }

  private static Node createNode(int type, int line, int column) {
    return new Node(type, line, column);
  }

  private static Node createFunctionNode() {
    return createNode(TOKEN_FUNCTION, 1, 1);
  }

  private static Node createGetPropNode(Node parent, String name) {
    Node node = createNode(TOKEN_GETPROP, 1, 1);
    node.setFirstChild(parent);
    node.setLastChild(createNode(TOKEN_IDENT, 1, 1));
    node.getLastChild().setString(name);
    return node;
  }

  private static Node createAssignNode(Node left, Node right) {
    Node node = createNode(TOKEN_ASSIGN, 1, 1);
    node.setFirstChild(left);
    node.setLastChild(right);
    return node;
  }

  private static Node createVariableNode(Node parent, String name) {
    Node node = createNode(TOKEN_VAR, 1, 1);
    node.setFirstChild(createNode(TOKEN_NAME, 1, 1));
    node.getFirstChild().setString(name);
    parent.addChild(node);
    node = createNode(TOKEN_ASSIGN, 1, 1);
    node.setFirstChild(node.getFirstChild());
    node.setLastChild(createNode(TOKEN_NULL, 1, 1));
    parent.addChild(node);
    return node.getFirstChild();
  }

  @Test
  public void testShouldTraverseFunctionWithThis() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);
    Node functionNode = createFunctionNode();

    Node traverseResult = new NodeTraversal(functionNode, checkGlobalThis).traverse();
    assertTrue(NodeUtil.isIdenticalNode(functionNode, traverseResult));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutThis() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);
    Node functionNode = createFunctionNode();

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    parentNode.addChild(functionNode);

    Node traverseResult = new NodeTraversal(parentNode, checkGlobalThis).traverse();
    assertFalse(NodeUtil.isIdenticalNode(functionNode, traverseResult.getFirstChild()));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorJSDoc() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);
    Node functionNode = createFunctionNode();

    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    functionNode.setJSDocInfo(jsDocInfo);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    parentNode.addChild(functionNode);

    Node traverseResult = new NodeTraversal(parentNode, checkGlobalThis).traverse();
    assertFalse(NodeUtil.isIdenticalNode(functionNode, traverseResult.getFirstChild()));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);
    Node functionNode = createFunctionNode();

    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType("Self");
    functionNode.setJSDocInfo(jsDocInfo);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    parentNode.addChild(functionNode);

    Node traverseResult = new NodeTraversal(parentNode, checkGlobalThis).traverse();
    assertFalse(NodeUtil.isIdenticalNode(functionNode, traverseResult.getFirstChild()));
  }

  @Test
  public void testShouldTraverseAssignmentWithThis() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);
    Node left = createVariableNode(createNode(TOKEN_SCRIPT, 1, 1), "a");
    Node right = createNode(TOKEN_THIS, 1, 1);

    Node assignNode = createAssignNode(left, right);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    parentNode.addChild(assignNode);

    Node traverseResult = new NodeTraversal(parentNode, checkGlobalThis).traverse();
    assertTrue(NodeUtil.isIdenticalNode(assignNode, traverseResult.getFirstChild()));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    Node prototypeNode = createNode(TOKEN_GETPROP, 1, 1);
    prototypeNode.getLastChild().setString("prototype");
    prototypeNode.setParent(parentNode);
    parentNode.addChild(prototypeNode);

    Node left = createVariableNode(prototypeNode, "a");
    Node right = createNode(TOKEN_THIS, 1, 1);

    Node assignNode = createAssignNode(left, right);

    Node traverseResult = new NodeTraversal(parentNode, checkGlobalThis).traverse();
    assertFalse(NodeUtil.isIdenticalNode(assignNode, traverseResult.getFirstChild()));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypePropertySubProperty() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    Node prototypeNode = createNode(TOKEN_GETPROP, 1, 1);
    prototypeNode.getLastChild().setString("prototype");
    prototypeNode.setParent(parentNode);
    parentNode.addChild(prototypeNode);

    Node prototypePrototypeNode = createNode(TOKEN_GETPROP, 1, 1);
    prototypePrototypeNode.getLastChild().setString("prototype");
    prototypePrototypeNode.setParent(prototypeNode);
    prototypeNode.addChild(prototypePrototypeNode);

    Node left = createVariableNode(prototypePrototypeNode, "a");
    Node right = createNode(TOKEN_THIS, 1, 1);

    Node assignNode = createAssignNode(left, right);

    Node traverseResult = new NodeTraversal(parentNode, checkGlobalThis).traverse();
    assertFalse(NodeUtil.isIdenticalNode(assignNode, traverseResult.getFirstChild()));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);

    Node left = createNode(TOKEN_THIS, 1, 1);
    Node right = createNode(TOKEN_NULL, 1, 1);

    Node assignNode = createAssignNode(left, right);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    parentNode.addChild(assignNode);

    checkGlobalThis.visit(new NodeTraversal(parentNode, checkGlobalThis), assignNode, parentNode);

    assertTrue(compiler.reportedErrors().size() > 0);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);

    Node left = createNode(TOKEN_VAR, 1, 1);
    Node thisNode = createNode(TOKEN_THIS, 1, 1);
    Node right = thisNode;

    Node assignNode = createAssignNode(left, right);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    parentNode.addChild(assignNode);

    checkGlobalThis.visit(new NodeTraversal(parentNode, checkGlobalThis), assignNode, parentNode);

    assertTrue(compiler.reportedErrors().isEmpty());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);

    Node parentNode = createNode(TOKEN_SCRIPT, 1, 1);
    Node thisNode = createNode(TOKEN_THIS, 1, 1);
    Node getPropNode = createGetPropNode(thisNode, "someProperty");

    checkGlobalThis.visit(new NodeTraversal(parentNode, checkGlobalThis), getPropNode, parentNode);

    assertTrue(compiler.reportedErrors().size() > 0);
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessOnNonGlobalContext() {
    AbstractCompiler compiler = createCompiler();
    CheckGlobalThis checkGlobalThis = createCheckGlobalThis(compiler, Warning);

    Node parentNode = createNode(TOKEN_FUNCTION, 1, 1);
    Node thisNode = createNode(TOKEN_THIS, 1, 1);
    Node getPropNode = createGetPropNode(thisNode, "someProperty");

    checkGlobalThis.visit(new NodeTraversal(parentNode, checkGlobalThis), getPropNode, parentNode);

    assertTrue(compiler.reportedErrors().isEmpty());
  }
}