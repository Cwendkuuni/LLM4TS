package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = new CompilerOptions(); // Initialize CompilerOptions here
    rootNode = Node.newBlock();
  }

  @Test
  public void testAddMarkers() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(runtimeTypeCheck);

    Node functionNode = NodeUtil.newFunctionNode(
        "MyFunction",
        ImmutableList.<Node>of(),
        new Node(Token.VOID, new Node(Token.EXPR_RESULT, Node.newString(""))));

    nodeTraversal.traverse(RuntimeTypeCheck.AddMarkers.class, functionNode);

    assertTrue(NodeUtil.hasProperty(functionNode, "prototype", "instance_of__MyFunction"));

    ObjectType interfaceType = new ObjectType("MyInterface");
    functionNode.setJSType(new FunctionType(interfaceType, ImmutableList.of()));

    nodeTraversal.traverse(RuntimeTypeCheck.AddMarkers.class, functionNode);

    assertTrue(NodeUtil.hasProperty(functionNode, "prototype", "implements__MyInterface"));
  }

  @Test
  public void testAddChecks() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(runtimeTypeCheck);

    FunctionType functionType = new FunctionType(
        new ObjectType("MyClass"),
        ImmutableList.<Node>of());

    Node functionNode = NodeUtil.newFunctionNode(
        "testFunction",
        ImmutableList.<Node>of(),
        functionType.getReturnType());

    rootNode.addChild(functionNode);

    NodeTraversal.traverse(runtimeTypeCheck, rootNode);

    Node paramNode = NodeUtil.newNameNode("param");

    functionNode.addChild(new Node(Token.VAR, paramNode, Node.newNull()));

    Node checkNode = NodeUtil.newNameNode("check");

    functionNode.addChild(new Node(Token.ASSIGN, paramNode, checkNode));

    Node checkTypeCallNode = runtimeTypeCheck.AddChecks.createCheckTypeCallNode(
        new ObjectType("String"), checkNode);

    functionNode.replaceChild(checkNode, checkTypeCallNode);

    assertTrue(functionNode.getChild(functionNode.getChildCount() - 1)
            instanceof NodeTraversal.AbstractCallCallback);

    assertTrue(functionNode.getChild(functionNode.getChildCount() - 2)
            instanceof Node.NewCallExpression);

    Node valueCheckerCallNode = (Node) functionNode.getChild(functionNode.getChildCount() - 3);
    assertTrue(valueCheckerCallNode.getFirstChild().getType() == Token.STRING);
    assertTrue(valueCheckerCallNode.getFirstChild().getStringValue().equals("string"));
  }

  @Test(expected = AssertionError.class)
  public void testAddChecksForUnknownType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(runtimeTypeCheck);

    FunctionType functionType = new FunctionType(
        new ObjectType("MyClass"),
        ImmutableList.<Node>of());

    Node functionNode = NodeUtil.newFunctionNode(
        "testFunction",
        ImmutableList.<Node>of(),
        functionType.getReturnType());

    rootNode.addChild(functionNode);

    Node paramNode = NodeUtil.newNameNode("param");

    functionNode.addChild(new Node(Token.VAR, paramNode, Node.newNull()));

    Node checkNode = NodeUtil.newNameNode("check");

    functionNode.addChild(new Node(Token.ASSIGN, paramNode, checkNode));

    Node checkTypeCallNode = runtimeTypeCheck.AddChecks.createCheckTypeCallNode(
        new ObjectType("UnknownType"), checkNode);

    functionNode.replaceChild(checkNode, checkTypeCallNode);

    NodeTraversal.traverse(runtimeTypeCheck, rootNode);

    assertFalse(functionNode.getChild(functionNode.getChildCount() - 1)
            instanceof NodeTraversal.AbstractCallCallback);
  }
}