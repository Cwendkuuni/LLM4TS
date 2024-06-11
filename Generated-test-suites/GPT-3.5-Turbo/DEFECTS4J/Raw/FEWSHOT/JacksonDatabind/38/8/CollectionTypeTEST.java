
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(rawType, collectionType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        CollectionType collectionTypeWithContentType = collectionType.withContentType(contentType);
        assertEquals(contentType, collectionTypeWithContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = null;
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        CollectionType collectionTypeWithTypeHandler = collectionType.withTypeHandler(handler);
        assertEquals(handler, collectionTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = null;
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        CollectionType collectionTypeWithContentTypeHandler = collectionType.withContentTypeHandler(handler);
        assertEquals(handler, collectionTypeWithContentTypeHandler.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = null;
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        CollectionType collectionTypeWithValueHandler = collectionType.withValueHandler(handler);
        assertEquals(handler, collectionTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = null;
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        CollectionType collectionTypeWithContentValueHandler = collectionType.withContentValueHandler(handler);
        assertEquals(handler, collectionTypeWithContentValueHandler.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        CollectionType collectionTypeWithStaticTyping = collectionType.withStaticTyping();
        assertEquals(true, collectionTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, refinedType.getRawClass());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
        String expected = "[collection type; class java.lang.String, contains null]";
        assertEquals(expected, collectionType.toString());
    }
}
