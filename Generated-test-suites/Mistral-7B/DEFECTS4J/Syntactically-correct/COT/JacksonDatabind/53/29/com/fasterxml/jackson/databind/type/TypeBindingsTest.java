package com.example.test; // Update this package name with your project's package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class TypeBindingsTest {

    private Class<?> erasedType;
    private JavaType[] expectedTypes;
    private TypeBindings typeBindings;

    @Parameters(name = "{index}: create({0}, {1})")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {List.class, new JavaType[]{}},
                {ArrayList.class, new JavaType[]{}},
                {Map.class, new JavaType[]{}},
                {HashMap.class, new JavaType[]{}},
                {LinkedHashMap.class, new JavaType[]{}},
                {ArrayList.class, Collections.singletonList(Integer.TYPE)},
                {HashMap.class, new JavaType[]{Integer.TYPE, String.class}},
                {LinkedHashMap.class, new JavaType[]{Integer.TYPE, String.class}},
                {MyClassWithGenericTypes.class, new JavaType[]{Integer.TYPE, String.class}},
                {MyClassWithGenericTypes.class, new JavaType[]{}}
        });
    }

    public TypeBindingsTest(Class<?> erasedType, JavaType[] expectedTypes) {
        this.erasedType = erasedType;
        this.expectedTypes = expectedTypes;
    }

    @Test
    public void testCreate() {
        typeBindings = TypeBindings.create(erasedType, expectedTypes);
        assertArrayEquals(expectedTypes, typeBindings.getTypeParameters().toArray(new JavaType[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIllegalArgumentException() {
        typeBindings = TypeBindings.create(erasedType, new JavaType[]{JavaType.constructParametricType(String[].class, Integer.TYPE)});
    }

    @Test
    public void testCreateIfNeeded() {
        typeBindings = TypeBindings.createIfNeeded(erasedType, expectedTypes);
        assertArrayEquals(expectedTypes, typeBindings.getTypeParameters().toArray(new JavaType[0]));
    }

    @Test
    public void testCreateIfNeededEmpty() {
        typeBindings = TypeBindings.createIfNeeded(erasedType, null);
        assertTrue(TypeBindings.emptyBindings().equals(typeBindings));
    }

    @Test
    public void testWithUnboundVariable() {
        String newVariableName = "newVar";
        typeBindings = typeBindings.withUnboundVariable(newVariableName);
        assertTrue(Arrays.asList(typeBindings._unboundVariables).contains(newVariableName));
    }

    @Test
    public void testFindBoundType() {
        String name = getTypeVariableName(0, erasedType);
        JavaType type = typeBindings.findBoundType(name);
        assertEquals(expectedTypes[0], type);
    }

    @Test
    public void testFindBoundTypeNotFound() {
        String name = "nonExistentVariableName";
        assertNull(typeBindings.findBoundType(name));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(TypeBindings.emptyBindings().isEmpty());
        assertFalse(typeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(expectedTypes.length, typeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        int index = 0;
        String name = getTypeVariableName(index, erasedType);
        assertEquals(name, typeBindings.getBoundName(index));
    }

    @Test
    public void testGetBoundType() {
        int index = 0;
        JavaType expectedType = expectedTypes[index];
        JavaType type = typeBindings.getBoundType(index);
        assertEquals(expectedType, type);
    }

    @Test
    public void testToString() {
        String expectedString = "<" + Arrays.toString(expectedTypes) + ">";
        assertEquals(expectedString, typeBindings.toString());
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = Arrays.hashCode(expectedTypes);
        assertEquals(expectedHashCode, typeBindings.hashCode());
    }

    @Test
    public void testEquals() {
        TypeBindings anotherTypeBindings = TypeBindings.create(erasedType, expectedTypes);
        assertTrue(typeBindings.equals(anotherTypeBindings));
        assertFalse(typeBindings.equals(new Object()));
    }

    private static String getTypeVariableName(int index, Class<?> clazz) {
        TypeVariable<?>[] declaredTypeVariables = clazz.getTypeParameters();
        return declaredTypeVariables[index].getName();
    }

    // Add test cases for other methods if necessary

    
    // Add any setup code or initialization logic here if needed

    
}