package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
        SimpleType expectedSimpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(expectedSimpleType, simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        Class<?> mapClass = HashMap.class;
        SimpleType.construct(mapClass);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> stringClass = String.class;
        Class<?> stringSubclass = StringBuilder.class;

        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = simpleType._narrow(stringSubclass);
        SimpleType expectedNarrowedType = new SimpleType(stringSubclass, TypeBindings.emptyBindings(), simpleType, new JavaType[0], null, null, false);
        assertEquals(expectedNarrowedType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowIllegalArgumentException() {
        Class<?> stringClass = String.class;
        Class<?> integerClass = Integer.class;

        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = simpleType._narrow(integerClass);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> stringClass = String.class;
        Object stringTypeHandler = "StringTypeHandler";

        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType withTypeHandlerType = simpleType.withTypeHandler(stringTypeHandler);
        SimpleType expectedWithTypeHandlerType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, stringTypeHandler, false);
        assertEquals(expectedWithTypeHandlerType, withTypeHandlerType);
    }

    @Test
    public void testGetCanonicalNameMethod() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);

        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "java.lang.String";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);

        boolean isContainerType = simpleType.isContainerType();
        assertFalse(isContainerType);
    }
}