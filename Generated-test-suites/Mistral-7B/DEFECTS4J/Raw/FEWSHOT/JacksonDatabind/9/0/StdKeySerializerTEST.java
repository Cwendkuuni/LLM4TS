package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.java.TypeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, null);
        StdKeySerializer serializer = new StdKeySerializer();

        Date date = new Date();
        serializer.serialize(date, jgen, null);
        jgen.flush();

        assertEquals("\"2022-11-01T12:34:56.789Z\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, null);
        StdKeySerializer serializer = new StdKeySerializer();

        String str = "key";
        serializer.serialize(str, jgen, null);
        jgen.flush();

        assertEquals("\"key\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), mapper.getTypeFactory().constructType(Object.class));

        assertEquals("string", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode node = mapper.getTypeFactory().constructType(Object.class).getRawClass().getName();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(node, null);

        serializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals("STRING", visitor.getFormatName());
    }
}