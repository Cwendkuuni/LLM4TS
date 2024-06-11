import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DevirtualizePrototypeMethodsTest {

 private Compiler compiler;
 private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

 @Before
 public void setUp() {
 compiler = new Compiler();
 devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 }

 @Test
 public void testDevirtualizePrototypeMethods() {
 // Create a simple test case for the DevirtualizePrototypeMethods class
 Node root = Node.objectLiteral();
 Node obj = Node.newObject("A");
 Node prototype = Node.objectLiteral();
 Node accumulateFunction = Node.functionLiteral(
 Node.newThis(), Node.newParam("value"), Node.newExpression(
 Node.newOperator("+"), Node.newMember(Node.newThis(), "total"), Node.newNumber(1)));
 Node total = Node.newName("total");
 Node accumulateCall = Node.newCall(Node.newGetProp(obj, "prototype"), "accumulate", Node.newArray(obj, Node.newNumber(2)));
 Node expectedTotal = Node.newMember(Node.newThis(), "total");
 Node expectedCall = Node.newCall(Node.newName("accumulate"), Node.newArray(obj, Node.newNumber(2)));
 Node expectedCode = Node.newBlock(
 Node.newExpression("var accumulate = function(self, value) { self.total += value; return self.total }"),
 Node.newExpression("var total = accumulate(obj, 2)"),
 Node.newExpression("assertEquals(total, " + expectedTotal.toSource() + ")"),
 Node.newExpression("assertEquals(obj, " + obj.toSource() + ")")
 );

 // Test the DevirtualizePrototypeMethods class on the given root node
 devirtualizePrototypeMethods.process(Node.nullNode(), root);
 Node actualCode = compiler.getCompilerOutput();

 // Check if the code has been transformed as expected
 assertEquals("Expected code to be transformed", expectedCode.toSource(), actualCode.toSource());
 // Check if the accumulate method has been transformed into a static method
 Node accumulateStatic = Node.nullNode();
 for (Node child : root.children()) {
 if (NodeUtil.isFunction(child) && child.getString().startsWith("JSCompiler_StaticMethods_accumulate")) {
 accumulateStatic = child;
 break;
 }
 }
 // Check if the accumulateStatic method accepts the correct arguments
 Node accumulateStaticCall = Node.newCall(accumulateStatic, Node.newArray(obj, Node.newNumber(2)));
 assertEquals("Expected total to be correct after calling the static method", expectedTotal.toSource(), accumulateStaticCall.getFirstChild().getLastChild().toSource());
 }

 @Test(expected = AssertionError.class)
 public void testDevirtualizePrototypeMethodsException() {
 // Create a test case for an exception when an ineligible method is attempted to be devirtualized
 Node root = Node.objectLiteral();
 Node obj = Node.newObject("A");
 Node prototype = Node.objectLiteral();
 Node accumulateFunction = Node.functionLiteral(
 Node.newParam("value"), Node.newExpression(
 Node.newOperator("+"), Node.newGetProp(Node.newThis(), "total"), Node.newNumber(1)));
 Node total = Node.newName("total");
 Node accumulateCall = Node.newCall(Node.newGetProp(obj, "prototype"), "accumulate", Node.newArray(Node.newNumber(2)));
 Node expectedTotal = Node.newMember(Node.newThis(), "total");
 Node expectedCall = Node.newCall(Node.newName("accumulate"), Node.newArray(Node.newNumber(2)));
 Node expectedCode = Node.newBlock(
 Node.newExpression("var accumulate = function(self, value) { self.total += value; return self.total }"),
 Node.newExpression("var total = accumulate(obj, 2)"),
 Node.newExpression("assertEquals(total, " + expectedTotal.toSource() + ")"),
 Node.newExpression("assertEquals(obj, " + obj.toSource() + ")")
 );

 // Test the DevirtualizePrototypeMethods class on the given root node
 devirtualizePrototypeMethods.process(Node.nullNode(), root);
 Node actualCode = compiler.getCompilerOutput();

 // Check if the code has not been transformed
 assertEquals("Expected code to be unchanged", expectedCode.toSource(), actualCode.toSource());
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithVarArgs() {
 // Create a test case for a method with varargs
 Node root = Node.objectLiteral();
 Node obj = Node.newObject("A");
 Node prototype = Node.objectLiteral();
 Node accumulateFunction = Node.functionLiteral(
 Node.newParam("arguments"), Node.newExpression(
 Node.newOperator("+"), Node.newGetProp(Node.newThis(), "total"), Node.newMember(Node.newThis(), "args").getLastChild())
 );
 Node total = Node.newName("total");
 Node accumulateCall = Node.newCall(Node.newGetProp(obj, "prototype"), "accumulate", Node.newArray(obj));
 Node expectedTotal = Node.newMember(Node.newThis(), "total");
 Node expectedCall = Node.newCall(Node.newName("accumulate"), Node.newArray(obj));
 Node args = Node.newArray();
 for (int i = 0; i < 5; i++) {
 args.addLastChild(Node.newNumber(i + 1));
 }
 Node expectedArgs = Node.newMember(Node.newThis(), "args");
 Node argsCode = Node.newExpression("assertEquals(args, " + expectedArgs.toSource() + ")");
 Node expectedCode = Node.newBlock(
 Node.newExpression("var accumulate = function(self, args) { self.total += args[0].toNumber(); for (var i = 1; i < args.length; i++) { self.total += args[i].toNumber(); } return self.total }"),
 Node.newExpression("var total = accumulate(obj)"),
 Node.newExpression("assertEquals(total, " + expectedTotal.toSource() + ")"),
 Node.newExpression("assertEquals(obj, " + obj.toSource() + ")"),
 argsCode
 );

 // Test the DevirtualizePrototypeMethods class on the given root node
 devirtualizePrototypeMethods.process(Node.nullNode(), root);
 Node actualCode = compiler.getCompilerOutput();

 // Check if the code has been transformed as expected
 assertEquals("Expected code to be transformed", expectedCode.toSource(), actualCode.toSource());
 // Check if the accumulate method accepts varargs correctly
 Node accumulateStatic = Node.nullNode();
 for (Node child : root.children()) {
 if (NodeUtil.isFunction(child) && child.getString().startsWith("JSCompiler_StaticMethods_accumulate")) {
 accumulateStatic = child;
 break;
 }
 }
 Node accumulateStaticCall = Node.newCall(accumulateStatic, Node.newArray(obj, args));
 assertEquals("Expected total to be correct after calling the static method", expectedTotal.toSource(), accumulateStaticCall.getFirstChild().getLastChild().getLastChild().toSource());
 }
}