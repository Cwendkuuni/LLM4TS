package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;

import java.util.List;

public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    JSType expectedType = registry.getType("String");

    when(registry.getType(reference)).thenReturn(expectedType);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testResolveViaProperties() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "String.prototype.charAt";
    String[] componentNames = reference.split("\\.");
    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(enclosing.getSlot(componentNames[0])).thenReturn(slot);

    JSType slotType = mock(JSType.class);
    when(slot.getType()).thenReturn(slotType);
    ObjectType parentClass = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);
    when(parentClass.getPropertyType(componentNames[componentNames.length - 1]))
        .thenReturn(functionType.getInstanceType());
    when(slotType).thenReturn(parentClass);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertEquals(functionType.getInstanceType(), actualType);
  }

  @Test
  public void testResolveViaPropertiesWithNullType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "String.prototype.charAt";
    String[] componentNames = reference.split("\\.");
    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(enclosing.getSlot(componentNames[0])).thenReturn(slot);

    JSType slotType = null;
    when(slot.getType()).thenReturn(slotType);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertNull(actualType);
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "String.prototype.x";
    String[] componentNames = reference.split("\\.");
    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(enclosing.getSlot(componentNames[0])).thenReturn(slot);

    JSType slotType = mock(JSType.class);
    when(slot.getType()).thenReturn(slotType);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertNull(actualType);
  }

  @Test
  public void testResolveViaRegistryWithUnknownType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "UnknownType";
    when(registry.getType(reference)).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertNull(actualType);
  }

  @Test
  public void testResolveWithEnumType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "MyEnum.MY_ENUM_CONSTANT";
    String[] componentNames = reference.split("\\.");
    EnumType enumType = mock(EnumType.class);
    when(registry.getType(componentNames[0])).thenReturn(enumType);
    EnumElementType enumElementType = mock(EnumElementType.class);
    when(enumType.getElementsType()).thenReturn(enumElementType);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertEquals(enumElementType, actualType);
  }

  @Test
  public void testResolveWithFunctionType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "MyFunction";
    FunctionType functionType = mock(FunctionType.class);
    when(registry.getType(reference)).thenReturn(functionType);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertEquals(functionType, actualType);
  }

  @Test
  public void testResolveWithNoObjectType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "Object";
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testResolveWithNoType() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    String reference = "undefined";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertNull(actualType);
  }

  @Test
  public void testFinishPropertyContinuations() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    NamedType namedType = new NamedType(registry, "MyType", null, 0, 0);

    ObjectType referencedObjType = mock(ObjectType.class);
    when(registry.isLastGeneration()).thenReturn(false);
    when(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)).thenReturn(referencedObjType);
    when(namedType.getReferencedTypeInternal()).thenReturn(referencedObjType);

    PropertyContinuation propertyContinuation1 =
        new PropertyContinuation("prop1", registry.getNativeStringType(), true, null);
    PropertyContinuation propertyContinuation2 =
        new PropertyContinuation("prop2", registry.getNativeNumberType(), false, null);
    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();

    namedType.propertyContinuations = propertyContinuations;

    namedType.finishPropertyContinuations();

    ObjectType target = mock(ObjectType.class);

    propertyContinuation1.commit(target);
    propertyContinuation2.commit(target);

    List<PropertyDescriptor> propertyDescriptors = target.getOwnPropertyDescriptors();

    assertEquals(2, propertyDescriptors.size());

    PropertyDescriptor propertyDescriptor1 = propertyDescriptors.get(0);
    assertEquals("prop1", propertyDescriptor1.getName());
    assertEquals(registry.getNativeStringType(), propertyDescriptor1.getValue());
    assertTrue(propertyDescriptor1.isWritable());
    assertTrue(propertyDescriptor1.isEnumerable());
    assertFalse(propertyDescriptor1.isConfigurable());

    PropertyDescriptor propertyDescriptor2 = propertyDescriptors.get(1);
    assertEquals("prop2", propertyDescriptor2.getName());
    assertEquals(registry.getNativeNumberType(), propertyDescriptor2.getValue());
    assertTrue(propertyDescriptor2.isWritable());
    assertTrue(propertyDescriptor2.isEnumerable());
    assertFalse(propertyDescriptor2.isConfigurable());
  }

  @Test
  public void testSetValidator() {
    ErrorReporter t = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    NamedType namedType = new NamedType(registry, "MyType", null, 0, 0);

    Predicate<JSType> validator = jstype -> jstype instanceof FunctionType;

    when(registry.isLastGeneration()).thenReturn(false);

    namedType.setValidator(validator);

    JSType actualType = namedType.resolveInternal(t, enclosing);

    assertTrue(validator.apply(actualType));
  }
}