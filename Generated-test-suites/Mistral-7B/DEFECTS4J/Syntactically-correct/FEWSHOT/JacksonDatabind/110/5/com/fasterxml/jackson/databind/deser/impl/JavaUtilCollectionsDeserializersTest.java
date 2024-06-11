package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueContainerDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @After
    public void teardown() throws Exception {
        mapper = null;
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);

        List<Boolean> list = Arrays.asList(Boolean.TRUE);
        Object deserialized = mapper.readValue("[]", javaType, deserializer);
        assertNotNull(deserialized);
        assertEquals(list, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);

        Set<Boolean> set = Collections.singleton(Boolean.TRUE);
        Object deserialized = mapper.readValue("{}", javaType, deserializer);
        assertNotNull(deserialized);
        assertEquals(set, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaType javaTypeUnmodifiable = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeUnmodifiable);
        assertNotNull(deserializer);

        Object deserialized = mapper.readValue("[]", javaTypeUnmodifiable, deserializer);
        assertNotNull(deserialized);
        assertEquals(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)), deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaType javaTypeUnmodifiable = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeUnmodifiable);
        assertNotNull(deserializer);

        Object deserialized = mapper.readValue("{}", javaTypeUnmodifiable, deserializer);
        assertNotNull(deserialized);
        assertEquals(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)), deserialized);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, Map.Entry.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
        assertNotNull(deserializer);

        Map<String, String> map = Collections.singletonMap("key", "value");
        Object deserialized = mapper.readValue("{\"key\":\"value\"}", javaType, deserializer);
        assertNotNull(deserialized);
        assertEquals(map, deserialized);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, Map.Entry.class);
        JavaType javaTypeUnmodifiable = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(map).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaTypeUnmodifiable);
        assertNotNull(deserializer);

        Map<String, String> map = Collections.singletonMap("key", "value");
        Object deserialized = mapper.readValue("{\"key\":\"value\"}", javaTypeUnmodifiable, deserializer);
        assertNotNull(deserialized);
        assertEquals(Collections.unmodifiableMap(map), deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidCollectionType() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_InvalidMapType() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
        assertNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_EmptyCollection() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);

        Object deserialized = mapper.readValue("[]", javaType, deserializer);
        assertNotNull(deserialized);
        assertEquals(Collections.emptyList(), deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_EmptyMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, Map.Entry.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
        assertNotNull(deserializer);

        Object deserialized = mapper.readValue("{}", javaType, deserializer);
        assertNotNull(deserialized);
        assertEquals(Collections.emptyMap(), deserialized);
    }
}