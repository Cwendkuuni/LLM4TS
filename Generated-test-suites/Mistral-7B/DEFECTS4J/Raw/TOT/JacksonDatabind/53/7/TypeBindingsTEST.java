package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("unused"));
    }

    @Test
    public void testSingleTypeParameter() {
        JavaType javaType = JavaType.construct(List.class, int.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("unused"));
    }

    @Test
    public void testTwoTypeParameters() {
        JavaType listInt = JavaType.construct(List.class, int.class);
        JavaType mapIntInt = JavaType.construct(Map.class, int.class, int.class);
        TypeBindings bindings = TypeBindings.create(Map.class, listInt, mapIntInt);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(listInt, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(mapIntInt, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("unused"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeParameters() {
        JavaType listInt = JavaType.construct(List.class, int.class);
        JavaType mapIntInt = JavaType.construct(Map.class, int.class, int.class);
        TypeBindings bindings = TypeBindings.create(Map.class, listInt, mapIntInt, new JavaType[] {});
    }

    @Test
    public void testFindBoundType() {
        JavaType listInt = JavaType.construct(List.class, int.class);
        JavaType mapIntInt = JavaType.construct(Map.class, int.class, int.class);
        JavaType mapStringInt = JavaType.construct(Map.class, String.class, int.class);
        TypeBindings bindings = TypeBindings.create(Map.class, listInt, mapIntInt, mapStringInt);
        assertEquals("java.lang.Integer", bindings.findBoundType("Integer").getName());
        assertEquals("java.lang.String", bindings.findBoundType("String").getName());
        assertNull(bindings.findBoundType("unused"));
    }

    @Test
    public void testUnboundVariable() {
        JavaType listInt = JavaType.construct(List.class, int.class);
        TypeBindings bindings = TypeBindings.create(List.class, listInt);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("T");
        assertEquals("T", bindingsWithUnboundVariable.findBoundType("T").getName());
        assertNull(bindingsWithUnboundVariable.findBoundType("java.lang.Integer"));
    }

    @Test
    public void testToString() {
        JavaType listInt = JavaType.construct(List.class, int.class);
        TypeBindings bindings = TypeBindings.create(List.class, listInt);
        String expectedTypeString = "<java.lang.Integer>";
        assertEquals(expectedTypeString, bindings.toString());
    }
}