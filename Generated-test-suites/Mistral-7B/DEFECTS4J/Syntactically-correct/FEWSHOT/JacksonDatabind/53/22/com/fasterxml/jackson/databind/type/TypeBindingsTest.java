package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("foo"));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithTypes() {
        Class<List<String>> listClass = List.class.getTypeParameter(0).getRawType();
        JavaType[] types = new JavaType[] {JavaType.constructParametricType(ArrayList.class, String.class)};
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateWithSingleTypeArgument() {
        Class<Map<String, Number>> mapClass = Map.class.getTypeParameter(0).getType();
        JavaType typeArg = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(mapClass, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertNull(bindings.findBoundType("V"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongNumberOfTypeArguments() {
        Class<Map<String, Number>> mapClass = Map.class.getTypeParameter(0).getType();
        JavaType[] types = new JavaType[] {JavaType.constructParametricType(ArrayList.class, String.class),
                JavaType.constructParametricType(ArrayList.class, Integer.class)};
        TypeBindings.create(mapClass, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongNumberOfTypeParameters() {
        Class<Map<String, Number>> mapClass = Map.class.getTypeParameter(0).getType();
        JavaType[] types = new JavaType[] {JavaType.constructParametricType(ArrayList.class, String.class)};
        TypeBindings.create(mapClass, types);
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<Map<String, Number>> mapClass = Map.class.getTypeParameter(1).getType();
        JavaType typeArg = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(mapClass, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("V", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertTrue(bindings.hasUnbound("K"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, Number>> mapClass = Map.class.getTypeParameter(1).getType();
        JavaType typeArg = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(mapClass, typeArg);
        TypeBindings boundBindings = bindings.withUnboundVariable("K");
        assertEquals("K", boundBindings.getBoundName(1));
        assertNull(boundBindings.getBoundType(1));
    }
}