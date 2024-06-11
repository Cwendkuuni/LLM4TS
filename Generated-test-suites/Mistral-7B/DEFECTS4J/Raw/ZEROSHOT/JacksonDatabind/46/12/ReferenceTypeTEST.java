package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/String;<T:Ljava/lang/Object;>", sb.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null, null);
        String toString = referenceType.toString();
        assertTrue(toString.contains("[reference"));
        assertTrue(toString.contains("class [" + stringClass.getName() + "]"));
        assertTrue(toString.contains("<T:" + refType.toString() + ">]"));
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType refType1 = ReferenceType.construct(stringClass, refType, null, null);
        ReferenceType refType2 = ReferenceType.construct(stringClass, refType, null, null);
        assertTrue(refType1.equals(refType2));
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType narrowType = refType.narrow(String[].class);
        ReferenceType narrowReferenceType = (ReferenceType) narrowType;
        assertEquals(String[].class, narrowReferenceType.getContentType().getRawClass());
        assertEquals(refType, narrowReferenceType.getReferencedType());
    }
}