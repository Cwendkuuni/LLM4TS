package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForSingleTypeArgument() {
        JavaType javaType = new SimpleType("java.util.List", List.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForTwoTypeArguments() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(javaType2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForClassWithNoTypeParameters() {
        JavaType javaType = new SimpleType("java.lang.String", String.class);
        TypeBindings bindings = TypeBindings.create(String.class, javaType);
        assertTrue(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForClassWithMismatchedTypeParameters() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.lang.Integer", Integer.class);
        TypeBindings.create(Map.class, javaType1, javaType2);
    }

    @Test
    public void testCreateBindingsForClassWithNoExplicitTypeArguments() {
        JavaType javaType = new SimpleType("java.util.ArrayList", ArrayList.class);
        TypeBindings bindings = TypeBindings.create(ArrayList.class, javaType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForCollectionTypeWithNoExplicitTypeArguments() {
        JavaType javaType = new SimpleType("java.util.Collection", Collection.class);
        TypeBindings bindings = TypeBindings.create(Collection.class, javaType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForMapTypeWithNoExplicitTypeArguments() {
        JavaType keyType = new SimpleType("java.lang.String", String.class);
        JavaType valueType = new SimpleType("java.lang.Integer", Integer.class);
        JavaType javaType = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForClassWithExplicitTypeArguments() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(javaType2, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertEquals("K", bindings.findBoundType("K").getName());
        assertEquals("V", bindings.findBoundType("V").getName());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindingsWithTypes = TypeBindings.create(Map.class, javaType1, javaType2);
        assertFalse(bindingsWithTypes.isEmpty());
    }

    @Test
    public void testGetBoundName() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        TypeBindings bindingsEmpty = TypeBindings.emptyBindings();
        assertNull(bindingsEmpty.getBoundName(0));
    }

    @Test
    public void testGetBoundType() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals(javaType2, bindings.getBoundType(1));
        TypeBindings bindingsEmpty = TypeBindings.emptyBindings();
        assertNull(bindingsEmpty.getBoundType(0));
    }

    @Test
    public void testGetTypeParameters() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        List<JavaType> expectedTypes = Arrays.asList(javaType1, javaType2);
        List<JavaType> actualTypes = bindings.getTypeParameters();
        assertEquals(expectedTypes, actualTypes);
        TypeBindings bindingsEmpty = TypeBindings.emptyBindings();
        assertTrue(bindingsEmpty.getTypeParameters().isEmpty());
    }

    @Test
    public void testHasUnbound() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("U");
        assertTrue(bindingsWithUnbound.hasUnbound("U"));
        assertFalse(bindingsWithUnbound.hasUnbound("K"));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType javaType1 = new SimpleType("java.util.List", List.class);
        JavaType javaType2 = new SimpleType("java.util.Map", Map.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("U");
        assertEquals(3, bindingsWithUnbound.size());
        assertEquals("K", bindingsWithUnbound.getBoundName(0));
        assertEquals(javaType1, bindingsWithUnbound.getBoundType(0));
        assertEquals("V", bindingsWithUnbound.getBoundName(1));
        assertEquals(javaType2, bindingsWithUnbound.getBoundType(1));
        assertEquals("U", bindingsWithUnbound.getBoundName(2));
        assertNull(bindingsWithUnbound.getBoundType(2));
    }
}

class SimpleType {
    private final Class<?> clazz;
    private final JavaType javaType;

    public SimpleType(String className, Class<?> clazz) {
        this.clazz = clazz;
        this.javaType = TypeFactory.defaultInstance().constructType(clazz);
    }

    public SimpleType(String className, JavaType javaType) {
        this.javaType = javaType;
        this.clazz = javaType.getRawType();
    }

    public String getName() {
        return clazz.getName();
    }

    public JavaType getRawType() {
        return javaType;
    }
}