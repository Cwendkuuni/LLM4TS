import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

  @Test
  public void testConstruct() {
    // Test case for the construct method
    CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
    assertNotNull(collectionType);
    assertEquals(ArrayList.class, collectionType.getRawClass());
    assertEquals(String.class, collectionType.getContentType());
  }

  @Test
  public void testWithContentType() {
    CollectionType collectionType = CollectionType.construct(LinkedList.class, null, null, null, Integer.class);
    CollectionType updatedCollectionType = collectionType.withContentType(String.class);
    assertEquals(LinkedList.class, updatedCollectionType.getRawClass());
    assertEquals(String.class, updatedCollectionType.getContentType());
  }

  // Additional test cases for other methods in the class

}