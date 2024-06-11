package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("SimpleType with class 'String'", cls.getName(), simpleType.getRawClass().getName());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals("Copied SimpleType", simpleType, copiedSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethodWithClass() {
        Class<?> cls = String.class;
        SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithCollectionClass() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithMapClass() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithArrayClass() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(handler);
        assertEquals("SimpleType with new type handler", handler, newSimpleType.getTypeHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue("SimpleType with static typing", newSimpleType.isStatic());
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse("SimpleType is not a container type", simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals("Canonical name of SimpleType with class 'String'", "java.lang.String", simpleType.getCanonicalName());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder("String");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Erased signature of SimpleType with class 'String'", expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<?> cls = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType listSimpleType = new SimpleType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder("List<String>");
        StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Generic signature of SimpleType with class 'String'", expectedSignature.toString(), actualSignature.toString());
    }
}