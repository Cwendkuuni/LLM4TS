package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollectionLikeTypeTest {

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        CollectionLikeType collectionLikeType2 = new CollectionLikeType(Object.class, null, null, null, null, null, null, false);
        assertFalse(collectionLikeType2.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elementType = new JavaType(Object.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals(collectionLikeType, collectionLikeType2);
    }

    @Test
    public void testToString() {
        JavaType elementType = new JavaType(Object.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains " + elementType + "]", collectionLikeType.toString());
    }
}