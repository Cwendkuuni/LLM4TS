package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = mock(JavaType.class);
        type.setReference(ref);

        JavaType expected = ref;
        assertSame(expected, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = mock(JavaType.class);
        type.setReference(ref);

        JavaType ref2 = mock(JavaType.class);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        JavaType expected = ref;
        assertSame(expected, type.getSelfReferencedType());
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
        JavaType referencedType = mock(JavaType.class);
        TypeBindings bindings = mock(TypeBindings.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(referencedType);

        String expected = "[recursive type; [class java.lang.Object]";
        String result = type.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        TypeBindings bindings1 = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        type1.setReference(ref1);

        Class<?> erasedType2 = Object.class;
        TypeBindings bindings2 = mock(TypeBindings.class);
        JavaType ref2 = mock(JavaType.class);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));

        type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        type1.setReference(ref1);
        type2.setReference(ref1);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsNull() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

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

    @Test
    public void testHashCode() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        type1.setReference(ref);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        type2.setReference(ref);

        assertEquals(type1.hashCode(), type2.hashCode());
    }
}