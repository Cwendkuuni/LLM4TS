package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResolvedRecursiveTypeTest {

    private Class<?> erasedType;
    private TypeBindings bindings;
    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        erasedType = String.class;
        bindings = new TypeBindings(null);
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        // assert your expectations here
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        // assert your expectations here
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
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{new JavaType(CharSequence.class)};
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(null, result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = resolvedRecursiveType.isContainerType();
        assertFalse(result);
    }

    @Test
    public void testToString() {
        String result = resolvedRecursiveType.toString();
        // assert your expectations here
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, bindings);
        resolvedRecursiveType.setReference(new JavaType(String.class));
        other.setReference(new JavaType(String.class));

        boolean result1 = resolvedRecursiveType.equals(other);
        assertTrue(result1);

        boolean result2 = resolvedRecursiveType.equals(null);
        assertFalse(result2);

        boolean result3 = resolvedRecursiveType.equals(new Object());
        assertFalse(result3);
    }
}