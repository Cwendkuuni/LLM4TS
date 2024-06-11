package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> testType = Object.class;
        TypeBindings typeBindings = mock(TypeBindings.class);
        ResolvedRecursiveType resType = new ResolvedRecursiveType(testType, typeBindings);

        JavaType refType = mock(JavaType.class);
        resType.setReference(refType);

        JavaType expectedRefType = refType;
        assertSame(expectedRefType, resType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> testType = Object.class;
        TypeBindings typeBindings = mock(TypeBindings.class);
        ResolvedRecursiveType resType = new ResolvedRecursiveType(testType, typeBindings);

        JavaType refType1 = mock(JavaType.class);
        JavaType refType2 = mock(JavaType.class);

        resType.setReference(refType1);
        resType.setReference(refType2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> testType = Object.class;
        TypeBindings typeBindings = mock(TypeBindings.class);
        JavaType refType = mock(JavaType.class);

        ResolvedRecursiveType resType = new ResolvedRecursiveType(testType, typeBindings);
        resType.setReference(refType);

        JavaType expectedRefType = refType;
        assertSame(expectedRefType, resType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType resType = new ResolvedRecursiveType(Object.class, mock(TypeBindings.class));
        assertNull(resType.getSelfReferencedType());
    }

    @Test
    public void testToString() {
        Class<?> testType = Object.class;
        TypeBindings typeBindings = mock(TypeBindings.class);
        JavaType refType = mock(JavaType.class);

        ResolvedRecursiveType resType = new ResolvedRecursiveType(testType, typeBindings);
        resType.setReference(refType);

        String expectedString = "[recursive type; [Ljava/lang/Object;@4d16d44b]";
        String actualString = resType.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Class<?> testType1 = Object.class;
        TypeBindings typeBindings1 = mock(TypeBindings.class);
        JavaType refType1 = mock(JavaType.class);

        Class<?> testType2 = Object.class;
        TypeBindings typeBindings2 = mock(TypeBindings.class);
        JavaType refType2 = mock(JavaType.class);

        ResolvedRecursiveType resType1 = new ResolvedRecursiveType(testType1, typeBindings1);
        ResolvedRecursiveType resType2 = new ResolvedRecursiveType(testType2, typeBindings2);
        resType1.setReference(refType1);
        resType2.setReference(refType2);

        assertTrue(resType1.equals(resType2));
    }

    @Test
    public void testEqualsDifferentClasses() {
        Class<?> testType1 = Object.class;
        TypeBindings typeBindings1 = mock(TypeBindings.class);
        JavaType refType1 = mock(JavaType.class);

        Class<?> testType2 = String.class;
        TypeBindings typeBindings2 = mock(TypeBindings.class);
        JavaType refType2 = mock(JavaType.class);

        ResolvedRecursiveType resType1 = new ResolvedRecursiveType(testType1, typeBindings1);
        ResolvedRecursiveType resType2 = new ResolvedRecursiveType(testType2, typeBindings2);
        resType1.setReference(refType1);
        resType2.setReference(refType2);

        assertFalse(resType1.equals(resType2));
    }

    @Test
    public void testEqualsNull() {
        ResolvedRecursiveType resType = new ResolvedRecursiveType(Object.class, mock(TypeBindings.class));
        assertFalse(resType.equals(null));
    }
}