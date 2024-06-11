package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;

/**
 * JUnit test cases for NamedType class
 */
@RunWith(MockitoJUnitRunner.class)
public class NamedTypeTest {

  @Mock
  private ErrorReporter errorReporter;

  @Mock
  private StaticScope<JSType> staticScope;

  @Mock
  private JSTypeRegistry registry;

  @Mock
  private StaticSlot<JSType> staticSlot;

  @Mock
  private FunctionType functionType;

  @Mock
  private EnumType enumType;

  @Mock
  private EnumElementType enumElementType;

  @Mock
  private Node node;

  private NamedType namedType;

  @Before
  public void setup() {
    // Initialize mocks
  }

  @Test
  public void testNamedTypeConstructor() {
    String reference = "TestType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(reference, namedType.getReferenceName());
    assertEquals(sourceName, namedType.toStringHelper(false));
    assertEquals(true, namedType.hasReferenceName());
    assertEquals(true, namedType.isNamedType());
    assertEquals(true, namedType.isNominalType());
    assertEquals(1, namedType.hashCode());
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "testProperty";
    JSType type = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);
    boolean inferred = true;

    namedType.defineProperty(propertyName, type, inferred, node);

    assertNull(namedType.getPropertyType(propertyName));
  }

  @Test
  public void testFinishPropertyContinuations() {
    String propertyName = "testProperty";
    JSType type = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);

    namedType.finishPropertyContinuations();

    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertEquals(type, referencedObjType.getPropertyType(propertyName));
  }

  @Test
  public void testGetReferencedType() {
    JSType referencedType = registry.getType("TestType");

    namedType = new NamedType(registry, "TestType", "", 0, 0);

    assertNull(namedType.getReferencedType());

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, staticScope);

    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistry() {
    JSType referencedType = registry.getType("TestType");

    namedType = new NamedType(registry, "TestType", "", 0, 0);

    assertFalse(namedType.resolveViaRegistry(errorReporter, staticScope));

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, staticScope);

    assertTrue(namedType.resolveViaRegistry(errorReporter, staticScope));
  }

  @Test
  public void testResolveViaProperties() {
    JSType referencedType = registry.getType("TestType");
    String[] componentNames = {"TestComponent1", "TestComponent2"};
    StaticSlot<JSType> slot = staticSlot;

    // Setup mocks

    when(registry.getType(anyString())).thenReturn(referencedType);
    when(staticScope.getSlot(anyString())).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    namedType = new NamedType(registry, "TestType.TestComponent1.TestComponent2", "", 0, 0);

    assertFalse(namedType.resolveViaRegistry(errorReporter, staticScope));
    assertFalse(namedType.resolveViaProperties(errorReporter, staticScope));

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, staticScope);

    assertTrue(namedType.resolveViaProperties(errorReporter, staticScope));
  }

  @Test
  public void testSetReferencedAndResolvedType() {
    JSType referencedType = registry.getType("TestType");
    JSType resolvedType = registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);

    namedType = new NamedType(registry, "TestType", "", 0, 0);

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, staticScope);

    assertEquals(referencedType, namedType.getReferencedType());
    assertEquals(referencedType, namedType.getResolvedType());

    namedType.setReferencedAndResolvedType(resolvedType, errorReporter, staticScope);

    assertEquals(resolvedType, namedType.getReferencedType());
    assertEquals(resolvedType, namedType.getResolvedType());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetResolvedTypeInternalWhenUnresolved() {
    namedType = new NamedType(registry, "TestType", "", 0, 0);

    namedType.setResolvedTypeInternal(new ObjectType(registry));
  }

  @Test
  public void testIsResolved() {
    JSType referencedType = registry.getType("TestType");

    namedType = new NamedType(registry, "TestType", "", 0, 0);

    assertFalse(namedType.isResolved());

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, staticScope);

    assertTrue(namedType.isResolved());
  }

  @Test
  public void testIsResolvedWhenUnresolved() {
    namedType = new NamedType(registry, "TestType", "", 0, 0);

    assertFalse(namedType.isResolved());
  }

  @Test
  public void testHandleTypeCycle() {
    namedType = new NamedType(registry, "TestType", "", 0, 0);

    namedType.setReferencedAndResolvedType(namedType, errorReporter, staticScope);

    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof ObjectType;
      }
    };

    namedType = new NamedType(registry, "TestType", "", 0, 0);

    namedType.setValidator(validator);

    assertNull(namedType.getReferencedType());

    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    namedType.setReferencedAndResolvedType(objectType, errorReporter, staticScope);

    assertTrue(validator.apply(namedType.getReferencedType()));
  }

  @Test
  public void testPropertyContinuation() {
    String propertyName = "testProperty";
    JSType type = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);
    boolean inferred = true;

    namedType = new NamedType(registry, "TestType", "", 0, 0);

    PropertyContinuation propertyContinuation = new PropertyContinuation(
        propertyName, type, inferred, node);

    namedType.defineProperty(propertyName, type, inferred, node);

    assertEquals(type, namedType.getPropertyType(propertyName));
  }
}