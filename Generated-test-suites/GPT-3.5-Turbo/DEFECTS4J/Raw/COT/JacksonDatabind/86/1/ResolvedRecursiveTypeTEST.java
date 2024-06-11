package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type.setReference(ref1);
        type.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ref.setGenericSignature(sb);
        type.setReference(ref);
        assertEquals(sb, type.getGenericSignature(new StringBuilder()));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ref.setErasedSignature(sb);
        type.setReference(ref);
        assertEquals(sb, type.getErasedSignature(new StringBuilder()));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertNull(type.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        assertSame(type, type.withContentType(contentType));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertNull(type.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertNull(type.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertNull(type.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertSame(type, type.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType narrowed = type._narrow(null);
        assertSame(type, narrowed);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertNull(type.refine(null, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        assertEquals("[recursive type; UNRESOLVED]", type1.toString());
        
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertEquals("[recursive type; com.fasterxml.jackson.databind.type.ResolvedRecursiveType]", type2.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        assertTrue(type1.equals(type2));
        
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type3.setReference(ref1);
        assertFalse(type3.equals(type2));
        type2.setReference(ref2);
        assertFalse(type3.equals(type2));
        type2.setReference(ref1);
        assertTrue(type3.equals(type2));
    }
}