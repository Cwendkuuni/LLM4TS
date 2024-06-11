package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private Node root;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);

    TokenStream input = new TokenStream(new StringBuilder("function Foo() {} "
        + "Foo.prototype.bar = function() {}; "
        + "Foo.prototype.baz = 42; "
        + "var obj = { foo: 42, Foo: {} }; "
        + "Foo.prototype = obj; "
        + "function Foo2() {} "));
    root = compiler.parseInitialSource(input, "test");
  }

  @Test
  public void testNoRenamingForExternals() {
    // Create a test pass that does not rename.
    RenamePrototypes noRenaming = new RenamePrototypes(compiler, false, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(noRenaming);

    // Run the pass and check that no renaming occurred.
    compiler.process(root, null);
    VariableMap propertyMap = noRenaming.getPropertyMap();
    assertEquals("bar", propertyMap.lookupName("Foo.prototype.bar"));
    assertEquals("baz", propertyMap.lookupName("Foo.prototype.baz"));
    assertEquals("foo", propertyMap.lookupName("obj.foo"));
    assertNull(propertyMap.lookupName("Foo.prototype"));
  }

  @Test
  public void testSimpleRenaming() {
    // Create a test pass that renames.
    RenamePrototypes renaming = new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(renaming);

    // Run the pass and check that renaming occurred.
    compiler.process(root, null);
    VariableMap propertyMap = renaming.getPropertyMap();
    assertSame("bar1", propertyMap.lookupName("Foo.prototype.bar"));
    assertSame("baz1", propertyMap.lookupName("Foo.prototype.baz"));
    assertNull(propertyMap.lookupName("Foo.prototype"));
  }

  @Test
  public void testPropertyRenamingWithSameNameInPrototypeAndObjectLit() {
    // Create a test pass that renames.
    RenamePrototypes renaming = new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(renaming);

    // Run the pass and check that renaming occurred.
    compiler.process(root, null);
    VariableMap propertyMap = renaming.getPropertyMap();
    assertSame("bar1", propertyMap.lookupName("Foo.prototype.bar"));
    assertSame("baz1", propertyMap.lookupName("Foo.prototype.baz"));
    assertNull(propertyMap.lookupName("Foo.prototype"));
    assertSame("foo1", propertyMap.lookupName("obj.foo"));
  }

  @Test
  public void testRenamingWithAggressiveOption() {
    // Create a test pass that renames aggressively.
    RenamePrototypes aggressiveRenaming =
        new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(aggressiveRenaming);

    // Run the pass and check that renaming occurred.
    compiler.process(root, null);
    VariableMap propertyMap = aggressiveRenaming.getPropertyMap();
    assertSame("bar1_", propertyMap.lookupName("Foo.prototype.bar"));
    assertSame("baz1_", propertyMap.lookupName("Foo.prototype.baz"));
    assertNull(propertyMap.lookupName("Foo.prototype"));
    assertSame("foo1_", propertyMap.lookupName("obj.foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReservedCharacters() {
    // Create a test pass with null reserved characters.
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // This should throw an IllegalArgumentException.
    renamePrototypes.process(root, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyReservedCharacters() {
    // Create a test pass with an empty array of reserved characters.
    char[] reservedCharacters = new char[0];
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true,
        reservedCharacters, null);

    // This should throw an IllegalArgumentException.
    renamePrototypes.process(root, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPrevUsedRenameMap() {
    // Create a test pass with a null previous used rename map.
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true,
        null, null);

    // This should throw an IllegalArgumentException.
    renamePrototypes.process(root, null);
  }

  @Test
  public void testReusingPreviousNames() {
    // Create a test pass that reuses previous names.
    Map<String, String> previousRenames = Maps.newHashMap();
    previousRenames.put("Foo.prototype.bar", "bar_");
    VariableMap prevUsedRenameMap = new VariableMap(previousRenames);

    RenamePrototypes renaming = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(renaming);

    // Run the pass and check that the previous names were reused.
    compiler.process(root, null);
    VariableMap propertyMap = renaming.getPropertyMap();
    assertSame("bar_", propertyMap.lookupName("Foo.prototype.bar"));
  }

  @Test
  public void testRenamingWithCustomPrototypeName() {
    // Create a test pass that renames.
    RenamePrototypes renaming = new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(renaming);

    // Run the pass and check that renaming occurred.
    compiler.process(root, null);
    VariableMap propertyMap = renaming.getPropertyMap();
    assertSame("Bar_", propertyMap.lookupName("Foo.prototype"));
  }

  @Test
  public void testRenamingWithReservedName() {
    // Create a test pass that renames with a reserved name.
    RenamePrototypes renaming = new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(renaming);

    // Run the pass and check that no renaming occurred for a reserved name.
    compiler.process(root, null);
    VariableMap propertyMap = renaming.getPropertyMap();
    assertNull(propertyMap.lookupName("indexOf"));
  }

  @Test
  public void testRenamingWithAmbiguousName() {
    // Create a test pass that renames with an ambiguous name.
    TokenStream input = new TokenStream(new StringBuilder("function Foo() {} "
        + "Foo.prototype.bar = function() {}; "
        + "Foo.prototype.bar = function() {};"));
    Node ambiguousNode = compiler.parseInitialSource(input, "test");

    RenamePrototypes renaming = new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(options);
    passManager.addPass(renaming);

    // Run the pass and check that no renaming occurred for the ambiguous name.
    compiler.process(ambiguousNode, null);
    VariableMap propertyMap = renaming.getPropertyMap();
    assertSame("bar", propertyMap.lookupName("Foo.prototype.bar"));
    assertSame("bar", propertyMap.lookupName("Foo.prototype.bar"));
  }
}