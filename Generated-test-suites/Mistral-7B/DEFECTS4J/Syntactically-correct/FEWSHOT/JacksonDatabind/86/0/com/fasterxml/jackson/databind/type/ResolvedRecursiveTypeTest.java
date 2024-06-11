package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testEquals() {
        JavaType type1 = new ResolvedRecursiveType(String.class, null);
        JavaType type2 = new ResolvedRecursiveType(String.class, null);
        JavaType type3 = new ResolvedRecursiveType(List.class, null);

        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));

        ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, null);
        type1.setReference(type4);
        assertFalse(type1.equals(type2));
        assertTrue(type1.equals(type4));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = type.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder expected = new StringBuilder("[recursive type; java.lang.String]");
        StringBuilder actual = type.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetReferencedType() {
        JavaType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());

        JavaType refType = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }
        };
        type.setReference(refType);
        assertEquals(refType, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType type = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }
        };
        type.setReference(refType);
        type.setReference(refType);
    }
}