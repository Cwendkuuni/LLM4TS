package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("test"));
    }

    @Test
    public void testCreateBindingsForSingleTypeParameter() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterTypes()[0];
        JavaType javaType = new SimpleType(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, javaType);

        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertSame(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForMultipleTypeParameters() {
        Class<Map<String, List<String>>> mapOfListOfStringClass = Map.class.getTypeParameterTypes()[0];
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(List.class.getTypeParameterTypes()[0]);
        TypeVariable<?>[] vars = mapOfListOfStringClass.getTypeParameters();
        String[] names = new String[vars.length];
        for (int i = 0; i < vars.length; ++i) {
            names[i] = vars[i].getName();
        }
        TypeBindings bindings = new TypeBindings(names, new JavaType[] { keyType, valueType }, null);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForCollectionType() {
        JavaType javaType = new SimpleType(List.class.getTypeParameterTypes()[0]);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, javaType);

        assertEquals(1, bindings.size());
        assertEquals("java.lang.Object", bindings.getBoundName(0));
        assertSame(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForMapType() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(List.class.getTypeParameterTypes()[0]);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, keyType, valueType);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<String>>> mapOfListOfStringClass = Map.class.getTypeParameterTypes()[0];
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(List.class.getTypeParameterTypes()[0]);
        TypeBindings bindings = new TypeBindings(new String[] { "K", "V" }, new JavaType[] { keyType, valueType }, null);

        assertEquals("K", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));

        assertEquals(keyType, bindings.findBoundType("K"));
        assertEquals(valueType, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("X"));
    }

    @Test
    public void testHasUnbound() {
        Class<Map<String, List<String>>> mapOfListOfStringClass = Map.class.getTypeParameterTypes()[0];
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(List.class.getTypeParameterTypes()[0]);
        TypeBindings bindings = new TypeBindings(new String[] { "K", "V" }, new JavaType[] { keyType, valueType }, null);

        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));
        assertFalse(bindings.hasUnbound("X"));

        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("X");
        assertTrue(bindingsWithUnbound.hasUnbound("X"));
        assertFalse(bindingsWithUnbound.hasUnbound("K"));
        assertFalse(bindingsWithUnbound.hasUnbound("V"));
    }

    @Test
    public void testCreateBindingsForAbstractCollectionType() {
        JavaType javaType = new SimpleType(AbstractList.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(AbstractList.class, javaType);

        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertSame(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForAbstractMapType() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(AbstractList.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(AbstractMap.class, keyType, valueType);

        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForConcreteCollectionType() {
        JavaType javaType = new SimpleType(ArrayList.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, javaType);

        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertSame(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForConcreteMapType() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(ArrayList.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(HashMap.class, keyType, valueType);

        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testToString() {
        JavaType javaType = new SimpleType(List.class.getTypeParameterTypes()[0]);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, javaType);

        assertEquals("<java.util.List>", bindings.toString());
    }
}