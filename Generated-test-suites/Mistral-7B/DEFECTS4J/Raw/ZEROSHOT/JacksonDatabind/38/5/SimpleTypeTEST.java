package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = simpleType;

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new Class[0], javaType.getSuperTypes());
        assertNull(javaType.getContentType());
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<String> cls = String.class;
        JavaType superClass = JavaType.construct(Object.class);
        SimpleType simpleType = new SimpleType(cls, null, superClass, null);
        JavaType javaType = simpleType;

        assertEquals(String.class, javaType.getRawClass());
        assertEquals(Object.class, javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
        assertNull(javaType.getContentType());
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType;

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
        assertNull(javaType.getContentType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStatic());

        TypeBindings bindings = new TypeBindings();
        JavaType javaTypeWithBindings = new SimpleType(cls, bindings, null, null, null, null, false);
        SimpleType simpleTypeWithBindings = (SimpleType) javaTypeWithBindings;
        assertEquals(String.class, simpleTypeWithBindings.getRawClass());
        assertEquals(bindings, simpleTypeWithBindings._bindings);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> cls = String.class;
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, valueHandler, null, false);
        JavaType javaType = simpleType;

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
        assertNull(javaType.getContentType());
        assertEquals(valueHandler, javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStatic());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> cls = String.class;
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, typeHandler, false);
        JavaType javaType = simpleType;

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
        assertNull(javaType.getValueHandler());
        assertEquals(typeHandler, javaType.getTypeHandler());
        assertFalse(javaType.isStatic());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType;

        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        SimpleType simpleTypeWithStaticTyping = (SimpleType) javaTypeWithStaticTyping;

        assertEquals(String.class, simpleTypeWithStaticTyping.getRawClass());
        assertEquals(simpleType.getRawClass(), simpleTypeWithStaticTyping.getRawClass());
        assertTrue(simpleTypeWithStaticTyping.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> cls = String.class;
        Object contentTypeHandler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> javaType.withContentTypeHandler(contentTypeHandler)
        );
        assertEquals("Simple types have no content types; can not call withContenTypeHandler()", exception.getMessage());
    }

    @Test
    public void testSimpleTypeWithValueHandlerWithContentTypeHandler() {
        Class<String> cls = String.class;
        Object valueHandler = new Object();
        Object contentTypeHandler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, valueHandler, null, false);
        JavaType javaType = simpleType;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> javaType.withContentValueHandler(contentTypeHandler)
        );
        assertEquals("Simple types have no content types; can not call withContenValueHandler()", exception.getMessage());
    }

    @Test
    public void testSimpleTypeWithNarrow() {
        Class<String> cls = String.class;
        Class<StringBuilder> subclass = StringBuilder.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = simpleType;

        SimpleType narrowedType = javaType._narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        JavaType javaType = simpleType;

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        SimpleType simpleType = new SimpleType(listClass);
        JavaType javaType = simpleType;

        String canonicalName = javaType.buildCanonicalName();
        assertEquals("java.util.List<java.lang.Integer>", canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        SimpleType simpleType = new SimpleType(listClass);
        JavaType javaType = simpleType;

        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = javaType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        SimpleType simpleType = new SimpleType(listClass);
        JavaType javaType = simpleType;

        StringBuilder expected = new StringBuilder("List<");
        StringBuilder actual = javaType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());

        expected.append("Integer>");
        expected.append(';');

        actual = javaType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }
}