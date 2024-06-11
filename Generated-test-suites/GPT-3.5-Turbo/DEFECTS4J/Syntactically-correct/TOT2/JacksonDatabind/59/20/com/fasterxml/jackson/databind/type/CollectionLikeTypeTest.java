import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        // Test constructing a CollectionLikeType instance
        Class<?> rawType = Collection.class;
        JavaType elementType = JavaType.construct(String.class);
        CollectionLikeType collectionType = CollectionLikeType.construct(rawType, null, null, null, elementType);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        // Test checking if a CollectionLikeType is a true collection type
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, JavaType.construct(String.class), null, null, false);
        
        assertTrue(collectionType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        // Test equals method of CollectionLikeType
        CollectionLikeType collectionType1 = new CollectionLikeType(Collection.class, null, null, null, JavaType.construct(String.class), null, null, false);
        CollectionLikeType collectionType2 = new CollectionLikeType(Collection.class, null, null, null, JavaType.construct(Integer.class), null, null, false);

        assertTrue(collectionType1.equals(collectionType1));
        assertFalse(collectionType1.equals(collectionType2));
    }

    @Test
    public void testToString() {
        // Test toString method of CollectionLikeType
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, JavaType.construct(String.class), null, null, false);
        
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.String]", collectionType.toString());
    }
}