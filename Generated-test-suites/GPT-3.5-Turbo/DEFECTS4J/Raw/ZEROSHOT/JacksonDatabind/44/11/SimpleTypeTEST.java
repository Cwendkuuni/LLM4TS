import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertNotNull(simpleType);
    assertEquals(String.class, simpleType.getRawClass());
  }
  
  @Test
  public void testConstruct() {
    SimpleType simpleType = SimpleType.construct(Integer.class);
    assertNotNull(simpleType);
    assertEquals(Integer.class, simpleType.getRawClass());
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
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = simpleType1.narrow(CharSequence.class);
    assertNotNull(simpleType2);
    assertEquals(CharSequence.class, simpleType2.getRawClass());
  }
  
  @Test
  public void testWithContentType() {
    SimpleType simpleType1 = new SimpleType(List.class);
    JavaType contentType = SimpleType.constructUnsafe(String.class);
    SimpleType simpleType2 = simpleType1.withContentType(contentType);
    assertNotNull(simpleType2);
    assertEquals(contentType, simpleType2.getContentType());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeException() {
    SimpleType simpleType = new SimpleType(String.class);
    simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
  }
  
  @Test
  public void testWithTypeHandler() {
    SimpleType simpleType1 = new SimpleType(String.class);
    Object typeHandler = new Object();
    SimpleType simpleType2 = simpleType1.withTypeHandler(typeHandler);
    assertNotNull(simpleType2);
    assertEquals(typeHandler, simpleType2.getTypeHandler());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    simpleType.withContentTypeHandler(new Object());
  }
  
  @Test
  public void testWithValueHandler() {
    SimpleType simpleType1 = new SimpleType(String.class);
    Object valueHandler = new Object();
    SimpleType simpleType2 = simpleType1.withValueHandler(valueHandler);
    assertNotNull(simpleType2);
    assertEquals(valueHandler, simpleType2.getValueHandler());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    simpleType.withContentValueHandler(new Object());
  }
  
  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = simpleType1.withStaticTyping();
    assertNotNull(simpleType2);
    assertTrue(simpleType2.isStatic());
  }
  
  @Test
  public void testRefine() {
    SimpleType simpleType1 = new SimpleType(String.class);
    JavaType refinedType = simpleType1.refine(String.class, TypeBindings.emptyBindings(), null, null);
    assertNull(refinedType);
  }
  
  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(String.class);
    assertFalse(simpleType.isContainerType());
  }
  
  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder signature = new StringBuilder();
    simpleType.getErasedSignature(signature);
    assertEquals("Ljava/lang/String;", signature.toString());
  }
  
  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder signature = new StringBuilder();
    simpleType.getGenericSignature(signature);
    assertEquals("Ljava/lang/String;", signature.toString());
  }
  
  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = new SimpleType(String.class);
    assertTrue(simpleType1.equals(simpleType2));
  }
  
  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals("[simple type, class java.lang.String]", simpleType.toString());
  }
}