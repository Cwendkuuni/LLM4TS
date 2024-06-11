package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = JavaType.construct(Object.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = JavaType.construct(Object.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        assertNull(deserializer);
    }

    @Test
    public void testConverter() {
        JavaType inputType = JavaType.construct(Object.class);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, inputType, List.class);

        assertNotNull(converter);
        assertEquals(inputType, converter.getInputType(null));
        assertEquals(inputType, converter.getOutputType(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterSingletonSetWithMultipleEntries() {
        JavaType inputType = JavaType.construct(Object.class);
        Set<Object> set = new HashSet<>();
        set.add(1);
        set.add(2);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, inputType);
        converter.convert(set);
    }

    @Test
    public void testConverterSingletonSetWithOneEntry() {
        JavaType inputType = JavaType.construct(Object.class);
        Set<Object> set = new HashSet<>();
        set.add(1);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, inputType);
        Object result = converter.convert(set);

        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertEquals(1, ((Set<?>) result).size());
        assertEquals(1, ((Set<?>) result).iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterSingletonListWithNoEntry() {
        JavaType inputType = JavaType.construct(Object.class);
        List<Object> list = new ArrayList<>();
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, inputType);
        converter.convert(list);
    }

    @Test
    public void testConverterSingletonListWithOneEntry() {
        JavaType inputType = JavaType.construct(Object.class);
        List<Object> list = new ArrayList<>();
        list.add(1);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, inputType);
        Object result = converter.convert(list);

        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(1, ((List<?>) result).size());
        assertEquals(1, ((List<?>) result).get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterSingletonMapWithNoEntry() {
        JavaType inputType = JavaType.construct(Object.class);
        Map<Object, Object> map = new HashMap<>();
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, inputType);
        converter.convert(map);
    }

    @Test
    public void testConverterSingletonMapWithOneEntry() {
        JavaType inputType = JavaType.construct(Object.class);
        Map<Object, Object> map = new HashMap<>();
        map.put(1, "a");
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, inputType);
        Object result = converter.convert(map);

        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertEquals("a", ((Map<?, ?>) result).get(1));
    }

}