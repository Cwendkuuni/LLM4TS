package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

/**
 * JUnit 4 test cases for ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(Integer.class, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertNotNull(selfReferencedType);
        assertEquals(refType.getClass(), selfReferencedType.getClass());
        assertEquals(int[].class, selfReferencedType.getRawClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType);
        resolvedRecursiveType.setReference(refType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertNotNull(selfReferencedType);
        assertEquals(refType.getClass(), selfReferencedType.getClass());
        assertEquals(int[].class, selfReferencedType.getRawClass());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        StringBuilder expectedSignature = new StringBuilder("[Ljava.lang.Class;")
                .append("<T extends java.lang.Object; Ljava.lang.Class<T>;>").append(";")
                .append("[recursive type; ")
                .append("[Ljava.lang.Class<T>;").append("]");
        StringBuilder actualSignature = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actualSignature);
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        StringBuilder expectedSignature = new StringBuilder("[Ljava.lang.Class;");
        StringBuilder actualSignature = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(actualSignature);
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testWithContentType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        JavaType contentType = new SimpleType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(null);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(null);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(null);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(null);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();
        assertNotNull(result);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), result.getSelfReferencedType());
    }

    @Test
    public void testNarrow() {
        fail("Method not implemented"); // _narrow method is deprecated
    }

    @Test
    public void testRefine() {
        fail("Method not implemented"); // refine method returns null
    }

    @Test
    public void testIsContainerType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(refType);
        String expectedToString = "[recursive type; [Ljava.lang.Class<java.lang.Integer>;]";
        String actualToString = resolvedRecursiveType.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        Class<Integer> erasedType1 = Integer.class;
        TypeBindings bindings1 = new TypeBindings();
        JavaType refType1 = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType1.setReference(refType1);

        Class<Integer> erasedType2 = Integer.class;
        TypeBindings bindings2 = new TypeBindings();
        JavaType refType2 = new SimpleType(int[].class);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        resolvedRecursiveType2.setReference(refType2);

        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType sameSelfReferencedType = refType1;
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType3.setReference(sameSelfReferencedType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}