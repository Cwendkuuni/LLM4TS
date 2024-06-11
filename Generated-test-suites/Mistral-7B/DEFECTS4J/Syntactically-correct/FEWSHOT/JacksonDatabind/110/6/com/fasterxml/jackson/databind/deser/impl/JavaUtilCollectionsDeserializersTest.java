package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test for ArrayList
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for SingletonList
        javaType = mapper.getTypeFactory().constructType(Collections.singletonList(String.class).getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for SingletonSet
        javaType = mapper.getTypeFactory().constructType(Collections.singleton(Boolean.TRUE).getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for UnmodifiableList
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(new ArrayList<>()).getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for UnmodifiableSet
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(new HashSet<>()).getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for UnmodifiableMap
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(new HashMap<>()).getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for other type
        javaType = mapper.getTypeFactory().constructType(String[].class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test for SingletonMap
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.singletonMap("key", "value").getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for UnmodifiableMap
        javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(new HashMap<>()).getType());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertNotNull(deserializer);

        // Test for other type
        javaType = mapper.getTypeFactory().constructType(Map.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertNull(deserializer);
    }
}