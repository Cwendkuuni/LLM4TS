package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateSimpleBindings() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        assertEquals(2, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals("Integer", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(0).getRawClass());
        assertEquals(Integer.class, bindings.getBoundType(1).getRawClass());

        JavaType javaType2 = new TypeReference<Map<String, Integer>>() {}.getType();
        TypeBindings bindings2 = TypeBindings.create(javaType2.getRawType(), javaType2);
        assertEquals(2, bindings2.size());
        assertEquals("Map", bindings2.getBoundName(0));
        assertEquals("String", bindings2.getBoundName(1));
        assertEquals("Integer", bindings2.getBoundName(2));
        assertEquals(Map.class, bindings2.getBoundType(0).getRawClass());
        assertEquals(String.class, bindings2.getBoundType(1).getRawClass());
        assertEquals(Integer.class, bindings2.getBoundType(2).getRawClass());
    }

    @Test
    public void testCreateBindingsWithUnboundVariable() {
        JavaType javaType = new TypeReference<List<List<Integer>>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        TypeBindings bindings2 = bindings.withUnboundVariable("T");
        assertEquals(3, bindings2.size());
        assertTrue(bindings2.hasUnbound("T"));
        assertNull(bindings2.findBoundType("Integer"));
        assertNull(bindings2.findBoundType("List"));
        assertNull(bindings2.findBoundType("T"));
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType = new TypeReference<List<List<Integer>>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        assertEquals("List", bindings.getBoundName(0));
        assertEquals("List", bindings.getBoundName(1));
        assertEquals("Integer", bindings.getBoundName(2));
        JavaType intType = Integer.TYPE;
        JavaType listType = new TypeReference<List<Integer>>() {}.getType();
        JavaType listOfListType = new TypeReference<List<List<Integer>>>() {}.getType();

        assertEquals(intType, bindings.findBoundType("Integer"));
        assertEquals(listType, bindings.findBoundType("List"));
        assertEquals(listOfListType, bindings.findBoundType("List<List<Integer>>"));
        assertNull(bindings.findBoundType("String"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings2 = TypeBindings.create(javaType.getRawType(), javaType);
        assertFalse(bindings2.isEmpty());
    }

    @Test
    public void testSize() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        assertEquals(2, bindings.size());
        JavaType javaType2 = new TypeReference<Map<String, Integer>>() {}.getType();
        TypeBindings bindings2 = TypeBindings.create(javaType2.getRawType(), javaType2);
        assertEquals(3, bindings2.size());
    }

    @Test
    public void testToString() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        assertEquals("<List<Integer>>", bindings.toString());
    }

    @Test
    public void testHashCode() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        JavaType javaType2 = new TypeReference<List<List<Integer>>>() {}.getType();
        TypeBindings bindings2 = TypeBindings.create(javaType2.getRawType(), javaType2);
        List<JavaType> list1 = Arrays.asList(Integer.TYPE, javaType);
        List<JavaType> list2 = Arrays.asList(Integer.TYPE, javaType);
        assertEquals(bindings.hashCode(), list1.hashCode());
        assertEquals(bindings2.hashCode(), list2.hashCode());
    }

    @Test
    public void testEquals() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);
        JavaType javaType2 = new TypeReference<List<List<Integer>>>() {}.getType();
        TypeBindings bindings2 = TypeBindings.create(javaType2.getRawType(), javaType2);
        List<JavaType> list1 = Arrays.asList(Integer.TYPE, javaType);
        List<JavaType> list2 = Arrays.asList(Integer.TYPE, javaType);
        List<JavaType> list3 = Arrays.asList(Integer.TYPE, javaType, javaType);
        List<JavaType> list4 = Arrays.asList(Integer.TYPE, javaType2);

        assertTrue(bindings.equals(list1));
        assertTrue(bindings.equals(bindings));
        assertFalse(bindings.equals(null));
        assertFalse(bindings.equals(""));
        assertFalse(bindings.equals(bindings2));
        assertFalse(bindings.equals(list3));
        assertFalse(bindings.equals(list4));
    }
}