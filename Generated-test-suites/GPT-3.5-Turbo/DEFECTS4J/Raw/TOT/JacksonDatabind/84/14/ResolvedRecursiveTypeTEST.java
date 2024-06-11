package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        JavaType newReferencedType = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(newReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        StringBuilder sb = new StringBuilder();
        StringBuilder signature = resolvedRecursiveType.getGenericSignature(sb);

        assertEquals(referencedType.getGenericSignature(sb), signature);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        StringBuilder sb = new StringBuilder();
        StringBuilder signature = resolvedRecursiveType.getErasedSignature(sb);

        assertEquals(referencedType.getErasedSignature(sb), signature);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType contentType = new JavaType(String.class);

        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());

        JavaType result = resolvedRecursiveType.withStaticTyping();

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> subclass = Object.class;

        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];

        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        String expectedString = "[recursive type; java.lang.Object]";
        String result = resolvedRecursiveType.toString();

        assertEquals(expectedString, result);
    }

    @Test
    public void testEquals_SameObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEquals_NullObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);

        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEquals_UnresolvedReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());

        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(Object.class, new TypeBindings())));
    }

    @Test
    public void testEquals_DifferentSelfReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType1 = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType1);

        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType2 = new JavaType(String.class);
        otherType.setReference(referencedType2);

        assertFalse(resolvedRecursiveType.equals(otherType));
    }

    @Test
    public void testEquals_SameSelfReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType1 = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType1);

        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType2 = new JavaType(Object.class);
        otherType.setReference(referencedType2);

        assertTrue(resolvedRecursiveType.equals(otherType));
    }

}