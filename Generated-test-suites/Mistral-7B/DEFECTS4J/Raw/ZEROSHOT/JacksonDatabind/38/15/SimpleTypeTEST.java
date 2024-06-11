package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType simpleType = new SimpleType(listIntClass, TypeBindings.bindingsForMapType(Map.class));
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(Map.class, simpleType.getSuperClass());
        assertArrayEquals(new Class[]{}, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.util.Map<K, java.util.List<I>>", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClassConstruction() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCollectionConstruction() {
        new SimpleType(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArrayConstruction() {
        new SimpleType(int[].class);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, new SimplePropertyType.StringSerializer(), null, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toString());
        assertEquals(SimplePropertyType.StringSerializer.class, simpleType.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, new SimplePropertyType.StringDeserializer(), false);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toString());
        assertEquals(SimplePropertyType.StringDeserializer.class, simpleType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertSame(simpleType, staticType);

        simpleType = new SimpleType(stringClass, null, null, null, null, null, true);
        staticType = simpleType.withStaticTyping();
        assertNotSame(simpleType, staticType);
    }

    @Test
    public void testSimpleTypeWithNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType narrowedType = simpleType.narrow(StringBuffer.class);
        assertNull(narrowedType);

        simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        simpleType = simpleType.narrow(StringBuffer.class);
        assertNotNull(simpleType);
        assertEquals(StringBuffer.class, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeWithContainedTypes() {
        Class<List<List<Integer>>> listListIntClass = List.class.getTypeParameters()[0].getType();
        List<Class<?>> classList = new ArrayList<Class<?>>(Arrays.asList(Integer.class, List.class.getTypeParameters()[0].getType()));
        SimpleType simpleType = new SimpleType(listListIntClass, null, null, null, null, null, false);
        assertArrayEquals(classList.toArray(new Class[classList.size()]), simpleType.getContainedTypes());
    }
}