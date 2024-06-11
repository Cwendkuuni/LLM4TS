package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertEquals("java.util.ArrayList<java.lang.String>", listType.toString());

        JavaType setType = CollectionType.construct(HashSet.class, null, null, null, Integer.class);
        assertEquals("java.util.HashSet<java.lang.Integer>", setType.toString());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class, null, null, false);
        JavaType listTypeWithBindings = CollectionType.construct(ArrayList.class, null, null, null, String.class, null, null, true);

        assertEquals("java.util.ArrayList<java.lang.String> (static)", listTypeWithBindings.toString());
        assertEquals("java.util.ArrayList<java.lang.String>", listType.toString());

        assertNotNull(listTypeWithBindings.getBindings());
        assertNull(listType.getBindings());
    }

    @Test
    public void testConstructWithWrongSuperClass() {
        try {
            JavaType listType = CollectionType.construct(String[].class, null, null, null, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType narrowedListType = listType.narrow(LinkedList.class);

        assertEquals("java.util.LinkedList<java.lang.String>", narrowedListType.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        listType.narrow(LinkedList.class);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType stringListType = listType.withContentType(String[].class);

        assertEquals("java.util.ArrayList<java.lang.String[]>", stringListType.toString());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType staticListType = listType.withStaticTyping();

        assertEquals("java.util.ArrayList<java.lang.String> (static)", staticListType.toString());
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType refinedListType = listType.refine(LinkedList.class, null, null, null);

        assertEquals("java.util.LinkedList<java.lang.String>", refinedListType.toString());
    }
}