package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  private static final String LOG_FUNCTION = "println";

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testProcess_SimpleFunction() {
    String sourceCode = "function foo(x) { return x; }";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    Node root = NodeUtil.parseTree(sourceCode);

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkFunctionCall = findCheckFunctionCall(root, "foo", "x");
    assertEquals("x", checkFunctionCall.getFirstChild().toString());
  }

  @Test
  public void testProcess_FunctionWithReturnType() {
    String sourceCode = "function foo(x) { return x * 2; }";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    Node root = NodeUtil.parseTree(sourceCode);

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkFunctionCall = findCheckFunctionCall(root, "foo", "x");
    assertEquals("x", checkFunctionCall.getFirstChild().toString());

    Node returnCheckFunctionCall = findCheckFunctionCall(root, "foo", null);
    assertEquals(new FunctionType(new ImmutableList.Builder<JSType>()
            .add(JSType.INT)
            .build()),
        returnCheckFunctionCall.getLastChild().getLastChild().getJSType());
  }

  @Test
  public void testProcess_FunctionWithInterfaceImplementation() {
    String sourceCode = "interface MyInterface {" +
        "  method1(x: number): number;" +
        "}" +
        "class MyClass implements MyInterface {" +
        "  method1(x: number): number {" +
        "    return x * 2;" +
        "  }" +
        "}";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    Node root = NodeUtil.parseTree(sourceCode);

    runtimeTypeCheck.process(input.getExterns(), root);

    Node myClassConstructor = findConstructor(root, "MyClass");
    Node myClassPrototype = myClassConstructor.getNext();

    Node markers = findMarkers(myClassPrototype);
    assertEquals("implements__MyInterface", markers.getFirstChild().toString());
  }

  @Test
  public void testProcess_FunctionWithClassImplementation() {
    String sourceCode = "class MyClass {" +
        "  method1(x: number): number {" +
        "    return x * 2;" +
        "  }" +
        "}" +
        "class MySubClass extends MyClass {}";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    Node root = NodeUtil.parseTree(sourceCode);

    runtimeTypeCheck.process(input.getExterns(), root);

    Node myClassConstructor = findConstructor(root, "MyClass");
    Node myClassPrototype = myClassConstructor.getNext();

    Node markers = findMarkers(myClassPrototype);
    assertEquals("instance_of__MyClass", markers.getFirstChild().toString());

    Node mySubClassConstructor = findConstructor(root, "MySubClass");
    Node mySubClassPrototype = mySubClassConstructor.getNext();

    markers = findMarkers(mySubClassPrototype);
    assertEquals("instance_of__MyClass", markers.getFirstChild().toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_FunctionWithMissingConstructor() {
    String sourceCode = "class MyClass {" +
        "  method1(x: number): number {" +
        "    return x * 2;" +
        "  }" +
        "}";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    Node root = NodeUtil.parseTree(sourceCode);

    runtimeTypeCheck.process(input.getExterns(), root);

    // This should throw an exception because the constructor is missing.
    findMarkers(root.getLastChild());
  }

  @Test
  public void testProcess_FunctionWithExternedClass() {
    String sourceCode = "class MyClass {" +
        "  method1(x: number): number {" +
        "    return x * 2;" +
        "  }" +
        "}" +
        "//^T MyClass";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    Node root = NodeUtil.parseTree(sourceCode);

    runtimeTypeCheck.process(input.getExterns(), root);

    Node myClassConstructor = findConstructor(root, "MyClass");
    Node myClassPrototype = myClassConstructor.getNext();

    Node markers = findMarkers(myClassPrototype);
    assertEquals("externClassChecker(MyClass)", markers.getFirstChild().toString());
  }

  private Node findCheckFunctionCall(Node root, String functionName,
      @Nullable Node paramName) {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    NodeTraversal.TraverseCallback callback = new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunctionCall(n) && NodeUtil.getFunctionName(n).equals(functionName)) {
          if (paramName != null) {
            Node paramNode = findParameterNode(n, paramName);
            if (paramNode != null) {
              return;
            }
          }
          return;
        }
      }
    };
    nodeTraversal.traverse(root, callback);

    if (callback.getLastVisited() == null) {
      throw new RuntimeException("Check function call not found.");
    }

    return callback.getLastVisited();
  }

  private Node findConstructor(Node root, String constructorName) {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    NodeTraversal.TraverseCallback callback = new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n) && NodeUtil.isConstructor(n) &&
            NodeUtil.getFunctionName(n).equals(constructorName)) {
          return;
        }
      }
    };
    nodeTraversal.traverse(root, callback);

    if (callback.getLastVisited() == null) {
      throw new RuntimeException("Constructor not found.");
    }

    return callback.getLastVisited();
  }

  private Node findMarkers(Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.GETPROP &&
          child.getFirstChild().toString().startsWith("prototype") &&
          child.getLastChild().getType() == Token.STRING &&
          (child.getLastChild().getFirstChild().getType() == Token.STRING ||
          child.getLastChild().getFirstChild().getType() == Token.GETPROP &&
          child.getLastChild().getFirstChild().getLastChild().getType() == Token.IDENT &&
          child.getLastChild().getLastChild().getFirstChild().toString().startsWith("instance_"))) {
        return child;
      }
    }

    throw new RuntimeException("Markers not found.");
  }

  private Node findParameterNode(Node node, Node paramName) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.GETELEM &&
          child.getFirstChild().getType() == Token.IDENT &&
          child.getFirstChild().toString().equals(paramName.toString())) {
        return child.getNext();
      }
    }

    return null;
  }
}