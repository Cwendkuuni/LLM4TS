package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testProcessVars_externsPass() {
    String code = "function foo() { var x = 1; } var y = 1;";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractNode.parseTree(code, null);
    renameVars.process(externs, null);

    Set<String> expectedExterns = new HashSet<>(Arrays.asList("foo", "x"));
    Set<String> actualExterns = new HashSet<>(renameVars.externNames);
    assertTrue(expectedExterns.equals(actualExterns));
  }

  @Test
  public void testProcessVars_sourcePass() {
    String code = "function foo() { var x = 1; } var y = 1; function bar() { var x = 2; }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;

    // Check global name nodes
    assertEquals(1, globalNameNodes.size());
    Node globalNameNode = globalNameNodes.get(0);
    assertEquals("x", globalNameNode.getString());

    // Check local name nodes
    assertEquals(2, localNameNodes.size());

    // Check first local name node
    Node firstLocalNameNode = localNameNodes.get(0);
    assertEquals("L 0", firstLocalNameNode.getString());

    // Check second local name node
    Node secondLocalNameNode = localNameNodes.get(1);
    assertEquals("L 1", secondLocalNameNode.getString());
  }

  @Test
  public void testProcessVars_localRenamingOnly() {
    String code = "function foo() { var x = 1; } function bar() { var x = 2; }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, true, false, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    List<Node> localNameNodes = renameVars.localNameNodes;

    // Check there's no global name
    assertTrue(renameVars.globalNameNodes.isEmpty());

    // Check local name nodes
    assertEquals(2, localNameNodes.size());

    // Check first local name node
    Node firstLocalNameNode = localNameNodes.get(0);
    assertFalse(firstLocalNameNode.getString().equals("x")); // not renamed

    // Check second local name node
    Node secondLocalNameNode = localNameNodes.get(1);
    assertTrue(secondLocalNameNode.getString().startsWith("L")); // renamed
  }

  @Test
  public void testProcessVars_preserveAnonymousFunctionNames() {
    String code = "function foo() { function bar() {} }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, true, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    List<Node> localNameNodes = renameVars.localNameNodes;

    // Check local name nodes
    assertEquals(1, localNameNodes.size());

    // Check local name node
    Node localNameNode = localNameNodes.get(0);
    assertEquals("bar", localNameNode.getString()); // not renamed
  }

  @Test
  public void testProcessVars_generatePseudoNames() {
    String code = "function foo() { var x = 1; }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, true, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;

    // Check global name nodes
    assertEquals(1, globalNameNodes.size());
    Node globalNameNode = globalNameNodes.get(0);
    assertTrue(globalNameNode.getString().startsWith("$")); // has a pseudo name

    // Check local name nodes
    assertEquals(0, localNameNodes.size());
  }

  @Test
  public void testProcessVars_okToRenameVar() {
    String code = "function foo() { var x = 1; }; function bar() {};";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    // Check okToRenameVar for local variables
    String localVarName = "x";
    assertTrue(renameVars.okToRenameVar(localVarName, true));

    // Check okToRenameVar for global variables
    String globalVarName = "foo";
    assertTrue(renameVars.okToRenameVar(globalVarName, false));

    // Check okToRenameVar for reserved names
    String reservedName = "function";
    assertFalse(renameVars.okToRenameVar(reservedName, false));
  }

  @Test
  public void testAssignNames() {
    String code = "function foo() { var x = 1; } function bar() { var x = 2; }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    Set<Assignment> assignments = renameVars.assignments;

    // Check assignments for 'x' from 'foo'
    Assignment xAssignment = assignments.stream()
        .filter(a -> a.oldName.equals("x"))
        .findFirst()
        .orElse(null);
    assertNotNull(xAssignment);
    assertEquals("x", xAssignment.oldName);
    assertEquals(1, xAssignment.orderOfOccurrence);
    assertNull(xAssignment.newName);

    // Check assignments for 'x' from 'bar'
    Assignment xAssignment2 = assignments.stream()
        .filter(a -> a.oldName.equals("x"))
        .findFirst()
        .orElse(null);
    assertNotNull(xAssignment2);
    assertEquals("x", xAssignment2.oldName);
    assertEquals(2, xAssignment2.orderOfOccurrence);
    assertNull(xAssignment2.newName);
  }

  @Test
  public void testFinalizeNameAssignment() {
    String code = "function foo() { var x = 1; } function bar() { var x = 2; }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    // Arrange
    Assignment xAssignment = assignments(renameVars, "x");

    // Act
    renameVars.finalizeNameAssignment(xAssignment, "newX");

    // Assert
    assertEquals("newX", xAssignment.newName);
  }

  @Test
  public void testGetVariableMap() {
    String code = "function foo() { var x = 1; } function bar() { var x = 2; }";
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractNode.parseTree("function foo() { }", null); // empty externs
    Node root = AbstractNode.parseTree(code, null);

    renameVars.process(externs, root);

    // Arrange
    Assignment xAssignment = assignments(renameVars, "x");

    // Act
    VariableMap variableMap = renameVars.getVariableMap();

    // Assert
    Map<String, String> variableMapMap = variableMap.getVariableMap();
    assertEquals("x", xAssignment.oldName);
    assertEquals("newX", variableMapMap.get(xAssignment.oldName));
  }

  private List<Node> localNameNodes(RenameVars renameVars) {
    return renameVars.localNameNodes;
  }

  private List<Node> globalNameNodes(RenameVars renameVars) {
    return renameVars.globalNameNodes;
  }

  private Set<Assignment> assignments(RenameVars renameVars) {
    return renameVars.assignments;
  }

  private Assignment assignments(RenameVars renameVars, String name) {
    return renameVars.assignments.stream()
        .filter(a -> a.oldName.equals(name))
        .findFirst()
        .orElse(null);
  }
}