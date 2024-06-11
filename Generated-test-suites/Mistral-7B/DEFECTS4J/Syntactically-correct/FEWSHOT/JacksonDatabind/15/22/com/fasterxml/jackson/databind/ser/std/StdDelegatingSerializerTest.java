package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testStdDelegatingSerializer_Converter() throws JsonMappingException, JsonGenerationException {
        Converter<Object, String> converter = new SimpleStringConverter();
        Object input = "test";
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(input, jsonGenerator, new SerializerProvider());
        Assert.assertEquals("\"test\"", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testStdDelegatingSerializer_Class() throws JsonMappingException, JsonGenerationException {
        String input = "test";
        Class<String> stringClass = String.class;
        Converter<String, Object> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(stringClass, converter);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(input, jsonGenerator, new SerializerProvider());
        Assert.assertEquals("\"test\"", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testStdDelegatingSerializer_DelegateSerializer() throws JsonMappingException, JsonGenerationException {
        String input = "test";
        SimpleStringSerializer delegateSerializer = new SimpleStringSerializer();
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateSerializer);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(input, jsonGenerator, new SerializerProvider());
        Assert.assertEquals("\"test\"", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testStdDelegatingSerializer_Resolve() throws JsonMappingException {
        String input = "test";
        SimpleStringSerializer delegateSerializer = new SimpleStringSerializer();
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        serializer.resolve(new SerializerProvider());
        serializer.serialize(input, jsonGenerator, new SerializerProvider());
        Assert.assertEquals("\"test\"", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testStdDelegatingSerializer_CreateContextual() throws JsonMappingException {
        String input = "test";
        SimpleStringSerializer delegateSerializer = new SimpleStringSerializer();
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(serializerProvider, null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        contextualSerializer.serialize("test", jsonGenerator, serializerProvider);
        Assert.assertEquals("\"test\"", jsonGenerator.getOutputContent().toString());

        // Test with null input
        Object nullInput = null;
        contextualSerializer = serializer.createContextual(serializerProvider, null);
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        contextualSerializer.serialize(nullInput, jsonGenerator, serializerProvider);
        Assert.assertEquals("null", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testStdDelegatingSerializer_SerializeWithType() throws JsonMappingException, JsonGenerationException {
        Object input = "test";
        SimpleStringSerializer delegateSerializer = new SimpleStringSerializer();
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        TypeSerializer typeSerializer = new TypeSerializerBuilder().build();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        serializer.serializeWithType(input, jsonGenerator, new SerializerProvider(), typeSerializer);
        Assert.assertEquals("\"test\"", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testStdDelegatingSerializer_GetSchema() throws JsonMappingException {
        String input = "test";
        SimpleStringSerializer delegateSerializer = new SimpleStringSerializer();
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = serializer.getSchema(new SerializerProvider(), String.class);
        Assert.assertNotNull(schema);
        JsonNode expectedSchema = mapper.readTree("{\"type\":\"string\"}");
        Assert.assertEquals(expectedSchema.toString(), schema.toString());
    }

    @Test
    public void testStdDelegatingSerializer_AcceptJsonFormatVisitor() throws JsonMappingException {
        String input = "test";
        SimpleStringSerializer delegateSerializer = new SimpleStringSerializer();
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mapper.getFactory()
                .constructJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, String.class);
        Assert.assertEquals("string", jsonFormatVisitorWrapper.asPrefixTree().toString());
    }

    // Add more test cases as needed for other methods
}

class SimpleStringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString();
    }
}

class SimpleStringSerializer extends StdSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value);
    }
}