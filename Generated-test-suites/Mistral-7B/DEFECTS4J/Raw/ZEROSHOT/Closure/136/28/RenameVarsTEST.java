package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private static <T> ListMultimap<String, T> newListMultimap() {
    return (ListMultimap<String, T>) ArrayListMultimap.create();
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> newArrayList(T... elements) {
    return Arrays.asList(elements);
  }

  @SuppressWarnings("unchecked")
  private static <T> List<List<T>> newListOfLists(List<T>... elements) {
    List<List<T>> list = new ArrayList<>();
    for (List<T> listElement : elements) {
      list.add(listElement);
    }
    return list;
  }

  private static class TestCompiler implements AbstractCompiler {
    @Override
    public void reportCodeChange() {}

    @Override
    public Node getExterns() {
      return null;
    }

    @Override
    public Node getSourceNode() {
      return null;
    }

    @Override
    public CompilerInput getCompilerInput() {
      return null;
    }

    @Override
    public void addToDebugLog(String message) {}

    @Override
    public CompilerOptions getOptions() {
      return null;
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @org.junit.Test
  public void testRenameVars() {
    AbstractCompiler compiler = mock(TestCompiler.class);
    String prefix = "myPrefix";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    Set<String> reservedNames = Sets.newHashSet();
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = new char[0];
    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(renameVars);

    // Test empty input
    Node root = Node.root();
    Node externs = Node.root();
    renameVars.process(externs, root);
    assertEquals(0, renameVars.getVariableMap().size());

    // Test simple variable renaming
    String oldName = "oldName";
    String newName = "newName";
    CompilerInput input = mock(CompilerInput.class);
    when(input.getSourceFile()).thenReturn(oldName);
    Node nameNode = Node.name(oldName);
    Node newNameNode = Node.name(newName);
    List<Node> inputNodes = newArrayList(nameNode);
    when(compiler.getCompilerInput()).thenReturn(input);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    List<Node> localNameNodes = newArrayList();
    List<String> localTempNames = newArrayList();
    List<Node> globalNameNodes = newArrayList();
    renameVars.process(externs, root);
    renameVars.localNameNodes = localNameNodes;
    renameVars.localTempNames = localTempNames;
    renameVars.globalNameNodes = globalNameNodes;

    renameVars.assignments.clear();
    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    assertEquals(1, renameVars.getVariableMap().size());
    assertEquals(newName, renameVars.getVariableMap().get(oldName));

    // Test multiple variables with same name
    oldName = "oldName2";
    newName = "newName2";
    String oldName3 = "oldName3";
    String newName3 = "newName3";
    input = mock(CompilerInput.class);
    when(input.getSourceFile()).thenReturn(oldName);
    nameNode = Node.name(oldName);
    newNameNode = Node.name(newName);
    List<Node> inputNodes2 = newArrayList(nameNode);
    List<Node> inputNodes3 = newArrayList(Node.name(oldName3));

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);
    renameVars.processAssignment(nameNode, inputNodes3, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    assertEquals(2, renameVars.getVariableMap().size());
    assertEquals(newName, renameVars.getVariableMap().get(oldName));
    assertEquals(newName3, renameVars.getVariableMap().get(oldName3));

    // Test renaming conflicting variable names
    oldName = "oldName4";
    newName = "newName4";
    input = mock(CompilerInput.class);
    when(input.getSourceFile()).thenReturn(oldName);
    nameNode = Node.name(oldName);
    newNameNode = Node.name(newName);
    when(compiler.getCompilerInput()).thenReturn(input);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    try {
      renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
          globalNameNodes, localTempNames, assignmentLog);
      fail("Expected IllegalStateException: Name collision.");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test renaming anonymous function names
    oldName = "anonymousFunction";
    when(compiler.getCodingConvention()).thenReturn(
        new CodingConvention() {
          @Override
          public boolean isExported(String name, boolean isLocal) {
            return false;
          }

          @Override
          public boolean isExportedAnonymousFunction(String name) {
            return true;
          }
        });
    preserveAnonymousFunctionNames = true;
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    // Anonymous function name should not be renamed
    Node anonymousFunction = Node.name(oldName);
    renameVars.processAssignment(anonymousFunction, null, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);
    assertNull(renameVars.getVariableMap().get(oldName));

    // Anonymous function name should not conflict with other variable name
    oldName = "anotherName";
    newName = "anotherNewName";
    input = mock(CompilerInput.class);
    when(input.getSourceFile()).thenReturn(oldName);
    nameNode = Node.name(oldName);
    when(compiler.getCompilerInput()).thenReturn(input);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);
    renameVars.processAssignment(anonymousFunction, null, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    assertEquals(2, renameVars.getVariableMap().size());
    assertEquals(newName, renameVars.getVariableMap().get(oldName));
    assertNull(renameVars.getVariableMap().get(oldName + "(function)"));

    // Test preserving anonymous function names and local variables
    preserveAnonymousFunctionNames = true;
    localRenamingOnly = true;
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    // Anonymous function name should be renamed
    anonymousFunction = Node.name(oldName);
    renameVars.processAssignment(anonymousFunction, null, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);
    String anonymousRenamedName = "anonymousFunction_" + assignmentCount;
    assertEquals(anonymousRenamedName, renameVars.getVariableMap().get(oldName));

    // Local variable should not be renamed
    oldName = "localVariable";
    nameNode = Node.name(oldName);
    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    assertEquals(2, renameVars.getVariableMap().size());
    assertEquals(newName, renameVars.getVariableMap().get(oldName));
    assertEquals(anonymousRenamedName, renameVars.getVariableMap().get(oldName + "(function)"));

    // Test prefix is added to local variable names
    prefix = "prefix_";
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    // Local variable should be renamed
    oldName = "localVariable";
    nameNode = Node.name(oldName);
    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    assertEquals(1, renameVars.getVariableMap().size());
    assertEquals("prefix_L0", renameVars.getVariableMap().get(oldName));

    // Test reserved characters are not used in variable names
    reservedNames.add("$abc");
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        new char[]{'a', 'b', 'c'}, reservedNames);

    localNameNodes.clear();
    localTempNames.clear();
    globalNameNodes.clear();

    // Local variable should be renamed with different name because of reserved character
    oldName = "$abcVariable";
    nameNode = Node.name(oldName);
    renameVars.processAssignment(nameNode, inputNodes, localNameNodes,
        globalNameNodes, localTempNames, assignmentLog);

    assertEquals(1, renameVars.getVariableMap().size());
    assertNull(renameVars.getVariableMap().get(oldName));
  }
}