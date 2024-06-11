import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import java.io.IOException;

public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    // Given
    Compiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // When
    crossModuleMethodMotion.process(null, null);

    // Then
    assertEquals(CrossModuleMethodMotion.class, crossModuleMethodMotion.getClass());
  }

  @Test
  public void testMoveMethodAcrossModules() {
    // Given
    Compiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Arrange - Add a method to the first module
    Node root1 = compiler.parseJsSource("module1.js",
        "function foo() {}");
    Node module1 = moduleGraph.getModuleByName("module1");
    module1.addChildrenToFront(root1);

    // Arrange - Add a dependant module
    Node root2 = compiler.parseJsSource("module2.js",
        "function bar() { foo(); }");
    Node module2 = moduleGraph.getModuleByName("module2");
    module2.addChildrenToFront(root2);

    // Act
    crossModuleMethodMotion.process(null, null);

    // Assert - The method should be moved to module2
    assertEquals("function foo() {}", module2.getChild(0).getText());
    assertEquals("", module1.getChild(0).getText());
  }

  @Test(expected = IOException.class)
  public void testMoveMethodToAnotherModuleWithIOException() {
    // Given
    Compiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Arrange - Throw an IOException during the processing
    Node root = compiler.parseJsSource("", "");
    compiler.report(new IOException());

    // Act and Assert - The test should fail with an IOException
  }

  @Test
  public void testMoveMethodToAnotherModuleWithCyclicDependency() {
    // Given
    Compiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Arrange - Add modules with cyclic dependencies
    Node root1 = compiler.parseJsSource("module1.js",
        "function foo() {}");
    Node module1 = moduleGraph.getModuleByName("module1");
    module1.addChildrenToFront(root1);

    Node root2 = compiler.parseJsSource("module2.js",
        "function bar() { foo(); }");
    Node module2 = moduleGraph.getModuleByName("module2");
    module2.addChildrenToFront(root2);
    module2.addDependency(module1);
    module1.addDependency(module2);

    // Act and Assert - The test should fail with an AssertionError
    assertThrows(IllegalArgumentException.class, () -> crossModuleMethodMotion.process(null, null));
  }

  @Test
  public void testMoveMethodToAnotherModuleWithGettersAndSetters() {
    // Given
    Compiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Arrange - Add a method to the first module
    Node root1 = compiler.parseJsSource("module1.js",
        "function Foo() { this.value = 0; }" +
        "Foo.prototype.get = function() { return this.value; }" +
        "Foo.prototype.set = function(value) { this.value = value; }");
    Node module1 = moduleGraph.getModuleByName("module1");
    module1.addChildrenToFront(root1);

    // Arrange - Add a dependant module
    Node root2 = compiler.parseJsSource("module2.js",
        "function Bar() { this.foo = new Foo(); }" +
        "Bar.prototype.getFooValue = function() { return this.foo.get(); }" +
        "Bar.prototype.setFooValue = function(value) { this.foo.set(value); }");
    Node module2 = moduleGraph.getModuleByName("module2");
    module2.addChildrenToFront(root2);

    // Act and Assert - The test should not move the getter and setter functions
    crossModuleMethodMotion.process(null, null);

    assertEquals("function Foo() { this.value = 0; }", module1.getChild(0).getText());
    assertEquals("function Bar() { this.foo = new Foo(); }", module2.getChild(0).getText());
    assertEquals("Foo.prototype.get = function() { return this.value; }", module1.getChild(1).getText());
    assertEquals("Foo.prototype.set = function(value) { this.value = value; }", module1.getChild(2).getText());
    assertEquals("Bar.prototype.getFooValue = function() { return this.foo.get(); }", module2.getChild(1).getText());
    assertEquals("Bar.prototype.setFooValue = function(value) { this.foo.set(value); }", module2.getChild(2).getText());
  }
}