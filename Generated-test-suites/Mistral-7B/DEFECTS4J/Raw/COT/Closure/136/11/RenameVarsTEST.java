package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test cases for RenameVars class.
 */
public class RenameVarsTest {

  private RenameVars renameVars;
  private AbstractCompiler compiler;
  private VariableMap prevUsedRenameMap;
  private Set<String> reservedNames;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    prevUsedRenameMap = mock(VariableMap.class);
    reservedNames = new HashSet<String>();

    renameVars = new RenameVars(compiler, "", false, false, false, prevUsedRenameMap,
        null, reservedNames);
  }

  @Test
  public void testProcess_externsPass() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }
    });

    when(compiler.getDebugOutput()).thenAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) {
        // Do nothing
        return null;
      }
    });

    // Process externs pass
    renameVars.process(externs, root);

    ArgumentCaptor<String> externNameCaptor = ArgumentCaptor.forClass(String.class);
    verify(reservedNames).add(externNameCaptor.capture());

    List<String> externNames = new ArrayList<>(reservedNames);

    assertEquals("Expected externs to be added to reservedNames", 5, externNames.size());
    assertEquals("Expected 'a' to be added as externName", "a", externNames.get(0));
    assertEquals("Expected 'b' to be added as externName", "b", externNames.get(1));
    assertEquals("Expected 'c' to be added as externName", "c", externNames.get(2));
    assertEquals("Expected 'd' to be added as externName", "d", externNames.get(3));
    assertEquals("Expected 'e' to be added as externName", "e", externNames.get(4));
  }

  @Test
  public void testProcess_sourcePass() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    // Create test nodes for Name, Identifier and Variable statement
    Node nameNode1 = new Node(Token.NAME, "a", 1, 1, 2);
    Node nameNode2 = new Node(Token.NAME, "b", 2, 1, 3);
    Node nameNode3 = new Node(Token.NAME, "c", 3, 1, 4);
    Node identifierNode1 = new Node(Token.IDENTIFIER, "a", 5, 1, 6);
    Node identifierNode2 = new Node(Token.IDENTIFIER, "b", 7, 1, 8);
    Node variableStatement = new Node(Token.VAR, null, 9, 1, 10);
    List<Node> variableAssignments = new ArrayList<>();
    variableAssignments.add(new Node(Token.VAR, "a", 11, 1, 12));
    variableAssignments.add(new Node(Token.VAR, "b", 13, 1, 14));
    variableStatement.setChildren(variableAssignments);

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }
    });

    when(compiler.getDebugOutput()).thenAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) {
        // Do nothing
        return null;
      }
    });

    // Process source pass
    renameVars.process(externs, root);

    // Check localNameNodes and localTempNames
    List<Node> localNameNodes = new ArrayList<>(renameVars.localNameNodes);
    List<String> localTempNames = new ArrayList<>(renameVars.localTempNames);

    assertEquals("Expected size of localNameNodes to be 3", 3, localNameNodes.size());
    assertEquals("Expected 'a' to be present as localNameNode", nameNode1, localNameNodes.get(0));
    assertEquals("Expected 'b' to be present as localNameNode", nameNode2, localNameNodes.get(1));
    assertEquals("Expected 'c' to be present as localNameNode", nameNode3, localNameNodes.get(2));

    assertEquals("Expected size of localTempNames to be 3", 3, localTempNames.size());
    assertEquals("Expected 'L 0' to be present as localTempName for 'a'", "L 0", localTempNames.get(0));
    assertEquals("Expected 'L 1' to be present as localTempName for 'b'", "L 1", localTempNames.get(1));
    assertEquals("Expected 'L 2' to be present as localTempName for 'c'", "L 2", localTempNames.get(2));

    // Check globalNameNodes
    List<Node> globalNameNodes = new ArrayList<>(renameVars.globalNameNodes);

    assertEquals("Expected size of globalNameNodes to be 0", 0, globalNameNodes.size());

    // Check assignments
    SortedMap<String, Assignment> assignments = renameVars.assignments;

    assertEquals("Expected size of assignments to be 3", 3, assignments.size());

    Assignment aAssignment = assignments.get("a");
    assertNotNull("Expected an assignment for 'a'", aAssignment);
    assertEquals("Expected oldName of 'a' assignment to be 'a'", "a", aAssignment.oldName);
    assertEquals("Expected newName of 'a' assignment to be null since not assigned yet", null, aAssignment.newName);

    Assignment bAssignment = assignments.get("b");
    assertNotNull("Expected an assignment for 'b'", bAssignment);
    assertEquals("Expected oldName of 'b' assignment to be 'b'", "b", bAssignment.oldName);
    assertEquals("Expected newName of 'b' assignment to be null since not assigned yet", null, bAssignment.newName);

    Assignment cAssignment = assignments.get("c");
    assertNotNull("Expected an assignment for 'c'", cAssignment);
    assertEquals("Expected oldName of 'c' assignment to be 'c'", "c", cAssignment.oldName);
    assertEquals("Expected newName of 'c' assignment to be null since not assigned yet", null, cAssignment.newName);
  }

  @Test
  public void testProcess_reservedNames() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }
    });

    when(compiler.getDebugOutput()).thenAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) {
        // Do nothing
        return null;
      }
    });

    // Add reservedNames to the set
    reservedNames.add("if");
    reservedNames.add("else");

    // Process
    renameVars.process(externs, root);

    // Check assignments
    SortedMap<String, Assignment> assignments = renameVars.assignments;

    // Check that 'if' and 'else' haven't been assigned new names
    Assignment ifAssignment = assignments.get("if");
    assertNotNull("Expected an assignment for 'if'", ifAssignment);
    assertEquals("Expected oldName of 'if' assignment to be 'if'", "if", ifAssignment.oldName);
    assertEquals("Expected newName of 'if' assignment to be null", null, ifAssignment.newName);

    Assignment elseAssignment = assignments.get("else");
    assertNotNull("Expected an assignment for 'else'", elseAssignment);
    assertEquals("Expected oldName of 'else' assignment to be 'else'", "else", elseAssignment.oldName);
    assertEquals("Expected newName of 'else' assignment to be null", null, elseAssignment.newName);
  }
}