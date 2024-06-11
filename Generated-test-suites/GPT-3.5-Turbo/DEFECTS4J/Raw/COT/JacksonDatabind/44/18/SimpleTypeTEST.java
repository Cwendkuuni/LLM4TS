
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
      SimpleType type = SimpleType.constructUnsafe(String.class);
      assertEquals(SimpleType.class, type.getClass());
      assertEquals(String.class, type.getRawClass());
  }
  
  @Test
  public void testConstruct() {
      SimpleType type = SimpleType.construct(Object.class);
      assertEquals(SimpleType.class, type.getClass());
      assertEquals(Object.class, type.getRawClass());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithMapType() {
      SimpleType.construct(Map.class);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithCollectionType() {
      SimpleType.construct(Collection.class);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithArrayType() {
      SimpleType.construct(String[].class);
  }
  
  @Test
  public void testWithContentType() {
      SimpleType type = SimpleType.constructUnsafe(List.class);
      assertEquals(SimpleType.class, type.getClass());
      assertNull(type.getContentType());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeException() {
      SimpleType type = new SimpleType(List.class);
      type.withContentType(String.class);
  }
  
  @Test
  public void testWithSuperClass() {
      SimpleType type = SimpleType.constructUnsafe(Integer.class);
      assertEquals(SimpleType.class, type.getClass());
      assertEquals(Object.class, type.getSuperClass().getRawClass());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRefine() {
      SimpleType type = new SimpleType(Integer.class);
      type.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
  }
  
}
