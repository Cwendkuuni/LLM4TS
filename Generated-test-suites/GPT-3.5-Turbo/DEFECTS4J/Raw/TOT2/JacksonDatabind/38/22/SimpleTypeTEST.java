import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertNotNull(simpleType);
  }
  
  @Test
  public void testConstruct() {
    SimpleType simpleType = SimpleType.construct(Integer.class);
    assertNotNull(simpleType);
  }
  
  @Test
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(String.class);
    JavaType narrowedType = simpleType._narrow(CharSequence.class);
    assertTrue(narrowedType instanceof SimpleType);
  }
  
  @Test
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentType(String.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
  
  @Test
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentTypeHandler(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
  
  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType staticType = simpleType.withStaticTyping();
    assertTrue(staticType.isStatic());
  }
  
  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(String.class);
    JavaType refinedType = simpleType.refine(CharSequence.class, null, null, null);
    assertNull(refinedType);
  }
  
  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
    assertNotNull(sb);
  }
  
  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
    assertNotNull(sb);
  }
  
  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = new SimpleType(String.class);
    assertTrue(simpleType1.equals(simpleType2));
  }
}