
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals(String.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertNull(simpleType.getContentType());
        Assert.assertEquals("class java.lang.String", simpleType.getErasedSignature(new StringBuilder()).toString());
        Assert.assertEquals("class java.lang.String;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertNull(simpleType.getContentType());
        Assert.assertEquals("interface java.util.List", simpleType.getErasedSignature(new StringBuilder()).toString());
        Assert.assertEquals("java.util.List<java.lang.String>;", simpleType
                .withContentType(SimpleType.constructUnsafe(String.class)).getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertNull(simpleType.getContentType());
        Assert.assertEquals("interface java.util.List", simpleType.getErasedSignature(new StringBuilder()).toString());
        Assert.assertEquals(10, simpleType.withContentTypeHandler(10).getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertNull(simpleType.getContentType());
        Assert.assertEquals("interface java.util.List", simpleType.getErasedSignature(new StringBuilder()).toString());
        Assert.assertEquals(10, simpleType.withContentValueHandler(10).getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertNull(simpleType.getContentType());
        Assert.assertEquals("interface java.util.List", simpleType.getErasedSignature(new StringBuilder()).toString());
        Assert.assertEquals(true, simpleType.withStaticTyping().isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertNull(simpleType.getContentType());
        Assert.assertEquals("interface java.util.List", simpleType.getErasedSignature(new StringBuilder()).toString());
        Assert.assertNull(simpleType.refine(HashMap.class, new TypeBindings(null),
                SimpleType.constructUnsafe(Map.class), null));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(List.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(List.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(Map.class);
        
        Assert.assertTrue(simpleType1.equals(simpleType2));
        Assert.assertFalse(simpleType1.equals(simpleType3));
        Assert.assertFalse(simpleType1.equals(null));
        Assert.assertFalse(simpleType1.equals(new Object()));
    }
}
