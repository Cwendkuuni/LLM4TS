package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    
    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.String.class);
    }
    
    @Test
    public void testConstruct() {
        assertNotNull(collectionType);
        assertEquals(java.util.ArrayList.class, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(java.lang.String.class, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.hasStaticTyping());
    }
    
    @Test
    public void testWithContentType() {
        JavaType newType = collectionType.withContentType(java.lang.Integer.class);
        assertEquals(java.lang.Integer.class, newType.getContentType());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType newType = collectionType.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType newType = collectionType.withContentTypeHandler(handler);
        assertEquals(handler, newType.getContentType().getTypeHandler());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType newType = collectionType.withValueHandler(handler);
        assertEquals(handler, newType.getValueHandler());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType newType = collectionType.withContentValueHandler(handler);
        assertEquals(handler, newType.getContentType().getValueHandler());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType newType = collectionType.withStaticTyping();
        assertTrue(newType.hasStaticTyping());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testRefine() {
        JavaType superClass = JavaType.construct(java.util.List.class);
        JavaType[] interfaces = {JavaType.construct(java.util.Collection.class)};
        JavaType newType = collectionType.refine(java.util.LinkedList.class, null, superClass, interfaces);
        assertEquals(java.util.LinkedList.class, newType.getRawClass());
        assertNull(newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(interfaces, newType.getSuperInterfaces());
        assertEquals(java.lang.String.class, newType.getContentType());
        assertNull(newType.getValueHandler());
        assertNull(newType.getTypeHandler());
        assertFalse(newType.hasStaticTyping());
        assertNotSame(collectionType, newType);
    }
    
    @Test
    public void testToString() {
        assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.String]", collectionType.toString());
    }
}