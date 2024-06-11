import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.*;

public class InlineObjectLiteralsTest {

 private Compiler compiler;
 private InlineObjectLiterals inlineObjectLiterals;

 @BeforeClass
 public static void setup() {
 // Initialize the compiler and the InlineObjectLiterals pass
 compiler = new Compiler();
 inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tempVar");
 }

 @Test
 public void testInlineSimpleObjectLiteral() {
 Node root = new Node("function foo() {" +
 "var x = {" +
 "a: 1," +
 "b: \"hello\"" +
 "};" +
 "}");
 inlineObjectLiterals.process(null, root);
 Node expected = new Node("function foo() {" +
 "var a = JSCompiler_object_inline_a_tempVar_0," +
 "var b = JSCompiler_object_inline_b_tempVar_1;" +
 "var x = {" +
 "a: a," +
 "b: b" +
 "};" +
 "}");
 assertEquals(expected.toString(), root.toString());
 }

 @Test
 public void testInlineComplexObjectLiteral() {
 Node root = new Node("function foo() {" +
 "var x = {" +
 "a: {" +
 "b: 1," +
 "c: 2" +
 "}," +
 "d: [1, 2, 3]" +
 "};" +
 "}");
 inlineObjectLiterals.process(null, root);
 Node expected = new Node("function foo() {" +
 "var a_b = JSCompiler_object_inline_a_b_tempVar_0," +
 "var a_c = JSCompiler_object_inline_a_c_tempVar_1;" +
 "var d_0 = JSCompiler_object_inline_d_0_tempVar_2," +
 "var d_1 = JSCompiler_object_inline_d_1_tempVar_3," +
 "var d_2 = JSCompiler_object_inline_d_2_tempVar_4;" +
 "var x = {" +
 "a: {" +
 "b: a_b," +
 "c: a_c" +
 "}," +
 "d: [d_0, d_1, d_2]" +
 "};" +
 "}");
 assertEquals(expected.toString(), root.toString());
 }

 @Test
 public void testInlineObjectLiteralWithSelfReferentialAssignment() {
 Node root = new Node("function foo() {" +
 "var x = {" +
 "a: {" +
 "b: x.a" +
 "}," +
 "c: 1" +
 "};" +
 "}");
 inlineObjectLiterals.process(null, root);
 // Self-referential assignment is not allowed, so the test should fail
 assertFalse(root.toString().contains("x.a.b = x.a.b"));
 }

 @Test
 public void testInlineObjectLiteralWithOverwrittenVariableName() {
 Node root = new Node("function foo() {" +
 "var x = {" +
 "a: 1," +
 "x: 2" +
 "};" +
 "var x = 3;" +
 "}");
 inlineObjectLiterals.process(null, root);
 Node expected = new Node("function foo() {" +
 "var a = JSCompiler_object_inline_a_tempVar_0," +
 "var x_1 = JSCompiler_object_inline_x_tempVar_1;" +
 "var x = 3;" +
 "var x = {" +
 "a: a" +
 "};" +
 "}");
 assertEquals(expected.toString(), root.toString());
 }

 @Test
 public void testInlineObjectLiteralWithUnexportedVariable() {
 Node root = new Node("function foo() {" +
 "var _ = {" +
 "x: 1" +
 "};" +
 "var x = _

.nextLine()
}");
 inlineObjectLiterals.process(null, root);
 Node expected = new Node("function foo() {" +
 "var _ = {" +
 "x: JSCompiler_object_inline_x_tempVar_0" +
 "};" +
 "var x = JSCompiler_object_inline_x_tempVar_0;" +
 "}");
 assertEquals(expected.toString(), root.toString());
 }

 @Test
 public void testInlineObjectLiteralWithGettersAndSetters() {
 Node root = new Node("function foo() {" +
 "function Foo() {" +
 "this.x = {" +
 "get X() {" +
 "return this._x;" +
 "}," +
 "set X(val) {" +
 "this._x = val;" +
 "}" +
 "}" +
 "var obj = new Foo();" +
 "obj.x = {" +
 "y: 1" +
 "};" +
 "}" +
 "}");
 inlineObjectLiterals.process(null, root);
 Node expected = new Node("function foo() {" +
 "function Foo() {" +
 "this.x = {" +
 "get X() {" +
 "return this._x;" +
 "}," +
 "set X(val) {" +
 "this._x = val;" +
 "}" +
 "}" +
 "var y = JSCompiler_object_inline_y_tempVar_0;" +
 "var obj = new Foo();" +
 "this.x = {" +
 "y: y" +
 "};" +
 "this.x.y = y;" +
 "}" +
 "}");
 assertEquals(expected.toString(), root.toString());
 }

 @Test
 public void testInlineObjectLiteralWithCyclicReferences() {
 Node root = new Node("function foo() {" +
 "var obj1 = {" +
 "x: obj2" +
 "}," +
 "var obj2 = {" +
 "x: obj1" +
 "};" +
 "}");
 inlineObjectLiterals.process(null, root);
 // Cyclic references are not allowed, so the test should fail
 assertFalse(root.toString().contains("obj1.x = obj2"));
 assertFalse(root.toString().contains("obj2.x = obj1"));
 }

 @After
 public void cleanUp() {
 // Clean up the compiler
 compiler = null;
 inlineObjectLiterals = null;
 }

}