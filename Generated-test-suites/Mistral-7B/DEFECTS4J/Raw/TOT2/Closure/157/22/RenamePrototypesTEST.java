import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerResult.Type;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

/**
 * Test class for RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.bar = function() {}"
        + "Foo.prototype.baz = function() {}"
        + "var x = { get foo() { return 1; } };"
        + "x.prototype.qux = function() {}";

    CompilerInput input = new CompilerInput("test", jsCode, "Test");
    CompilerOptions options = new CompilerOptions(null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    CompilerResult result = compiler.process(input, options, new CompilerPass[] {renamePrototypes});

    assertEquals(Type.ERROR, result.getResultCode());
    assertNotNull(result.errorList);
    assertEquals(3, result.errorList.size());

    // Test errors for non-renamable properties
    assertEquals("Cannot rename property 'prototype' of prototype 'Foo'",
        result.errorList.get(0).getMessage());
    assertEquals("Method 'toString' does not need to be renamed",
        result.errorList.get(1).getMessage());
    assertEquals("Method 'valueOf' does not need to be renamed",
        result.errorList.get(2).getMessage());

    // Test successful renaming
    jsCode = "function Foo() {}"
        + "Foo.prototype._bar = function() {}" // Rename by user
        + "Foo.prototype._baz = function() {}"
        + "var x = { get _foo() { return 1; } };"
        + "x.prototype._qux = function() {}";

    input = new CompilerInput("test", jsCode, "Test");
    result = compiler.process(input, options, new CompilerPass[] {renamePrototypes});

    assertEquals(Type.OK, result.getResultCode());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("_bar", propertyMap.lookupNewName("bar"));
    assertEquals("_baz", propertyMap.lookupNewName("baz"));
    assertEquals("_foo", propertyMap.lookupNewName("foo"));
    assertEquals("_qux", propertyMap.lookupNewName("qux"));
  }

  @Test
  public void testAggressiveRenaming() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.bar = function() {}"
        + "Foo.prototype.baz = function() {}"
        + "var x = { get foo() { return 1; } };"
        + "x.prototype.qux = function() {}";

    CompilerInput input = new CompilerInput("test", jsCode, "Test");
    CompilerOptions options = new CompilerOptions(null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.process(input, options, new CompilerPass[] {renamePrototypes});

    assertEquals(Type.OK, result.getResultCode());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    String newName;

    newName = propertyMap.lookupNewName("prototype.bar");
    assertEquals("_$Renamed_bar", newName);

    newName = propertyMap.lookupNewName("prototype.baz");
    assertEquals("_$Renamed_baz", newName);

    newName = propertyMap.lookupNewName("x.foo");
    assertEquals("_$Renamed_foo", newName);

    newName = propertyMap.lookupNewName("x.qux");
    assertEquals("_$Renamed_qux", newName);
  }

  @Test
  public void testEdgeCases() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.get _bar = function() {}" // Leading underscore
        + "Foo.prototype.get_bar = function() {}" // Trailing underscore
        + "Foo.prototype.foo_bar = function() {}" // Mixed underscores
        + "Foo.prototype.fooBar = function() {}" // CamelCase
        + "Foo.prototype._FOO_BAR_ = function() {}" // Uppercase in the middle
        + "Foo.prototype.FOO_BAR_ = function() {}" // Uppercase at the beginning
        + "var x = { get _foo_bar() { return 1; } };"
        + "x.prototype._FOO_BAR = function() {}"
        + "x.prototype.FOO_BAR = function() {}";

    CompilerInput input = new CompilerInput("test", jsCode, "Test");
    CompilerOptions options = new CompilerOptions(null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    CompilerResult result = compiler.process(input, options, new CompilerPass[] {renamePrototypes});

    assertEquals(Type.OK, result.getResultCode());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    String newName;

    newName = propertyMap.lookupNewName("prototype.get _bar");
    assertEquals("_$Renamed__bar", newName);

    newName = propertyMap.lookupNewName("prototype.get_bar");
    assertEquals("_$Renamed_getbar", newName);

    newName = propertyMap.lookupNewName("prototype.foo_bar");
    assertEquals("_$Renamed_foo_bar", newName);

    newName = propertyMap.lookupNewName("prototype.fooBar");
    assertEquals("_$Renamed_foobar", newName);

    newName = propertyMap.lookupNewName("prototype._FOO_BAR_");
    assertEquals("_$Renamed__FOO__BAR_", newName);

    newName = propertyMap.lookupNewName("prototype.FOO_BAR_");
    assertEquals("_$Renamed_FOO_BAR_", newName);

    newName = propertyMap.lookupNewName("x.get _foo_bar");
    assertEquals("_$Renamed__foo_bar", newName);

    newName = propertyMap.lookupNewName("x.get _FOO_BAR");
    assertEquals("_$Renamed__FOO_BAR", newName);

    newName = propertyMap.lookupNewName("x.prototype._FOO_BAR");
    assertEquals("_$Renamed__FOO_BAR", newName);

    newName = propertyMap.lookupNewName("x.prototype.FOO_BAR");
    assertEquals("_$Renamed_FOO_BAR", newName);
  }

  @Test
  public void testReservedNames() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.indexOf = function() {}"
        + "Foo.prototype.lastIndexOf = function() {}"
        + "Foo.prototype.toString = function() {}"
        + "Foo.prototype.valueOf = function() {}";

    CompilerInput input = new CompilerInput("test", jsCode, "Test");
    CompilerOptions options = new CompilerOptions(null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    CompilerResult result = compiler.process(input, options, new CompilerPass[] {renamePrototypes});

    assertEquals(Type.OK, result.getResultCode());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("indexOf", propertyMap.lookupNewName("prototype.indexOf"));
    assertEquals("lastIndexOf", propertyMap.lookupNewName("prototype.lastIndexOf"));
    assertEquals("toString", propertyMap.lookupNewName("prototype.toString"));
    assertEquals("valueOf", propertyMap.lookupNewName("prototype.valueOf"));
  }

  @Test
  public void testNoRenaming() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.foo = function() {}";

    CompilerInput input = new CompilerInput("test", jsCode, "Test");
    CompilerOptions options = new CompilerOptions(null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    CompilerResult result = compiler.process(input, options, new CompilerPass[] {renamePrototypes});

    assertEquals(Type.OK, result.getResultCode());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("foo", propertyMap.lookupNewName("prototype.foo"));
  }
}