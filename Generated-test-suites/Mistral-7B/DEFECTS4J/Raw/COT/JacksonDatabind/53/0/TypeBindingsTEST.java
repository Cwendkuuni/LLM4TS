package com.example; // Replace this package name with the actual package name

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateEmptyListBindings() {
        Class<?> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, Collections.emptyList());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateSingleTypeArgumentBindings() {
        Class<?> intListClass = ArrayList.class;
        TypeVariable<?> intVariable = intListClass.getTypeParameters()[0];
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.create(intListClass, intType);
        assertFalse(bindings.isEmpty());
        assertSame(intVariable.getName(), bindings.getBoundName(0));
        assertSame(intType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateTwoTypeArgumentBindings() {
        Class<?> mapClass = HashMap.class;
        TypeVariable<?> keyVariable = mapClass.getTypeParameters()[0];
        TypeVariable<?> valueVariable = mapClass.getTypeParameters()[1];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        assertFalse(bindings.isEmpty());
        assertSame(keyVariable.getName(), bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertSame(valueVariable.getName(), bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        Class<?> mapClass = HashMap.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        JavaType foundType = bindings.findBoundType("String");
        assertSame(keyType, foundType);
        foundType = bindings.findBoundType("Integer");
        assertSame(valueType, foundType);
        foundType = bindings.findBoundType("Object");
        assertNull(foundType);
    }

    @Test
    public void testCreateIfNeededWithNullTypes() {
        Class<?> listClass = List.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, intType);
        assertFalse(bindings.isEmpty());
        assertSame(intType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithEmptyTypes() {
        Class<?> mapClass = HashMap.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneTypeArgument() {
        Class<?> intListClass = ArrayList.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(intListClass, intType);
        assertFalse(bindings.isEmpty());
        assertSame(intType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        Class<?> mapClass = HashMap.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, stringType, integerType);
        assertFalse(bindings.isEmpty());
        assertSame(stringType, bindings.getBoundType(0));
        assertSame(integerType, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNamesAndTypes() {
        Class<?> listClass = ArrayList.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        String[] names = {"String", "Integer"};
        String[] boundNames = {"String", "Int"};
        TypeBindings bindings = new TypeBindings(names, new JavaType[] { intType, intType }, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNamesAndTypesLength() {
        Class<?> listClass = ArrayList.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        String[] names = {"String"};
        JavaType[] types = {intType, intType, intType};
        TypeBindings bindings = new TypeBindings(names, types, null);
    }

    
}