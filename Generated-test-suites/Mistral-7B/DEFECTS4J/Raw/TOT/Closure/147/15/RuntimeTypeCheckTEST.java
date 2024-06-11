import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import junit.framework.Assert;

import java.io.IOException;
import java.util.Arrays;

public class RuntimeTypeCheckTest {

  private static CompilerPass createPass(AbstractCompiler compiler, String logFunction) {
    return new RuntimeTypeCheck(compiler, logFunction);
  }

  @org.junit.Test
  public void testSimpleFunctionWithNumberParameter() throws IOException {
    CompilerInput input = new CompilerInput(
        "test_simple_function_with_number_parameter.js",
        "test_simple_function_with_number_parameter.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, null);
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNode = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        1.0);
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNode);

    Assert.assertEquals(1.0, resultNode.getNumber().doubleValueExact());
  }

  @org.junit.Test
  public void testFunctionWithStringParameterAndStringReturn() throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_string_parameter_and_string_return.js",
        "test_function_with_string_parameter_and_string_return.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, null);
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNode = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        "hello world");
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNode);

    Assert.assertEquals("hello world", resultNode.getString());
  }

  @org.junit.Test
  public void testFunctionWithNumberParameterAndStringReturn() throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_number_parameter_and_string_return.js",
        "test_function_with_number_parameter_and_string_return.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, null);
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNode = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        1.0);
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNode);

    Assert.assertEquals("1", resultNode.getString());
  }

  @org.junit.Test
  public void testFunctionWithBooleanParameterAndBooleanReturn() throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_boolean_parameter_and_boolean_return.js",
        "test_function_with_boolean_parameter_and_boolean_return.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, null);
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNodeTrue = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        true);
    Node resultNodeTrue = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNodeTrue);

    Assert.assertTrue(resultNodeTrue.getBooleanValue());

    Node callNodeFalse = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        false);
    Node resultNodeFalse = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNodeFalse);

    Assert.assertFalse(resultNodeFalse.getBooleanValue());
  }

  @org.junit.Test
  public void testFunctionWithNullParameterAndNullReturn() throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_null_parameter_and_null_return.js",
        "test_function_with_null_parameter_and_null_return.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, null);
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNodeNull = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"), null);
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNodeNull);

    Assert.assertNull(resultNode);
  }

  @org.junit.Test
  public void testFunctionWithNumberParameterAndStringReturnWithLogFunction()
      throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_number_parameter_and_string_return_with_log_function.js",
        "test_function_with_number_parameter_and_string_return_with_log_function.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, "println");
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNode = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        1.0);
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNode);

    Assert.assertEquals("testFunction(1)", compiler.getDiagnostics().getMessages().get(0).getMessage());
    Assert.assertEquals(1.0, resultNode.getNumber().doubleValueExact());
  }

  @org.junit.Test
  public void testFunctionWithStringParameterAndNumberReturnWithLogFunction()
      throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_string_parameter_and_number_return_with_log_function.js",
        "test_function_with_string_parameter_and_number_return_with_log_function.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, "println");
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNode = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        "hello world");
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNode);

    Assert.assertEquals("testFunction(hello world)", compiler.getDiagnostics().getMessages().get(0).getMessage());
    Assert.assertEquals(11.0, resultNode.getNumber().doubleValueExact());
  }

  @org.junit.Test
  public void testFunctionWithBooleanParameterAndStringReturnWithLogFunction()
      throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_boolean_parameter_and_string_return_with_log_function.js",
        "test_function_with_boolean_parameter_and_string_return_with_log_function.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, "println");
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNodeTrue = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        true);
    Node resultNodeTrue = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNodeTrue);

    Assert.assertEquals("testFunction(true)", compiler.getDiagnostics().getMessages().get(0).getMessage());
    Assert.assertEquals("true", resultNodeTrue.getString());

    Node callNodeFalse = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"),
        false);
    Node resultNodeFalse = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNodeFalse);

    Assert.assertEquals("testFunction(false)", compiler.getDiagnostics().getMessages().get(0).getMessage());
    Assert.assertEquals("false", resultNodeFalse.getString());
  }

  @org.junit.Test
  public void testFunctionWithNullParameterAndStringReturnWithLogFunction()
      throws IOException {
    CompilerInput input = new CompilerInput(
        "test_function_with_null_parameter_and_string_return_with_log_function.js",
        "test_function_with_null_parameter_and_string_return_with_log_function.js");
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = createPass(compiler, "println");
    Node root = NodeUtil.parseCompilationUnit(input.getText(), input);
    pass.process(input, root);

    Node functionNode = NodeUtil.getFunctionByName(root, "testFunction");
    Node callNodeNull = NodeUtil.newExprCall(NodeUtil.getIdentifier("testFunction"), null);
    Node resultNode = NodeUtil.runCompilation(compiler, Arrays.asList(input), callNodeNull);

    Assert.assertEquals("testFunction(null)", compiler.getDiagnostics().getMessages().get(0).getMessage());
    Assert.assertEquals("null", resultNode.getString());
  }
}