
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
  
  @Test
  public void testConstruct() {
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    assertEquals(ArrayList.class, collectionType.getRawClass());
    assertEquals(String.class, collectionType.getContentType());
    assertNull(collectionType.getValueHandler());
    assertNull(collectionType.getTypeHandler());
    assertFalse(collectionType.isStaticTyping());
  }
  
  @Test
  public void testWithContentType() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.withContentType(String.class);
    assertEquals(ArrayList.class, newType.getRawClass());
    assertEquals(String.class, newType.getContentType());
    assertNull(newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertFalse(newType.isStaticTyping());
  }
  
  @Test
  public void testWithTypeHandler() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.withTypeHandler(new CustomTypeHandler());
    assertEquals(ArrayList.class, newType.getRawClass());
    assertEquals(Integer.class, newType.getContentType());
    assertNull(newType.getValueHandler());
    assertNotNull(newType.getTypeHandler());
    assertFalse(newType.isStaticTyping());
  }
  
  @Test
  public void testWithContentTypeHandler() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.withContentTypeHandler(new CustomTypeHandler());
    assertEquals(ArrayList.class, newType.getRawClass());
    assertEquals(Integer.class, newType.getContentType());
    assertNull(newType.getValueHandler());
    assertNotNull(newType.getTypeHandler());
    assertFalse(newType.isStaticTyping());
  }
  
  @Test
  public void testWithValueHandler() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.withValueHandler(new CustomValueHandler());
    assertEquals(ArrayList.class, newType.getRawClass());
    assertEquals(Integer.class, newType.getContentType());
    assertNotNull(newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertFalse(newType.isStaticTyping());
  }
  
  @Test
  public void testWithContentValueHandler() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.withContentValueHandler(new CustomValueHandler());
    assertEquals(ArrayList.class, newType.getRawClass());
    assertEquals(Integer.class, newType.getContentType());
    assertNotNull(newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertFalse(newType.isStaticTyping());
  }
  
  @Test
  public void testWithStaticTyping() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.withStaticTyping();
    assertEquals(ArrayList.class, newType.getRawClass());
    assertEquals(Integer.class, newType.getContentType());
    assertNull(newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertTrue(newType.isStaticTyping());
  }
  
  @Test
  public void testRefine() {
    CollectionType originalType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, Integer.class);
    CollectionType newType = originalType.refine(LinkedList.class, new TypeBindings(), null, null);
    assertEquals(LinkedList.class, newType.getRawClass());
    assertEquals(Integer.class, newType.getContentType());
    assertNull(newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertFalse(newType.isStaticTyping());
  }
  
  @Test
  public void testToString() {
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.String]", collectionType.toString());
  }
  
}
