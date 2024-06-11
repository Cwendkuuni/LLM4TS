package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType javaType1 = new JavaType(Object.class);
        JavaType javaType2 = new JavaType(String.class);

        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        assertNull(resolvedRecursiveType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testGetErasedSignature() {
        assertNull(resolvedRecursiveType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testWithContentType() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(new JavaType(String.class)));
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
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(String.class));
    }

    @Test
    public void testRefine() {
        assertNull(resolvedRecursiveType.refine(String.class, new TypeBindings(), new JavaType(Object.class), new JavaType[] {}));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringUnresolved() {
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testToStringResolved() {
        JavaType javaType = new JavaType(String.class);
        resolvedRecursiveType.setReference(javaType);

        assertEquals("[recursive type; java.lang.String]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        other.setReference(new JavaType(String.class));

        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsSameReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        other.setReference(new JavaType(Object.class));

        assertTrue(resolvedRecursiveType.equals(other));
    }
}