
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(null, simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertEquals(null, simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStatic());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
        
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertEquals(null, simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStatic());
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType newSimpleType = simpleType._narrow(Long.class);
        assertEquals(Long.class, newSimpleType.getRawClass());
        assertEquals(null, newSimpleType.getBindings());
        assertEquals(null, newSimpleType.getSuperClass());
        assertEquals(null, newSimpleType.getSuperInterfaces());
        assertEquals(null, newSimpleType.getValueHandler());
        assertEquals(null, newSimpleType.getTypeHandler());
        assertEquals(false, newSimpleType.isStatic());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
        
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));
    }
}
