package com.example; // Replace this with the actual package name

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterStash;
import com.fasterxml.jackson.databind.type.TypeVariableWrapper;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("test"));
    }

    @Test
    public void testCreateWithTypeArguments() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, Arrays.asList(String.class));
        assertEquals(1, bindings.size());
        JavaType boundType = bindings.findBoundType("0");
        assertNotNull(boundType);
        assertEquals(String.class, boundType.getRawClass());
    }

    @Test
    public void testCreateWithNullTypeArguments() {
        Class<List> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        JavaType typeArg = mock(JavaType.class);
        when(typeArg.getRawClass()).thenReturn(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, typeArg);
        assertEquals(1, bindings.size());
        JavaType boundType = bindings.findBoundType("0");
        assertNotNull(boundType);
        assertEquals(String.class, boundType.getRawClass());
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getTypeParameterized(String.class, List.class.getTypeParameterized(Integer.class));
        JavaType typeArg1 = mock(JavaType.class);
        JavaType typeArg2 = mock(JavaType.class);
        when(typeArg1.getRawClass()).thenReturn(String.class);
        when(typeArg2.getRawClass()).thenReturn(List.class.getTypeParameterized(Integer.class).getComponentType());
        TypeBindings bindings = TypeBindings.create(mapClass, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        JavaType keyBoundType = bindings.findBoundType("0");
        assertNotNull(keyBoundType);
        assertEquals(String.class, keyBoundType.getRawClass());
        JavaType valueBoundType = bindings.findBoundType("1");
        assertNotNull(valueBoundType);
        assertEquals(List.class.getTypeParameterized(Integer.class).getComponentType(), valueBoundType.getRawClass());
    }

    @Test
    public void testCreateWithNoTypeArguments() {
        Class<Object> objectClass = Object.class;
        TypeBindings bindings = TypeBindings.create(objectClass, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithOneTypeArgument() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        JavaType typeArg = mock(JavaType.class);
        when(typeArg.getRawClass()).thenReturn(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfStringClass, typeArg);
        assertEquals(1, bindings.size());
        JavaType boundType = bindings.findBoundType("0");
        assertNotNull(boundType);
        assertEquals(String.class, boundType.getRawClass());
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getTypeParameterized(String.class, List.class.getTypeParameterized(Integer.class));
        JavaType typeArg1 = mock(JavaType.class);
        JavaType typeArg2 = mock(JavaType.class);
        when(typeArg1.getRawClass()).thenReturn(String.class);
        when(typeArg2.getRawClass()).thenReturn(List.class.getTypeParameterized(Integer.class).getComponentType());
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        JavaType keyBoundType = bindings.findBoundType("0");
        assertNotNull(keyBoundType);
        assertEquals(String.class, keyBoundType.getRawClass());
        JavaType valueBoundType = bindings.findBoundType("1");
        assertNotNull(valueBoundType);
        assertEquals(List.class.getTypeParameterized(Integer.class).getComponentType(), valueBoundType.getRawClass());
    }

    @Test
    public void testCreateIfNeededWithNoTypeArguments() {
        Class<Object> objectClass = Object.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(objectClass, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        JavaType typeArg = mock(JavaType.class);
        when(typeArg.getRawClass()).thenReturn(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, typeArg);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("test");
        String[] unboundVariables = bindingsWithUnboundVariable.getUnboundVariables();
        assertArrayEquals(new String[] {"test"}, unboundVariables);
    }

    @Test
    public void testFindBoundTypeWithBoundType() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        JavaType stringType = mock(JavaType.class);
        when(stringType.getRawClass()).thenReturn(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, stringType);
        JavaType boundType = bindings.findBoundType("0");
        assertEquals(String.class, boundType.getRawClass());
    }

    @Test
    public void testFindBoundTypeWithUnboundType() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        TypeVariable<String> typeVariable = mock(TypeVariable.class);
        TypeParameterStash.TypeVariableWrapper wrapper = new TypeParameterStash.TypeVariableWrapper(typeVariable);
        when(typeVariable.getName()).thenReturn("test");
        JavaType typeArg = mock(JavaType.class);
        when(typeArg.unwrap(TypeVariableWrapper.class)).thenReturn(wrapper);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, typeArg);
        assertNull(bindings.findBoundType("0"));
    }

    @Test
    public void testFindBoundTypeWithNull() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNull(bindings.findBoundType("test"));
    }

    @Test
    public void testEquals() {
        Class<List<String>> listOfStringClass1 = List.class.getTypeParameterized(String.class);
        JavaType stringType1 = mock(JavaType.class);
        when(stringType1.getRawClass()).thenReturn(String.class);
        TypeBindings bindings1 = TypeBindings.create(listOfStringClass1, stringType1);

        Class<List<String>> listOfStringClass2 = List.class.getTypeParameterized(String.class);
        JavaType stringType2 = mock(JavaType.class);
        when(stringType2.getRawClass()).thenReturn(String.class);
        TypeBindings bindings2 = TypeBindings.create(listOfStringClass2, stringType2);

        assertEquals(bindings1, bindings1);
        assertEquals(bindings1, bindings2);
        assertEquals(bindings2, bindings1);
    }

    @Test
    public void testHashCode() {
        Class<List<String>> listOfStringClass = List.class.getTypeParameterized(String.class);
        JavaType stringType = mock(JavaType.class);
        when(stringType.getRawClass()).thenReturn(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, stringType);
        int hashCode1 = bindings.hashCode();

        Class<List<String>> listOfStringClass2 = List.class.getTypeParameterized(String.class);
        JavaType stringType2 = mock(JavaType.class);
        when(stringType2.getRawClass()).thenReturn(String.class);
        TypeBindings bindings2 = TypeBindings.create(listOfStringClass2, stringType2);

        assertEquals(hashCode1, bindings2.hashCode());
    }

    @Test
    public void testToString() {
        Class<List<List<String>>> listOfListOfStringClass = List.class.getTypeParameterized(List.class.getTypeParameterized(String.class));
        JavaType stringType = mock(JavaType.class);
        JavaType listType = mock(JavaType.class);
        when(stringType.getRawClass()).thenReturn(String.class);
        when(listType.getRawClass()).thenReturn(List.class.getTypeParameterized(String.class).getComponentType());
        TypeBindings bindings = TypeBindings.create(listOfListOfStringClass, stringType, listType);

        String expected = "<List<List<String>>>";
        assertEquals(expected, bindings.toString());
    }
}