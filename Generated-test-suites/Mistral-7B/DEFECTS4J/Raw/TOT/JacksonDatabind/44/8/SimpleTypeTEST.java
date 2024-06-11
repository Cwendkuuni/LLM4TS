package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(expectedType, stringSimpleType);

        SimpleType stringSimpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings());
        assertEquals(expectedType, stringSimpleTypeWithBindings);

        SimpleType stringSimpleTypeWithSuperClass = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(expectedType, stringSimpleTypeWithSuperClass);

        SimpleType stringSimpleTypeWithSuperClassAndSuperInterfaces = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, new JavaType[] {TypeFactory.defaultInstance().constructType(List.class)});
        JavaType expectedTypeWithSuperInterfaces = new SimpleType(List.class, TypeBindings.emptyBindings(), expectedType, new JavaType[] {expectedType});
        assertEquals(expectedTypeWithSuperInterfaces, stringSimpleTypeWithSuperClassAndSuperInterfaces);
    }

    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = SimpleType.constructUnsafe(stringClass);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(expectedType, stringSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithMap() {
        Class<Map<String, String>> mapClass = Map.class;
        SimpleType mapSimpleType = new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithCollection() {
        Class<List<String>> listClass = List.class;
        SimpleType listSimpleType = new SimpleType(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithArray() {
        Class<String[]> stringArrayClass = String[].class;
        SimpleType stringArraySimpleType = new SimpleType(stringArrayClass);
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        JavaType refinedType = stringSimpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertEquals(null, refinedType);
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType1 = new SimpleType(stringClass);
        SimpleType stringSimpleType2 = new SimpleType(stringClass);
        SimpleType stringSimpleType3 = new SimpleType(Object.class);
        assertEquals(stringSimpleType1, stringSimpleType2);
        assertNotEquals(stringSimpleType1, stringSimpleType3);
    }
}