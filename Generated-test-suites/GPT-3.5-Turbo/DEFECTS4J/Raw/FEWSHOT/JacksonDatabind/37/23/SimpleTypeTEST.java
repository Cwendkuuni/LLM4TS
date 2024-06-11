package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        assertEquals(Map.class, simpleType.getRawClass());
        
        SimpleType simpleType2 = SimpleType.constructUnsafe(Collection.class);
        assertEquals(Collection.class, simpleType2.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Object[].class);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }
    }
    
    @Test
    public void testNewInstanceOfSubclass() {
        SimpleType simpleType = SimpleType.construct(String.class);
        
        JavaType javaType = simpleType.narrow(String.class);
        assertTrue(javaType instanceof SimpleType);
        
        SimpleType newSimpleType = (SimpleType) javaType;
        assertEquals(String.class, newSimpleType.getRawClass());
    }
    
    @Test
    public void testNewInstanceOfNonSubclass() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        
        JavaType javaType = simpleType.narrow(Map.class);
        assertTrue(javaType instanceof SimpleType);
        
        SimpleType newSimpleType = (SimpleType) javaType;
        assertEquals(Map.class, newSimpleType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        
        try {
            simpleType.withContentType(String.class);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        try {
            simpleType.withContentTypeHandler(null);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        try {
            simpleType.withContentValueHandler(null);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        
        try {
            simpleType.refine(Map.class, TypeBindings.emptyBindings(), null, null);
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        
        assertEquals("[simple type, class java.util.Map]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        SimpleType sameSimpleType = SimpleType.construct(Map.class);
        
        assertTrue(simpleType.equals(sameSimpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));
        
        SimpleType differentSimpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.equals(differentSimpleType));
        
        SimpleType differentSimpleType2 = SimpleType.construct(Map.class);
        TypeBindings typeBindings = simpleType.getBindings();
        differentSimpleType2.setBindings(typeBindings);
        assertFalse(simpleType.equals(differentSimpleType2));
    }
    
}