package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        CollectionType collectionType = new CollectionType();
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(contentType, withContentType.getContentType());
        assertNotEquals(collectionType, withContentType);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType();
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertNotNull(withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertNotEquals(collectionType, withTypeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType();
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);
        assertNotNull(withContentTypeHandler);
        assertEquals(typeHandler, withContentTypeHandler.getElementType().getTypeHandler());
        assertNotEquals(collectionType, withContentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType();
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertNotNull(withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertNotEquals(collectionType, withValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType();
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);
        assertNotNull(withContentValueHandler);
        assertEquals(valueHandler, withContentValueHandler.getElementType().getValueHandler());
        assertNotEquals(collectionType, withContentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertTrue(withStaticTyping.isStaticTyping());
        assertNotEquals(collectionType, withStaticTyping);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType();
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInts);
        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInts, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getElementType());
        assertNotEquals(collectionType, refinedType);
    }

    @Test
    public void testToString() {
        Class<?> rawType = CollectionType.class;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType();
        String expectedString = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        assertEquals(expectedString, collectionType.toString());
    }
}