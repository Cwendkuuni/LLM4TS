package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = mock(JavaType.class);

        type.setReference(ref);
        assertSame(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.getSelfReferencedType());

        JavaType ref = mock(JavaType.class);
        type.setReference(ref);
        assertSame(ref, type.getSelfReferencedType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
        StringBuilder actual = type.toString();
        assertSame(expected, actual);

        JavaType ref = mock(JavaType.class);
        type.setReference(ref);
        expected = new StringBuilder("[recursive type; " + ref.getRawClass().getName() + "]");
        actual = type.toString();
        assertSame(expected, actual);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type1.equals(type2));

        JavaType ref = mock(JavaType.class);
        type1.setReference(ref);
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
        type3.setReference(ref);
        assertTrue(type1.equals(type3));
    }
}