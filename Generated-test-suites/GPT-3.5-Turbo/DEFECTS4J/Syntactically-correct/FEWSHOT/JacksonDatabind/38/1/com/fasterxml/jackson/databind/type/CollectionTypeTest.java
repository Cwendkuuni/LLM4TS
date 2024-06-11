package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertEquals(false, collectionType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};
        Object typeHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType collectionTypeWithTypeHandler = collectionType.withTypeHandler(typeHandler);

        assertEquals(rawType, collectionTypeWithTypeHandler.getRawClass());
        assertEquals(bindings, collectionTypeWithTypeHandler.getBindings());
        assertEquals(superClass, collectionTypeWithTypeHandler.getSuperClass());
        assertEquals(superInts, collectionTypeWithTypeHandler.getSuperInterfaces());
        assertEquals(elemT, collectionTypeWithTypeHandler.getElementType());
        assertNull(collectionTypeWithTypeHandler.getValueHandler());
        assertEquals(typeHandler, collectionTypeWithTypeHandler.getTypeHandler());
        assertEquals(false, collectionTypeWithTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};
        Object typeHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType collectionTypeWithContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);

        assertEquals(rawType, collectionTypeWithContentTypeHandler.getRawClass());
        assertEquals(bindings, collectionTypeWithContentTypeHandler.getBindings());
        assertEquals(superClass, collectionTypeWithContentTypeHandler.getSuperClass());
        assertEquals(superInts, collectionTypeWithContentTypeHandler.getSuperInterfaces());
        assertEquals(elemT, collectionTypeWithContentTypeHandler.getElementType());
        assertNull(collectionTypeWithContentTypeHandler.getValueHandler());
        assertNull(collectionTypeWithContentTypeHandler.getTypeHandler());
        assertEquals(false, collectionTypeWithContentTypeHandler.isStatic());
        assertEquals(typeHandler, collectionTypeWithContentTypeHandler.getElementType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};
        Object valueHandler = new Object();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType collectionTypeWithContentValueHandler = collectionType.withContentValueHandler(valueHandler);

        assertEquals(rawType, collectionTypeWithContentValueHandler.getRawClass());
        assertEquals(bindings, collectionTypeWithContentValueHandler.getBindings());
        assertEquals(superClass, collectionTypeWithContentValueHandler.getSuperClass());
        assertEquals(superInts, collectionTypeWithContentValueHandler.getSuperInterfaces());
        assertEquals(elemT, collectionTypeWithContentValueHandler.getElementType());
        assertEquals(valueHandler, collectionTypeWithContentValueHandler.getElementType().getValueHandler());
        assertNull(collectionTypeWithContentValueHandler.getTypeHandler());
        assertEquals(false, collectionTypeWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType collectionTypeWithStaticTyping = collectionType.withStaticTyping();

        assertEquals(rawType, collectionTypeWithStaticTyping.getRawClass());
        assertEquals(bindings, collectionTypeWithStaticTyping.getBindings());
        assertEquals(superClass, collectionTypeWithStaticTyping.getSuperClass());
        assertEquals(superInts, collectionTypeWithStaticTyping.getSuperInterfaces());
        assertEquals(elemT.withStaticTyping(), collectionTypeWithStaticTyping.getElementType());
        assertNull(collectionTypeWithStaticTyping.getValueHandler());
        assertNull(collectionTypeWithStaticTyping.getTypeHandler());
        assertEquals(true, collectionTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};
        Class<?> subclass = java.util.ArrayList.class;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType refinedCollectionType = (CollectionType) collectionType._narrow(subclass);

        assertEquals(subclass, refinedCollectionType.getRawClass());
        assertEquals(bindings, refinedCollectionType.getBindings());
        assertEquals(superClass, refinedCollectionType.getSuperClass());
        assertEquals(superInts, refinedCollectionType.getSuperInterfaces());
        assertEquals(elemT, refinedCollectionType.getElementType());
        assertNull(refinedCollectionType.getValueHandler());
        assertNull(refinedCollectionType.getTypeHandler());
        assertEquals(false, refinedCollectionType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT1 = new JavaType();
        JavaType elemT2 = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT1);
        CollectionType collectionTypeWithContentType = collectionType.withContentType(elemT2);

        assertEquals(rawType, collectionTypeWithContentType.getRawClass());
        assertEquals(bindings, collectionTypeWithContentType.getBindings());
        assertEquals(superClass, collectionTypeWithContentType.getSuperClass());
        assertEquals(superInts, collectionTypeWithContentType.getSuperInterfaces());
        assertEquals(elemT2, collectionTypeWithContentType.getElementType());
        assertNull(collectionTypeWithContentType.getValueHandler());
        assertNull(collectionTypeWithContentType.getTypeHandler());
        assertEquals(false, collectionTypeWithContentType.isStatic());
    }

    @Test
    public void testToString() {
        Class<?> rawType = java.util.List.class;
        JavaType elemT = new JavaType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[]{};

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        String expectedToString = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        assertEquals(expectedToString, collectionType.toString());
    }
}