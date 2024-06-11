package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeRegistry;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Test cases for {@link NamedType} class.
 */
public class NamedTypeTest {

  private static final String REFERENCE_A = "A";
  private static final String REFERENCE_B = "B";
  private static final String REFERENCE_C = "C";
  private static final String REFERENCE_D = "D";
  private static final String SOURCE_NAME = "NamedTypeTest";

  private int LINE_NUMBER = 1;
  private int CHAR_NUMBER = 1;

  private StaticScope<JSType> enclosingScope;
  private TypeRegistry registry;
  private ErrorReporter errorReporter;
  private NamedType namedTypeA;
  private NamedType namedTypeB;
  private NamedType namedTypeC;
  private NamedType namedTypeD;
  private ObjectType objType;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    registry = mock(TypeRegistry.class);

    enclosingScope = mock(StaticScope.class);
    objType = mock(ObjectType.class);

    when(registry.getType(REFERENCE_A)).thenReturn(getObjectType("A"));
    when(registry.getType(REFERENCE_B)).thenReturn(getObjectType("B"));
    when(registry.getType(REFERENCE_C)).thenReturn(getObjectType("C"));
    when(registry.getType(REFERENCE_D)).thenReturn(null);
    when(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE))
        .thenReturn(getObjectType("UnknownType"));

    when(registry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE))
        .thenReturn(getFunctionType("NoObjectType"));

    when(enclosingScope.getSlot(REFERENCE_A)).thenReturn(
        new StaticSlot<JSType>() {{
          setType(getObjectType("A"));
        }});
    when(objType.getPropertyType(REFERENCE_B)).thenReturn(getObjectType("BProperty"));

    namedTypeA = new NamedType(registry, REFERENCE_A, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeB = new NamedType(registry, REFERENCE_B, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeC = new NamedType(registry, REFERENCE_C, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeD = new NamedType(registry, REFERENCE_D, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedTypeA.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedTypeA.isNominalType());
  }

  @Test
  public void testGetReferenceName() {
    assertEquals(REFERENCE_A, namedTypeA.getReferenceName());
  }

  @Test
  public void testToStringHelper() {
    assertEquals(REFERENCE_A, namedTypeA.toStringHelper(false));
  }

  @Test
  public void testHasReferenceName() {
    assertTrue(namedTypeA.hasReferenceName());
  }

  @Test
  public void testIsResolved() {
    assertFalse(namedTypeA.isResolved());
  }

  @Test
  public void testGetReferencedTypeInternal() {
    assertNull(namedTypeA.getReferencedTypeInternal());
  }

  @Test
  public void testResolveInternal() {
    resolveTypeFor(namedTypeA, REFERENCE_A);
    assertSame(getObjectType("A"), namedTypeA.getReferencedType());
    assertSame(getObjectType("A"), namedTypeA.getResolvedType());
  }

  @Test(expected = TypeError.class)
  public void testResolveTypeCycle() {
    when(registry.getType(REFERENCE_A)).thenReturn(namedTypeA);
    when(registry.getType(REFERENCE_B)).thenReturn(getObjectType("B"));

    namedTypeA = new NamedType(registry, REFERENCE_A, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeB = new NamedType(registry, REFERENCE_B, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);

    namedTypeA.resolveInternal(errorReporter, enclosingScope);
  }

  @Test
  public void testResolveViaRegistry() {
    resolveTypeFor(namedTypeA, REFERENCE_A);
    assertSame(getObjectType("A"), namedTypeA.getReferencedType());
    assertSame(getObjectType("A"), namedTypeA.getResolvedType());
  }

  @Test
  public void testResolveViaProperties() {
    namedTypeA = new NamedType(registry, REFERENCE_A, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeB = new NamedType(registry, REFERENCE_B, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);

    when(registry.getType(REFERENCE_C)).thenReturn(getObjectType("C"));
    when(objType.getPropertyType(REFERENCE_B)).thenReturn(getObjectType("BProperty"));

    namedTypeA.resolveInternal(errorReporter, enclosingScope);
    assertSame(getObjectType("C"), namedTypeA.getReferencedType());
    assertSame(getObjectType("C"), namedTypeA.getResolvedType());
  }

  @Test
  public void testResolveWithValidator() {
    JSType validator = mock(JSType.class);
    when(registry.getType(REFERENCE_A)).thenReturn(getObjectType("A"));

    namedTypeA = new NamedType(registry, REFERENCE_A, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeA.setValidator(validator);

    namedTypeA.resolveInternal(errorReporter, enclosingScope);
    ArgumentCaptor<JSType> captor = ArgumentCaptor.forClass(JSType.class);
    verify(validator).apply(captor.capture());
    assertSame(getObjectType("A"), captor.getValue());
  }

  @Test
  public void testUnresolvedType() {
    when(registry.getType(REFERENCE_D)).thenReturn(null);

    namedTypeD = new NamedType(registry, REFERENCE_D, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeD.resolveInternal(errorReporter, enclosingScope);

    assertSame(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        namedTypeD.getReferencedType());
    assertSame(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        namedTypeD.getResolvedType());
  }

  @Test
  public void testForwardDeclaredType() {
    when(registry.isForwardDeclaredType(REFERENCE_A)).thenReturn(true);

    namedTypeA = new NamedType(registry, REFERENCE_A, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeA.resolveInternal(errorReporter, enclosingScope);

    assertNull(namedTypeA.getReferencedType());
    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        namedTypeA.getResolvedType());
  }

  @Test
  public void testDefinitionProperties() {
    JSTypeType propertyType = mock(JSTypeType.class);
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    when(propertyType.getName()).thenReturn("propertyName");
    when(propertyType.isFunctionType()).thenReturn(false);
    when(propertyType.isInterface()).thenReturn(false);
    when(propertyType.getInstanceType()).thenReturn(type);

    when(objType.getPropertyType("propertyName")).thenReturn(propertyType);

    namedTypeA = new NamedType(registry, REFERENCE_A, SOURCE_NAME, LINE_NUMBER,
        CHAR_NUMBER);
    namedTypeA.resolveInternal(errorReporter, enclosingScope);

    namedTypeA.defineProperty("propertyName", propertyType, false, propertyNode);
    assertSame(propertyType, namedTypeA.getProperty("propertyName"));
  }

  private void resolveTypeFor(NamedType namedType, String reference) {
    namedType.resolveInternal(errorReporter, enclosingScope);
  }

  private ObjectType getObjectType(String name) {
    return new ObjectType(registry, null, name, null, null, null,
        Lists.newArrayList(), null, null, null, null, null);
  }

  private FunctionType getFunctionType(String name) {
    return new FunctionType(registry, null, null, null, null, null,
        null, null, null, null, null, null, null);
  }
}