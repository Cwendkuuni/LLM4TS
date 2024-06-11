import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerUtils;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    options = new CompilerOptions();
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function(value) {" +
        " this.total += value; return this.total" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self, value) {" +
        " self.total += value; return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root, "Devirtualization test failed");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionType() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function(value) {" +
        " this.total += value; return this.total" +
        " } " +
        "A.prototype.process = function(a, b) {" +
        " this.accumulate(1);" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self, value) {" +
        " self.total += value; return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate " +
        "A.prototype.process = function(self, a, b) {" +
        " accumulate(self, 1);" +
        " }",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with function type");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithNoArguments() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function() {" +
        " this.total += 1; return this.total" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self) {" +
        " self.total += 1; return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root, "Devirtualization test failed with no arguments");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithReturnType() {
    String inputCode = "function A() {" +
        " this.value = 1;" +
        " } " +
        "A.prototype.accumulate = function(value) {" +
        " this.total += value; return this;" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {" +
        " this.value = 1;" +
        " } " +
        "var accumulate = function(self, value) {" +
        " self.total += value; return self;" +
        " } " +
        "A.prototype.accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root, "Devirtualization test failed with return type");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithVarargsFunction() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function() {" +
        " this.total += arguments[0];" +
        " for (var i = 1, len = arguments.length; i < len; i++) {" +
        " this.total += arguments[i];" +
        " }" +
        " return this.total" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self, var_args) {" +
        " var args = [].slice.call(var_args);" +
        " self.total += args[0];" +
        " for (var i = 1, len = args.length; i < len; i++) {" +
        " self.total += args[i];" +
        " }" +
        " return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with varargs function");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleDefinitions() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function() {" +
        " this.total += 1;" +
        " }" +
        "A.prototype.accumulate = function(value) {" +
        " this.total += value; return this.total" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self) {" +
        " self.total += 1;" +
        " } " +
        "A.prototype.accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with multiple definitions");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithStaticMethod() {
    String inputCode = "function A() {" +
        " this.total = 0;" +
        " } " +
        "A.prototype.accumulate = function() {" +
        " this.total += 1;" +
        " } " +
        "A.static = function() {" +
        " this.total += 1;" +
        " };";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {" +
        " this.total = 0;" +
        " } " +
        "var accumulate = function(self) {" +
        " self.total += 1;" +
        " } " +
        "A.prototype.accumulate = accumulate " +
        "A.static = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with static method");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithCyclicDependency() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function(value) {" +
        " this.total += value;" +
        " return this.B.accumulate(value * 2)" +
        " } " +
        "function B() {} " +
        "B.prototype.accumulate = function(value) {" +
        " this.total += value;" +
        " return this.A.accumulate(value * 2)" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "function B() {} " +
        "var accumulateA = function(self, value) {" +
        " self.total += value;" +
        " return accumulateB(self, value * 2)" +
        " } " +
        "var accumulateB = function(self, value) {" +
        " self.total += value;" +
        " return accumulateA(self, value * 2)" +
        " } " +
        "A.prototype.accumulate = accumulateA " +
        "B.prototype.accumulate = accumulateB",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with cyclic dependency");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithExportedMethod() {
    String inputCode = "function A() {" +
        " this.total = 0;" +
        " this.accumulate = function(value) {" +
        " this.total += value;" +
        " return this.total" +
        " }" +
        "}" +
        "exports.accumulate = A.accumulate";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {" +
        " this.total = 0;" +
        " var accumulate = function(self, value) {" +
        " self.total += value;" +
        " return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate" +
        "exports.accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with exported method");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithUnusedMethod() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function(value) {" +
        " return this.total += value;" +
        " } " +
        "function B() {} " +
        "B.prototype.useAccumulate = A.prototype.accumulate";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self, value) {" +
        " self.total += value;" +
        " return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate" +
        "function B() {} " +
        "B.prototype.useAccumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with unused method");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithUnreachableMethod() {
    String inputCode = "function A() {} " +
        "A.prototype.accumulate = function(value) {" +
        " return this.total += value;" +
        " } " +
        "function B() {} " +
        "B.prototype.useAccumulate = function() {" +
        " return this.accumulate(1); // dead code" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {} " +
        "var accumulate = function(self, value) {" +
        " self.total += value;" +
        " return self.total" +
        " } " +
        "A.prototype.accumulate = accumulate" +
        "function B() {} " +
        "B.prototype.useAccumulate = function() { }",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with dead code");
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithPrivateMethod() {
    String inputCode = "function A() {" +
        "__defineGetter__('accumulate', function() {" +
        " this._total += arguments[0];" +
        " })" +
        "}" +
        "A.prototype._accumulate = function(value) {" +
        " this._total += value;" +
        " return this._total" +
        " }";

    Node root = NodePrinter.parseJs(inputCode, "input", null);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node expectedOutput = NodePrinter.parseJs(
        "function A() {" +
        "__defineGetter__('accumulate', function() {" +
        " var accumulate = function(self, value) {" +
        " self._total += value;" +
        " return self._total" +
        " } " +
        " A.prototype.accumulate = accumulate" +
        "}" +
        "A.prototype._accumulate = accumulate",
        "output", null);

    assertCodeEqual(expectedOutput, root,
        "Devirtualization test failed with private method");
  }

  private void assertCodeEqual(Node expected, Node actual, String message) {
    String expectedCode = NodePrinter.printNode(expected);
    String actualCode = NodePrinter.printNode(actual);

    if (!expectedCode.equals(actualCode)) {
      throw new AssertionError(message + ". Expected: " + expectedCode +
          ". Actual: " + actualCode);
    }
  }
}