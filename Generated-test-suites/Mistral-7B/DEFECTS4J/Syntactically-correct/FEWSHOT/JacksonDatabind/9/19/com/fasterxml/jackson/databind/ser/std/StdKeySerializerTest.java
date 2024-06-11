package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        Date date = new Date(System.currentTimeMillis());
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(date, generator, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        assertEquals("\"$date\",\"2001-02-03T04:05:06.078Z\"", generator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize("test", generator, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        assertEquals("\"test\"", generator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(null, generator, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        assertEquals("null", generator.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = new StdKeySerializer().getSchema(new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), Object.class);
        assertEquals("string", schema.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitor_UnexpectedFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonDeserializer.None(JsonType.stringType());
        new StdKeySerializer().acceptJsonFormatVisitor(visitor, Object.class);
    }
}