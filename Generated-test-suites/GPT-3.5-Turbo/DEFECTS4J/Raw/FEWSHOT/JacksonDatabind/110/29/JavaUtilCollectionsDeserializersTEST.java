package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() {
        // Test data
        JavaType type1 = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType type2 = TypeFactory.defaultInstance().constructCollectionType(Set.class, Integer.class);
        JavaType type3 = TypeFactory.defaultInstance().constructCollectionType(List.class, Double.class);
        JavaType type4 = TypeFactory.defaultInstance().constructCollectionType(Set.class, Boolean.class);

        // Test cases
        assertEquals(null, JavaUtilCollectionsDeserializers.findForCollection(null, type1));
        assertEquals(null, JavaUtilCollectionsDeserializers.findForCollection(null, type2));
        assertEquals(null, JavaUtilCollectionsDeserializers.findForCollection(null, type3));
        assertEquals(null, JavaUtilCollectionsDeserializers.findForCollection(null, type4));
    }

    @Test
    public void testFindForMap() {
        // Test data
        JavaType type1 = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaType type2 = TypeFactory.defaultInstance().constructMapType(Map.class, Double.class, Long.class);
        JavaType type3 = TypeFactory.defaultInstance().constructMapType(Map.class, Boolean.class, Float.class);

        // Test cases
        assertEquals(null, JavaUtilCollectionsDeserializers.findForMap(null, type1));
        assertEquals(null, JavaUtilCollectionsDeserializers.findForMap(null, type2));
        assertEquals(null, JavaUtilCollectionsDeserializers.findForMap(null, type3));
    }

    @Test
    public void testConverter() {
        // Test data
        int kind1 = 1;
        int kind2 = 2;
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawSuper1 = List.class;
        Class<?> rawSuper2 = Set.class;

        // Test case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter1 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind1, concreteType);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter2 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind2, concreteType);

        assertEquals(kind1, converter1._kind);
        assertEquals(kind2, converter2._kind);
        assertEquals(concreteType, converter1._inputType);
        assertEquals(concreteType, converter2._inputType);
    }

    @Test
    public void testConvert() {
        // Test data
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter1 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter2 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter3 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter4 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(4, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter5 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(5, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter6 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(6, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter7 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(7, null);

        // Test cases
        assertNull(converter1.convert(null));
        assertNull(converter2.convert(null));
        assertNull(converter3.convert(null));
        assertNull(converter4.convert(null));
        assertNull(converter5.convert(null));
        assertNull(converter6.convert(null));
        assertNull(converter7.convert(null));
    }

    @Test
    public void testGetInputType() {
        // Test data
        JavaType inputType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        // Test case
        assertEquals(inputType, converter.getInputType(null));
    }

    @Test
    public void testGetOutputType() {
        // Test data
        JavaType inputType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        // Test case
        assertEquals(inputType, converter.getOutputType(null));
    }

    @Test
    public void test_checkSingleton() {
        // Test cases
        try {
            JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                    new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, null);
            converter._checkSingleton(1);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Can not deserialize Singleton container from 1 entries", e.getMessage());
        }

        try {
            JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                    new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, null);
            converter._checkSingleton(2);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Can not deserialize Singleton container from 2 entries", e.getMessage());
        }
    }
}