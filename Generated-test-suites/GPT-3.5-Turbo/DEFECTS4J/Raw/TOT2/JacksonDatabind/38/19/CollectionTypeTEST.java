package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        CollectionType collectionType = CollectionType.construct(LinkedList.class, null, null, null, Integer.class);
        CollectionType newType = collectionType.withTypeHandler(handler);

        assertNotNull(newType.getTypeHandler());
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = CollectionType.construct(HashSet.class, null, null, null, Double.class);
        CollectionType newType = collectionType.withStaticTyping();

        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(HashMap.class, null, null, null, Long.class);
        String expected = "[collection type; class java.util.HashMap, contains class java.lang.Long]";
        assertEquals(expected, collectionType.toString());
    }
}