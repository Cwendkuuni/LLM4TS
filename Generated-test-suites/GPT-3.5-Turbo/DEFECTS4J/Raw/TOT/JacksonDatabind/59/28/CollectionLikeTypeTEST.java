import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {

    // Test for isTrueCollectionType() method
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, null, null, null, false);
        assertTrue(collectionType.isTrueCollectionType());

        CollectionLikeType nonCollectionType = new CollectionLikeType(Object.class, null,
                null, null, null, null, null, false);
        assertFalse(nonCollectionType.isTrueCollectionType());
    }

    // Test for isContainerType() method
    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, null, null, null, false);
        assertTrue(collectionType.isContainerType());

        CollectionLikeType nonCollectionType = new CollectionLikeType(Object.class, null,
                null, null, null, null, null, false);
        assertTrue(nonCollectionType.isContainerType());
    }

    // Test for isCollectionLikeType() method
    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, null, null, null, false);
        assertTrue(collectionType.isCollectionLikeType());

        CollectionLikeType nonCollectionType = new CollectionLikeType(Object.class, null,
                null, null, null, null, null, false);
        assertTrue(nonCollectionType.isCollectionLikeType());
    }

    // Test for getContentType() method
    @Test
    public void testGetContentType() {
        JavaType elementType = new JavaType();
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, elementType, null, null, false);
        assertEquals(elementType, collectionType.getContentType());
    }

    // Test for getContentValueHandler() method
    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType elementType = new JavaType();
        elementType.setValueHandler(valueHandler);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, elementType, null, null, false);
        assertEquals(valueHandler, collectionType.getContentValueHandler());
    }

    // Test for getContentTypeHandler() method
    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType elementType = new JavaType();
        elementType.setTypeHandler(typeHandler);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, elementType, null, null, false);
        assertEquals(typeHandler, collectionType.getContentTypeHandler());
    }

    // Test for hasHandlers() method
    @Test
    public void testHasHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType elementType = new JavaType();
        elementType.setValueHandler(valueHandler);
        elementType.setTypeHandler(typeHandler);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, elementType, null, null, false);
        assertTrue(collectionType.hasHandlers());
    }

    // Test for getErasedSignature() method
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/util/Collection<*>;");
        assertEquals(expected.toString(), collectionType.getErasedSignature(sb).toString());
    }

    // Test for getGenericSignature() method
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/util/Collection<*>;");
        assertEquals(expected.toString(), collectionType.getGenericSignature(sb).toString());
    }

    // Test for buildCanonicalName() method
    @Test
    public void testBuildCanonicalName() {
        JavaType elementType = new JavaType();
        elementType.setCanonical("java.lang.String");
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, elementType, null, null, false);
        assertEquals("java.util.Collection<java.lang.String>", collectionType.buildCanonicalName());
    }

    // Test for equals() method
    @Test
    public void testEquals() {
        JavaType elementType1 = new JavaType();
        JavaType elementType2 = new JavaType();
        CollectionLikeType collectionType1 = new CollectionLikeType(Collection.class, null,
                null, null, elementType1, null, null, false);
        CollectionLikeType collectionType2 = new CollectionLikeType(Collection.class, null,
                null, null, elementType2, null, null, false);
        assertTrue(collectionType1.equals(collectionType1));
        assertTrue(collectionType1.equals(collectionType2));
    }

    // Test for toString() method
    @Test
    public void testToString() {
        JavaType elementType = new JavaType();
        elementType.setCanonical("java.lang.String");
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null,
                null, null, elementType, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.String]",
                collectionType.toString());
    }

}