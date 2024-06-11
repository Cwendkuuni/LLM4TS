package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            SimpleType.construct(Collection.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            SimpleType.construct(int[].class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        SimpleType simpleType = SimpleType.construct(String.class);
        Assert.assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(CharSequence.class);
        Assert.assertEquals(CharSequence.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Collection.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(String.class));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(typeHandler);
        Assert.assertEquals(typeHandler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Collection.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object valueHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(valueHandler);
        Assert.assertEquals(valueHandler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Collection.class);
        try {
            simpleType.withContentValueHandler(new Object());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType newType = simpleType.withStaticTyping();
        Assert.assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType.refine(CharSequence.class, null, null, null);
        Assert.assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Assert.assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        Assert.assertEquals("class " + String.class.getName(), sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        Assert.assertEquals("class " + String.class.getName() + ";", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals("[simple type, class " + String.class.getName() + "]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals(simpleType1, simpleType2);
    }
}