package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testEquals() {
        JavaType refType1 = new ResolvedRecursiveType(String.class, null);
        JavaType refType2 = new ResolvedRecursiveType(String.class, null);

        assertEquals(refType1, refType1);
        assertEquals(refType1, refType2);
        assertNotEquals(refType1, new Object());

        refType1.setReference(new SimpleType(String.class));
        assertNotEquals(refType1, refType2);

        JavaType refType3 = new ResolvedRecursiveType(String.class, null);
        refType3.setReference(refType1);
        assertEquals(refType1, refType3);
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType refType = new ResolvedRecursiveType(String.class, null);
        assertNull(refType.getSelfReferencedType());

        JavaType referencedType = new SimpleType(String.class);
        refType.setReference(referencedType);
        assertSame(referencedType, refType.getSelfReferencedType());
    }

    @Test
    public void testToString() {
        JavaType refType = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED]", refType.toString());

        JavaType referencedType = new SimpleType(String.class);
        refType.setReference(referencedType);
        assertEquals("[recursive type; " + referencedType.getRawClass().getName() + "]", refType.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType refType = new ResolvedRecursiveType(String.class, null);
        JavaType referencedType = new SimpleType(String.class);
        refType.setReference(referencedType);
        refType.setReference(new SimpleType(Integer.class));
    }
}