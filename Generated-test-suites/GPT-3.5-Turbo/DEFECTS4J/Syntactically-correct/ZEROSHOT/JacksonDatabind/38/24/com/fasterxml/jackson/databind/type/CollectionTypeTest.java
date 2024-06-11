package com.fasterxml.jackson.databind.type;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();
        JavaType[] superInterfaces = {JavaType.genericJavaType()};

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getElementType());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();
        JavaType contentType = JavaType.genericJavaType();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        JavaType withContentType = collectionType.withContentType(contentType);

        assertEquals(rawType, withContentType.getRawClass());
        assertEquals(bindings, withContentType.getBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertEquals(superInts, withContentType.getInterfaces());
        assertEquals(contentType, withContentType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();
        Object typeHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);

        assertEquals(rawType, withTypeHandler.getRawClass());
        assertEquals(bindings, withTypeHandler.getBindings());
        assertEquals(superClass, withTypeHandler.getSuperClass());
        assertEquals(superInts, withTypeHandler.getInterfaces());
        assertEquals(elemT, withTypeHandler.getElementType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();
        Object contentTypeHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);

        assertEquals(rawType, withContentTypeHandler.getRawClass());
        assertEquals(bindings, withContentTypeHandler.getBindings());
        assertEquals(superClass, withContentTypeHandler.getSuperClass());
        assertEquals(superInts, withContentTypeHandler.getInterfaces());
        assertEquals(elemT.withTypeHandler(contentTypeHandler), withContentTypeHandler.getElementType());
        assertEquals(contentTypeHandler, withContentTypeHandler.getElementType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();
        Object valueHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);

        assertEquals(rawType, withValueHandler.getRawClass());
        assertEquals(bindings, withValueHandler.getBindings());
        assertEquals(superClass, withValueHandler.getSuperClass());
        assertEquals(superInts, withValueHandler.getInterfaces());
        assertEquals(elemT, withValueHandler.getElementType());
        assertEquals(valueHandler, withValueHandler.getElementType().getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();
        Object contentValueHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);

        assertEquals(rawType, withContentValueHandler.getRawClass());
        assertEquals(bindings, withContentValueHandler.getBindings());
        assertEquals(superClass, withContentValueHandler.getSuperClass());
        assertEquals(superInts, withContentValueHandler.getInterfaces());
        assertEquals(elemT.withValueHandler(contentValueHandler), withContentValueHandler.getElementType());
        assertEquals(contentValueHandler, withContentValueHandler.getElementType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType withStaticTyping = collectionType.withStaticTyping();

        assertEquals(rawType, withStaticTyping.getRawClass());
        assertEquals(bindings, withStaticTyping.getBindings());
        assertEquals(superClass, withStaticTyping.getSuperClass());
        assertEquals(superInts, withStaticTyping.getInterfaces());
        assertEquals(elemT.withStaticTyping(), withStaticTyping.getElementType());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.genericJavaType();
        JavaType[] superInts = {JavaType.genericJavaType()};
        JavaType elemT = JavaType.genericJavaType();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals("[collection type; class "+rawType.getName()+", contains "+elemT+"]", collectionType.toString());
    }
}