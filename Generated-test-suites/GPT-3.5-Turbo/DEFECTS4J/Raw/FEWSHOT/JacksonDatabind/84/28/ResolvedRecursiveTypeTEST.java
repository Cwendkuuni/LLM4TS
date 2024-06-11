package com.fasterxml.jackson.databind.type;
    
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(String.class, type.getRawClass());
        assertEquals(new TypeBindings(), type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(0, type.getModifiers());
        assertNull(type.getNarrowedSubtype());
        assertNull(type.getValueHandler());
        assertFalse(type.isContainerType());
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
        type.setReference(new ResolvedRecursiveType(Integer.class, new TypeBindings()));
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        assertEquals(type, type.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type._narrow(Integer.class));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.refine(String.class, new TypeBindings(), null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type1, type2);
        assertEquals(type1.hashCode(), type2.hashCode());
    }
}