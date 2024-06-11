package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(Object[].class);
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Number.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(Integer.class);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        SimpleType withContentType = (SimpleType) simpleType.withContentType(null);
        assertEquals(simpleType, withContentType);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithContentTypeException() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        simpleType.withContentType(null);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        SimpleType withContentTypeHandler = (SimpleType) simpleType.withContentTypeHandler(null);
        assertEquals(simpleType, withContentTypeHandler);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithContentTypeHandlerException() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        simpleType.withContentTypeHandler(null);
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        SimpleType withValueHandler = (SimpleType) simpleType.withValueHandler(null);
        assertEquals(simpleType, withValueHandler);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithValueHandlerException() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        simpleType.withValueHandler(null);
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        SimpleType withContentValueHandler = (SimpleType) simpleType.withContentValueHandler(null);
        assertEquals(simpleType, withContentValueHandler);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithContentValueHandlerException() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        simpleType.withContentValueHandler(null);
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        SimpleType withStaticTyping = (SimpleType) simpleType.withStaticTyping();
        assertEquals(simpleType, withStaticTyping);
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        JavaType refinedType = simpleType.refine(null, null, null, null);
        assertTrue(refinedType == null);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Integer.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Integer.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(""));
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }
}