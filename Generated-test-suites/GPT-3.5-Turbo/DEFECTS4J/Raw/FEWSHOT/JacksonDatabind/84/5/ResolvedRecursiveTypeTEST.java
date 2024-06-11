package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType();
        resolvedRecursiveType = new ResolvedRecursiveType(TypeBase.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        resolvedRecursiveType.setReference(referencedType);
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(new JavaType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(referencedType.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(referencedType.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(new JavaType()));
    }

    @Test
    public void testWithTypeHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(new Object()));
    }

    @Test
    public void testWithContentTypeHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(new Object()));
    }

    @Test
    public void testWithValueHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(new Object()));
    }

    @Test
    public void testWithContentValueHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(new Object()));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(Object.class));
    }

    @Test
    public void testRefine() {
        assertNull(resolvedRecursiveType.refine(Object.class, new TypeBindings(), new JavaType(), new JavaType[]{}));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        String expectedToString = "[recursive type; UNRESOLVED]";
        assertEquals(expectedToString, resolvedRecursiveType.toString());
        
        resolvedRecursiveType.setReference(referencedType);
        expectedToString = "[recursive type; "+referencedType.getRawClass().getName()+"]";
        assertEquals(expectedToString, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));

        resolvedRecursiveType.setReference(referencedType);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(TypeBase.class, new TypeBindings())));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings())));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(TypeBase.class, new TypeBindings()).setReference(referencedType)));
    }

}