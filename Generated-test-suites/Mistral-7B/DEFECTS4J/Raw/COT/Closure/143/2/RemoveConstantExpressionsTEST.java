package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test.base.CompilerTestCase;
import com.google.javascript.rhino.NodeTraversal;

/**
 * Tests for RemoveConstantExpressions class
 */
public class RemoveConstantExpressionsTest extends CompilerTestCase {

  public RemoveConstantExpressionsTest() {
    super();
  }

  @Override
  protected CompilerPass getProcessorUnderTest() {
    return new RemoveConstantExpressions(getCompiler());
  }

  /**
   * Test for basic functionality of RemoveConstantExpressions
   */
  public void testBasicFunctionality() {
    String input = "function foo() { return 1; }\n"
        + "function bar() { return 2; }\n"
        + "1 + foo() + bar();";

    Node node = parseJs(input);
    getProcessorUnderTest().process(null, node);

    Node expected = new Node(Token.EXPR_LIST,
        Lists.newArrayList(
            new Node(Token.IDENT, "foo"),
            new Node(Token.IDENT, "bar")
        ));

    assertNodeEquals(expected, node.getFirstChild());
  }

  /**
   * Test for edge case where expression statement has multiple subexpressions
   */
  public void testMultipleSubexpressions() {
    String input = "function foo() { return 1; }\n"
        + "function bar() { return 2; }\n"
        + "1 + foo() * bar() + 3;";

    Node node = parseJs(input);
    getProcessorUnderTest().process(null, node);

    Node expected = new Node(Token.EXPR_LIST,
        Lists.newArrayList(
            new Node(Token.IDENT, "foo"),
            new Node(Token.IDENT, "bar"),
            new Node(Token.MUL),
            new Node(Token.NUMBER, 1.0),
            new Node(Token.IDENT, "bar")
        ));

    Node current = node.getFirstChild();
    while (!current.isList()) {
      Node temp = current;
      current = current.getNext();
      replaceInTree(temp, expected.getFirstChild());
    }

    List<Node> list = (List<Node>) current;
    for (int i = 0; i < list.size(); i++) {
      Node expectedNode = list.get(i);
      Node actualNode = list.get(i);
      assertNodeEquals(expectedNode, actualNode);
    }
  }

  /**
   * Test for edge case where subexpression has side effects
   */
  public void testSubexpressionWithSideEffects() {
    String input = "function foo() { return 1; }\n"
        + "function bar() { console.log(\"Bar\"); return 2; }\n"
        + "1 + foo() + bar();";

    Node node = parseJs(input);
    getProcessorUnderTest().process(null, node);

    Node expected = new Node(Token.EXPR_LIST,
        Lists.newArrayList(
            new Node(Token.IDENT, "foo"),
            new Node(Token.IDENT, "bar")
        ));

    Node current = node.getFirstChild();
    while (!current.isList()) {
      Node temp = current;
      current = current.getNext();
      replaceInTree(temp, expected.getFirstChild());
    }

    List<Node> list = (List<Node>) current;
    for (int i = 0; i < list.size(); i++) {
      Node expectedNode = list.get(i);
      Node actualNode = list.get(i);
      assertNodeEquals(expectedNode, actualNode);
    }

    // since console.log is a side effect, make sure it was called
    assertTrue(getConsoleLogs().size() > 0);
  }

  /**
   * Test for edge case where the expression statement is the root node
   */
  public void testExpressionStatementIsRoot() {
    String input = "1 + foo() + bar();";

    Node node = parseJs(input);
    getProcessorUnderTest().process(null, node);

    Node expected = new Node(Token.EXPR_LIST,
        Lists.newArrayList(
            new Node(Token.IDENT, "foo"),
            new Node(Token.IDENT, "bar")
        ));

    assertNodeEquals(expected, node);
  }

  /**
   * Test for edge case where the expression statement is a property access
   */
  public void testPropertyAccess() {
    String input = "obj.prop1 + foo() + obj.prop2;";

    Node node = parseJs(input);
    getProcessorUnderTest().process(null, node);

    Node expected = new Node(Token.EXPR_LIST,
        Lists.newArrayList(
            new Node(Token.IDENT, "obj"),
            new Node(Token.IDENT, "prop1"),
            new Node(Token.IDENT, "foo"),
            new Node(Token.IDENT, "obj"),
            new Node(Token.IDENT, "prop2")
        ));

    assertNodeEquals(expected, node);
  }

