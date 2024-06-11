
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithDeprecated() {
        Class<?> rawType = String.class;
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawType());
        assertNull(collectionType.getBindings());
        assertNotNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        JavaType narrowedType = collectionType.narrow(String.class);

        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawType());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInts, narrowedType.getSuperInterfaces());
        assertEquals(elemT, narrowedType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        Object typeHandler = new Object();
        CollectionType typeWithHandler = collectionType.withTypeHandler(typeHandler);

        assertNotNull(typeWithHandler);
        assertEquals(rawType, typeWithHandler.getRawType());
        assertEquals(bindings, typeWithHandler.getBindings());
        assertEquals(superClass, typeWithHandler.getSuperClass());
        assertEquals(superInts, typeWithHandler.getSuperInterfaces());
        assertEquals(elemT, typeWithHandler.getElementType());
        assertNull(typeWithHandler.getValueHandler());
        assertEquals(typeHandler, typeWithHandler.getTypeHandler());
        assertFalse(typeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        Object typeHandler = new Object();
        CollectionType typeWithContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);

        assertNotNull(typeWithContentTypeHandler);
        assertEquals(rawType, typeWithContentTypeHandler.getRawType());
        assertEquals(bindings, typeWithContentTypeHandler.getBindings());
        assertEquals(superClass, typeWithContentTypeHandler.getSuperClass());
        assertEquals(superInts, typeWithContentTypeHandler.getSuperInterfaces());
        assertEquals(elemT, typeWithContentTypeHandler.getElementType().getTypeHandler());
        assertNull(typeWithContentTypeHandler.getValueHandler());
        assertNull(typeWithContentTypeHandler.getTypeHandler());
        assertFalse(typeWithContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        Object valueHandler = new Object();
        CollectionType typeWithValueHandler = collectionType.withValueHandler(valueHandler);

        assertNotNull(typeWithValueHandler);
        assertEquals(rawType, typeWithValueHandler.getRawType());
        assertEquals(bindings, typeWithValueHandler.getBindings());
        assertEquals(superClass, typeWithValueHandler.getSuperClass());
        assertEquals(superInts, typeWithValueHandler.getSuperInterfaces());
        assertEquals(elemT, typeWithValueHandler.getElementType());
        assertEquals(valueHandler, typeWithValueHandler.getValueHandler());
        assertNull(typeWithValueHandler.getTypeHandler());
        assertFalse(typeWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        Object valueHandler = new Object();
        CollectionType typeWithContentValueHandler = collectionType.withContentValueHandler(valueHandler);

        assertNotNull(typeWithContentValueHandler);
        assertEquals(rawType, typeWithContentValueHandler.getRawType());
        assertEquals(bindings, typeWithContentValueHandler.getBindings());
        assertEquals(superClass, typeWithContentValueHandler.getSuperClass());
        assertEquals(superInts, typeWithContentValueHandler.getSuperInterfaces());
        assertEquals(elemT.getTypeHandler(), typeWithContentValueHandler.getElementType().getTypeHandler());
        assertEquals(valueHandler, typeWithContentValueHandler.getValueHandler());
        assertNull(typeWithContentValueHandler.getTypeHandler());
        assertFalse(typeWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        CollectionType typeWithStaticTyping = collectionType.withStaticTyping();

        assertNotNull(typeWithStaticTyping);
        assertEquals(rawType, typeWithStaticTyping.getRawType());
        assertEquals(bindings, typeWithStaticTyping.getBindings());
        assertEquals(superClass, typeWithStaticTyping.getSuperClass());
        assertEquals(superInts, typeWithStaticTyping.getSuperInterfaces());
        assertEquals(elemT.withStaticTyping(), typeWithStaticTyping.getElementType());
        assertNull(typeWithStaticTyping.getValueHandler());
        assertNull(typeWithStaticTyping.getTypeHandler());
        assertTrue(typeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);

        Class<?> refinedRawType = Integer.class;
        TypeBindings refinedBindings = new TypeBindings();
        JavaType refinedSuperClass = null;
        JavaType[] refinedSuperInterfaces = null;
        JavaType refinedElemT = new JavaType();
        JavaType refinedType = collectionType.refine(refinedRawType, refinedBindings, refinedSuperClass, refinedSuperInterfaces);

        assertNotNull(refinedType);
        assertEquals(refinedRawType, refinedType.getRawType());
        assertEquals(refinedBindings, refinedType.getBindings());
        assertEquals(refinedSuperClass, refinedType.getSuperClass());
        assertEquals(refinedSuperInterfaces, refinedType.getSuperInterfaces());
        assertEquals(refinedElemT, refinedType.getElementType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testToString() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);

        String expectedToString = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        assertEquals(expectedToString, collectionType.toString());
    }
}

