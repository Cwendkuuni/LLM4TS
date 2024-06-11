package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class CollectionTypeTest {
    // Test constructor

    @Test
    public void testCollectionTypeConstructor() {
        JavaType elemType = new SimpleType(int.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        assertInstanceOf(CollectionType.class, collectionType);
        assertSame(List.class, collectionType.getRawClass());
        assertSame(elemType, collectionType.getContentType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertFalse(collectionType.isStatic());
    }

    // Test construct deprecated constructor

    @Test
    public void testConstructDeprecated() {
        JavaType elemType = new SimpleType(int.class);
        JavaType collectionType = CollectionType.construct(List.class, elemType);
        assertInstanceOf(CollectionType.class, collectionType);
        assertSame(List.class, collectionType.getRawClass());
        assertSame(elemType, collectionType.getContentType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertFalse(collectionType.isStatic());
    }

    // Test refine method

    @Test
    public void testRefine() {
        JavaType elemType = new SimpleType(int.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType refinedType = collectionType.refine(ArrayList.class, null, null, null);
        assertInstanceOf(ArrayListType.class, refinedType);
        assertSame(ArrayList.class, refinedType.getRawClass());
        assertSame(elemType, refinedType.getContentType());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
        assertFalse(refinedType.isStatic());
    }

    // Test _narrow method (deprecated)

    @Test(expected = DeprecationException.class)
    public void testNarrowDeprecated() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, new SimpleType(int.class));
        JavaType arrayListType = listType._narrow(ArrayList.class);
    }

    // Test withContentType method

    @Test
    public void testWithContentType() {
        JavaType elemType = new SimpleType(int.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType stringListType = listType.withContentType(String.class);
        assertInstanceOf(CollectionType.class, stringListType);
        assertSame(List.class, stringListType.getRawClass());
        assertSame(String.class, stringListType.getContentType());
        assertNull(stringListType.getSuperClass());
        assertNull(stringListType.getSuperInterfaces());
        assertFalse(stringListType.isStatic());
    }

    // Test withTypeHandler method

    @Test
    public void testWithTypeHandler() {
        JavaType elemType = new SimpleType(int.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType handlerListType = listType.withTypeHandler(handler);
        assertInstanceOf(CollectionType.class, handlerListType);
        assertSame(List.class, handlerListType.getRawClass());
        assertSame(elemType, handlerListType.getContentType());
        assertNull(handlerListType.getSuperClass());
        assertNull(handlerListType.getSuperInterfaces());
        assertFalse(handlerListType.isStatic());
        assertEquals(handler, handlerListType.getTypeHandler());
    }

    // Test withContentTypeHandler method

    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = new SimpleType(int.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType handlerListType = listType.withContentTypeHandler(handler);
        assertInstanceOf(CollectionType.class, handlerListType);
        assertSame(List.class, handlerListType.getRawClass());
        assertSame(elemType, handlerListType.getContentType());
        assertNull(handlerListType.getSuperClass());
        assertNull(handlerListType.getSuperInterfaces());
        assertFalse(handlerListType.isStatic());
        assertEquals(handler, handlerListType.getContentTypeHandler());
    }

    // Test withValueHandler method

    @Test
    public void testWithValueHandler() {
        JavaType elemType = new SimpleType(int.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType handlerListType = listType.withValueHandler(handler);
        assertInstanceOf(CollectionType.class, handlerListType);
        assertSame(List.class, handlerListType.getRawClass());
        assertSame(elemType, handlerListType.getContentType());
        assertNull(handlerListType.getSuperClass());
        assertNull(handlerListType.getSuperInterfaces());
        assertFalse(handlerListType.isStatic());
        assertEquals(handler, handlerListType.getValueHandler());
    }

    // Test withContentValueHandler method

    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = new SimpleType(int.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType handlerListType = listType.withContentValueHandler(handler);
        assertInstanceOf(CollectionType.class, handlerListType);
        assertSame(List.class, handlerListType.getRawClass());
        assertSame(elemType, handlerListType.getContentType());
        assertNull(handlerListType.getSuperClass());
        assertNull(handlerListType.getSuperInterfaces());
        assertFalse(handlerListType.isStatic());
        assertEquals(handler, handlerListType.getContentValueHandler());
    }

    // Test withStaticTyping method

    @Test
    public void testWithStaticTyping() {
        JavaType elemType = new SimpleType(int.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType staticListType = listType.withStaticTyping();
        assertInstanceOf(CollectionType.class, staticListType);
        assertSame(List.class, staticListType.getRawClass());
        assertSame(elemType, staticListType.getContentType());
        assertNull(staticListType.getSuperClass());
        assertNull(staticListType.getSuperInterfaces());
        assertTrue(staticListType.isStatic());
    }
}