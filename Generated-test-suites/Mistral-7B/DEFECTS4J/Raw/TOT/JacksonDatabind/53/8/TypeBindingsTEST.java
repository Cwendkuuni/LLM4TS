package com.example.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals("[]", empty.toString());
        assertNull(empty.findBoundType("T"));
        assertEquals(0, empty.size());
    }

    @Test
    public void testSingleTypeBinding() {
        Class<List<String>> listClass = new ArrayList<>() {}.getClass();
        JavaType javaType = TypeBindings.create(listClass, Collections.singletonList(String.class));
        TypeBindings bindings = TypeBindings.create(listClass, javaType);
        assertEquals("[Ljava.lang.String;", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.lang.String[]", bindings.toString());
    }

    @Test
    public void testTwoTypeBindings() {
        Class<Map<String, Integer>> mapClass = new HashMap<>() {}.getClass();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType javaType = TypeBindings.create(mapClass, keyType, valueType);
        TypeBindings bindings = TypeBindings.create(mapClass, javaType);
        assertEquals("Ljava.util.Map;Ljava/lang/String;Ljava/lang/Integer;", bindings.getTypeParameters().get(0).toString());
        assertEquals("Ljava.lang.String;", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("Ljava.lang.Integer;", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1));
        assertEquals("{Ljava.lang.String;Ljava.lang.Integer;}", bindings.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeParameters() {
        Class<List<String>> listClass = new ArrayList<>() {}.getClass();
        JavaType javaType = TypeBindings.create(listClass, Collections.singletonList(String.class));
        TypeBindings bindings = TypeBindings.create(listClass, javaType, Integer.class);
    }
}