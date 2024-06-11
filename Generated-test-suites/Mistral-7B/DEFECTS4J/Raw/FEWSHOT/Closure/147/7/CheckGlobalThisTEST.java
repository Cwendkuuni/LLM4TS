package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.NodeTraversal.CallbackFlag.SYNCHRONOUS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    checker = new Checker(compiler, new CheckGlobalThis(compiler, CHECK_LEVEL_WARNING));
    traversal = new NodeTraversal(compiler, checker, null, SYNCHRONOUS);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(root), ArgumentMatchers.eq(null));
    checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(root));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node root = new Node(Token.BLOCK);
    Node functionNode = new Node(Token.FUNCTION);
    checker.shouldNotTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(root));
  }

  @Test
  public void testShouldNotTraverseFunctionConstructor() {
    Node root = new Node(Token.SCRIPT);
    Node constructorNode = new Node(Token.FUNCTION);
    constructorNode.setJSDocInfo(new JSDocInfo(true, null, null, null, null, null, null, null, null));
    checker.shouldNotTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(constructorNode), ArgumentMatchers.eq(root));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node root = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, "this", null, null));
    checker.shouldNotTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(root));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node root = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, "override", null));
    checker.shouldNotTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(root));
  }

  @Test
  public void testReportGlobalThis() {
    Node root = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    Node thisNode = new Node(Token.THIS);
    Node assignmentNode = new Node(Token.ASSIGN);
    assignmentNode.setLeft(thisNode);
    functionNode.addChildToStatementList(assignmentNode);
    root.addChildToStatementList(functionNode);

    checker.visit(traversal, ArgumentMatchers.eq(root), ArgumentMatchers.eq(null));

    Node errorNode = root.getFirstError();
    assertEquals(DiagnosticType.WARNING, errorNode.getType());
    assertEquals("JSC_USED_GLOBAL_THIS", errorNode.getCode());
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Node root = new Node(Token.SCRIPT);
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setLeft(thisNode);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToStatementList(assignNode);
    root.addChildToStatementList(functionNode);

    checker.visit(traversal, ArgumentMatchers.eq(root), ArgumentMatchers.eq(null));

    Node errorNode = root.getFirstError();
    assertNull(errorNode);
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccess() {
    Node root = new Node(Token.SCRIPT);
    Node thisNode = new Node(Token.THIS);
    Node getNode = new Node(Token.GETPROP);
    getNode.setLeft(thisNode);
    getNode.setLastChild(new Node("prototype"));
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToStatementList(getNode);
    root.addChildToStatementList(functionNode);

    checker.visit(traversal, ArgumentMatchers.eq(root), ArgumentMatchers.eq(null));

    Node errorNode = root.getFirstError();
    assertNull(errorNode);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentInPrototype() {
    Node root = new Node(Token.SCRIPT);
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setLeft(thisNode);
    Node getNode = new Node(Token.GETPROP);
    getNode.setLeft(assignNode);
    getNode.setLastChild(new Node("prototype"));
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToStatementList(getNode);
    root.addChildToStatementList(functionNode);

    checker.visit(traversal, ArgumentMatchers.eq(root), ArgumentMatchers.eq(null));

    Node errorNode = root.getFirstError();
    assertNull(errorNode);
  }
}