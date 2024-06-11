package com.fasterxml.jackson.databind.type; // adjust the package as needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeVariableStash;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreate() {
        JavaType javaType = JavaType.construct(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType);
        assertEquals(List.class, bindings.getClass());
        assertEquals(javaType, bindings.getBoundType(0));
        assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType javaType1 = JavaType.construct(List.class, Integer.class);
        JavaType javaType2 = JavaType.construct(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType1, javaType2);
        assertFalse(bindings.hasUnbound(""));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");
        assertTrue(bindingsWithUnbound.hasUnbound("T"));
        assertFalse(bindingsWithUnbound.hasUnbound(""));
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType1 = JavaType.construct(List.class, Integer.class);
        JavaType javaType2 = JavaType.construct(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType1, javaType2);
        assertSame(javaType1, bindings.findBoundType("Integer"));
        assertSame(javaType2, bindings.findBoundType("String"));
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType javaType = JavaType.construct(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, javaType);
        assertEquals(javaType, bindings.getTypeParameters().get(0));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithUnboundVariables() {
        JavaType javaType1 = JavaType.construct(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, javaType1);
        assertSame(javaType1, bindings.getTypeParameters().get(0));
        assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateIfNeededWithEmptyTypeList() {
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testToString() {
        JavaType javaType1 = JavaType.construct(List.class, Integer.class);
        JavaType javaType2 = JavaType.construct(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType1, javaType2);
        String expected = "<List<Integer>, List<String>>";
        assertEquals(expected, bindings.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = JavaType.construct(List.class, Integer.class);
        JavaType javaType2 = JavaType.construct(List.class, Integer.class);
        JavaType javaType3 = JavaType.construct(List.class, String.class);
        TypeBindings bindings1 = TypeBindings.create(List.class, javaType1);
        TypeBindings bindings2 = TypeBindings.create(List.class, javaType1);
        TypeBindings bindings3 = TypeBindings.create(List.class, javaType2);
        TypeBindings bindings4 = TypeBindings.create(List.class, javaType3);

        assertTrue(bindings1.equals(bindings1));
        assertTrue(bindings1.equals(bindings2));
        assertFalse(bindings1.equals(bindings3));
        assertFalse(bindings1.equals(bindings4));
    }

    @Test
    public void testHashCode() {
        JavaType javaType1 = JavaType.construct(List.class, Integer.class);
        JavaType javaType2 = JavaType.construct(List.class, Integer.class);
        JavaType javaType3 = JavaType.construct(List.class, String.class);
        TypeBindings bindings1 = TypeBindings.create(List.class, javaType1);
        TypeBindings bindings2 = TypeBindings.create(List.class, javaType1);
        TypeBindings bindings3 = TypeBindings.create(List.class, javaType2);
        TypeBindings bindings4 = TypeBindings.create(List.class, javaType3);

        int hashCode1 = bindings1.hashCode();
        int hashCode2 = bindings2.hashCode();
        int hashCode3 = bindings3.hashCode();
        int hashCode4 = bindings4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
    }

    @Test
    public void testTypeParameterArray() {
        JavaType javaType1 = JavaType.construct(List.class, Integer.class);
        JavaType javaType2 = JavaType.construct(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType1, javaType2);
        JavaType[] expected = { javaType1, javaType2 };
        assertArrayEquals(expected, bindings.typeParameterArray());
    }

    @Test
    public void testCreateWithNullTypes() {
        List<JavaType> types = Collections.emptyList();
        try {
            TypeBindings.create(Object.class, types.toArray(new JavaType[types.size()]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateWithNullClass() {
        JavaType[] types = new JavaType[2];
        types[0] = JavaType.construct(Integer.class);
        types[1] = JavaType.construct(String.class);
        try {
            TypeBindings.create(null, types);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateWithEmptyClassArray() {
        try {
            TypeBindings.create(Object.class, new JavaType[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateWithMismatchedTypeAndNameCounts() {
        JavaType javaType = JavaType.construct(List.class, Integer.class);
        try {
            TypeBindings.create(List.class, javaType, JavaType.construct(Map.class, String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateWithNullTypeVariableArray() {
        Class<?> clazz = LinkedHashMap.class;
        try {
            TypeBindings.create(clazz, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateWithNullUnboundVariables() {
        JavaType javaType = JavaType.construct(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType, null);
        assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateWithUnboundVariableWithNullName() {
        JavaType javaType = JavaType.construct(List.class, Integer.class);
        try {
            TypeBindings bindings = TypeBindings.create(List.class, javaType, null);
            bindings.withUnboundVariable(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testCreateWithUnboundVariableWithEmptyNameArray() {
        JavaType javaType = JavaType.construct(List.class, Integer.class);
        try {
            TypeBindings bindings = TypeBindings.create(List.class, javaType, new String[0]);
            bindings.withUnboundVariable(new String[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testTypeVariableStash() {
        JavaType javaType = JavaType.construct(ArrayList.class, Integer.class);
        TypeVariable<?>[] variables = TypeParamStash.paramsFor1(javaType.getRawClass());
        assertArrayEquals(new String[] { "E" }, variables);
    }

    @Test
    public void testTypeVariableStashForMap() {
        JavaType javaType = JavaType.construct(HashMap.class, Integer.class, String.class);
        TypeVariable<?>[] variables = TypeParamStash.paramsFor2(javaType.getRawClass());
        assertArrayEquals(new String[] { "K", "V" }, variables);
    }

    
}