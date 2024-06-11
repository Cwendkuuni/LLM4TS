package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    // Test for findForCollection method with List types

    @Test
    public void testFindForCollection_SingleList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingleList_Deserialization() throws Exception {
        String json = "[]";
        List<Boolean> list = mapper.readValue(json, new TypeReference<List<Boolean>>() {}.getType());
        assertEquals(Boolean.TRUE, list.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_WrongListType_Deserialization() throws Exception {
        String json = "[1,2,3]";
        mapper.readValue(json, new TypeReference<List<Boolean>>() {}.getType());
    }

    // Test for findForCollection method with Set types

    @Test
    public void testFindForCollection_SingleSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingleSet_Deserialization() throws Exception {
        String json = "[]";
        Set<Boolean> set = mapper.readValue(json, new TypeReference<Set<Boolean>>() {}.getType());
        assertEquals(Boolean.TRUE, set.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_WrongSetType_Deserialization() throws Exception {
        String json = "[1,2,3]";
        mapper.readValue(json, new TypeReference<Set<Boolean>>() {}.getType());
    }

    // Test for findForMap method with Map types

    @Test
    public void testFindForMap_SingleMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Arrays.asList(
                new Map.Entry<String, Integer>("key1", 1), new Map.Entry<String, Integer>("key2", 2))).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_SingleMap_Deserialization() throws Exception {
        String json = "{}";
        Map<String, Integer> map = mapper.readValue(json, new TypeReference<Map<String, Integer>>() {}.getType());
        assertEquals("key1", map.keySet().iterator().next());
        assertEquals(1, map.values().iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_WrongMapType_Deserialization() throws Exception {
        String json = "[{\"key\":1}]";
        mapper.readValue(json, new TypeReference<Map<String, Integer>>() {}.getType());
    }
}