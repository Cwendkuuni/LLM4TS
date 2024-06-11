import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.rhino.Node;

public class DevirtualizePrototypeMethodsTest {

 private Compiler compiler;
 private CompilationTestHelper helper;

 @Before
 public void setup() {
 compiler = new Compiler();
 helper = new CompilationTestHelper(compiler);
 }

 @Test
 public void testDevirtualizePrototypeMethods() {
 // Given a simple class with a prototype method
 String source = "function A() {}\n" +
 "A.prototype.accumulate = function(value) { this.total += value; return this.total; };\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n" +
 "assertEquals(4, total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype method has been replaced with a static method
 assertTrue(output.contains("var accumulate = function(self, value) { self.total += value; return self.total; }"));
 assertTrue(output.contains("total = accumulate(a, 2)"));

 // And the total variable has the expected value
 assertTrue(output.contains("assertEquals(4, total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithMultipleMethods() {
 // Given a class with multiple prototype methods
 String source = "function A() {}\n" +
 "A.prototype.accumulate = function(value) { this.total += value; return this.total; };\n" +
 "A.prototype.subtract = function(value) { this.total -= value; return this.total; };\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n" +
 "total = a.subtract(1);\n" +
 "assertEquals(3, total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype methods have been replaced with static methods
 assertTrue(output.contains("var accumulate = function(self, value) { self.total += value; return self.total; }"));
 assertTrue(output.contains("var subtract = function(self, value) { self.total -= value; return self.total; }"));
 assertTrue(output.contains("total = accumulate(a, 2)"));
 assertTrue(output.contains("total = subtract(a, 1)"));
 assertTrue(output.contains("assertEquals(3, total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithExportedProperty() {
 // Given a class with an exported prototype property
 String source = "function A() {}\n" +
 "A.prototype.total = 0;\n" +
 "exports.accumulate = function(a) { return a.total += 1; };\n" +
 "var a = new A();\n" +
 "exports.accumulate(a);\n" +
 "assertEquals(1, a.total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype property is not touched
 assertTrue(output.contains("exports.accumulate = function(a) { return a.total += 1; }"));
 assertTrue(output.contains("assertEquals(1, a.total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithCallSiteInModuleLoadedAfterDefinition() {
 // Given a class with a prototype method and a call site in a module loaded after the definition
 String source = "function A() {}\n" +
 "A.prototype.accumulate = function(value) { this.total += value; return this.total; };\n" +
 "var a = new A();\n" +
 "module.exports.usePrototypeMethod = function() { a.accumulate(1); };\n" +
 "module B = require('./main.js').usePrototypeMethod;\n" +
 "B();\n" +
 "assertEquals(1, a.total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype method is not touched
 assertTrue(output.contains("module.exports.usePrototypeMethod = function() { a.accumulate(1); };"));
 assertTrue(output.contains("B();"));
 assertTrue(output.contains("assertEquals(1, a.total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithCallSiteInSameModule() {
 // Given a class with a prototype method and a call site in the same module
 String source = "function A() {}\n" +
 "A.prototype.accumulate = function(value) { this.total += value; return this.total; };\n" +
 "module.exports.usePrototypeMethod = function() { a.accumulate(1); };\n" +
 "var a = new A();\n" +
 "module.exports.usePrototypeMethod();\n" +
 "assertEquals(1, a.total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype method is replaced with a static method
 assertTrue(output.contains("var accumulate = function(self, value) { self.total += value; return self.total; }"));
 assertTrue(output.contains("module.exports.usePrototypeMethod = function() { accumulate(a, 1); };"));
 assertTrue(output.contains("assertEquals(1, a.total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithArgumentsVariable() {
 // Given a class with a prototype method that uses the arguments variable
 String source = "function A() {}\n" +
 "A.prototype.accumulate = function() {\n" +
 " var total = 0;\n" +
 " for (var _i = 0; _i < arguments.length; _i++) {\n" +
 " total += arguments[_i];\n" +
 " }\n" +
 " return total;\n" +
 "};\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(1, 2, 3);\n" +
 "assertEquals(6, total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype method is not touched
 assertTrue(output.contains("function A() {}\n" +
 "A.prototype.accumulate = function() {\n" +
 " var total = 0;\n" +
 " for (var _i = 0; _i < arguments.length; _i++) {\n" +
 " total += arguments[_i];\n" +
 " }\n" +
 " return total;\n" +
 "};"));
 assertTrue(output.contains("assertEquals(6, total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithVarArgsFunction() {
 // Given a class with a prototype method that is a varargs function
 String source = "function A() {}\n" +
 "A.prototype.accumulate = function() {\n" +
 " var args = Array.prototype.slice.call(arguments, 0);\n" +
 " var total = 0;\n" +
 " for (var i = 0; i < args.length; i++) {\n" +
 " total += args[i];\n" +
 " }\n" +
 " return total;\n" +
 "};\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(1, 2, 3);\n" +
 "assertEquals(6, total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype method is not touched
 assertTrue(output.contains("function A() {}\n" +
 "A.prototype.accumulate = function() {\n" +
 " var args = Array.prototype.slice.call(arguments, 0);\n" +
 " var total = 0;\n" +
 " for (var i = 0; i < args.length; i++) {\n" +
 " total += args[i];\n" +
 " }\n" +
 " return total;\n" +
 "};"));
 assertTrue(output.contains("assertEquals(6, total)"));
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithPrototypeMethodInObjectLiteral() {
 // Given a class with a prototype method defined in an object literal
 String source = "function A() {}\n" +
 "var obj = {\n" +
 " prototype: {\n" +
 " accumulate: function(value) { this.total += value; return this.total; }\n" +
 " },\n" +
 " total: 0\n" +
 " };\n" +
 "A.prototype = obj.prototype;\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(1);\n" +
 "assertEquals(1, total);";

 // When the compiler passes DevirtualizePrototypeMethods
 helper.run(source, DevirtualizePrototypeMethods.class);
 String output = helper.getCompiledCode();

 // Then the prototype method is not touched
 assertTrue(output.contains("var obj = {\n" +
 " prototype: {\n" +
 " accumulate: function(self, value) { self.total += value; return self.total; }\n" +
 " },\n" +
 " total: 0\n" +
 " };\n" +
 "A.prototype = obj.prototype;\n" +
 "var a = new A();\n" +
 "var total = accumulate(a, 1);\n" +
 "assertEquals(1, total);"));
 }
}