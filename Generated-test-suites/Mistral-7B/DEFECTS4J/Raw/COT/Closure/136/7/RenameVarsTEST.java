import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  private CompilerPass renameVars;
  private Node node;
  private Node root;
  private Node externs;
  private VariableMap prevUsedRenameMap;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    renameVars = new RenameVars(mock(Compiler.class), "", false, false, false,
        prevUsedRenameMap, null, null);
    node = mock(Node.class);
    root = mock(Node.class);
    externs = mock(Node.class);
  }

  @Test
  public void testProcess_externsPass() {
    // Given
    when(externs.getChild(0)).thenReturn(node);

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verify(renameVars, times(1)).traverse(eq(compiler), externs, new ProcessVars(true));
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    verify(renameVars, times(1)).incCount(eq(nodeArgumentCaptor.capture().getString()), eq(externs));
    String capturedName = nodeArgumentCaptor.getValue().getString();
    assertEquals("Expected extern variable to be added to the list.", capturedName, node.getString());
    assertEquals("Expected extern set to contain the captured name.", Arrays.asList(capturedName), externNames);
  }

  @Test
  public void testProcess_sourcePass() {
    // Given
    when(root.getChild(0)).thenReturn(node);

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verify(renameVars, times(1)).traverse(eq(compiler), root, new ProcessVars(false));
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CompilerInput> inputArgumentCaptor = ArgumentCaptor.forClass(CompilerInput.class);
    verify(renameVars, times(1)).incCount(eq(nodeArgumentCaptor.capture().getString()), eq(inputArgumentCaptor.capture().getInput()));
    String capturedName = nodeArgumentCaptor.getValue().getString();
    CompilerInput capturedInput = inputArgumentCaptor.getValue();
    assertEquals("Expected local variable to be added to the map.", capturedName, node.getString());
    assertEquals("Expected CompilerInput to be set correctly.", capturedInput, node.getCompilerInput());
  }

  @Test
  public void testAssignNames_noPreviouslyUsedMap() {
    // Given
    List<Assignment> assignments = new ArrayList<>();
    assignments.add(new Assignment("a", new CompilerInput("TestFile.java", 1, 1)));
    assignments.add(new Assignment("b", new CompilerInput("TestFile.java", 1, 2)));
    assignments.add(new Assignment("c", new CompilerInput("TestFile.java", 1, 3)));

    // When
    RenameVars renameVars = new RenameVars(mock(Compiler.class), "", false, false, false, null, null, null);
    renameVars.assignNames(assignments);

    // Then
    List<String> generatedNames = new ArrayList<>();
    for (Assignment assignment : assignments) {
      String generatedName = assignment.newName;
      assertTrue("Expected a name to be generated.", generatedName != null);
      generatedNames.add(generatedName);
    }
    List<Assignment> sortedAssignments = new ArrayList<>(assignments);
    Collections.sort(sortedAssignments, FREQUENCY_COMPARATOR);
    for (int i = 0; i < assignments.size(); i++) {
      Assignment assignment = assignments.get(i);
      Assignment sortedAssignment = sortedAssignments.get(i);
      assertEquals("Expected the generated name to match the name in the sorted list.", sortedAssignment.newName, assignment.newName);
      assertEquals("Expected the generated name to be different from the original name.", !sortedAssignment.newName.equals(sortedAssignment.oldName), true);
      assertEquals("Expected the original name to be different from the generated name.", !sortedAssignment.oldName.equals(sortedAssignment.newName), true);
      assertEquals("Expected the generated names to be unique.", !generatedNames.contains(sortedAssignment.newName), false);
    }
  }

  @Test
  public void testAssignNames_withPreviouslyUsedMap() {
    // Given
    List<Assignment> assignments = new ArrayList<>();
    ListMultimap<String, Assignment> prevUsedAssignments = ArrayListMultimap.create();
    prevUsedAssignments.put("a", new Assignment("a", new CompilerInput("TestFile.java", 1, 1)));
    prevUsedAssignments.put("b", new Assignment("b", new CompilerInput("TestFile.java", 1, 2)));
    prevUsedAssignments.put("c", new Assignment("c", new CompilerInput("TestFile.java", 1, 3)));

    for (Assignment assignment : prevUsedAssignments.values()) {
      assignments.add(assignment);
    }

    assignments.add(new Assignment("d", new CompilerInput("TestFile.java", 1, 4)));

    // When
    RenameVars renameVars = new RenameVars(mock(Compiler.class), "", false, false, false,
        new VariableMap(prevUsedAssignments.asMap()), null, null);
    renameVars.assignNames(assignments);

    // Then
    List<String> generatedNames = new ArrayList<>();
    for (Assignment assignment : assignments) {
      String generatedName = assignment.newName;
      assertTrue("Expected a name to be generated.", generatedName != null);
      generatedNames.add(generatedName);
    }
    List<Assignment> sortedAssignments = new ArrayList<>(assignments);
    Collections.sort(sortedAssignments, FREQUENCY_COMPARATOR);
    for (int i = 0; i < assignments.size(); i++) {
      Assignment assignment = assignments.get(i);
      Assignment sortedAssignment = sortedAssignments.get(i);
      assertEquals("Expected the generated name to match the name in the sorted list.", sortedAssignment.newName, assignment.newName);
      assertEquals("Expected the generated name to be different from the original name.", !sortedAssignment.newName.equals(sortedAssignment.oldName), true);
      assertEquals("Expected the original name to be different from the generated name.", !sortedAssignment.oldName.equals(sortedAssignment.newName), true);
      assertEquals("Expected the generated names to be unique after reuse.", !generatedNames.contains(sortedAssignment.newName), false);
    }
  }

  @Test(expected = AssertionError.class)
  public void testAssignNames_invalidName() {
    // Given
    List<Assignment> assignments = new ArrayList<>();
    assignments.add(new Assignment("", new CompilerInput("TestFile.java", 1, 1)));

    // When
    RenameVars renameVars = new RenameVars(mock(Compiler.class), "", false, false, false, null, null, null);
    renameVars.assignNames(assignments);

    // Then - an AssertionError should be thrown
  }

  @Test
  public void testProcess_noNameAssignments() {
    // Given

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verifyNoMoreInteractions(renameVars);
  }

  @Test
  public void testProcess_nameAssignments() {
    // Given
    when(externs.getChild(0)).thenReturn(node);
    when(node.getString()).thenReturn("a");

    when(root.getChild(0)).thenReturn(node);
    when(node.getString()).thenReturn("b");

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    doReturn(true).when(compileCodingConvention)
        .isExported("a", false);
    doReturn(false).when(compileCodingConvention)
        .isExported("b", false);

    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> newNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verify(compileCodingConvention, times(2)).isExported(anyString(), anyBoolean());
    verify(renameVars, times(1)).traverse(eq(compiler), externs, new ProcessVars(true));
    verify(renameVars, times(1)).traverse(eq(compiler), root, new ProcessVars(false));
    verify(renameVars, times(1)).incCount(nodeArgumentCaptor.capture(), eq(externs));
    String capturedName = nodeArgumentCaptor.getValue().getString();
    assertEquals("Expected the first node's name to be captured.", "a", capturedName);
    verify(renameVars, times(1)).finalizeNameAssignment(eq(new NameAssignment("a", "a")), anyString());
    verify(renameVars, times(1)).getVariableMap().put("a", eq("a"));

    verifyNoMoreInteractions(renameVars);

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verifyNoMoreInteractions(renameVars);

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verifyNoMoreInteractions(renameVars);

    // When
    renameVars.process(externs, root);

    // Then
    verify(renameVars, times(1)).process(externs, root);
    verifyNoMoreInteractions(renameVars);
  }

  @Test
  public void testOkayToRenameVar_exportedLocalVariable() {
    // Given
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    doReturn(true).when(compileCodingConvention)
        .isExported("a", false);

    // When
    boolean result = renameVars.okToRenameVar("a", false);

    // Then
    assertTrue("Expected the method to return false since the local variable is exported.", result);
  }

  @Test
  public void testOkayToRenameVar_exportedGlobalVariable() {
    // Given
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    doReturn(true).when(compileCodingConvention)
        .isExported("a", true);

    // When
    boolean result = renameVars.okToRenameVar("a", false);

    // Then
    assertFalse("Expected the method to return false since the global variable is exported.", result);
  }

  @Test
  public void testOkayToRenameVar_localVariable() {
    // Given
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    doReturn(false).when(compileCodingConvention)
        .isExported("a", false);

    // When
    boolean result = renameVars.okToRenameVar("a", false);

    // Then
    assertTrue("Expected the method to return true since the local variable is not exported.", result);
  }

  @Test
  public void testOkayToRenameVar_globalVariable() {
    // Given
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    doReturn(false).when(compileCodingConvention)
        .isExported("a", true);

    // When
    boolean result = renameVars.okToRenameVar("a", false);

    // Then
    assertTrue("Expected the method to return true since the global variable is not exported.", result);
  }

  private CodingConvention mockCodingConvention() {
    return mock(CodingConvention.class);
  }
}