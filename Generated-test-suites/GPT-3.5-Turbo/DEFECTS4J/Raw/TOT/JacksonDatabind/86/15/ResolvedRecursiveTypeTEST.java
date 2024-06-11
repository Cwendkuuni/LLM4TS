package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_whenAlreadySet() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        assertTrue(result == sb);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        assertTrue(result == sb);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        
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
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = Object.class;

        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString_whenReferencedTypeIsNull() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);

        String result = resolvedRecursiveType.toString();

        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testToString_whenReferencedTypeIsNotNull() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new JavaType(Integer.class);

        resolvedRecursiveType.setReference(referencedType);

        String result = resolvedRecursiveType.toString();

        assertEquals("[recursive type; java.lang.Integer]", result);
    }

    @Test
    public void testEquals_sameInstance() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);

        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEquals_null() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);

        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object obj = new Object();

        assertFalse(resolvedRecursiveType.equals(obj));
    }

    @Test
    public void testEquals_whenReferencedTypeIsNull() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);

        assertEquals(resolvedRecursiveType1, resolvedRecursiveType2);
    }

    @Test
    public void testEquals_whenReferencedTypeIsDifferent() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType1 = new JavaType(Integer.class);
        JavaType referencedType2 = new JavaType(String.class);
        
        resolvedRecursiveType1.setReference(referencedType1);
        resolvedRecursiveType2.setReference(referencedType2);

        assertNotEquals(resolvedRecursiveType1, resolvedRecursiveType2);
    }

    @Test
    public void testEquals_whenReferencedTypeIsSame() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new JavaType(Integer.class);
        
        resolvedRecursiveType1.setReference(referencedType);
        resolvedRecursiveType2.setReference(referencedType);

        assertEquals(resolvedRecursiveType1, resolvedRecursiveType2);
    }

}