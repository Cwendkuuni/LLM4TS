package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        type.setReference(new SimpleType(Long.class));
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertNotNull(sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertNotNull(sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType contentType = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType result = type.withContentType(contentType);
        assertSame(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withContentTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = type.withContentValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType result = type.withStaticTyping();
        assertSame(type, result);
    }

    @Test
    public void testNarrow() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = String.class;
        assertNull(type.narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Object.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new SimpleType(List.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Collection.class) };
        ResolvedRecursiveType result = type.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Integer.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new SimpleType(String.class);
        type1.setReference(ref1);

        Class<?> erasedType2 = Integer.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new SimpleType(String.class);
        type2.setReference(ref2);

        assertEquals(type1, type1);
        assertEquals(type1, type2);
        assertNotEquals(type1, new Object());
        assertNotEquals(type1, type2);
        assertNotEquals(type1, null);
        assertFalse(type1.equals("test"));
        assertFalse(type1.equals(ref1));
        assertFalse(type1.equals(ref2));
        JavaType ref3 = new SimpleType(Long.class);
        assertFalse(type1.equals(ref3));
    }
}