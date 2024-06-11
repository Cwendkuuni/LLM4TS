package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsForSingleType() {
        JavaType typeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(String[].class, typeArg);
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("T0"));
        assertNull(bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("T0"));
        assertEquals(typeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForMultipleTypes() {
        JavaType typeArg1 = new SimpleType(Integer.class);
        JavaType typeArg2 = new SimpleType(String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, typeArg1, typeArg2);
        assertEquals(3, bindings.size());
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("K"));
        assertNull(bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("T"));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals(typeArg2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForList() {
        JavaType typeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, typeArg);
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("T0"));
        assertNull(bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("T0"));
        assertEquals(typeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForAbstractList() {
        JavaType typeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(AbstractList.class, typeArg);
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("E"));
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("E"));
        assertNull(bindings.findBoundType("T"));
        assertEquals(typeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForCollection() {
        JavaType typeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(Collection.class, typeArg);
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("E"));
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("E"));
        assertNull(bindings.findBoundType("T"));
        assertEquals(typeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForIterable() {
        JavaType typeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(Iterable.class, typeArg);
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("T0"));
        assertNull(bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("T0"));
        assertEquals(typeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForMap() {
        JavaType keyTypeArg = new SimpleType(String.class);
        JavaType valueTypeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, keyTypeArg, valueTypeArg);
        assertEquals(3, bindings.size());
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("K"));
        assertNull(bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("T"));
        assertEquals(keyTypeArg, bindings.getBoundType(0));
        assertEquals(valueTypeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForHashMap() {
        JavaType keyTypeArg = new SimpleType(String.class);
        JavaType valueTypeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(HashMap.class, keyTypeArg, valueTypeArg);
        assertEquals(3, bindings.size());
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("K"));
        assertNull(bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("T"));
        assertEquals(keyTypeArg, bindings.getBoundType(0));
        assertEquals(valueTypeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForLinkedHashMap() {
        JavaType keyTypeArg = new SimpleType(String.class);
        JavaType valueTypeArg = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(LinkedHashMap.class, keyTypeArg, valueTypeArg);
        assertEquals(3, bindings.size());
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("K"));
        assertNull(bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("T"));
        assertEquals(keyTypeArg, bindings.getBoundType(0));
        assertEquals(valueTypeArg, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForInvalidNumberOfTypes() {
        JavaType typeArg1 = new SimpleType(Integer.class);
        JavaType typeArg2 = new SimpleType(String.class);
        TypeBindings.create(Map.class, typeArg1, typeArg2, new SimpleType(Double.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForInvalidTypes() {
        JavaType typeArg1 = new SimpleType(Integer.class);
        JavaType typeArg2 = new SimpleType(String.class);
        TypeBindings.create(Map.class, new SimpleType(Integer.class), typeArg2, new SimpleType(Double.class));
    }

    @Test
    public void testCreateBindingsForArray() {
        JavaType arrayType = new ArrayType(Integer[].class);
        TypeBindings bindings = TypeBindings.create(Integer[].class, arrayType);
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("T0"));
        assertNull(bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("T0"));
        assertEquals(arrayType, bindings.getBoundType(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(1).getComponentType());
    }

    @Test
    public void testCreateBindingsForVarargs() {
        JavaType arrayType = new ArrayType(Integer[].class);
        TypeBindings bindings = TypeBindings.create(Integer[].class, new GenericArrayTypeImpl(Integer[].class));
        assertEquals(3, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("T0"));
        assertTrue(bindings.hasUnbound("T1"));
        assertNull(bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("T0"));
        assertNull(bindings.findBoundType("T1"));
        assertEquals(arrayType, bindings.getBoundType(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(1).getComponentType());
    }

    @Test
    public void testGetUnboundVariable() {
        JavaType typeArg1 = new SimpleType(Integer.class);
        JavaType typeArg2 = new SimpleType(String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, typeArg1, typeArg2);
        assertEquals("K", bindings.withUnboundVariable("K").getBoundName(0));
        assertEquals("V", bindings.withUnboundVariable("V").getBoundName(0));
        assertEquals("T", bindings.withUnboundVariable("T").getBoundName(0));
    }
}