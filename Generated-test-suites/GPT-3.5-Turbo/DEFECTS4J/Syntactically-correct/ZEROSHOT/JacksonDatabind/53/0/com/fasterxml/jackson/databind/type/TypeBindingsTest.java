package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;
import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {
    
    private TypeBindings typeBindings;

    @Before
    public void setUp() {
        String[] names = {"name1", "name2"};
        JavaType[] types = {new JavaType(), new JavaType()};
        typeBindings = new TypeBindings(names, types, null);
    }

    @Test
    public void testFindBoundType() {
        JavaType result = typeBindings.findBoundType("name1");
        assertEquals(new JavaType(), result);
    }

    @Test
    public void testSize() {
        int result = typeBindings.size();
        assertEquals(2, result);
    }

    @Test
    public void testGetBoundName() {
        String result = typeBindings.getBoundName(0);
        assertEquals("name1", result);
    }

    @Test
    public void testGetBoundType() {
        JavaType result = typeBindings.getBoundType(0);
        assertEquals(new JavaType(), result);
    }

    @Test
    public void testGetTypeParameters() {
        List<JavaType> result = typeBindings.getTypeParameters();
        List<JavaType> expectedResult = Arrays.asList(new JavaType(), new JavaType());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHasUnbound() {
        boolean result = typeBindings.hasUnbound("name1");
        assertFalse(result);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings result = typeBindings.withUnboundVariable("variable");
        String[] names = {"name1", "name2"};
        JavaType[] types = {new JavaType(), new JavaType()};
        TypeBindings expectedResult = new TypeBindings(names, types, new String[]{"variable"});
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHashCode() {
        int result = typeBindings.hashCode();
        assertEquals(2, result);
    }

    @Test
    public void testEquals() {
        TypeBindings obj = new TypeBindings(new String[]{"name1", "name2"}, new JavaType[]{new JavaType(), new JavaType()}, null);
        boolean result = typeBindings.equals(obj);
        assertTrue(result);
    }

    @Test
    public void testToString() {
        String result = typeBindings.toString();
        assertEquals("<>", result);
    }
}