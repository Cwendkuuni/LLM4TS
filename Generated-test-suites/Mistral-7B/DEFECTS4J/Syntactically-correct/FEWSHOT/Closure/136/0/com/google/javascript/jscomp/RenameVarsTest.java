package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@RunWith(JUnit4.class)
public class RenameVarsTest {

  @Test
  public void testOkToRenameVar() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    assertNotNull(renameVars.okToRenameVar("_myVar", false));
    assertNotNull(renameVars.okToRenameVar("myVar", false));
    assertNotNull(renameVars.okToRenameVar("my_var", false));
    assertNotNull(renameVars.okToRenameVar("myVar_", false));
    assertNotNull(renameVars.okToRenameVar("my_var_", false));
    assertNotNull(renameVars.okToRenameVar("my_var__", false));
    assertNotNull(renameVars.okToRenameVar("myVar__", false));
    assertNotNull(renameVars.okToRenameVar("myVar_1", false));
    assertNotNull(renameVars.okToRenameVar("myVar_1_", false));
    assertNotNull(renameVars.okToRenameVar("myVar__1", false));
    assertNotNull(renameVars.okToRenameVar("myVar__1_", false));

    assertNull(renameVars.okToRenameVar("__myVar", false));
    assertNull(renameVars.okToRenameVar("myVar__", true));
    assertNull(renameVars.okToRenameVar("myVar__", false));
    assertNull(renameVars.okToRenameVar("myVar", "myVar"));
    assertNull(renameVars.okToRenameVar("myVar", "export myVar"));
  }

  @Test
  public void testProcessVars() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    Node root = Node.newCompilationUnit();
    NodeTraversal.traverse(renameVars, root, new ProcessVars(true));

    List<String> globals = new ArrayList<>();
    globals.add("foo");
    globals.add("bar");
    globals.add("baz");

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<String> expectedGlobals = new ArrayList<>();
    for (Node n : globalNameNodes) {
      expectedGlobals.add(n.getString());
    }
    assertEquals(globals, expectedGlobals);

    List<Node> localNameNodes = renameVars.localNameNodes;
    List<String> expectedLocals = new ArrayList<>();
    for (Node n : localNameNodes) {
      String name = n.getString();
      expectedLocals.add(LOCAL_VAR_PREFIX + (Integer.parseInt(name.substring(1))));
    }
    assertEquals(expectedLocals, localNameNodes);

    List<String> tempNames = renameVars.localTempNames;
    assertEquals(expectedLocals, tempNames);

    Set<String> reservedNames = renameVars.reservedNames;
    Set<String> expectedReservedNames = new HashSet<>(ImmutableSet.of("foo", "bar", "baz"));
    assertEquals(expectedReservedNames, reservedNames);
  }

  @Test
  public void testAssignNames() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    Set<Assignment> assignments = new TreeSet<>(RenameVars.FREQUENCY_COMPARATOR);
    assignments.add(new Assignment("foo", null));
    assignments.add(new Assignment("bar", null));
    assignments.add(new Assignment("baz", null));
    assignments.add(new Assignment("qux", null));

    List<String> expectedNames = new ArrayList<>();
    expectedNames.add("a");
    expectedNames.add("b");
    expectedNames.add("c");

    renameVars.assignNames(assignments);

    for (Assignment a : assignments) {
      String name = a.newName;
      assertNotNull(name);
      assertEquals(expectedNames.get(assignments.indexOf(a)), name);
    }
  }

  @Test
  public void testGetNewGlobalName() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    Node node = Node.newName("foo");
    String newName = renameVars.getNewGlobalName(node);
    assertNull(newName);

    node = Node.newName("L_0");
    newName = renameVars.getNewGlobalName(node);
    assertEquals("foo", newName);

    node = Node.newName("export foo");
    newName = renameVars.getNewGlobalName(node);
    assertNull(newName);

    node = Node.newName("export foo_");
    newName = renameVars.getNewGlobalName(node);
    assertEquals("foo", newName);
  }

  @Test
  public void testGetNewLocalName() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    Node node = Node.newName("foo");
    int index = 0;
    String newName = renameVars.getNewLocalName(node, index);
    assertNull(newName);

    node = Node.newName("L_0");
    newName = renameVars.getNewLocalName(node, index);
    assertEquals(LOCAL_VAR_PREFIX + index, newName);

    index = 1;
    newName = renameVars.getNewLocalName(node, index);
    assertEquals(LOCAL_VAR_PREFIX + index, newName);

    node = Node.newName("export foo");
    newName = renameVars.getNewLocalName(node, index);
    assertNull(newName);

    node = Node.newName("export foo_");
    newName = renameVars.getNewLocalName(node, index);
    assertEquals(LOCAL_VAR_PREFIX + index, newName);
  }

  @Test
  public void testReusePreviouslyUsedVariableMap() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    Map<String, String> prevUsedRenameMap = new HashMap<>();
    prevUsedRenameMap.put("foo", "bar");
    prevUsedRenameMap.put("baz", "qux");

    renameVars.reusePreviouslyUsedVariableMap();

    Set<String> reservedNames = renameVars.reservedNames;
    Set<String> expectedReservedNames = new HashSet<>(ImmutableSet.of("bar", "qux"));
    assertEquals(expectedReservedNames, reservedNames);
  }

  @Test
  public void testAssignNamesWithPreviouslyUsedVariableMap() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

    Map<String, String> prevUsedRenameMap = new LinkedHashMap<>();
    prevUsedRenameMap.put("foo", "bar");
    prevUsedRenameMap.put("baz", "qux");

    Set<Assignment> assignments = new TreeSet<>(RenameVars.FREQUENCY_COMPARATOR);
    assignments.add(new Assignment("foo", null));
    assignments.add(new Assignment("bar", null));
    assignments.add(new Assignment("baz", null));
    assignments.add(new Assignment("qux", null));

    renameVars.prevUsedRenameMap = prevUsedRenameMap;
    renameVars.assignNames(assignments);

    for (Assignment a : assignments) {
      String name = a.newName;
      assertNotNull(name);
      assertEquals("bar", name); // or "qux" if the order of assignments changes
    }
  }
}