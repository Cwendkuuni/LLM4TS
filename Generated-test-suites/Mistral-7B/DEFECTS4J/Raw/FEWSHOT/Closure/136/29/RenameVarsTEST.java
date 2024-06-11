package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String prefix = "test_";
    Set<String> reservedNames = new HashSet<>();
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = new char[]{'.'};

    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);

    // Test empty input
    Node externs = null;
    Node root = null;
    renameVars.process(externs, root);
    assertTrue(renameVars.assignments.isEmpty());

    // Test with externs
    externs = createNode("extern1");
    root = createNode("function test() {}");
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test_extern1", renameVars.getVariableMap().lookupName("extern1"));

    // Test with locals
    externs = createNode("extern1");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, c = 3;" +
        "}");
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test_a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test_b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test_c", renameVars.getVariableMap().lookupName("c"));

    // Test with local and global
    externs = createNode("extern1");
    root = createNode("function test() {" +
        "  var a = 1, b = 2;" +
        "  function foo() {}" +
        "}");
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test_a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test_b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test_foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2;" +
        "  function foo() {}" +
        "}");
    renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test__a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test__b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test__foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision and reserved names
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, x = 3;" +
        "  function foo() {}" +
        "}");
    renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test__a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test__b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test__x", renameVars.getVariableMap().lookupName("x"));
    assertEquals("test__foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision, reserved names, and collision
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, x = 3, x = 4;" +
        "  function foo() {}" +
        "}");
    renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test___a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test___b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test__x", renameVars.getVariableMap().lookupName("x"));
    assertEquals("test__foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision, reserved names, and collision, and previous used map
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, x = 3, x = 4;" +
        "  function foo() {}" +
        "}");
    VariableMap prevUsedRenameMap = new VariableMap( Arrays.asList(Arrays.asList("a", "test_a"), Arrays.asList("x", "test_x")) );
    renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test___a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test___b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test___x", renameVars.getVariableMap().lookupName("x"));
    assertEquals("test__foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision, reserved names, collision, and previous used map with collision
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, x = 3, x = 4, y = 5;" +
        "  function foo() {}" +
        "}");
    VariableMap prevUsedRenameMap = new VariableMap( Arrays.asList(Arrays.asList("a", "test_a"), Arrays.asList("x", "test_x"), Arrays.asList("y", "test_y")) );
    renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test___a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test___b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test___x", renameVars.getVariableMap().lookupName("x"));
    assertEquals("test___y", renameVars.getVariableMap().lookupName("y"));
    assertEquals("test__foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision, reserved names, collision, previous used map with collision, and local renaming only
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, x = 3, x = 4, y = 5;" +
        "  function foo() {}" +
        "}");
    VariableMap prevUsedRenameMap = new VariableMap( Arrays.asList(Arrays.asList("a", "test_a"), Arrays.asList("x", "test_x"), Arrays.asList("y", "test_y")) );
    renameVars = new RenameVars(null, prefix, true, false, false, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test___a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test___b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test_L0_x", renameVars.getVariableMap().lookupName("x"));
    assertEquals("test___y", renameVars.getVariableMap().lookupName("y"));
    assertEquals("test__foo", renameVars.getVariableMap().lookupName("foo"));

    // Test with local and global with prefix collision, reserved names, collision, previous used map with collision, and local renaming only and generate pseudo names
    externs = createNode("extern1_");
    root = createNode("function test() {" +
        "  var a = 1, b = 2, x = 3, x = 4, y = 5;" +
        "  function foo() {}" +
        "}");
    VariableMap prevUsedRenameMap = new VariableMap( Arrays.asList(Arrays.asList("a", "test_a"), Arrays.asList("x", "test_x"), Arrays.asList("y", "test_y"), Arrays.asList("$foo", "foo")) );
    renameVars = new RenameVars(null, prefix, true, false, true, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(externs, root);
    assertFalse(renameVars.assignments.isEmpty());
    assertEquals("test___a", renameVars.getVariableMap().lookupName("a"));
    assertEquals("test___b", renameVars.getVariableMap().lookupName("b"));
    assertEquals("test_L0_$x", renameVars.getVariableMap().lookupName("x"));
    assertEquals("test___y", renameVars.getVariableMap().lookupName("y"));
    assertEquals("test_$foo", renameVars.getVariableMap().lookupName("foo"));
  }

  private Node createNode(String name) {
    Node node = new Node();
    node.setString(name);
    return node;
  }

  private static class Node {
    private int type;
    private String string;

    public void setString(String string) {
      this.string = string;
    }

    public String getString() {
      return string;
    }
  }
}