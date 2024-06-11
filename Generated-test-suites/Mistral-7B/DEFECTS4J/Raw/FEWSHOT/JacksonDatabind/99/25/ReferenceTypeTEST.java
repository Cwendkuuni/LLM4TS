package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;[]", sb.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, referenceType, null, null, false);
        assertTrue(referenceType.isAnchorType());
    }
}