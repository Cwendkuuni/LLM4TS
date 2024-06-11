import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  /**
   * Tests that the compiler correctly renames a simple prototype property.
   */
  @Test
  public void testSimplePrototypePropertyRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.foo = 1;\n" +
                    "Foo.prototype.bar = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, true, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype._foo_ = 1;\n" +
        "Foo.prototype._bar_ = function() {}\n");

    assertEquals(expected, ci.getRoot());
  }

  /**
   * Tests that the compiler doesn't rename a property with a leading underscore.
   */
  @Test
  public void testLeadingUnderscorePropertyRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype._foo = 1;\n" +
                    "Foo.prototype.bar = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype._foo = 1;\n" +
        "Foo.prototype.bar = function() {}\n");

    assertEquals(expected, ci.getRoot());

    // Make sure no name was added to the property map
    VariableMap propertyMap = renamePass.getPropertyMap();
    assertEquals(0, propertyMap.size());
  }

  /**
   * Tests that the compiler renames a property with a non-lowercase character.
   */
  @Test
  public void testNonLowercasePropertyRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.Foo = 1;\n" +
                    "Foo.prototype.bar = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype._Foo_ = 1;\n" +
        "Foo.prototype.bar = function() {}\n");

    assertEquals(expected, ci.getRoot());

    // Make sure the property was renamed
    VariableMap propertyMap = renamePass.getPropertyMap();
    Map<String, String> map = propertyMap.asMap();
    assertEquals("_Foo_", map.get("Foo"));
  }

  /**
   * Tests that the compiler doesn't rename a property with an uppercase character.
   */
  @Test
  public void testUppercasePropertyRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.FOO = 1;\n" +
                    "Foo.prototype.bar = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype.FOO = 1;\n" +
        "Foo.prototype.bar = function() {}\n");

    assertEquals(expected, ci.getRoot());

    // Make sure no name was added to the property map
    VariableMap propertyMap = renamePass.getPropertyMap();
    assertEquals(0, propertyMap.size());
  }

  /**
   * Tests that the compiler doesn't rename a method with a lowercase character.
   */
  @Test
  public void testLowercaseMethodRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.foo = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {}\n");

    assertEquals(expected, ci.getRoot());

    // Make sure no name was added to the property map
    VariableMap propertyMap = renamePass.getPropertyMap();
    assertEquals(0, propertyMap.size());
  }

  /**
   * Tests that the compiler doesn't rename a method with a single uppercase character.
   */
  @Test
  public void testSingleUppercaseMethodRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.Foo = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype.Foo = function() {}\n");

    assertEquals(expected, ci.getRoot());

    // Make sure no name was added to the property map
    VariableMap propertyMap = renamePass.getPropertyMap();
    assertEquals(0, propertyMap.size());
  }

  /**
   * Tests that the compiler renames a property with multiple non-lowercase characters.
   */
  @Test
  public void testMultipleUppercasePropertyRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.FOOBAZ = 1;\n" +
                    "Foo.prototype.bar = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype._FOOBAZ_ = 1;\n" +
        "Foo.prototype.bar = function() {}\n");

    assertEquals(expected, ci.getRoot());

    // Make sure the property was renamed
    VariableMap propertyMap = renamePass.getPropertyMap();
    Map<String, String> map = propertyMap.asMap();
    assertEquals("_FOOBAZ_", map.get("FOOBAZ"));
  }

  /**
   * Tests that the compiler doesn't rename a property with a reserved character.
   */
  @Test
  public void testReservedCharacterPropertyRenaming() {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.foo = function() {}\n" +
                    "Foo.prototype.indexOf = 1;\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, false, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {}\n" +
        "Foo.prototype.indexOf = 1;\n");

    assertEquals(expected, ci.getRoot());

    // Make sure no name was added to the property map
    VariableMap propertyMap = renamePass.getPropertyMap();
    assertEquals(0, propertyMap.size());
  }

  /**
   * Tests that the compiler correctly renames a prototype property with a different name when there is
   * both a prototype property and an object literal property with the same name.
   */
  @Test
  public void testPrototypeAndObjLitPropertyRenaming() throws IOException {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.foo = 1;\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "var o = { foo: 2, bar: function() {}};\n";

    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, true, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype._foo_ = 1;\n" +
        "Foo.prototype._bar_ = function() {}\n" +
        "var o = { _foo__: 2, _bar_: function() {}};\n");

    assertEquals(expected, ci.getRoot());
  }

  /**
   * Tests that the compiler correctly renames a prototype property and an object literal property with
   * the same name when there is only an object literal property.
   */
  @Test
  public void testObjLitPropertyRenaming() throws IOException {
    String input = "var o = { foo: 1, bar: function() {}};\n";

    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, true, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "var o = { _foo_: 1, _bar_: function() {}};\n");

    assertEquals(expected, ci.getRoot());
  }

  /**
   * Tests that the compiler doesn't rename a prototype property and an object literal property with
   * the same name when there is only a prototype property.
   */
  @Test
  public void testPrototypePropertyRenaming() throws IOException {
    String input = "function Foo() {}\n" +
                    "Foo.prototype.foo = 1;\n";

    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);
    Compiler compiler = new Compiler(options);

    NodeFactory factory = new NodeFactory();
    TokenStream ts = new TokenStream(factory, new StringReader(input));
    CompilerInput ci = new CompilerInput("Test", ts);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, ci);
    RenamePrototypes renamePass = new RenamePrototypes(abstractCompiler, true, null, null);
    renamePass.process(ci.getRoot(), ci.getExterns());

    Node expected = factory.parseTree(
        "function Foo() {}\n" +
        "Foo.prototype.foo = 1;\n");

    assertEquals(expected, ci.getRoot());
  }
}