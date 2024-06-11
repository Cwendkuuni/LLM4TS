package com.fasterxml.jackson.databind.type;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TypeBindingsTest {

    private static TypeBindings typeBindings;

    @BeforeClass
    public static void setUp() {
        typeBindings = TypeBindings.create(Class.class, new JavaType[0]);
    }

    @Test
    public void testFindBoundType() {
        JavaType boundType = typeBindings.findBoundType("T");
        assertNull(boundType);

        boundType = typeBindings.findBoundType("Test");
        assertNull(boundType);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(typeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        String boundName = typeBindings.getBoundName(0);
        assertNull(boundName);
    }

    @Test
    public void testGetBoundType() {
        JavaType boundType = typeBindings.getBoundType(0);
        assertNull(boundType);
    }

    @Test
    public void testGetTypeParameters() {
        assertTrue(typeBindings.getTypeParameters().isEmpty());
    }

    @Test
    public void testHasUnbound() {
        assertFalse(typeBindings.hasUnbound("Unbound"));
    }

    @Test
    public void testHashCode() {
        int hashCode = typeBindings.hashCode();
        assertEquals(1, hashCode);
    }

    @Test
    public void testEquals() {
        TypeBindings identicalBindings = TypeBindings.create(Class.class, new JavaType[0]);
        assertTrue(typeBindings.equals(identicalBindings));

        JavaType[] differentJavaTypes = new JavaType[]{};
        TypeBindings differentBindings = TypeBindings.create(ArrayList.class, differentJavaTypes);
        assertFalse(typeBindings.equals(differentBindings));
    }

    @Test
    public void testToString() {
        String expected = "<>";
        String actual = typeBindings.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTypeParameterArray() {
        JavaType[] typeParameters = typeBindings.typeParameterArray();
        assertEquals(0, typeParameters.length);
    }
}