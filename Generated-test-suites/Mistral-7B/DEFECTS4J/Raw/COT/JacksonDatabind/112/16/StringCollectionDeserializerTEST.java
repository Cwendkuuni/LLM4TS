package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for StringCollectionDeserializer class
 */
public class StringCollectionDeserializerTest {

    // Initialize test context and objects
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("[]");
    }

    // Test method: testBasicDeserialization
    @Test
    public void testBasicDeserialization() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        List<String> result = deserializer.deserialize(jsonParser, null);
        assertEquals(Collections.emptyList(), result);
    }

    // Test method: testDeserializationWithCustomValueDeserializer
    @Test
    public void testDeserializationWithCustomValueDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, new BasicStringDeserializer(), null, null);

        List<String> result = deserializer.deserialize(jsonParser, null);
        assertEquals(Collections.emptyList(), result);
    }

    // Test method: testDeserializationWithDelegateDeserializer
    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        JsonDeserializer<List<String>> delegateDeserializer = new ListDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, delegateDeserializer, null, null, null);

        List<String> result = deserializer.deserialize(jsonParser, null);
        assertEquals(Collections.emptyList(), result);
    }

    // Test method: testDeserializationWithNullValueProvider
    @Test
    public void testDeserializationWithNullValueProvider() throws IOException {
        NullValueProvider nullValueProvider = new StandardSerializers.NullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, nullValueProvider, null);

        List<String> result = deserializer.deserialize(jsonParser, null);
        assertNull(result);
    }

    // Test method: testDeserializationWithFormatFeature
    @Test
    public void testDeserializationWithFormatFeature() throws IOException {
        ObjectMapper objectMapperWithFeature = new ObjectMapper();
        objectMapperWithFeature.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonParser jsonParserWithFeature = objectMapperWithFeature.getFactory().createParser("\"element\"");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapperWithFeature.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        List<String> result = deserializer.deserialize(jsonParserWithFeature, null);
        assertEquals(Collections.singletonList("element"), result);
    }

    // Test method: testDeserializationWithContentConverter
    @Test
    public void testDeserializationWithContentConverter() throws IOException {
        JsonDeserializer<String[]> stringArrayDeserializer = new FromStringArrayDeserializer();

        ObjectMapper objectMapperWithContentConverter = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, stringArrayDeserializer);
        objectMapperWithContentConverter.registerModule(module);

        JsonParser jsonParserWithContentConverter = objectMapperWithContentConverter.getFactory().createParser("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapperWithContentConverter.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        List<String> result = deserializer.deserialize(jsonParserWithContentConverter, null);
        assertEquals(Collections.emptyList(), result);
    }

    // Test method: testDeserializationWithValueInstantiator
    @Test
    public void testDeserializationWithValueInstantiator() throws IOException {
        ValueInstantiator valueInstantiator = new StringListValueInstantiator();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                valueInstantiator, null, null, null, null);

        List<String> result = deserializer.deserialize(jsonParser, null);
        assertEquals(Collections.emptyList(), result);
    }
}

// Class used as delegate deserializer for testing
class ListDeserializer extends StdDeserializer<List<String>> {
    public ListDeserializer() {
        this(null);
    }

    protected ListDeserializer(Class<List<String>> t) {
        super(t);
    }

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<String> result = new ArrayList<>();
        while (p.nextToken() != JsonToken.END_ARRAY) {
            result.add(p.getValueAsString());
        }
        return result;
    }
}

// Custom value deserializer for testing
class BasicStringDeserializer extends StdDeserializer<String> {
    public BasicStringDeserializer() {
        this(null);
    }

    protected BasicStringDeserializer(Class<String> t) {
        super(t);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

// Custom value instantiator for testing
class StringListValueInstantiator extends ValueInstantiator {
    @Override
    public Object createValueInstance(SerializationConfig config, TypeType type) {
        return new ArrayList<>();
    }
}

// Custom deserializer from String array for testing
class FromStringArrayDeserializer extends JsonDeserializer<String[]> {
    @Override
    public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken currentToken = p.getCurrentToken();
        if (currentToken != JsonToken.START_ARRAY) {
            throw new IOException("Expected START_ARRAY but found " + currentToken);
        }

        String[] result = new String[0];
        while (p.nextToken() != JsonToken.END_ARRAY) {
            String value = p.getValueAsString();
            String[] newResult = new String[result.length + 1];
            System.arraycopy(result, 0, newResult, 0, result.length);
            newResult[result.length] = value;
            result = newResult;
        }
        return result;
    }
}