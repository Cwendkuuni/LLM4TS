package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;

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
        Class<?> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(1, bindings.size());
        assertEquals("list", bindings.getBoundName(0));
        assertNull(bindings.findBoundType("size"));
    }

    @Test
    public void testCreateBindingsWithTwoTypeParameters() {
        Class<?> clazz = Map.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(2, bindings.size());
        assertEquals("keyType", bindings.getBoundName(0));
        assertEquals("valueType", bindings.getBoundName(1));
        assertNull(bindings.findBoundType("toString"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeParameters() {
        Class<?> clazz = Map.class;
        TypeBindings.create(clazz, new JavaType[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypeParameterNames() {
        Class<?> clazz = Map.class;
        TypeBindings.create(clazz, new JavaType[] { JavaType.constructRawType(String.class), JavaType.constructRawType(Integer.class) });
        TypeBindings bindings = TypeBindings.create(clazz, new JavaType[] { JavaType.constructRawType(Integer.class), JavaType.constructRawType(String.class) });
        assertNotEquals("keyType", bindings.getBoundName(0));
        assertNotEquals("valueType", bindings.getBoundName(1));
    }

    @Test
    public void testCreateWithTypeParameters() {
        Class<?> clazz = Map.class;
        List<JavaType> types = Arrays.asList(JavaType.constructRawType(String.class), JavaType.constructRawType(Integer.class));
        TypeBindings bindings = TypeBindings.create(clazz, types);
        assertEquals(2, bindings.size());
        assertEquals("keyType", bindings.getBoundName(0));
        assertEquals("valueType", bindings.getBoundName(1));
        assertNotNull(bindings.findBoundType("keyType"));
        assertNotNull(bindings.findBoundType("valueType"));
    }

    @Test
    public void testCreateWithTypeArguments() {
        Class<?> clazz = Map.class;
        TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
        JavaType[] types = new JavaType[2];
        types[0] = JavaType.constructTypeParameter(clazz, 0);
        types[1] = JavaType.constructTypeParameter(clazz, 1);
        TypeBindings bindings = TypeBindings.create(clazz, types);
        assertEquals(2, bindings.size());
        assertEquals("keyType", bindings.getBoundName(0));
        assertEquals("valueType", bindings.getBoundName(1));
        assertNotNull(bindings.findBoundType("keyType"));
        assertNotNull(bindings.findBoundType("valueType"));
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameter() {
        Class<?> clazz = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithTwoTypeParameters() {
        Class<?> clazz = Map.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());

        List<JavaType> types = Arrays.asList(JavaType.constructRawType(String.class), JavaType.constructRawType(Integer.class));
        bindings = TypeBindings.createIfNeeded(clazz, types.toArray(new JavaType[0]));
        assertEquals(2, bindings.size());
        assertEquals("keyType", bindings.getBoundName(0));
        assertEquals("valueType", bindings.getBoundName(1));
        assertNotNull(bindings.findBoundType("keyType"));
        assertNotNull(bindings.findBoundType("valueType"));
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameterAndTypeArgument() {
        Class<?> clazz = ArrayList.class;
        JavaType typeArgument = JavaType.constructRawType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, typeArgument);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertNotNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeParametersAndTypeArguments() {
        Class<?> clazz = LinkedHashMap.class;
        JavaType keyTypeArgument = JavaType.constructTypeParameter(clazz, 0);
        JavaType valueTypeArgument = JavaType.constructTypeParameter(clazz, 1);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, new JavaType[] { keyTypeArgument, valueTypeArgument });
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> clazz = Map.class;
        TypeBindings bindings = TypeBindings.create(clazz, new JavaType[0]);
        TypeBindings newBindings = bindings.withUnboundVariable("K");
        assertFalse(bindings.hasUnbound("K"));
        assertTrue(newBindings.hasUnbound("K"));
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = Map.class;
        JavaType keyTypeArgument = JavaType.constructTypeParameter(clazz, 0);
        JavaType valueTypeArgument = JavaType.constructTypeParameter(clazz, 1);
        TypeBindings bindings = TypeBindings.create(clazz, new JavaType[] { keyTypeArgument, valueTypeArgument });
        assertEquals(JavaType.constructType(clazz, "Map"), bindings.getTypeParameters().get(0).getRawClass());
        assertEquals(keyTypeArgument, bindings.findBoundType("K"));
        assertEquals(valueTypeArgument, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("toString"));
    }
}