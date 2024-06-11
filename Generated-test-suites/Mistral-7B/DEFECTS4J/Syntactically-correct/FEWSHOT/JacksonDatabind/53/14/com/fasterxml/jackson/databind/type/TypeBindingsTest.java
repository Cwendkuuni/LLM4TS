package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreate() {
        Class<List<String>> clazz = List.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String name = vars[0].getName();
        JavaType typeArg1 = String.class;

        TypeBindings bindings = TypeBindings.create(clazz, typeArg1);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals(name, bindings.getBoundName(0));
        assertSame(typeArg1, bindings.getBoundType(0));

        // checking getTypeParameters() method
        List<JavaType> typeParams = bindings.getTypeParameters();
        assertSame(typeArg1, typeParams.get(0));
    }

    @Test
    public void testCreateWithNull() {
        Class<List<String>> clazz = List.class;

        try {
            TypeBindings.create(clazz, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class List<String> with 1 type parameter: class expects 1", e.getMessage());
        }
    }

    @Test
    public void testCreateWithNotEnoughTypes() {
        Class<List<String>> clazz = List.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String name = vars[0].getName();
        JavaType typeArg1 = String.class;
        JavaType typeArg2 = Integer.class;

        try {
            TypeBindings.create(clazz, new JavaType[] { typeArg1 });
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class List<String> with 1 type parameter: class expects 2", e.getMessage());
        }
    }

    @Test
    public void testCreateWithExtraTypes() {
        Class<List<String>> clazz = List.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String name = vars[0].getName();
        JavaType typeArg1 = String.class;
        JavaType typeArg2 = Integer.class;

        try {
            TypeBindings.create(clazz, new JavaType[] { typeArg1, typeArg2, Integer.class });
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class List<String> with 2 type parameter"
                    + "(s)s: class expects 2", e.getMessage());
        }
    }

    @Test
    public void testCreateWithEmptyList() {
        Class<List<String>> clazz = List.class;

        TypeBindings bindings = TypeBindings.create(clazz, Collections.emptyList().toArray(new JavaType[0]));
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithNullTypes() {
        Class<List<String>> clazz = List.class;

        try {
            TypeBindings.create(clazz, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class List<String> with 0 type parameter", e.getMessage());
        }
    }

    @Test
    public void testCreateWithNoTypeParameters() {
        Class<String> clazz = String.class;

        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<String>>> clazz = Map.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String kTypeName = vars[0].getName();
        String vTypeName = vars[1].getName();
        JavaType kType = String.class;
        JavaType vType = List.class;

        JavaType[] types = new JavaType[] { kType, vType };
        TypeBindings bindings = TypeBindings.create(clazz, types);

        assertEquals(kType, bindings.findBoundType(kTypeName));
        assertEquals(vType, bindings.findBoundType(vTypeName));
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType("unknown"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<String>>> clazz = Map.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String kTypeName = vars[0].getName();
        String vTypeName = vars[1].getName();
        JavaType kType = String.class;
        JavaType vType = List.class;

        TypeBindings bindings = TypeBindings.create(clazz, new JavaType[] { kType, vType });

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable(kTypeName);
        JavaType unboundType = Object.class;
        assertEquals(new String[] { kTypeName }, bindingsWithUnboundVariable._unboundVariables);
        assertEquals(unboundType, bindingsWithUnboundVariable.findBoundType(kTypeName));
    }

    @Test
    public void testToString() {
        Class<Map<String, List<String>>> clazz = Map.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String kTypeName = vars[0].getName();
        String vTypeName = vars[1].getName();
        JavaType kType = String.class;
        JavaType vType = List.class;

        JavaType[] types = new JavaType[] { kType, vType };
        TypeBindings bindings = TypeBindings.create(clazz, types);

        String expected = "<Map<String,List<String>>>";
        assertEquals(expected, bindings.toString());
    }

    @Test
    public void testEquals() {
        Class<Map<String, List<String>>> clazz = Map.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String kTypeName = vars[0].getName();
        String vTypeName = vars[1].getName();
        JavaType kType = String.class;
        JavaType vType = List.class;

        JavaType[] types = new JavaType[] { kType, vType };
        JavaType[] types2 = new JavaType[] { kType, vType };
        TypeBindings bindings1 = TypeBindings.create(clazz, types);
        TypeBindings bindings2 = TypeBindings.create(clazz, types2);

        TypeBindings bindings3 = TypeBindings.create(clazz, new JavaType[] { kType, vType, Integer.class });
        assertNotEquals(bindings1, bindings3);

        TypeBindings bindings4 = new TypeBindings(new String[0], new JavaType[0], null);
        assertNotEquals(bindings1, bindings4);

        assertEquals(bindings1, bindings2);
    }

    @Test
    public void testHashCode() {
        Class<Map<String, List<String>>> clazz = Map.class;
        TypeVariable<?>[] vars = clazz.getTypeParameters();
        String kTypeName = vars[0].getName();
        String vTypeName = vars[1].getName();
        JavaType kType = String.class;
        JavaType vType = List.class;

        JavaType[] types = new JavaType[] { kType, vType };
        JavaType[] types2 = new JavaType[] { kType, vType };
        TypeBindings bindings1 = TypeBindings.create(clazz, types);
        TypeBindings bindings2 = TypeBindings.create(clazz, types2);

        TypeBindings bindings3 = TypeBindings.create(clazz, new JavaType[] { kType, vType, Integer.class });
        int hashCode1 = bindings1.hashCode();
        int hashCode2 = bindings2.hashCode();
        int hashCode3 = bindings3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}