package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(0, simpleType.containedTypeCount());
    }
    
    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = new TypeBindings();
        bindings.addType(String.class);
        
        SimpleType simpleType = new SimpleType(String.class, bindings, null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(0, simpleType.containedTypeCount());
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType narrowedType = simpleType._narrow(String.class);
        
        assertEquals(String.class, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(simpleType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(new JavaType() {});
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType modifiedType = simpleType.withContentTypeHandler(new Object());
        assertEquals(simpleType, modifiedType);
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType modifiedType = simpleType.withValueHandler(new Object());
        assertEquals(simpleType, modifiedType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertEquals(simpleType, modifiedType);
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), null, null);
        assertEquals(null, refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(false, simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/Object", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/Object;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        
        assertEquals(true, simpleType1.equals(simpleType2));
    }
}