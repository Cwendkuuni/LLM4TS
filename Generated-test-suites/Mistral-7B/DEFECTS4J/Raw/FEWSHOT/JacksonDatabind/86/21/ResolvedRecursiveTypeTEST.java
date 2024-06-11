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
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());

        JavaType ref = mock(JavaType.class);
        type.setReference(ref);

        JavaType expected = ref;
        assertSame(expected, type.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeWhenNull() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());

        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());
        JavaType ref = mock(JavaType.class);
        type.setReference(ref);

        JavaType expected = ref;
        assertSame(expected, type.getSelfReferencedType());
    }

    @Test
    public void testToStringWhenNullReferencedType() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());

        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testToStringWithReferencedType() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());
        JavaType ref = mock(JavaType.class);
        type.setReference(ref);

        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testEqualsWhenSameInstance() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, new TypeBindings());
        ResolvedRecursiveType type2 = type1;

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsWhenDifferentInstancesButSameType() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, new TypeBindings());

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsWhenDifferentInstancesAndDifferentTypes() {
        Class<?> erasedType1 = Object.class;
        Class<?> erasedType2 = String.class;
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, new TypeBindings());

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithNull() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());

        assertFalse(type.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, new TypeBindings());

        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentType() {
        Class<?> erasedType1 = Object.class;
        Class<?> erasedType2 = String.class;
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, new TypeBindings());

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithUnresolvedReference() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, new TypeBindings());
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        type1.setReference(ref1);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithSameReferenceButDifferentInstances() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, new TypeBindings());
        JavaType ref = mock(JavaType.class);
        type1.setReference(ref);
        type2.setReference(ref);

        assertTrue(type1.equals(type2));
    }
}