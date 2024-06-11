package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertNotNull(listType);
        assertEquals("java.util.List", listType.getClassName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidSuperInts() {
        CollectionType.construct(List.class, null, null, new JavaType[] {String.class}, String.class);
    }

    @Test
    public void testConstructDeprecated() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertEquals("java.util.List", listType.getClassName());

        JavaType listTypeDeprecated = CollectionType.construct(List.class, null, null, null, String.class);
        assertEquals("java.util.List", listTypeDeprecated.getClassName());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType subListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);

        CollectionType narrowedListType = (CollectionType) listType._narrow(ArrayList.class);
        CollectionType narrowedSubListType = (CollectionType) subListType._narrow(ArrayList.class);

        assertEquals(narrowedListType._class, subListType._class);
        assertEquals(narrowedSubListType._class, ArrayList.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType subListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);

        CollectionType narrowedListType = (CollectionType) listType._narrow(ArrayList.class);
        CollectionType narrowedSubListType = (CollectionType) subListType._narrow(ArrayList.class);

        narrowedListType._narrow(LinkedList.class);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithIntContentType = listType.withContentType(Integer.TYPE);

        assertEquals("java.util.List<java.lang.Integer>", listWithIntContentType.getClassName());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithStringHandler = listType.withTypeHandler(String.class);

        assertEquals("java.util.List<java.lang.String>", listWithStringHandler.getClassName());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithIntHandler = listType.withContentTypeHandler(Integer.class);

        assertEquals("java.util.List<java.lang.Integer>", listWithIntHandler.getClassName());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithStringHandler = listType.withValueHandler(String.class);

        assertEquals("java.util.List<java.lang.String>", listWithStringHandler.getClassName());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithIntHandler = listType.withContentValueHandler(Integer.class);

        assertEquals("java.util.List<java.lang.Integer>", listWithIntHandler.getClassName());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType staticListType = listType.withStaticTyping();

        assertEquals(true, staticListType._asStatic);
        assertEquals("java.util.List<java.lang.String>", staticListType.getClassName());
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType refinedListType = listType.refine(ArrayList.class, null, null, null);

        assertEquals(ArrayList.class, refinedListType._class);
        assertEquals("java.util.ArrayList<java.lang.String>", refinedListType.getClassName());
    }

    @Test
    public void testToString() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        String expected = "[collection type; class java.util.List, contains java.lang.String]";

        assertEquals(expected, listType.toString());
    }
}