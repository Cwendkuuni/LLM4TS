package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructor with a simple class
        Class<String> stringClass = String.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        assertEquals(String.class, simpleTypeString.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeString.getBindings());
        assertNull(simpleTypeString.getSuperClass());
        assertNull(simpleTypeString.getSuperInterfaces());

        // Test constructor with bindings, superClass and superInterfaces
        JavaType javaTypeListString = JavaType.construct(List.class, String.class);
        SimpleType simpleTypeListString = new SimpleType(stringClass, TypeBindings.emptyBindings(), javaTypeListString, null);
        assertEquals(String.class, simpleTypeListString.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeListString.getBindings());
        assertEquals(List.class, simpleTypeListString.getSuperClass());
        assertNull(simpleTypeListString.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        // Test narrowing with the same class
        Class<String> stringClass = String.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        SimpleType narrowedString = simpleTypeString.narrow(String.class);
        assertEquals(simpleTypeString, narrowedString);

        // Test narrowing with a subclass
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        SimpleType simpleTypeStringBuilder = new SimpleType(stringClass);
        SimpleType narrowedStringBuilder = simpleTypeStringBuilder.narrow(StringBuilder.class);
        assertNotSame(simpleTypeStringBuilder, narrowedStringBuilder);
        assertEquals(StringBuilder.class, narrowedStringBuilder.getRawClass());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        // Test withTypeHandler
        Class<String> stringClass = String.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        Object stringHandler = new Object() {};
        SimpleType simpleTypeStringWithHandler = simpleTypeString.withTypeHandler(stringHandler);
        assertEquals(simpleTypeString, simpleTypeStringWithHandler.getBaseType());
        assertEquals(stringHandler, simpleTypeStringWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        // Test constructor with Map class
        Class<Map<String, Integer>> mapClass = Map.class;
        new SimpleType(mapClass);
    }
}