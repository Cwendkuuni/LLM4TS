import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(Class.class);
    assertNotNull(simpleType);
  }
  
  @Test
  public void testConstruct() {
    SimpleType simpleType = SimpleType.construct(SimpleType.class);
    assertNotNull(simpleType);
  }
  
  @Test
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(Object.class);
    SimpleType narrowedType = simpleType._narrow(SimpleType.class);
    assertEquals(SimpleType.class, narrowedType.getClass());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
  }
  
  @Test
  public void testWithTypeHandler() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    Object typeHandler = new Object();
    SimpleType typeWithHandler = simpleType.withTypeHandler(typeHandler);
    assertEquals(typeHandler, typeWithHandler.getTypeHandler());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    simpleType.withContentTypeHandler(new Object());
  }
  
  @Test
  public void testWithValueHandler() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    Object valueHandler = new Object();
    SimpleType typeWithValueHandler = simpleType.withValueHandler(valueHandler);
    assertEquals(valueHandler, typeWithValueHandler.getValueHandler());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    simpleType.withContentValueHandler(new Object());
  }
  
  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    SimpleType staticType = simpleType.withStaticTyping();
    assertTrue(staticType.isStatic());
  }
  
  @Test
  public void testRefine() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
    assertNull(refinedType);
  }
  
  @Test
  public void testIsContainerType() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertFalse(simpleType.isContainerType());
  }
  
  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(null).toString());
  }
  
  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertEquals("Ljava/lang/String;", simpleType.getGenericSignature(null).toString());
  }
  
  @Test
  public void testToString() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertEquals("[simple type, class java.lang.String]", simpleType.toString());
  }
  
  @Test
  public void testEquals() {
    SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
    SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
    assertTrue(simpleType1.equals(simpleType2));
  }

}