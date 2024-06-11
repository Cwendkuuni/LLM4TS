package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.assertNull;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() {
        Converter<Object, String> converter = new DefaultConverterFactory().getConverter(String.class, Object.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Object input = "test";
        String expectedOutput = "test";

        String output = serializer.convertValue(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        Converter<Object, String> converter = new DefaultConverterFactory().getConverter(String.class, Object.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter,
                JsonTypeFactory.getDefaultInstance().constructType(String.class),
                new StringSerializer());

        Object input = "test";
        String expectedOutput = "test";

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(input, gen, new SerializerProvider());
        assertEquals(expectedOutput, gen.getOutputContentAsString());
        gen.close();
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        Converter<Object, String> converter = new DefaultConverterFactory().getConverter(String.class, Object.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter,
                JsonTypeFactory.getDefaultInstance().constructType(String.class),
                new StringSerializer());

        Object input = "test";
        String expectedOutput = "test";

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        TypeSerializer typeSerializer = new StandardSerializer(String.class);
        serializer.serializeWithType(input, gen, new SerializerProvider(), typeSerializer);
        assertEquals(expectedOutput, gen.getOutputContentAsString());
        gen.close();
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException {
        Converter<Object, String> converter = new DefaultConverterFactory().getConverter(String.class, Object.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter,
                JsonTypeFactory.getDefaultInstance().constructType(String.class),
                new StringSerializer());

        Object input = null;

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(input, gen, new SerializerProvider());
        assertNull(gen.getOutputContentAsString());
        gen.close();
    }

    @Test
    public void testGetDelegateSerializer() {
        Converter<Object, String> converter = new DefaultConverterFactory().getConverter(String.class, Object.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter,
                JsonTypeFactory.getDefaultInstance().constructType(String.class),
                new StringSerializer());

        JsonSerializer<String> expectedOutput = new StringSerializer();
        JsonSerializer<Object> output = serializer.getDelegatee();

        assertEquals(expectedOutput.getClass(), output.getClass());
    }
}