package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);

        type.setReference(ref);
        JavaType expected = ref;
        assertNotNull(type.getSelfReferencedType());
        assertEquals(expected, type.getSelfReferencedType());

        try {
            type.setReference(ref);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        JavaType expected = ref;
        assertNotNull(type.getSelfReferencedType());
        assertEquals(expected, type.getSelfReferencedType());

        type = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(null, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        StringBuilder expected = new StringBuilder("[recursive type; java.lang.String]");
        StringBuilder sb = new StringBuilder(40);
        JavaType result = type.getGenericSignature(sb);
        assertEquals(expected, sb);
        assertEquals(result, type);

        type = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(null, type.getGenericSignature(sb));
        assertEquals(sb, type.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder sb = new StringBuilder(40);
        JavaType result = type.getErasedSignature(sb);
        assertEquals(expected, sb);
        assertEquals(result, type);

        type = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(null, type.getErasedSignature(sb));
        assertEquals(sb, type.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new SimpleType(String.class);

        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = String.class;

        JavaType result = type._narrow(subclass);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = List.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new ClassType(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Collection.class), new SimpleType(Iterable.class) };

        JavaType result = type.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        StringBuilder expected = new StringBuilder("[recursive type; java.lang.String]");
        assertEquals(expected.toString(), type.toString());

        type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder("[recursive type; UNRESOLVED]");
        assertEquals(sb.toString(), type.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new SimpleType(String.class);
        type1.setReference(ref1);

        Class<?> erasedType2 = Object.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new SimpleType(String.class);
        type2.setReference(ref2);

        assertEquals(type1, type2);

        type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        assertFalse(type1.equals(type2));
    }
}