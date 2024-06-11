package com.fasterxml.jackson.databind.type;

import org.junit.*;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType refdType = null;
        ReferenceType result = ReferenceType.upgradeFrom(baseType, refdType);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_exception() {
        ReferenceType.upgradeFrom(null, null);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;
        ReferenceType result = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertNotNull(result);
    }

    @Test
    public void testConstruct_2() {
        Class<?> cls = null;
        JavaType refType = null;
        ReferenceType result = ReferenceType.construct(cls, refType);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        h = null;
        ReferenceType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testGetContentType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType expResult = null;
        JavaType result = instance.getContentType();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType expResult = null;
        JavaType result = instance.getReferencedType();
        assertEquals(expResult, result);
    }

    @Test
    public void testHasContentType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean expResult = true;
        boolean result = instance.hasContentType();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean expResult = true;
        boolean result = instance.isReferenceType();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expResult = new StringBuilder();
        StringBuilder result = instance.getErasedSignature(sb);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expResult = new StringBuilder();
        StringBuilder result = instance.getGenericSignature(sb);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType expResult = null;
        JavaType result = instance.getAnchorType();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean expResult = false;
        boolean result = instance.isAnchorType();
        assertEquals(expResult, result);
    }

    @Test
    public void testToString() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        String expResult = null;
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }

}