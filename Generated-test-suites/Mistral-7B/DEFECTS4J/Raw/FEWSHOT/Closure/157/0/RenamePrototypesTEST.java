package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private CompilerInput input;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler(new CompilerOptions());
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    input = new CompilerInput("test", "test.js", "");
  }

  @Test
  public void testProcess() {
    Node root = new Node("test", null, Arrays.asList(
        new Node("var prototype = { getBar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.getBar();", null)
    ));

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());
  }

  @Test
  public void testProcessNoProcess() {
    Node root = new Node("test", null, Arrays.asList(
        new Node("var prototype = { getBar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.getBar();", null),
        new Node("prototype.getBar = function() { return 2; }")
    ));

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());

    Node getBar = root.getLastChild();
    assertEquals(2, getBar.getInt());
  }

  @Test
  public void testProcessWithExterns() {
    Node externs = new Node("test", null, Arrays.asList(
        new Node("String.prototype.foo = function() { return 1; }")
    ));

    Node root = new Node("test", null, Arrays.asList(
        externs,
        new Node("var prototype = { getBar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.getBar();", null)
    ));

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());
  }

  @Test
  public void testProcessWithRenamedProperties() {
    Node externs = new Node("test", null, Arrays.asList(
        new Node("String.prototype.foo = function() { return 1; }")
    ));

    Node root = new Node("test", null, Arrays.asList(
        externs,
        new Node("var prototype = { get_bar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.get_bar();", null)
    ));

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());

    Node getBar = root.getLastChild();
    assertEquals(1, getBar.getInt());
  }

  @Test
  public void testProcessWithReservedNames() {
    Node externs = new Node("test", null, Arrays.asList(
        new Node("String.prototype.indexOf = function() { return 1; }")
    ));

    Node root = new Node("test", null, Arrays.asList(
        externs,
        new Node("var prototype = { getBar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.indexOf = function() { return 2; }", null),
        new Node("obj.getBar();", null)
    ));

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());

    Node getBar = root.getLastChild();
    assertEquals(1, getBar.getInt());

    Node indexOf = root.getChildAt(3);
    assertNull(indexOf);
  }

  @Test
  public void testProcessWithPropertyRenamingMap() {
    Node externs = new Node("test", null, Arrays.asList(
        new Node("String.prototype.foo = function() { return 1; }")
    ));

    Node root = new Node("test", null, Arrays.asList(
        externs,
        new Node("var prototype = { get_bar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.get_bar = function() { return 2; }", null)
    ));

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());

    VariableMap propertyRenamingMap = renamePrototypes.getPropertyMap();
    assertEquals("get_bar", propertyRenamingMap.lookupNewName("get_bar"));
    assertNull(propertyRenamingMap.lookupNewName("getBar"));
  }

  @Test
  public void testProcessWithReservedCharacters() {
    Node externs = new Node("test", null, Arrays.asList(
        new Node("String.prototype.indexOf = function() { return 1; }")
    ));

    Node root = new Node("test", null, Arrays.asList(
        externs,
        new Node("var prototype = { get_//bar: function() { return 1; } }", null),
        new Node("var obj = Object.create(prototype);", null),
        new Node("obj.get_//bar();", null)
    ));

    renamePrototypes = new RenamePrototypes(compiler, false, "\\/".toCharArray(), null);

    renamePrototypes.process(input.getNode(), root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());

    VariableMap propertyRenamingMap = renamePrototypes.getPropertyMap();
    assertNull(propertyRenamingMap.lookupNewName("get_//bar"));
  }
}