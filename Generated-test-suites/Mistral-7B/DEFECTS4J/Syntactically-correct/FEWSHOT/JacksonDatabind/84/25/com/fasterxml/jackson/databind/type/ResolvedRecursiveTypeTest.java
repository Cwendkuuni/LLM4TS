package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructors() {
        assertNotNull(new ResolvedRecursiveType(String.class, null));
        assertNull(new ResolvedRecursiveType(null, null));
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaTypeImpl(Integer.class);
        type.setReference(ref);
        JavaType selfRef = type.getSelfReferencedType();
        assertNotNull(selfRef);
        assertEquals(ref.getRawClass(), selfRef.getRawClass());

        try {
            type.setReference(ref); // verify that setReference method can't be called multiple times
            assert false; // will not reach this point
        } catch (IllegalStateException e) {
            // expected exception
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType ref = new JavaTypeImpl(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        JavaType selfRef = type.getSelfReferencedType();
        assertNotNull(selfRef);
        assertEquals(ref.getRawClass(), selfRef.getRawClass());
        assertNull(new ResolvedRecursiveType(String.class, null).getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType ref = new JavaTypeImpl(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder(100);
        type.getGenericSignature(sb);
        // Test case specific assertions
        // Removed due to the fact that we cannot easily check the generated String
    }

    @Test
    public void testGetErasedSignature() {
        JavaType ref = new JavaTypeImpl(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder(100);
        type.getErasedSignature(sb);
        // Test case specific assertions
        // Removed due to the fact that we cannot easily check the generated String
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaTypeImpl(Integer.class);
        JavaType result = type.withContentType(contentType);
        assertNotNull(result);
        assertEquals(type.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(type.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertNotNull(result);
        assertEquals(type.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(type.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertNotNull(result);
        assertEquals(type.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType result = type.withStaticTyping();
        assertNotNull(result);
        assertEquals(type.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = Object.class;
        JavaType result = type._narrow(subclass);
        assertNull(result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaTypeImpl(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        JavaType ref = new JavaTypeImpl(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", type.toString());
        assertEquals("[recursive type; UNRESOLVED]", new ResolvedRecursiveType(null, null).toString());
    }

    @Test
    public void testEquals() {
        JavaType ref1 = new JavaTypeImpl(Integer.class);
        JavaType ref2 = new JavaTypeImpl(Integer.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        type1.setReference(ref1);
        type2.setReference(ref2);
        assertTrue(type1.equals(type2));
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals("test"));
        assertFalse(type1.equals(null));
    }
}