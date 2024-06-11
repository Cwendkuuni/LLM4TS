package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(date, gen, new SerializerProvider() {
            @Override
            public void defaultSerializeDateKey(Date date, JsonGenerator jsonGenerator) throws IOException {
                jsonGenerator.writeString(date.toString());
            }
        });

        gen.flush();
    }

    @Test
    public void testSerializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String key = "testKey";
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(key, gen, new SerializerProvider());

        gen.flush();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);
        JsonNode schema = serializer.getSchema(new SerializerProvider(), typeHint);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getJsonFormatVisitor(null, null, null);
        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}