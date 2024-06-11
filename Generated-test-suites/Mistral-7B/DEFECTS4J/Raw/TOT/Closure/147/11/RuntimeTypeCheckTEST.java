package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.javascript.jscomp.AbstractCompiler.CompilationLevel.ADVANCED_OPTIMIZATIONS;
import static com.google.javascript.jscomp.RuntimeTypeCheck.ALPHA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testBasicTypeChecks() {
    String sourceCode = "function foo(x) {" +
        "if (checkType(x, [valueChecker('number'), numberChecker()])) {" +
        "  return x * 2;" +
        "} else {" +
        "  throw new Error('Type error');" +
        "}";

    AbstractCompiler compiler = new Compiler();
    compiler.setCompilerOptions(ImmutableList.of(ADVANCED_OPTIMIZATIONS));

    Node root = NodeUtil.parse(sourceCode);
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node checkTypeCall = findCheckTypeCall(root, 0);
    Node valueCheckerCall = findCheckerCall(checkTypeCall, "valueChecker");
    Node numberCheckerCall = findCheckerCall(checkTypeCall, "numberChecker");

    assertTrue(valueCheckerCall.isCall());
    assertTrue(valueCheckerCall.getFirstChild().isString());
    assertEquals("number", valueCheckerCall.getFirstChild().getString());

    assertTrue(numberCheckerCall.isCall());
    assertEquals(1, numberCheckerCall.getChildCount());

    Node numberNode = NodeUtil.newNumberLiteral(42);
    Node callResult = compileAndCallFunction(checkTypeCall, numberNode);
    assertEquals(84, callResult.getInt());

    Node stringNode = NodeUtil.newStringLiteral("hello");
    Node checkResult = compileAndCallFunction(checkTypeCall, stringNode);
    assertFalse(checkResult.isNumber());
  }

  @Test
  public void testFunctionTypeChecks() {
    String sourceCode = "function Bar(x) {" +
        "function foo(y) {" +
        "if (checkType(y, [valueChecker('number'), classChecker('Bar')])) {" +
        "  return x + y;" +
        "} else {" +
        "  throw new Error('Type error');" +
        "}";

    AbstractCompiler compiler = new Compiler();
    compiler.setCompilerOptions(ImmutableList.of(ADVANCED_OPTIMIZATIONS));

    Node root = NodeUtil.parse(sourceCode);
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node checkTypeCall = findCheckTypeCall(root, 1);
    Node valueCheckerCall = findCheckerCall(checkTypeCall, "valueChecker");
    Node classCheckerCall = findCheckerCall(checkTypeCall, "classChecker");

    assertTrue(valueCheckerCall.isCall());
    assertTrue(valueCheckerCall.getFirstChild().isString());
    assertEquals("number", valueCheckerCall.getFirstChild().getString());

    assertTrue(classCheckerCall.isCall());
    assertEquals(1, classCheckerCall.getChildCount());

    Node barConstructor = findConstructorDeclaration(root, "Bar");
    Node barInstance = newInstance(barConstructor);

    Node numberNode = NodeUtil.newNumberLiteral(42);
    Node callResult = compileAndCallFunction(checkTypeCall, numberNode);
    assertEquals(86, callResult.getInt());

    Node barInstanceNode = NodeUtil.newThisNode();
    Node barInstanceCallResult = compileAndCallFunction(checkTypeCall, barInstanceNode);
    assertEquals(barInstance, barInstanceCallResult.getFirstChild());
  }

  @Test
  public void testInterfaceTypeChecks() {
    String sourceCode = "function Foo() {}" +
        "Foo.prototype.bar = function() {" +
        "if (checkType(this, [interfaceChecker('Foo')])) {" +
        "  return this.foo();" +
        "} else {" +
        "  throw new Error('Type error');" +
        "}";

    AbstractCompiler compiler = new Compiler();
    compiler.setCompilerOptions(ImmutableList.of(ADVANCED_OPTIMIZATIONS));

    Node root = NodeUtil.parse(sourceCode);
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node checkTypeCall = findCheckTypeCall(root, 0);
    Node interfaceCheckerCall = findCheckerCall(checkTypeCall, "interfaceChecker");

    assertTrue(interfaceCheckerCall.isCall());
    assertEquals(1, interfaceCheckerCall.getChildCount());

    Node fooConstructor = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node fooInstance = newInstance(fooConstructor);

    Node fooCallResult = compileAndCallFunction(fooInstance, "bar");
    assertEquals(42, fooCallResult.getInt());

    Node nonFooInstance = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("String", -1, -1));
    Node checkResult = compileAndCallFunction(checkTypeCall, nonFooInstance);
    assertFalse(checkResult.isNumber());
  }

  private Node findCheckTypeCall(Node root, int index) {
    return findNodeByTypeAndIndex(root, Node.class, "CALL", index,
        node -> node.getFirstChild().isCall() &&
        node.getFirstChild().getFirstChild().isIdentifier() &&
        node.getFirstChild().getFirstChild().getString().equals("checkType"));
  }

  private Node findConstructorDeclaration(Node root, String name) {
    return findNodeByTypeAndIndex(root, Node.class, "EXPR_STMT", 0, node -> {
      Node function = node.getFirstChild();
      return function.getFirstChild().isIdentifier() &&
             function.getLastChild().isNewExpression() &&
             NodeUtil.getFunctionName(function.getLastChild().getFirstChild()).equals(name);
    });
  }

  private Node findCheckerCall(Node checkTypeCall, String checkerName) {
    return findNodeByTypeAndIndex(checkTypeCall, Node.class, "CALL", 1, node -> {
      Node firstChild = node.getFirstChild();
      return firstChild.isIdentifier() && firstChild.getString().equals(checkerName);
    });
  }

  private Node newInstance(Node constructor) {
    Node newExpression = new Node(Token.NEW, constructor);
    Node callExpression = new Node(Token.CALL, constructor, null);
    Node arrayLiteral = new Node(Token.ARRAYLIT);
    Node constructorArgument = NodeUtil.newUndefinedNode();
    arrayLiteral.addChildToBack(constructorArgument);
    newExpression.setFirstChild(callExpression);
    callExpression.setFirstChild(arrayLiteral);
    return newExpression;
  }

  private Node compileAndCallFunction(Node checkTypeCall, Node argument) {
    Node root = NodeUtil.newBlockNode(checkTypeCall);
    Node argumentNode = NodeUtil.newExpressionStatement(NodeUtil.newAssign(
            NodeUtil.newIdentifier("arg"), argument));
    root.addChildToBack(argumentNode);
    Node result = compiler.compile(root);
    Node callResult = compileAndCallFunction(result, checkTypeCall);
    return callResult;
  }

  private Node compileAndCallFunction(Node root, Node checkTypeCall) {
    Node function = NodeUtil.newFunctionNode("test", new Node(Token.VOID, null),
        new Node(Token.BLOCK, root));
    Node callExpression = new Node(Token.CALL, function, NodeUtil.newArrayNode());
    Node callStatement = new Node(Token.EXPR_STMT, callExpression);
    Node newScript = new Node(Token.SCRIPT, null, null, callStatement);
    AbstractCompiler compiler = new Compiler();
    Node result = compiler.compile(newScript);
    return findNodeByTypeAndIndex(result, Node.class, "CALL", 0, node -> {
      Node firstChild = node.getFirstChild();
      Node secondChild = firstChild.getFirstChild();
      return secondChild.isCall() &&
             secondChild.getFirstChild().isIdentifier() &&
             secondChild.getFirstChild().getString().equals("checkType") &&
             node.getLastChild().isIdentifier() &&
             node.getLastChild().getString().equals("test");
    });
  }

  private Node findNodeByTypeAndIndex(Node root, Node type, String nodeType, int index, Predicate<Node> predicate) {
    int count = 0;
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == type && nodeType.equals(currentNode.getNodeType()) && ++count == index) {
        if (predicate.test(currentNode)) {
          return currentNode;
        }
      }
      currentNode = currentNode.getNext();
    }
    return null;
  }

  private interface Predicate<T> {
    boolean test(T t);
  }
}