package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType referencedType = new ClassType(String.class);
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        selfReference.setReference(referencedType);
        JavaType actual = selfReference.getSelfReferencedType();

        assertEquals(referencedType, actual);
        assertNotSame(selfReference, actual);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        assertNull(selfReference.getSelfReferencedType());

        JavaType referencedType = new ClassType(String.class);
        selfReference.setReference(referencedType);

        JavaType actual = selfReference.getSelfReferencedType();
        assertEquals(referencedType, actual);
    }

    @Test
    public void testGetGenericSignatureAndErasedSignature() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        StringBuilder expected = new StringBuilder("[recursive type; ");
        expected.append(String.class.getName());
        expected.append("]");

        StringBuilder actualGeneric = new StringBuilder(50);
        selfReference.getGenericSignature(actualGeneric);

        assertEquals(expected.toString(), actualGeneric.toString());

        expected = new StringBuilder("[recursive type; ");
        expected.append("UNRESOLVED");
        expected.append("]");

        StringBuilder actualErased = new StringBuilder(50);
        selfReference.getErasedSignature(actualErased);

        assertEquals(expected.toString(), actualErased.toString());

        JavaType referencedType = new ClassType(String.class);
        selfReference.setReference(referencedType);

        expected = new StringBuilder("[recursive type; ");
        expected.append(String.class.getName());
        expected.append("]");

        actualGeneric = new StringBuilder(50);
        selfReference.getGenericSignature(actualGeneric);

        assertEquals(expected.toString(), actualGeneric.toString());

        expected = new StringBuilder("[recursive type; ");
        expected.append(referencedType.getRawClass().getName());
        expected.append("]");

        actualErased = new StringBuilder(50);
        selfReference.getErasedSignature(actualErased);

        assertEquals(expected.toString(), actualErased.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);
        JavaType contentType = new ClassType(Integer.class);

        JavaType actual = selfReference.withContentType(contentType);

        assertEquals(selfReference, actual);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        Object handler = new Object();

        JavaType actual = selfReference.withTypeHandler(handler);

        assertEquals(selfReference, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);
        Object handler = new Object();

        JavaType actual = selfReference.withContentTypeHandler(handler);

        assertEquals(selfReference, actual);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        Object handler = new Object();

        JavaType actual = selfReference.withValueHandler(handler);

        assertEquals(selfReference, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);
        Object handler = new Object();

        JavaType actual = selfReference.withContentValueHandler(handler);

        assertEquals(selfReference, actual);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        JavaType actual = selfReference.withStaticTyping();

        assertEquals(selfReference, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);
        JavaType referencedType = new ClassType(String.class);

        selfReference.setReference(referencedType);
        selfReference.setReference(referencedType);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType selfReference = new ResolvedRecursiveType(String[].class, null);

        assertEquals("[recursive type; UNRESOLVED]", selfReference.toString());

        JavaType referencedType = new ClassType(String.class);
        selfReference.setReference(referencedType);

        assertEquals("[recursive type; " + String.class.getName() + "]", selfReference.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType selfReference1 = new ResolvedRecursiveType(String[].class, null);
        ResolvedRecursiveType selfReference2 = new ResolvedRecursiveType(String[].class, null);

        assertFalse(selfReference1.equals(selfReference2));

        JavaType referencedType1 = new ClassType(String.class);
        selfReference1.setReference(referencedType1);

        JavaType referencedType2 = new ClassType(String.class);
        selfReference2.setReference(referencedType2);

        assertTrue(selfReference1.equals(selfReference2));
    }
}