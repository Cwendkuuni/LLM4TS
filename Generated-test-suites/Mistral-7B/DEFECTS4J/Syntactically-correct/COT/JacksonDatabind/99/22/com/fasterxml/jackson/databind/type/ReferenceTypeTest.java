package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, refType);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(int[].class);
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNotNull(referenceType);
        assertEquals(baseType.getRawClass(), referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullType() {
        JavaType baseType = new JavaType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWrongType() {
        JavaType baseType = new JavaType(int[].class);
        JavaType refType = new JavaType(int.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testGetContentType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = new JavaType(int.class);
        JavaType newContentType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaType(int.class);
        Object handler = new Object() {};
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaType(int.class);
        Object contentHandler = new Object() {};
        Object typeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentHandler).withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentHandler, newReferenceType.<Object>getValueHandler());
        assertEquals(typeHandler, newReferenceType.<Object>getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaType(int.class);
        Object handler = new Object() {};
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaType(int.class);
        Object contentHandler = new Object() {};
        Object valueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentHandler).withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentHandler, newReferenceType.<Object>getValueHandler());
        assertEquals(valueHandler, newReferenceType.<Object>getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType rawType = new JavaType(int[].class);
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(rawType, null, null, null, refType, null, null, null, false);
        JavaType newReferenceType = referenceType.refine(String[].class, null, null, null);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(refType, newReferenceType.getContentType());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, null, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        String expected = "[reference type, class java.lang.String<int>]";
        assertEquals(expected, canonicalName);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new JavaType(int.class);
        JavaType refType2 = new JavaType(int.class);
        ReferenceType referenceType1 = new ReferenceType(null, null, null, null, refType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(null, null, null, null, refType2, null, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(null, null, null, null, refType1, null, null, null, true);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referenceType3));
    }

}