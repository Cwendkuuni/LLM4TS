import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;

public class DevirtualizePrototypeMethodsTest {
  @Test
  public void testDevirtualizePrototypeMethods() {
    // Setup
    Compiler compiler = new Compiler();
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);

    // Test case 1: Basic functionality
    String sourceCode = "function A() {} A.prototype.accumulate = function(value) {"
        + " this.total += value; return this.total;"
        + "} var a = new A(); a.total = 0; var total = a.accumulate(2); assertEquals(2, total);";
    Node root = Parser.parse(sourceCode, null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);
    devirtualizer.process(null, root);

    // Test the rewritten code
    Node rewrittenCode = compiler.getProcessedSource();
    assertEquals("var accumulate = function(self, value) {"
        + " self.total += value; return self.total;"
        + "} var total = accumulate(a, 2); assertEquals(2, total);",
        rewrittenCode.toString());

    // Test case 2: Edge case - Function with no arguments
    sourceCode = "function A() {} A.prototype.accumulate = function() {"
        + " this.total += 1; return this.total;"
        + "} var a = new A(); var total = a.accumulate(); assertEquals(1, total);";
    root = Parser.parse(sourceCode, null);
    devirtualizer.process(null, root);
    rewrittenCode = compiler.getProcessedSource();
    assertEquals("var accumulate = function(self) {"
        + " self.total += 1; return self.total;"
        + "} var total = accumulate(a); assertEquals(1, total);",
        rewrittenCode.toString());

    // Test case 3: Edge case - Function with varargs
    sourceCode = "function A() {} A.prototype.accumulate = function() {"
        + " var sum = 0; for (var i = 0; i < arguments.length; i++) {"
        + " sum += arguments[i];"
        + " } this.total = sum; return this.total;"
        + "} var a = new A(); var total = a.accumulate(1, 2, 3); assertEquals(6, total);";
    root = Parser.parse(sourceCode, null);
    devirtualizer.process(null, root);
    rewrittenCode = compiler.getProcessedSource();
    assertEquals("var accumulate = function(self) {"
        + " var sum = 0, _args = arguments; for (var i = 0; i < _args.length; i++) {"
        + " sum += _args[i];"
        + " } self.total = sum; return self.total;"
        + "} var total = accumulate(a, [1, 2, 3]); assertEquals(6, total);",
        rewrittenCode.toString());

    // Test case 4: Error scenario - Method not called from prototype
    sourceCode = "function A() { this.total = 0; } A.prototype.accumulate = function(value) {"
        + " this.total += value; return this.total;"
        + "} var a = new A(); var total = accumulate(a, 2); // 'accumulate' should not be accessible outside of prototypes";
    root = Parser.parse(sourceCode, null);
    try {
      devirtualizer.process(null, root);
      fail("Exception expected");
    } catch (RuntimeException e) {
      // Expected
    }
  }
}