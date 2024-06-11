package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(TestClass.class, null, null, null, null);
        assertEquals(TestClass.class, collectionType.getRawClass());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertEquals(false, collectionType.isAsStatic());
    }
    
    @Test
    public void test_refine() {
        CollectionType baseType = CollectionType.construct(BaseClass.class, null, null, null, null);
        JavaType elemT = new JavaType(TestClass.class);
        CollectionType refinedType = baseType.refine(RefinedClass.class, null, null, null);
        assertEquals(RefinedClass.class, refinedType.getRawClass());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertEquals(false, refinedType.isAsStatic());
        assertNotEquals(baseType, refinedType);
    }
    
    @Test
    public void test_withContentType() {
        JavaType contentType = new JavaType(TestClass.class);
        CollectionType collectionType = CollectionType.construct(BaseClass.class, null, null, null, null);
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertEquals(contentType, withContentType.getContentType());
        assertNotEquals(collectionType, withContentType);
    }
    
    @Test
    public void test_withTypeHandler() {
        Object testHandler = new Object();
        CollectionType collectionType = CollectionType.construct(BaseClass.class, null, null, null, null);
        CollectionType withTypeHandler = collectionType.withTypeHandler(testHandler);
        assertEquals(testHandler, withTypeHandler.getTypeHandler());
        assertNotEquals(collectionType, withTypeHandler);
    }
    
    @Test
    public void test_withContentTypeHandler() {
        Object testHandler = new Object();
        CollectionType collectionType = CollectionType.construct(BaseClass.class, null, null, null, null);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(testHandler);
        assertEquals(testHandler, withContentTypeHandler.getContentType().getTypeHandler());
        assertNotEquals(collectionType, withContentTypeHandler);
    }
    
    @Test
    public void test_withValueHandler() {
        Object testHandler = new Object();
        CollectionType collectionType = CollectionType.construct(BaseClass.class, null, null, null, null);
        CollectionType withValueHandler = collectionType.withValueHandler(testHandler);
        assertEquals(testHandler, withValueHandler.getValueHandler());
        assertNotEquals(collectionType, withValueHandler);
    }
    
    @Test
    public void test_withContentValueHandler() {
        Object testHandler = new Object();
        CollectionType collectionType = CollectionType.construct(BaseClass.class, null, null, null, null);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(testHandler);
        assertEquals(testHandler, withContentValueHandler.getContentType().getValueHandler());
        assertNotEquals(collectionType, withContentValueHandler);
    }
    
    @Test
    public void test_withStaticTyping() {
        CollectionType collectionType = CollectionType.construct(BaseClass.class, null, null, null, null);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals(true, withStaticTyping.isAsStatic());
        assertNotEquals(collectionType, withStaticTyping);
    }
    
    @Test
    public void test_toString() {
        CollectionType collectionType = CollectionType.construct(TestClass.class, null, null, null, null);
        assertEquals("[collection type; class com.fasterxml.jackson.databind.type.TestClass, contains null]", collectionType.toString());
    }
    
    private static class TestClass{}
    
    private static class BaseClass{}
    
    private static class RefinedClass extends BaseClass{}
}