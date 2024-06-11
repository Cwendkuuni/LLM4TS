package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        SimpleType simpleType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType, simpleType2);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType simpleTypeCopy = new SimpleType(simpleType);
        assertEquals(simpleType, simpleTypeCopy);
    }

    @Test
    public void testSimpleTypeWithSuperTypes() {
        Class<String> stringClass = String.class;
        Class<List<Integer>> listClass = List.class.getTypeParameters()[0].getType();
        JavaType superClass = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.defaultInstance().constructType(Comparable.class) };
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(List.class, simpleType.getSuperClass());
        assertEquals(Arrays.asList(Integer.class), simpleType.getSuperTypes());
        assertEquals(Comparable.class, simpleType.getSuperInterface(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class.getTypeParameters()[0].getType();
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructMethod() {
        Class<String> stringClass = String.class;
        new SimpleType(stringClass, null, null, null, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        new SimpleType(stringClass, null, null, null, 0, null, false);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowType = simpleType._narrow(stringBuilderClass);
        assertNull(narrowType);
    }

    @Test
    public void testNarrowMethodWithSubclass() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowType = simpleType._narrow(stringBufferClass);
        assertEquals(StringBuffer.class, narrowType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowType.getBindings());
        assertEquals(null, narrowType.getSuperClass());
        assertEquals(null, narrowType.getSuperInterfaces());
    }
}