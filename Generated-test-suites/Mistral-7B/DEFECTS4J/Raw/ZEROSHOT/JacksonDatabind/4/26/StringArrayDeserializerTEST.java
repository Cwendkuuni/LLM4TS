package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeDefault() throws IOException {
        String json = "[\"apple\", \"banana\", \"orange\"]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        String json = "[{\"name\":\"apple\"}, {\"name\":\"banana\"}, {\"name\":\"orange\"}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> elementDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, new TypeReference<String>() {});
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        String json = "[1, 2, 3]";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getFactory().findValueDeserializer(Integer[].class, null);
        String[] result = mapper.readValue(json, StringArrayDeserializer.class, typeDeserializer);
        assertArrayEquals(new String[]{"1", "2", "3"}, result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArray() throws IOException {
        String json = "{\"name\":\"John\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, StringArrayDeserializer.class);
    }

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"apple\", \"banana\", \"orange\"]";
        JsonDeserializer<String> elementDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, new TypeReference<String>() {});
        StringArrayDeserializer deserializer1 = mapper.readValue(json, StringArrayDeserializer.class);
        StringArrayDeserializer deserializer2 = mapper.findContextualValueDeserializer(String[].class, null);
        StringArrayDeserializer deserializer3 = new StringArrayDeserializer(elementDeserializer);
        DefinePropertyProperty property = new DefinePropertyProperty("testProperty", String[].class);
        JsonDeserializer<?> deserializer4 = deserializer1.createContextual(mapper.getDeserializationContext(), property);
        assertEquals(deserializer1, deserializer4);
        JsonDeserializer<?> deserializer5 = deserializer2.createContextual(mapper.getDeserializationContext(), property);
        assertEquals(deserializer3, deserializer5);
    }

    @Test
    public void testIsDefaultDeserializer() {
        JsonDeserializer<String> deserializer = null;
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        assertFalse(stringArrayDeserializer.isDefaultDeserializer(deserializer));
    }
}