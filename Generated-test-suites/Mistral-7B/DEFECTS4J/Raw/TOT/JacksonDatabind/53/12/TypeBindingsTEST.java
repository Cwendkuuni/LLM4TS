package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals("", empty.toString());
        assertEquals(0, empty.size());
        assertFalse(empty.hasUnbound(""));
    }

    @Test
    public void testCreateForSingleTypeArg() {
        JavaType javaType = new SimpleType("java.util.ArrayList", List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(ArrayList.class, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertEquals("[Ljava.lang.Integer;", bindings.toString());
    }

    @Test
    public void testCreateForTwoTypeArgs() {
        JavaType javaType1 = new SimpleType("java.util.ArrayList", List.class, Integer.class);
        JavaType javaType2 = new SimpleType("java.util.HashMap", Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(HashMap.class, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(javaType2, bindings.getBoundType(1));
        assertEquals("java.util.Map<java.lang.Integer, java.lang.String>", bindings.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArgs() {
        JavaType javaType = new SimpleType("java.util.ArrayList", List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(ArrayList.class, javaType, javaType);
        assertEquals(2, bindings.size());
        // should throw an exception
        bindings = TypeBindings.create(ArrayList.class, javaType, javaType, javaType);
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType javaType = new SimpleType("java.util.ArrayList", List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertEquals("java.util.ArrayList<java.lang.Integer>", bindings.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeededWithInvalidNumberOfTypeArgs() {
        JavaType javaType = new SimpleType("java.util.ArrayList", List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, javaType, javaType);
        assertEquals(1, bindings.size());
        // should throw an exception
        bindings = TypeBindings.createIfNeeded(ArrayList.class, javaType, javaType, javaType);
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType1 = new SimpleType("java.util.ArrayList", List.class, Integer.class);
        JavaType javaType2 = new SimpleType("java.util.HashMap", Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(HashMap.class, javaType1, javaType2);
        assertEquals(javaType1, bindings.findBoundType("java.util.ArrayList"));
        assertNull(bindings.findBoundType("java.util.LinkedHashMap"));
    }

    @Test
    public void testWithUnboundVariable() {
        String unboundVariable = "unbound";
        TypeBindings bindings = new TypeBindings(new String[]{"T"}, new JavaType[]{Object.class}, new String[]{unboundVariable});
        TypeBindings newBindings = bindings.withUnboundVariable(unboundVariable);
        assertEquals(2, newBindings.size());
        assertEquals("T", newBindings.getBoundName(0));
        assertEquals(Object.class, newBindings.getBoundType(0));
        assertEquals(unboundVariable, newBindings.getBoundName(1));
        assertNull(newBindings.getBoundType(1));
    }
}