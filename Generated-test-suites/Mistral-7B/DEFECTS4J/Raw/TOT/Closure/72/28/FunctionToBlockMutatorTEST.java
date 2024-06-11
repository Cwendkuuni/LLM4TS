package com.google.javascript.jscomp;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  private static FunctionToBlockMutator createFunctionToBlockMutator(
      AbstractCompiler compiler, Supplier<String> safeNameIdSupplier) {
    return new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testSimpleFunctionMutation() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = createFunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeOperations.parseTemplate("function testFunction(x) { x++; return x; }");
    Node callNode = NodeOperations.parseTemplate("testFunction(5)");
    String resultName = "result";
    Node resultNode = NodeOperations.parseTemplate("var " + resultName + ";");

    Node newFnNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, false);
    Node expectedBlock = NodeOperations.parseTemplate("var test_inline_test_0 = x; x++; test_inline_test_0++; break;");

    // Compare the mutated node tree with the expected one.
    NodeUtil.deepEquals(newFnNode, expectedBlock);
  }

  @Test
  public void testFunctionWithArgumentsMutation() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = createFunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeOperations.parseTemplate("function testFunction(x, y) { x++; y++; return x + y; }");
    Node callNode = NodeOperations.parseTemplate("testFunction(5, 10)");
    String resultName = "result";
    Node resultNode = NodeOperations.parseTemplate("var " + resultName + ";");

    LinkedHashMap<String, Node> args = Maps.newLinkedHashMap();
    args.put("x", NodeOperations.parseTemplate("5"));
    args.put("y", NodeOperations.parseTemplate("10"));

    Node newFnNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, false);
    Node expectedBlock = NodeOperations.parseTemplate("var test_inline_x_0 = 5; test_inline_x_0++; var test_inline_y_1 = 10; test_inline_y_1++; test_inline_x_0 += test_inline_y_1; break;");

    // Compare the mutated node tree with the expected one.
    NodeUtil.deepEquals(newFnNode, expectedBlock);
  }

  @Test
  public void testFunctionWithLabelMutation() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = createFunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeOperations.parseTemplate(
        "function testFunction(x) { x++; return x; } testFunction: { x++; break testFunction; }");
    Node callNode = NodeOperations.parseTemplate("testFunction(5)");
    String resultName = "result";
    Node resultNode = NodeOperations.parseTemplate("var " + resultName + ";");

    Node newFnNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, true);

    Node expectedBlock = NodeOperations.parseTemplate(
        "testFunction_inline_testFunction_0: { var test_inline_x_0 = x; x++; test_inline_x_0++; break testFunction_inline_testFunction_0; }");

    // Compare the mutated node tree with the expected one.
    NodeUtil.deepEquals(newFnNode, expectedBlock);
  }
}