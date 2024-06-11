
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        
        resolvedType.setReference(ref);
        
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        resolvedType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        
        assertEquals(ref.getGenericSignature(sb).toString(), resolvedType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        resolvedType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        
        assertEquals(ref.getErasedSignature(sb).toString(), resolvedType.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new ResolvedRecursiveType(Object.class, null);
        
        assertEquals(resolvedType, resolvedType.withContentType(contentType));
    }

    // Add more test cases for the remaining methods

}

