// Test file for StringArrayDeserializer

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

public class StringArrayDeserializerTest {
    private JsonFactory jsonFactory;

    @Before
    public void setUp() throws JsonProcessingException {
        jsonFactory = new JsonFactory();
    }

    // Tests for Method 1: constructor with no arguments

    // ...

    // Tests for Method 2: deserialize method with null elementDeserializer

    // ...

    // Edge Cases for Method 2: deserialize method

    // Empty JSON array
    @Test
    public void testDeserializeEmptyJsonArray() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("[]");
        String[] result = serializer.deserialize(jp, new DeserializationContext(), null);
        assertArrayEquals(new String[0], result);
    }

    // Null value
    @Test
    public void testDeserializeWithNullValue() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("[null]");
        String[] result = serializer.deserialize(jp, new DeserializationContext(), null);
        assertArrayEquals(new String[]{null}, result);
    }

    // JsonToken.VALUE_NUMBER
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonTokenValueNumber() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("[1, 2, 3.5]");
        serializer.deserialize(jp, new DeserializationContext(), null);
    }

    // JsonToken.VALUE_FALSE
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonTokenValueFalse() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("[false]");
        serializer.deserialize(jp, new DeserializationContext(), null);
    }

    // JsonToken.VALUE_TRUE
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonTokenValueTrue() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("[true]");
        serializer.deserialize(jp, new DeserializationContext(), null);
    }

    // JsonToken.START_OBJECT
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonTokenStartObject() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("{\"name\":\"John\"}");
        serializer.deserialize(jp, new DeserializationContext(), null);
    }

    // JsonToken.END_OBJECT
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonTokenEndObject() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("{}");
        serializer.deserialize(jp, new DeserializationContext(), null);
    }

    // JsonToken.END_ARRAY (when deserialize method is called twice)
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonTokenEndArray() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer();
        JsonParser jp = jsonFactory.createParser("[1, 2, 3]");
        serializer.deserialize(jp, new DeserializationContext(), null);
        jp.nextToken();
        serializer.deserialize(jp, new DeserializationContext(), null);
    }

    // Tests for Method 3: deserializeCustom method

    // ...

    // Helper method for creating JsonParser
    private JsonParser createParser(String jsonString) throws IOException {
        String json = Objects.requireNonNull(jsonString);
        JsonParser jp = jsonFactory.createParser(json);
        return jp;
    }
}