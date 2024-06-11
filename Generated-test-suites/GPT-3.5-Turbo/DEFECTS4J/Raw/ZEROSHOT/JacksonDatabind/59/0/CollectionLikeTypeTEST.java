package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class null, contains null]", collectionLikeType.toString());
    }

}