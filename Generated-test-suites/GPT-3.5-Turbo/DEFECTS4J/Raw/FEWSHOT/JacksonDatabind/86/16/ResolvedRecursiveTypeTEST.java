package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(TestClass.class, new TypeBindings());
    }

    @Test
    public void setReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void getGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(ref, result);
    }

    @Test
    public void getErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(ref, result);
    }

    @Test
    public void withContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void withTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void withContentTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void withValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void withContentValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void withStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void _narrow() {
        Class<?> subclass = TestClass.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void refine() {
        Class<?> rawType = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = {};
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void isContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals("[recursive type; " + TestClass.class.getName() + "]", resolvedRecursiveType.toString());
    }

    @Test
    public void equals() {
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(TestClass.class, new TypeBindings())));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(TestClass.class, new TypeBindings(), null, null, 0, null, null, false)));
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(TestClass.class, new TypeBindings())));
        assertTrue(resolvedRecursiveType.equals(new ResolvedRecursiveType(TestClass.class, new TypeBindings(), null, null, 0, null, null, false)));
    }

    static class TestClass {
    }
}