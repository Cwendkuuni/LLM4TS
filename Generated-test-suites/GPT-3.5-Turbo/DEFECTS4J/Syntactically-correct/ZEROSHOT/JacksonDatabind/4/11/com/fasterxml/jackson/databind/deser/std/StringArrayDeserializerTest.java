package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

@JacksonStdImpl
public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;
    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setup() throws IOException {
        deserializer = new StringArrayDeserializer();
        context = new DefaultDeserializationContext(JsonFactory.builder().build().createParser("[]"), new DefaultDeserializationConfig(), new ObjectBuffer());
        parser = context.getParser();
    }

    @Test
    public void testDeserialize() throws IOException {
        String[] result = deserializer.deserialize(parser, context);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithNull() throws IOException {
        parser.nextToken(); // Start array
        parser.nextToken(); // Null value
        parser.nextToken(); // End array

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testDeserializeWithValues() throws IOException {
        parser.nextToken(); // Start array
        parser.nextToken(); // Text value 1
        parser.nextToken(); // Text value 2
        parser.nextToken(); // End array

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(2, result.length);
        assertEquals("Text value 1", result[0]);
        assertEquals("Text value 2", result[1]);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String[] result = deserializer.handleNonArray(parser, context);

        assertNull(result);
    }

}