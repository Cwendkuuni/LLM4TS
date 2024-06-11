package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Test cases for {@link StdKeySerializer}.
 */
public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, jgen, new SerializerProvider());
        jgen.flush();

        assertEquals("\"2022-12-20T12:12:12.123Z\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String key = "foo";
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(key, jgen, new SerializerProvider());
        jgen.flush();

        assertEquals("\"foo\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(String.class));

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(String.class));

        assertTrue(visitor.wasVisited(JsonFormatVisitorWrapper.FormatFeature.CAN_OVERLOAD_ARRAY_FORMATS));
        assertTrue(visitor.wasVisited(JsonFormatVisitorWrapper.FormatFeature.CAN_OVERLOAD_VALUE_FORMATS));
        assertTrue(visitor.wasVisited(JsonFormatVisitorWrapper.FormatFeature.CAN_WRITE_DIRECT));
        assertTrue(visitor.wasVisited(JsonFormatVisitorWrapper.FormatFeature.CAN_WRITE_THROWS_NULLS));
    }
}