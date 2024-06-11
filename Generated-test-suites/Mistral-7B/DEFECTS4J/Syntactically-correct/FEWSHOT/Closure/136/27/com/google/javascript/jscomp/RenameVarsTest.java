package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testOkToRenameVar() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    // Test local variables
    assertTrue(renamer.okToRenameVar("a", true));
    assertTrue(renamer.okToRenameVar("_a", true));
    assertFalse(renamer.okToRenameVar("export", true));
    assertFalse(renamer.okToRenameVar("export default", true));

    // Test global variables
    assertTrue(renamer.okToRenameVar("a", false));
    assertTrue(renamer.okToRenameVar("_a", false));
    assertFalse(renamer.okToRenameVar("export", false));
    assertFalse(renamer.okToRenameVar("export default", false));
    assertFalse(renamer.okToRenameVar("__RESERVED__", false));
  }

  @Test
  public void testProcessVars() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Node root = makeNode("function test() { var a = 1; }");

    NodeTraversal traversal = new NodeTraversal(root, new TraversalContext(), renamer);
    ProcessVars processVars = new ProcessVars(false);
    NodeTraversal.traverse(renamer.compiler, root, processVars);

    List<Node> globalNameNodes = renamer.globalNameNodes;
    List<Node> localNameNodes = renamer.localNameNodes;
    List<String> localTempNames = renamer.localTempNames;

    assertEquals(0, globalNameNodes.size());
    assertEquals(1, localNameNodes.size());
    assertEquals(1, localTempNames.size());

    assertEquals("a", localNameNodes.get(0).getString());
    assertEquals("L 0", localTempNames.get(0));

    assertEquals(0, processVars.assignments.size());
  }

  @Test
  public void testProcessVarsWithExterns() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    CompilerInput input1 = new CompilerInput("test1.js", 1, 1);
    Node externs1 = makeNode("var a = 1;");

    CompilerInput input2 = new CompilerInput("test2.js", 1, 1);
    Node root2 = makeNode("function test() { var a = 1; }");

    // Collect global variables
    NodeTraversal traversal1 = new NodeTraversal(externs1, new TraversalContext(), renamer);
    ProcessVars processVars1 = new ProcessVars(true);
    NodeTraversal.traverse(renamer.compiler, externs1, processVars1);

    // Collect local variables
    NodeTraversal.traverse(renamer.compiler, root2, processVars1);

    Set<String> externNames = renamer.externNames;
    List<Node> globalNameNodes = renamer.globalNameNodes;
    List<Node> localNameNodes = renamer.localNameNodes;
    List<String> localTempNames = renamer.localTempNames;

    assertEquals(1, externNames.size());
    assertEquals(0, globalNameNodes.size());
    assertEquals(1, localNameNodes.size());
    assertEquals(1, localTempNames.size());

    assertEquals("a", localNameNodes.get(0).getString());
    assertEquals("L 0", localTempNames.get(0));

    assertEquals("a", externNames.iterator().next());

    assertEquals(0, processVars1.assignments.size());
  }

  @Test
  public void testAssignNames() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    Set<String> reservedNames = renamer.reservedNames;
    List<Assignment> pendingAssignments = new ArrayList<>();
    List<String> generatedNamesForAssignments = new ArrayList<>();

    // Assign names to variables
    renamer.assignNames(Collections.emptyList());

    assertEquals(0, generatedNamesForAssignments.size());
    assertEquals(0, pendingAssignments.size());
    assertEquals(0, renamer.renameMap.size());

    // Assign names to variables with same length
    String name1 = "a";
    String name2 = "b";
    String name3 = "c";
    String name4 = "d";
    String name5 = "e";

    Assignment assignment1 = new Assignment(name1, null);
    Assignment assignment2 = new Assignment(name2, null);
    Assignment assignment3 = new Assignment(name3, null);
    Assignment assignment4 = new Assignment(name4, null);
    Assignment assignment5 = new Assignment(name5, null);

    pendingAssignments.add(assignment1);
    pendingAssignments.add(assignment2);
    pendingAssignments.add(assignment3);
    pendingAssignments.add(assignment4);
    pendingAssignments.add(assignment5);

    generatedNamesForAssignments.add("x");
    generatedNamesForAssignments.add("y");
    generatedNamesForAssignments.add("z");

    renamer.assignNames(Collections.singleton(assignment1));

    assertEquals("x", renamer.renameMap.get(name1));

    assertEquals(1, generatedNamesForAssignments.size());
    assertEquals(1, pendingAssignments.size());

    renamer.assignNames(Collections.singleton(assignment2));

    assertEquals("y", renamer.renameMap.get(name2));

    assertEquals(1, generatedNamesForAssignments.size());
    assertEquals(0, pendingAssignments.size());

    // Assign names to variables with different length
    String name6 = "foo";
    String name7 = "bar";

    Assignment assignment6 = new Assignment(name6, null);
    Assignment assignment7 = new Assignment(name7, null);

    pendingAssignments.add(assignment6);
    pendingAssignments.add(assignment7);

    generatedNamesForAssignments.add("w");
    generatedNamesForAssignments.add("v");

    renamer.assignNames(Arrays.asList(assignment6, assignment7));

    assertEquals("w", renamer.renameMap.get(name6));
    assertEquals("v", renamer.renameMap.get(name7));

    assertEquals(2, renamer.renameMap.size());
    assertEquals(0, pendingAssignments.size());
  }

  @Test
  public void testGetNewGlobalName() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    String oldName = "a";

    String newName = renamer.getNewGlobalName(makeNode(oldName));

    assertNull(newName);
  }

  @Test
  public void testGetNewLocalName() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    int index = 0;
    String oldTempName = "L " + index;

    String newName = renamer.getNewLocalName(makeNode(oldTempName.substring(1)), index);

    assertNull(newName);
  }

  @Test
  public void testGetNewLocalNameWithIndexIncremented() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    int index = 0;
    String oldTempName = "L " + index;

    String newName = renamer.getNewLocalName(makeNode(oldTempName.substring(1)), ++index);

    assertNotNull(newName);
    assertEquals("L " + index, newName);
  }

  @Test
  public void testGetNewLocalNameWithSameIndex() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    int index = 0;
    String oldTempName = "L " + index;

    String newName = renamer.getNewLocalName(makeNode(oldTempName.substring(1)), index);

    assertSame(oldTempName, newName);
  }

  @Test
  public void testFinalizeNameAssignment() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    String oldName = "a";
    String newName = "x";

    Assignment assignment = new Assignment(oldName, null);

    renamer.finalizeNameAssignment(assignment, newName);

    assertEquals(newName, renamer.renameMap.get(oldName));
  }

  @Test
  public void testGetVariableMap() {
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

    String oldName1 = "a";
    String oldName2 = "b";
    String oldName3 = "c";

    String newName1 = "x";
    String newName2 = "y";
    String newName3 = "z";

    renamer.renameMap.put(oldName1, newName1);
    renamer.renameMap.put(oldName2, newName2);
    renamer.renameMap.put(oldName3, newName3);

    VariableMap variableMap = renamer.getVariableMap();

    assertEquals(3, variableMap.size());

    assertEquals(newName1, variableMap.get(oldName1));
    assertEquals(newName2, variableMap.get(oldName2));
    assertEquals(newName3, variableMap.get(oldName3));
  }

  private Node makeNode(String nodeString) {
    return new Node() {
      @Override
      public String getString() {
        return nodeString;
      }

      @Override
      public int getType() {
        return Token.NAME;
      }
    };
  }
}