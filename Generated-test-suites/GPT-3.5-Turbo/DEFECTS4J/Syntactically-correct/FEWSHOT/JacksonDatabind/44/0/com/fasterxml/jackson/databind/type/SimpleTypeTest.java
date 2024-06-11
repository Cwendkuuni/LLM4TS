package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, type.getRawClass());
        assertNull(type.getContentType());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertFalse(type.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(java.util.Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(java.util.Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(Object.class);
        SimpleType narrowedType = type.narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(Object.class);
        SimpleType withContentType = type.withContentType(SimpleType.construct(String.class));
        assertEquals(String.class, withContentType.getContentType().getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType type = SimpleType.construct(String.class);
        type.withContentType(SimpleType.construct(Integer.class));
    }
    
    @Test
    public void testWithNullValueHandler() {
        SimpleType type = SimpleType.construct(Object.class);
        SimpleType withValueHandler = type.withValueHandler(null);
        assertNull(withValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithNonNullValueHandler() {
        SimpleType type = SimpleType.construct(Object.class);
        DummyValueHandler valueHandler = new DummyValueHandler();
        SimpleType withValueHandler = type.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerThrowsException() {
        SimpleType type = SimpleType.construct(Object.class);
        type.withContentValueHandler(new DummyValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(Object.class);
        SimpleType withStaticTyping = type.withStaticTyping();
        assertTrue(withStaticTyping.isContainerType());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(Object.class);
        JavaType refinedType = type.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(Object.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(String.class);
        String toString = type.toString();
        assertEquals("[simple type, class java.lang.String]", toString);
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(Object.class);
        SimpleType type2 = SimpleType.construct(Object.class);
        assertTrue(type1.equals(type2));
    }
    
    private static class DummyValueHandler {
    	
    }
}