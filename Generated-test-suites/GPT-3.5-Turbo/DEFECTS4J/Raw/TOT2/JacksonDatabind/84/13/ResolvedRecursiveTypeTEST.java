
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(ref.getGenericSignature(sb), result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(resolvedRecursiveType2);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    // Add more test cases for other methods here...

}

