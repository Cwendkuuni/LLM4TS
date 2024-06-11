
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeParameter() {
        List<JavaType> typeList = new ArrayList<>();
        typeList.add(JavaTypeBuilder.create().withClass(Integer.class).build());
        TypeBindings bindings = TypeBindings.create(ArrayList.class, typeList);
        assertEquals(ArrayList.class.getTypeParameters()[0].getName(), bindings.getBoundName(0));
        assertEquals(typeList.get(0), bindings.getBoundType(0));
        assertEquals(typeList, bindings.getTypeParameters());
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        List<JavaType> typeList = new ArrayList<>();
        typeList.add(JavaTypeBuilder.create().withClass(Integer.class).build());
        typeList.add(JavaTypeBuilder.create().withClass(String.class).build());
        TypeBindings bindings = TypeBindings.create(HashMap.class, typeList);
        assertEquals(HashMap.class.getTypeParameters()[0].getName(), bindings.getBoundName(0));
        assertEquals(HashMap.class.getTypeParameters()[1].getName(), bindings.getBoundName(1));
        assertEquals(typeList.get(0), bindings.getBoundType(0));
        assertEquals(typeList.get(1), bindings.getBoundType(1));
        assertEquals(typeList, bindings.getTypeParameters());
    }

    @Test
    public void testCreateIfNeededWithoutTypeParameters() {
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameter() {
        JavaType type = JavaTypeBuilder.create().withClass(HashMap.class).build();
        TypeBindings bindings = TypeBindings.createIfNeeded(HashMap.class, type);
        assertEquals(HashMap.class.getTypeParameters()[0].getName(), bindings.getBoundName(0));
        assertEquals(type, bindings.getBoundType(0));
        assertEquals(type, bindings.getTypeParameters().get(0));
    }

    @Test
    public void testCreateIfNeededWithMultipleTypeParameters() {
        JavaType type1 = JavaTypeBuilder.create().withClass(Integer.class).build();
        JavaType type2 = JavaTypeBuilder.create().withClass(String.class).build();
        List<JavaType> typeList = new ArrayList<>();
        typeList.add(type1);
        typeList.add(type2);
        TypeBindings bindings = TypeBindings.createIfNeeded(LinkedHashMap.class, typeList);
        assertEquals(LinkedHashMap.class.getTypeParameters()[0].getName(), bindings.getBoundName(0));
        assertEquals(LinkedHashMap.class.getTypeParameters()[1].getName(), bindings.getBoundName(1));
        assertEquals(type1, bindings.getBoundType(0));
        assertEquals(type2, bindings.getBoundType(1));
        assertEquals(typeList, bindings.getTypeParameters());
    }

    @Test
    public void testWithUnboundVariable() {
        String name = "T";
        TypeBindings bindings = TypeBindings.create(ArrayList.class, null);
        TypeBindings newBindings = bindings.withUnboundVariable(name);
        assertTrue(bindings.isEmpty());
        assertFalse(newBindings.isEmpty());
        assertEquals(0, bindings.size());
        assertEquals(1, newBindings.size());
        assertEquals(name, newBindings.getBoundName(0));
    }
}
