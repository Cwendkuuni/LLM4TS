package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("T"));
    }

    @Test
    public void testSingletonBindings() {
        Class<List<String>> erasedType = ArrayList.class;
        TypeBindings bindings = TypeBindings.create(erasedType, JavaType.constructParametricType(ArrayList.class, String.class));
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getType());
        assertNull(bindings.findBoundType("S"));
    }

    @Test
    public void testMultipleBindings() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        TypeBindings bindings = TypeBindings.create(erasedType, JavaType.constructParametricType(HashMap.class, String.class, ArrayList.class));
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getType());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(JavaType.constructParametricType(ArrayList.class, Integer.class), bindings.getBoundType(1));
        assertNull(bindings.findBoundType("S"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedBindings() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        TypeBindings bindings = TypeBindings.create(erasedType, JavaType.constructParametricType(HashMap.class, String.class, Integer.class));
        fail("Should not reach this point");
    }

    @Test
    public void testUnboundVariable() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        TypeBindings bindings = TypeBindings.create(erasedType, JavaType.constructParametricType(HashMap.class, String.class, JavaType.constructRawType(List.class)));
        assertTrue(bindings.hasUnbound("V"));
        TypeBindings withUnbound = bindings.withUnboundVariable("V");
        assertNotSame(bindings, withUnbound);
        assertTrue(withUnbound.hasUnbound("V"));
        assertNull(withUnbound.findBoundType("V"));
    }
}