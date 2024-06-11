package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testNewInstance() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertNotNull(referenceType.getContentType());
        assertEquals(String[].class, referenceType.getContentType().getRawClass());
        assertEquals(refType.getClass(), referenceType.getClass());
    }

    @Test
    public void testTypeHandlers() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        JavaType newRefType = referenceType.withTypeHandler(new Object() {});
        assertNotSame(referenceType, newRefType);
        assertEquals(String[].class, newRefType.getRawClass());
        assertEquals(refType.hashCode(), newRefType.getTypeId());
        assertEquals(newRefType.getClass(), ReferenceType.class);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        JavaType newRefType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newRefType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(newRefType.hashCode(), newReferenceType.getTypeId());
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newRefType, newReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(new Object() {});
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.getTypeId());
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        JavaType newRefType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newRefType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(newRefType.hashCode(), newReferenceType.getTypeId());
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newRefType, newReferenceType.getContentType());
    }

    @Test
    public void testStaticTyping() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.getTypeId());
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        String result = referenceType.toString();
        String expected = "[reference type, class [Ljava.lang.String;<T> (collection type: java.util.List<java.lang.String>)]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        JavaType newRefType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = new ReferenceType(String[].class, newRefType, null, null, false);
        assertNotEquals(referenceType, newReferenceType);

        ReferenceType sameReferenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(sameReferenceType, referenceType);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidNarrowing() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        referenceType.narrow(Integer[].class);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(referenceType.getReferencedType(), referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(String[].class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        String expected = "[Ljava.lang.String;<T> (collection type: java.util.List<java.lang.String>)]";
        assertEquals(expected, sb.toString());
    }
}