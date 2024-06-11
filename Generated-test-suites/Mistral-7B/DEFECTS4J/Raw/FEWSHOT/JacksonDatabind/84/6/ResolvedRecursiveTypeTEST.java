package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;
import org.mockito.Mockito;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        Class<?> erasedType = Mockito.mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType referencedType = mock(JavaType.class);

        recursiveType.setReference(referencedType);
        assertSame(referencedType, recursiveType.getSelfReferencedType());

        JavaType newReferencedType = mock(JavaType.class);
        recursiveType.setReference(newReferencedType);
        assertSame(newReferencedType, recursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceIllegalStateException() {
        Class<?> erasedType = Mockito.mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        recursiveType.setReference(ref1);
        recursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Mockito.mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = mock(JavaType.class);
        recursiveType.setReference(ref);

        assertSame(ref, recursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<?> erasedType = Mockito.mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertNull(recursiveType.getSelfReferencedType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Mockito.mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
        recursiveType.setReference(ref);

        String expected = "[recursive type; [Ljava/lang/Class;@5e75b59d]";
        String actual = recursiveType.toString();
        assertSame(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Mockito.mock(Class.class);
        TypeBindings bindings1 = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        recursiveType1.setReference(ref1);

        Class<?> erasedType2 = Mockito.mock(Class.class);
        TypeBindings bindings2 = mock(TypeBindings.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        recursiveType2.setReference(ref2);

        assertFalse(recursiveType1.equals(recursiveType2));

        Class<?> erasedType3 = Mockito.mock(Class.class);
        TypeBindings bindings3 = mock(TypeBindings.class);
        JavaType ref3 = mock(JavaType.class);
        ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(erasedType3, bindings3);
        recursiveType3.setReference(ref3);
        assertSame(recursiveType1, recursiveType3);

        assertFalse(recursiveType1.equals(null));
        assertFalse(recursiveType1.equals(""));
    }
}