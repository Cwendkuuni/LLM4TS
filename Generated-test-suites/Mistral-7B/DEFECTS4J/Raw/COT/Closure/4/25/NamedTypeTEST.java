package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.StaticScopeTestCase;

import java.util.List;

/**
 * Test cases for {@link NamedType} class.
 */
public class NamedTypeTest extends StaticScopeTestCase {

  public void testCreateNamedType() {
    JSTypeRegistry registry = getTestRegistry();
    JSType namedType = new NamedType(registry, "String", "sourceName", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertEquals("sourceName", namedType.toStringHelper(true));
    assertFalse(namedType.isResolved());
  }

  public void testNamedTypeIsNamedType() {
    JSTypeRegistry registry = getTestRegistry();
    JSType namedType = new NamedType(registry, "String", "sourceName", 1, 1);
    assertTrue(namedType.isNamedType());
  }

  public void testNamedTypeIsNominalType() {
    JSTypeRegistry registry = getTestRegistry();
    JSType namedType = new NamedType(registry, "String", "sourceName", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  public void testNamedTypeIsResolvedWhenReferencedTypeIsResolved() {
    JSTypeRegistry registry = getTestRegistry();
    JSType referencedType = new IntType(registry);
    JSType namedType = new NamedType(registry, "IntType", "sourceName", 1, 1);
    namedType.setReferencedType(referencedType);
    namedType.setResolvedTypeInternal(referencedType);
    assertTrue(namedType.isResolved());
  }

  public void testNamedTypeIsNotResolvedWhenReferencedTypeIsNotResolved() {
    JSTypeRegistry registry = getTestRegistry();
    JSType namedType = new NamedType(registry, "IntType", "sourceName", 1, 1);
    assertFalse(namedType.isResolved());
  }

  public void testDefinePropertyWhenUnresolved() {
    JSTypeRegistry registry = getTestRegistry();
    JSType namedType = new NamedType(registry, "IntType", "sourceName", 1, 1);
    Node propertyNode = newNode("prop");
    namedType.defineProperty("prop", new IntType(registry), false, propertyNode);
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
  }

  public void testDefinePropertyWhenResolved() {
    JSTypeRegistry registry = getTestRegistry();
    JSType referencedType = new IntType(registry);
    JSType namedType = new NamedType(registry, "IntType", "sourceName", 1, 1);
    namedType.setReferencedType(referencedType);
    namedType.setResolvedTypeInternal(referencedType);
    Node propertyNode = newNode("prop");
    namedType.defineProperty("prop", new IntType(registry), false, propertyNode);
    assertTrue(namedType.isResolved());
    assertEquals(referencedType, namedType.getReferencedType());
  }

  public void testResolveViaRegistry() {
    JSTypeRegistry registry = getTestRegistry();
    registry.put("IntType", new IntType(registry));
    JSType namedType = new NamedType(registry, "IntType", "sourceName", 1, 1);
    JSType referencedType = namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>(registry));
    assertTrue(namedType.isResolved());
    assertEquals(registry.getType("IntType"), referencedType);
  }

  public void testResolveViaProperties() {
    JSTypeRegistry registry = getTestRegistry();
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    JSType propertyType = new IntType(registry);
    objectType.defineProperty("prop", propertyType, false, null);
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);
    scope.setSlot("ObjectType", objectType);
    JSType namedType = new NamedType(registry, "ObjectType.prop", "sourceName", 1, 1);
    JSType referencedType = namedType.resolveInternal(new ErrorReporter(), scope);
    assertTrue(namedType.isResolved());
    assertEquals(propertyType, referencedType);
  }

  public void testResolveViaPropertiesWithImplicitPrototypeCycle() {
    JSTypeRegistry registry = getTestRegistry();
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    ObjectType cycleType = new ObjectType(registry, "CycleType");
    JSType propertyType = new IntType(registry);
    objectType.defineProperty("cycle", cycleType, false, null);
    cycleType.defineProperty("object", objectType, false, null);
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);
    scope.setSlot("ObjectType", objectType);
    scope.setSlot("CycleType", cycleType);
    JSType namedType = new NamedType(registry, "ObjectType.cycle.object", "sourceName", 1, 1);
    ErrorReporter errorReporter = new ErrorReporter();
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertNotNull(namedType.getReferencedType());
  }

  public void testResolveViaPropertiesWithEnumElementCycle() {
    JSTypeRegistry registry = getTestRegistry();
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    EnumType enumType = new EnumType(registry, "EnumType", null);
    EnumElementType enumElementType = enumType.addElement("Element");
    JSType propertyType = new IntType(registry);
    objectType.defineProperty("enumElement", enumElementType, false, null);
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);
    scope.setSlot("ObjectType", objectType);
    JSType namedType = new NamedType(registry, "ObjectType.enumElement", "sourceName", 1, 1);
    ErrorReporter errorReporter = new ErrorReporter();
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertNotNull(namedType.getReferencedType());
  }

  public void testSetValidator() {
    JSTypeRegistry registry = getTestRegistry();
    JSType namedType = new NamedType(registry, "IntType", "sourceName", 1, 1);
    Predicate<JSType> validator = jstype -> jstype.isNumberType();
    namedType.setValidator(validator);
    JSType referencedType = new IntType(registry);
    namedType.setReferencedType(referencedType);
    namedType.setResolvedTypeInternal(referencedType);
    assertTrue(validator.apply(referencedType));
  }

  public void testResolveWithImplicitPrototypeCycleWarning() {
    JSTypeRegistry registry = getTestRegistry();
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    ObjectType cycleType = new ObjectType(registry, "CycleType");
    JSType propertyType = new IntType(registry);
    objectType.defineProperty("cycle", cycleType, false, null);
    cycleType.defineProperty("object", objectType, false, null);
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);
    scope.setSlot("ObjectType", objectType);
    scope.setSlot("CycleType", cycleType);
    JSType namedType = new NamedType(registry, "ObjectType.cycle.object", "sourceName", 1, 1);
    ErrorReporter errorReporter = new ErrorReporter();
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertEquals(1, errorReporter.getNumWarnings());
  }

  public void testResolveWithEnumElementCycleWarning() {
    JSTypeRegistry registry = getTestRegistry();
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    EnumType enumType = new EnumType(registry, "EnumType", null);
    EnumElementType enumElementType = enumType.addElement("Element");
    JSType propertyType = new IntType(registry);
    objectType.defineProperty("enumElement", enumElementType, false, null);
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);
    scope.setSlot("ObjectType", objectType);
    JSType namedType = new NamedType(registry, "ObjectType.enumElement", "sourceName", 1, 1);
    ErrorReporter errorReporter = new ErrorReporter();
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertEquals(1, errorReporter.getNumWarnings());
  }
}