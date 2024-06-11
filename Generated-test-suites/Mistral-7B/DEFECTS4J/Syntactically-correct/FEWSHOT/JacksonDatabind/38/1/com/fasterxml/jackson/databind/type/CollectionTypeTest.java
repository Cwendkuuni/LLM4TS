package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertNull(listType.getRawType());
        assertNull(listType.getSuperClass());
        assertNull(listType.getSuperInterfaces());

        JavaType setType = CollectionType.construct(Set.class, null, null, null, Integer.class);
        assertEquals(Set.class, setType.getRawClass());
        assertEquals(Integer.class, setType.getContentType());
        assertNull(setType.getRawType());
        assertNull(setType.getSuperClass());
        assertNull(setType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType listType = CollectionType.construct(List.class, TypeBindings.defaultBindings(), null, null, String.class);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertNull(listType.getRawType());
        assertNull(listType.getSuperClass());
        assertNull(listType.getSuperInterfaces());

        JavaType setType = CollectionType.construct(Set.class, TypeBindings.defaultBindings(), null, null, Integer.class);
        assertEquals(Set.class, setType.getRawClass());
        assertEquals(Integer.class, setType.getContentType());
        assertNull(setType.getRawType());
        assertNull(setType.getSuperClass());
        assertNull(setType.getSuperInterfaces());
    }

    @Test
    public void testConstructEmpty() {
        JavaType emptyType = CollectionType.construct(Object.class, null, null, null, Object.class);
        assertEquals(Object.class, emptyType.getRawClass());
        assertEquals(Object.class, emptyType.getContentType());
        assertNull(emptyType.getRawType());
        assertNull(emptyType.getSuperClass());
        assertNull(emptyType.getSuperInterfaces());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType subListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType narrowedType = listType._narrow(ArrayList.class);
        assertEquals(subListType, narrowedType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType narrowedType = listType.withTypeHandler(null)._narrow(ArrayList.class);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withContentType = listType.withContentType(Integer.class);
        assertEquals(List.class, withContentType.getRawClass());
        assertEquals(Integer.class, withContentType.getContentType());
        assertNull(withContentType.getRawType());
        assertNull(withContentType.getSuperClass());
        assertNull(withContentType.getSuperInterfaces());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withTypeHandler = listType.withTypeHandler(new MyTypeHandler());
        assertEquals(List.class, withTypeHandler.getRawClass());
        assertEquals(String.class, withTypeHandler.getContentType());
        assertNull(withTypeHandler.getRawType());
        assertNull(withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getSuperInterfaces());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withContentTypeHandler = listType.withContentTypeHandler(new MyContentTypeHandler());
        assertEquals(List.class, withContentTypeHandler.getRawClass());
        assertEquals(String.class, withContentTypeHandler.getContentType().getRawClass());
        assertNull(withContentTypeHandler.getRawType());
        assertNull(withContentTypeHandler.getSuperClass());
        assertNull(withContentTypeHandler.getSuperInterfaces());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withValueHandler = listType.withValueHandler(new MyValueHandler());
        assertEquals(List.class, withValueHandler.getRawClass());
        assertEquals(String.class, withValueHandler.getContentType());
        assertNull(withValueHandler.getRawType());
        assertNull(withValueHandler.getSuperClass());
        assertNull(withValueHandler.getSuperInterfaces());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withContentValueHandler = listType.withContentValueHandler(new MyContentValueHandler());
        assertEquals(List.class, withContentValueHandler.getRawClass());
        assertEquals(String.class, withContentValueHandler.getContentType().getRawClass());
        assertNull(withContentValueHandler.getRawType());
        assertNull(withContentValueHandler.getSuperClass());
        assertNull(withContentValueHandler.getSuperInterfaces());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withStaticTyping = listType.withStaticTyping();
        assertEquals(List.class, withStaticTyping.getRawClass());
        assertEquals(String.class, withStaticTyping.getContentType());
        assertNull(withStaticTyping.getRawType());
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getSuperInterfaces());
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType refinedType = listType.refine(ArrayList.class, null, null, null);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType());
        assertNull(refinedType.getRawType());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
    }

    @Test
    public void testToString() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        String toString = listType.toString();
        assertTrue(toString.contains("collection type; class " + List.class.getName()));
        assertTrue(toString.contains("contains " + String.class.getName()));
    }
}