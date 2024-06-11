package com.fasterxml.jackson.databind.deser.impl;

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
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();

        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, listType);
        assertEquals(List.class, deserializer.getClass());

        JavaType singletonListType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE, new TypeReference<Class<?>>() {});
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, singletonListType);
        assertEquals(List.class, deserializer.getClass());

        JavaType unmodifiableListType = TypeFactory.defaultInstance().constructType(List.class, Boolean.class, new TypeReference<Class<List<Boolean>>>() {});
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, unmodifiableListType);
        assertEquals(List.class, deserializer.getClass());

        JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, setType);
        assertEquals(Set.class, deserializer.getClass());

        JavaType singletonSetType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE, new TypeReference<Class<?>>() {});
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, singletonSetType);
        assertEquals(Set.class, deserializer.getClass());

        JavaType unmodifiableSetType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE, new TypeReference<Class<Set<Boolean>>>() {});
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, unmodifiableSetType);
        assertEquals(Set.class, deserializer.getClass());

        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, mapType);
        assertEquals(Map.class, deserializer.getClass());

        JavaType unmodifiableMapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class, new TypeReference<Class<Map<String, String>>>() {});
        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, unmodifiableMapType);
        assertEquals(Map.class, deserializer.getClass());

        JavaType otherType = TypeFactory.defaultInstance().constructType(String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, otherType);
        assertNull(deserializer);
    }

    @Test
    public void testConverter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = mapper.getTypeFactory();

        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
        JavaDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, listType);
        JavaType inputType = deserializer.getInputType(typeFactory);
        Converter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(1, inputType, List.class);
        assertInstanceOf(JavaUtilCollectionsConverter.class, converter);
    }
}