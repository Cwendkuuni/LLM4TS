package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructor1() {
        SimpleType type = new SimpleType(LinkedList.class);
        assertEquals(LinkedList.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }
    
    @Test
    public void testConstructor2() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class);
        SimpleType superClass = new SimpleType(Object.class);
        LinkedList<JavaType> interfaces = new LinkedList<>();
        SimpleType type = new SimpleType(LinkedList.class, bindings, superClass, interfaces.toArray(new JavaType[0]));
        assertEquals(LinkedList.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertArrayEquals(interfaces.toArray(new JavaType[0]), type.getInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }
    
    @Test
    public void testConstructor3() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType type = new SimpleType(LinkedList.class, null, null, null, valueHandler, typeHandler, true);
        assertEquals(LinkedList.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(valueHandler, type.getValueHandler());
        assertEquals(typeHandler, type.getTypeHandler());
        assertTrue(type.isStatic());
    }
    
    @Test
    public void testConstructor4() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class);
        SimpleType superClass = new SimpleType(Object.class);
        LinkedList<JavaType> interfaces = new LinkedList<>();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType type = new SimpleType(LinkedList.class, bindings, superClass, interfaces.toArray(new JavaType[0]), valueHandler, typeHandler, true);
        assertEquals(LinkedList.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertArrayEquals(interfaces.toArray(new JavaType[0]), type.getInterfaces());
        assertEquals(valueHandler, type.getValueHandler());
        assertEquals(typeHandler, type.getTypeHandler());
        assertTrue(type.isStatic());
    }
    
    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(LinkedList.class);
        SimpleType narrowedType = (SimpleType) baseType._narrow(ArrayList.class);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(baseType.getBindings(), narrowedType.getBindings());
        assertEquals(baseType.getSuperClass(), narrowedType.getSuperClass());
        assertArrayEquals(baseType.getInterfaces(), narrowedType.getInterfaces());
        assertEquals(baseType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(baseType.isStatic(), narrowedType.isStatic());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = new SimpleType(LinkedList.class);
        type.withContentType(new SimpleType(String.class));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(LinkedList.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler1 = new Object();
        Object valueHandler2 = new Object();
        SimpleType type = new SimpleType(LinkedList.class, null, null, null, valueHandler1, null, false);
        SimpleType newType = type.withValueHandler(valueHandler2);
        assertEquals(type.getRawClass(), newType.getRawClass());
        assertEquals(type.getBindings(), newType.getBindings());
        assertEquals(type.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(type.getInterfaces(), newType.getInterfaces());
        assertEquals(valueHandler2, newType.getValueHandler());
        assertEquals(type.getTypeHandler(), newType.getTypeHandler());
        assertEquals(type.isStatic(), newType.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(LinkedList.class);
        try {
            type.withContentValueHandler(new Object());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(LinkedList.class);
        SimpleType newType = type.withStaticTyping();
        assertEquals(type.getRawClass(), newType.getRawClass());
        assertEquals(type.getBindings(), newType.getBindings());
        assertEquals(type.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(type.getInterfaces(), newType.getInterfaces());
        assertEquals(type.getValueHandler(), newType.getValueHandler());
        assertEquals(type.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(LinkedList.class);
        JavaType refinedType = type.refine(ArrayList.class, null, null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(LinkedList.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(LinkedList.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/util/LinkedList;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(LinkedList.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/util/LinkedList;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = new SimpleType(LinkedList.class);
        assertEquals("[simple type, class java.util.LinkedList]", type.toString());
    }
    
    @Test
    public void testEquals() {
        TypeBindings bindings1 = new TypeBindings();
        bindings1.addBinding(String.class);
        TypeBindings bindings2 = new TypeBindings();
        bindings2.addBinding(Integer.class);
        
        SimpleType type1 = new SimpleType(LinkedList.class, bindings1, null, null);
        SimpleType type2 = new SimpleType(ArrayList.class, bindings1, null, null);
        SimpleType type3 = new SimpleType(LinkedList.class, bindings1, null, null);
        SimpleType type4 = new SimpleType(LinkedList.class, bindings2, null, null);
        
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type2));
        assertTrue(type1.equals(type3));
        assertFalse(type1.equals(type4));
    }
}