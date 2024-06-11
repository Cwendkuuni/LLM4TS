package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.*;

public class CollectionTypeTest {

    @Test
    public void constructTest() {
        Class<?> rawType = String.class;
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
        assertFalse(collectionType.isContainerType());
        assertFalse(collectionType.isMapLikeType());
        assertFalse(collectionType.isReferenceType());
        assertTrue(collectionType.isCollectionLikeType());
        assertEquals(rawType.getName() + " contains " + elemT, collectionType.toString());
    }

    @Test
    public void narrowTest() {
        Class<?> subclass = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elementType = new JavaType();
        CollectionType collectionType = new CollectionType(subclass, bindings, superClass, superInterfaces, elementType, null, null, false);
        JavaType narrowedType = collectionType._narrow(subclass);
        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInterfaces, narrowedType.getInterfaces());
        assertEquals(elementType, narrowedType.getElementType());
        assertFalse(narrowedType.isContainerType());
        assertFalse(narrowedType.isMapLikeType());
        assertFalse(narrowedType.isReferenceType());
        assertTrue(narrowedType.isCollectionLikeType());
        assertFalse(collectionType.isStaticTyping());
        assertEquals(subclass.getName() + " contains " + elementType, narrowedType.toString());
    }

    @Test
    public void withContentTypeTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType contentType = new JavaType();
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(rawType, withContentType.getRawClass());
        assertEquals(bindings, withContentType.getBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertEquals(superInts, withContentType.getInterfaces());
        assertEquals(contentType, withContentType.getElementType());
        assertFalse(withContentType.isContainerType());
        assertFalse(withContentType.isMapLikeType());
        assertFalse(withContentType.isReferenceType());
        assertTrue(withContentType.isCollectionLikeType());
        assertFalse(withContentType.isStaticTyping());
        assertEquals(rawType.getName() + " contains " + contentType, withContentType.toString());
    }

    @Test
    public void withTypeHandlerTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertNotNull(withTypeHandler);
        assertEquals(rawType, withTypeHandler.getRawClass());
        assertEquals(bindings, withTypeHandler.getBindings());
        assertEquals(superClass, withTypeHandler.getSuperClass());
        assertEquals(superInts, withTypeHandler.getInterfaces());
        assertEquals(elemT, withTypeHandler.getElementType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isContainerType());
        assertFalse(withTypeHandler.isMapLikeType());
        assertFalse(withTypeHandler.isReferenceType());
        assertTrue(withTypeHandler.isCollectionLikeType());
        assertFalse(withTypeHandler.isStaticTyping());
        assertEquals(rawType.getName() + " contains " + elemT, withTypeHandler.toString());
    }

    @Test
    public void withContentTypeHandlerTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType contentType = new JavaType();
        CollectionType withContentType = collectionType.withContentTypeHandler(typeHandler);
        assertNotNull(withContentType);
        assertEquals(rawType, withContentType.getRawClass());
        assertEquals(bindings, withContentType.getBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertEquals(superInts, withContentType.getInterfaces());
        assertEquals(contentType, withContentType.getElementType());
        assertEquals(typeHandler, withContentType.getTypeHandler());
        assertFalse(withContentType.isContainerType());
        assertFalse(withContentType.isMapLikeType());
        assertFalse(withContentType.isReferenceType());
        assertTrue(withContentType.isCollectionLikeType());
        assertFalse(withContentType.isStaticTyping());
        assertEquals(rawType.getName() + " contains " + contentType, withContentType.toString());
    }

    @Test
    public void withValueHandlerTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertNotNull(withValueHandler);
        assertEquals(rawType, withValueHandler.getRawClass());
        assertEquals(bindings, withValueHandler.getBindings());
        assertEquals(superClass, withValueHandler.getSuperClass());
        assertEquals(superInts, withValueHandler.getInterfaces());
        assertEquals(elemT, withValueHandler.getElementType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isContainerType());
        assertFalse(withValueHandler.isMapLikeType());
        assertFalse(withValueHandler.isReferenceType());
        assertTrue(withValueHandler.isCollectionLikeType());
        assertFalse(withValueHandler.isStaticTyping());
        assertEquals(rawType.getName() + " contains " + elemT, withValueHandler.toString());
    }

    @Test
    public void withContentValueHandlerTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType contentType = new JavaType();
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);
        assertNotNull(withContentValueHandler);
        assertEquals(rawType, withContentValueHandler.getRawClass());
        assertEquals(bindings, withContentValueHandler.getBindings());
        assertEquals(superClass, withContentValueHandler.getSuperClass());
        assertEquals(superInts, withContentValueHandler.getInterfaces());
        assertEquals(contentType, withContentValueHandler.getElementType());
        assertEquals(valueHandler, withContentValueHandler.getValueHandler());
        assertFalse(withContentValueHandler.isContainerType());
        assertFalse(withContentValueHandler.isMapLikeType());
        assertFalse(withContentValueHandler.isReferenceType());
        assertTrue(withContentValueHandler.isCollectionLikeType());
        assertFalse(withContentValueHandler.isStaticTyping());
        assertEquals(rawType.getName() + " contains " + contentType, withContentValueHandler.toString());
    }

    @Test
    public void withStaticTypingTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(rawType, withStaticTyping.getRawClass());
        assertEquals(bindings, withStaticTyping.getBindings());
        assertEquals(superClass, withStaticTyping.getSuperClass());
        assertEquals(superInts, withStaticTyping.getInterfaces());
        assertEquals(elemT, withStaticTyping.getElementType());
        assertFalse(withStaticTyping.isContainerType());
        assertFalse(withStaticTyping.isMapLikeType());
        assertFalse(withStaticTyping.isReferenceType());
        assertTrue(withStaticTyping.isCollectionLikeType());
        assertTrue(withStaticTyping.isStaticTyping());
        assertEquals(rawType.getName() + " contains " + elemT, withStaticTyping.toString());
    }

    @Test
    public void refineTest() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Class<?> refinedRawType = String.class;
        TypeBindings refinedBindings = new TypeBindings();
        JavaType refinedSuperClass = new JavaType();
        JavaType[] refinedSuperInterfaces = new JavaType[0];
        CollectionType refinedType = (CollectionType) collectionType.refine(refinedRawType, refinedBindings, refinedSuperClass, refinedSuperInterfaces);
        assertNotNull(refinedType);
        assertEquals(refinedRawType, refinedType.getRawClass());
        assertEquals(refinedBindings, refinedType.getBindings());
        assertEquals(refinedSuperClass, refinedType.getSuperClass());
        assertEquals(refinedSuperInterfaces, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getElementType());
        assertFalse(refinedType.isContainerType());
        assertFalse(refinedType.isMapLikeType());
        assertFalse(refinedType.isReferenceType());
        assertTrue(refinedType.isCollectionLikeType());
        assertFalse(refinedType.isStaticTyping());
        assertEquals(refinedRawType.getName() + " contains " + elemT, refinedType.toString());
    }

}