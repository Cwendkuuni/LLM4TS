package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.StaticType;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.NamedType;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test the NamedType class.
 */
public class NamedTypeTest {

  private ErrorReporter errorReporter;
  private NamedType namedType;
  private StaticScope<JSType> staticScope;
  private StaticSlot<JSType> staticSlot;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    staticScope = mock(StaticScope.class);
    staticSlot = mock(StaticSlot.class);
    when(staticScope.getSlot(anyString())).thenReturn(staticSlot);

    namedType = new NamedType(
        mock(JSTypeRegistry.class), "java.lang.String", null, 1, 1);
  }

  @Test
  public void testSendNullReferenceToConstructor() {
    try {
      new NamedType(null, null, null, 1, 1);
      fail("Expected TypeError");
    } catch (TypeError e) {
      assertEquals("TypeError: Unable to construct instance of type NamedType",
          e.getMessage());
    }
  }

  @Test
  public void testBasicFunctionality() {
    JSType referencedType = mock(JSType.class);
    NamedType namedTypeWithReferencedType = new NamedType(
        mock(JSTypeRegistry.class), "testType", null, 1, 1);

    when(namedTypeWithReferencedType.getReferencedType())
        .thenReturn(referencedType);

    assertEquals("testType", namedTypeWithReferencedType.toString());

    assertSame(referencedType, namedTypeWithReferencedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistry() {
    JSType referencedType = mock(JSType.class);
    JSType nativeObjectType = JSTypeNative.UNKNOWN_TYPE;

    when(registry.getType("testType"))
        .thenReturn(referencedType);

    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType", null, 1, 1);

    assertEquals(referencedType, namedType.resolveInternal(errorReporter, staticScope));
  }

  @Test
  public void testResolveViaProperties() {
    ObjectType parentObjectType = mock(ObjectType.class);
    JSType primitiveType = JSTypeNative.NUMBER_TYPE;
    EnumElementType enumElementType = mock(EnumElementType.class);
    FunctionType functionType = mock(FunctionType.class);
    InstanceType instanceType = mock(InstanceType.class);
    StaticType staticType = mock(StaticType.class);

    when(parentObjectType.getPropertyType("property"))
        .thenReturn(enumElementType);
    when(enumElementType.getPrimitiveType())
        .thenReturn(primitiveType);
    when(registry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE))
        .thenReturn(functionType);

    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType.property", null, 1, 1);

    ArgumentCaptor<JSType> captor = ArgumentCaptor.forClass(JSType.class);
    assertFalse(namedType.isResolved());
    namedType.resolveInternal(errorReporter, staticScope);
    assertTrue(namedType.isResolved());
    verify(staticScope).setSlot("testType", captor.capture());
    assertEquals(primitiveType, captor.getValue());
  }

  @Test
  public void testResolveWithImplicitPrototypeCycle() {
    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType", null, 1, 1);

    when(registry.isLastGeneration()).thenReturn(false);
    when(staticScope.getSlot("testType"))
        .thenReturn(namedType);

    namedType.resolveInternal(errorReporter, staticScope);
    namedType.handleTypeCycle(errorReporter);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getResolvedType());
  }

  @Test
  public void testResolveWithEnumElementCycle() {
    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType.property", null, 1, 1);

    JSType enumElementType = mock(EnumElementType.class);
    JSType primitiveType = JSTypeNative.NUMBER_TYPE;

    when(enumElementType.getPrimitiveType()).thenReturn(primitiveType);
    when(staticScope.getSlot("testType")).thenReturn(namedType);
    when(staticScope.getSlot("testType.property")).thenReturn(enumElementType);

    namedType.resolveInternal(errorReporter, staticScope);
    namedType.checkEnumElementCycle(errorReporter);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getResolvedType());
  }

  @Test
  public void testResolveWithUnresolvedType() {
    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType", null, 1, 1);

    when(registry.isLastGeneration()).thenReturn(false);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getResolvedType());
  }

  @Test
  public void testDefineProperty() {
    JSType referencedType = mock(JSType.class);
    JSType propertyType = JSTypeNative.STRING_TYPE;
    Node propertyNode = mock(Node.class);

    when(namedType.getReferencedType()).thenReturn(referencedType);
    when(referencedType.defineProperty(anyString(), any(JSType.class), anyBoolean(), any(Node.class)))
        .thenReturn(propertyType);

    namedType.defineProperty("property", null, false, propertyNode);

    assertEquals(propertyType, namedType.getPropertyType("property"));
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSType referencedObjectType = JSTypeNative.OBJECT_TYPE;
    JSType referencedType = mock(JSType.class);
    ObjectType referencedObjectTypeInstance = mock(ObjectType.class);
    JSType propertyType = JSTypeNative.STRING_TYPE;
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "property", propertyType, false, mock(Node.class));

    when(referencedType.getInstanceType())
        .thenReturn(referencedObjectTypeInstance);
    when(referencedObjectTypeInstance.isUnknownType())
        .thenReturn(false);
    when(referencedObjectTypeInstance.getProperties())
        .thenReturn(Lists.newArrayList());

    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType", null, 1, 1);
    namedType.propertyContinuations = Lists.newArrayList(propertyContinuation);

    namedType.finishPropertyContinuations();

    assertEquals(propertyType, namedType.getPropertyType("property"));
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = (type) -> true;

    namedType = new NamedType(
        mock(JSTypeRegistry.class), "testType", null, 1, 1);

    assertTrue(namedType.setValidator(validator));

    Optional<JSType> referencedTypeOptional = Optional.ofNullable(namedType.getReferencedType());
    validator.apply(referencedTypeOptional.orElse(JSTypeNative.UNKNOWN_TYPE));
  }
}