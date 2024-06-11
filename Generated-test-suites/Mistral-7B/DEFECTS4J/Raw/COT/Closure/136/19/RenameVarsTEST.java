package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

public class RenameVarsTest {
  private AbstractCompiler compiler;
  private RenameVars renameVars;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
  }

  @Test
  public void testEmptyClass() {
    // Given
    Node root = Node.NEW_AST();

    // When
    renameVars.process(Node.NEW_AST(), root);

    // Then
    assertTrue("No assignments were logged", !renameVars.assignmentLog.isEmpty());
    assertEquals("Empty class should not have any assignments.", 0, renameVars.assignmentCount);
  }

  @Test
  public void testRenameGlobalVariables() {
    // Given
    String oldName1 = "oldGlobal1";
    String oldName2 = "oldGlobal2";
    String newName1 = "newGlobal1";
    String newName2 = "newGlobal2";

    CompilerInput input1 = mock(CompilerInput.class);
    CompilerInput input2 = mock(CompilerInput.class);

    Node root = Node.NEW_AST();

    Node oldGlobal1Node = nodeFactory.createNameNode(oldName1);
    Node oldGlobal2Node = nodeFactory.createNameNode(oldName2);

    root.addChild(oldGlobal1Node);
    root.addChild(oldGlobal2Node);

    when(compiler.getNodeFactory()).thenReturn(nodeFactory);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(compiler.getCodingConvention().isExported(oldName1, false)).thenReturn(false);
    when(compiler.getCodingConvention().isExported(oldName2, false)).thenReturn(false);

    when(compiler.getGlobalScope()).thenReturn(mock(Scope.class));

    when(compiler.getGlobalScope().getVar(oldName1)).thenReturn(null);
    when(compiler.getGlobalScope().getVar(oldName2)).thenReturn(null);

    ListMultimap<String, CompilerInput> globalVars = ArrayListMultimap.newSetMultiMap();
    when(compiler.getGlobalScope()).thenReturn(new Scope(null, null, globalVars, null));

    when(compiler.getNodeTraversal()).thenReturn(mock(NodeTraversal.class));

    // When
    renameVars.process(Node.NEW_AST(), root);

    // Then
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).reportCodeChange();

    verify(compiler).addToDebugLog("JS var assignments:");

    verify(compiler).addToDebugLog(oldName1 + " => " + newName1);
    verify(compiler).addToDebugLog(oldName2 + " => " + newName2);

    assertEquals("Unexpected number of assignments.", 2, renameVars.assignmentCount);
    assertEquals("Unexpected assignment log.", newName1 + " => " + oldName1 + "\n" +
        newName2 + " => " + oldName2, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameLocalVariables() {
    // Given
    String oldName1 = "oldLocal1";
    String oldName2 = "oldLocal2";
    String newName1 = "newLocal1";
    String newName2 = "newLocal2";

    Node root = Node.NEW_AST();
    Node functionNode = nodeFactory.createFunctionDeclarationNode(null, null, null, null, null);
    Node variableDeclaration1Node = nodeFactory.createVariableDeclarationListNode(null, null, null, null, null);
    Node variableDeclaration2Node = nodeFactory.createVariableDeclarationListNode(null, null, null, null, null);
    Node variableDeclaration1 = nodeFactory.createVariableDeclarationNode(null, oldName1, null, null, null, null, null);
    Node variableDeclaration2 = nodeFactory.createVariableDeclarationNode(null, oldName2, null, null, null, null, null);
    Node variableDeclarationList1 = nodeFactory.createVariableDeclarationListNode(null, null, null, null, null, null, null);
    Node variableDeclarationList2 = nodeFactory.createVariableDeclarationListNode(null, null, null, null, null, null, null);

    functionNode.setFirstChild(variableDeclarationList1);
    variableDeclarationList1.setFirstChild(variableDeclaration1);
    variableDeclarationList1.addChild(variableDeclaration2);

    functionNode.setLastChild(variableDeclarationList2);

    root.addChild(functionNode);

    when(compiler.getNodeFactory()).thenReturn(nodeFactory);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(compiler.getCodingConvention().isExported(oldName1, true)).thenReturn(false);
    when(compiler.getCodingConvention().isExported(oldName2, true)).thenReturn(false);

    when(compiler.getGlobalScope()).thenReturn(mock(Scope.class));

    when(compiler.getNodeTraversal()).thenReturn(mock(NodeTraversal.class));

    // When
    renameVars.process(Node.NEW_AST(), root);

    // Then
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).reportCodeChange();

    verify(compiler).addToDebugLog("JS var assignments:");

    verify(compiler).addToDebugLog(oldName1 + " => " + newName1);
    verify(compiler).addToDebugLog(oldName2 + " => " + newName2);

    assertEquals("Unexpected number of assignments.", 2, renameVars.assignmentCount);
    assertEquals("Unexpected assignment log.", newName1 + " => " + oldName1 + "\n" +
        newName2 + " => " + oldName2, renameVars.assignmentLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenameVariablesWithExistingName() {
    // Given
    String oldName1 = "oldName";
    String oldName2 = "oldName";

    Node root = Node.NEW_AST();

    when(compiler.getNodeFactory()).thenReturn(nodeFactory);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    // When & Then
    renameVars.process(Node.NEW_AST(), root);

    fail("Should throw an IllegalArgumentException.");
  }

  @Test
  public void testRenameVariablesWithReservedNames() {
    // Given
    String oldName1 = "oldReserved";
    String oldName2 = "oldName";
    String newName1 = "newReserved";
    String newName2 = "newName";

    Node root = Node.NEW_AST();

    when(compiler.getNodeFactory()).thenReturn(nodeFactory);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(compiler.getCodingConvention().isExported(oldName1, false)).thenReturn(false);
    when(compiler.getCodingConvention().isExported(oldName2, false)).thenReturn(false);

    when(compiler.getGlobalScope()).thenReturn(mock(Scope.class));

    when(compiler.getGlobalScope().getVar(oldName1)).thenReturn(null);
    when(compiler.getGlobalScope().getVar(oldName2)).thenReturn(null);

    when(compiler.getNodeTraversal()).thenReturn(mock(NodeTraversal.class));

    Set<String> reservedNames = Sets.newHashSet("oldReserved", "newReserved");
    when(renameVars.reservedNames).thenReturn(reservedNames);

    // When
    renameVars.process(Node.NEW_AST(), root);

    // Then
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).reportCodeChange();

    verify(compiler).addToDebugLog("JS var assignments:");

    verify(compiler).addToDebugLog(oldName1 + " => " + newName1);
    verify(compiler).addToDebugLog(oldName2 + " => " + newName2);

    // Check that the reserved names were not renamed
    assertEquals("Unexpected number of assignments.", 2, renameVars.assignmentCount);
    assertEquals("Unexpected assignment log.", "oldName => newName\n" +
        "oldReserved => oldReserved\n" +
        "newName => oldName", renameVars.assignmentLog.toString());
  }
}