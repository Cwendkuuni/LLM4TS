package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertNotNull(resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType reference = new ClassType(String.class);

        resolvedRecursiveType.setReference(reference);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();

        assertNotNull(selfReferencedType);
        assertEquals(reference.getRawClass(), selfReferencedType.getRawClass());

        try {
            resolvedRecursiveType.setReference(reference); // exception test
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType reference = new ClassType(String.class);
        resolvedRecursiveType.setReference(reference);

        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(reference, selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType reference = new ClassType(String.class);
        resolvedRecursiveType.setReference(reference);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/String;", sb.toString());

        sb = new StringBuilder();
        try {
            resolvedRecursiveType.getGenericSignature(null); // null test
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType reference = new ClassType(String.class);
        resolvedRecursiveType.setReference(reference);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());

        sb = new StringBuilder();
        try {
            resolvedRecursiveType.getErasedSignature(null); // null test
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType contentType = new ClassType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        resolvedRecursiveType.withContentType(contentType);
        JavaType result = resolvedRecursiveType.getInstance();
        assertNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        // not applicable as there is no implementation in ResolvedRecursiveType
    }

    @Test
    public void testWithContentTypeHandler() {
        // not applicable as there is no implementation in ResolvedRecursiveType
    }

    @Test
    public void testWithValueHandler() {
        // not applicable as there is no implementation in ResolvedRecursiveType
    }

    @Test
    public void testWithContentValueHandler() {
        // not applicable as there is no implementation in ResolvedRecursiveType
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        resolvedRecursiveType.withStaticTyping();
        JavaType result = resolvedRecursiveType.getInstance();
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        // not applicable as _narrow method is deprecated since 2.7
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Object.class;
        TypeBindings refinedBindings = new TypeBindings();
        JavaType superClass = new ClassType(String.class);
        JavaType[] superInterfaces = new JavaType[0];

        JavaType result = resolvedRecursiveType.refine(rawType, refinedBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType reference = new ClassType(String.class);
        resolvedRecursiveType.setReference(reference);

        String expected = "[recursive type; java.lang.String]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType reference1 = new ClassType(String.class);
        resolvedRecursiveType1.setReference(reference1);

        Class<?> erasedType2 = Object.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType reference2 = new ClassType(String.class);
        resolvedRecursiveType2.setReference(reference2);

        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        Class<?> erasedType3 = Object.class;
        TypeBindings bindings3 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType3, bindings3);
        JavaType reference3 = reference1;
        resolvedRecursiveType3.setReference(reference3);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}