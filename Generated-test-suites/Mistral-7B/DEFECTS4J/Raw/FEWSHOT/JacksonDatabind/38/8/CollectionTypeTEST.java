package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.Class;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        assertNotNull(collectionType);
        assertSame(List.class, collectionType.getRawClass());
        assertSame(elemType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType elemType = new SimpleType(Integer.class);
        TypeBindings bindings = new TypeBindings();
        JavaType collectionType = CollectionType.construct(List.class, bindings, null, null, elemType);
        assertNotNull(collectionType);
        assertSame(List.class, collectionType.getRawClass());
        assertSame(elemType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertSame(bindings, collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType collectionType = CollectionType.construct(List.class, null, elemType);
        assertNotNull(collectionType);
        assertSame(List.class, collectionType.getRawClass());
        assertSame(elemType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertFalse(collectionType.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, IntegerType.INSTANCE);
        JavaType setType = CollectionType.construct(Set.class, null, null, null, IntegerType.INSTANCE);
        JavaType narrowedListType = listType._narrow(Set.class);
        narrowedListType.narrow(ArrayList.class);
    }

    @Test
    public void testWithContentType() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType stringListType = listType.withContentType(StringType.INSTANCE);
        assertNotNull(stringListType);
        assertSame(List.class, stringListType.getRawClass());
        assertSame(StringType.INSTANCE, stringListType.getContentType());
        assertNull(stringListType.getTypeHandler());
        assertNull(stringListType.getValueHandler());
        assertNull(stringListType.getSuperClass());
        assertNull(stringListType.getSuperInterfaces());
        assertFalse(stringListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType listTypeWithHandler = listType.withTypeHandler(handler);
        assertNotNull(listTypeWithHandler);
        assertSame(List.class, listTypeWithHandler.getRawClass());
        assertSame(elemType, listTypeWithHandler.getContentType());
        assertSame(handler, listTypeWithHandler.getTypeHandler());
        assertNull(listTypeWithHandler.getValueHandler());
        assertNull(listTypeWithHandler.getSuperClass());
        assertNull(listTypeWithHandler.getSuperInterfaces());
        assertFalse(listTypeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object contentHandler = new Object();
        Object typeHandler = new Object();
        JavaType listTypeWithContentHandler = listType.withContentTypeHandler(contentHandler).withTypeHandler(typeHandler);
        assertNotNull(listTypeWithContentHandler);
        assertSame(List.class, listTypeWithContentHandler.getRawClass());
        assertSame(elemType, listTypeWithContentHandler.getContentType());
        assertSame(contentHandler, listTypeWithContentHandler.getContentTypeHandler());
        assertSame(typeHandler, listTypeWithContentHandler.getTypeHandler());
        assertNull(listTypeWithContentHandler.getValueHandler());
        assertNull(listTypeWithContentHandler.getSuperClass());
        assertNull(listTypeWithContentHandler.getSuperInterfaces());
        assertFalse(listTypeWithContentHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType listTypeWithHandler = listType.withValueHandler(handler);
        assertNotNull(listTypeWithHandler);
        assertSame(List.class, listTypeWithHandler.getRawClass());
        assertSame(elemType, listTypeWithHandler.getContentType());
        assertNull(listTypeWithHandler.getTypeHandler());
        assertSame(handler, listTypeWithHandler.getValueHandler());
        assertNull(listTypeWithHandler.getSuperClass());
        assertNull(listTypeWithHandler.getSuperInterfaces());
        assertFalse(listTypeWithHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Object contentHandler = new Object();
        Object valueHandler = new Object();
        JavaType listTypeWithContentValueHandler = listType.withContentValueHandler(contentHandler).withValueHandler(valueHandler);
        assertNotNull(listTypeWithContentValueHandler);
        assertSame(List.class, listTypeWithContentValueHandler.getRawClass());
        assertSame(elemType, listTypeWithContentValueHandler.getContentType());
        assertSame(contentHandler, listTypeWithContentValueHandler.getContentValueHandler());
        assertSame(valueHandler, listTypeWithContentValueHandler.getValueHandler());
        assertNull(listTypeWithContentValueHandler.getTypeHandler());
        assertNull(listTypeWithContentValueHandler.getSuperClass());
        assertNull(listTypeWithContentValueHandler.getSuperInterfaces());
        assertFalse(listTypeWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType staticListType = listType.withStaticTyping();
        assertNotNull(staticListType);
        assertSame(List.class, staticListType.getRawClass());
        assertSame(elemType, staticListType.getContentType());
        assertNull(staticListType.getTypeHandler());
        assertNull(staticListType.getValueHandler());
        assertNull(staticListType.getSuperClass());
        assertNull(staticListType.getSuperInterfaces());
        assertTrue(staticListType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType elemType = new SimpleType(Integer.class);
        JavaType listType = CollectionType.construct(List.class, null, null, null, elemType);
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refinedListType = listType.refine(rawType, bindings, null, null);
        assertNotNull(refinedListType);
        assertSame(ArrayList.class, refinedListType.getRawClass());
        assertSame(elemType, refinedListType.getContentType());
        assertNull(refinedListType.getTypeHandler());
        assertNull(refinedListType.getValueHandler());
        assertNull(refinedListType.getSuperClass());
        assertArrayEquals(new Class[] { ArrayList.class }, refinedListType.getSuperInterfaces());
        assertFalse(refinedListType.isStatic());
    }

    private JavaType[] assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (Arrays.equals(expected, actual)) {
            return null;
        }
        throw new AssertionError("Expected: " + Arrays.toString(expected) + ", but was: " + Arrays.toString(actual));
    }
}