package com.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Unit test for JavaUtilCollectionsDeserializers.
 */
public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testConverterSingletonSet() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, concreteType);
        Set<String> set = new HashSet<>();
        set.add("test");
        Object result = converter.convert(set);
        assertTrue(result instanceof Set<?>);
        assertEquals(1, ((Set<?>) result).size());
        assertEquals("test", ((Set<?>) result).iterator().next());
    }

    @Test
    public void testConverterSingletonList() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, concreteType);
        List<String> list = new ArrayList<>();
        list.add("test");
        Object result = converter.convert(list);
        assertTrue(result instanceof List<?>);
        assertEquals(1, ((List<?>) result).size());
        assertEquals("test", ((List<?>) result).get(0));
    }

    @Test
    public void testConverterSingletonMap() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, concreteType);
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        Object result = converter.convert(map);
        assertTrue(result instanceof Map<?, ?>);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertTrue(((Map<?, ?>) result).containsKey("key"));
        assertEquals("value", ((Map<?, ?>) result).get("key"));
    }

    @Test
    public void testConverterUnmodifiableSet() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(4, concreteType);
        Set<String> set = new HashSet<>();
        set.add("test");
        Object result = converter.convert(set);
        assertTrue(result instanceof Set<?>);
        assertTrue(result instanceof Set<?>);
        assertEquals(1, ((Set<?>) result).size());
        assertEquals("test", ((Set<?>) result).iterator().next());
        assertTrue(result instanceof UnmodifiableSet<?>);
    }

    @Test
    public void testConverterUnmodifiableList() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(5, concreteType);
        List<String> list = new ArrayList<>();
        list.add("test");
        Object result = converter.convert(list);
        assertTrue(result instanceof List<?>);
        assertEquals(1, ((List<?>) result).size());
        assertEquals("test", ((List<?>) result).get(0));
        assertTrue(result instanceof UnmodifiableList<?>);
    }

    @Test
    public void testConverterUnmodifiableMap() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(6, concreteType);
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        Object result = converter.convert(map);
        assertTrue(result instanceof Map<?, ?>);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertTrue(((Map<?, ?>) result).containsKey("key"));
        assertEquals("value", ((Map<?, ?>) result).get("key"));
        assertTrue(result instanceof UnmodifiableMap<?, ?>);
    }

    @Test
    public void testConverterAsList() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(7, concreteType);
        List<String> list = new ArrayList<>();
        list.add("test");
        Object result = converter.convert(list);
        assertTrue(result instanceof List<?>);
        assertEquals(1, ((List<?>) result).size());
        assertEquals("test", ((List<?>) result).get(0));
    }
}