package com.yourpackagename.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Tests for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        assertSame(erasedType, resolvedType.getRawClass());
        assertSame(bindings, resolvedType.getTypeBindings());
        assertNull(resolvedType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new SimpleType(String[].class);
        resolvedType.setReference(ref);
        assertSame(ref, resolvedType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceIllegalStateException() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new SimpleType(String.class);
        resolvedType.setReference(ref); // This should fail as _referencedType is already set
        resolvedType.setReference(new SimpleType(String[].class));
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String[].class);
        resolvedType.setReference(ref);
        assertSame(ref, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;"); // String[]
        JavaType ref = new SimpleType(String[].class);
        resolvedType.setReference(ref);
        StringBuilder actual = new StringBuilder();
        resolvedType.getGenericSignature(actual);
        assertSame(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;"); // String
        JavaType ref = new SimpleType(String.class);
        resolvedType.setReference(ref);
        StringBuilder actual = new StringBuilder();
        resolvedType.getErasedSignature(actual);
        assertSame(expected, actual);
    }

    @Test
    public void testWithContentType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new SimpleType(Integer.TYPE);
        ResolvedRecursiveType result = resolvedType.withContentType(contentType);
        assertSame(resolvedType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedType.withTypeHandler(handler);
        assertSame(resolvedType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedType.withContentTypeHandler(handler);
        assertSame(resolvedType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedType.withValueHandler(handler);
        assertSame(resolvedType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedType.withContentValueHandler(handler);
        assertSame(resolvedType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType result = resolvedType.withStaticTyping();
        assertSame(resolvedType, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = StringBuffer.class;
        ResolvedRecursiveType result = resolvedType._narrow(subclass);
    }

    @Test
    public void testRefine() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Object.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        ResolvedRecursiveType result = resolvedType.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String[].class);
        resolvedType.setReference(ref);
        String expected = "[recursive type; [Ljava.lang.String;]";
        String actual = resolvedType.toString();
        assertSame(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> erasedType1 = String.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new SimpleType(String[].class);
        resolvedType1.setReference(ref1);

        Class<String> erasedType2 = String.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new SimpleType(String[].class);
        resolvedType2.setReference(ref2);

        assertFalse(resolvedType1.equals(resolvedType2));

        ResolvedRecursiveType resolvedType3 = new ResolvedRecursiveType(erasedType1, bindings1);
        assertTrue(resolvedType1.equals(resolvedType3));
    }

    @Test
    public void testEqualsWithNull() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedType.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        assertTrue(resolvedType.equals(resolvedType));
    }
}