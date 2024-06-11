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
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        type.setReference(ref);

        JavaType expectedRef = ref;
        when(type.getSelfReferencedType()).thenReturn(expectedRef);
        assertSame(expectedRef, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        JavaType expectedRef = ref;
        when(type.getSelfReferencedType()).thenReturn(expectedRef);
        assertSame(expectedRef, type.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        String expectedString = "[recursive type; " + ref.getRawClass().getName() + "]";
        when(type.toString()).thenReturn(expectedString);
        String result = type.toString();
        assertEquals(expectedString, result);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        Class<?> erasedType2 = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings);
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));

        JavaType sameRef = ref1;
        type2.setReference(sameRef);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsNull() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        assertFalse(type.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        assertTrue(type.equals(type));
    }
}