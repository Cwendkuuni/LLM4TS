package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;


public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = List.class;
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNotNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = ArrayList.class;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        JavaType narrowType = collectionType._narrow(subclass);

        assertNotNull(narrowType);
        assertEquals(subclass, narrowType.getRawClass());
        assertEquals(collectionType.getBindings(), narrowType.getBindings());
        assertEquals(collectionType.getSuperClass(), narrowType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), narrowType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), narrowType.getElementType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        JavaType withContentType = collectionType.withContentType(contentType);

        assertNotNull(withContentType);
        assertEquals(collectionType.getRawClass(), withContentType.getRawClass());
        assertEquals(collectionType.getBindings(), withContentType.getBindings());
        assertEquals(collectionType.getSuperClass(), withContentType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withContentType.getSuperInterfaces());
        assertEquals(contentType, withContentType.getElementType());
        assertEquals(collectionType.getValueHandler(), withContentType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withContentType.getTypeHandler());
        assertEquals(collectionType.isStatic(), withContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);

        assertNotNull(withTypeHandler);
        assertEquals(collectionType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(collectionType.getBindings(), withTypeHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withTypeHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withTypeHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), withTypeHandler.getElementType());
        assertEquals(collectionType.getValueHandler(), withTypeHandler.getValueHandler());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);

        assertNotNull(withContentTypeHandler);
        assertEquals(collectionType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(collectionType.getBindings(), withContentTypeHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withContentTypeHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withContentTypeHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(typeHandler), withContentTypeHandler.getElementType());
        assertEquals(collectionType.getValueHandler(), withContentTypeHandler.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withContentTypeHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);

        assertNotNull(withValueHandler);
        assertEquals(collectionType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(collectionType.getBindings(), withValueHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withValueHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withValueHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), withValueHandler.getElementType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withValueHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);

        assertNotNull(withContentValueHandler);
        assertEquals(collectionType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(collectionType.getBindings(), withContentValueHandler.getBindings());
        assertEquals(collectionType.getSuperClass(), withContentValueHandler.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withContentValueHandler.getSuperInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(valueHandler), withContentValueHandler.getElementType());
        assertEquals(collectionType.getValueHandler(), withContentValueHandler.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withContentValueHandler.getTypeHandler());
        assertEquals(collectionType.isStatic(), withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        CollectionType withStaticTyping = collectionType.withStaticTyping();

        assertNotNull(withStaticTyping);
        assertEquals(collectionType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(collectionType.getBindings(), withStaticTyping.getBindings());
        assertEquals(collectionType.getSuperClass(), withStaticTyping.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), withStaticTyping.getSuperInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), withStaticTyping.getElementType());
        assertEquals(collectionType.getValueHandler(), withStaticTyping.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), withStaticTyping.getTypeHandler());
        assertEquals(true, withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        JavaType refineType = collectionType.refine(rawType, bindings, superClass, superInts);

        assertNotNull(refineType);
        assertEquals(rawType, refineType.getRawClass());
        assertEquals(bindings, refineType.getBindings());
        assertEquals(superClass, refineType.getSuperClass());
        assertEquals(superInts, refineType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), refineType.getElementType());
        assertEquals(collectionType.getValueHandler(), refineType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), refineType.getTypeHandler());
        assertEquals(collectionType.isStatic(), refineType.isStatic());
    }

    @Test
    public void testToString() {
        Class<?> rawType = List.class;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(null, null, null, null, elemT, null, null, false);

        String toString = collectionType.toString();

        assertEquals("[collection type; class " + rawType.getName() + ", contains " + elemT + "]", toString);
    }
}