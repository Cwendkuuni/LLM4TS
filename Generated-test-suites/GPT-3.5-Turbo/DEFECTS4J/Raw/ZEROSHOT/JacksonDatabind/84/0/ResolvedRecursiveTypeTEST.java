package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = String.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = resolvedRecursiveType.isContainerType();
        assertFalse(result);
    }

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
        
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());
        other.setReference(new JavaType());
        assertTrue(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEquals_SameInstance() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEquals_DifferentReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());
        other.setReference(new JavaType());
        assertFalse(resolvedRecursiveType.equals(other));
    }

}