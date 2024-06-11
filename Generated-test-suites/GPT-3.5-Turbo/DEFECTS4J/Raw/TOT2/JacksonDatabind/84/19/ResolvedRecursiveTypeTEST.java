package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        
        String expected = "[recursive type; java.lang.String";
        String actual = resolvedRecursiveType.toString();
        
        assertEquals(expected, actual);
    }
}