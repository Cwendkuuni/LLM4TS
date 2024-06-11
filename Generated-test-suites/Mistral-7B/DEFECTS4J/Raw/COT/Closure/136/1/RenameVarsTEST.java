package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @org.junit.Test
  public void testRenameVars() {
    String prefix = "test_";
    List<Node> initialGlobalNameNodes = Lists.newArrayList();
    List<Node> initialLocalNameNodes = Lists.newArrayList();
    List<String> initialLocalTempNames = Lists.newArrayList();
    Set<String> initialExternNames = Sets.newHashSet();
    Set<String> initialReservedNames = Sets.newHashSet();
    Map<String, String> initialRenameMap = Maps.newHashMap();
    VariableMap initialPrevUsedRenameMap = null;
    char[] initialReservedCharacters = new char[0];

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameVars renameVars = new RenameVars(compiler, prefix, false, false, false,
        initialPrevUsedRenameMap, initialReservedCharacters, initialReservedNames);

    // Test process method with empty nodes
    renameVars.process(Node.nullLiteral(), Node.nullLiteral());
    assertEquals(0, renameVars.globalNameNodes.size());
    assertEquals(0, renameVars.localNameNodes.size());
    assertEquals(0, renameVars.localTempNames.size());
    assertEquals(0, renameVars.externNames.size());

    // Test process method with initial variables
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraversal()).thenReturn(nodeTraversal);
    when(nodeTraversal.getRootNode()).thenReturn(root);
    when(nodeTraversal.getScope()).thenReturn(createMock(Scope.class));

    renameVars.process(externs, root);

    verify(compiler, times(1)).addToDebugLog(containsString("JS var assignments:"));
    verifyNoMoreInteractions(compiler);

    assertEquals(initialGlobalNameNodes, renameVars.globalNameNodes);
    assertEquals(initialLocalNameNodes, renameVars.localNameNodes);
    assertEquals(initialLocalTempNames, renameVars.localTempNames);
    assertEquals(initialExternNames, renameVars.externNames);
    assertEquals(initialReservedNames, renameVars.reservedNames);
    assertEquals(initialRenameMap, renameVars.renameMap);

    // Test okToRenameVar method
    String validLocalName = "a";
    String validGlobalName = "b";
    String validAnonymousFunctionName = "c()";
    String invalidName = "export";
    boolean isLocal = true;

    assertTrue(renameVars.okToRenameVar(validLocalName, isLocal));
    assertTrue(renameVars.okToRenameVar(validGlobalName, isLocal));
    assertTrue(renameVars.okToRenameVar(validAnonymousFunctionName, isLocal));
    assertFalse(renameVars.okToRenameVar(invalidName, isLocal));

    // Test process method with renamed variables
    NameGenerator globalNameGenerator = mock(NameGenerator.class);
    NameGenerator localNameGenerator = mock(NameGenerator.class);
    when(globalNameGenerator.generateNextName()).thenReturn("d");
    when(localNameGenerator.generateNextName()).thenReturn("e");

    renameVars = new RenameVars(compiler, prefix, false, false, false,
        initialPrevUsedRenameMap, new char[0], initialReservedNames) {
      @Override
      NameGenerator getGlobalNameGenerator() {
        return globalNameGenerator;
      }

      @Override
      NameGenerator getLocalNameGenerator() {
        return localNameGenerator;
      }
    };

    Node nameNode1 = mock(Node.class);
    Node nameNode2 = mock(Node.class);

    // Test renaming local variables
    when(nodeTraversal.getNode(anyInt())).thenReturn(nameNode1);
    when(nameNode1.getType()).thenReturn(Token.NAME);
    when(nameNode1.getString()).thenReturn("local_name");

    renameVars.process(externs, root);

    String expectedNewLocalName = prefix + "e";
    assertEquals(expectedNewLocalName, renameVars.getNewLocalName(nameNode1, 0));

    // Test renaming global variables
    when(nameNode1.getString()).thenReturn("global_name");

    renameVars.process(externs, root);

    String expectedNewGlobalName = prefix + "d";
    assertEquals(expectedNewGlobalName, renameVars.getNewGlobalName(nameNode1));
  }

  @org.junit.Test
  public void testRenameVarsWithExistingVariables() {
    String prefix = "test_";
    List<Node> initialGlobalNameNodes = Arrays.asList(mock(Node.class));
    List<Node> initialLocalNameNodes = Arrays.asList(mock(Node.class));
    List<String> initialLocalTempNames = Arrays.asList("temp1");
    Set<String> initialExternNames = Sets.newHashSet("extern1");
    Set<String> initialReservedNames = Sets.newHashSet("reserved1");
    Map<String, String> initialRenameMap = Maps.newHashMap();
    VariableMap initialPrevUsedRenameMap = createMock(VariableMap.class);
    char[] initialReservedCharacters = new char[0];

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameVars renameVars = new RenameVars(compiler, prefix, false, false, false,
        initialPrevUsedRenameMap, initialReservedCharacters, initialReservedNames);

    // Test process method with existing variables
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraversal()).thenReturn(nodeTraversal);
    when(nodeTraversal.getRootNode()).thenReturn(root);
    when(nodeTraversal.getScope()).thenReturn(createMock(Scope.class));

    when(initialPrevUsedRenameMap.lookupNewName(eq("global_name"))).thenReturn("oldName");

    renameVars.process(externs, root);

    String expectedNewGlobalName = prefix + "oldName";
    assertEquals(expectedNewGlobalName, renameVars.getNewGlobalName(initialGlobalNameNodes.get(0)));
  }

  @org.junit.Test
  public void testRenameVarsWithConflictingNames() {
    String prefix = "test_";
    List<Node> initialGlobalNameNodes = Lists.newArrayList();
    List<Node> initialLocalNameNodes = Lists.newArrayList();
    List<String> initialLocalTempNames = Lists.newArrayList();
    Set<String> initialExternNames = Sets.newHashSet();
    Set<String> initialReservedNames = Sets.newHashSet();
    Map<String, String> initialRenameMap = Maps.newHashMap();
    VariableMap initialPrevUsedRenameMap = mock(VariableMap.class);
    char[] initialReservedCharacters = new char[0];

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameVars renameVars = new RenameVars(compiler, prefix, false, false, false,
        initialPrevUsedRenameMap, initialReservedCharacters, initialReservedNames);

    // Test process method with conflicting names
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraversal()).thenReturn(nodeTraversal);
    when(nodeTraversal.getRootNode()).thenReturn(root);
    when(nodeTraversal.getScope()).thenReturn(createMock(Scope.class));

    when(initialPrevUsedRenameMap.lookupNewName(eq("conflicting_name"))).thenReturn("oldName1");
    when(initialPrevUsedRenameMap.lookupNewName(eq("conflicting_name"))).thenReturn("oldName2");

    renameVars.process(externs, root);

    String expectedNewGlobalName1 = prefix + "oldName1";
    String expectedNewGlobalName2 = prefix + "oldName2";

    for (Node nameNode : initialGlobalNameNodes) {
      if (nameNode.getString().equals("conflicting_name")) {
        assertEquals(expectedNewGlobalName1, renameVars.getNewGlobalName(nameNode));
      } else {
        assertEquals(nameNode.getString(), renameVars.getNewGlobalName(nameNode));
      }
    }
  }

  @org.junit.Test
  public void testRenameVarsWithLongVariableNames() {
    String prefix = "test_";
    List<Node> initialGlobalNameNodes = Lists.newArrayList();
    List<Node> initialLocalNameNodes = Lists.newArrayList();
    List<String> initialLocalTempNames = Lists.newArrayList();
    Set<String> initialExternNames = Sets.newHashSet();
    Set<String> initialReservedNames = Sets.newHashSet();
    Map<String, String> initialRenameMap = Maps.newHashMap();
    VariableMap initialPrevUsedRenameMap = mock(VariableMap.class);
    char[] initialReservedCharacters = new char[0];

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameVars renameVars = new RenameVars(compiler, prefix, false, false, false,
        initialPrevUsedRenameMap, initialReservedCharacters, initialReservedNames);

    // Test process method with long variable names
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraversal()).thenReturn(nodeTraversal);
    when(nodeTraversal.getRootNode()).thenReturn(root);
    when(nodeTraversal.getScope()).thenReturn(createMock(Scope.class));

    for (int i = 0; i < 10; i++) {
      Node nameNode = mock(Node.class);
      when(nameNode.getType()).thenReturn(Token.NAME);
      when(nameNode.getString()).thenReturn("long_variable_name_" + i);

      initialGlobalNameNodes.add(nameNode);
      initialLocalNameNodes.add(nameNode);
      initialLocalTempNames.add("temp" + i);
    }

    renameVars.process(externs, root);

    for (Node nameNode : initialGlobalNameNodes) {
      String expectedNewGlobalName = prefix + "long_variable_name_" + getIndex(nameNode);
      assertEquals(expectedNewGlobalName, renameVars.getNewGlobalName(nameNode));
    }

    for (Node nameNode : initialLocalNameNodes) {
      String expectedNewLocalName = prefix + "temp" + getIndex(nameNode);
      assertEquals(expectedNewLocalName, renameVars.getNewLocalName(nameNode, getIndex(nameNode)));
    }
  }

  /**
   * Helper method to get index of a Node for test purposes.
   */
  private int getIndex(Node node) {
    for (int i = 0; i < initialGlobalNameNodes.size(); i++) {
      if (initialGlobalNameNodes.get(i).equals(node)) {
        return i;
      }
    }

    for (int i = 0; i < initialLocalNameNodes.size(); i++) {
      if (initialLocalNameNodes.get(i).equals(node)) {
        return i;
      }
    }

    throw new RuntimeException("Node not found.");
  }
}