package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testGetReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getReferencedType());

        JavaType refType = new JavaType(null, null, null);
        type.setReference(refType);
        assertEquals(refType, type.getReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedSb = new StringBuilder();
        
        type.getReferencedType().getGenericSignature(sb);
        type.getReferencedType().getGenericSignature(expectedSb);
        
        assertEquals(expectedSb.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedSb = new StringBuilder();
        
        type.getReferencedType().getErasedSignature(sb);
        type.getReferencedType().getErasedSignature(expectedSb);
        
        assertEquals(expectedSb.toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaType(null, null, null);
        
        assertEquals(type, type.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type._narrow(null));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.refine(null, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, type.toString());

        JavaType refType = new JavaType(null, null, null);
        type.setReference(refType);
        expected = "[recursive type; " + refType.getRawClass().getName() + "]";
        assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        assertTrue(type1.equals(type2));

        type1.setReference(new JavaType(null, null, null));
        assertFalse(type1.equals(type2));

        type2.setReference(new JavaType(null, null, null));
        assertTrue(type1.equals(type2));
    }
}