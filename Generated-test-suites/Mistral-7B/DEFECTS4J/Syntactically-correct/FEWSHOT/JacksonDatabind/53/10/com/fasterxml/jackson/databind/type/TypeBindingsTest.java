package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("test"));
    }

    @Test
    public void testCreateBindingsForSingleTypeParameter() {
        Class<?> type = List.class;
        JavaType boundType = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.create(type, boundType);
        assertEquals(1, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(int.class, bindings.getBoundType(0).getRawType());
    }

    @Test
    public void testCreateBindingsForMultipleTypeParameters() {
        Class<?> type = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(type, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawType());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getRawType());
    }

    @Test
    public void testCreateBindingsForNullType() {
        try {
            TypeBindings.create(null, new JavaType[] {});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            TypeBindings.create(null, new JavaType[] { TypeFactory.defaultInstance().constructType(String.class) });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateBindingsForMismatchedTypeParameters() {
        Class<?> type = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(int.class);
        try {
            TypeBindings.create(type, keyType, valueType, new String[] { "test" });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateBindingsForTypeWithNoTypeParameters() {
        Class<?> type = Integer.class;
        TypeBindings bindings = TypeBindings.create(type, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        Class<?> type = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(type, keyType, valueType);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(String.class, bindings.findBoundType("K").getRawType());
        assertEquals(Integer.class, bindings.findBoundType("V").getRawType());
        assertNull(bindings.findBoundType("test"));
    }

    @Test
    public void testHasUnboundVariable() {
        Class<?> type = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(type, keyType, valueType);
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));
        assertFalse(bindings.hasUnbound("test"));

        TypeBindings unboundBindings = bindings.withUnboundVariable("test");
        assertFalse(unboundBindings.hasUnbound("K"));
        assertFalse(unboundBindings.hasUnbound("V"));
        assertTrue(unboundBindings.hasUnbound("test"));
    }
}