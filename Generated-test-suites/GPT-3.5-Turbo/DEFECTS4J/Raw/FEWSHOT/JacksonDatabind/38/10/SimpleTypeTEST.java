package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType type = new SimpleType(TestClass.class);
        
        assertEquals(TestClass.class, type.getRawClass());
        assertEquals(null, type.getBindings());
        assertEquals(null, type.getSuperClass());
        assertEquals(null, type.getInterfaces());
        assertEquals(0, type.getExtraHash());
        assertEquals(null, type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertNull(type.getContentType());
    }
    
    @Test
    public void testConstructorWithParams() {
        JavaType superClass = new SimpleType(SuperClass.class);
        JavaType[] interfaces = new JavaType[] { new SimpleType(Interface1.class), new SimpleType(Interface2.class) };
        SimpleType type = new SimpleType(TestClass.class, new TypeBindings(), superClass, interfaces);
        
        assertEquals(TestClass.class, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(2, type.getInterfaces().length);
        assertEquals(0, type.getExtraHash());
        assertEquals(null, type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertNull(type.getContentType());
    }
    
    @Test
    public void testConstructorWithHandler() {
        Object typeHandler = new Object();
        SimpleType type = new SimpleType(TestClass.class, new TypeBindings(), null, null, null, typeHandler, false);
        
        assertEquals(TestClass.class, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
        assertEquals(null, type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(0, type.getExtraHash());
        assertEquals(typeHandler, type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertNull(type.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = new SimpleType(TestClass.class);
        JavaType contentType = new SimpleType(ContentClass.class);
        SimpleType newType = (SimpleType) type.withContentType(contentType);
        
        assertEquals(TestClass.class, newType.getRawClass());
        assertEquals(contentType, newType.getContentType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(TestClass.class);
        Object contentTypeHandler = new Object();
        SimpleType newType = (SimpleType) type.withContentTypeHandler(contentTypeHandler);
        
        assertEquals(TestClass.class, newType.getRawClass());
        assertNull(newType.getContentType());
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = new SimpleType(TestClass.class);
        Object valueHandler = new Object();
        SimpleType newType = (SimpleType) type.withValueHandler(valueHandler);
        
        assertEquals(TestClass.class, newType.getRawClass());
        assertEquals(valueHandler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(TestClass.class);
        Object contentValueHandler = new Object();
        SimpleType newType = (SimpleType) type.withContentValueHandler(contentValueHandler);
        
        assertEquals(TestClass.class, newType.getRawClass());
        assertNull(newType.getContentType());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(TestClass.class);
        SimpleType newType = (SimpleType) type.withStaticTyping();
        
        assertEquals(TestClass.class, newType.getRawClass());
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(TestClass.class);
        JavaType superClass = new SimpleType(SuperClass.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Interface1.class), new SimpleType(Interface2.class) };
        SimpleType newType = (SimpleType) type.refine(TestClass.class, new TypeBindings(), superClass, superInterfaces);
        
        assertNull(newType);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder("L").append(TestClass.class.getName()).append(';');
        
        type.getErasedSignature(sb);
        
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder("L").append(TestClass.class.getName()).append(';');
        
        type.getGenericSignature(sb);
        
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = new SimpleType(TestClass.class);
        StringBuilder expected = new StringBuilder("[simple type, class ").append(TestClass.class.getName()).append(']');
        
        assertEquals(expected.toString(), type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(TestClass.class);
        SimpleType type2 = new SimpleType(TestClass.class);
        SimpleType type3 = new SimpleType(ContentClass.class);
        
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(""));
    }
    
    private static class TestClass {}
    
    private static class SuperClass {}
    
    private interface Interface1 {}
    
    private interface Interface2 {}
    
    private static class ContentClass {}
}