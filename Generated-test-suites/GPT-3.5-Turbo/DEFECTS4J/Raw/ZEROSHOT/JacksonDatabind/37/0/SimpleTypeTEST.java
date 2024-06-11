package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(List.class);
    }
    
    @Test
    public void testConstructor() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType st = new SimpleType(List.class, bindings, superClass, superInterfaces);
        Assert.assertNotNull(st);
        Assert.assertEquals(List.class, st.getRawClass());
        Assert.assertEquals(bindings, st.getBindings());
        Assert.assertEquals(superClass, st.getSuperClass());
        Assert.assertEquals(superInterfaces, st.getInterfaces());
        Assert.assertEquals(valueHandler, st.getValueHandler());
        Assert.assertEquals(typeHandler, st.getTypeHandler());
        Assert.assertEquals(asStatic, st.isStatic());
    }
    
    @Test
    public void testConstructorWithExtraHash() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        int extraHash = 0;

        SimpleType st = new SimpleType(List.class, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
        Assert.assertNotNull(st);
        Assert.assertEquals(List.class, st.getRawClass());
        Assert.assertEquals(bindings, st.getBindings());
        Assert.assertEquals(superClass, st.getSuperClass());
        Assert.assertEquals(superInterfaces, st.getInterfaces());
        Assert.assertEquals(valueHandler, st.getValueHandler());
        Assert.assertEquals(typeHandler, st.getTypeHandler());
        Assert.assertEquals(asStatic, st.isStatic());
        Assert.assertEquals(extraHash, st.getExtraHash());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(List.class);
        Assert.assertNotNull(st);
        Assert.assertEquals(List.class, st.getRawClass());
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
    public void testNarrow() {
        SimpleType narrowedType = simpleType._narrow(ArrayList.class);
        Assert.assertNotNull(narrowedType);
        Assert.assertEquals(ArrayList.class, narrowedType.getRawClass());
        Assert.assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        Assert.assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        Assert.assertEquals(simpleType.getInterfaces(), narrowedType.getInterfaces());
        Assert.assertEquals(simpleType.getValueHandler(), narrowedType.getValueHandler());
        Assert.assertEquals(simpleType.getTypeHandler(), narrowedType.getTypeHandler());
        Assert.assertEquals(simpleType.isStatic(), narrowedType.isStatic());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        simpleType.withContentType(new SimpleType(String.class));
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType type = simpleType.withTypeHandler(typeHandler);
        Assert.assertNotNull(type);
        Assert.assertEquals(typeHandler, type.getTypeHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        simpleType.withContentTypeHandler(new Object());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType type = simpleType.withValueHandler(valueHandler);
        Assert.assertNotNull(type);
        Assert.assertEquals(valueHandler, type.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType st = simpleType.withStaticTyping();
        Assert.assertNotNull(st);
        Assert.assertTrue(st.isStatic());
    }
    
    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        Assert.assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        boolean isContainer = simpleType.isContainerType();
        Assert.assertFalse(isContainer);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder actualSignature = simpleType.getErasedSignature(sb);
        Assert.assertEquals(sb, actualSignature);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder actualSignature = simpleType.getGenericSignature(sb);
        Assert.assertEquals(sb, actualSignature);
    }
    
    @Test
    public void testToString() {
        String expectedString = "[simple type, class java.util.List]";
        Assert.assertEquals(expectedString, simpleType.toString());
    }
    
    @Test
    public void testEqualsWithSameType() {
        SimpleType sameType = new SimpleType(List.class);
        Assert.assertEquals(simpleType, sameType);
    }
    
    @Test
    public void testEqualsWithDifferentType() {
        SimpleType differentType = new SimpleType(Set.class);
        Assert.assertNotEquals(simpleType, differentType);
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        Object obj = new Object();
        Assert.assertNotEquals(simpleType, obj);
    }
}