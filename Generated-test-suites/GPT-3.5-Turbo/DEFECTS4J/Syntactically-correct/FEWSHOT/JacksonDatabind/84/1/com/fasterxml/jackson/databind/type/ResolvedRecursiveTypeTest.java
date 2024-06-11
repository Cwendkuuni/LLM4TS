package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType recursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType();
        recursiveType = new ResolvedRecursiveType(java.lang.String.class, new TypeBindings());
        recursiveType.setReference(referencedType);
    }

    @Test
    public void testSetReference() {
        recursiveType.setReference(null);
        assertNull(recursiveType.getSelfReferencedType());
        
        recursiveType.setReference(referencedType);
        assertEquals(referencedType,recursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();
        
        referencedType.getGenericSignature(expectedResult);
        
        recursiveType.getGenericSignature(sb);
        
        assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();
        
        referencedType.getErasedSignature(expectedResult);
        
        recursiveType.getErasedSignature(sb);
        
        assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        assertTrue(recursiveType.withContentType(new JavaType()) == recursiveType);
    }

    @Test
    public void testWithTypeHandler() {
        assertTrue(recursiveType.withTypeHandler(new Object()) == recursiveType);
    }

    @Test
    public void testWithContentTypeHandler() {
        assertTrue(recursiveType.withContentTypeHandler(new Object()) == recursiveType);
    }

    @Test
    public void testWithValueHandler() {
        assertTrue(recursiveType.withValueHandler(new Object()) == recursiveType);
    }

    @Test
    public void testWithContentValueHandler() {
        assertTrue(recursiveType.withContentValueHandler(new Object()) == recursiveType);
    }

    @Test
    public void testWithStaticTyping() {
        assertTrue(recursiveType.withStaticTyping() == recursiveType);
    }

    @Test
    public void test_narrow() {
        assertTrue(recursiveType._narrow(java.lang.String.class) == recursiveType);
    }

    @Test
    public void testRefine() {
        assertNull(recursiveType.refine(java.lang.String.class, new TypeBindings(), new JavaType(), new JavaType[] {}));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        String expectedResult = "[recursive type; java.lang.String]";
        
        assertEquals(expectedResult, recursiveType.toString());
    }

    @Test
    public void testEquals() {
        assertFalse(recursiveType.equals(null));
        
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(java.lang.StringBuilder.class, new TypeBindings());
        assertFalse(recursiveType.equals(otherType));
        
        otherType.setReference(referencedType);
        assertTrue(recursiveType.equals(otherType));
    }
}