package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeSerializer.EmptyTypeSerializer;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertNotNull(listType);
        assertEquals(ArrayList.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(ArrayList.class, listType.getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(ArrayList.class, String.class);
            fail("Expected DeprecationException");
        } catch (DeprecationException de) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType hashSetType = new CollectionType(HashSet.class, null, null, null, String.class);
        JavaType narrowedType = listType._narrow(HashSet.class);
        assertNotNull(narrowedType);
        assertEquals(HashSet.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType());
    }

    @Test(expected = DeprecationException.class)
    public void testNarrowDeprecated() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType narrowedType = listType._narrow(ArrayList.class);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType stringArrayType = listType.withContentType(String[].class);
        assertNotNull(stringArrayType);
        assertEquals(ArrayList.class, stringArrayType.getRawClass());
        assertEquals(String[].class, stringArrayType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType emptyListType = listType.withTypeHandler(EmptyTypeSerializer.INSTANCE);
        assertNotNull(emptyListType);
        assertEquals(ArrayList.class, emptyListType.getRawClass());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getContentTypeHandler());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getValueHandler());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType emptyListType = listType.withContentTypeHandler(EmptyTypeDeserializer.INSTANCE);
        assertNotNull(emptyListType);
        assertEquals(ArrayList.class, emptyListType.getRawClass());
        assertEquals(EmptyTypeDeserializer.class, emptyListType.getContentTypeHandler());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getValueHandler());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType emptyListType = listType.withValueHandler(EmptyTypeSerializer.INSTANCE);
        assertNotNull(emptyListType);
        assertEquals(ArrayList.class, emptyListType.getRawClass());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getValueHandler());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getTypeHandler());
        assertEquals(EmptyTypeDeserializer.class, emptyListType.getContentTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType emptyListType = listType.withContentValueHandler(EmptyTypeDeserializer.INSTANCE);
        assertNotNull(emptyListType);
        assertEquals(ArrayList.class, emptyListType.getRawClass());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getValueHandler());
        assertEquals(EmptyTypeSerializer.class, emptyListType.getTypeHandler());
        assertEquals(EmptyTypeDeserializer.class, emptyListType.getContentTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType staticListType = listType.withStaticTyping();
        assertNotNull(staticListType);
        assertEquals(ArrayList.class, staticListType.getRawClass());
        assertEquals(ArrayList.class, staticListType.getRawClass());
        assertTrue(staticListType._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType linkedListType = listType.refine(LinkedList.class, new TypeBindings(), null, null);
        assertNotNull(linkedListType);
        assertEquals(LinkedList.class, linkedListType.getRawClass());
        assertEquals(String.class, linkedListType.getContentType());
    }

    @Test
    public void testToString() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        String toString = listType.toString();
        assertNotNull(toString);
        assertTrue(toString.startsWith("[collection type; class "));
    }
}