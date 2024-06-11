package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.withoutValueHandler(null);

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getGenericTypeParameters());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null);
        JavaType javaType = simpleType.withoutValueHandler(null);

        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getGenericTypeParameters());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass, null, null, null);
        SimpleType copy = new SimpleType(original);

        assertEquals(original, copy);
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null);
        JavaType javaType = simpleType.narrow(intClass);

        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidNarrowing() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.narrow(intClass);
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertNull(simpleType.withContentTypeHandler(null));
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.withTypeHandler(handler);

        assertEquals(handler, javaType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.withValueHandler(handler);

        assertEquals(handler, javaType.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.withStaticTyping();

        assertTrue(javaType.isStatic());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.refine(String[].class, null, null, null);

        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null);
        JavaType javaType = simpleType.narrow(int.class.getGenericType());

        assertNull(javaType);

        StringBuilder expected = new StringBuilder();
        expected.append("java.lang.Integer");
        expected.append('<');
        expected.append("I");
        expected.append('>');

        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/lang/String;");

        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava/lang/Object;");

        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testSimpleTypeIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, null, null, null);
        SimpleType simpleType2 = new SimpleType(stringClass, null, null, null);

        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeEqualsDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(stringClass, null, null, null);
        SimpleType simpleType2 = new SimpleType(integerClass, null, null, null);

        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeEqualsNull() {
        SimpleType simpleType = new SimpleType(String.class);

        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testSimpleTypeHashCode() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, null, null, null);
        SimpleType simpleType2 = new SimpleType(stringClass, null, null, null);

        int hashCode1 = simpleType1.hashCode();
        int hashCode2 = simpleType2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testSimpleTypeHashCodeDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(stringClass, null, null, null);
        SimpleType simpleType2 = new SimpleType(integerClass, null, null, null);

        int hashCode1 = simpleType1.hashCode();
        int hashCode2 = simpleType2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}