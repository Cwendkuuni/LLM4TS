package com.fasterxml.jackson.databind.type; // Changing the package to match the class

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    

    // Basic tests for factory methods

    @Test
    public void testCreateEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateOneType() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        assertEquals(1, bindings.size());
        assertNotNull(bindings.findBoundType(""));
        assertEquals(javaType, bindings.findBoundType(""));
    }

    @Test
    public void testCreateTwoTypes() {
        JavaType javaType1 = new ClassType(String.class);
        JavaType javaType2 = new ClassType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType(""));
        assertEquals(javaType1, bindings.findBoundType(""));
        assertNotNull(bindings.findBoundType(""));
        assertEquals(javaType2, bindings.findBoundType(""));
    }

    @Test
    public void testCreateIfNeededOneType() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(String[].class, javaType);
        assertEquals(1, bindings.size());
        assertNotNull(bindings.findBoundType(""));
        assertEquals(javaType, bindings.findBoundType(""));
    }

    @Test
    public void testCreateIfNeededTwoTypes() {
        JavaType javaType1 = new ClassType(String.class);
        JavaType javaType2 = new ClassType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType(""));
        assertEquals(javaType1, bindings.findBoundType(""));
        assertNotNull(bindings.findBoundType(""));
        assertEquals(javaType2, bindings.findBoundType(""));
    }

    // Test for factory methods with unresolved types

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUnmatchedTypesOneType() {
        JavaType javaType = new ClassType(Integer.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUnmatchedTypesTwoTypes() {
        JavaType javaType1 = new ClassType(String.class);
        JavaType javaType2 = new ClassType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType1, javaType2);
    }

    // Test for method withUnboundVariable

    @Test
    public void testWithUnboundVariable() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        TypeBindings newBindings = bindings.withUnboundVariable("newVariable");
        assertNotSame(bindings, newBindings);
        assertEquals(bindings.size() + 1, newBindings.size());
        assertNotNull(newBindings.findBoundType("newVariable"));
    }

    // Test for accessor methods

    @Test
    public void testGetBoundType() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testGetBoundName() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        assertEquals("", bindings.getBoundName(0));
    }

    @Test
    public void testTypeParameters() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        List<JavaType> javaTypes = bindings.getTypeParameters();
        assertEquals(1, javaTypes.size());
        assertEquals(javaType, javaTypes.get(0));
    }

    @Test
    public void testHasUnbound() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        assertFalse(bindings.hasUnbound(""));

        TypeBindings newBindings = bindings.withUnboundVariable("newVariable");
        assertTrue(newBindings.hasUnbound("newVariable"));
    }

    // Test for toString method

    @Test
    public void testToString() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, javaType);
        String expected = "<T>";
        String actual = bindings.toString();
        assertEquals(expected, actual);

        JavaType javaType1 = new ClassType(String.class);
        JavaType javaType2 = new ClassType(Integer.class);
        TypeBindings bindings2 = TypeBindings.create(List.class, javaType1, javaType2);
        String expected2 = "<T, U>";
        String actual2 = bindings2.toString();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testEquals() {
        JavaType javaType = new ClassType(String.class);
        TypeBindings bindings1 = TypeBindings.create(String[].class, javaType);
        TypeBindings bindings2 = TypeBindings.create(String[].class, javaType);

        TypeBindings bindings3 = TypeBindings.create(ArrayList.class, javaType);
        assertNotEquals(bindings1, bindings3);

        TypeBindings bindings4 = new TypeBindings(new String[]{"T1"}, new JavaType[0]);
        assertNotEquals(bindings1, bindings4);

        TypeBindings bindings5 = bindings1;
        assertEquals(bindings1, bindings5);
    }

    
}