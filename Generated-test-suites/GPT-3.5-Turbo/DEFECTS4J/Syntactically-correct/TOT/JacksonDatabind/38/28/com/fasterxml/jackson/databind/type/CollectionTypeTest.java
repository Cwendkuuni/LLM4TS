import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
  
  @Test
  public void testConstruct() {
    // typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNotNull(collectionType1);
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, null);
    assertNotNull(collectionType2);
    
    // error scenario
    CollectionType collectionType3 = CollectionType.construct(null, new TypeBindings());
    assertNull(collectionType3);
  }
  
  @Test
  public void testNarrow() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    JavaType narrowedType = collectionType._narrow(Vector.class);
    assertEquals(Vector.class, narrowedType.getRawClass());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNull(collectionType2._narrow(null));
  }
  
  @Test
  public void testWithTypeHandler() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType = collectionType.withTypeHandler(new TypeHandler());
    assertNotNull(newType.getTypeHandler());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNull(collectionType2.withTypeHandler(null).getTypeHandler());
  }
  
  @Test
  public void testWithContentType() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType = collectionType.withContentType(new JavaType());
    assertEquals(newType.getContentType(), new JavaType());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertEquals(collectionType2.withContentType(null).getContentType(), collectionType2.getContentType());
  }
  
  @Test
  public void testWithContentTypeHandler() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType = collectionType.withContentTypeHandler(new TypeHandler());
    assertNotNull(newType.getContentType().getTypeHandler());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNull(collectionType2.withContentTypeHandler(null).getContentType().getTypeHandler());
  }
  
  @Test
  public void testWithValueHandler() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType = collectionType.withValueHandler(new ValueHandler());
    assertNotNull(newType.getValueHandler());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNull(collectionType2.withValueHandler(null).getValueHandler());
  }
  
  @Test
  public void testWithContentValueHandler() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType = collectionType.withContentValueHandler(new ValueHandler());
    assertNotNull(newType.getContentType().getValueHandler());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNull(collectionType2.withContentValueHandler(null).getContentType().getValueHandler());
  }
  
  @Test
  public void testWithStaticTyping() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType = collectionType.withStaticTyping();
    assertTrue(newType.isStatic());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType newType2 = collectionType2.withStaticTyping();
    assertTrue(collectionType2.isStatic() == newType2.isStatic());
  }
  
  @Test
  public void testRefine() {
    // typical use case
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    CollectionType refinedType = collectionType.refine(LinkedList.class, new TypeBindings(), null, null);
    assertEquals(LinkedList.class, refinedType.getRawClass());
    
    // edge case
    CollectionType collectionType2 = CollectionType.construct(ArrayList.class, new TypeBindings());
    assertNull(collectionType2.refine(null, null, null, null));
  }
  
  @Test
  public void testToString() {
    CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings());
    String typeString = collectionType.toString();
    assertNotNull(typeString);
    assertTrue(typeString.contains("collection type"));
  }
}