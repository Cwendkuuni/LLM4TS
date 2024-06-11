package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        JavaType result = typeBindings.findBoundType("");
        assertNull(result);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        boolean result = typeBindings.isEmpty();
        assertTrue(result);
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        int result = typeBindings.size();
        assertEquals(0, result);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        String result = typeBindings.getBoundName(0);
        assertNull(result);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        JavaType result = typeBindings.getBoundType(0);
        assertNull(result);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        List<JavaType> result = typeBindings.getTypeParameters();
        assertNotNull(result);
    }

    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        boolean result = typeBindings.hasUnbound("");
        assertFalse(result);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        TypeBindings result = typeBindings.withUnboundVariable("");
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        String result = typeBindings.toString();
        assertNotNull(result);
    }

    @Test
    public void testHashCode() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        int result = typeBindings.hashCode();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings = new TypeBindings(new String[]{}, new JavaType[]{}, null);
        boolean result = typeBindings.equals("");
        assertFalse(result);
    }
}