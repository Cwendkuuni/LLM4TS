package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private NodeTraversal traversal;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    traversal = new NodeTraversal(nodeFactory, null, null, null);
    checker = new CheckGlobalThis(compiler, CheckLevel.MANDATORY);
  }

  @Test
  public void testShouldTraverseGlobalContexts() {
    Node root = new Node(Token.PROGRAM, new Node[0]);
    Node function1 = new Node(Token.FUNCTION, new Node[0], null);
    Node function2 = new Node(Token.FUNCTION, new Node[0], null);
    Node function3 = new Node(Token.FUNCTION, new Node[0], null);
    Node assign1 = new Node(Token.ASSIGN, new Node[] {function1}, root);
    Node assign2 = new Node(Token.ASSIGN, new Node[] {function2}, root);
    Node assign3 = new Node(Token.ASSIGN, new Node[] {function3}, root);

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function1);
    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function2);
    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function3);

    checker.shouldTraverse(traversal, function1, assign1);
    checker.shouldTraverse(traversal, function2, assign2);
    checker.shouldTraverse(traversal, function3, assign3);
  }

  @Test
  public void testShouldNotTraverseConstructorFunctions() {
    Node constructor = new Node(Token.FUNCTION, new Node[0], null);
    constructor.setJSDocInfo(new JSDocInfo(null, null, true));

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(constructor);

    checker.shouldTraverse(traversal, constructor, null);
    assertThat(checker.shouldTraverse(traversal, constructor, constructor)).isFalse();
  }

  @Test
  public void testShouldNotTraverseAnnotatedFunctions() {
    Node function = new Node(Token.FUNCTION, new Node[0], null);
    function.setJSDocInfo(new JSDocInfo(null, "@this", null));

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function);

    checker.shouldTraverse(traversal, function, null);
    assertThat(checker.shouldTraverse(traversal, function, function)).isFalse();
  }

  @Test
  public void testShouldNotTraverseAnnotatedFunctionsWithPrototypeAccess() {
    Node function = new Node(Token.FUNCTION, new Node[0], null);
    Node prototypeAccess = new Node(Token.GETPROP, new Node[] {function}, null);
    prototypeAccess.setString("prototype");

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function);

    checker.shouldTraverse(traversal, function, prototypeAccess);
    assertThat(checker.shouldTraverse(traversal, function, function)).isFalse();
  }

  @Test
  public void testShouldNotTraverseAnnotatedFunctionsWithPrototypeAccessDeep() {
    Node function = new Node(Token.FUNCTION, new Node[0], null);
    Node prototypeAccess = new Node(Token.GETPROP, new Node[] {function}, null);
    prototypeAccess.setString("prototype");
    Node getProperty = new Node(Token.GETPROP, new Node[] {prototypeAccess}, null);
    getProperty.setString("deepProperty");

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function);

    checker.shouldTraverse(traversal, function, getProperty);
    assertThat(checker.shouldTraverse(traversal, function, function)).isFalse();
  }

  @Test
  public void testShouldNotTraversePrototypeFunctions() {
    Node function = new Node(Token.FUNCTION, new Node[0], null);
    Node prototype = new Node(Token.NAME, new String[] {"prototype"}, null);
    Node functionPrototype = new Node(Token.GETPROP, new Node[] {prototype}, null);
    Node prototypeFunction = new Node(Token.FUNCTION, new Node[0], functionPrototype);

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function);
    when(nodeFactory.newName(null, "prototype")).thenReturn(prototype);
    when(nodeFactory.newGetProp(null, prototype, null, null)).thenReturn(functionPrototype);

    checker.shouldTraverse(traversal, functionPrototype, prototypeFunction);
    checker.shouldTraverse(traversal, functionPrototype, function);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node root = new Node(Token.PROGRAM, new Node[0]);
    Node function = new Node(Token.FUNCTION, new Node[0], null);
    Node assign = new Node(Token.ASSIGN, new Node[] {function}, root);
    Node thisKeyword = new Node(Token.THIS, new Node[0], null);

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function);

    checker.visit(traversal, thisKeyword, assign);

    assertThat(compiler.getDiagnostics()).hasSize(1);
    assertThat(compiler.getDiagnostics().get(0).getCode()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPrototypeAccess() {
    Node root = new Node(Token.PROGRAM, new Node[0]);
    Node functionPrototype = new Node(Token.GETPROP, new Node[] {new Node(Token.NAME, new String[] {"prototype"}, null)}, null);
    Node function = new Node(Token.FUNCTION, new Node[0], functionPrototype);
    Node assign = new Node(Token.ASSIGN, new Node[] {function}, root);
    Node thisKeyword = new Node(Token.THIS, new Node[0], null);

    when(nodeFactory.newName(null, "prototype")).thenReturn(new Node(Token.NAME, new String[] {"prototype"}, null));

    checker.visit(traversal, thisKeyword, assign);

    assertThat(compiler.getDiagnostics()).hasSize(1);
    assertThat(compiler.getDiagnostics().get(0).getCode()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node root = new Node(Token.PROGRAM, new Node[0]);
    Node function = new Node(Token.FUNCTION, new Node[0], null);
    Node assign = new Node(Token.ASSIGN, new Node[0], null);
    Node thisKeyword = new Node(Token.THIS, new Node[0], null);

    when(nodeFactory.newFunctionNoArgs(null)).thenReturn(function);

    checker.visit(traversal, thisKeyword, assign);

    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testShouldNotReportThisOnFunctionPrototype() {
    Node root = new Node(Token.PROGRAM, new Node[0]);
    Node functionPrototype = new Node(Token.GETPROP, new Node[] {new Node(Token.NAME, new String[] {"prototype"}, null)}, null);
    Node function = new Node(Token.FUNCTION, new Node[0], functionPrototype);
    Node thisKeyword = new Node(Token.THIS, new Node[0], null);

    when(nodeFactory.newName(null, "prototype")).thenReturn(new Node(Token.NAME, new String[] {"prototype"}, null));

    checker.visit(traversal, thisKeyword, function);

    assertThat(compiler.getDiagnostics()).isEmpty();
  }
}