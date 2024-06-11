package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, new JavaType());
        assertEquals("List", collectionType.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, elemT, null, null, false);
        JavaType contentType = new JavaType();
        CollectionType updatedType = collectionType.withContentType(contentType);
        assertEquals(contentType, updatedType.getElementType());
    }

    // Add more test cases for other methods

}