package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType type = CollectionType.construct(java.util.List.class, null, null, null, null);
        assertNotNull(type);
        assertEquals("java.util.List", type.toString());
        assertEquals(null, type.getBindings());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType type = CollectionType.construct(java.util.Stack.class, null, null, null, null);
        assertNotNull(type);
        assertEquals("java.util.Stack", type.toString());

        Object handler = new Object();
        CollectionType updatedType = type.withContentTypeHandler(handler);
        assertNotNull(updatedType);
        assertEquals("java.util.Stack", updatedType.toString());
        assertEquals(handler, updatedType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType type = CollectionType.construct(java.util.Set.class, null, null, null, null);
        assertNotNull(type);
        assertEquals("java.util.Set", type.toString());

        CollectionType updatedType = type.withStaticTyping();
        assertNotNull(updatedType);
        assertEquals("java.util.Set", updatedType.toString());
        assertTrue(updatedType.isStaticTyped());
    }

    @Test
    public void testRefine() {
        CollectionType type = CollectionType.construct(java.util.Map.class, null, null, null, null);
        assertNotNull(type);
        assertEquals("java.util.Map", type.toString());

        CollectionType refinedType = (CollectionType) type.refine(java.util.HashMap.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals("java.util.HashMap", refinedType.toString());
    }

    @Test
    public void testToString() {
        CollectionType type = CollectionType.construct(java.util.ArrayList.class, null, null, null, null);
        assertNotNull(type);
        assertEquals("[collection type; class java.util.ArrayList, contains null]", type.toString());
    }
}