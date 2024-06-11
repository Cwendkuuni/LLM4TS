package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType);
        JavaType contentType = referenceType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType);
        JavaType referencedTypeFromReference = referenceType.getReferencedType();
        assertEquals(Integer.class, referencedTypeFromReference.getRawClass());
    }

    @Test
    public void testGetAnchorType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType, null);
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, null, null, new JavaType[]{}, referencedType, referenceType);
        assertTrue(referenceType.isAnchorType());
    }
}