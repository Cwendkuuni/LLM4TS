package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        Assert.assertNotNull(simpleType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        try {
            simpleType.withContentType(null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(handler);
        Assert.assertEquals(handler, newSimpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        try {
            simpleType.withContentValueHandler(null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        Assert.assertTrue(newSimpleType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType newJavaType = simpleType.refine(null, null, null, null);
        Assert.assertNull(newJavaType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        Assert.assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        Assert.assertNotNull(sb);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        Assert.assertNotNull(sb);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        String toString = simpleType.toString();
        Assert.assertNotNull(toString);
        Assert.assertTrue(toString.contains("[simple type"));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Object.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Object.class);
        Assert.assertEquals(simpleType1, simpleType2);
    }
}