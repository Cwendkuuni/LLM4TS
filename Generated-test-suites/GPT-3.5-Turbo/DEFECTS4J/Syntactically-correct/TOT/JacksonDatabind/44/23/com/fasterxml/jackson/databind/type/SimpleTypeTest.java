package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(String.class);
        Assert.assertEquals(String.class, simpleType.getRawClass());
        Assert.assertEquals(0, simpleType.containedTypeCount());
    }

    @Test
    public void testConstructor2() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new SimpleType[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(Integer.class, bindings, superClass, superInterfaces);
        Assert.assertEquals(Integer.class, simpleType.getRawClass());
        Assert.assertEquals(0, simpleType.containedTypeCount());
        Assert.assertEquals(superClass, simpleType.getSuperClass());
        Assert.assertEquals(superInterfaces.length, simpleType.getInterfaces().length);
    }

    @Test
    public void testConstructor3() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new SimpleType[0];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(Long.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        Assert.assertEquals(Long.class, simpleType.getRawClass());
        Assert.assertEquals(0, simpleType.containedTypeCount());
        Assert.assertEquals(superClass, simpleType.getSuperClass());
        Assert.assertEquals(superInterfaces.length, simpleType.getInterfaces().length);
        Assert.assertEquals(valueHandler, simpleType.getValueHandler());
        Assert.assertEquals(typeHandler, simpleType.getTypeHandler());
        Assert.assertEquals(asStatic, simpleType.isStatic());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Double.class);
        Assert.assertEquals(Double.class, simpleType.getRawClass());
        Assert.assertEquals(0, simpleType.containedTypeCount());
        Assert.assertNull(simpleType.getSuperClass());
        Assert.assertEquals(0, simpleType.getInterfaces().length);
        Assert.assertNull(simpleType.getValueHandler());
        Assert.assertNull(simpleType.getTypeHandler());
        Assert.assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType contentType = new SimpleType(String.class);
        try {
            simpleType.withContentType(contentType);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withValueHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentValueHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(List.class);
        SimpleType newType = simpleType.withStaticTyping();
        Assert.assertEquals(simpleType, newType);
        Assert.assertTrue(simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(List.class);
        Assert.assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(List.class);
        Assert.assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(sb, simpleType.getErasedSignature(sb));
        Assert.assertEquals("Ljava/util/List;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(sb, simpleType.getGenericSignature(sb));
        Assert.assertEquals("Ljava/util/List<*>;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Assert.assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        Assert.assertTrue(type1.equals(type2));
        Assert.assertFalse(type1.equals(null));
        Assert.assertFalse(type1.equals(new Object()));
        
    }
}