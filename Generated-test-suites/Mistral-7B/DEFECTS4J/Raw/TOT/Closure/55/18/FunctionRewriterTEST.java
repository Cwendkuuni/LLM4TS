import static com.google.javascript.jscomp.FunctionRewriter.*;
import static com.google.javascript.jscomp.NodeTraversal.simple;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

/**
 * Tests for FunctionRewriter class.
 */
public class FunctionRewriterTest extends CompilerTestCase {

  @Override
  protected CompilerPass getProcessor(AbstractCompiler compiler) {
    return new FunctionRewriter(compiler);
  }

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    options.setWarningLevel("JS_DEPRECATED", Code.INFO);
    return options;
  }

  /**
   * Test empty function reduction.
   */
  public void testEmptyFunctionReduction() {
    testReduction("emptyFunction.js", "function() {}",
                  "function() {}", "function() {}");
  }

  /**
   * Test identity function reduction.
   */
  public void testIdentityFunctionReduction() {
    testReduction("identityFunction.js", "function(x) { return x; }",
                  "function(x) { return x; }", "function(x) { return x; }");

    testReduction("identityFunction.js", "function() { return this; }",
                  "function() { return this; }", "function() { return this; }");
  }

  /**
   * Test return constant reduction.
   */
  public void testReturnConstantReduction() {
    testReduction("returnConstant.js", "function() { return 1; }",
                  "function() { return 1; }", "function() { return 1; }");

    testReduction("returnConstant.js", "function() { return 'hello'; }",
                  "function() { return 'hello'; }", "function() { return 'hello'; }");
  }

  /**
   * Test getter reduction.
   */
  public void testGetterReduction() {
    testReduction("getter.js", "function getA() { return this.a; }",
                  "function getA() { return JSCompiler_get('a'); }",
                  "function getA() { return this.a; }");

    testReduction("getter.js", "function getB(obj) { return obj.b; }",
                  "function getB(obj) { return JSCompiler_get('b'); }",
                  "function getB(obj) { return obj.b; }");
  }

  /**
   * Test setter reduction.
   */
  public void testSetterReduction() {
    testReduction("setter.js", "function setA(value) { this.a = value; }",
                  "function setA(value) { JSCompiler_set('a', value); }",
                  "function setA(value) { this.a = value; }");

    testReduction("setter.js", "function setB(obj, value) { obj.b = value; }",
                  "function setB(obj, value) { JSCompiler_set('b', value); }",
                  "function setB(obj, value) { obj.b = value; }");
  }

  /**
   * Test helper methods are not inlined.
   */
  public void testHelperMethodsNotInlined() {
    testReduction("helperMethods.js",
                  "function getA() { return JSCompiler_get('a'); }",
                  "function getA() { return JSCompiler_get('a'); }",
                  "function getA() { return JSCompiler_get('a'); }");

    testReduction("helperMethods.js",
                  "function setA(value) { JSCompiler_set('a', value); }",
                  "function setA(value) { JSCompiler_set('a', value); }",
                  "function setA(value) { JSCompiler_set('a', value); }");
  }

  /**
   * Test reductions are not applied when savings threshold is not met.
   */
  public void testSavingsThreshold() {
    testReduction("savingsThreshold.js",
                  "function getA() { return this.a; }",
                  "function getA() { return JSCompiler_get('a'); }",
                  "function getA() { return this.a; }");

    testReduction("savingsThreshold.js",
                  "function getA() { return this.a; }",
                  "function getA() { return this.a; }",
                  "function getA() { return this.a; }");
  }

  /**
   * Test reduction does not break existing code.
   */
  public void testReductionDoesNotBreakExistingCode() {
    String source =
        "function A() {" +
        "  this.a = 10;" +
        "  function getA() { return this.a; }" +
        "  function setA(value) { this.a = value; }" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not break existing code when using named functions.
   */
  public void testReductionDoesNotBreakExistingCodeWithNamedFunction() {
    String source =
        "function A() {" +
        "  this.a = 10;" +
        "  function getA() {" +
        "    return this.a;" +
        "  }" +
        "  function setA(value) {" +
        "    this.a = value;" +
        "  }" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not break existing code when using arrow functions.
   */
  public void testReductionDoesNotBreakExistingCodeWithArrowFunction() {
    String source =
        "function A() {" +
        "  this.a = 10;" +
        "  const getA = () => this.a;" +
        "  const setA = value => this.a = value;" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not break existing code when using computed properties.
   */
  public void testReductionDoesNotBreakExistingCodeWithComputedProperties() {
    String source =
        "class A {" +
        "  a = 10;" +
        "  get getA() {" +
        "    return this.a;" +
        "  }" +
        "  set setA(value) {" +
        "    this.a = value;" +
        "  }" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not break existing code when using getters and setters.
   */
  public void testReductionDoesNotBreakExistingCodeWithGettersAndSetters() {
    String source =
        "class A {" +
        "  get a() {" +
        "    return this._a;" +
        "  }" +
        "  set a(value) {" +
        "    this._a = value;" +
        "  }" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not clash with existing identifiers.
   */
  public void testReductionDoesNotClashWithExistingIdentifiers() {
    String source =
        "function A() {" +
        "  this.a = 10;" +
        "  function getA() { return this.a; }" +
        "  function JSCompiler_get(name) {" +
        "    return function() { return this[name]; }.bind(this);" +
        "  }" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not clash with existing helper names.
   */
  public void testReductionDoesNotClashWithExistingHelperNames() {
    String source =
        "function JSCompiler_get(name) {" +
        "  return function() { return this[name]; }.bind(this);" +
        "}" +
        "function A() {" +
        "  this.a = 10;" +
        "  function getA() { return JSCompiler_get('a'); }" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not clash with existing property names.
   */
  public void testReductionDoesNotClashWithExistingPropertyNames() {
    String source =
        "function A() {" +
        "  this.getA = function() { return this.a; }" +
        "  this.setA = function(value) { this.a = value; }" +
        "  this.JSCompiler_get = function(name) {" +
        "    return function() { return this[name]; }.bind(this);" +
        "  }" +
        "}" +
        "A.prototype.a = 10;" +
        "function test() {" +
        "  var a = new A();" +
        "  assertEquals(10, a.getA());" +
        "  a.setA(20);" +
        "  assertEquals(20, a.getA());" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Test reduction does not clash with existing method names.
   */
  public void testReductionDoesNotClashWithExistingMethodNames() {
    String source =
        "function A() {" +
        "  this.getA = function() { return this.a; }" +
        "  this.setA = function(value) { this.a = value; }" +
        "  this.get = function() { return this.a; }" +
        "}" +
        "A.prototype.a = 10;" +
        "function test() {" +
        "  var a = new A();" +
        "  assertEquals(10, a.getA());" +
        "  a.setA(20);" +
        "  assertEquals(20, a.getA());" +
        "  assertEquals(20, a.get()); // getter method name clash test" +
        "}";

    testReduction(source, source, source, source);
  }

  /**
   * Helper method for testing reduction.
   */
  private void testReduction(String inputFileName, String inputCode,
                            String expectedOutputCode, String expectedTestCode) {
    String outputFileName = inputFileName + "_output.js";
    String testFileName = inputFileName + "_test.js";

    String input = readFileIntoString(inputFileName);
    CompilerPass processor = getProcessor(newNullCompiler());
    Node root = processString(processor, input, CompilerOptions.standard());

    String expectedOutput = readFileIntoString(outputFileName);
    assertEquals(expectedOutputCode, getSource(root));

    String test = readFileIntoString(testFileName);
    Node testRoot = processString(processor, test, CompilerOptions.standard());
    assertEquals(expectedTestCode, getSource(testRoot));

    NodeTraversal.traverse(processor, root, new NodePrinter(new NullPrinter()));
    System.out.println();

    NodeTraversal.traverse(processor, testRoot, new NodePrinter(new NullPrinter()));
    System.out.println();
  }
}