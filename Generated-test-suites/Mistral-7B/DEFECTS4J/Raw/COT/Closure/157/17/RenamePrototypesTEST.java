import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private NodeTraversal nodeTraversal;

  @Mock
  private CompilerInput input;

  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
    when(compiler.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.NORMALIZED);
    when(compiler.addToDebugLog(anyString())).thenReturn(true);

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testNewInstance() {
    RenamePrototypes instance = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);
    assertNotNull(instance);
  }

  @Test
  public void testProcessWithEmptyExternsAndRoot() {
    renamePrototypes.process(mock(Node.class), mock(Node.class));
    verify(compiler, times(1)).setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED);
  }

  @Test
  public void testProcessWithNotNormalizedInput() {
    when(compiler.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.PARSED);
    renamePrototypes.process(mock(Node.class), mock(Node.class));
    verify(compiler, never()).setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED);
  }

  @Test
  public void testProcessWithExternsAndRoot() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    when(compiler.getCodingConvention())
        .thenReturn(mock(CodingConvention.class));

    when(compiler.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.NORMALIZED);

    when(compiler.addToDebugLog(anyString())).thenReturn(true);

    when(input.getRoot()).thenReturn(root);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraversal(input)).thenReturn(nodeTraversal);

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    renamePrototypes.process(externs, root);

    verify(compiler, times(1)).setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED);
  }

  @Test
  public void testGetPropertyMap_EmptyMap() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);
    VariableMap map = renamePrototypes.getPropertyMap();
    assertEquals(0, map.size());
  }

  @Test
  public void testGetPropertyMap_SingleProperty() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);
    Property property = new Property("foo");
    Map<String, Property> properties = new HashMap<>();
    properties.put("foo", property);

    RenamePrototypes spy = spy(renamePrototypes);
    when(spy.properties).thenReturn(properties);

    VariableMap map = spy.getPropertyMap();
    assertEquals(1, map.size());
    assertEquals("foo", map.get("foo").getName());
  }

  @Test
  public void testCanRenamePrototypeProperty_PrivateName() {
    Property property = new Property("_foo");
    assertTrue(property.canRename());
  }

  @Test
  public void testCanRenamePrototypeProperty_ExportedName() {
    Property property = new Property("bar");
    assertFalse(property.canRename());
  }

  @Test
  public void testCanRenamePrototypeProperty_AggressiveRenaming() {
    Property property = new Property("fooBar");
    assertTrue(property.canRename());
  }

  @Test
  public void testCanRenamePrototypeProperty_CapitalLetter() {
    Property property = new Property("fooFoo");
    assertTrue(property.canRename());
  }

  @Test
  public void testCanRenameObjLitProperty_PrivateName() {
    Property property = new Property("_foo");
    assertTrue(property.canRename());
  }

  @Test
  public void testCanRenameObjLitProperty_ExportedName() {
    Property property = new Property("bar");
    assertFalse(property.canRename());
  }

  @Test
  public void testCanRenameObjLitProperty_QuotedString() {
    Property property = new Property("\"foo\"");
    assertFalse(property.canRename());
  }

  @Test
  public void testCanRenameObjLitProperty_NormalString() {
    Property property = new Property("foo");
    assertFalse(property.canRename());
  }

  @Test
  public void testCanRenamePrototypePropertyAndObjLitProperty_BothFalse() {
    Property property = new Property("foo");
    assertFalse(property.canRename());
  }

  @Test
  public void testCanRenamePrototypePropertyAndObjLitProperty_BothTrue() {
    Property property = new Property("_foo");
    assertTrue(property.canRename());
  }

  @Test
  public void testCanRenamePrototypePropertyAndObjLitProperty_PrototypeIsFalse() {
    Property property = new Property("foo");
    assertFalse(property.canRename());
  }

  @Test
  public void testCanRenamePrototypePropertyAndObjLitProperty_ObjLitIsFalse() {
    Property property = new Property("_foo");
    assertTrue(property.canRename());
  }

  @Test
  public void testCount_PrototypeIsZero() {
    Property property = new Property("foo");
    assertEquals(0, property.count());
  }

  @Test
  public void testCount_PrototypeIsOne() {
    Property property = new Property("foo");
    property.prototypeCount = 1;
    assertEquals(1, property.count());
  }

  @Test
  public void testCount_ObjLitIsZero() {
    Property property = new Property("foo");
    assertEquals(0, property.count());
  }

  @Test
  public void testCount_ObjLitIsOne() {
    Property property = new Property("foo");
    property.objLitCount = 1;
    assertEquals(1, property.count());
  }

  @Test
  public void testCount_PrototypeAndObjLitAreOne() {
    Property property = new Property("foo");
    property.prototypeCount = 1;
    property.objLitCount = 1;
    assertEquals(2, property.count());
  }

  @Test
  public void testCount_PrototypeAndObjLitAreBothZero() {
    Property property = new Property("foo");
    assertEquals(0, property.count());
  }

  @Test
  public void testGetProperty_NoPropertyFound() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);
    Property property = renamePrototypes.getProperty("foo");
    assertNull(property);
  }

  @Test
  public void testGetProperty_PropertyFound() {
    Property property = new Property("foo");
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);
    Map<String, Property> properties = new HashMap<>();
    properties.put("foo", property);
    when(renamePrototypes.properties).thenReturn(properties);

    Property result = renamePrototypes.getProperty("foo");
    assertEquals(property, result);
  }

  @Test
  public void testProcessProperties_NoPrototypePropertyAccess() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.GETPROP);
    Node dest = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getNext()).thenReturn(dest);
    when(dest.getType()).thenReturn(Token.STRING);
    when(dest.getString()).thenReturn("prototype");

    renamePrototypes.processProperties(nodeTraversal, input);

    verify(nodeTraversal, never()).traverse(any(Node.class), any(AbstractPostOrderCallback.class));
  }

  @Test
  public void testProcessProperties_PrototypePropertyAccessWithPrototypeName() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.GETPROP);
    Node dest = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getNext()).thenReturn(dest);
    when(dest.getType()).thenReturn(Token.STRING);
    when(dest.getString()).thenReturn("prototype");

    Node prototype = mock(Node.class);
    when(prototype.getType()).thenReturn(Token.GETPROP);
    when(prototype.getFirstChild()).thenReturn(mock(Node.class));
    when(prototype.getFirstChild().getNext()).thenReturn(mock(Node.class));
    when(prototype.getLastChild()).thenReturn(mock(Node.class));
    when(input.getRoot()).thenReturn(prototype);

    renamePrototypes.processProperties(nodeTraversal, input);

    verify(nodeTraversal, times(1)).traverse(prototype, any(AbstractPostOrderCallback.class));
  }

  @Test
  public void testProcessProperties_PrototypePropertyAccessWithPropertyName() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.GETPROP);
    Node dest = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getNext()).thenReturn(dest);
    when(dest.getType()).thenReturn(Token.STRING);
    when(dest.getString()).thenReturn("prototype");
    when(input.getRoot()).thenReturn(mock(Node.class));

    renamePrototypes.processProperties(nodeTraversal, input);

    verify(nodeTraversal, never()).traverse(any(Node.class), any(AbstractPostOrderCallback.class));
  }

  @Test
  public void testProcessProperties_ObjLitPropertyAccessWithQuotedString() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.GETPROP);
    Node dest = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getNext()).thenReturn(dest);
    when(dest.getType()).thenReturn(Token.STRING);
    when(dest.getString()).thenReturn("\"foo\"");

    renamePrototypes.processProperties(nodeTraversal, input);

    verify(nodeTraversal, never()).traverse(any(Node.class), any(AbstractPostOrderCallback.class));
  }

  @Test
  public void testProcessProperties_ObjLitPropertyAccessWithNormalString() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.GETPROP);
    Node dest = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getNext()).thenReturn(dest);
    when(dest.getType()).thenReturn(Token.STRING);
    when(dest.getString()).thenReturn("foo");

    renamePrototypes.processProperties(nodeTraversal, input);

    verify(nodeTraversal, times(1)).traverse(dest, any(AbstractPostOrderCallback.class));
  }

  @Test
  public void testProcessProperties_ObjLitAssignmentToPrototype() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.ASSIGN);
    Node map = mock(Node.class);
    when(node.getLastChild()).thenReturn(map);
    when(map.getType()).thenReturn(Token.OBJECTLIT);

    renamePrototypes.processProperties(nodeTraversal, input);

    verify(nodeTraversal, times(1)).traverse(map, any(AbstractPostOrderCallback.class));
  }
}