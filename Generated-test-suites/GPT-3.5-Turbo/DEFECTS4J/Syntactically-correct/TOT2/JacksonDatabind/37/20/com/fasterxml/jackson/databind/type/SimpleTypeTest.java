import org.junit.Test;
import static org.junit.Assert.*;

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
  public void test_narrow() {
    SimpleType simpleType = new SimpleType(String.class);
    JavaType narrowedType = simpleType._narrow(String.class);
    assertEquals(simpleType, narrowedType);
  }
  
  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(String.class);
    assertFalse(simpleType.isContainerType());
  }
  
  @Test
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(String.class);
    assertThrows(IllegalArgumentException.class, () -> {
      simpleType.withContentType(new JavaType());
    });
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
    JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), new JavaType(), new JavaType[] {});
    assertNull(refinedType);
  }
}