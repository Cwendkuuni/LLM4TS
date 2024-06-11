package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

/**
 * Test cases for ReferenceType class.
 */
public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(superClass, referenceType.getSuperClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        JavaType anchorType = new SimpleType(Long.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, anchorType, null, null, false);
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        JavaType anchorType = new SimpleType(Long.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, anchorType, null, null, false);
        assertFalse(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = new SimpleType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = new SimpleType(String.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testWithContentType() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        JavaType newContentType = new SimpleType(Long.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertEquals(handler, newReferenceType.getTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        JavaType newContentType = new SimpleType(Long.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        JavaType newContentTypeWithHandler = newReferenceType.withContentType(newContentType);
        assertEquals(newContentTypeWithHandler, newReferenceType.getContentType());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertEquals(handler, newReferenceType.getValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        JavaType newContentType = new SimpleType(Long.class);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        JavaType newContentTypeWithHandler = newReferenceType.withContentType(newContentType);
        assertEquals(newContentTypeWithHandler, newReferenceType.getContentType());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testRefine() {
        JavaType rawType = new SimpleType(String.class);
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(rawType, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        JavaType newReferenceType = referenceType.refine(String[].class, TypeBindings.emptyBindings(), superClass, new JavaType[0]);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(refType, newReferenceType.getContentType());
    }

    @Test
    public void testToString() {
        JavaType superClass = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, null, null, null, false);
        String expected = "[reference type, class " + String.class.getName() + "<Integer>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType superClass1 = new SimpleType(String.class);
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType superClass2 = new SimpleType(String.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass1, new JavaType[0], refType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass2, new JavaType[0], refType2, null, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass1, new JavaType[0], refType1, null, null, null, true);
        ReferenceType referenceType4 = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass1, new JavaType[0], refType2, null, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));
    }
}