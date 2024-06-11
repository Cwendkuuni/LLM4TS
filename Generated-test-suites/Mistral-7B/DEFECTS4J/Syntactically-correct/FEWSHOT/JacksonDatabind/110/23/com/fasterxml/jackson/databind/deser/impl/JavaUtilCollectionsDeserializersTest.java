package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testFindForCollectionSingletonSet() throws JsonMappingException {
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        assertEquals(Set.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollectionSingletonList() throws JsonMappingException {
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        assertEquals(List.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollectionSingletonMap() throws JsonMappingException {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        assertEquals(Map.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws JsonMappingException {
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), Collections.unmodifiableSet(Arrays.asList(new Boolean(true))));
        assertEquals(Set.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws JsonMappingException {
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), Collections.unmodifiableList(Arrays.asList(new Boolean(true))));
        assertEquals(List.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws JsonMappingException {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), Collections.unmodifiableMap(Collections.singletonMap("key", "value")));
        assertEquals(Map.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForMapSingletonSet() throws JsonMappingException {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);
        assertEquals(Map.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws JsonMappingException {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), Collections.unmodifiableMap(Collections.singletonMap("key", "value")));
        assertEquals(Map.class, deserializer.getTargetValueType(mapper.getTypeFactory()));
    }

    @Test
    public void testConvertSingletonSet() throws JsonMappingException {
        Set<?> set = Collections.singleton(Boolean.TRUE);
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type);
        assertEquals(Boolean.TRUE, converter.convert(set));
    }

    @Test
    public void testConvertSingletonList() throws JsonMappingException {
        List<?> list = Collections.singletonList(Boolean.TRUE);
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type);
        assertEquals(Boolean.TRUE, converter.convert(list));
    }

    @Test
    public void testConvertSingletonMap() throws JsonMappingException {
        Map<?,?> map = Collections.singletonMap("key", "value");
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type);
        assertEquals("value", converter.convert(map));
    }

    @Test
    public void testConvertUnmodifiableSet() throws JsonMappingException {
        Set<?> set = Collections.unmodifiableSet(Arrays.asList(new Boolean(true)));
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, type);
        assertEquals(set, converter.convert(set));
    }

    @Test
    public void testConvertUnmodifiableList() throws JsonMappingException {
        List<?> list = Collections.unmodifiableList(Arrays.asList(new Boolean(true)));
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, type);
        assertEquals(list, converter.convert(list));
    }

    @Test
    public void testConvertUnmodifiableMap() throws JsonMappingException {
        Map<?,?> map = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type);
        assertEquals(map, converter.convert(map));
    }

    @Test
    public void testConvertAsList() throws JsonMappingException {
        List<?> list = Arrays.asList(new Boolean(true), new Boolean(false));
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, type);
        assertEquals(list, converter.convert(list));
    }
}