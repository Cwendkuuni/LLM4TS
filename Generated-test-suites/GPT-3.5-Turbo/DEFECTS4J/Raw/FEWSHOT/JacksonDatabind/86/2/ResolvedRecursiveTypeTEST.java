
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        assertEquals(Object.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(0, type.getModifiers());
        assertNull(type.getNarrowedType());
        assertNull(type.getKeyType());
        assertFalse(type.hasContentType());
        assertFalse(type.hasHandlers());
    }
    
    @Test
    public void testSetReference() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        JavaType reference = new JavaType();
        type.setReference(reference);
        
        assertEquals(reference, type.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        JavaType reference1 = new JavaType();
        JavaType reference2 = new JavaType();
        
        type.setReference(reference1);
        type.setReference(reference2);
    }
    
    @Test
    public void testGetGenericSignature() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType reference = new JavaType();
        reference.setGenericSignature(expected);
        type.setReference(reference);
        
        assertEquals(expected, type.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType reference = new JavaType();
        reference.setErasedSignature(expected);
        type.setReference(reference);
        
        assertEquals(expected, type.getErasedSignature(sb));
    }
    
    @Test
    public void testWithTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        Object handler = new Object();
        
        assertEquals(type, type.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        Object handler = new Object();
        
        assertEquals(type, type.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        Object handler = new Object();
        
        assertEquals(type, type.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        Object handler = new Object();
        
        assertEquals(type, type.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        Class<?> subclass = Object.class;
        
        assertEquals(type, type._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        Class<?> rawType = Object.class;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        assertNull(type.refine(rawType, null, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, bindings);
        
        String expected = "[recursive type; UNRESOLVED]";
        
        assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, bindings);
        
        JavaType reference = new JavaType();
        type1.setReference(reference);
        type2.setReference(reference);
        
        assertTrue(type1.equals(type2));
    }
    
    @Test
    public void testNotEquals() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, bindings);
        
        JavaType reference1 = new JavaType();
        JavaType reference2 = new JavaType();
        type1.setReference(reference1);
        type2.setReference(reference2);
        
        assertFalse(type1.equals(type2));
    }
}
