package com.example.junit; // replace with your test package

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {
    // Test constructor with a Class
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInts
    @Test
    public void testSimpleTypeConstructorWithClassAndBindings() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertNotNull(simpleType2);
        assertEquals(simpleType1, simpleType2);
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test construct method (deprecated since 2.7)
    @Test
    public void testConstructMethodDeprecated() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test _narrow method
    @Test
    public void testNarrowMethod() {
        Class<?> superclass = Object.class;
        Class<?> subclass = String.class;
        SimpleType simpleTypeSuper = new SimpleType(superclass);
        SimpleType simpleTypeSub = new SimpleType(subclass);
        assertEquals(simpleTypeSub, simpleTypeSub._narrow(subclass));
        assertNull(simpleTypeSub._narrow(superclass));
        assertNotNull(simpleTypeSub._narrow(simpleTypeSuper));
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignatureMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignatureMethod() {
        Class<?> cls = List.class;
        Type type = new TypeToken<List<String>>() {}.getType();
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.String>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test isContainerType method
    @Test
    public void testIsContainerTypeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    // Test withContentType method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withContentType(null);
    }

    // Test withTypeHandler method
    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> cls = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(cls);
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertNotNull(simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler._typeHandler);
    }
}