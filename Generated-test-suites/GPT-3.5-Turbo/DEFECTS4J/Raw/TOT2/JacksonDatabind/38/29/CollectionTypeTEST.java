package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        CollectionType newCollectionType = collectionType.withContentType(Integer.class);
        assertNotEquals(collectionType, newCollectionType);
        assertEquals(Integer.class, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        CollectionType newCollectionType = collectionType.withTypeHandler(new CustomTypeHandler());
        assertNotEquals(collectionType, newCollectionType);
        assertEquals(new CustomTypeHandler(), newCollectionType.getTypeHandler());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType refinedType = collectionType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        assertEquals("[collection type; class java.util.List, contains class java.lang.String]", collectionType.toString());
    }
}