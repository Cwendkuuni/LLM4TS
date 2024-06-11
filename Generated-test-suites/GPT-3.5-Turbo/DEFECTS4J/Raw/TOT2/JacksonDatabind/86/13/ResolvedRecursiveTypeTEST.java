package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Test
    public void testSetReference() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
        
        // Test exception handling
        try {
            resolvedRecursiveType.setReference(new SimpleType(Integer.class));
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Success
        }
    }

    @Test
    public void testGetGenericSignature() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNotNull(resolvedRecursiveType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testGetErasedSignature() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNotNull(resolvedRecursiveType.getErasedSignature(new StringBuilder()));
    }
    
    @Test
    public void testWithContentType() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new SimpleType(String.class);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    // Add test cases for the remaining methods...

}