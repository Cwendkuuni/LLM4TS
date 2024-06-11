package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;

/**
 * Test class for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

    @Test
    public void testAnyGetterWriter() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        AnnotatedClass ac = mapper.getTypeFactory().constructType(TestClass.class);
        BeanPropertyDefinition bpd = ac.findProperty("anyMap");
        BeanProperty property = BeanProperty.std(bpd);
        AnnotatedMember accessor = property.getMember();

        MapSerializer mapSerializer = new StdSerializer<>(Map.class, mapper.getConfiguration());

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        // Test getAndSerialize
        TestClass testClass = new TestClass();
        testClass.anyMap = new HashMap<>();
        testClass.anyMap.put("key1", "value1");
        testClass.anyMap.put("key2", "value2");

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndSerialize(testClass, gen, provider);

        String actual = gen.getOutputFile();
        String expected = "{\"anyMap\":{\"key1\":\"value1\",\"key2\":\"value2\"}}";

        assertEquals(expected, actual);

        // Test getAndFilter
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("filteredMap", mapper.valueToTree(testClass.anyMap));

        TestClass filterTestClass = mapper.readValue(rootNode.toString(), TestClass.class);

        JsonGenerator filterGen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider filterProvider = mapper.getSerializerProvider();
        PropertyFilter filter = new Filter();

        anyGetterWriter.getAndFilter(filterTestClass, filterGen, filterProvider, filter);

        String filterActual = filterGen.getOutputFile();
        String filterExpected = "{\"anyMap\":{}}";

        assertEquals(filterExpected, filterActual);

        // Test resolve
        SerializerProvider provider1 = mapper.getSerializerProvider();
        AnyGetterWriter anyGetterWriter1 = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider2 = mapper.getSerializerProvider();
        AnyGetterWriter anyGetterWriter2 = new AnyGetterWriter(property, accessor, mapSerializer);

        assertEquals(anyGetterWriter1._mapSerializer, anyGetterWriter2._mapSerializer);
        assertEquals(anyGetterWriter1._mapSerializer, provider1.getPrimaryContextualizer().findSerializer(MapSerializer.class));
        assertEquals(anyGetterWriter2._mapSerializer, provider2.getPrimaryContextualizer().findSerializer(MapSerializer.class));
    }

    static class Filter {
        boolean include = true;
    }

    static class TestClass {
        @JsonAnyGetter
        public Map<String, String> anyMap;
    }

}