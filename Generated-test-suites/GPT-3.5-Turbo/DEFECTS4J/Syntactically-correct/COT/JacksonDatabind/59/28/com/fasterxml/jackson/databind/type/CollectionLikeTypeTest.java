
package com.fasterxml.jackson.databind.type;
import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionLikeTypeTest {
  
  @Test
  public void testIsTrueCollectionType() {
    CollectionLikeType collectionLikeType = new CollectionLikeType(Object.class);
    assertTrue(collectionLikeType.isTrueCollectionType());
  }
  
  @Test
  public void testEquals() {
    CollectionLikeType collectionLikeType1 = new CollectionLikeType(Object.class);
    CollectionLikeType collectionLikeType2 = new CollectionLikeType(Object.class);
    assertTrue(collectionLikeType1.equals(collectionLikeType2));
  }
  
  @Test
  public void testToString() {
    CollectionLikeType collectionLikeType = new CollectionLikeType(Object.class);
    assertEquals("[collection-like type; class java.lang.Object, contains java.lang.Object]", collectionLikeType.toString());
  }
}
