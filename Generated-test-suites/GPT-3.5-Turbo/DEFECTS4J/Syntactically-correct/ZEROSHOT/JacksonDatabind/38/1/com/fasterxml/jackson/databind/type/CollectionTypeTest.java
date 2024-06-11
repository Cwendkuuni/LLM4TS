package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(null, null, null, null, null, null, null, false);
    }

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
    public void testConstructDeprecated() {
        Class<?> rawType = null;
        JavaType elemT = null;
        CollectionType result = CollectionType.construct(rawType, elemT);
        assertNotNull(result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        JavaType result = collectionType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        JavaType result = collectionType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType result = collectionType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType result = collectionType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType result = collectionType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType result = collectionType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType result = collectionType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = collectionType.toString();
        assertEquals("[collection type; class null, contains null]", result);
    }
}