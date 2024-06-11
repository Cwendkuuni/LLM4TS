package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("[]");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeStringArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("[\"element1\", \"element2\"]");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"element1", "element2"}, result);
    }

    @Test
    public void testDeserializeStringArrayWithCustomElementDeserializer() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customElementDeserializer = mapper.getDeserializationFactory().findValueDeserializer(String.class, null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customElementDeserializer);
        String[] result = deserializer.deserialize(factory.createParser("[\"customElement1\", \"customElement2\"]"), mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"customElement1", "customElement2"}, result);
    }

    @Test
    public void testDeserializeStringArrayWithNullValues() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("[\"element1\", null, \"element3\"]");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"element1", null, "element3"}, result);
    }

    @Test
    public void testDeserializeStringArrayWithEmptyStringAsNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("[\"\", \"element2\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"", "element2"}, result);
        assertEquals(null, result[0]);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("{ \"invalid\": [] }");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeJsonObject() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("{\"key\": \"value\"}");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeCustomDeserializerNullValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customElementDeserializer = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customElementDeserializer);
        deserializer.deserialize(factory.createParser("[null]"), mapper.getDeserializationContext());
    }
}