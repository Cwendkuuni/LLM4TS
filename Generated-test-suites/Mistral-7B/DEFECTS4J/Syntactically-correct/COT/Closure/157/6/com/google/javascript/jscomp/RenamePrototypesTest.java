package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

/**
 * Tests for RenamePrototypes class
 */
public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;
  private AbstractCompiler compiler;
  private Node rootNode;
  private CompilerInput input;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    MockitoAnnotations.initMocks(this);

    input = mock(CompilerInput.class);
    when(compiler.getInput()).thenReturn(input);
    rootNode = mock(Node.class);
  }

  @Test
  public void testProcess_NormalCase() {
    // Given
    Node externs = mock(Node.class);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When
    renamePrototypes.process(externs, rootNode);

    // Then
    assertNotNull(renamePrototypes.properties);
  }

  @Test
  public void testProcess_NullInputs() {
    // Given
    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When & Then
    assertThrows(NullPointerException.class, () -> renamePrototypes.process(null, rootNode));
    assertThrows(NullPointerException.class, () -> renamePrototypes.process(externs, null));
  }

  @Test
  public void testProcess_NullExterns() {
    // Given
    Node rootNode = mock(Node.class);
    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When
    renamePrototypes.process(null, rootNode);

    // Then
    assertFalse(renamePrototypes.properties.isEmpty());
  }

  @Test
  public void testProcess_NullRootNode() {
    // Given
    Node externs = mock(Node.class);
    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When
    renamePrototypes.process(externs, null);

    // Then
    assertFalse(renamePrototypes.properties.isEmpty());
  }

  @Test
  public void testProcessExternedProperties_StringNode() {
    // Given
    Node externs = mock(Node.class);
    Node stringNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    when(externs.getFirstChild()).thenReturn(parentNode);
    when(parentNode.getNext()).thenReturn(stringNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    ArgumentCaptor<Node> stringNodeCaptor = ArgumentCaptor.forClass(Node.class);

    // When
    renamePrototypes.new ProcessExternedProperties().visit(null, externs, parentNode);

    // Then
    verify(compiler).addToDebugLog("JS property assignments:");
    verify(compiler).addToDebugLog(stringNodeCaptor.capture().getString() + " => ");
    assertNull(stringNodeCaptor.getValue().newName);
    verifyZeroIterations(renamePrototypes.stringNodes);
  }

  @Test
  public void testProcessProperties_StringNode_Prototype() {
    // Given
    Node rootNode = mock(Node.class);
    Node stringNode = mock(Node.class);
    Node prototypeNode = mock(Node.class);
    Node getpropNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(rootNode.getLastChild()).thenReturn(getpropNode);
    when(getpropNode.getLastChild()).thenReturn(prototypeNode);
    when(prototypeNode.getLastChild()).thenReturn(stringNode);
    when(prototypeNode.getPreviousSibling()).thenReturn(parentNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    ArgumentCaptor<Node> getpropNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);

    // When
    renamePrototypes.new ProcessProperties().visit(null, rootNode, parentNode);

    // Then
    verify(renamePrototypes).getProperty(oldNameCaptor.capture()).prototypeCount++;
    assertEquals("prototype", oldNameCaptor.getValue());
    verify(renamePrototypes).properties.putIfAbsent(oldNameCaptor.getValue(), new RenamePrototypes.Property(oldNameCaptor.getValue()));
  }

  @Test
  public void testProcessProperties_StringNode_ObjectLiteral() {
    // Given
    Node rootNode = mock(Node.class);
    Node stringNode = mock(Node.class);
    Node objLitNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(rootNode.getLastChild()).thenReturn(objLitNode);
    when(objLitNode.getFirstChild()).thenReturn(stringNode);
    when(parentNode.getNext()).thenReturn(objLitNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    ArgumentCaptor<Node> objLitNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);

    // When
    renamePrototypes.new ProcessProperties().visit(null, rootNode, parentNode);

    // Then
    verifyZeroIterations(renamePrototypes.stringNodes);
    verify(renamePrototypes).getProperty(oldNameCaptor.capture()).objLitCount++;
    assertEquals("", oldNameCaptor.getValue());
    verify(renamePrototypes).properties.putIfAbsent(oldNameCaptor.getValue(), new RenamePrototypes.Property(oldNameCaptor.getValue()));
  }

  @Test
  public void testProcessProperties_NumberNode() {
    // Given
    Node rootNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    Node numberNode = mock(Node.class);

    when(parentNode.getNext()).thenReturn(numberNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When
    renamePrototypes.new ProcessProperties().visit(null, rootNode, parentNode);

    // Then
    verifyZeroIterations(renamePrototypes.stringNodes);
  }

  @Test
  public void testProcessPrototypeParent_GetProp() {
    // Given
    Node prototypeNode = mock(Node.class);
    Node getpropNode = mock(Node.class);
    Node stringNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(prototypeNode.getLastChild()).thenReturn(getpropNode);
    when(getpropNode.getLastChild()).thenReturn(stringNode);
    when(getpropNode.getFirstChild()).thenReturn(mock(Token.class));

    when(parentNode.getNext()).thenReturn(prototypeNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    ArgumentCaptor<Node> getpropNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);

    // When
    renamePrototypes.processPrototypeParent(parentNode, input);

    // Then
    verify(renamePrototypes).markPrototypePropertyCandidate(stringNode, input);
  }

  @Test
  public void testProcessPrototypeParent_Call() {
    // Given
    Node prototypeNode = mock(Node.class);
    Node callNode = mock(Node.class);
    Node mapNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(prototypeNode.getLastChild()).thenReturn(callNode);
    when(callNode.getLastChild()).thenReturn(mapNode);
    when(parentNode.getNext()).thenReturn(prototypeNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When
    renamePrototypes.processPrototypeParent(parentNode, input);

    // Then
    verify(renamePrototypes).processPrototype(mapNode, input);
  }

  @Test
  public void testProcessPrototype_ObjectLit() {
    // Given
    Node mapNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(parentNode.getNext()).thenReturn(mapNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    ArgumentCaptor<Node> mapNodeCaptor = ArgumentCaptor.forClass(Node.class);

    // When
    renamePrototypes.processPrototype(parentNode, input);

    // Then
    verify(renamePrototypes).processPrototypeObjLit(parentNode, input);
  }

  @Test
  public void testProcessPrototypeObjLit_Null() {
    // Given
    Node parentNode = mock(Node.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    // When
    renamePrototypes.processPrototypeObjLit(parentNode, input);

    // Then
    verifyZeroIterations(renamePrototypes.stringNodes);
  }

  @Test
  public void testProcessPrototypeObjLit_ObjectLit() {
    // Given
    Node objLitNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(parentNode.getNext()).thenReturn(objLitNode);

    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    ArgumentCaptor<Node> objLitNodeCaptor = ArgumentCaptor.forClass(Node.class);

    // When
    renamePrototypes.processPrototypeObjLit(parentNode, input);

    // Then
    verify(renamePrototypes, times(objLitNode.getNumChildren())).markObjLitPropertyCandidate(objLitNode.getChild(ArgumentMatchers.anyInt()), input);
    verifyZeroIterations(renamePrototypes.stringNodes);
  }

  @Test
  public void testGetProperty_NewProperty() {
    // Given
    String name = "newProperty";

    // When
    Property property = renamePrototypes.getProperty(name);

    // Then
    assertNull(property.newName);
    assertEquals(name, property.oldName);
    assertEquals(0, property.prototypeCount);
    assertEquals(0, property.objLitCount);
    assertEquals(0, property.refCount);
  }

  @Test
  public void testGetProperty_ExistingProperty() {
    // Given
    String name = "existingProperty";
    Property property = new RenamePrototypes.Property(name);
    renamePrototypes.properties.put(name, property);

    // When
    Property result = renamePrototypes.getProperty(name);

    // Then
    assertEquals(property, result);
  }

  @Test
  public void testGetProperty_NonExistentProperty() {
    // Given
    String name = "nonExistentProperty";

    // When
    Property result = renamePrototypes.getProperty(name);

    // Then
    assertNull(result);
  }

  @Test
  public void testGetPropertyMap_EmptyProperties() {
    // Given

    // When
    VariableMap result = renamePrototypes.getPropertyMap();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetPropertyMap_PropertiesWithNewNames() {
    // Given
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
    String name1 = "name1";
    String name2 = "name2";
    String newName1 = "newName1";
    String newName2 = "newName2";

    Property property1 = new RenamePrototypes.Property(name1);
    Property property2 = new RenamePrototypes.Property(name2);

    renamePrototypes.properties.put(name1, property1);
    renamePrototypes.properties.put(name2, property2);

    property1.newName = newName1;
    property2.newName = newName2;

    // When
    VariableMap result = renamePrototypes.getPropertyMap();

    // Then
    Map<String, String> map = result.getVariableMap();
    assertEquals(2, map.size());
    assertEquals(newName1, map.get(name1));
    assertEquals(newName2, map.get(name2));
  }

  @Test
  public void testGetPropertyMap_PropertiesWithoutNewNames() {
    // Given
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
    String name1 = "name1";
    String name2 = "name2";

    Property property1 = new RenamePrototypes.Property(name1);
    Property property2 = new RenamePrototypes.Property(name2);

    renamePrototypes.properties.put(name1, property1);
    renamePrototypes.properties.put(name2, property2);

    // When
    VariableMap result = renamePrototypes.getPropertyMap();

    // Then
    Map<String, String> map = result.getVariableMap();
    assertEquals(2, map.size());
    assertNull(map.get(name1));
    assertNull(map.get(name2));
  }

  @Test(expected = Preconditions.CheckStateException.class)
  public void testProcess_InvalidLifeCycleStage() {
    // Given
    Node externs = mock(Node.class);
    Node rootNode = mock(Node.class);
    when(compiler.getCodingConvention()).thenReturn(mock(CompilerPass.CodingConvention.class));

    when(compiler.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.PARSED);

    // When
    renamePrototypes.process(externs, rootNode);

    // Then
    throw new AssertionError("Should have thrown Preconditions.CheckStateException");
  }

  private void verifyZeroIterations(Set<Node> set) {
    verify(set, never()).iterator();
  }
}