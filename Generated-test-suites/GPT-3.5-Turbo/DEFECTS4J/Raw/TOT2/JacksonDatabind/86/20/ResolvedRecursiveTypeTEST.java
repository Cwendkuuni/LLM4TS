package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
    }

    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.setReference(new JavaType(String.class));
        
        assertEquals("Ljava/lang/String;", resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringUnresolved() {
        assertEquals("[recursive type; UNRESOLVED", resolvedRecursiveType.toString());
    }

    @Test
    public void testToStringResolved() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);

        assertEquals("[recursive type; java.lang.String", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);

        resolvedRecursiveType.setReference(ref1);
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(String.class, null);
        sameType.setReference(ref1);
        ResolvedRecursiveType differentType = new ResolvedRecursiveType(Integer.class, null);
        differentType.setReference(ref2);

        assertTrue(resolvedRecursiveType.equals(sameType));
        assertFalse(resolvedRecursiveType.equals(differentType));
    }

    // Add more test cases for remaining methods and edge cases

}