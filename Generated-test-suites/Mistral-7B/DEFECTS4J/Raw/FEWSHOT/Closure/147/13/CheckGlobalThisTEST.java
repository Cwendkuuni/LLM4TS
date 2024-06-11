package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.Diagnostics.context;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Test cases for the CheckGlobalThis class.
 */
public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Callback checker;
  private NodeFactory nodeFactory;
  private Parser parser;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    parser = mock(Parser.class);
    when(compiler.getFactory()).thenReturn(nodeFactory);
    when(parser.parseCompilationUnit(context())).thenReturn(nodeFactory.parseTree(
        "function test() { this; }"));
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @org.junit.Test
  public void testShouldTraverseFunction() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node functionNode = root.getFirstChild();
    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructorFunction() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node constructorNode = root.getFirstChild();
    constructorNode.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag[] { new JSDocInfo.Tag("constructor") }));

    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node functionNode = root.getFirstChild();
    functionNode.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag[] { new JSDocInfo.Tag("@this", "MyType") }));

    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInNonGlobalContext() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node functionNode = root.getFirstChild();
    root.setNext(functionNode);

    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertFalse(checker.shouldTraverse(traversal, functionNode, root));
  }

  @org.junit.Test
  public void testShouldTraverseLeftSideOfAssignment() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node assignNode = nodeFactory.makeStatement(Token.ASSIGN, "a", null);
    assignNode.setFirstChild(nodeFactory.makeIdent("this"));
    root.appendChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertTrue(checker.shouldReportThis(traversal, assignNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node assignNode = nodeFactory.makeStatement(Token.ASSIGN, "a.prototype.x", null);
    assignNode.setFirstChild(nodeFactory.makeIdent("this"));
    root.appendChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertFalse(checker.shouldReportThis(traversal, assignNode.getFirstChild()));
  }

  @org.junit.Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node assignNode = nodeFactory.makeStatement(Token.ASSIGN, "a.prototype.x.y", null);
    assignNode.setFirstChild(nodeFactory.makeGetProp(null, "this", new Node[0]));
    root.appendChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(root, checker);

    assertFalse(checker.shouldReportThis(traversal, assignNode.getFirstChild()));
  }

  @org.junit.Test
  public void testShouldReportGlobalThis() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node thisNode = nodeFactory.makeIdent("this");
    NodeTraversal traversal = new NodeTraversal(root, checker);

    when(compiler.report(eq(traversal.makeError(thisNode, WARNING, GLOBAL_THIS)))).thenReturn(null);

    checker.visit(traversal, null, root);
    checker.visit(traversal, thisNode, root);

    verify(compiler, times(1)).report(eq(traversal.makeError(thisNode, WARNING, GLOBAL_THIS)));
  }

  @org.junit.Test
  public void testShouldNotReportThisInConstructor() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node constructorNode = root.getFirstChild();
    constructorNode.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag[] { new JSDocInfo.Tag("constructor") }));

    Node thisNode = nodeFactory.makeIdent("this");
    NodeTraversal traversal = new NodeTraversal(root, checker);

    checker.visit(traversal, thisNode, constructorNode);

    verifyNoMoreInteractions(compiler);
  }

  @org.junit.Test
  public void testShouldNotReportThisInFunctionWithThisAnnotation() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node functionNode = root.getFirstChild();
    functionNode.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag[] { new JSDocInfo.Tag("@this", "MyType") }));

    Node thisNode = nodeFactory.makeIdent("this");
    NodeTraversal traversal = new NodeTraversal(root, checker);

    checker.visit(traversal, thisNode, functionNode);

    verifyNoMoreInteractions(compiler);
  }

  @org.junit.Test
  public void testShouldNotReportThisInFunctionWithLogicalPlaceForThisAnnotation() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, new Node[0]);
    NodeTraversal traversal = new NodeTraversal(root, checker);

    checker.visit(traversal, null, functionNode);

    Node thisNode = nodeFactory.makeIdent("this");
    checker.visit(traversal, thisNode, functionNode);

    verifyNoMoreInteractions(compiler);
  }

  @org.junit.Test
  public void testShouldNotReportThisInVariableDeclaration() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node variableNode = nodeFactory.makeStatement(Token.VAR, "a", new Node[0]);
    Node thisNode = nodeFactory.makeIdent("this");
    variableNode.setFirstChild(thisNode);

    NodeTraversal traversal = new NodeTraversal(root, checker);

    checker.visit(traversal, thisNode, variableNode);

    verifyNoMoreInteractions(compiler);
  }

  @org.junit.Test
  public void testShouldNotReportThisInVariableDeclarationWithAssignment() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node variableNode = nodeFactory.makeStatement(Token.VAR, "a", new Node[0]);
    Node assignNode = nodeFactory.makeStatement(Token.ASSIGN, "a", null);
    assignNode.setFirstChild(nodeFactory.makeIdent("this"));
    variableNode.setFirstChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(root, checker);

    checker.visit(traversal, assignNode.getFirstChild(), assignNode);

    verifyNoMoreInteractions(compiler);
  }

  @org.junit.Test
  public void testShouldReportThisInPrototypeMethod() {
    NodeTree tree = new NodeTree(parser.parseCompilationUnit(context()));
    Node root = tree.getRootNode();

    Node constructorNode = root.getFirstChild();
    constructorNode.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag[] { new JSDocInfo.Tag("constructor") }));

    Node prototypeNode = nodeFactory.makeStatement(Token.PROP, "prototype", null);
    Node methodNode = new Node(Token.FUNCTION, "m", null, null, null, new Node[0]);
    methodNode.setFirstChild(nodeFactory.makeStatement(Token.THIS, "this", null));
    prototypeNode.setFirstChild(methodNode);
    constructorNode.setLastChild(prototypeNode);

    Node thisNode = nodeFactory.makeIdent("this");
    NodeTraversal traversal = new NodeTraversal(root, checker);

    when(compiler.report(eq(traversal.makeError(thisNode, WARNING, GLOBAL_THIS)))).thenReturn(null);

    checker.visit(traversal, thisNode, constructorNode);
    checker.visit(traversal, thisNode, methodNode);

    verify(compiler, times(1)).report(eq(traversal.makeError(thisNode, WARNING, GLOBAL_THIS)));
  }
}

class NodeTree {
  private Node root;

  NodeTree(Node node) {
    root = node;
  }

  Node getRootNode() {
    return root;
  }
}