package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = null;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<?> baseClass = String.class;
        JavaType baseType = JavaType.construct(baseClass, TypeBindings.emptyBindings());
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(baseClass, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        Class<?> baseClass = String.class;
        JavaType baseType = JavaType.construct(baseClass, TypeBindings.emptyBindings());
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        Class<?> baseClass = String.class;
        JavaType baseType = JavaType.construct(Object.class, TypeBindings.emptyBindings());
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = null;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        JavaType newContentType = JavaType.construct(Integer.class, bindings);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeNullType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        referenceType.withContentType(null);
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = null;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        JavaType actualContentType = referenceType.getContentType();
        assertEquals(refType, actualContentType);
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = null;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = null;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = JavaType.construct(Integer.class, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        JavaType actualAnchorType = referenceType.getAnchorType();
        assertEquals(anchorType, actualAnchorType);
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class, bindings);
        JavaType anchorType = JavaType.construct(Integer.class, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertFalse(referenceType.isAnchorType());
        ReferenceType anchorReferenceType = new ReferenceType(cls, bindings, superClass, superInts, null, anchorType, null, null, false);
        assertTrue(anchorReferenceType.isAnchorType());
    }
}