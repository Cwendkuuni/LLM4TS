package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Collection;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        assertNotNull(simpleType);
        assertEquals(Map.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(int[].class);
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Map.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Map.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        
        SimpleType simpleType3 = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        assertEquals("[simple type, class java.util.Map]", simpleType.toString());
    }
}