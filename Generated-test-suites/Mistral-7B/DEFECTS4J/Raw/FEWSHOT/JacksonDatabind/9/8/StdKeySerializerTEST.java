package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializers;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);

        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        jgen.flush();

        assertEquals("\"2023-03-28T15:18:34.584Z\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize("foo", jgen, provider);
        jgen.flush();

        assertEquals("\"foo\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonFormatVisitorWrapper visitor = mapper.getFactory().getJsonFormatVisitor(provider.getDefaultSerializerProvider());

        JsonNode schema = serializer.getSchema(provider, Types.collections().map());
        assertEquals(JsonNode.Value.textValue("string"), schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.getFactory().getJsonFormatVisitor(mapper.getSerializerProvider().getDefaultSerializerProvider());

        serializer.acceptJsonFormatVisitor(visitor, Types.collections().map());
    }

    @Test
    public void testSerializeMapToJsonObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", new Date());

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(jgen, map);
        jgen.flush();

        assertEquals("{\"foo\":\"bar\",\"baz\":\"2023-03-28T15:18:34.584Z\"}", jgen.getOutputBuffer().toString());
    }
}