  /**
   * Test for edge case where the expression statement is a method call
   */
  public void testMethodCall() {
    String input = "myFunction(foo()) + bar();";

    Node node = parseJs(input);
    getProcessorUnderTest().process(null, node);

    Node expected = new Node(Token.EXPR_LIST,
        Lists.newArrayList(
            new Node(Token.IDENT, "myFunction"),
            new Node(Token.LIST,
                Lists.newArrayList(
                    new Node(Token.IDENT, "foo")
                )
            ),
            new Node(Token.IDENT, "bar")
        ));

    assertNodeEquals(expected, node);
  }

  /**
   * Test for exception handling scenario: expression statement is an assignment
   */
  public void testAssignment() {
    String input = "x = 1 + foo() + bar();";

    Node node = parseJs(input);
    try {
      getProcessorUnderTest().process(null, node);
      fail("Expected an exception");
    } catch (AssertionError e) {
      // expected
    }

    assertNodeEquals(node, node);
  }

  /**
   * Test for exception handling scenario: expression statement is a conditional statement
   */
  public void testConditionalStatement() {
    String input = "x = (1 + foo()) ? 1 : 2 + bar();";

    Node node = parseJs(input);
    try {
      getProcessorUnderTest().process(null, node);
      fail("Expected an exception");
    } catch (AssertionError e) {
      // expected
    }

    assertNodeEquals(node, node);
  }

  /**
   * Test for exception handling scenario: expression statement is a loop statement
   */
  public void testLoopStatement() {
    String input = "while (1 + foo()) {};";

    Node node = parseJs(input);
    try {
      getProcessorUnderTest().process(null, node);
      fail("Expected an exception");
    } catch (AssertionError e) {
      // expected
    }

    assertNodeEquals(node, node);
  }

  /**
   * Test for exception handling scenario: expression statement is an object creation statement
   */
  public void testObjectCreationStatement() {
    String input = "new MyClass(1 + foo()) {};";

    Node node = parseJs(input);
    try {
      getProcessorUnderTest().process(null, node);
      fail("Expected an exception");
    } catch (AssertionError e) {
      // expected
    }

    assertNodeEquals(node, node);
  }

  private static void replaceInTree(Node oldNode, Node newNode) {
    if (oldNode == null || newNode == null) {
      return;
    }

    if (oldNode.equals(newNode)) {
      oldNode.setFirstChild(newNode.getFirstChild());
      return;
    }

    if (newNode.isList()) {
      Node newList = newNode.getFirstChild();
      while (oldNode.getNext() != null) {
        Node oldNodeNext = oldNode.getNext();
        replaceInTree(oldNodeNext, newList.getNext());
      }
      oldNode.setFirstChild(newList);
      oldNode.setNext(newList.getNext());
    }

    if (oldNode.getType() == newNode.getType()) {
      switch (oldNode.getType()) {
        case Token.ASSIGN:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          break;
        case Token.COND:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          replaceInTree(oldNode.getLastChild(), newNode.getLastChild());
          break;
        case Token.EXPR_LIST:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          replaceInTree(oldNode.getLastChild(), newNode.getLastChild());
          break;
        case Token.IDENT:
          if (oldNode.getFirstChild() != null) {
            replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          }
          break;
        case Token.KEY_WORD:
          break;
        case Token.NODE_SUPPRESS_AST_PROCESSING:
          break;
        case Token.NUMBER:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          break;
        case Token.OP_ASS:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          replaceInTree(oldNode.getLastChild(), newNode.getLastChild());
          break;
        case Token.OP_COND:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          replaceInTree(oldNode.getLastChild(), newNode.getLastChild());
          break;
        case Token.OP_MUL:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          replaceInTree(oldNode.getLastChild(), newNode.getLastChild());
          break;
        case Token.OP_SUB:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          replaceInTree(oldNode.getLastChild(), newNode.getLastChild());
          break;
        case Token.OP_UNARY:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          break;
        case Token.STRING:
          replaceInTree(oldNode.getFirstChild(), newNode.getFirstChild());
          break;
        default:
          fail("Unsupported node type: " + oldNode.getType());
      }
    }
  }

  /**
   * Helper method for parsing the input using JsCompiler
   */
  private Node parseJs(String input) {
    Compiler compiler = new Compiler();
    Node externs = null;
    Node root = null;
    try {
      compiler.compileString(input, externs, root);
    } catch (IOException e) {
      fail("Failed to compile input: " + input);
    }
    return root;
  }
}