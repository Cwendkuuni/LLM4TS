package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnyGetterWriterTest extends TestBase {

    @SuppressWarnings("unchecked")
    public void testAnyGetterWriter_NullValue() throws Exception {
        BeanProperty property = new BeanProperty.Std("anyProperty", String.class);
        AnnotatedMember accessor = AnnotatedMember.Std(property, AnyGetterWriter.class, "getAnyProperty");
        MapSerializer mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance(), MapSerializer.StandardConfig.builder().build());
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object() {{}};
        JsonGenerator gen = getTestGenerator();
        SerializerProvider provider = getTestProvider();

        writer.getAndSerialize(bean, gen, provider);
        gen.flush(); // consumes any remaining output
        assertNull(gen.toString());
    }

    public void testAnyGetterWriter_EmptyMap() throws Exception {
        BeanProperty property = new BeanProperty.Std("anyProperty", Map.class);
        AnnotatedMember accessor = AnnotatedMember.Std(property, AnyGetterWriter.class, "getAnyProperty");
        MapSerializer mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance(), MapSerializer.StandardConfig.builder().build());
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object() {{
            @SuppressWarnings("unchecked")
            Map<String, String> map = new HashMap<>();
            put("key1", "value1");
            put("key2", "value2");
            _anyProperty = map;
        }};

        JsonGenerator gen = getTestGenerator();
        SerializerProvider provider = getTestProvider();

        writer.getAndSerialize(bean, gen, provider);
        String result = gen.toString();
        JsonFactory jsonFactory = new JsonFactory();
        Map<String, Object> deserialized = mapper.readValue(new StringReader(result), Map.class);
        assertEquals(2, deserialized.size());
        assertEquals("value1", deserialized.get("key1"));
        assertEquals("value2", deserialized.get("key2"));

        gen.flush(); // consumes any remaining output
    }

    public void testAnyGetterWriter_NullMap() throws Exception {
        BeanProperty property = new BeanProperty.Std("anyProperty", Map.class);
        AnnotatedMember accessor = AnnotatedMember.Std(property, AnyGetterWriter.class, "getAnyProperty");
        MapSerializer mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance(), MapSerializer.StandardConfig.builder().build());
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object() {{
            _anyProperty = null;
        }};

        JsonGenerator gen = getTestGenerator();
        SerializerProvider provider = getTestProvider();

        writer.getAndSerialize(bean, gen, provider);
        gen.flush(); // consumes any remaining output
        assertNull(gen.toString());
    }

    public void testAnyGetterWriter_InvalidValueType() throws Exception {
        BeanProperty property = new BeanProperty.Std("anyProperty", String.class);
        AnnotatedMember accessor = AnnotatedMember.Std(property, AnyGetterWriter.class, "getAnyProperty");
        MapSerializer mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance(), MapSerializer.StandardConfig.builder().build());
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object() {{
            @SuppressWarnings("unchecked")
            Object invalidValue = new Object();
            _anyProperty = invalidValue;
        }};

        JsonGenerator gen = getTestGenerator();
        SerializerProvider provider = getTestProvider();

        try {
            writer.getAndSerialize(bean, gen, provider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (anyProperty()) not java.util.Map but "+invalidValue.getClass().getName(), e.getMessage());
        }

        gen.flush(); // consumes any remaining output
    }

    public void testAnyGetterWriter_Filter() throws Exception {
        BeanProperty property = new BeanProperty.Std("anyProperty", Map.class);
        AnnotatedMember accessor = AnnotatedMember.Std(property, AnyGetterWriter.class, "getAnyProperty");
        MapSerializer mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance(), MapSerializer.StandardConfig.builder().build());
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object() {{
            @SuppressWarnings("unchecked")
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            map.put("key2", "value2");
            map.put("key3", "value3");
            _anyProperty = map;
        }};

        JsonGenerator gen = getTestGenerator();
        SerializerProvider provider = getTestProvider();
        PropertyFilter filter = new FilterProvider().setFailOnEmptyAndNull(false).getFilterInstance("testFilter");

        writer.getAndFilter(bean, gen, provider, filter);
        String result = gen.toString();
        JsonFactory jsonFactory = new JsonFactory();
        Map<String, Object> deserialized = mapper.readValue(new StringReader(result), Map.class);
        assertEquals(2, deserialized.size());
        assertEquals("value1", deserialized.get("key1"));
        assertEquals("value2", deserialized.get("key2"));

        gen.flush(); // consumes any remaining output
    }

    public void testAnyGetterWriter_Resolve() throws Exception {
        BeanProperty property = new BeanProperty.Std("anyProperty", Map.class);
        AnnotatedMember accessor = AnnotatedMember.Std(property, AnyGetterWriter.class, "getAnyProperty");
        MapSerializer mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance(), MapSerializer.StandardConfig.builder().build());
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider = getTestProvider();
        writer.resolve(provider);
        assertNotNull(writer._mapSerializer);
    }

}