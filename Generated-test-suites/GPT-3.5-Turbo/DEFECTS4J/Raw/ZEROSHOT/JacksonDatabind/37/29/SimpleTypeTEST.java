package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Assert.assertNotNull(type);
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType type = SimpleType.construct(Map.class);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType type = SimpleType.construct(Collection.class);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType type = SimpleType.construct(String[].class);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType base = new SimpleType(String.class);
        SimpleType derived = (SimpleType) base._narrow(Object.class);
        Assert.assertNotEquals(base, derived);
        Assert.assertEquals(base.getBindings().size(), derived.getBindings().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = new SimpleType(Integer.class);
        type.withContentType(new SimpleType(String.class));
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = new SimpleType(Integer.class);
        SimpleType newType = type.withValueHandler("handler");
        Assert.assertNotEquals(type, newType);
        Assert.assertEquals(type.getValueHandler(), newType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(Integer.class);
        type.withContentValueHandler("handler");
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(Integer.class);
        SimpleType newType = type.withStaticTyping();
        Assert.assertNotEquals(type, newType);
        Assert.assertTrue(newType.isAsStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(Integer.class);
        JavaType refinedType = type.refine(Integer.class, new TypeBindings(), null, null);
        Assert.assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(Integer.class);
        Assert.assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(Integer.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        Assert.assertNotNull(sb);
        String signature = sb.toString();
        Assert.assertFalse(signature.isEmpty());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(Integer.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        Assert.assertNotNull(sb);
        String signature = sb.toString();
        Assert.assertFalse(signature.isEmpty());
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(Integer.class);
        String string = type.toString();
        Assert.assertFalse(string.isEmpty());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(Integer.class);
        SimpleType type2 = new SimpleType(Integer.class);
        SimpleType type3 = new SimpleType(String.class);
        Assert.assertEquals(type1, type2);
        Assert.assertNotEquals(type1, type3);
    }
}