
package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    private TypeBindings typeBindings;

    @Before
    public void setUp() {
        String[] names = new String[]{"name1", "name2"};
        JavaType[] types = new JavaType[]{new JavaType(), new JavaType()};
        String[] unboundVariables = new String[]{"unbound1", "unbound2"};
        typeBindings = new TypeBindings(names, types, unboundVariables);
    }
    
    @Test
    public void testFindBoundType() {
        JavaType result = typeBindings.findBoundType("name1");
        assertNotNull(result);
    }

    @Test
    public void testFindBoundTypeNonExistent() {
        JavaType result = typeBindings.findBoundType("nonexistent");
        assertNull(result);
    }

    @Test
    public void testEmpty() {
        assertFalse(typeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        int size = typeBindings.size();
        assertEquals(2, size);
    }

    @Test
    public void testGetBoundName() {
        String name = typeBindings.getBoundName(0);
        assertEquals("name1", name);
    }

    @Test
    public void testGetBoundType() {
        JavaType type = typeBindings.getBoundType(0);
        assertNotNull(type);
    }

    @Test
    public void testGetTypeParameters() {
        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assertNotNull(typeParameters);
        assertEquals(2, typeParameters.size());
    }

    @Test
    public void testHasUnbound() {
        boolean result = typeBindings.hasUnbound("unbound1");
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        int hashCode = typeBindings.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        TypeBindings other = new TypeBindings(new String[]{"name1", "name2"}, new JavaType[]{new JavaType(), new JavaType()}, new String[]{"unbound1", "unbound2"});
        boolean result = typeBindings.equals(other);
        assertTrue(result);
    }

    @Test
    public void testToString() {
        String result = typeBindings.toString();
        assertNotNull(result);
    }

    @Test
    public void testTypeParameterArray() {
        JavaType[] array = typeBindings.typeParameterArray();
        assertNotNull(array);
        assertEquals(2, array.length);
    }
}
