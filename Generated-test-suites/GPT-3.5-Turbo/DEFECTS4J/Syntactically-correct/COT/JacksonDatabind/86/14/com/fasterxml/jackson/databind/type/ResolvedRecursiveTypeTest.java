
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append(ref.getGenericSignature(sb));
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append(ref.getErasedSignature(sb));
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType(String.class);
        // WithContentType method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        // WithTypeHandler method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        // WithContentTypeHandler method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        // WithValueHandler method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        // WithContentValueHandler method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        // WithStaticTyping method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = String.class;
        // _narrow method returns the object itself, so both references should be the same
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        // refine method returns null
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(ref);
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));        
    }
}
