package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.Integer]");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.Integer]");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        String expected = "[reference type, class java.lang.Integer<java.lang.String>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType1 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType1);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType2 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType2);
        JavaType refType3 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType3 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType3);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(refType3));
        assertFalse(referenceType2.equals(referenceType1));
        assertFalse(referenceType2.equals(refType3));
        assertFalse(refType1.equals(referenceType1));
        assertFalse(refType1.equals(referenceType2));
        assertFalse(refType1.equals(refType3));
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertNull(referenceType.getAnchorType());

        JavaType anchorType = TypeFactory.defaultInstance().constructType(String[].class);
        referenceType = new ReferenceType(referenceType, anchorType);
        assertEquals(referenceType.getClass(), anchorType.getClass());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertFalse(referenceType.isAnchorType());

        JavaType anchorType = TypeFactory.defaultInstance().constructType(String[].class);
        referenceType = new ReferenceType(referenceType, anchorType);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType result = referenceType.withContentType(contentType);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertEquals(Long.class, result.getContentType().getRawClass());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        Object handler = new Object();
        JavaType result = referenceType.withTypeHandler(handler);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertSame(handler, result.getTypeHandler());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        Object handler = new Object();
        JavaType contentType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType result = referenceType.withContentTypeHandler(handler);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertEquals(Long.class, result.getContentType().getRawClass());
        assertSame(handler, result.getContentTypeHandler());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        Object handler = new Object();
        JavaType result = referenceType.withValueHandler(handler);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertSame(handler, result.getValueHandler());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        Object handler = new Object();
        JavaType result = referenceType.withContentValueHandler(handler);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertSame(handler, result.getContentValueHandler());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        JavaType result = referenceType.withStaticTyping();
        assertFalse(referenceType.isStaticTyping());
        assertTrue(result.isStaticTyping());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testRefine() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, baseType);
        Class<?> subclass = Object[].class;
        JavaType result = referenceType.refine(subclass, TypeBindings.emptyBindings(), null, null);
        assertEquals(subclass, result.getRawClass());
        assertEquals(Integer.class, result.getContentType().getRawClass());
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithNonReferenceType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, baseType);
        Class<?> nonReferenceType = Integer.class;
        referenceType.refine(nonReferenceType, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMissingReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }
}