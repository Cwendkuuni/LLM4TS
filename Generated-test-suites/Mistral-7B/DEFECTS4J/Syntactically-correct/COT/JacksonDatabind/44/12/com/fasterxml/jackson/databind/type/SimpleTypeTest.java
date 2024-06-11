package com.yourpackagename; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings());
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertSame(simpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertSame(simpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(simpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<String> cls = String.class;
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(cls));
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType intType = new SimpleType(intClass);
        SimpleType narrowedType = stringType.narrow(intClass);
        assertNull(narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(JavaType.construct(int[].class)));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertSame(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertSame(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.constructType(intClass);
        assertNull(simpleType.refine(intClass, TypeBindings.emptyBindings(), javaType, new JavaType[0]));
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("[simple type, class java.lang.String]", simpleType.buildCanonicalName());

        Class<List<String>> listStringClass = List.class.getTypeParameters()[0].getRawType();
        SimpleType listSimpleType = new SimpleType(listStringClass);
        assertEquals("[simple type, class java.util.List<java.lang.String>]", listSimpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder stringBuilder = new StringBuilder();
        simpleType.getErasedSignature(stringBuilder);
        assertEquals("java.lang.String", stringBuilder.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listStringClass = List.class.getTypeParameters()[0].getRawType();
        SimpleType simpleType = new SimpleType(listStringClass);
        StringBuilder stringBuilder = new StringBuilder();
        simpleType.getGenericSignature(stringBuilder);
        assertEquals("[java.lang.String]", stringBuilder.toString());
    }

    
}