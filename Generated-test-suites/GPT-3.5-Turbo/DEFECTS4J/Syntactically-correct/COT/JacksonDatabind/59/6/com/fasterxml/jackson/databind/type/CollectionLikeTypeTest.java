
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    /**
     * Test that checks the functionality of the isTrueCollectionType method
     */
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    /**
     * Test that checks the functionality of the getContentType method
     */
    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertEquals(Object.class, collectionLikeType.getContentType());
    }

    /**
     * Test that checks the functionality of the getContentValueHandler method
     */
    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertNull(collectionLikeType.getContentValueHandler());
    }

    /**
     * Test that checks the functionality of the getContentTypeHandler method
     */
    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    /**
     * Test that checks the functionality of the hasHandlers method
     */
    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertFalse(collectionLikeType.hasHandlers());
    }

    /**
     * Test that checks the functionality of the getErasedSignature method
     */
    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertEquals("Ljava/util/CollectionLikeType;", collectionLikeType.getErasedSignature());
    }

    /**
     * Test that checks the functionality of the getGenericSignature method
     */
    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertEquals("Ljava/util/CollectionLikeType<*>;", collectionLikeType.getGenericSignature());
    }

    /**
     * Test that checks the functionality of the toString method
     */
    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.Object]", collectionLikeType.toString());
    }

    /**
     * Test that checks the functionality of the equals method
     */
    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }
}
