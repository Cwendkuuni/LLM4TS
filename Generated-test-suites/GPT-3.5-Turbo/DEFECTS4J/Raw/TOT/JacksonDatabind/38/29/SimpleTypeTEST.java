import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

  @Test
  public void testConstructor1() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals(String.class, simpleType.getRawClass());
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testConstructor2() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    assertEquals(String.class, simpleType.getRawClass());
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testConstructor3() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
    assertEquals(String.class, simpleType.getRawClass());
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testConstructor4() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
    assertEquals(String.class, simpleType.getRawClass());
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertEquals(String.class, simpleType.getRawClass());
    assertFalse(simpleType.isContainerType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithMap() {
    SimpleType.construct(Map.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithCollection() {
    SimpleType.construct(Collection.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithArray() {
    SimpleType.construct(String[].class);
  }

  @Test
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    JavaType narrowedType = simpleType.narrow(Object.class);
    assertEquals(Object.class, narrowedType.getRawClass());
    assertFalse(narrowedType.isContainerType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNarrowWithInvalidClass() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    simpleType.narrow(Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    simpleType.withContentType(String.class);
  }

  @Test
  public void testWithTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    Object typeHandler = new Object();
    SimpleType modifiedType = simpleType.withTypeHandler(typeHandler);
    assertEquals(typeHandler, modifiedType.getTypeHandler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    simpleType.withContentTypeHandler(new Object());
  }

  @Test
  public void testWithValueHandler() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    Object valueHandler = new Object();
    SimpleType modifiedType = simpleType.withValueHandler(valueHandler);
    assertEquals(valueHandler, modifiedType.getValueHandler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    simpleType.withContentValueHandler(new Object());
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    SimpleType modifiedType = simpleType.withStaticTyping();
    assertTrue(modifiedType.isStatic());
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    StringBuilder sb = new StringBuilder();
    StringBuilder expectedSignature = simpleType.getErasedSignature(sb);
    assertEquals(expectedSignature, sb);
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    StringBuilder sb = new StringBuilder();
    StringBuilder expectedSignature = simpleType.getGenericSignature(sb);
    assertEquals(expectedSignature, sb);
  }

  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    String expectedString = "[simple type, class java.lang.String]";
    assertEquals(expectedString, simpleType.toString());
  }

  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
    assertTrue(simpleType1.equals(simpleType2));
  }
}