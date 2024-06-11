package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    // Initialize the compiler for testing
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    compiler = new AbstractCompiler(options, null, null, null, null);
    compiler.init();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_EmptyScript() {
    Node root = Node.newBlock("{}");
    runtimeTypeCheck.process(null, root);
    // Add an assertion here to check the result of processing an empty script
  }

  @Test
  public void testProcess_FunctionWithSingleParameter() {
    String testCode = "function foo(x) {}";
    Node root = Node.parse(testCode);
    runtimeTypeCheck.process(null, root);

    Node fooNode = NodeUtil.findFirstFunction(root, "foo");
    Node functionTypeNode = fooNode.getFirstChild();
    FunctionType functionType = (FunctionType) functionTypeNode.getJSType();

    Node paramNode = NodeUtil.getFnParameters(fooNode).getFirstChild();
    Node checkNode = functionTypeNode.getLastChild().getLastChild();

    // Add assertions here to check the inserted checkType calls
    // for the function's parameter and return value
  }

  @Test
  public void testProcess_FunctionWithMultipleParameters() {
    String testCode = "function foo(x, y) {}";
    Node root = Node.parse(testCode);
    runtimeTypeCheck.process(null, root);

    Node fooNode = NodeUtil.findFirstFunction(root, "foo");
    FunctionType functionType = (FunctionType) fooNode.getJSType();

    Node paramNodes = NodeUtil.getFnParameters(fooNode);
    Node checkNodes = functionTypeNode.getLastChild().getLastChild().getChildren();

    // Add assertions here to check the inserted checkType calls
    // for all parameters and return value

    assertEquals(functionType.getParameters().length, checkNodes.size());
  }

  @Test
  public void testProcess_FunctionWithUnionTypeParameter() {
    String testCode = "function foo(x) { x = 42; }";
    Node root = Node.parse(testCode);

    // Set the JSType of the function's parameter to a union type
    FunctionType functionType = new FunctionType(
        "foo", ImmutableList.of(), Collections.singletonList(
            new UnionType(
                new ObjectType("String"), new ObjectType("Number"))));
    functionTypeNode.setJSType(functionType);

    runtimeTypeCheck.process(null, root);

    Node fooNode = NodeUtil.findFirstFunction(root, "foo");
    FunctionType functionTypeAfterCheck = (FunctionType) fooNode.getJSType();

    Node paramNode = NodeUtil.getFnParameters(fooNode).getFirstChild();
    Node checkNode = functionTypeAfterCheck.getParameters().get(0).getChecker();

    // Add assertions here to check the inserted checkType calls
    // for the union type parameter
  }

  @Test
  public void testProcess_FunctionWithInterfaceTypeParameter() {
    String testCode = "function foo(x) {}";
    SourceName sourceName = new SourceName("testInterface.js");

    Node root = Node.parse(testCode);

    // Set the JSType of the function's parameter to an interface type
    ObjectType interfaceType = new ObjectType("MyInterface");
    FunctionType functionType = new FunctionType(
        "foo", ImmutableList.of(),
        ImmutableList.of(new FunctionParameterType("x", interfaceType)));
    functionTypeNode.setJSType(functionType);

    CompilerInput interfaceInput = new CompilerInput("testInterface.js", "testInterface.js", null);
    CompilerPass interfaceCheck = new InterfaceCheck(compiler);
    interfaceCheck.process(null, interfaceInput.getInput());

    runtimeTypeCheck.process(null, root);

    Node fooNode = NodeUtil.findFirstFunction(root, "foo");
    FunctionType functionTypeAfterCheck = (FunctionType) fooNode.getJSType();

    Node paramNode = NodeUtil.getFnParameters(fooNode).getFirstChild();
    Node checkNode = functionTypeAfterCheck.getParameters().get(0).getChecker();

    // Add assertions here to check the inserted checkType calls
    // for the interface type parameter
  }
}