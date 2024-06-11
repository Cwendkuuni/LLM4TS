import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp. ast.AstChangeProxy;
import com.google.javascript.jscomp.util.NodeUtil;
import com.google.common.collect.Lists;

public class RemoveConstantExpressionsTest {

 private AbstractCompiler compiler;
 private RemoveConstantExpressions pass;
 private RemoveConstantRValuesCallback callback;
 private AstChangeProxy changeProxy;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 pass = new RemoveConstantExpressions(compiler);
 callback = new RemoveConstantRValuesCallback();
 changeProxy = new AstChangeProxy();
 }

 @Test
 public void testProcess_EmptyProgram() {
 Node root = new Node(Token.PROGRAM, null);
 pass.process(null, root);
 assertFalse(changeProxy.hasChanged());
 }

 @Test
 public void testProcess_SimpleExpression() {
 Node root = NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("foo"),
 Token.PLUS,
 NodeUtil.createIdentifier("bar"));
 pass.process(null, root);

 Node expected = NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("foo"),
 Token.PLUS,
 NodeUtil.createIdentifier("bar"));

 assertEquals(expected, changeProxy.getChangedAST());
 }

 @Test
 public void testProcess_ComplexExpression() {
 Node root = NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("foo"),
 Token.PLUS,
 NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("bar"),
 Token.TIMES,
 NodeUtil.createNumeric(10),
 NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("baz"),
 Token.MINUS,
 NodeUtil.createNumeric(5))));

 Node expected = NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("foo"),
 Token.PLUS,
 NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("bar"),
 Token.TIMES,
 NodeUtil.createNumeric(10)),
 NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("baz"),
 Token.MINUS,
 NodeUtil.createNumeric(5)));

 pass.process(null, root);
 assertFalse(changeProxy.hasChanged());
 }

 @Test
 public void testProcess_ConstantExpression() {
 Node root = NodeUtil.createBinaryExpression(
 NodeUtil.createNumeric(5),
 Token.PLUS,
 NodeUtil.createNumeric(10));

 Node expected = NodeUtil.createIdentifier("5");

 pass.process(null, root);
 assertEquals(expected, changeProxy.getChangedAST());
 }

 @Test(expected = AssertionError.class)
 public void testProcess_ConstantExpression_WithSideEffects() {
 Node root = NodeUtil.createBinaryExpression(
 NodeUtil.createIdentifier("foo"),
 Token.PLUS,
 NodeUtil.createCallExpression(
 NodeUtil.createIdentifier("console"),
 NodeUtil.createIdentifier("log"),
 NodeUtil.createString("Hello World")));

 pass.process(null, root);
 // This test case should fail since the constant expression has side effects
 fail("Expected an AssertionError but no error was thrown");
 }

 @Test
 public void testGetSideEffectNodes_EmptyExpression() {
 Node node = new Node(Token.EXPR_RESULT, null);
 List<Node> result = callback.getSideEffectNodes(node);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetSideEffectNodes_Statement() {
 Node node = NodeUtil.createExpressionStatement(
 NodeUtil.createIdentifier("foo"));
 List<Node> result = callback.getSideEffectNodes(node);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetSideEffectNodes_ExpressionWithSideEffects() {
 Node node = NodeUtil.createCallExpression(
 NodeUtil.createIdentifier("console"),
 NodeUtil.createIdentifier("log"),
 NodeUtil.createString("Hello World"));
 List<Node> result = callback.getSideEffectNodes(node);
 assertEquals(1, result.size());
 assertEquals(node, result.get(0));
 }
}