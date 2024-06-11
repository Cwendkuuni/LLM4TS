package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateTypeBindingsForList() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Object", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateTypeBindingsForMap() {
        TypeBindings bindings = TypeBindings.create(Map.class, String.class, Integer.class);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Object", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.lang.Object", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1));
    }

    @Test
    public void testCreateTypeBindingsForAbstractCollection() {
        TypeBindings bindings = TypeBindings.create(AbstractList.class);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
    }

    @Test
    public void testCreateTypeBindingsForHashMap() {
        TypeBindings bindings = TypeBindings.create(HashMap.class, String.class, Integer.class);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Object", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(Class.class, bindings.getBoundType(1).getRawClass());
        assertEquals(ClassWrapper.TYPE, bindings.getBoundType(1).getRawType());
        assertEquals(Integer.class, bindings.getBoundType(1).getComponentType());
    }

    @Test
    public void testCreateTypeBindingsForArrayList() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, String.class);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateTypeBindingsForIterable() {
        TypeBindings bindings = TypeBindings.create(Iterable.class, String[].class);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(String[].class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateTypeBindingsForLinkedHashMap() {
        TypeBindings bindings = TypeBindings.create(LinkedHashMap.class, String.class, Integer.class);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Object", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(Class.class, bindings.getBoundType(1).getRawClass());
        assertEquals(ClassWrapper.TYPE, bindings.getBoundType(1).getRawType());
        assertEquals(Integer.class, bindings.getBoundType(1).getComponentType());
    }

    @Test
    public void testFindBoundTypeForStringList() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        assertEquals(String.class, bindings.findBoundType("java.lang.String"));
    }

    @Test
    public void testFindBoundTypeForIntegerMap() {
        TypeBindings bindings = TypeBindings.create(Map.class, String.class, Integer.class);
        assertEquals(Integer.class, bindings.findBoundType("java.lang.Integer"));
    }

    @Test
    public void testFindBoundTypeForUnknownType() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        assertNull(bindings.findBoundType("java.util.Date"));
    }

    @Test
    public void testFindBoundTypeForUnknownTypeWithUnboundVariable()
    {
        TypeBindings bindings = bindingsWithUnboundVariable("T", List.class, String.class);
        assertEquals(String.class, bindings.findBoundType("java.lang.String"));
        assertEquals(ResolvedType.constructParametricType(List.class, String.class), bindings.findBoundType("java.util.List"));
    }

    @Test
    public void testFindBoundTypeForUnknownTypeWithUnboundVariable2()
    {
        TypeBindings bindings = bindingsWithUnboundVariable("T", Map.class, String.class, Integer.class);
        assertEquals(String.class, bindings.findBoundType("java.lang.String"));
        assertEquals(ResolvedType.constructParametricType(Map.class, String.class, Integer.class), bindings.findBoundType("java.util.Map"));
    }

    @Test
    public void testFindBoundTypeForUnknownTypeWithUnboundVariable3()
    {
        TypeBindings bindings = bindingsWithUnboundVariable("T", Map.class, String[].class);
        assertEquals(String[].class, bindings.findBoundType("java.lang.String[]"));
        assertEquals(ResolvedType.constructParametricType(Map.class, String[].class), bindings.findBoundType("java.util.Map"));
    }

    @Test
    public void testFindBoundTypeForUnknownTypeWithUnboundVariable4()
    {
        TypeBindings bindings = bindingsWithUnboundVariable("K", Map.class, String.class, Integer.class);
        assertEquals(String.class, bindings.findBoundType("java.lang.String"));
        assertEquals(ResolvedType.constructParametricType(Map.class, String.class, Integer.class), bindings.findBoundType("java.util.Map"));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(ResolvedType.constructUnresolvedType(String.class), bindings.getBoundType(1));
    }

    @Test
    public void testHasUnboundVariable() {
        TypeBindings bindings = bindingsWithUnboundVariable("T", Map.class, String.class, Integer.class);
        assertTrue(bindings.hasUnbound("T"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(TypeBindings.emptyBindings().isEmpty());
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        String stringRepresentation = bindings.toString();
        assertTrue(stringRepresentation.startsWith("<"));
        assertTrue(stringRepresentation.endsWith(">"));
    }

    private static TypeBindings bindingsWithUnboundVariable(String unboundVar, Class<?>... types) {
        JavaType[] javaTypes = new TypeFactory().constructTypes(types);
        TypeBindings bindings = new TypeBindings(TypeBindings.create(types[0], javaTypes)._names, javaTypes, new String[]{ unboundVar });
        return bindings;
    }

}