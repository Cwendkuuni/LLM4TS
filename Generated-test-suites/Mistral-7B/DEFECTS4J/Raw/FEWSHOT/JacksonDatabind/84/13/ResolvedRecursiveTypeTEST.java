package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType referencedType = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        type.setReference(referencedType);
        assertSame(referencedType, type.getSelfReferencedType());

        // check that setReference() is not called multiple times
        type.setReference(referencedType);
        assertSame(referencedType, type.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType referencedType = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(referencedType);

        assertSame(referencedType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType referencedType = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(referencedType);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        assertNull(sb.toString());

        when(referencedType.getGenericSignature(Mockito.anyStringBuilder()))
                .thenReturn("referencedTypeSignature");

        sb = new StringBuilder();
        type.getGenericSignature(sb);

        assertEquals("referencedTypeSignature", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType referencedType = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(referencedType);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);

        assertNull(sb.toString());

        when(referencedType.getErasedSignature(Mockito.anyStringBuilder()))
                .thenReturn("referencedTypeSignature");

        sb = new StringBuilder();
        type.getErasedSignature(sb);

        assertEquals("referencedTypeSignature", sb.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = String.class;
        Class<?> erasedType2 = Integer.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType referencedType = mock(JavaType.class);

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings);
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type4 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type5 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type6 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type7 = new ResolvedRecursiveType(erasedType1, bindings);
        type7.setReference(referencedType);

        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(null));
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type3));
        assertTrue(type1.equals(type4));
        assertTrue(type1.equals(type5));
        assertTrue(type1.equals(type6));
        assertFalse(type1.equals(type7));

        when(referencedType.getSelfReferencedType()).thenReturn(referencedType);
        assertTrue(type1.equals(type7));
    }
}