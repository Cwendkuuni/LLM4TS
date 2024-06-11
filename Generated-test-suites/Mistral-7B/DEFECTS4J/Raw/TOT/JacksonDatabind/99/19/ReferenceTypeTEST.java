package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = SimpleType.construct(String.class);
        JavaType referencedType = TypeReference.getType(String[].class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
        assertEquals(String[].class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testConstruct() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeReference.getType(String.class);
        ReferenceType referenceType = ReferenceType.construct(cls, null, superClass, superInts, refType);
        assertEquals(String[].class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType refType = TypeReference.getType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, null, superClass, new JavaType[0], refType, null, null, null, false);
        assertEquals(String[].class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType refType = TypeReference.getType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, null, superClass, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType refType = TypeReference.getType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, null, superClass, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType refType = TypeReference.getType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, null, superClass, new JavaType[0], refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType refType = TypeReference.getType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, null, superClass, new JavaType[0], refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;<T:Ljava.lang.String;>", sb.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType cls = TypeReference.getType(String[].class);
        JavaType superClass = TypeReference.getType(Object.class);
        JavaType refType = TypeReference.getType(String.class);
        JavaType anchorType = TypeReference.getType(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, null, superClass, new JavaType[0], refType, anchorType, null, null, false);
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType simpleType = SimpleType.construct(String.class);
        JavaType referencedType = null;
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType simpleType = SimpleType.construct(int[].class);
        JavaType referencedType = TypeReference.getType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
    }
}