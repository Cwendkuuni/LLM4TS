package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("Foo"));
    }

    @Test
    public void testCreateBindingsForEmptyType() {
        Class<List<Number>> emptyListClass = new ArrayList<Number>().getClass();
        TypeBindings bindings = TypeBindings.create(emptyListClass, null);
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("Foo"));
    }

    @Test
    public void testCreateBindingsForTypeWithOneTypeParameter() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        TypeVariable<Integer> intTypeVar = listClass.getTypeParameters()[0];
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(listClass, intType);

        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertSame(intType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("Foo"));
    }

    @Test
    public void testCreateBindingsForTypeWithTwoTypeParameters() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<String, List<Integer>>().getClass();
        TypeVariable<String> keyTypeVar = mapClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVar = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("Foo"));

        JavaType keyType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType2 = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));
        TypeBindings bindings2 = TypeBindings.create(mapClass, keyType2, valueType2);
        assertEquals(2, bindings2.size());
        assertEquals("java.lang.String", bindings2.getBoundName(0));
        assertSame(keyType2, bindings2.getBoundType(0));
        assertEquals("java.util.List", bindings2.getBoundName(1));
        assertSame(valueType2, bindings2.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForTypeWithNoTypeParameters() {
        Class<Object> objClass = Object.class;
        TypeBindings bindings = TypeBindings.create(objClass, null);
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("Foo"));
    }

    @Test
    public void testCreateBindingsForTypeWithOneTypeParameterAndUnbound() {
        Class<List<Foo>> listClass = new ArrayList<Foo>().getClass();
        TypeVariable<Foo> intTypeVar = listClass.getTypeParameters()[0];
        JavaType intType = TypeFactory.defaultInstance().constructType(Foo.class);
        TypeBindings bindings = TypeBindings.create(listClass, intType);
        TypeBindings bindings2 = bindings.withUnboundVariable("Foo");
        assertSame(bindings, bindings2);
        assertEquals(1, bindings2.size());
        assertEquals("Foo", bindings2.getBoundName(0));
        assertNull(bindings2.getBoundType(0));
        assertNull(bindings2.findBoundType(null));
        assertNull(bindings2.findBoundType(""));
        assertNull(bindings2.findBoundType("Bar"));

        // Check that unbound variable is resolved properly
        JavaType fooType = TypeFactory.defaultInstance().constructType(Foo.class);
        JavaType bindings2Types[] = bindings2.typeParameterArray();
        JavaType fooType2 = bindings2Types[0];
        assertSame(fooType, fooType2);
    }

    @Test
    public void testCreateBindingsForTypeWithTwoTypeParametersAndUnbound() {
        Class<Map<String, List<Foo>>> mapClass = new HashMap<String, List<Foo>>().getClass();
        TypeVariable<String> keyTypeVar = mapClass.getTypeParameters()[0];
        TypeVariable<List<Foo>> valueTypeVar = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Foo.class));
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        TypeBindings bindings2
                = bindings.withUnboundVariable("java.util.List<Foo>");
        assertEquals(2, bindings2.size());
        assertEquals("java.lang.String", bindings2.getBoundName(0));
        assertSame(keyType, bindings2.getBoundType(0));
        assertEquals("java.util.List<Foo>", bindings2.getBoundName(1));
        assertNull(bindings2.getBoundType(1));
        assertNull(bindings2.findBoundType(null));
        assertNull(bindings2.findBoundType(""));
        assertNull(bindings2.findBoundType("Bar"));

        // Check that unbound variable is resolved properly
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Foo.class));
        JavaType bindings2Types[] = bindings2.typeParameterArray();
        JavaType listType2 = bindings2Types[1];
        assertSame(listType, listType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForTypeWithMismatchedNumberOfTypeParameters() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<String, List<Integer>>().getClass();
        TypeVariable<String> keyTypeVar = mapClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVar = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType, new JavaType[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForTypeWithMismatchedTypes() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<String, List<Integer>>().getClass();
        TypeVariable<String> keyTypeVar = mapClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVar = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Number.class);
        try {
            TypeBindings.create(mapClass, keyType, valueType);
        } catch (IllegalArgumentException iae) {
            // Expected
            throw iae;
        }
    }

    @Test
    public void testHashCodeAndEquals() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<String, List<Integer>>().getClass();
        TypeVariable<String> keyTypeVar = mapClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVar = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));

        // Create first instance
        TypeBindings bindings1 = TypeBindings.create(mapClass, keyType, valueType);

        // Create second instance, using same types, but with different order
        JavaType valueType2 = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings2 = TypeBindings.create(mapClass, keyType2, valueType2);

        assertTrue(bindings1.equals(bindings1));
        assertTrue(bindings1.equals(bindings1));
        assertTrue(bindings1.equals(bindings2));
        assertTrue(bindings2.equals(bindings1));
        assertTrue(bindings2.equals(bindings2));
        assertEquals(bindings1.hashCode(), bindings1.hashCode());
        assertEquals(bindings1.hashCode(), bindings2.hashCode());

        // Create third instance, with different types
        JavaType keyType3 = TypeFactory.defaultInstance().constructType(Number.class);
        JavaType valueType3 = TypeFactory.defaultInstance().constructType(Number.class);
        TypeBindings bindings3 = TypeBindings.create(mapClass, keyType3, valueType3);

        assertFalse(bindings1.equals(bindings3));
        assertFalse(bindings2.equals(bindings3));
        assertFalse(bindings3.equals(bindings1));
        assertFalse(bindings3.equals(bindings2));

        // Test hashCode() consistency
        TypeBindings bindings4 = new TypeBindings(new String[] { "Foo" }, new JavaType[] { TypeFactory.defaultInstance()
                .constructType(String.class) }, new String[] { "Bar" });
        JavaType keyType4 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType4 = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));
        TypeBindings bindings5 = new TypeBindings(new String[] { "Bar" }, new JavaType[] { TypeFactory.defaultInstance()
                .constructType(String.class) }, new String[] { "Foo" });
        assertEquals(bindings4.hashCode(), bindings5.hashCode());
        assertFalse(bindings4.equals(bindings5));
    }

    @Test
    public void testToString() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<String, List<Integer>>().getClass();
        TypeVariable<String> keyTypeVar = mapClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVar = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class, TypeFactory.defaultInstance()
                .constructType(Integer.TYPE));
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        String toString = bindings.toString();
        assertTrue(toString.startsWith("<"));
        assertTrue(toString.endsWith(">"));
        assertTrue(toString.contains("java.lang.String"));
        assertTrue(toString.contains("java.util.List<java.lang.Integer>"));
        assertTrue(toString.contains("Map"));
    }

    private static class Foo {
        // Empty class
    }
}