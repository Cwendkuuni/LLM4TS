import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {
  
  private CollectionLikeType collectionLikeType;
  
  @Before
  public void setUp() {
    collectionLikeType = new CollectionLikeType(ArrayList.class, new TypeBindings(), null, null, JavaType.forType(String.class), null, null, false);
  }
  
  @Test
  public void testIsTrueCollectionType() {
    assertTrue(collectionLikeType.isTrueCollectionType());
  }
  
  @Test
  public void testIsContainerType() {
    assertTrue(collectionLikeType.isContainerType());
  }
  
  @Test
  public void testIsCollectionLikeType() {
    assertTrue(collectionLikeType.isCollectionLikeType());
  }
  
  @Test
  public void testGetContentType() {
    JavaType contentType = collectionLikeType.getContentType();
    assertEquals(JavaType.forType(String.class), contentType);
  }
  
  @Test
  public void testGetContentValueHandler() {
    Object contentValueHandler = collectionLikeType.getContentValueHandler();
    assertNull(contentValueHandler);
  }
  
  @Test
  public void testGetContentTypeHandler() {
    Object contentTypeHandler = collectionLikeType.getContentTypeHandler();
    assertNull(contentTypeHandler);
  }
  
  @Test
  public void testHasHandlers() {
    boolean hasHandlers = collectionLikeType.hasHandlers();
    assertFalse(hasHandlers);
  }
  
  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    collectionLikeType.getErasedSignature(sb);
    assertEquals("Ljava/util/ArrayList;", sb.toString());
  }
  
  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    collectionLikeType.getGenericSignature(sb);
    assertEquals("Ljava/util/ArrayList<Ljava/lang/String;>;", sb.toString());
  }
  
  @Test
  public void testEquals() {
    CollectionLikeType other = new CollectionLikeType(ArrayList.class, new TypeBindings(), null, null, JavaType.forType(String.class), null, null, false);
    assertTrue(collectionLikeType.equals(other));
  }
  
  @Test
  public void testToString() {
    String stringRep = collectionLikeType.toString();
    assertEquals("[collection-like type; class java.util.ArrayList, contains JavaType(String)]", stringRep);
  }

}