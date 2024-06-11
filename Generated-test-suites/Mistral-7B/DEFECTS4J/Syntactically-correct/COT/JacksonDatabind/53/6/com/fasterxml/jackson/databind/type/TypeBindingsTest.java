package com.example.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(Collections.emptyList(), bindings.getTypeParameters());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateEmptyType() {
        Class<?> clazz = Object.class;
        TypeBindings bindings = TypeBindings.create(clazz, Collections.emptyList());
        assertTrue(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongNumberOfTypes() {
        Class<?> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, new JavaType[] { JavaType.valueOf(int.class), JavaType.valueOf(long.class), JavaType.valueOf(String.class) });
        fail("Should have thrown an exception");
    }

    @Test
    public void testCreateWithOneType() {
        Class<?> intListClass = List.class;
        TypeVariable<?> intTypeVariable = intListClass.getTypeParameters()[0];
        JavaType intType = JavaType.constructParametricType(intListClass, JavaType.constructRawType(int.class));
        TypeBindings bindings = TypeBindings.create(intListClass, new JavaType[] { intType });
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(Arrays.asList(intType), typeParameters);
        assertEquals("java.lang.Integer", typeParameters.get(0).getRawClass().getName());
        assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateWithTwoTypes() {
        Class<?> mapClass = Map.class;
        TypeVariable<?> keyTypeVariable = mapClass.getTypeParameters()[0];
        TypeVariable<?> valueTypeVariable = mapClass.getTypeParameters()[1];
        JavaType keyType = JavaType.constructParametricType(mapClass, JavaType.constructRawType(String.class), JavaType.constructRawType(String.class));
        JavaType valueType = JavaType.constructParametricType(mapClass, JavaType.constructRawType(Integer.class), JavaType.constructRawType(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, new JavaType[] { keyType, valueType });
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(Arrays.asList(keyType, valueType), typeParameters);
        assertEquals("java.lang.String", typeParameters.get(0).getRawClass().getName());
        assertEquals("java.lang.Integer", typeParameters.get(1).getRawClass().getName());
        assertFalse(bindings.hasUnbound(""));
        assertFalse(bindings.hasUnbound(""));

        TypeBindings bindingsWithUnboundKey = bindings.withUnboundVariable("key");
        assertTrue(bindingsWithUnboundKey.hasUnbound("key"));
        assertFalse(bindingsWithUnboundKey.hasUnbound("value"));

        TypeBindings bindingsWithUnboundValue = bindingsWithUnboundKey.withUnboundVariable("value");
        assertTrue(bindingsWithUnboundKey.hasUnbound("key"));
        assertTrue(bindingsWithUnboundKey.hasUnbound("value"));
    }

    
}