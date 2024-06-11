package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.TypeFactory;

public class NamedTypeTest {

  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private TypeFactory typeFactory;
  private NamedType namedType;

  @Before
  public void setup() {
    this.errorReporter = mock(ErrorReporter.class);
    this.staticScope = mock(StaticScope.class);
    this.typeFactory = mock(TypeFactory.class);

    when(staticScope.getTypeFactory()).thenReturn(typeFactory);

    this.namedType = new NamedType(
        typeFactory.getTypeRegistry(), "TestType", "TestSource", 1, 1);
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsResolved() {
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("TestType", namedType.getReferenceName());
  }

  @Test
  public void testToStringHelper() {
    assertEquals("TestType", namedType.toStringHelper(false));
  }

  @Test
  public void testHasReferenceName() {
    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testHashCode() {
    assertEquals(namedType.hashCode(), "TestType".hashCode());
  }

  @Test(expected = TypeError.class)
  public void testDefinePropertyUnresolvedType() {
    namedType.defineProperty("testProperty", null, false, null);
  }

  @Test
  public void testDefinePropertyResolvedType() {
    JSType referencedType = TypeFactory.newObjectType("TestObject");
    namedType.setReferencedAndResolvedType(referencedType, null, null);
    namedType.defineProperty("testProperty", null, false, null);
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSType referencedType = TypeFactory.newObjectType("TestObject");
    namedType.setReferencedAndResolvedType(referencedType, null, null);
    namedType.finishPropertyContinuations();

    ObjectType testObject = (ObjectType) referencedType;
    assertFalse(testObject.isResolved());
    assertEquals(1, testObject.getPropertyCount());
    assertEquals("testProperty", testObject.getPropertyNames()[0]);
  }

  @Test
  public void testGetReferencedType() {
    JSType referencedType = TypeFactory.newObjectType("TestObject");
    namedType.setReferencedAndResolvedType(referencedType, null, null);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testGetReferencedTypeInternal() {
    JSType referencedType = TypeFactory.newObjectType("TestObject");
    namedType.setReferencedTypeInternal(referencedType);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveInternal() {
    when(typeFactory.getTypeRegistry().getType("TestObject"))
        .thenReturn(TypeFactory.newObjectType("TestObject"));

    namedType.resolveInternal(errorReporter, staticScope);

    JSType referencedType = namedType.getReferencedType();
    assertEquals("TestObject", referencedType.getClass().getSimpleName());
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testResolveViaRegistry() {
    when(typeFactory.getTypeRegistry().getType("TestObject"))
        .thenReturn(TypeFactory.newObjectType("TestObject"));

    NamedType namedType = new NamedType(
        typeFactory.getTypeRegistry(), "TestObject", "TestSource", 1, 1);

    namedType.resolveInternal(errorReporter, staticScope);

    JSType referencedType = namedType.getReferencedType();
    assertEquals("TestObject", referencedType.getClass().getSimpleName());
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testResolveViaProperties() {
    StaticSlot<JSType> slot = new StaticSlot<>("TestObject", null, null);
    when(staticScope.getSlot("TestObject")).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    ObjectType testObject = TypeFactory.newObjectType("TestObject");
    when(slot.getType()).thenReturn(testObject);

    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "testProperty", null, false, null);

    when(testObject.getPropertyType("testProperty")).thenReturn(null);
    when(testObject.getPropertyType("testProperty")).thenReturn(
        TypeFactory.newStringType());

    when(testObject.getPropertyContinuations()).thenReturn(Lists.newArrayList(propertyContinuation));

    NamedType namedType = new NamedType(
        typeFactory.getTypeRegistry(), "TestObject.testProperty", "TestSource", 1, 1);

    namedType.resolveInternal(errorReporter, staticScope);

    JSType referencedType = namedType.getReferencedType();
    assertEquals(TypeFactory.newStringType(), referencedType);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testLookupViaProperties() {
    StaticSlot<JSType> slot = new StaticSlot<>("TestObject", null, null);
    when(staticScope.getSlot("TestObject")).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    ObjectType testObject1 = TypeFactory.newObjectType("TestObject1");
    ObjectType testObject2 = TypeFactory.newObjectType("TestObject2");
    ObjectType testObject3 = TypeFactory.newObjectType("TestObject3");

    when(testObject1.getPropertyType("testProperty")).thenReturn(testObject2);
    when(testObject2.getPropertyType("testProperty")).thenReturn(testObject3);

    NamedType namedType = new NamedType(
        typeFactory.getTypeRegistry(), "TestObject1.testObject2.testProperty", "TestSource", 1, 1);

    namedType.resolveInternal(errorReporter, staticScope);

    JSType referencedType = namedType.getReferencedType();
    assertEquals(testObject3, referencedType);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testSetReferencedAndResolvedType() {
    JSType referencedType = TypeFactory.newObjectType("TestObject");
    namedType.setReferencedAndResolvedType(referencedType, null, null);
    assertEquals(referencedType, namedType.getReferencedType());
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testSetReferencedTypeInternal() {
    JSType referencedType = TypeFactory.newObjectType("TestObject");
    namedType.setReferencedTypeInternal(referencedType);
    assertEquals(referencedType, namedType.getReferencedType());
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testHandleTypeCycle() {
    when(typeFactory.getTypeRegistry().getType("TestObject"))
        .thenThrow(new TypeError("Cycle detected in inheritance chain of type TestObject"));

    namedType.resolveInternal(errorReporter, staticScope);

    assertNull(namedType.getReferencedType());
    assertFalse(namedType.isResolved());
    assertEquals("TestObject", namedType.getReferencedName());
  }

  @Test
  public void testCheckEnumElementCycle() {
    ObjectType testObject = TypeFactory.newObjectType("TestObject");
    EnumElementType enumElementType = new EnumElementType(
        testObject, "TestEnumElement", null);
    namedType.setReferencedAndResolvedType(enumElementType, null, null);

    when(enumElementType.getPrimitiveType()).thenReturn(namedType);

    namedType.checkEnumElementCycle(errorReporter);

    assertNull(namedType.getReferencedType());
    assertFalse(namedType.isResolved());
    assertEquals("TestObject.TestEnumElement", namedType.getReferencedName());
  }

  @Test
  public void testGetTypedefType() {
    StaticSlot<JSType> slot = new StaticSlot<>("TestObject", null, null);
    when(staticScope.getSlot("TestObject")).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    ObjectType testObject = TypeFactory.newObjectType("TestObject");

    when(testObject.getPropertyType("testProperty")).thenReturn(null);
    when(testObject.getPropertyType("testProperty")).thenReturn(testObject);

    NamedType namedType = new NamedType(
        typeFactory.getTypeRegistry(), "TestObject.testProperty", "TestSource", 1, 1);

    namedType.resolveInternal(errorReporter, staticScope);

    JSType referencedType = namedType.getReferencedType();
    assertEquals(testObject, referencedType);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = mock(Predicate.class);

    namedType.setValidator(validator);
    assertSame(validator, namedType.getValidator());
  }
}