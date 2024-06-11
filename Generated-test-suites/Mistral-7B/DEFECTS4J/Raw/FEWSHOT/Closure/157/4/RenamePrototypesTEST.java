package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    Compiler compiler = Mockito.mock(Compiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node root = Mockito.mock(Node.class);
    Node externs = Mockito.mock(Node.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    renamePrototypes.process(externs, root);

    // Test renaming a private prototype property
    String oldNamePrivate = "foo_";
    String newNamePrivate = "bar_";

    Property propertyPrivate = new Property(oldNamePrivate);
    renamePrototypes.properties.put(oldNamePrivate, propertyPrivate);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNamePrivate)).thenReturn(true);

    renamePrototypes.process(externs, root);

    assertEquals(newNamePrivate, propertyPrivate.newName);

    // Test renaming an exported prototype property, should not rename
    String oldNameExported = "toString";
    String newNameExported = "toString";

    Property propertyExported = new Property(oldNameExported);
    renamePrototypes.properties.put(oldNameExported, propertyExported);

    Mockito.when(compiler.getCodingConvention().isExported(oldNameExported)).thenReturn(true);

    renamePrototypes.process(externs, root);

    assertNotNull(propertyExported.newName);
    assertEquals(oldNameExported, propertyExported.newName);

    // Test renaming an internal prototype property with aggressive renaming
    String oldNameAggressive = "fooBar";
    String newNameAggressive = "bazBaz";

    Property propertyAggressive = new Property(oldNameAggressive);
    renamePrototypes.properties.put(oldNameAggressive, propertyAggressive);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNameAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isExported(oldNameAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isAggressiveRenaming()).thenReturn(true);

    renamePrototypes.process(externs, root);

    assertEquals(newNameAggressive, propertyAggressive.newName);

    // Test renaming an internal prototype property without aggressive renaming
    String oldNameNotAggressive = "fooBar";
    String newNameNotAggressive = "fooBaz";

    Property propertyNotAggressive = new Property(oldNameNotAggressive);
    renamePrototypes.properties.put(oldNameNotAggressive, propertyNotAggressive);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNameNotAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isExported(oldNameNotAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isAggressiveRenaming()).thenReturn(false);

    renamePrototypes.process(externs, root);

    assertNotNull(propertyNotAggressive.newName);
    char[] chars = oldNameNotAggressive.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (!Character.isLetterOrDigit(chars[i])) {
        assertEquals(newNameNotAggressive, propertyNotAggressive.newName);
        break;
      }
    }
  }

  @Test
  public void testRenameObjLitProperties() {
    Compiler compiler = Mockito.mock(Compiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node root = Mockito.mock(Node.class);
    Node externs = Mockito.mock(Node.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    renamePrototypes.process(externs, root);

    // Test renaming an internal obj lit property with aggressive renaming
    String oldNameAggressive = "fooBar";
    String newNameAggressive = "bazBaz";

    Property propertyAggressive = new Property(oldNameAggressive);
    renamePrototypes.properties.put(oldNameAggressive, propertyAggressive);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNameAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isExported(oldNameAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isAggressiveRenaming()).thenReturn(true);

    renamePrototypes.process(externs, root);

    assertEquals(newNameAggressive, propertyAggressive.newName);

    // Test renaming an internal obj lit property without aggressive renaming
    String oldNameNotAggressive = "fooBar";
    String newNameNotAggressive = "fooBaz";

    Property propertyNotAggressive = new Property(oldNameNotAggressive);
    renamePrototypes.properties.put(oldNameNotAggressive, propertyNotAggressive);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNameNotAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isExported(oldNameNotAggressive)).thenReturn(false);
    Mockito.when(compiler.getCodingConvention().isAggressiveRenaming()).thenReturn(false);

    renamePrototypes.process(externs, root);

    assertNotNull(propertyNotAggressive.newName);
    char[] chars = oldNameNotAggressive.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == '_') {
        assertEquals(newNameNotAggressive, propertyNotAggressive.newName);
        break;
      }
    }
  }

  @Test
  public void testUpdateStringNodes() {
    Compiler compiler = Mockito.mock(Compiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node root = Mockito.mock(Node.class);
    Node externs = Mockito.mock(Node.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    renamePrototypes.process(externs, root);

    // Test updating a string node with an internal prototype property name
    String oldNamePrototype = "foo_";
    String newNamePrototype = "bar_";

    Property propertyPrototype = new Property(oldNamePrototype);
    renamePrototypes.properties.put(oldNamePrototype, propertyPrototype);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNamePrototype)).thenReturn(true);

    Node stringNode = Mockito.mock(Node.class);
    Mockito.when(stringNode.getString()).thenReturn(oldNamePrototype);

    renamePrototypes.stringNodes.add(stringNode);

    renamePrototypes.process(externs, root);

    assertEquals(newNamePrototype, stringNode.getString());

    // Test updating a string node with an internal obj lit property name
    String oldNameObjLit = "foo_";
    String newNameObjLit = "bar_";

    Property propertyObjLit = new Property(oldNameObjLit);
    renamePrototypes.properties.put(oldNameObjLit, propertyObjLit);

    Mockito.when(compiler.getCodingConvention().isPrivate(oldNameObjLit)).thenReturn(true);

    Node stringNodeObjLit = Mockito.mock(Node.class);
    Mockito.when(stringNodeObjLit.getString()).thenReturn(oldNameObjLit);

    renamePrototypes.stringNodes.add(stringNodeObjLit);

    renamePrototypes.process(externs, root);

    assertEquals(newNameObjLit, stringNodeObjLit.getString());
  }
}