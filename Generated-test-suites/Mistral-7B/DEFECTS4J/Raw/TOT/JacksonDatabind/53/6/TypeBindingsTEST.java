package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(TypeBindings.class.newInstance(), bindings);
        assertNull(bindings.findBoundType("T"));
        assertEquals(0, bindings.size());
    }

    @Test
    public void testTypeBindingsCreation() {
        JavaType[] types = new JavaType[] { JavaType.constructCollectionType(ArrayList.class, String.class),
                JavaType.constructCollectionType(LinkedHashMap.class, String.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(String[].class, types);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(JavaType.constructRawType(ArrayList.class), bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(JavaType.constructRawType(String.class), bindings.getBoundType(1));
        assertEquals(JavaType.constructRawType(Integer.class), bindings.getBoundType(2));
        assertNull(bindings.findBoundType("U"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeBindingsCreationInvalid() {
        JavaType[] types = new JavaType[] { JavaType.constructCollectionType(ArrayList.class, String.class),
                JavaType.constructCollectionType(LinkedHashMap.class, String.class, Integer.class),
                JavaType.constructCollectionType(ArrayList.class, Integer.class) };
        TypeBindings.create(String[].class, types);
    }

    @Test
    public void testTypeBindingsCreationList() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class);
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(JavaType.constructRawType(ArrayList.class), bindings.getBoundType(0));
    }

    @Test
    public void testTypeBindingsCreationMap() {
        TypeBindings bindings = TypeBindings.create(LinkedHashMap.class, JavaType.constructTypeParameter(HashMap.class, 0),
                JavaType.constructTypeParameter(HashMap.class, 1));
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(JavaType.constructRawType(Map.Entry.class), bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(JavaType.constructRawType(String.class), bindings.getBoundType(2));
    }

    @Test
    public void testTypeBindingsCreationMapWithTypes() {
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, JavaType.constructTypeParameter(HashMap.class, 0),
                JavaType.constructTypeParameter(HashMap.class, 1), JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(JavaType.constructRawType(Map.Entry.class), bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(JavaType.constructRawType(String.class), bindings.getBoundType(2));
    }

    @Test
    public void testTypeBindingsCreationMapWithUnbound() {
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, JavaType.constructTypeParameter(HashMap.class, 0), null);
        assertEquals("K", bindings.getBoundName(0));
        assertNull(bindings.getBoundType(0));
        assertEquals(1, bindings.hasUnbound("V"));
    }
}