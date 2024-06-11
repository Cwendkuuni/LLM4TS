package com.fasterxml.jackson.databind.type; // adjust the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        Class<?> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> stringClass = String.class;
        Class<?> stringSubClass = StringBuffer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringSubType = new SimpleType(stringSubClass);

        JavaType result = stringType._narrow(stringSubClass);
        assertEquals(stringSubType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        Class<?> stringClass = String.class;
        Class<?> integerClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertNull(stringType._narrow(integerClass));
    }

    @Test
    public void testGetCanonicalName() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals("java.lang.Object[]", simpleType.buildCanonicalName());

        cls = List.class;
        simpleType = new SimpleType(cls);
        assertNotNull(simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Map.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.Map<K,V>", sb.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withContentTypeHandler(handler);
        assertNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withValueHandler(handler);
        assertEquals(new SimpleType(String.class, handler), result);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withStaticTyping();
        assertTrue(result._asStatic);

        simpleType = simpleType.withStaticTyping();
        assertFalse(simpleType._asStatic);
    }

    @Test
    public void testRefineMethod() {
        Class<?> cls = Integer[].class;
        SimpleType simpleType = new SimpleType(cls);
        assertNull(simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String result = simpleType.toString();
        String expected = "[simple type, class " + cls.getName() + ']';
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = StringBuffer.class;

        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls1);
        SimpleType simpleType3 = new SimpleType(cls2);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }

    @Test
    public void testGetSuperClass() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType superClass = simpleType.getSuperClass();
        assertNull(superClass);

        cls = ArrayList.class;
        simpleType = new SimpleType(cls);
        superClass = simpleType.getSuperClass();
        assertEquals(JavaType.getType(Object[].class), superClass);
    }

    @Test
    public void testGetSuperInterfaces() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType[] superInterfaces = simpleType.getSuperInterfaces();
        assertNull(superInterfaces);

        cls = HashMap.class;
        simpleType = new SimpleType(cls);
        superInterfaces = simpleType.getSuperInterfaces();
        assertArrayEquals(new JavaType[]{}, superInterfaces);
    }

    @Test
    public void testConstructWithBindings() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = {};
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInterfaces);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType._bindings);
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithSuperClass() {
        Class<?> superClass = List.class;
        JavaType[] superInterfaces = {};
        SimpleType simpleType = new SimpleType(superClass);
        assertEquals(superClass, simpleType.getRawClass());
        assertNull(simpleType._bindings);
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithSuperClassAndSuperInterfaces() {
        Class<?> superClass = List.class;
        JavaType[] superInterfaces = {Arrays.asList(Collections.class).getType()};
        SimpleType simpleType = new SimpleType(superClass, null, null, superInterfaces);
        assertEquals(superClass, simpleType.getRawClass());
        assertNull(simpleType._bindings);
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindingsAndSuperClass() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = {};
        SimpleType simpleType = new SimpleType(cls, bindings, superClass);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType._bindings);
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindingsAndSuperClassAndSuperInterfaces() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = {Arrays.asList(Collections.class).getType()};
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInterfaces);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType._bindings);
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    
}