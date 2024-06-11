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
    public void testCreateBindingsWithOneTypeParameter() {
        Class<?> erasedType = List.class;
        TypeBindings bindings = TypeBindings.create(erasedType, TypeBindings.TypeParamStash.paramsFor1(erasedType));
        assertEquals(1, bindings.size());
        String name = bindings.getBoundName(0);
        JavaType type = bindings.getBoundType(0);
        assertEquals("E", name);
        assertEquals(List.class, type.getRawType());
        assertEquals(List.class, type.getTypeArguments()[0].getType());
    }

    @Test
    public void testCreateBindingsWithTwoTypeParameters() {
        Class<?> erasedType = Map.class;
        TypeVariable<?>[] types = TypeBindings.TypeParamStash.paramsFor2(erasedType);
        JavaType[] javaTypes = new JavaType[2];
        javaTypes[0] = List.class;
        javaTypes[1] = String.class;
        TypeBindings bindings = TypeBindings.create(erasedType, javaTypes);
        assertEquals(2, bindings.size());
        String name1 = bindings.getBoundName(0);
        String name2 = bindings.getBoundName(1);
        JavaType type1 = bindings.getBoundType(0);
        JavaType type2 = bindings.getBoundType(1);
        assertEquals("K", name1);
        assertEquals(List.class, type1.getRawType());
        assertEquals(javaTypes[0], type1.getTypeArguments()[0].getType());
        assertEquals("V", name2);
        assertEquals(javaTypes[1], type2.getTypeArguments()[0].getType());
    }

    @Test
    public void testCreateBindingsWithUnresolvedTypeParameters() {
        Class<?> erasedType = Map.class;
        TypeVariable<?>[] types = TypeBindings.TypeParamStash.paramsFor2(erasedType);
        JavaType[] javaTypes = new JavaType[2];
        javaTypes[0] = JavaType.constructUnresolvedType(List.class, javaTypes[1].getTypeArguments());
        javaTypes[1] = String.class;
        try {
            TypeBindings bindings = TypeBindings.create(erasedType, javaTypes);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class Map<K,V> with 2 type parameter"
                    + "s: class expects 2, but found 1", e.getMessage());
        }
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        TypeVariable<?>[] types = TypeBindings.TypeParamStash.paramsFor2(erasedType);
        JavaType[] javaTypes = new JavaType[2];
        javaTypes[0] = List.class;
        javaTypes[1] = String.class;
        TypeBindings bindings = TypeBindings.create(erasedType, javaTypes);
        JavaType keyType = bindings.findBoundType("K");
        JavaType valueType = bindings.findBoundType("V");
        assertEquals(List.class, keyType.getRawType());
        assertEquals(String.class, valueType.getRawType());
        assertNull(bindings.findBoundType("UnknownType"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> erasedType = List.class;
        TypeBindings bindings = TypeBindings.create(erasedType, TypeBindings.TypeParamStash.paramsFor1(erasedType));
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        String unboundVariableName = bindingsWithUnboundVariable.getBoundName(2);
        assertEquals("X", unboundVariableName);
    }

}