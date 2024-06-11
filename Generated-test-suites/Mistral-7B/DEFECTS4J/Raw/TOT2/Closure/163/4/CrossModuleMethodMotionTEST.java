import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;

public class CrossModuleMethodMotionTest {
  @Test
  public void testMoveMethods() {
    // Create test compiler and pass
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);
    
    // Create test root node
    Node root = compiler.parseJavaScript("", null);

    // Test with empty node tree
    pass.process(null, root);
    assertEquals(0, pass.idGenerator.currentId);

    // Test with simple node tree
    Node simpleNodeTree = compiler.parseJavaScript("function Foo() {}", null);
    pass.process(null, simpleNodeTree);
    assertEquals(0, pass.idGenerator.currentId);

    // Test with node tree containing prototype properties
    Node nodeTreeWithPrototypeProperties = compiler.parseJavaScript(
        "function Foo() {} Foo.prototype.bar = function() {};", null);
    pass.process(null, nodeTreeWithPrototypeProperties);
    assertEquals(1, pass.idGenerator.currentId);

    // Test with node tree containing prototype properties in different modules
    Node nodeTreeWithPrototypePropertiesInDifferentModules = compiler.parseJavaScript(
        "function Foo() {}" +
        "function Bar() { Foo.prototype.baz = function() {}; }", null);
    pass.process(null, nodeTreeWithPrototypePropertiesInDifferentModules);
    assertEquals(2, pass.idGenerator.currentId);

    // Test with node tree containing prototype properties and exception
    Node nodeTreeWithPrototypePropertiesAndException = compiler.parseJavaScript(
        "function Foo() {" +
        "  function bar() {" +
        "    throw new Error('Test error');" +
        "  }" +
        "  Foo.prototype.baz = bar;" +
        "}" +
        "function Bar() {}", null);
    try {
      pass.process(null, nodeTreeWithPrototypePropertiesAndException);
      fail("Expected exception");
    } catch (Exception e) {
      // Expected exception, continue testing
    }
    assertEquals(3, pass.idGenerator.currentId);

    // Test with node tree containing prototype properties and null deepest common module
    Node nodeTreeWithPrototypePropertiesAndNullDeepestCommonModule = compiler.parseJavaScript(
        "function Foo() {}" +
        "function Bar() { Foo = null; Foo.prototype.baz = function() {}; }", null);
    try {
      pass.process(null, nodeTreeWithPrototypePropertiesAndNullDeepestCommonModule);
      fail("Expected exception");
    } catch (NullPointerException e) {
      // Expected exception, continue testing
    }
    assertEquals(3, pass.idGenerator.currentId);
  }
}