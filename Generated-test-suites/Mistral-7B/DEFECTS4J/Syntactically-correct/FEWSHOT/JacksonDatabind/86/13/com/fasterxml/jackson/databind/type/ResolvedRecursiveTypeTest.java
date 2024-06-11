package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testEmptyConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(refType, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);
        type.setReference(new ClassType(Integer.class));
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);
        assertEquals(refType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder(50);
        type.getGenericSignature(sb);
        assertEquals("[recursive type; " + refType.getRawClass().getName() + "]", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder(50);
        type.getErasedSignature(sb);
        assertEquals("[recursive type; " + refType.getRawClass().getName() + "]", sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        JavaType contentType = new ClassType(Integer.class);
        JavaType result = type.withContentType(contentType);
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        JavaType result = type.withStaticTyping();
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test
    @Deprecated
    public void testNarrow() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        Class<?> subclass = Integer.class;
        JavaType result = type._narrow(subclass);
        assertNotNull(result);
        assertEquals(type.getClass(), result.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefine() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        boolean result = type.isContainerType();
        assertFalse(result);
    }

    @Test
    public void testToString() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(refType);

        assertEquals("[recursive type; " + refType.getRawClass().getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        type1.setReference(refType);

        JavaType refType2 = new ClassType(Integer.class);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        type2.setReference(refType2);

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);

        assertFalse(type1.equals(null));
        assertFalse(type1.equals(""));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertTrue(type1.equals(type1));
    }
}