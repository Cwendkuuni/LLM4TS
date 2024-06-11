package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

public class ResolvedRecursiveTypeTest {

    private JavaType createTestType(Class<?> erasedType, TypeBindings bindings) {
        return new ResolvedRecursiveType(erasedType, bindings);
    }

    @Test
    public void testDefaultConstruction() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(Object.class, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSettingReference() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = createTestType(Bar.class, bindings);
        type.setReference(refType);

        JavaType selfRefType = type.getSelfReferencedType();
        assertEquals(refType.getRawClass(), selfRefType.getRawClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSettingReferenceTwice() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = createTestType(Bar.class, bindings);
        type.setReference(refType);

        try {
            type.setReference(refType);
            fail("IllegalStateException expected but not thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[recursive type; ");
        expected.append(Bar.class.getName()).append("]");

        StringBuilder actual = new StringBuilder();
        type.getGenericSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[recursive type; ");
        expected.append(Bar.class.getName().replaceAll("\\.", "").replaceAll("$", "").replaceAll("[", "").replaceAll("]", ""));
        expected.append("]");

        StringBuilder actual = new StringBuilder();
        type.getErasedSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType contentType = createTestType(String.class, bindings);

        JavaType result = type.withContentType(contentType);

        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();

        JavaType result = type.withTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();

        JavaType result = type.withContentTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();

        JavaType result = type.withValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();

        JavaType result = type.withContentValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType result = type.withStaticTyping();

        assertEquals(type, result);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Foo.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);

        Class<?> erasedType2 = Foo.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType1, bindings1);

        ResolvedRecursiveType type4 = new ResolvedRecursiveType(Bar.class, bindings1);

        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type3));
        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(type4));
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[recursive type; ");
        expected.append(Bar.class.getName()).append("]");

        StringBuilder actual = new StringBuilder();
        actual.append(type.toString());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Foo.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(type.isContainerType());
    }

    private static class Foo {
        // empty class
    }

    private static class Bar {
        // empty class
    }
}