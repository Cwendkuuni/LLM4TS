package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testConstructWithAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType, anchorType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertNull(referenceType.getSuperClass());
        assertArrayEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonTypeBase() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructValueType(Boolean.TYPE);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testGetContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals("[Ljava.lang.String;", referenceType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals("[Ljava.lang.String;<T>", referenceType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertTrue(referenceType.isAnchorType());

        ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertFalse(referenceType2.isAnchorType());
    }
}