import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.NodeFactory;

public class FunctionRewriterTest {

 private Compiler compiler;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = new Compiler();
 nodeFactory = compiler.nodeFactory();
 }

 @Test
 public void testEmptyFunctionReduction() {
 Node emptyFunctionNode = nodeFactory.createFunctionExpression(
 null, null, null, null, null);
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 Node expectedHelperNode = nodeFactory.createCall(
 compiler.parseSyntheticCode(
 "JSCompiler_emptyFn",
 "function JSCompiler_emptyFn() {" +
 "  return function() {}" +
 "}")
 .removeFirstChild(), null);
 assertEquals(expectedHelperNode, rewriter.parseHelperCode(EmptyFunctionReducer.class));
 }

 @Test
 public void testIdentityFunctionReduction() {
 Node identityFunctionNode = nodeFactory.createFunctionExpression(
 null, null, null,
 nodeFactory.createName("a"),
 nodeFactory.createReturnStatement(nodeFactory.createName("a")));
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 Node expectedHelperNode = nodeFactory.createCall(
 compiler.parseSyntheticCode(
 "JSCompiler_identityFn",
 "function JSCompiler_identityFn() {" +
 "  function JSCompiler_identityFn_value(value) {" +
 "    return value;" +
 "  }" +
 "  return JSCompiler_identityFn_value;" +
 "}")
 .removeFirstChild(), null);
 assertEquals(expectedHelperNode, rewriter.parseHelperCode(IdentityReducer.class));
 }

 @Test
 public void testReturnConstantReduction() {
 Node returnConstantFunctionNode = nodeFactory.createFunctionExpression(
 null, null, null,
 null,
 nodeFactory.createReturnStatement(nodeFactory.createNumberLiteral(10)));
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 Node expectedHelperNode = nodeFactory.createCall(
 compiler.parseSyntheticCode(
 "JSCompiler_returnArg",
 "function JSCompiler_returnArg(value) {" +
 "  return function() {return value}" +
 "}")
 .removeFirstChild(),
 nodeFactory.createNumberLiteral(10));
 assertEquals(expectedHelperNode, rewriter.parseHelperCode(ReturnConstantReducer.class));
 }

 @Test
 public void testGetterReduction() {
 Node getterFunctionNode = nodeFactory.createFunctionExpression(
 null, null, null,
 nodeFactory.createName("this"),
 nodeFactory.createGetProp(nodeFactory.createName("this"), "b_"), null);
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 Node expectedHelperNode = nodeFactory.createCall(
 compiler.parseSyntheticCode(
 "JSCompiler_get",
 "function JSCompiler_get(name) {" +
 "  return function() {return this[name]}" +
 "}")
 .removeFirstChild(), nodeFactory.createName("b_"));
 assertEquals(expectedHelperNode, rewriter.parseHelperCode(GetterReducer.class));
 }

 @Test
 public void testSetterReduction() {
 Node setterFunctionNode = nodeFactory.createFunctionExpression(
 null, null, null,
 nodeFactory.createName("this"),
 nodeFactory.createName("value"), null,
 nodeFactory.createSetProp(nodeFactory.createThis(), "b_", nodeFactory.createName("value")));
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 Node expectedHelperNode = nodeFactory.createCall(
 compiler.parseSyntheticCode(
 "JSCompiler_set",
 "function JSCompiler_set(name) {" +
 "  return function(value) {" +
 "this[name] = value}" +
 "}")
 .removeFirstChild(), nodeFactory.createName("b_"));
 assertEquals(expectedHelperNode, rewriter.parseHelperCode(SetterReducer.class));
 }

 @Test
 public void testComplexFunctionReduction() {
 Node complexFunctionNode = nodeFactory.createFunctionExpression(
 null, null, null,
 nodeFactory.createName("this"),
 nodeFactory.createReturnStatement(nodeFactory.createCall(
 nodeFactory.createName("Math"), "sqrt",
 nodeFactory.createCall(nodeFactory.createName("Math"), "pow",
 nodeFactory.createNumberLiteral(2),
 nodeFactory.createNumberLiteral(3)))));
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 Node expectedHelperNode = nodeFactory.createCall(
 compiler.parseSyntheticCode(
 "JSCompiler_function",
 "function JSCompiler_function(name) {" +
 "  function JSCompiler_function_helper(value) {" +
 "    return function() {" +
 "      return Math.sqrt(Math.pow(2, value))" +
 "    }" +
 "  }" +
 "  return JSCompiler_function_helper;" +
 "}")
 .removeFirstChild(), null);
 assertEquals(expectedHelperNode, rewriter.parseHelperCode(FunctionRewriter.class));
 }

 @Test (expected = AssertionError.class)
 public void testNonFunctionExpressionNode() {
 Node nonFunctionNode = nodeFactory.createCall(nodeFactory.createName("Math"), "sqrt", nodeFactory.createNumberLiteral(2));
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 try {
 rewriter.parseHelperCode(NullPointerException.class);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test (expected = AssertionError.class)
 public void testCannotSaveSpace() {
 Node functionNode = nodeFactory.createFunctionExpression(
 null, null, null,
 null,
 nodeFactory.createReturnStatement(nodeFactory.createStringLiteral("hello")));
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, null);
 try {
 Node helperNode = rewriter.parseHelperCode(ReturnConstantReducer.class);
 fail();
 } catch (AssertionError e) {
 // Expected, helper code should not be generated since it would not save enough space
 }
 }
}