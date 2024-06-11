package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;

public class RenameVarsTest {

  @Test
  public void testProcessVars() {
    RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, null);
    ProcessVars processVars = new ProcessVars(true);

    Node externs = Node.newObject();
    Node root = Node.newProgram();

    NodeTraversal.traverse(renameVars.compiler, externs, processVars);
    NodeTraversal.traverse(renameVars.compiler, root, processVars);

    Node[] globalNameNodesExpected = {
        Node.newName("a"),
        Node.newName("b"),
        Node.newName("c")
    };

    Node[] localNameNodesExpected = {
        Node.newVar("x", Node.OP_ASSIGN, Node.newName("x"), Node.newInt(1)),
        Node.newVar("y", Node.OP_ASSIGN, Node.newName("y"), Node.newInt(2)),
        Node.newFunction(Node.newName("f"), new Node[]{}, Node.newBlock(2))
    };

    assertEquals(Arrays.asList(globalNameNodesExpected), renameVars.globalNameNodes);
    assertEquals(Arrays.asList(localNameNodesExpected), renameVars.localNameNodes);
  }

  @Test
  public void testProcessVarsExternsOnly() {
    RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, null);
    ProcessVars processVars = new ProcessVars(true);

    Node externs = Node.newObject();
    Node root = Node.newProgram();

    Node nameNodeA = Node.newName("a");
    Node nameNodeB = Node.newName("b");
    Node nameNodeC = Node.newName("c");
    Node nameNodeD = Node.newName("d");

    externs.addChild(nameNodeA);
    externs.addChild(nameNodeB);
    externs.addChild(nameNodeC);

    root.addChild(externs);
    root.addChild(Node.newVar("x", Node.OP_ASSIGN, Node.newName("x"), Node.newInt(1)));
    root.addChild(Node.newVar("y", Node.OP_ASSIGN, Node.newName("y"), Node.newInt(2)));

    NodeTraversal.traverse(renameVars.compiler, externs, processVars);
    NodeTraversal.traverse(renameVars.compiler, root, processVars);

    Set<String> externNamesExpected = Sets.newHashSet("a", "b", "c");

    assertEquals(externNamesExpected, renameVars.externNames);
  }

  @Test
  public void testAssignNames() {
    RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, null);

    List<Assignment> assignments = new ArrayList<Assignment>();
    Assignment assignmentA = new Assignment("a", null);
    Assignment assignmentB = new Assignment("b", null);
    Assignment assignmentC = new Assignment("c", null);
    Assignment assignmentD = new Assignment("d", null);
    Assignment assignmentE = new Assignment("e", null);

    assignments.add(assignmentA);
    assignments.add(assignmentB);
    assignments.add(assignmentC);
    assignments.add(assignmentD);
    assignments.add(assignmentE);

    renameVars.assignNames(assignments);

    Map<String, String> renameMapExpected = new LinkedHashMap<String, String>() {{
      put("a", "a_1");
      put("b", "b_1");
      put("c", "c_1");
      put("d", "d_1");
      put("e", "e_1");
    }};

    assertEquals(renameMapExpected, renameVars.renameMap);
  }

  @Test
  public void testAssignNamesWithPreviouslyUsedVariableMap() {
    RenameVars renameVars = new RenameVars(null, null, false, false, false, new VariableMap(Maps.newHashMap()), null, null);
    ProcessVars processVars = new ProcessVars(true);

    Node externs = Node.newObject();
    Node root = Node.newProgram();

    // Externs.
    Node nameNodeA = Node.newName("a");
    Node nameNodeB = Node.newName("b");
    Node nameNodeC = Node.newName("c");
    externs.addChild(nameNodeA);
    externs.addChild(nameNodeB);
    externs.addChild(nameNodeC);

    // Source.
    Node nameNodeD = Node.newName("d");
    Node nameNodeE = Node.newName("e");
    Node nameNodeF = Node.newName("f");
    Node nameNodeG = Node.newName("g");
    root.addChild(externs);
    root.addChild(Node.newVar("x", Node.OP_ASSIGN, Node.newName("x"), Node.newInt(1)));
    root.addChild(Node.newVar("y", Node.OP_ASSIGN, Node.newName("y"), Node.newInt(2)));
    root.addChild(Node.newFunction(Node.newName("h"), new Node[]{}, Node.newBlock(2)));
    root.addChild(Node.newVar("z", Node.OP_ASSIGN, nameNodeD, Node.newInt(3)));
    root.addChild(Node.newVar("z", Node.OP_ASSIGN, nameNodeE, Node.newInt(4)));
    root.addChild(nameNodeF);
    root.addChild(nameNodeG);
    root.addChild(Node.newFunction(Node.newName("k"), new Node[]{}, Node.newBlock(2)));
    root.addChild(Node.newVar("z", Node.OP_ASSIGN, nameNodeF, Node.newInt(5)));

    NodeTraversal.traverse(renameVars.compiler, externs, processVars);
    NodeTraversal.traverse(renameVars.compiler, root, processVars);

    Set<String> externNames = renameVars.externNames;
    List<Assignment> assignments = renameVars.assignments.values();

    // Externs should have been kept.
    assertEquals(Sets.newHashSet("a", "b", "c"), externNames);

    // All other variables should be renamed.
    assertEquals(5, assignments.size());

    Assignment assignmentD = null;
    Assignment assignmentE = null;
    Assignment assignmentF = null;
    Assignment assignmentG = null;
    Assignment assignmentZ = null;

    for (Assignment a : assignments) {
      if (a.oldName.equals("d")) {
        assignmentD = a;
      } else if (a.oldName.equals("e")) {
        assignmentE = a;
      } else if (a.oldName.equals("f")) {
        assignmentF = a;
      } else if (a.oldName.equals("g")) {
        assignmentG = a;
      } else if (a.oldName.equals("z")) {
        assignmentZ = a;
      }
    }

    assertNotNull(assignmentD);
    assertNotNull(assignmentE);
    assertNotNull(assignmentF);
    assertNotNull(assignmentG);
    assertNotNull(assignmentZ);

    String oldNameD = assignmentD.oldName;
    String oldNameE = assignmentE.oldName;
    String oldNameF = assignmentF.oldName;
    String oldNameG = assignmentG.oldName;
    String oldNameZ = assignmentZ.oldName;

    String newNameD = assignmentD.newName;
    String newNameE = assignmentE.newName;
    String newNameF = assignmentF.newName;
    String newNameG = assignmentG.newName;
    String newNameZ = assignmentZ.newName;

    // Renamed variables should have new names.
    assertEquals("d_1", newNameD);
    assertEquals("e_1", newNameE);
    assertEquals("f_1", newNameF);
    assertEquals("g_1", newNameG);
    assertEquals("z_2", newNameZ);

    // Original names should not be in the map.
    assertNull(renameVars.renameMap.get("d"));
    assertNull(renameVars.renameMap.get("e"));
    assertNull(renameVars.renameMap.get("f"));
    assertNull(renameVars.renameMap.get("g"));
    assertNull(renameVars.renameMap.get("z"));

    // Old names should not be in the list of extern names.
    assertFalse(externNames.contains("d"));
    assertFalse(externNames.contains("e"));
    assertFalse(externNames.contains("f"));
    assertFalse(externNames.contains("g"));
    assertFalse(externNames.contains("z"));

    // New names should be in the list of all names.
    Set<String> allNames = renameVars.externNames;
    allNames.addAll(renameVars.renameMap.keySet());

    assertEquals(Sets.newHashSet("a", "b", "c", "d_1", "e_1", "f_1", "g_1", "z_2"), allNames);
  }

  @Test
  public void testGeneratePseudoNames() {
    RenameVars renameVars = new RenameVars(null, null, false, false, true, null, null, null);

    List<Assignment> assignments = new ArrayList<Assignment>();
    Assignment assignmentA = new Assignment("a", null);
    Assignment assignmentB = new Assignment("b", null);

    assignments.add(assignmentA);
    assignments.add(assignmentB);

    renameVars.assignNames(assignments);

    Map<String, String> renameMapExpected = new LinkedHashMap<String, String>() {{
      put("a", "$a$$");
      put("b", "$b$$");
    }};

    assertEquals(renameMapExpected, renameVars.renameMap);
  }
}