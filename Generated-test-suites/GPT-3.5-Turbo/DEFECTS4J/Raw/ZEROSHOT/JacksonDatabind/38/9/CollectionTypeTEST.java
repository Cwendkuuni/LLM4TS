package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = null;
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.class;

        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType updatedCollectionType = collectionType.withContentType(contentType);

        assertEquals(contentType, updatedCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();

        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType updatedCollectionType = collectionType.withTypeHandler(typeHandler);

        assertEquals(typeHandler, updatedCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();

        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType updatedCollectionType = collectionType.withContentTypeHandler(typeHandler);

        assertEquals(typeHandler, updatedCollectionType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();

        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType updatedCollectionType = collectionType.withValueHandler(valueHandler);

        assertEquals(valueHandler, updatedCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();

        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType updatedCollectionType = collectionType.withContentValueHandler(valueHandler);

        assertEquals(valueHandler, updatedCollectionType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType updatedCollectionType = collectionType.withStaticTyping();

        assertTrue(updatedCollectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = null;
        JavaType superClass = JavaType.class;
        JavaType[] superInterfaces = new JavaType[0];

        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getSuperInterfaces());
    }

    @Test
    public void testToString() {
        Class<?> rawType = CollectionType.class;
        JavaType elementType = JavaType.class;

        CollectionType collectionType = new CollectionType(rawType, null, null, null, elementType, null, null, false);

        assertEquals("[collection type; class " + rawType.getName() + ", contains " + elementType + "]", collectionType.toString());
    }
}