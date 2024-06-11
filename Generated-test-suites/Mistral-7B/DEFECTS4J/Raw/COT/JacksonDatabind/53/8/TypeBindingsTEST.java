package com.fasterxml.jackson.databind.type; // make sure the package is correct

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeVariableStash; // Assuming TypeVariableStash is a helper class used in TypeBindings

public class TypeBindingsTest {

    

    // Basic tests to check method functionality

    /**
     * Test method for {@link TypeBindings#emptyBindings()}
     */
    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertFalse(empty.hasUnbound(""));
    }

    /**
     * Test method for {@link TypeBindings#create(Class, List)}
     */
    @Test
    public void testCreateWithList() {
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(ArrayList.class, Integer.class),
                JavaType.constructParametricType(ArrayList.class, String.class) };
        TypeBindings bindings = TypeBindings.create(ArrayList.class, types);
        assertEquals(types.length, bindings.size());
        assertArrayEquals(types, bindings.getTypeParameters().toArray());
        assertFalse(bindings.hasUnbound(""));
    }

    /**
     * Test method for {@link TypeBindings#create(Class, JavaType)}
     */
    @Test
    public void testCreateWithJavaType() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.constructParametricType(ArrayList.class, Integer.class));
        assertEquals(1, bindings.size());
        assertEquals("java.util.ArrayList<java.lang.Integer>", bindings.toString());
        assertFalse(bindings.hasUnbound(""));
    }

    // Edge cases and exception handling tests

    /**
     * Test method for {@link TypeBindings#create(Class, JavaType)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullInput() {
        TypeBindings.create(null, JavaType.constructParametricType(ArrayList.class, Integer.class));
    }

    /**
     * Test method for {@link TypeBindings#create(Class, JavaType)} with incorrect number of type arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithIncorrectNumberOfTypes() {
        TypeBindings.create(ArrayList.class, JavaType.constructParametricType(ArrayList.class, Integer.class, Integer.class));
    }

    /**
     * Test method for {@link TypeBindings#createIfNeeded(Class, JavaType)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeededWithNullInput() {
        TypeBindings.createIfNeeded(null, JavaType.constructParametricType(ArrayList.class, Integer.class));
    }

    /**
     * Test method for {@link TypeBindings#createIfNeeded(Class, JavaType)} with incorrect number of type arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeededWithIncorrectNumberOfTypes() {
        TypeBindings.createIfNeeded(ArrayList.class, JavaType.constructParametricType(ArrayList.class, Integer.class, Integer.class));
    }

    /**
     * Test method for {@link TypeBindings#createIfNeeded(Class, JavaType)} with empty type argument list
     */
    @Test
    public void testCreateIfNeededWithEmptyTypes() {
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    /**
     * Test method for {@link TypeBindings#createIfNeeded(Class, JavaType)} with unbound type variable
     */
    @Test
    public void testCreateIfNeededWithUnboundType() {
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, JavaType.constructParametricType(ArrayList.class, TypeVariable.class));
        assertEquals(1, bindings.size());
        assertEquals("java.util.ArrayList<java.lang.Object>", bindings.toString());
        assertTrue(bindings.hasUnbound(""));
    }

    /**
     * Test method for {@link TypeBindings#create(Class, JavaType)} with duplicate type arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithDuplicateTypes() {
        TypeBindings.create(ArrayList.class, JavaType.constructParametricType(ArrayList.class, Integer.class),
                JavaType.constructParametricType(ArrayList.class, Integer.class));
    }

    // Test method for helper class TypeParamStash

    /**
     * Test method for {@link TypeVariableStash#paramsFor1(Class)}
     */
    @Test
    public void testTypeParamStashParamsFor1() {
        TypeVariable<?>[] vars = TypeParamStash.paramsFor1(ArrayList.class);
        assertEquals(1, vars.length);
        assertEquals("E", vars[0].getName());
    }

    /**
     * Test method for {@link TypeVariableStash#paramsFor2(Class)}
     */
    @Test
    public void testTypeParamStashParamsFor2() {
        TypeVariable<?>[] vars = TypeParamStash.paramsFor2(HashMap.class);
        assertEquals(2, vars.length);
        assertEquals("K", vars[0].getName());
        assertEquals("V", vars[1].getName());
    }

    

}