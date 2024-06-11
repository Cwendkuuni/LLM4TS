package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);

        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(referencedType); // should throw IllegalStateException
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertNull(resolvedRecursiveType.getGenericSignature(sb));
        assertEquals("", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertNull(resolvedRecursiveType.getErasedSignature(sb));
        assertEquals("", sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(typeHandler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(contentTypeHandler));
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(contentValueHandler));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = new Object().getClass();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = new Object().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());

        JavaType referencedType = new JavaType();
        referencedType.setRawClass(String.class);
        resolvedRecursiveType.setReference(referencedType);

        assertEquals("[recursive type; java.lang.String]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));

        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType.equals(other));

        JavaType referencedType1 = new JavaType();
        referencedType1.setRawClass(String.class);
        resolvedRecursiveType.setReference(referencedType1);

        assertFalse(resolvedRecursiveType.equals(other));

        JavaType referencedType2 = new JavaType();
        referencedType2.setRawClass(String.class);
        other.setReference(referencedType2);

        assertTrue(resolvedRecursiveType.equals(other));
    }
}