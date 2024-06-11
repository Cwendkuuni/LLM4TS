package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType();
        resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
    }

    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        resolvedRecursiveType.setReference(referencedType);
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());

        try {
            resolvedRecursiveType.setReference(new JavaType());
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + referencedType + ", new = " + new JavaType(), e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        referencedType.setGenericSignature(sb1);
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.getGenericSignature(sb2);

        assertEquals(sb1, sb2);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        referencedType.setErasedSignature(sb1);
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.getErasedSignature(sb2);

        assertEquals(sb1, sb2);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Deprecated
    @Test
    public void test_narrow() {
        Class<?> subclass = ResolvedRecursiveType.class;

        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; UNRESOLVED");

        assertEquals(sb.toString(), resolvedRecursiveType.toString());

        resolvedRecursiveType.setReference(referencedType);

        sb = new StringBuilder(40)
                .append("[recursive type; ").append(referencedType.getRawClass().getName());

        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType sameResolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        sameResolvedRecursiveType.setReference(referencedType);

        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(sameResolvedRecursiveType));

        resolvedRecursiveType.setReference(referencedType);
        sameResolvedRecursiveType.setReference(referencedType);

        assertTrue(resolvedRecursiveType.equals(sameResolvedRecursiveType));
    }
}