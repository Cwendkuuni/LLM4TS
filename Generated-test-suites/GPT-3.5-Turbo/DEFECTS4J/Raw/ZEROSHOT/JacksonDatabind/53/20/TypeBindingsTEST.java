
package com.fasterxml.jackson.databind.type;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;

/**
 * Test class for TypeBindings
 */
public class TypeBindingsTest {
    
    private TypeBindings typeBindings;

    @Before
    public void setUp() {
        String[] names = { "T1", "T2" };
        JavaType[] types = { new JavaType(String.class), new JavaType(Integer.class) };
        typeBindings = new TypeBindings(names, types, null);
    }

    @Test
    public void testFindBoundType() {
        assertEquals("String", typeBindings.findBoundType("T1").getRawClass().getName());
        assertEquals("Integer", typeBindings.findBoundType("T2").getRawClass().getName());
        assertNull(typeBindings.findBoundType("T3"));
    }

    @Test
    public void testSize() {
        assertEquals(2, typeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        assertEquals("T1", typeBindings.getBoundName(0));
        assertEquals("T2", typeBindings.getBoundName(1));
        assertNull(typeBindings.getBoundName(2));
    }

    @Test
    public void testGetBoundType() {
        assertEquals("String", typeBindings.getBoundType(0).getRawClass().getName());
        assertEquals("Integer", typeBindings.getBoundType(1).getRawClass().getName());
        assertNull(typeBindings.getBoundType(2));
    }

    @Test
    public void testGetTypeParameters() {
        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertEquals("String", typeParameters.get(0).getRawClass().getName());
        assertEquals("Integer", typeParameters.get(1).getRawClass().getName());
    }

    @Test
    public void testHasUnbound() {
        assertFalse(typeBindings.hasUnbound("T1"));
        assertFalse(typeBindings.hasUnbound("T2"));
        assertTrue(typeBindings.hasUnbound("T3"));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("T3");
        assertFalse(newTypeBindings.hasUnbound("T1"));
        assertFalse(newTypeBindings.hasUnbound("T2"));
        assertTrue(newTypeBindings.hasUnbound("T3"));
    }

    @Test
    public void testToString() {
        assertEquals("<String,Integer>", typeBindings.toString());
    }

    @Test
    public void testHashCode() {
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("T3");
        assertNotEquals(typeBindings.hashCode(), newTypeBindings.hashCode());
    }

    @Test
    public void testEquals() {
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("T3");
        assertFalse(typeBindings.equals(newTypeBindings));
    }
}
