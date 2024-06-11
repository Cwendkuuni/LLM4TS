package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = instance.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
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
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = instance.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        CollectionType instance = new CollectionType(null, null, null, null, null, null, null, false);
        String result = instance.toString();
        assertNotNull(result);
    }
}