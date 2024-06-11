package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(List.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());

        String json = "[]";
        List<Boolean> list = mapper.readValue(json, javaType);
        assertInstanceOf(Collections.singletonList(Boolean.TRUE), list);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(Set.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());

        String json = "[]";
        Set<Boolean> set = mapper.readValue(json, javaType);
        assertInstanceOf(Collections.singleton(Boolean.TRUE), set);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}.getType());
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)).getClass());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(List.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());

        String json = "[]";
        List<Boolean> list = mapper.readValue(json, javaType);
        assertInstanceOf(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)), list);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}.getType());
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(Set.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());

        String json = "{}";
        Set<Boolean> set = mapper.readValue(json, javaType);
        assertInstanceOf(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)), set);
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertEquals(Map.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());

        String json = "{\"key\":\"value\"}";
        Map<String, String> map = mapper.readValue(json, javaType);
        assertEquals("value", map.get("key"));
        assertInstanceOf(Collections.singletonMap("key", "value"), map);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Collections.singletonMap("key", "value")).getClass());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertEquals(Map.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());

        String json = "{\"key\":\"value\"}";
        Map<String, String> map = mapper.readValue(json, javaType);
        assertEquals("value", map.get("key"));
        assertInstanceOf(Collections.unmodifiableMap(Collections.singletonMap("key", "value")), map);
    }

    @Test
    public void testFindForCollection_InvalidType() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertNull(converter);
    }

    @Test
    public void testFindForMap_InvalidType() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertNull(converter);
    }

    @Test
    public void testConverter_InvalidType() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(1, javaType, Object.class);
        assertNull(converter);
    }

    @Test
    public void testConverter_ValidType() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}.getType());
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(TYPE_AS_LIST, javaType, List.class);
        assertEquals(List.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());
    }

    @Before
    public void setup() throws Exception {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}