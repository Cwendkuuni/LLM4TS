package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test cases for the ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetAndGetReference() {
        Class<ResolvedRecursiveType> type1 = new Class<ResolvedRecursiveType>() {};
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(type1, null);
        JavaType ref1 = new ClassType(List.class);

        instance1.setReference(ref1);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(type1, null);
        assertNull(instance2.getSelfReferencedType());

        instance2.setReference(instance1);
        JavaType ref2 = new ClassType(Map.class);
        instance1.setReference(ref2);

        assertNotNull(instance1.getSelfReferencedType());
        assertNotNull(instance2.getSelfReferencedType());
        assertSame(ref2, instance2.getSelfReferencedType());
        assertSame(ref2, instance1.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> type = new Class<ResolvedRecursiveType>() {};
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);
        JavaType ref1 = new ClassType(String[].class);

        instance.setReference(ref1);
        instance.setReference(new ClassType(Integer[].class));
    }

    @Test
    public void testGetSignatures() {
        Class<ResolvedRecursiveType> type = new Class<ResolvedRecursiveType>() {};
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);
        JavaType ref = new ClassType(String[].class);
        instance.setReference(ref);

        StringBuilder stringBuilder = new StringBuilder();

        assertSame(ref, instance.getSelfReferencedType());
        instance.getGenericSignature(stringBuilder);
        instance.getErasedSignature(stringBuilder);

        String expected = "[recursive type; [Ljava.lang.String;]";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void testEquality() {
        Class<ResolvedRecursiveType> type1 = new Class<ResolvedRecursiveType>() {};
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(type1, null);
        JavaType ref1 = new ClassType(List.class);

        instance1.setReference(ref1);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(type1, null);
        assertFalse(instance1.equals(instance2));

        instance2.setReference(ref1);
        assertTrue(instance1.equals(instance2));

        JavaType ref2 = new ClassType(Map.class);
        instance1.setReference(ref2);
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> type = new Class<ResolvedRecursiveType>() {};
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);
        JavaType ref = new ClassType(String[].class);
        instance.setReference(ref);

        String expected = "[recursive type; [Ljava.lang.String;]";
        assertEquals(expected, instance.toString());

        instance.setReference(null);
        assertEquals("{[recursive type; UNRESOLVED]}", instance.toString());
    }
}