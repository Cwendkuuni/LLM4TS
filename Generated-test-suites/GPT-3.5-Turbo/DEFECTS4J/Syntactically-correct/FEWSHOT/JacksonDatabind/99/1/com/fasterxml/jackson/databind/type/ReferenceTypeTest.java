package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(null, referenceType.getContentType());
        assertFalse(referenceType.hasContentType());
        assertFalse(referenceType.isReferenceType());
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        JavaType refdType = new ReferenceType(Integer.class, null, null, null, null,
                null, null, null, false);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        JavaType contentType = new ReferenceType(Integer.class, null, null, null, null,
                null, null, null, false);

        ReferenceType updatedType = (ReferenceType) referenceType.withContentType(contentType);

        assertEquals(String.class, updatedType.getRawClass());
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        Object typeHandler = new Object();

        ReferenceType updatedType = referenceType.withTypeHandler(typeHandler);

        assertEquals(String.class, updatedType.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        Object typeHandler = new Object();

        ReferenceType updatedType = referenceType.withContentTypeHandler(typeHandler);

        assertEquals(String.class, updatedType.getRawClass());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        Object valueHandler = new Object();

        ReferenceType updatedType = referenceType.withValueHandler(valueHandler);

        assertEquals(String.class, updatedType.getRawClass());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        Object valueHandler = new Object();

        ReferenceType updatedType = referenceType.withContentValueHandler(valueHandler);

        assertEquals(String.class, updatedType.getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);

        ReferenceType updatedType = referenceType.withStaticTyping();

        assertEquals(String.class, updatedType.getRawClass());
    }

    @Test
    public void testRefine() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);
        Class<?> rawType = Integer.class;
        JavaType superClass = new ReferenceType(Number.class, null, null, null, null,
                null, null, null, false);
        JavaType[] superInterfaces = null;

        ReferenceType refinedType = (ReferenceType) referenceType.refine(rawType, null, superClass,
                superInterfaces);

        assertEquals(Integer.class, refinedType.getRawClass());
        assertEquals(1, refinedType.countTypeParameters());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null,
                null, null, null, false);

        String toString = referenceType.toString();

        assertTrue(toString.contains(referenceType.getRawClass().getName()));
    }
}