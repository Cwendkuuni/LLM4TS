package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.List;

/**
 * Test class for NamedType.
 */
public class NamedTypeTest {

  private static final String REFERENCE = "TypeReference";
  private static final String SOURCE_NAME = "NamedTypeTest";
  private static final int LINE_NUMBER = 123;
  private static final int CHAR_NUMBER = 456;

  @org.junit.Before
  public void setUp() throws Exception {
    // Set up the JSTypeRegistry and ErrorReporter for testing.
  }

  @org.junit.After
  public void tearDown() throws Exception {
    // Clean up the JSTypeRegistry and ErrorReporter after testing.
  }

  /**
   * Test the constructor of NamedType.
   */
  @org.junit.Test
  public void testNamedTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(REFERENCE, namedType.getReferenceName());
    assertEquals(SOURCE_NAME, namedType.toStringHelper(true).substring(1, namedType.toStringHelper(true).length() - 1));
    assertEquals(LINE_NUMBER, namedType.getLineNumber());
    assertEquals(CHAR_NUMBER, namedType.getCharNumber());
  }

  /**
   * Test the defineProperty method when the type is not resolved.
   */
  @org.junit.Test
  public void testDefinePropertyUnresolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    List<NamedType.PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType.defineProperty("propertyName", JSType.getUndefinedType(), false, null);
    assertEquals(propertyContinuations.size(), namedType.getPropertyContinuations().size());
  }

  /**
   * Test the finishPropertyContinuations method when the type is resolved.
   */
  @org.junit.Test
  public void testFinishPropertyContinuationsResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType referencedObjType = new ObjectType("ReferencedType", registry);
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedAndResolvedType(referencedObjType);
    namedType.finishPropertyContinuations();
    ObjectType target = new ObjectType("TargetType", registry);
    target.defineProperty("propertyName", JSType.getUndefinedType(), false, null);
    assertEquals(referencedObjType, target.getPropertyType("propertyName").getReferencedType());
  }

  /**
   * Test the getReferencedType method when the type is unresolved.
   */
  @org.junit.Test
  public void testGetReferencedTypeUnresolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertNull(namedType.getReferencedType());
  }

  /**
   * Test the getReferencedType method when the type is resolved.
   */
  @org.junit.Test
  public void testGetReferencedTypeResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType referencedObjType = new ObjectType("ReferencedType", registry);
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedAndResolvedType(referencedObjType);
    assertEquals(referencedObjType, namedType.getReferencedType());
  }

  /**
   * Test the toStringHelper method when the type is not resolved.
   */
  @org.junit.Test
  public void testToStringHelperUnresolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(REFERENCE, namedType.toStringHelper(false));
  }

  /**
   * Test the toStringHelper method when the type is resolved.
   */
  @org.junit.Test
  public void testToStringHelperResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType referencedObjType = new ObjectType("ReferencedType", registry);
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedAndResolvedType(referencedObjType);
    assertEquals("ReferencedType", namedType.toStringHelper(false));
  }

  /**
   * Test the isNamedType method.
   */
  @org.junit.Test
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertTrue(namedType.isNamedType());
  }

  /**
   * Test the isNominalType method.
   */
  @org.junit.Test
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertTrue(namedType.isNominalType());
  }

  /**
   * Test the hashCode method.
   */
  @org.junit.Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType1 = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    NamedType namedType2 = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(namedType1.hashCode(), namedType2.hashCode());
  }

  /**
   * Test the resolveInternal method when the type is resolved via the registry.
   */
  @org.junit.Test
  public void testResolveInternalViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType referencedObjType = new ObjectType("ReferencedType", registry);
    JSType resolvedType = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedType(referencedObjType);
    namedType.setResolvedTypeInternal(resolvedType);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(null);
    JSType type = namedType.resolveInternal(errorReporter, enclosingScope);
    assertEquals(resolvedType, type);
  }

  /**
   * Test the resolveInternal method when the type is resolved via properties.
   */
  @org.junit.Test
  public void testResolveInternalViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType parentClass = new ObjectType("ParentClass", registry);
    ObjectType childClass = new ObjectType("ChildClass", registry);
    parentClass.defineProperty("propertyName", childClass, false, null);
    JSType propertyType = parentClass.getPropertyType("propertyName");

    NamedType namedType = new NamedType(registry, "ChildClass", SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedType(propertyType);
    namedType.setResolvedTypeInternal(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(null);
    JSType type = namedType.resolveInternal(errorReporter, enclosingScope);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), type);
  }

  /**
   * Test the resolveInternal method when the type is not resolved.
   */
  @org.junit.Test
  public void testResolveInternalUnresolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType parentClass = new ObjectType("ParentClass", registry);
    ObjectType childClass = new ObjectType("ChildClass", registry);
    parentClass.defineProperty("propertyName", new NamedType(registry, "UnresolvedType", null, 0, 0), false, null);

    NamedType namedType = new NamedType(registry, "UnresolvedType", SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedType(parentClass.getPropertyType("propertyName"));
    namedType.setResolvedTypeInternal(registry.getNativeObjectType(JSTypeNative.UNDEFINED_TYPE));

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(null);
    JSType type = namedType.resolveInternal(errorReporter, enclosingScope);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNDEFINED_TYPE), type);
  }

  /**
   * Test the setValidator method and validate the type.
   */
  @org.junit.Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof ObjectType;
      }
    };
    namedType.setValidator(validator);

    ObjectType referencedObjType = new ObjectType("ReferencedType", registry);
    namedType.setReferencedAndResolvedType(referencedObjType);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(null);
    namedType.resolveInternal(errorReporter, enclosingScope);

    assertTrue(namedType.isValidated());
  }
}