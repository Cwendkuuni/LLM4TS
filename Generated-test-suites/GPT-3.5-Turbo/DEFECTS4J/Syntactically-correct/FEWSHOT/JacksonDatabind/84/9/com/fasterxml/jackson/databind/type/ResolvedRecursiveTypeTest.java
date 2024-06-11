package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
        // Test resetting self reference
        try {
            type.setReference(ref);
            fail("Exception not thrown");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, e.getMessage());
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals(ref.getGenericSignature(sb).toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals(ref.getErasedSignature(sb).toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaType(String.class);
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        JavaType result = type.withTypeHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        JavaType result = type.withContentTypeHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        JavaType result = type.withValueHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        JavaType result = type.withContentValueHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }
    
    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = String.class;
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        assertEquals("[recursive type; java.lang.String]", type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type2));
        JavaType ref = new JavaType(String.class);
        type1.setReference(ref);
        assertFalse(type1.equals(type2));
        type2.setReference(ref);
        assertTrue(type1.equals(type2));
    }
    
}