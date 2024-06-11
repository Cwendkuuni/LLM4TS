package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType simpleType = TypeReference.construct(String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, IntegerType.getInstance());
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(IntegerType.getInstance(), referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType simpleType = TypeReference.construct(String.class);
        JavaType referenceType = new ReferenceType(
                TypeReference.construct(String.class),
                TypeBindings.emptyBindings(),
                null, null,
                IntegerType.getInstance(),
                referenceType,
                null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(IntegerType.getInstance(), referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeGetters() {
        JavaType simpleType = TypeReference.construct(String.class);
        JavaType referenceType = new ReferenceType(
                TypeReference.construct(String.class),
                TypeBindings.emptyBindings(),
                null, null,
                IntegerType.getInstance(),
                referenceType,
                null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(IntegerType.getInstance(), referenceType.getContentType());
        assertEquals(referenceType, referenceType.withContentType(IntegerType.getInstance()));
        assertEquals(referenceType, referenceType.withTypeHandler(null));
        assertEquals(referenceType, referenceType.withContentTypeHandler(null));
        assertEquals(referenceType, referenceType.withValueHandler(null));
        assertEquals(referenceType, referenceType.withContentValueHandler(null));
        assertEquals(referenceType, referenceType.withStaticTyping());
        assertEquals(referenceType, referenceType.refine(String.class, TypeBindings.emptyBindings(), null, null));
        assertFalse(referenceType.isAnchorType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeInvalidConstruction() {
        JavaType simpleType = TypeReference.construct(String.class);
        ReferenceType referenceType = new ReferenceType(
                TypeReference.construct(String.class),
                TypeBindings.emptyBindings(),
                null, null,
                null, // missing refType
                referenceType,
                null, null, false);
    }
}