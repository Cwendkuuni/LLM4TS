package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreate() {
        Class<List<String>> clazz = List.class;
        TypeVariable<String> typeVariable = clazz.getTypeParameters()[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));

        TypeBindings bindings2 = TypeBindings.create(clazz, null);
        assertEquals(0, bindings2.size());
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<String>> clazz = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));

        TypeBindings bindings2 = TypeBindings.createIfNeeded(clazz, null);
        assertEquals(0, bindings2.size());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(javaType2, bindings.getBoundType(1));

        TypeBindings bindings2 = bindings.withUnboundVariable("T");
        assertEquals(3, bindings2.size());
        assertNotNull(bindings2.findBoundType("T"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArguments() {
        Class<List<String>> clazz = List.class;
        JavaType[] types = new JavaType[2];
        TypeBindings bindings = TypeBindings.create(clazz, types);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeParameterNames() {
        Class<List<String>> clazz = List.class;
        TypeVariable<String>[] typeVariables = clazz.getTypeParameters();
        String[] names = new String[2];
        TypeBindings bindings = new TypeBindings(names, null, null);
        try {
            bindings = TypeBindings.create(clazz, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindBoundType() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertEquals("java.lang.String", bindings.findBoundType("E").getName());
        assertEquals(javaType1, bindings.findBoundType("E"));

        assertEquals("java.lang.Integer", bindings.findBoundType("K").getName());
        assertEquals(javaType2, bindings.findBoundType("K"));

        assertNull(bindings.findBoundType("notExistent"));
    }

    @Test
    public void testHasUnbound() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertFalse(bindings.hasUnbound("E"));
        assertFalse(bindings.hasUnbound("K"));

        TypeBindings bindings2 = bindings.withUnboundVariable("T");
        assertTrue(bindings2.hasUnbound("T"));
    }

    @Test
    public void testToString() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertEquals("<MyList<java.lang.String, java.lang.Integer>>", bindings.toString());

        TypeBindings bindings2 = bindings.withUnboundVariable("T");
        assertEquals("<MyList<java.lang.String, java.lang.Integer, T>>", bindings2.toString());
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());

        Class<List<String>> clazz = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings2 = TypeBindings.create(clazz, javaType);
        assertFalse(bindings2.isEmpty());
    }

    @Test
    public void testSize() {
        Class<List<String>> clazz = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertEquals(1, bindings.size());
    }

    @Test
    public void testGetBoundName() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertEquals("E", bindings.getBoundName(0));
        assertEquals("K", bindings.getBoundName(1));

        TypeBindings bindings2 = bindings.withUnboundVariable("T");
        assertEquals("T", bindings2.getBoundName(2));
    }

    @Test
    public void testGetBoundType() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals(javaType2, bindings.getBoundType(1));

        TypeBindings bindings2 = bindings.withUnboundVariable("T");
        assertNull(bindings2.getBoundType(2));
    }

    @Test
    public void testGetTypeParameters() {
        Class<MyList<String, Integer>> clazz = MyList.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        List<JavaType> javaTypes = bindings.getTypeParameters();
        assertEquals(2, javaTypes.size());
        assertEquals(javaType1, javaTypes.get(0));
        assertEquals(javaType2, javaTypes.get(1));
    }

    private static class MyList<E, K> {
    }
}