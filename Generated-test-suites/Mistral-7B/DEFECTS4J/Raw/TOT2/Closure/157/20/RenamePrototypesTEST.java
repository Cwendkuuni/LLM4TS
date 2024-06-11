import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperty() {
    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {};";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);
    Node root = input.getTree();

    // Check that the property was renamed
    Property barProperty = pass.properties.get("bar");
    assertEquals("The property 'bar' was not renamed", "renamedBar", barProperty.newName);
  }

  @Test
  public void testDontRenamePrototypePropertyWithLeadingUnderscore() {
    String sourceCode = "function Foo() {} Foo.prototype._bar = function() {};";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);
    Node root = input.getTree();

    // Check that the property was not renamed
    Property barProperty = pass.properties.get("_bar");
    assertEquals("The property '_bar' was renamed", "_bar", barProperty.newName);
  }

  @Test
  public void testRenameObjLitProperty() {
    String sourceCode = "var obj = { bar: 1 };";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);
    Node root = input.getTree();

    // Check that the property was renamed
    Property barProperty = pass.properties.get("bar");
    assertEquals("The property 'bar' was not renamed", "renamedBar", barProperty.newName);
  }

  @Test(expected = AssertionError.class)
  public void testDontRenameObjLitPropertyWithQuotedKey() {
    String sourceCode = "var obj = {'bar': 1 };";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);

    // This test should fail because the obj lit property with a quoted key
    // should not be renamed
  }

  @Test
  public void testRenamePrototypeAndObjLitProperty() {
    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {}; var obj = { bar: 1 };";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);
    Node root = input.getTree();

    // Check that the prototype property was renamed
    Property barProperty = pass.properties.get("bar");
    assertEquals("The prototype property 'bar' was not renamed", "renamedBar", barProperty.newName);

    // Check that the obj lit property was not renamed
    Property objBarProperty = pass.properties.get("bar");
    assertEquals("The obj lit property 'bar' was renamed", "bar", objBarProperty.newName);
  }

  @Test(expected = AssertionError.class)
  public void testRenameExternedProperty() {
    String sourceCode = "function Foo() { this.bar = 1; } Foo.prototype.bar = function() {};";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);

    // This test should fail because an externed property should not be renamed
  }

  @Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    String sourceCode = "function Foo() {} Foo.prototype.My_Variable = function() {};";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test with aggressive renaming
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);

    // Compile the code
    compiler.compile(input);
    Node root = input.getTree();

    // Check that the property was renamed
    Property myVariableProperty = pass.properties.get("My_Variable");
    assertEquals("The property 'My_Variable' was not renamed", "renamedMyVariable", myVariableProperty.newName);
  }

  @Test(expected = AssertionError.class)
  public void testRenameBuiltInProperty() {
    String sourceCode = "function Foo() { this.indexOf = function() {}; }";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);

    // This test should fail because built-in properties should not be renamed
  }

  @Test
  public void testRenamePrototypePropertyWithPrivateName() {
    String sourceCode = "function Foo() { var _bar = function() {}; } Foo.prototype.bar = function() {};";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    CompilerOptions options = CompilerOptions.defaults();

    // Set up the test
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    // Compile the code
    compiler.compile(input);
    Node root = input.getTree();

    // Check that the property was not renamed
    Property barProperty = pass.properties.get("bar");
    assertEquals("The property 'bar' was renamed", "bar", barProperty.newName);
  }
}