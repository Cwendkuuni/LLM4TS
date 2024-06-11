package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(Integer.class);
        type1.setReference(ref);

        assertEquals(ref, type1.getSelfReferencedType());
        assertNotSame(type1, ref);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        JavaType ref1 = new SimpleType(Integer.class);
        type1.setReference(ref1);

        JavaType ref2 = new SimpleType(Long.class);
        type1.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(Integer.class);
        type1.setReference(ref);

        assertEquals(ref, type1.getSelfReferencedType());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);

        assertNotSame(type1, type2);

        JavaType ref1 = new SimpleType(Integer.class);
        JavaType ref2 = new SimpleType(Integer.class);

        type1.setReference(ref1);
        type2.setReference(ref2);

        assertSame(type1, type2);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        JavaType ref = new SimpleType(Integer[].class);
        type.setReference(ref);

        assertEquals("[recursive type; [" + ref.getRawClass().getName() + "]]", type.toString());
    }

    @Test
    public void testEqualsWithNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
        assertFalse(type.equals(null));
    }
}