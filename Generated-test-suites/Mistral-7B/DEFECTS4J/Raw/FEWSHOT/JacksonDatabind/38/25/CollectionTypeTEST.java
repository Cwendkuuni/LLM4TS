package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    // Test constructor
    @Test
    public void testConstruct() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, elemType);
        assertNotNull(collectionType);
        assertEquals(listType, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getSupertypes());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    // Test deprecated constructor
    @Test
    public void testConstructDeprecated() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = CollectionType.construct(listType, elemType);
        assertNotNull(collectionType);
        assertEquals(listType, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertArrayEquals(new JavaType[0], collectionType.getSupertypes());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    // Test _narrow method (deprecated)
    @Test
    public void testNarrowDeprecated() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        assertNotNull(collectionType);
        CollectionType narrowedType = collectionType._narrow(LinkedList.class);
        assertNotNull(narrowedType);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(elemType, narrowedType.getContentType());
        assertArrayEquals(new JavaType[0], narrowedType.getSupertypes());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    // Test withContentType method
    @Test
    public void testWithContentType() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        JavaType stringType = StringType.getInstance();
        CollectionType newCollectionType = collectionType.withContentType(stringType);
        assertNotNull(newCollectionType);
        assertEquals(listType, newCollectionType.getRawClass());
        assertEquals(stringType, newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(newCollectionType);
        assertEquals(listType, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertNull(newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(valueHandler).withTypeHandler(typeHandler);
        assertNotNull(newCollectionType);
        assertEquals(listType, newCollectionType.getRawClass());
        assertEquals(elemType.withValueHandler(valueHandler), newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        Object valueHandler = new Object();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(listType, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler).withTypeHandler(typeHandler);
        assertNotNull(newCollectionType);
        assertEquals(listType, newCollectionType.getRawClass());
        assertEquals(elemType.withValueHandler(valueHandler), newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotNull(newCollectionType);
        assertEquals(listType, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isStatic());
    }

    // Test refine method
    @Test
    public void testRefine() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = IntegerType.getInstance();
        CollectionType collectionType = new CollectionType(listType, null, null, null, elemType, null, null, false);
        Class<?> subListType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[0];
        CollectionType newCollectionType = collectionType.refine(subListType, bindings, superClass, superInterfaces);
        assertNotNull(newCollectionType);
        assertEquals(subListType, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertArrayEquals(new JavaType[0], newCollectionType.getSupertypes());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }
}