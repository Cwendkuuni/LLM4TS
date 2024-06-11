package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyTypeBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("TestType"));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testSingleTypeParameter() {
        Class<List<String>> listOfStrings = new ArrayList<List<String>>() {{ add(null); }}.get(0).getClass();
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(listOfStrings, javaType);

        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertNull(bindings.findBoundType("OtherType"));
    }

    @Test
    public void testMultipleTypeParameters() {
        Class<Map<String, List<Integer>>> mapOfStringListInt = new ArrayList<Map<String, List<Integer>>>() {{ add(null); }}.get(0).getClass();
        JavaType mapJavaType = new TypeReference<Map<String, List<Integer>>>() {}.getType();
        JavaType listJavaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(mapOfStringListInt, mapJavaType, listJavaType);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getRawClass());
        assertNull(bindings.findBoundType("OtherType"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeParameters() {
        Class<List<String>> listOfStrings = new ArrayList<List<String>>() {{ add(null); }}.get(0).getClass();
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType[] wrongTypes = new JavaType[]{javaType, javaType, javaType};
        try {
            TypeBindings bindings = TypeBindings.create(listOfStrings, wrongTypes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeParameterNames() {
        Class<Map<String, List<Integer>>> mapOfStringListInt = new ArrayList<Map<String, List<Integer>>>() {{ add(null); }}.get(0).getClass();
        JavaType mapJavaType = new TypeReference<Map<String, List<Integer>>>() {}.getType();
        JavaType listJavaType = new TypeReference<List<Integer>>() {}.getType();
        String wrongName = "java.lang.Object";
        try {
            TypeBindings bindings = TypeBindings.create(mapOfStringListInt, mapJavaType, listJavaType, wrongName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}