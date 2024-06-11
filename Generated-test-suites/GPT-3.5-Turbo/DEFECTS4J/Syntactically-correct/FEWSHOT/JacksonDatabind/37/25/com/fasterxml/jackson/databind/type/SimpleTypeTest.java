package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForArray() {
        SimpleType.construct(int[].class);
    }
    
    @Test
    public void testConstructSimpleType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals("", simpleType.getCanonicalName());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        
        String toString = simpleType.toString();
        assertEquals("[simple type, class String]", toString);
        
        String erasedSignature = simpleType.getErasedSignature(new StringBuilder()).toString();
        assertEquals("Ljava/lang/String;", erasedSignature);
        
        String genericSignature = simpleType.getGenericSignature(new StringBuilder()).toString();
        assertEquals("Ljava/lang/String;", genericSignature);
        
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals("String"));
        assertTrue(simpleType.equals(SimpleType.construct(String.class)));
        assertFalse(simpleType.equals(SimpleType.construct(Integer.class)));
    }
}