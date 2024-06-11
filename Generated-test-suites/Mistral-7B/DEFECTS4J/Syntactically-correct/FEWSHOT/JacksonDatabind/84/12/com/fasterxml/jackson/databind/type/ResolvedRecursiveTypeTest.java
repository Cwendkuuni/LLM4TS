package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = mock(JavaType.class);

        selfType.setReference(refType);

        assertSame(refType, selfType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = mock(JavaType.class);

        selfType.setReference(refType);
        selfType.setReference(refType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType refType = mock(JavaType.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, bindings);
        selfType.setReference(refType);

        assertSame(refType, selfType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<?> erasedType = mock(Class.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, null);

        assertNull(selfType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType refType = mock(JavaType.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, bindings);
        selfType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        selfType.getGenericSignature(sb);

        assertSame(refType.getGenericSignature(sb), sb);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType refType = mock(JavaType.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, bindings);
        selfType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        selfType.getErasedSignature(sb);

        assertSame(refType.getErasedSignature(sb), sb);
    }

    @Test
    public void testToString() {
        Class<?> erasedType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType refType = mock(JavaType.class);
        ResolvedRecursiveType selfType = new ResolvedRecursiveType(erasedType, bindings);
        selfType.setReference(refType);

        String expected = "[recursive type; " + refType.getRawClass().getName() + "]";

        String actual = selfType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = mock(Class.class);
        TypeBindings bindings1 = mock(TypeBindings.class);
        JavaType refType1 = mock(JavaType.class);
        ResolvedRecursiveType selfType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        selfType1.setReference(refType1);

        Class<?> erasedType2 = mock(Class.class);
        TypeBindings bindings2 = mock(TypeBindings.class);
        JavaType refType2 = mock(JavaType.class);
        ResolvedRecursiveType selfType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        selfType2.setReference(refType2);

        assertFalse(selfType1.equals(selfType2));

        JavaType refType3 = mock(JavaType.class);
        ResolvedRecursiveType selfType3 = new ResolvedRecursiveType(erasedType1, bindings1);
        selfType3.setReference(refType3);

        assertTrue(selfType1.equals(selfType3));

        assertFalse(selfType1.equals(null));
    }
}