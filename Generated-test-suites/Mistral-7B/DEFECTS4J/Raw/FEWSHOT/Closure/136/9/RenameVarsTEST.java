package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testProcessVars() {
    // Test processing of global and local variables
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
    Node root = Node.newText("function foo(a, b) { var c = function(d, e) { return a; } }");

    renamer.process(null, root);

    // Check that local variables have been renamed and collected
    List<Node> localNameNodes = renamer.localNameNodes;
    List<String> localTempNames = renamer.localTempNames;
    for (int i = 0; i < localNameNodes.size(); i++) {
      Node n = localNameNodes.get(i);
      String tempName = localTempNames.get(i);
      String expectedNewName = "L " + i;
      String actualNewName = renamer.getNewLocalName(n, i);
      assertEquals(expectedNewName, actualNewName);
    }

    // Check that global variables have not been renamed
    List<Node> globalNameNodes = renamer.globalNameNodes;
    for (Node n : globalNameNodes) {
      String oldName = n.getString();
      String newName = renamer.getNewGlobalName(n);
      assertNull(newName);
      assertEquals(oldName, n.getString());
    }
  }

  @Test
  public void testProcessVarsWithExterns() {
    // Test processing of global and local variables with externs
    RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, Sets.newHashSet("exports"));
    Node root = Node.newText("function foo(a) { exports.bar = a; }");

    renamer.process(Node.newText("var exports = {}"), root);

    // Check that local variable has not been renamed
    List<Node> localNameNodes = renamer.localNameNodes;
    for (Node n : localNameNodes) {
      String oldName = n.getString();
      String newName = renamer.getNewLocalName(n, 0);
      assertNull(newName);
      assertEquals(oldName, n.getString());
    }

    // Check that extern has not been renamed
    List<Node> globalNameNodes = renamer.globalNameNodes;
    assertEquals("exports", globalNameNodes.get(0).getString());
  }

  @Test
  public void testProcessVarsWithLocalRenamingOnly() {
    // Test processing of local variables only
    RenameVars renamer = new RenameVars(null, null, true, false, false, null, null, null);
    Node root = Node.newText("function foo(a) { var b = a; }");

    renamer.process(null, root);

    // Check that local variable has been renamed
    List<Node> localNameNodes = renamer.localNameNodes;
    List<String> localTempNames = renamer.localTempNames;
    for (int i = 0; i < localNameNodes.size(); i++) {
      Node n = localNameNodes.get(i);
      String tempName = localTempNames.get(i);
      String expectedNewName = "L " + i;
      String actualNewName = renamer.getNewLocalName(n, i);
      assertEquals(expectedNewName, actualNewName);
    }

    // Check that global variables have not been renamed
    List<Node> globalNameNodes = renamer.globalNameNodes;
    for (Node n : globalNameNodes) {
      String oldName = n.getString();
      String newName = renamer.getNewGlobalName(n);
      assertNull(newName);
      assertEquals(oldName, n.getString());
    }
  }

  @Test
  public void testProcessVarsWithPreserveAnonymousFunctionNames() {
    // Test processing of local variables with anonymous function names preserved
    RenameVars renamer = new RenameVars(null, null, false, true, false, null, null, null);
    Node root = Node.newText("function foo(a) { var b = function(c) { return a; }; }");

    renamer.process(null, root);

    // Check that local variable has not been renamed
    List<Node> localNameNodes = renamer.localNameNodes;
    for (Node n : localNameNodes) {
      String oldName = n.getString();
      String newName = renamer.getNewLocalName(n, 0);
      assertNull(newName);
      assertEquals(oldName, n.getString());
    }

    // Check that anonymous function name has not been renamed
    List<Node> functionNodes = NodeTraversal.collectDescendantsOfTypes(root, Node.FUNCTION);
    Node anonymousFunction = functionNodes.get(0);
    String oldName = anonymousFunction.getChild(0).getString();
    String newName = renamer.getNewLocalName(anonymousFunction, -1);
    assertNull(newName);
    assertEquals(oldName, anonymousFunction.getChild(0).getString());
  }

  @Test
  public void testProcessVarsWithGeneratePseudoNames() {
    // Test processing of local variables with pseudo names generated
    RenameVars renamer = new RenameVars(null, null, false, false, true, null, null, null);
    Node root = Node.newText("function foo() { var a = 1; }");

    renamer.process(null, root);

    // Check that local variable has been renamed with pseudo name
    List<Node> localNameNodes = renamer.localNameNodes;
    List<String> localTempNames = renamer.localTempNames;
    for (int i = 0; i < localNameNodes.size(); i++) {
      Node n = localNameNodes.get(i);
      String tempName = localTempNames.get(i);
      String expectedNewName = "$$a$$";
      String actualNewName = renamer.getNewLocalName(n, i);
      assertEquals(expectedNewName, actualNewName);
    }

    // Check that
  }
}