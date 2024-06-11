
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        assertEquals(erasedType, resolvedRecursiveType.getErasedType());
        assertNull(resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getKeyType());
        assertNull(resolvedRecursiveType.getContentType());
        assertNull(resolvedRecursiveType.getValueHandler());
        assertNull(resolvedRecursiveType.getKeyHandler());
        assertFalse(resolvedRecursiveType.isContainerType());
        assertFalse(resolvedRecursiveType.isEnumType());
        assertFalse(resolvedRecursiveType.isInterface());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference() {
        JavaType ref = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
        
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        
        JavaType ref = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        
        JavaType ref = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        
        String result = resolvedRecursiveType.toString();
        
        assertTrue(result.contains("UNRESOLVED"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertTrue(resolvedRecursiveType2.equals(resolvedRecursiveType1));
    }
}
