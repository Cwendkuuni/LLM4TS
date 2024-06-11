package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.JacksonTestCase;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

public class XmlSerializerProviderTest extends JacksonTestCase {

    private XmlSerializerProvider xmlProvider;
    private SerializationConfig config;
    private ObjectMapper mapper;

    @BeforeMethod
    public void setup() {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
    }

    // Test for serializeValue method when value is null
    @Test
    public void testSerializeNull() throws JsonProcessingException {
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, null);
        JsonGenerator gen = createContext().createGenerator();
        serializer.serialize(null, gen, xmlProvider);
        String actual = gen.getAsString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>";
        assertEquals(actual, expected);
    }

    // Test for serializeValue method when value is not null
    @Test
    public void testSerializeValue() throws JsonProcessingException {
        JsonSerializer<String> serializer = xmlProvider.findValueSerializer(String.class, null);
        String value = "Hello World";
        JsonGenerator gen = createContext().createGenerator();
        serializer.serialize(value, gen, xmlProvider);
        String actual = gen.getAsString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<string>Hello World</string>";
        assertEquals(actual, expected);
    }

    // Test for serializeValue method when rootType is specified
    @Test
    public void testSerializeValueWithRootType() throws JsonProcessingException {
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(rootType, null);
        String[] values = {"Value 1", "Value 2"};
        JsonGenerator gen = createContext().createGenerator();
        serializer.serialize(values, gen, xmlProvider);
        String actual = gen.getAsString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<array name=\"array\">\n<item>\n<string>Value 1</string>\n</item>\n<item>\n<string>Value 2</string>\n</item>\n</array>";
        assertEquals(actual, expected);
    }

    // Test for serializeValue method when customSerializer is provided
    @Test
    public void testSerializeValueWithCustomSerializer() throws JsonProcessingException {
        class MySerializer extends StdSerializer<Integer> {
            public MySerializer() {
                this(null);
            }

            protected MySerializer(Class<Integer> t) {
                super(t);
            }

            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
                gen.writeString("Squared");
            }
        }
        JavaType rootType = mapper.getTypeFactory().constructType(Integer.class);
        config.setSerializerProvider((SerializerProvider) xmlProvider);
        config.setRootValueSerializer(new MySerializer());
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(rootType, null);
        Integer value = 5;
        JsonGenerator gen = createContext().createGenerator();
        serializer.serialize(value, gen, xmlProvider);
        String actual = gen.getAsString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<integer>5</integer>Squared";
        assertEquals(actual, expected);
    }

    // Test for edge case: serializeValue method when value is an array and rootType is not specified
    @Test
    public void testSerializeArrayValueWithoutRootType() throws JsonProcessingException {
        Object[] values = {"Value 1", "Value 2"};
        JsonSerializer<Object> serializer = xmlProvider.findTypedValueSerializer(values.getClass(), true, null);
        JsonGenerator gen = createContext().createGenerator();
        serializer.serialize(values, gen, xmlProvider);
        String actual = gen.getAsString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<array name=\"array\">\n<item>\n<string>Value 1</string>\n</item>\n<item>\n<string>Value 2</string>\n</item>\n</array>";
        assertEquals(actual, expected);
    }

    // Test for edge case: serializeValue method when value is an array and rootType is specified
    @Test
    public void testSerializeArrayValueWithRootType() throws JsonProcessingException {
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        JsonSerializer<Object> serializer = xmlProvider.findTypedValueSerializer(rootType, true, null);
        String[] values = {"Value 1", "Value 2"};
        JsonGenerator gen = createContext().createGenerator();
        serializer.serialize(values, gen, xmlProvider);
        String actual = gen.getAsString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<array name=\"array\">\n<item>\n<string>Value 1</string>\n</item>\n<item>\n<string>Value 2</string>\n</item>\n</array>";
        assertEquals(actual, expected);
    }

    // Test for exception handling: serializeValue method when an IOException is thrown
    @Test(expectedExceptions = IOException.class)
    public void testSerializeValueWithIOException() throws JsonProcessingException {
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(String.class, null);
        String value = "Hello World";
        JsonGenerator gen = createContext().createGenerator();
        // Simulate an IOException
        throw new IOException();
        serializer.serialize(value, gen, xmlProvider);
    }

    // Test for exception handling: serializeValue method when a JsonMappingException is thrown
    @Test(expectedExceptions = JsonMappingException.class)
    public void testSerializeValueWithJsonMappingException() throws JsonProcessingException {
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(String.class, null);
        throw new JsonMappingException("TestExceptionMessage");
        serializer.serialize("Hello World", createContext().createGenerator(), xmlProvider);
    }
}