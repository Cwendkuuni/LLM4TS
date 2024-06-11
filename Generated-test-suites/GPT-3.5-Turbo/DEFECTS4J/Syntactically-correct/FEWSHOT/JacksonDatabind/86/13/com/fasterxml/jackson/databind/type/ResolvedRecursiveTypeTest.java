package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        // create ResolvedRecursiveType instance
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        // assert the instance variables are set correctly
        assertEquals(erasedType, resolvedRecursiveType._erasedType);
        assertEquals(bindings, resolvedRecursiveType._bindings);
    }

    @Test
    public void testSetReference() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // set reference
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        // assert the referenced type is set correctly
        assertEquals(ref, resolvedRecursiveType._referencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // set reference twice
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2); // should throw IllegalStateException
    }

    @Test
    public void testGetSelfReferencedType() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // set reference
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        // assert the self-referenced type is retrieved correctly
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock JavaType object
        JavaType ref = new JavaType();
        StringBuilder sb = new StringBuilder();
        ref.getGenericSignature(sb);

        // assert getGenericSignature() returns the same result as JavaType.getGenericSignature()
        assertEquals(sb.toString(), resolvedRecursiveType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetErasedSignature() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock JavaType object
        JavaType ref = new JavaType();
        StringBuilder sb = new StringBuilder();
        ref.getErasedSignature(sb);

        // assert getErasedSignature() returns the same result as JavaType.getErasedSignature()
        assertEquals(sb.toString(), resolvedRecursiveType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithContentType() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock JavaType object
        JavaType contentType = new JavaType();

        // assert withContentType() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock type handler object
        Object h = new Object();

        // assert withTypeHandler() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock content type handler object
        Object h = new Object();

        // assert withContentTypeHandler() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock value handler object
        Object h = new Object();

        // assert withValueHandler() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // create mock content value handler object
        Object h = new Object();

        // assert withContentValueHandler() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // assert withStaticTyping() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // assert _narrow() returns the same instance
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(String.class));
    }

    @Test
    public void testRefine() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // assert refine() returns null
        assertNull(resolvedRecursiveType.refine(String.class, new TypeBindings(), new JavaType(), new JavaType[] {}));
    }

    @Test
    public void testIsContainerType() {
        // create ResolvedRecursiveType instance
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        // assert isContainerType() returns false
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        // create ResolvedRecursiveType instance without reference
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType1.toString());

        // create ResolvedRecursiveType instance with reference
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new JavaType();
        resolvedRecursiveType2.setReference(ref2);
        assertEquals("[recursive type; java.lang.String]", resolvedRecursiveType2.toString());
    }

    @Test
    public void testEquals() {
        // create ResolvedRecursiveType instances with null reference
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());

        // assert equals() returns true for the same instance
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));

        // assert equals() returns true for instances with the same values
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        // create mock JavaType object
        JavaType ref = new JavaType();

        // set reference for one instance
        resolvedRecursiveType1.setReference(ref);

        // assert equals() returns false for instances with different references
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        // set reference for the other instance
        resolvedRecursiveType2.setReference(ref);

        // assert equals() returns true for instances with the same reference
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        // assert equals() returns false for instances with different classes
        assertFalse(resolvedRecursiveType1.equals(new Object()));
    }
}