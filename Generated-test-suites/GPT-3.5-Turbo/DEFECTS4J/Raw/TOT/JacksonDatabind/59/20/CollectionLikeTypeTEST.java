
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elementType = new SimpleType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elementType);

        assertNotNull(collectionLikeType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(List.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertNotNull(collectionLikeType);
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test
    public void testNarrow() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null,
                null, new SimpleType(String.class), null, null, false);

        JavaType narrowedType = collectionLikeType._narrow(ArrayList.class);

        assertNotNull(narrowedType);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType elementType1 = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null,
                null, elementType1, null, null, false);

        JavaType elementType2 = new SimpleType(Integer.class);
        JavaType withContentType = collectionLikeType.withContentType(elementType2);

        assertNotNull(withContentType);
        assertEquals(Collection.class, withContentType.getRawClass());
        assertEquals(elementType2, withContentType.getContentType());
    }

    @Test
    public void testWithElementType() {
        JavaType baseType = new SimpleType(List.class);
        JavaType elementType1 = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType1);

        JavaType elementType2 = new SimpleType(Integer.class);
        CollectionLikeType withElementType = collectionLikeType.withContentType(elementType2);

        assertNotNull(withElementType);
        assertEquals(List.class, withElementType.getRawClass());
        assertEquals(elementType2, withElementType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null,
                null, new SimpleType(String.class), null, null, false);

        CollectionLikeType withStaticTyping = collectionLikeType.withStaticTyping();

        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null,
                null, new SimpleType(String.class), null, null, false);

        JavaType refinedType = collectionLikeType.refine(
                ArrayList.class, null, null, null);

        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType().getRawClass());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null,
                null, new SimpleType(String.class), null, null, false);

        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elementType1 = new SimpleType(String.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class, null, null,
                null, elementType1, null, null, false);

        JavaType elementType2 = new SimpleType(Integer.class);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(List.class, null, null,
                null, elementType2, null, null, false);

        assertEquals(collectionLikeType1, collectionLikeType1);
        assertNotEquals(collectionLikeType1, collectionLikeType2);
    }

    @Test
    public void testToString() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null,
                null, elementType, null, null, false);

        String expected = "[collection-like type; class java.util.Collection, contains String]";
        assertEquals(expected, collectionLikeType.toString());
    }
}
