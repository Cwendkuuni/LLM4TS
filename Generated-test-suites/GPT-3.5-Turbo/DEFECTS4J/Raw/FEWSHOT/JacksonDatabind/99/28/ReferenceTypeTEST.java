package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        ReferenceType refType = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertNotNull(refType);
        assertEquals(Class.class, refType.getRawClass());
        assertNull(refType.getSuperClass());
        assertNull(refType.getSuperInterfaces());
        assertNull(refType.getValueHandler());
        assertNull(refType.getTypeHandler());
        assertNull(refType.getContentType());
        assertEquals(refType, refType.getReferencedType());
        assertTrue(refType.isReferenceType());
        assertFalse(refType.hasContentType());
        assertFalse(refType.isAnchorType());
    }

    @Test
    public void testConstructorWithRefType() {
        JavaType refType = new SimpleType(Class.class);
        ReferenceType rt = new ReferenceType(refType);
        assertNotNull(rt);
        assertNull(rt.getRawClass());
        assertNull(rt.getSuperClass());
        assertNull(rt.getSuperInterfaces());
        assertNull(rt.getValueHandler());
        assertNull(rt.getTypeHandler());
        assertNull(rt.getContentType());
        assertEquals(rt, rt.getReferencedType());
        assertTrue(rt.isReferenceType());
        assertFalse(rt.hasContentType());
        assertTrue(rt.isAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Object.class);
        JavaType refdType = new SimpleType(Integer.class);
        ReferenceType rt = ReferenceType.upgradeFrom(baseType, refdType);
        assertNotNull(rt);
        assertNull(rt.getRawClass());
        assertNull(rt.getSuperClass());
        assertNull(rt.getSuperInterfaces());
        assertNull(rt.getValueHandler());
        assertNull(rt.getTypeHandler());
        assertNull(rt.getContentType());
        assertEquals(rt, rt.getReferencedType());
        assertTrue(rt.isReferenceType());
        assertFalse(rt.hasContentType());
        assertTrue(rt.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefdType() {
        JavaType baseType = new SimpleType(Object.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        ReferenceType.upgradeFrom(new SimpleType(String.class), new SimpleType(Integer.class));
    }

    @Test
    public void testRefine() {
        ReferenceType refType = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{new SimpleType(Comparable.class)};
        ReferenceType rt = (ReferenceType) refType.refine(Object.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNotNull(rt);
        assertEquals(Object.class, rt.getRawClass());
        assertEquals(superClass, rt.getSuperClass());
        assertArrayEquals(superInterfaces, rt.getSuperInterfaces());
        assertNull(rt.getValueHandler());
        assertNull(rt.getTypeHandler());
        assertNull(rt.getContentType());
        assertEquals(rt, rt.getReferencedType());
        assertTrue(rt.isReferenceType());
        assertFalse(rt.hasContentType());
        assertFalse(rt.isAnchorType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertTrue(rt.hasContentType());
        assertEquals(refType, rt.getContentType());

        JavaType newContentType = new SimpleType(String.class);
        ReferenceType updatedRt = (ReferenceType) rt.withContentType(newContentType);
        assertEquals(newContentType, updatedRt.getContentType());
        assertFalse(updatedRt.hasContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertNull(rt.getTypeHandler());

        Object handler = new Object();
        ReferenceType updatedRt = (ReferenceType) rt.withTypeHandler(handler);
        assertEquals(handler, updatedRt.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertNull(rt.getContentType().getValueHandler());

        Object handler = new Object();
        ReferenceType updatedRt = (ReferenceType) rt.withContentTypeHandler(handler);
        assertEquals(handler, updatedRt.getContentType().getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertNull(rt.getValueHandler());

        Object handler = new Object();
        ReferenceType updatedRt = (ReferenceType) rt.withValueHandler(handler);
        assertEquals(handler, updatedRt.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertNull(rt.getContentType().getValueHandler());

        Object handler = new Object();
        ReferenceType updatedRt = (ReferenceType) rt.withContentValueHandler(handler);
        assertEquals(handler, updatedRt.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertFalse(rt.isJavaLangObject());
        ReferenceType updatedRt = (ReferenceType) rt.withStaticTyping();
        assertTrue(updatedRt.isJavaLangObject());
        assertNotEquals(rt, updatedRt);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertNull(rt.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        assertNull(rt.getGenericSignature(sb));
        assertEquals("class java.lang.Object;<>", sb.toString());
    }

    @Test
    public void testToString() {
        ReferenceType rt = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertEquals("[reference type, class java.lang.Class<>]", rt.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType rt1 = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        ReferenceType rt2 = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertTrue(rt1.equals(rt2));
        assertTrue(rt2.equals(rt1));

        ReferenceType rt3 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertFalse(rt1.equals(rt3));
        assertFalse(rt3.equals(rt1));

        ReferenceType rt4 = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, new SimpleType(Integer.class), null, null, null, false);
        ReferenceType rt5 = new ReferenceType(Class.class, TypeBindings.emptyBindings(), null, null, new SimpleType(Double.class), null, null, null, false);
        assertFalse(rt4.equals(rt5));
        assertFalse(rt5.equals(rt4));
    }
}