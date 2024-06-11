package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private static Object mapSingletonTest(ObjectMapper mapper, Object input) throws JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(input.getBytes())), javaType);
        return deserializer.deserialize(mapper.getDeserializationContext(), readValue);
    }

    private static Object listSingletonTest(ObjectMapper mapper, Object input) throws JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(input.getBytes())), javaType);
        return deserializer.deserialize(mapper.getDeserializationContext(), readValue);
    }

    private static Object setSingletonTest(ObjectMapper mapper, Object input) throws JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class);
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(input.getBytes())), javaType);
        return deserializer.deserialize(mapper.getDeserializationContext(), readValue);
    }

    @Test
    public void testFindForMapSingleton() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$SingletonMap\",\"value\":{\"key\":\"foo\",\"value\":\"bar\"}}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), Map.class);

        Object output = mapSingletonTest(mapper, input);

        Map map = (Map) output;
        assertEquals("foo", map.keySet().iterator().next());
        assertEquals("bar", map.values().iterator().next());
        assertEquals("{foo=bar}", map.toString());
    }

    @Test
    public void testFindForMapUnmodifiable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$UnmodifiableMap\",\"value\":{\"key\":\"foo\",\"value\":\"bar\"}}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), Map.class);

        Object output = mapSingletonTest(mapper, input);

        Map map = (Map) output;
        assertEquals("foo", map.keySet().iterator().next());
        assertEquals("bar", map.values().iterator().next());
        assertFalse(map.isEmpty());
        assertFalse(map.containsKey("foo2"));
        assertFalse(map.containsValue("bar2"));
        assertFalse(map instanceof MutableMap);
        assertEquals("{foo=bar}", map.toString());
    }

    @Test
    public void testFindForListSingleton() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$SingletonList\",\"value\":[\"foo\"]}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), List.class);

        Object output = listSingletonTest(mapper, input);

        List list = (List) output;
        assertEquals("foo", list.get(0));
        assertEquals("[foo]", list.toString());
    }

    @Test
    public void testFindForListUnmodifiable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$UnmodifiableList\",\"value\":[\"foo\"]}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), List.class);

        Object output = listSingletonTest(mapper, input);

        List list = (List) output;
        assertEquals("foo", list.get(0));
        assertEquals("[foo]", list.toString());
        assertFalse(list.isEmpty());
        assertFalse(list.remove(0));
        assertFalse(list.add(0, "foo2"));
        assertFalse(list instanceof MutableList);
    }

    @Test
    public void testFindForSetSingleton() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$SingletonSet\",\"value\":[\"foo\"]}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), Set.class);

        Object output = setSingletonTest(mapper, input);

        Set set = (Set) output;
        assertTrue(set.contains("foo"));
        assertFalse(set.isEmpty());
        assertFalse(set.add("foo2"));
        assertFalse(set.remove("foo"));
        assertFalse(set instanceof MutableSet);
    }

    @Test
    public void testFindForSetUnmodifiable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$UnmodifiableSet\",\"value\":[\"foo\"]}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), Set.class);

        Object output = setSingletonTest(mapper, input);

        Set set = (Set) output;
        assertTrue(set.contains("foo"));
        assertFalse(set.isEmpty());
        assertFalse(set.add("foo2"));
        assertFalse(set.remove("foo"));
        assertFalse(set instanceof MutableSet);
    }

    @Test
    public void testFindForCollectionAsList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"class\":\"java.util.Collections$ArrayList\",\"size\":1,\"list\":[\"foo\"]}";
        Object input = mapper.readValue(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), Collection.class);

        Object output = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(Collection.class)).deserialize(mapper.getDeserializationContext(), input);

        List list = (List) output;
        assertEquals("foo", list.get(0));
        assertEquals("[foo]", list.toString());
        assertFalse(list.isEmpty());
        assertTrue(list instanceof MutableList);
    }
}