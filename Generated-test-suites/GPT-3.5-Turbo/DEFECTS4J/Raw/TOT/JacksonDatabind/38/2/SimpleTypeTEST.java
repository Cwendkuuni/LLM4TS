package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  private SimpleType simpleType;

  /* Typical Use Cases */

  // Test constructor with valid class
  @Test
  public void testConstructorWithValidClass() {
    simpleType = new SimpleType(Object.class);
    assertNotNull(simpleType);
  }

  // Test constructor with null class (Edge case)
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithNullClass() {
    simpleType = new SimpleType(null);
  }

  // Test constructUnsafe method with valid class
  @Test
  public void testConstructUnsafeWithValidClass() {
    simpleType = SimpleType.constructUnsafe(Object.class);
    assertNotNull(simpleType);
  }

  // Test constructUnsafe method with null class (Edge case)
  @Test
  public void testConstructUnsafeWithNullClass() {
    simpleType = SimpleType.constructUnsafe(null);
    assertNotNull(simpleType);
  }

  // Test construct method with valid class
  @Test
  public void testConstructWithValidClass() {
    simpleType = SimpleType.construct(Object.class);
    assertNotNull(simpleType);
  }

  // Test construct method with null class (Edge case)
  @Test
  public void testConstructWithNullClass() {
    simpleType = SimpleType.construct(null);
    assertNotNull(simpleType);
  }

  /* Edge Cases */

  // Test constructor with null class and non-null bindings
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithNullClassAndNonNullBindings() {
    TypeBindings bindings = new TypeBindings();
    bindings.addBinding("key", Object.class);
    simpleType = new SimpleType(null, bindings, null, null);
  }

  // Test constructor with null bindings and non-null class
  @Test
  public void testConstructorWithNonNullBindingsAndNullClass() {
    TypeBindings bindings = new TypeBindings();
    bindings.addBinding("key", Object.class);
    simpleType = new SimpleType(Object.class, bindings, null, null);
    assertNotNull(simpleType);
  }

  // Test _narrow method with sub-class of the simple type's class
  @Test
  public void testNarrowWithSubClass() {
    SimpleType baseType = new SimpleType(Object.class);
    JavaType narrowedType = baseType._narrow(String.class);
    assertTrue(narrowedType instanceof SimpleType);
    assertEquals(String.class, narrowedType.getRawClass());
  }

  // Test _narrow method with same class as the simple type's class
  @Test
  public void testNarrowWithSameClass() {
    SimpleType baseType = new SimpleType(Object.class);
    JavaType narrowedType = baseType._narrow(Object.class);
    assertTrue(narrowedType instanceof SimpleType);
    assertEquals(Object.class, narrowedType.getRawClass());
  }

  // Test withContentType method (not applicable for SimpleType)
  @Test(expected=IllegalArgumentException.class)
  public void testWithContentType() {
    simpleType = new SimpleType(Object.class);
    simpleType.withContentType(Object.class);
  }

  // Test withTypeHandler method with same type handler
  @Test
  public void testWithTypeHandlerWithSameTypeHandler() {
    simpleType = new SimpleType(Object.class);
    Object typeHandler = new Object();
    SimpleType updatedType = simpleType.withTypeHandler(typeHandler);
    assertSame(simpleType, updatedType);
  }

  // Test withContentTypeHandler method (not applicable for SimpleType)
  @Test(expected=IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    simpleType = new SimpleType(Object.class);
    simpleType.withContentTypeHandler(new Object());
  }

  // Test withValueHandler method with same value handler
  @Test
  public void testWithValueHandlerWithSameValueHandler() {
    simpleType = new SimpleType(Object.class);
    Object valueHandler = new Object();
    SimpleType updatedType = simpleType.withValueHandler(valueHandler);
    assertSame(simpleType, updatedType);
  }

  // Test withContentValueHandler method (not applicable for SimpleType)
  @Test(expected=IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    simpleType = new SimpleType(Object.class);
    simpleType.withContentValueHandler(new Object());
  }

  // Test withStaticTyping method with static typing enabled
  @Test
  public void testWithStaticTypingWithStaticTypingEnabled() {
    simpleType = new SimpleType(Object.class);
    SimpleType updatedType = simpleType.withStaticTyping();
    assertSame(simpleType, updatedType);
  }

  // Test refine method with non-null rawType
  @Test
  public void testRefineWithNonNullRawType() {
    simpleType = new SimpleType(Object.class);
    JavaType refinedType = simpleType.refine(SimpleType.class, null, null, null);
    assertNull(refinedType);
  }

  // Test refine method with null rawType
  @Test
  public void testRefineWithNullRawType() {
    simpleType = new SimpleType(Object.class);
    JavaType refinedType = simpleType.refine(null, null, null, null);
    assertNull(refinedType);
  }

  /* Error Scenarios */

  // Test constructor with map class
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithMapClass() {
    simpleType = new SimpleType(Map.class);
  }

  // Test constructor with collection class
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithCollectionClass() {
    simpleType = new SimpleType(Collection.class);
  }

  // Test constructor with array class
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithArrayClass() {
    simpleType = new SimpleType(ArrayList[].class);
  }

  // Test constructor with null bindings
  @Test
  public void testConstructorWithNullBindings() {
    simpleType = new SimpleType(ArrayList.class, null, null, null);
    assertNotNull(simpleType);
  }

  // Test equals method with null object
  @Test
  public void testEqualsWithNullObject() {
    simpleType = new SimpleType(Object.class);
    assertFalse(simpleType.equals(null));
  }

  // Test equals method different class
  @Test
  public void testEqualsWithDifferentClass() {
    simpleType = new SimpleType(String.class);
    assertFalse(simpleType.equals(Object.class));
  }

  // Test equals method with same class and different bindings
  @Test
  public void testEqualsWithSameClassAndDifferentBindings() {
    SimpleType type1 = new SimpleType(ArrayList.class);
    TypeBindings bindings = new TypeBindings();
    bindings.addBinding("key", Object.class);
    SimpleType type2 = new SimpleType(ArrayList.class, bindings, null, null);
    assertFalse(type1.equals(type2));
  }

  // Test toString method
  @Test
  public void testToString() {
    simpleType = new SimpleType(ArrayList.class);
    assertEquals("[simple type, class java.util.ArrayList]", simpleType.toString());
  }

}