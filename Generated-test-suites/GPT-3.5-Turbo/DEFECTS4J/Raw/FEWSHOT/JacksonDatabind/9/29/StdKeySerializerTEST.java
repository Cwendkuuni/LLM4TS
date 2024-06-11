package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        // Test with Date value
        Date date = new Date();
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(date, generator, provider);

        // Test with String value
        String str = "test";
        generator = factory.createGenerator(System.out);
        serializer.serialize(str, generator, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        // Test with Type value
        JavaType type = provider.constructType(String.class);
        serializer.getSchema(provider, type);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        // Test with Type value
        JavaType type = provider.constructType(String.class);
        serializer.acceptJsonFormatVisitor(null, type);
    }
}