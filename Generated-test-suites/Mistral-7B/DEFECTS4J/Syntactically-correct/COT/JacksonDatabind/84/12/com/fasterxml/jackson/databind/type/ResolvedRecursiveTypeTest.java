package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertSame(erasedType, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref1);
        JavaType ref2 = new SimpleType(Float.class);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder(100);
        resolvedRecursiveType.getGenericSignature(sb);
        assertNull(sb.toString()); // getGenericSignature should not modify sb

        sb = new StringBuilder(100);
        JavaType listType = new SimpleType(new TypeReference<List<String>>(){}.getType());
        JavaType listWithGenericType = new ParameterizedTypeImpl(listType, new Type[] { new SimpleType(String.class) });
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(listWithGenericType);
        resolvedRecursiveType.getGenericSignature(sb);
        String expectedSignature = "<java.util.List<java.lang.String>>";
        String actualSignature = sb.toString();
        assertSame(expectedSignature, actualSignature);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder(100);
        resolvedRecursiveType.getErasedSignature(sb);
        assertNull(sb.toString()); // getErasedSignature should not modify sb

        sb = new StringBuilder(100);
        JavaType listType = new SimpleType(new TypeReference<List<String>>(){}.getType());
        JavaType listWithGenericType = new ParameterizedTypeImpl(listType, new Type[] { new SimpleType(String.class) });
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(listWithGenericType);
        resolvedRecursiveType.getErasedSignature(sb);
        String expectedSignature = "java.util.List";
        String actualSignature = sb.toString();
        assertSame(expectedSignature, actualSignature);
    }

    @Test
    public void testWithContentType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        JavaType contentType = new SimpleType(String[].class);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // deprecated, not implemented
    }

    @Test
    public void testRefine() {
        // not implemented
    }

    @Test
    public void testIsContainerType() {
        Class<String> erasedType = String.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType referencedType = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(referencedType);
        String expectedResult = "[recursive type; [java.util.List<java.lang.Integer]]";
        String actualResult = resolvedRecursiveType.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEquals() {
        Class<String> erasedType1 = String.class;
        TypeBindings bindings1 = new TypeBindings();
        JavaType referencedType1 = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType1.setReference(referencedType1);

        Class<String> erasedType2 = String.class;
        TypeBindings bindings2 = new TypeBindings();
        JavaType referencedType2 = new SimpleType(new TypeReference<List<Integer>>(){}.getType());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        resolvedRecursiveType2.setReference(referencedType2);

        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType1, bindings1);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));

        JavaType ref1 = new SimpleType(Integer.class);
        resolvedRecursiveType1.setReference(ref1);
        JavaType ref2 = new SimpleType(Float.class);
        resolvedRecursiveType2.setReference(ref2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType ref3 = ref1;
        resolvedRecursiveType1.setReference(ref3);
        assertSame(resolvedRecursiveType1, resolvedRecursiveType2);
    }
}