package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructorNormal() {
        JavaType type = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        assertNotNull(tested);
        assertEquals(int[].class, tested.getRawClass());
        assertNull(tested.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        tested.setReference(referencedType);
        assertNotNull(tested.getSelfReferencedType());
        assertEquals(referencedType, tested.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        tested.setReference(referencedType);
        tested.setReference(new JavaType(int.class));
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        tested.setReference(referencedType);
        assertEquals(referencedType, tested.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        StringBuilder sb = new StringBuilder(10);
        tested.getGenericSignature(sb);
        assertEquals("[Ljava.lang.Object;", sb.toString());

        tested.setReference(referencedType);
        sb.setLength(0);
        tested.getGenericSignature(sb);
        assertEquals("[L" + referencedType.getClassName() + ";", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        StringBuilder sb = new StringBuilder(10);
        tested.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());

        tested.setReference(referencedType);
        sb.setLength(0);
        tested.getErasedSignature(sb);
        assertEquals(referencedType.getErasedSignature(sb).toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        JavaType contentType = new JavaType(Double.class);
        ResolvedRecursiveType result = tested.withContentType(contentType);
        assertNotNull(result);
        assertEquals(tested, result);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        Object handler = new Object();
        ResolvedRecursiveType result = tested.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(tested, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        Object handler = new Object();
        ResolvedRecursiveType result = tested.withContentTypeHandler(handler);
        assertNotNull(result);
        assertEquals(tested, result);
    }

    @Test
    public void testWithValueHandler() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        Object handler = new Object();
        ResolvedRecursiveType result = tested.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(tested, result);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        Object handler = new Object();
        ResolvedRecursiveType result = tested.withContentValueHandler(handler);
        assertNotNull(result);
        assertEquals(tested, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        ResolvedRecursiveType result = tested.withStaticTyping();
        assertNotNull(result);
        assertEquals(tested, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        tested.narrow(Object.class);
    }

    @Test
    public void testRefine() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        Class<?> rawType = int[].class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = tested.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        assertFalse(tested.isContainerType());

        tested.setReference(referencedType);
        assertTrue(tested.isContainerType());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new JavaType(String.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(int[].class, null);
        assertEquals("[recursive type; UNRESOLVED]", tested.toString());

        tested.setReference(referencedType);
        assertEquals("[recursive type; " + referencedType.getRawClass().getName() + "]", tested.toString());
    }

    @Test
    public void testEquals() {
        JavaType referencedType1 = new JavaType(String.class);
        JavaType referencedType2 = new JavaType(String.class);
        ResolvedRecursiveType tested1 = new ResolvedRecursiveType(int[].class, null);
        ResolvedRecursiveType tested2 = new ResolvedRecursiveType(int[].class, null);
        ResolvedRecursiveType tested3 = new ResolvedRecursiveType(int[].class, null);
        tested1.setReference(referencedType1);
        tested2.setReference(referencedType2);
        assertFalse(tested1.equals(tested2));
        assertFalse(tested1.equals(tested3));
        assertFalse(tested1.equals(new Object()));
        assertTrue(tested1.equals(tested1));

        ResolvedRecursiveType tested4 = new ResolvedRecursiveType(int[].class, null);
        tested1.setReference(referencedType1);
        tested4.setReference(referencedType1);
        assertTrue(tested1.equals(tested4));
    }
}