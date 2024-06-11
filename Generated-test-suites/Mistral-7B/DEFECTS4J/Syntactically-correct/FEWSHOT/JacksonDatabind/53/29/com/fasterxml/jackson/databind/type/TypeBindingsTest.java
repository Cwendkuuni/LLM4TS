package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateBindingsForSingleType() {
        Class<?> clazz = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(clazz.getTypeParameters()[0].getTypeName());
        TypeBindings bindings = TypeBindings.create(clazz, javaType);

        JavaType boundType = bindings.findBoundType("E");
        assertNotNull(boundType);
        assertEquals(javaType, boundType);

        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(1, typeParameters.size());
        assertEquals(javaType, typeParameters.get(0));
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateBindingsForTwoTypes() {
        Class<?> clazz = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(clazz.getTypeParameters()[0].getTypeName());
        JavaType valueType = TypeFactory.defaultInstance()
                .constructType(clazz.getTypeParameters()[1].getTypeName());
        TypeBindings bindings = TypeBindings.create(clazz, keyType, valueType);

        JavaType keyBoundType = bindings.findBoundType("K");
        assertNotNull(keyBoundType);
        assertEquals(keyType, keyBoundType);

        JavaType valueBoundType = bindings.findBoundType("V");
        assertNotNull(valueBoundType);
        assertEquals(valueType, valueBoundType);

        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals(valueType, bindings.getBoundType(1));
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertEquals(keyType, typeParameters.get(0));
        assertEquals(valueType, typeParameters.get(1));
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));
    }

    @Test
    public void testCreateBindingsForTwoTypesWithUnboundVariable() {
        Class<?> clazz = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(clazz.getTypeParameters()[0].getTypeName());
        JavaType valueType = TypeFactory.defaultInstance()
                .constructType(clazz.getTypeParameters()[1].getTypeName());
        TypeBindings bindings = TypeBindings.create(clazz, keyType, valueType);

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("W");

        JavaType keyBoundType = bindingsWithUnboundVariable.findBoundType("K");
        assertNotNull(keyBoundType);
        assertEquals(keyType, keyBoundType);

        JavaType valueBoundType = bindingsWithUnboundVariable.findBoundType("V");
        assertNotNull(valueBoundType);
        assertEquals(valueType, valueBoundType);

        JavaType unboundVariable = bindingsWithUnboundVariable.findBoundType("W");
        assertNull(unboundVariable);

        assertEquals(3, bindingsWithUnboundVariable.size());
        assertEquals("K", bindingsWithUnboundVariable.getBoundName(0));
        assertEquals("V", bindingsWithUnboundVariable.getBoundName(1));
        assertEquals("W", bindingsWithUnboundVariable.getBoundName(2));
        assertEquals(keyType, bindingsWithUnboundVariable.getBoundType(0));
        assertEquals(valueType, bindingsWithUnboundVariable.getBoundType(1));
        assertEquals(null, bindingsWithUnboundVariable.getBoundType(2));
        List<JavaType> typeParameters = bindingsWithUnboundVariable.getTypeParameters();
        assertEquals(3, typeParameters.size());
        assertEquals(keyType, typeParameters.get(0));
        assertEquals(valueType, typeParameters.get(1));
        assertNotNull(typeParameters.get(2));
        assertTrue(bindingsWithUnboundVariable.hasUnbound("W"));
    }

    @Test
    public void testToString() {
        Class<List<Integer>> clazz = new ArrayList<>() {}.getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(clazz.getTypeParameters()[0].getTypeName());
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        String toString = bindings.toString();
        String expectedToString = "<List<Integer>>";
        assertEquals(expectedToString, toString);
    }
}