package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    private TypeBindings typeBindings;

    @Before
    public void setUp() {
        typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, new String[]{});
    }

    @Test
    public void testFindBoundType() {
        assertNotNull(typeBindings.findBoundType("name"));
    }

    @Test
    public void testEmpty() {
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        assertNull(typeBindings.getBoundName(0));
    }

    @Test
    public void testGetBoundType() {
        assertNull(typeBindings.getBoundType(0));
    }

    @Test
    public void testGetTypeParameters() {
        assertEquals(0, typeBindings.getTypeParameters().size());
    }

    @Test
    public void testHasUnbound() {
        assertFalse(typeBindings.hasUnbound("name"));
    }

    @Test
    public void testWithUnboundVariable() {
        assertNotNull(typeBindings.withUnboundVariable("name"));
    }

    @Test
    public void testHashCode() {
        assertNotEquals(0, typeBindings.hashCode());
    }

    @Test
    public void testEquals() {
        assertEquals(typeBindings, typeBindings);
    }

    @Test
    public void testTypeParameterArray() {
        assertNotNull(typeBindings.typeParameterArray());
    }

    @Test
    public void testToString() {
        assertNotNull(typeBindings.toString());
    }
}