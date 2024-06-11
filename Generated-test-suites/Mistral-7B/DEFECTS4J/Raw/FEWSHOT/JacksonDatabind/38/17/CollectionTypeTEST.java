package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType javaType = CollectionType.construct(String[].class, null, null, null, String.class);
        assertNotNull(javaType);
        assertEquals(String[].class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNull() {
        CollectionType.construct(null, null, null, null, null);
    }

    @Test
    public void testConstructWithBindings() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        TypeBindings bindings = new TypeBindings();
        TypeBindings newBindings = javaType.withBindings(bindings);
        assertEquals(newBindings, javaType.withBindings(bindings));
        assertNotNull(newBindings);
        assertEquals(ArrayList.class, newBindings.getRawType());
        assertEquals(String.class, newBindings.getContentType());
    }

    @Test
    public void testConstructWithSuperClass() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, List.class, null, String.class);
        assertEquals(List.class, javaType.getSuperClass());
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
        assertNull(javaType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithSuperInterfaces() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, new Class[] {Collection.class}, String.class);
        assertNull(javaType.getSuperClass());
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
        assertNotNull(javaType.getSuperInterfaces());
        assertEquals(Collection.class, javaType.getSuperInterfaces()[0]);
    }

    @Test
    public void testConstructWithAll() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, List.class, new Class[] {Collection.class}, String.class, null, null, false);
        assertEquals(List.class, javaType.getSuperClass());
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
        assertNotNull(javaType.getSuperInterfaces());
        assertEquals(Collection.class, javaType.getSuperInterfaces()[0]);
    }

    @Test
    public void testRefine() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType refinedType = javaType.refine(LinkedList.class, null, null, null);
        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
    }

    @Test
    public void testRefineWithBindings() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType refinedType = javaType.refine(LinkedList.class, null, null, null, new TypeBindings());
        assertNotNull(refinedType);
        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType withContentType = javaType.withContentType(Integer.class);
        assertEquals(Integer.class, withContentType.getContentType());
        assertEquals(ArrayList.class, withContentType.getRawClass());
        assertNull(withContentType.getSuperClass());
        assertNull(withContentType.getSuperInterfaces());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object();
        JavaType withTypeHandler = javaType.withTypeHandler(handler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
        assertEquals(ArrayList.class, withTypeHandler.getRawClass());
        assertNull(withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getSuperInterfaces());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object();
        JavaType withContentTypeHandler = javaType.withContentTypeHandler(handler);
        assertEquals(handler, withContentTypeHandler.getContentTypeHandler());
        assertEquals(ArrayList.class, withContentTypeHandler.getRawClass());
        assertNull(withContentTypeHandler.getSuperClass());
        assertNull(withContentTypeHandler.getSuperInterfaces());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object();
        JavaType withValueHandler = javaType.withValueHandler(handler);
        assertEquals(handler, withValueHandler.getValueHandler());
        assertEquals(ArrayList.class, withValueHandler.getRawClass());
        assertNull(withValueHandler.getSuperClass());
        assertNull(withValueHandler.getSuperInterfaces());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object();
        JavaType withContentValueHandler = javaType.withContentValueHandler(handler);
        assertEquals(handler, withContentValueHandler.getContentValueHandler());
        assertEquals(ArrayList.class, withContentValueHandler.getRawClass());
        assertNull(withContentValueHandler.getSuperClass());
        assertNull(withContentValueHandler.getSuperInterfaces());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType withStaticTyping = javaType.withStaticTyping();
        assertTrue(withStaticTyping._asStatic);
        assertEquals(ArrayList.class, withStaticTyping.getRawClass());
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getSuperInterfaces());
    }

    @Test
    public void testToString() {
        JavaType javaType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        String toString = javaType.toString();
        assertTrue(toString.startsWith("[collection type; class "));
        assertTrue(toString.endsWith("]"));
        assertTrue(toString.contains("ArrayList"));
        assertTrue(toString.contains("String"));
    }
}