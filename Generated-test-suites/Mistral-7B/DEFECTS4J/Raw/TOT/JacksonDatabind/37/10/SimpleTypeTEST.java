package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Test constructor when class is provided
    @Test
    public void testSimpleTypeConstructionWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test constructor when class, bindings, superClass and superInterfaces are provided
    @Test
    public void testSimpleTypeConstructionWithAllParams() {
        Class<String> stringClass = String.class;
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(stringClass, typeBindings, superClass, superInterfaces);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test constructor when another SimpleType is provided
    @Test
    public void testSimpleTypeConstructionWithAnotherSimpleType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        JavaType javaType = simpleType2.getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test construct method deprecated since 2.7
    @Test(expected = Deprecated.class)
    public void testConstructDeprecatedMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
    }

    // Test narrow method
    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        SimpleType simpleType2 = simpleType1.narrow(stringBufferClass);
        JavaType javaType1 = simpleType1.getRawType();
        JavaType javaType2 = simpleType2.getRawType();
        assertEquals(String.class, javaType1.getRawClass());
        assertEquals(StringBuffer.class, javaType2.getRawClass());
    }

    // Test toString method
    @Test
    public void testToStringMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String result = simpleType.toString();
        String expected = "[simple type, class " + String.class.getName() + ']';
        assertEquals(expected, result);
    }

    // Test equals method
    @Test
    public void testEqualsMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignatureMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String result = sb.toString();
        String expected = "<T>";
        assertEquals(expected, result);
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignatureMethod() {
        Class<List<String>> listClass = List.class;
        TypeVariable<List<String>> typeVariable = listClass.getTypeParameters()[0];
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String result = sb.toString();
        String expected = "<T>";
        assertEquals(expected, result);
    }

    // Test buildCanonicalName method
    @Test
    public void testBuildCanonicalNameMethod() {
        Class<List<List<String>>> listOfListClass = List.class;
        TypeVariable<List<List<String>>> listOfListTypeVariable = listOfListClass.getTypeParameters()[0];
        TypeVariable<List<String>> listTypeVariable = listOfListClass.getTypeParameters()[1];
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        simpleType.buildCanonicalName(sb);
        String result = sb.toString();
        String expected = "[simple type, class java.util.List]";
        assertEquals(expected, result.substring(0, expected.length() - 1));
        sb.setLength(0);
        simpleType.buildCanonicalName(sb);
        sb.append('<');
        TypeVariable<String> stringTypeVariable = List.class.getTypeParameters()[0];
        simpleType = new SimpleType(String.class);
        JavaType javaType = simpleType.getRawType();
        sb.append(javaType.toCanonical());
        simpleType = new SimpleType(List.class, Collections.singletonMap(listTypeVariable, javaType));
        simpleType.buildCanonicalName(sb);
        sb.append('>');
        result = sb.toString();
        expected = "[simple type, class java.util.List<java.lang.String>]";
        assertEquals(expected, result);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }
}