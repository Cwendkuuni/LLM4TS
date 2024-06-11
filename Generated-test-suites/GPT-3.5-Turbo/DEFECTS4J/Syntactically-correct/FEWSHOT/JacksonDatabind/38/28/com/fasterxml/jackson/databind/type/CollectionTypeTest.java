package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType result = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(result);
    }

    @Test
    public void test_withContentType() {
        JavaType contentType = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void test_withTypeHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withContentTypeHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withValueHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withContentValueHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withStaticTyping() {
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        String result = instance.toString();
        assertNotNull(result);
    }
}