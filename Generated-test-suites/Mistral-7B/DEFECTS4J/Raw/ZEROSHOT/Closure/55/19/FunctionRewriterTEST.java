package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.checkers.TypeChecker;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for FunctionRewriter.
 */
public class FunctionRewriterTest {
  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new TestCompiler(options);
    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunction() {
    String inputCode = "function Foo() {}";
    Node rootNode = NodeFactory.compile(inputCode);

    functionRewriter.process(compiler.getExterns(), rootNode);

    List<Node> helperFunctions = Arrays.asList(
        NodeFactory.fromJsonString(FunctionRewriter.EmptyFunctionReducer.HELPER_SOURCE)
    );
    TypeChecker checker = new TypeChecker(compiler, helperFunctions);
    checker.checkTypes(rootNode, null);

    Node expectedRootNode = NodeFactory.fromJsonString(
        "function Foo() {emptyFn();}"
    );
    Node actualRootNode = compiler.getRootNode();
    assertEquals(expectedRootNode, actualRootNode);
  }

  @org.junit.Test
  public void testIdentityFunction() {
    String inputCode = "function Foo(x) { return x; }";
    Node rootNode = NodeFactory.compile(inputCode);

    functionRewriter.process(compiler.getExterns(), rootNode);

    List<Node> helperFunctions = Arrays.asList(
        NodeFactory.fromJsonString(FunctionRewriter.IdentityReducer.HELPER_SOURCE)
    );
    TypeChecker checker = new TypeChecker(compiler, helperFunctions);
    checker.checkTypes(rootNode, null);

    Node expectedRootNode = NodeFactory.fromJsonString(
        "function Foo(x) {return this[" + FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME +
        "_name](" + "x" + ");}"
    );
    Node actualRootNode = compiler.getRootNode();
    assertEquals(expectedRootNode, actualRootNode);
  }

  @org.junit.Test
  public void testReturnConstantFunction() {
    String inputCode = "function Foo() { return 10; }";
    Node rootNode = NodeFactory.compile(inputCode);

    functionRewriter.process(compiler.getExterns(), rootNode);

    List<Node> helperFunctions = Arrays.asList(
        NodeFactory.fromJsonString(FunctionRewriter.ReturnConstantReducer.HELPER_SOURCE)
    );
    TypeChecker checker = new TypeChecker(compiler, helperFunctions);
    checker.checkTypes(rootNode, null);

    Node expectedRootNode = NodeFactory.fromJsonString(
        "function Foo() {" + FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME +
        "_value = 10;" +
        "return " + FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME + "_value;" +
        "}"
    );
    Node actualRootNode = compiler.getRootNode();
    assertEquals(expectedRootNode, actualRootNode);
  }

  @org.junit.Test
  public void testGetterFunction() {
    String inputCode = "function Foo() {" +
                       "  this.x_ = 10;" +
                       "  this.getX = function() { return this.x_; };" +
                       "}";
    Node rootNode = NodeFactory.compile(inputCode);

    functionRewriter.process(compiler.getExterns(), rootNode);

    List<Node> helperFunctions = Arrays.asList(
        NodeFactory.fromJsonString(FunctionRewriter.GetterReducer.HELPER_SOURCE),
        NodeFactory.fromJsonString(FunctionRewriter.EmptyFunctionReducer.HELPER_SOURCE)
    );
    TypeChecker checker = new TypeChecker(compiler, helperFunctions);
    checker.checkTypes(rootNode, null);

    Node expectedRootNode = NodeFactory.fromJsonString(
        "function Foo() {" +
        "  this.x_ = 10;" +
        "  this.getX = " + FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME + "_get;" +
        "}"
    );
    Node actualRootNode = compiler.getRootNode();
    assertEquals(expectedRootNode, actualRootNode);
  }

  @org.junit.Test
  public void testSetterFunction() {
    String inputCode = "function Foo() {" +
                       "  this.x_ = 10;" +
                       "  this.setX = function(value) {" +
                       "    this.x_ = value;" +
                       "  };" +
                       "}";
    Node rootNode = NodeFactory.compile(inputCode);

    functionRewriter.process(compiler.getExterns(), rootNode);

    List<Node> helperFunctions = Arrays.asList(
        NodeFactory.fromJsonString(FunctionRewriter.SetterReducer.HELPER_SOURCE),
        NodeFactory.fromJsonString(FunctionRewriter.EmptyFunctionReducer.HELPER_SOURCE)
    );
    TypeChecker checker = new TypeChecker(compiler, helperFunctions);
    checker.checkTypes(rootNode, null);

    Node expectedRootNode = NodeFactory.fromJsonString(
        "function Foo() {" +
        "  this.x_ = 10;" +
        "  this.setX = " + FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME + "_set;" +
        "}"
    );
    Node actualRootNode = compiler.getRootNode();
    assertEquals(expectedRootNode, actualRootNode);
  }

  @org.junit.Test
  public void testMultipleFunctions() {
    String inputCode = "function Foo() {" +
                       "  this.getX = function() { return this.x_; };" +
                       "  this.setX = function(value) { this.x_ = value; };" +
                       "  this.getY = function() { return this.y_; };" +
                       "  this.setY = function(value) { this.y_ = value; };" +
                       "}" +
                       "function Bar() { return Foo(); }";
    Node rootNode = NodeFactory.compile(inputCode);

    functionRewriter.process(compiler.getExterns(), rootNode);

    List<Node> helperFunctions = Arrays.asList(
        NodeFactory.fromJsonString(FunctionRewriter.EmptyFunctionReducer.HELPER_SOURCE),
        NodeFactory.fromJsonString(FunctionRewriter.GetterReducer.HELPER_SOURCE),
        NodeFactory.fromJsonString(FunctionRewriter.SetterReducer.HELPER_SOURCE)
    );
    TypeChecker checker = new TypeChecker(compiler, helperFunctions);
    checker.checkTypes(rootNode, null);

    Node expectedRootNode = NodeFactory.fromJsonString(
        "function Foo() {" +
        "  this.getX = " + FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME + "_get;" +
        "  this.setX = " + FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME + "_set;" +
        "  this.getY = " + FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME + "_get;" +
        "  this.setY = " + FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME + "_set;" +
        "}" +
        "function Foo() {}" +
        "function Bar() {" +
        "  return Foo();" +
        "}"
    );
    Node actualRootNode = compiler.getRootNode();
    assertEquals(expectedRootNode, actualRootNode);
  }
}