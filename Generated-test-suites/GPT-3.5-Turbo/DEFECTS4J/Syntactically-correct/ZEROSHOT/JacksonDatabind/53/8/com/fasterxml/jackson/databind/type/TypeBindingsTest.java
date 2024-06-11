package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertNotNull(typeBindings.findBoundType("T"));
        assertNull(typeBindings.findBoundType("X"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertFalse(typeBindings.isEmpty());

        typeBindings = new TypeBindings(null, null, null);
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertEquals(1, typeBindings.size());

        typeBindings = new TypeBindings(null, null, null);
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertEquals("T", typeBindings.getBoundName(0));
        assertNull(typeBindings.getBoundName(1));
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertNotNull(typeBindings.getBoundType(0));
        assertNull(typeBindings.getBoundType(1));
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertEquals(1, typeBindings.getTypeParameters().size());
        assertEquals(new JavaType(), typeBindings.getTypeParameters().get(0));
    }

    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()}, new String[] {"T"});
        assertTrue(typeBindings.hasUnbound("T"));
        assertFalse(typeBindings.hasUnbound("X"));
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertEquals("<T>", typeBindings.toString());

        typeBindings = new TypeBindings(null, null, null);
        assertEquals("<>", typeBindings.toString());
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings1 = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        TypeBindings typeBindings2 = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertEquals(typeBindings1, typeBindings2);

        typeBindings2 = new TypeBindings(new String[] {"X"}, new JavaType[] {new JavaType()});
        assertNotEquals(typeBindings1, typeBindings2);
    }

    @Test
    public void testHashCode() {
        TypeBindings typeBindings1 = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        TypeBindings typeBindings2 = new TypeBindings(new String[] {"T"}, new JavaType[] {new JavaType()});
        assertEquals(typeBindings1.hashCode(), typeBindings2.hashCode());

        typeBindings2 = new TypeBindings(new String[] {"X"}, new JavaType[] {new JavaType()});
        assertNotEquals(typeBindings1.hashCode(), typeBindings2.hashCode());
    }
}