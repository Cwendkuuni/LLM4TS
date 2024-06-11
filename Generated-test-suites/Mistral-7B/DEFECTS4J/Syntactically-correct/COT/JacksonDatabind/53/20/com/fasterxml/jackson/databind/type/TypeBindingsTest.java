package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeParamStash;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateBasic() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(int.class) };
        TypeBindings bindings = TypeBindings.create(listIntType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        JavaType integerType = bindings.getBoundType(0);
        assertNotNull(integerType);
        assertEquals(int.class, integerType.getRawClass());
    }

    @Test
    public void testCreateWithMultipleTypes() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(int.class),
                TypeFactory.defaultInstance().constructType(String.class)
        };
        TypeBindings bindings = TypeBindings.create(mapIntStringType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
    }

    @Test
    public void testCreateWithUnresolvedTypes() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType[] types = new JavaType[0];
        TypeBindings bindings = TypeBindings.create(mapIntStringType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertNull(bindings.getBoundType(0));
        assertNull(bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypes() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(int[].class),
                TypeFactory.defaultInstance().constructType(String.class)
        };
        TypeBindings.create(mapIntStringType, types);
    }

    @Test
    public void testCreateIfNeededBasic() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntType, typeArg1);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        JavaType integerType = bindings.getBoundType(0);
        assertNotNull(integerType);
        assertEquals(int.class, integerType.getRawClass());
    }

    @Test
    public void testCreateIfNeededWithNoTypes() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntType, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertFalse(bindings.hasUnbound("Integer"));
        assertFalse(bindings.hasUnbound("String"));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("Integer");
        assertTrue(bindingsWithUnbound.hasUnbound("Integer"));
        assertFalse(bindingsWithUnbound.hasUnbound("String"));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        assertEquals(int.class, bindings.findBoundType("Integer").getRawClass());
        assertEquals(String.class, bindings.findBoundType("String").getRawClass());
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType("Unbound"));
    }

    @Test
    public void testTypeParameterArray() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        JavaType[] types = bindings.typeParameterArray();
        assertArrayEquals(new JavaType[] { typeArg1, typeArg2 }, types);
    }

    @Test
    public void testToString() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        String expected = "<Map<java.lang.Integer, java.lang.String>>";
        assertEquals(expected, bindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg1Copy = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings1 = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        TypeBindings bindings2 = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        TypeBindings bindings3 = TypeBindings.create(mapIntStringType, typeArg1Copy, typeArg2);
        TypeBindings bindings4 = TypeBindings.create(mapIntStringType, typeArg1, typeArg1);
        assertEquals(bindings1, bindings1);
        assertEquals(bindings1, bindings2);
        assertNotEquals(bindings1, bindings3);
        assertNotEquals(bindings1, bindings4);
        List<JavaType> list1 = Arrays.asList(typeArg1, typeArg2);
        List<JavaType> list2 = new ArrayList<>(list1);
        TypeBindings bindings5 = TypeBindings.create(mapIntStringType, list1.toArray(new JavaType[0]));
        TypeBindings bindings6 = TypeBindings.create(mapIntStringType, list2.toArray(new JavaType[0]));
        assertEquals(bindings5, bindings6);
    }

    @Test
    public void testIterateTypes() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<Integer, String>().getClass();
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(int.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapIntStringType, typeArg1, typeArg2);
        Iterator<JavaType> iterator = bindings.getTypeParameters().iterator();
        JavaType type1 = iterator.next();
        JavaType type2 = iterator.next();
        assertEquals(typeArg1, type1);
        assertEquals(typeArg2, type2);
    }
}