import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {
  
  private SimpleType simpleType;
  
  @Before
  public void setUp() {
    simpleType = new SimpleType(String.class);
  }
  
  @After
  public void tearDown() {
    simpleType = null;
  }
  
  // Test cases for constructor
  @Test
  public void testConstructor() {
    assertNotNull(simpleType);
  }
  
  // Test cases for constructUnsafe()
  @Test
  public void testConstructUnsafe() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertEquals(String.class, type.getRawClass());
  }
  
  // Test cases for construct()
  @Test(expected = IllegalArgumentException.class)
  public void testConstructForMap() {
    SimpleType.construct(Map.class);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructForCollection() {
    SimpleType.construct(Collection.class);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructForArray() {
    SimpleType.construct(String[].class);
  }
  
  // Test cases for _narrow(Class<?> subclass)
  @Test
  public void testNarrowWhenClassEquals() {
    JavaType narrowedType = simpleType._narrow(String.class);
    assertEquals(simpleType, narrowedType);
  }
  
  @Test
  public void testNarrowWhenClassNotEquals() {
    JavaType narrowedType = simpleType._narrow(Integer.class);
    assertNotEquals(simpleType, narrowedType);
    assertEquals(Integer.class, narrowedType.getRawClass());
  }
  
  // Test cases for withTypeHandler(Object h)
  @Test
  public void testWithTypeHandler() {
    Object handler = new Object();
    SimpleType type = simpleType.withTypeHandler(handler);
    assertEquals(handler, type.getTypeHandler());
  }
  
  // Test cases for withValueHandler(Object h)
  @Test
  public void testWithValueHandler() {
    Object handler = new Object();
    SimpleType type = simpleType.withValueHandler(handler);
    assertEquals(handler, type.getValueHandler());
  }
  
  // Test cases for withStaticTyping()
  @Test
  public void testWithStaticTyping() {
    SimpleType type = simpleType.withStaticTyping();
    assertTrue(type.isStaticTyping());
  }
  
  // Test cases for refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
  @Test
  public void testRefine() {
    JavaType refinedType = simpleType.refine(String.class, null, null, null);
    assertNull(refinedType);
  }
  
  // Test case for isContainerType()
  @Test
  public void testIsContainerType() {
    assertFalse(simpleType.isContainerType());
  }
  
  // Test cases for getErasedSignature(StringBuilder sb)  
  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
    assertEquals(sb, erasedSignature);
  }
  
  // Test cases for getGenericSignature(StringBuilder sb)
  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    StringBuilder genericSignature = simpleType.getGenericSignature(sb);
    assertEquals(sb, genericSignature);
  }
  
  // Test cases for toString()
  @Test
  public void testToString() {
    String expected = "[simple type, class java.lang.String]";
    assertEquals(expected, simpleType.toString());
  }
  
  // Test cases for equals(Object o)
  @Test
  public void testEqualsWhenObjectsAreTheSame() {
    assertTrue(simpleType.equals(simpleType));
  }
  
  @Test
  public void testEqualsWhenObjectsAreNotTheSame() {
    SimpleType other = new SimpleType(String.class);
    assertTrue(simpleType.equals(other));
  }
  
  @Test
  public void testEqualsWhenObjectsAreDifferentClass() {
    Object object = new Object();
    assertFalse(simpleType.equals(object));
  }
  
  @Test
  public void testEqualsWhenClassNotEquals() {
    SimpleType other = new SimpleType(Integer.class);
    assertFalse(simpleType.equals(other));
  }
  
  @Test
  public void testEqualsWhenBindingsNotEquals() {
    TypeBindings bindings = new TypeBindings();
    SimpleType other = new SimpleType(String.class, bindings, null, null);
    assertFalse(simpleType.equals(other));
  }
  
  @Test
  public void testEqualsWhenClassAndBindingsEquals() {
    TypeBindings bindings = new TypeBindings();
    SimpleType other = new SimpleType(String.class, bindings, null, null);
    assertTrue(simpleType.equals(other));
  }
  
}