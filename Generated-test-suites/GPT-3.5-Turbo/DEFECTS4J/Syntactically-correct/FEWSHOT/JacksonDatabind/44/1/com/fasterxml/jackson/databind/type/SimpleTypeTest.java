package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructor() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        
        assertEquals(Integer.class, simpleType1.getRawClass());
        assertEquals(String.class, simpleType2.getRawClass());
    }
    
    @Test
    public void testCopyConstructor() {
        SimpleType base = new SimpleType(Integer.class);
        SimpleType simpleType = new SimpleType(base);
        
        assertEquals(Integer.class, simpleType.getRawClass());
        assertNotEquals(base.getTypeHandler(), simpleType.getTypeHandler());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        
        assertEquals(Integer.class, simpleType.getRawClass());
    }
    
    @Test
    @Deprecated
    public void testConstruct() {
        SimpleType simpleType1 = SimpleType.construct(Integer.class);
        
        assertEquals(Integer.class, simpleType1.getRawClass());
        
        try {
            SimpleType.construct(Map.class);
        } catch(IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
        } catch(IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Integer[].class);
        } catch(IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Integer;)", e.getMessage());
        }
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(Number.class);
        SimpleType simpleType2 = simpleType1.narrow(Integer.class);
        
        assertEquals(Number.class, simpleType1.getRawClass());
        assertEquals(Integer.class, simpleType2.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Number.class);
        
        try {
            simpleType.withContentType(new SimpleType(Integer.class));
        } catch(IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Number.class);
        
        try {
            simpleType.withContentValueHandler(new Object());
        } catch(IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Number.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        
        assertFalse(simpleType.isStaticTyping());
        assertTrue(staticType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Number.class);
        
        assertNull(simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Number.class);
        
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Number.class);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();
        
        sb = simpleType.getErasedSignature(sb);
        expectedResult.append("Ljava/lang/Number;");
        
        assertEquals(expectedResult.toString(), sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType1 = new SimpleType(Number.class);
        SimpleType simpleType2 = new SimpleType(Number.class, TypeBindings.emptyBindings(), simpleType1, null, null, null, false);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult1 = new StringBuilder();
        StringBuilder expectedResult2 = new StringBuilder();
        
        sb = simpleType1.getGenericSignature(sb);
        expectedResult1.append("Ljava/lang/Number;");
        
        assertEquals(expectedResult1.toString(), sb.toString());
        
        sb = simpleType2.getGenericSignature(sb);
        expectedResult2.append("Ljava/lang/Number<").append(expectedResult1).append(">;");
        
        assertEquals(expectedResult2.toString(), sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Number.class);
        
        StringBuilder expectedResult = new StringBuilder();
        
        expectedResult.append("[simple type, class ").append(simpleType.buildCanonicalName()).append(']');
        
        assertEquals(expectedResult.toString(), simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Number.class);
        SimpleType simpleType2 = new SimpleType(Number.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
    
}