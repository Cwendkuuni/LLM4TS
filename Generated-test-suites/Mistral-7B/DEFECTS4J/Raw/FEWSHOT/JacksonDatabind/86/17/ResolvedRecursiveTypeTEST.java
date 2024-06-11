package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = mock(JavaType.class);

        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);

        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);

        String expected = "[recursive type; " + ref.getGenericSignature(sb).toString() + "]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);

        String expected = ref.getErasedSignature(sb).toString();
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = String.class;
        TypeBindings bindings1 = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType1.setReference(ref1);

        Class<?> erasedType2 = String.class;
        TypeBindings bindings2 = mock(TypeBindings.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        resolvedRecursiveType2.setReference(ref2);

        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));

        when(ref1.equals(ref2)).thenReturn(true);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testToString() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testToStringUnresolved() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }
}