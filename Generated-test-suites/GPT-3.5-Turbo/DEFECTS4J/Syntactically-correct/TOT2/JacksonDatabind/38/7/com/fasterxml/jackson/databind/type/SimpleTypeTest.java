import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertNotNull(simpleType);
  }
  
  @Test
  public void testConstructIllegalArgumentException() {
    try {
      SimpleType.construct(Map.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testConstructArrayIllegalArgumentException() {
    try {
      SimpleType.construct(String[].class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testNarrowSameClass() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals(simpleType, simpleType._narrow(String.class));
  }
  
  @Test
  public void testContentTypeException() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentType(String.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testContentValueHandlerException() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentValueHandler("handler");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = new SimpleType(String.class);
    assertEquals(simpleType1, simpleType2);
  }

}