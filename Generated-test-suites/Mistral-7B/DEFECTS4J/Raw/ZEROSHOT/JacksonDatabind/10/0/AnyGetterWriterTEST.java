package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializers;

/**
 * Comprehensive test for {@link AnyGetterWriter} class
 */
public class AnyGetterWriterTest {

    @Test
    public void testAnyGetterWriter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test constructor
        BeanProperty property = new BeanProperty(null, "anyMap", Map.class);
        AnnotatedMember accessor = new AnnotatedMember(new AnnotatedClass(getClass()), "anyMap", null);
        accessor.setValue(new JsonAnyGetter());
        JavaType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, new MapSerializer(mapper.getConfig(), mapType));

        // Test getAndSerialize method
        Object testObject = new TestClass();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        writer.getAndSerialize(testObject, gen, provider);
        gen.flush();

        // Test getAndFilter method
        gen = mapper.getFactory().createGenerator(System.out);
        provider = mapper.getSerializerProvider();
        PropertyFilter filter = new JsonFilter() {};
        writer.getAndFilter(testObject, gen, provider, filter);
        gen.flush();

        // Test resolve method
        provider = mapper.getSerializerProvider();
        writer.resolve(provider);
        assertEquals(StdSerializers.FIELD_FILTERING_MAP_SERIALIZER.getClass(), writer._mapSerializer.getClass());
    }

    // Test class for AnyGetterWriter test
    static class TestClass {
        @JsonSerialize(using = AsArraySerializerBase.class)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, String> anyMap;
    }

    static class JsonFilter {}
}