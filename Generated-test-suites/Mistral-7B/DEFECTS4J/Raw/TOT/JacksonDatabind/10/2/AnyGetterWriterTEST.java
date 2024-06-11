package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AnyGetterWriterTest {

    private ObjectMapper objectMapper;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        AnnotatedClass ac = objectMapper.getTypeFactory().constructType(TestBean.class);
        BeanProperty property = ac.findProperty("mapProperty");
        AnnotatedMember accessor = ac.findMember("getMapProperty");
        MapSerializer mapSerializer = objectMapper.getSerializerProvider().findValueSerializer(Map.class);

        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws JsonProcessingException, JsonGenerationException {
        TestBean bean = new TestBean();
        bean.mapProperty.put("key1", "value1");
        bean.mapProperty.put("key2", "value2");

        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());

        anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());

        ObjectNode result = objectMapper.readTree(gen.getOutputStream());
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1").asText());
        assertEquals("value2", result.get("key2").asText());
    }

    @Test
    public void testGetAndFilter() throws JsonProcessingException, JsonGenerationException {
        TestBean bean = new TestBean();
        bean.mapProperty.put("key1", "value1");
        bean.mapProperty.put("key2", "value2");

        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());

        PropertyFilter filter = new FilterAnnotation();
        anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);

        ObjectNode result = objectMapper.readTree(gen.getOutputStream());
        assertNotNull(result);
        assertEquals(1, result.size());
        // The filter should have removed one of the key-value pairs
        assertEquals("value1", result.get(0).get("key1").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidValueType() throws JsonProcessingException, JsonGenerationException {
        TestBean bean = new TestBean();
        bean.mapProperty = new int[0];

        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());

        anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
    }

    @Test
    public void testResolver() {
        // Resolver is not tested directly as it's not part of the public API
    }

    static class TestBean {
        @JsonAnyGetter
        Map<String, String> mapProperty = new LinkedHashMap<>();

        public Map<String, String> getMapProperty() {
            return mapProperty;
        }
    }

    static class FilterAnnotation {
        // A simple filter implementation for testing purposes
    }
}