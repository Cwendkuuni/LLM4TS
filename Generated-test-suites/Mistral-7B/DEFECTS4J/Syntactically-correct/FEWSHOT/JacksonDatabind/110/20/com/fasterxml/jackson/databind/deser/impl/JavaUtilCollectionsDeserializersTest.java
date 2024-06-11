package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        JavaType typeSet = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
        JavaType typeList = mapper.getTypeFactory().constructType(List.class, Boolean.class);

        ObjectWriter writer = mapper.writer().withTypeValidator(null);
        String jsonSet = writer.writeValueAsString(Collections.singleton(Boolean.TRUE));
        String jsonList = writer.writeValueAsString(Collections.singletonList(Boolean.TRUE));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<?> deserializerSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeSet);
        JsonDeserializer<?> deserializerList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeList);

        assertEquals(Set.class, deserializerSet.getTargetValueType(null).getRawClass());
        assertEquals(List.class, deserializerList.getTargetValueType(null).getRawClass());

        Object inputSet = mapper.readValue(jsonSet, Object.class);
        Object inputList = mapper.readValue(jsonList, Object.class);

        assertNotNull(inputSet);
        assertNotNull(inputList);

        JsonDeserializer<?> deserializerDeserializerSet = new StdDelegatingDeserializer<>(deserializerSet);
        JsonDeserializer<?> deserializerDeserializerList = new StdDelegatingDeserializer<>(deserializerList);

        TypeReference<Set<Boolean>> expectedSetType = new TypeReference<Set<Boolean>>() {
        };
        TypeReference<List<Boolean>> expectedListType = new TypeReference<List<Boolean>>() {
        };

        Set<Boolean> outputSet = mapper.readValue(jsonSet, expectedSetType);
        List<Boolean> outputList = mapper.readValue(jsonList, expectedListType);

        assertEquals(outputSet, inputSet);
        assertEquals(outputList, inputList);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindForMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        JavaType typeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);

        ObjectWriter writer = mapper.writer().withTypeValidator(null);
        String jsonMap = writer.writeValueAsString(Collections.singletonMap("key", "value"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<?> deserializerMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, typeMap);

        assertEquals(Map.class, deserializerMap.getTargetValueType(null).getRawClass());

        Object inputMap = mapper.readValue(jsonMap, Object.class);

        JsonDeserializer<?> deserializerDeserializerMap = new StdDelegatingDeserializer<>(deserializerMap);

        TypeReference<Map<String, String>> expectedMapType = new TypeReference<Map<String, String>>() {
        };

        Map<String, String> outputMap = mapper.readValue(jsonMap, expectedMapType);

        assertEquals(outputMap, inputMap);
    }

    @Test
    public void testUnsupportedTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        JavaType typeArray = mapper.getTypeFactory().constructType(int[].class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<?> deserializerArray = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeArray);

        assertNull(deserializerArray);
    }
}