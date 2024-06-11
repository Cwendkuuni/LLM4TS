package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertSame(erasedType, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        assertSame(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        assertSame(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("[recursive type; [Ljava.lang.Integer;]", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("java.lang.Integer[]", sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        JavaType result = resolvedRecursiveType.withContentType(javaType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // Deprecated method
        assertThrows(UnsupportedOperationException.class, ()-> {
            Class<Integer> erasedType = Integer.class;
            TypeBindings bindings = new TypeBindings();
            ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
            Class<?> subclass = Integer[].class;
            resolvedRecursiveType._narrow(subclass);
        });
    }

    @Test
    public void testRefine() {
        assertNull(new ResolvedRecursiveType(Integer.class, new TypeBindings()).refine(Integer.class, new TypeBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new ResolvedRecursiveType(Integer.class, new TypeBindings()).isContainerType());
    }

    @Test
    public void testToString() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        // [databind#1301]: Typically resolves to a loop so short-cut
        //   and only include type-erased class
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType.setReference(javaType);
        String result = resolvedRecursiveType.toString();
        assertEquals("[recursive type; java.lang.Integer[]]", result);
    }

    @Test
    public void testEquals() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType1.setReference(javaType);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        JavaType javaType2 = new SimpleModule().addTypeHandler(Integer.class, new IntHandler()).build().getTypeFactory()
                .constructType(JavaType.Builder.genericType(Integer[].class).build());
        resolvedRecursiveType1.setReference(javaType2);
        resolvedRecursiveType2.setReference(javaType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}