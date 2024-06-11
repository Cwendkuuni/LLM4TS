package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType baseType = JavaType.construct(Integer.class);
        JavaType refType = JavaType.construct(String.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType baseType = JavaType.construct(Integer.class);

        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testWithContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);
        JavaType newContentType = JavaType.construct(Long.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);

        StringBuilder stringBuilder = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        String result = referenceType.getErasedSignature(stringBuilder).toString();
        assertEquals("[Ljava.lang.Object;", result);
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);

        StringBuilder stringBuilder = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        String result = referenceType.getGenericSignature(stringBuilder).toString();
        assertEquals("[Ljava.lang.reflect.Type;<T>Lcom.fasterxml.jackson.databind.type.ReferenceType;T;>", result);
    }

    @Test
    public void testGetAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);
        JavaType anchorType = JavaType.construct(Double.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(Integer.class);
        JavaType anchorType = JavaType.construct(Double.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        assertTrue(referenceType.isAnchorType());

        ReferenceType superType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertFalse(superType.isAnchorType());
    }
}