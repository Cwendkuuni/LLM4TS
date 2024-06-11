package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeSerializerBindings;
import com.fasterxml.jackson.databind.type.TypeDeserializer.EmptyConstructors;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        assertNotNull(listType);
        assertEquals(java.util.List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertEquals(String.class, listType.getRawType());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(java.util.List.class, String.class);
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // expected
        }
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType refinedListType = listType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedListType);
        assertEquals(ArrayList.class, refinedListType.getRawClass());
        assertEquals(String.class, refinedListType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType intListType = listType.withContentType(Integer.TYPE);
        assertNotNull(intListType);
        assertEquals(java.util.List.class, intListType.getRawClass());
        assertEquals(Integer.TYPE, intListType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType stringListType = listType.withTypeHandler(StringDeserializer.class);
        assertNotNull(stringListType);
        assertEquals(java.util.List.class, stringListType.getRawClass());
        assertNotNull(stringListType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType stringListType = listType.withContentTypeHandler(StringDeserializer.class);
        assertNotNull(stringListType);
        assertEquals(java.util.List.class, stringListType.getRawClass());
        assertNotNull(stringListType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType stringListType = listType.withValueHandler(StringSerializer.class);
        assertNotNull(stringListType);
        assertEquals(java.util.List.class, stringListType.getRawClass());
        assertNotNull(stringListType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType stringListType = listType.withContentValueHandler(StringSerializer.class);
        assertNotNull(stringListType);
        assertEquals(java.util.List.class, stringListType.getRawClass());
        assertNotNull(stringListType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        JavaType staticListType = listType.withStaticTyping();
        assertNotNull(staticListType);
        assertEquals(java.util.List.class, staticListType.getRawClass());
        assertTrue(staticListType.isStaticTyping());
    }

    @Test
    public void testToString() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        String toString = listType.toString();
        assertEquals("[collection type; class java.util.List, contains class java.lang.String]", toString);
    }

    @Test
    public void testNarrowDeprecated() {
        try {
            JavaType listType = CollectionType.construct(java.util.ArrayList.class, null, null, null, String.class);
            JavaType intListType = listType._narrow(java.util.List.class);
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // expected
        }
    }

    @Test
    public void testBindings() {
        JavaType listType = CollectionType.construct(java.util.List.class, null, null, null, String.class);
        TypeBindings bindings = listType.getBindings();
        assertNotNull(bindings);
        assertEquals(TypeSerializerBindings.Std.LIST_SER, bindings.getSerializer());
        assertEquals(TypeSerializerBindings.Std.LIST_DESER, bindings.getDeserializer());
        assertEquals(EmptyConstructors.class, bindings.getEmptyConstructor());
    }
}