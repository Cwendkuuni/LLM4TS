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
        SimpleType type = new SimpleType(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.hasGenericTypes());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType type = new SimpleType(Integer.class, bindings, null, null);
        assertEquals(Integer.class, type.getRawClass());
        assertTrue(type.hasGenericTypes());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstructorWithSuperClass() {
        SimpleType superType = new SimpleType(Number.class);
        SimpleType type = new SimpleType(Integer.class, null, superType, null);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.hasGenericTypes());
        assertEquals(superType, type.getSuperClass());
        assertNull(type.getInterfaces());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstructorWithInterfaces() {
        SimpleType interfaceType = new SimpleType(Cloneable.class);
        SimpleType type = new SimpleType(Integer.class, null, null, new JavaType[]{interfaceType});
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.hasGenericTypes());
        assertNull(type.getSuperClass());
        assertEquals(1, type.getInterfaces().length);
        assertEquals(interfaceType, type.getInterfaces()[0]);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        SimpleType superType = new SimpleType(Number.class);
        SimpleType interfaceType = new SimpleType(Cloneable.class);
        SimpleType type = new SimpleType(Integer.class, null, superType, new JavaType[]{interfaceType});
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.hasGenericTypes());
        assertEquals(superType, type.getSuperClass());
        assertEquals(1, type.getInterfaces().length);
        assertEquals(interfaceType, type.getInterfaces()[0]);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstructorWithExtraHash() {
        SimpleType type = new SimpleType(Integer.class, null, null, null, 10, null, null, false);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.hasGenericTypes());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(10, type.getExtraHash());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.hasGenericTypes());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testRefine() {
        SimpleType superType = new SimpleType(Number.class);
        SimpleType interfaceType = new SimpleType(Cloneable.class);
        SimpleType baseType = new SimpleType(Integer.class, null, superType, new JavaType[]{interfaceType});
        SimpleType refinedType = (SimpleType) baseType.refine(String.class, null, null, null);
        assertEquals(String.class, refinedType.getRawClass());
        assertFalse(refinedType.hasGenericTypes());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getInterfaces());
        assertFalse(refinedType.isContainerType());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = new SimpleType(Integer.class);
        try {
            type.withContentType(new SimpleType(Double.class));
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(Integer.class);
        try {
            type.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(Integer.class);
        try {
            type.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(Integer.class, null, null, null, false);
        SimpleType staticType = type.withStaticTyping();
        assertEquals(Integer.class, staticType.getRawClass());
        assertFalse(staticType.hasGenericTypes());
        assertNull(staticType.getSuperClass());
        assertNull(staticType.getInterfaces());
        assertTrue(staticType.isStaticTyping());
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType type = new SimpleType(Integer.class);
        assertEquals("java.lang.Integer", type.buildCanonicalName());
        
        TypeBindings bindings = new TypeBindings();
        bindings.addType(Integer.class, new SimpleType(String.class));
        bindings.addType(Float.class, new SimpleType(Double.class));
        SimpleType genericType = new SimpleType(List.class, bindings, null, null);
        assertEquals("java.util.List<java.lang.String,java.lang.Double>", genericType.buildCanonicalName());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType type = new SimpleType(Integer.class);
        StringBuilder signature = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Integer;", signature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType type = new SimpleType(Integer.class);
        StringBuilder signature = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Integer;", signature.toString());
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(Integer.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        SimpleType type = new SimpleType(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(Integer.class);
        SimpleType type2 = new SimpleType(Integer.class);
        SimpleType type3 = new SimpleType(String.class);
        
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}