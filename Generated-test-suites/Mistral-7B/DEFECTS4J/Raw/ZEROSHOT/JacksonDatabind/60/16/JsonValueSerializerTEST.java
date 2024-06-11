package com.example.json; // replace with your test package name

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test file for JsonValueSerializer class.
 */
public class JsonValueSerializerTest {

    @JsonValue
    public static class TestBean {
        private int value;

        public int getValue() {
            return value;
        }

        @JsonSerialize
        public void setValue(int value) {
            this.value = value;
        }
    }

    @Test
    public void testJsonValueSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Integer.TYPE, null);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, valueSerializer, false);
        BeanProperty property = new BeanProperty("value", JsonSerializer.EmptyInstance.getClass(), null, null, null);

        JsonValueSerializer contextualSerializer = jsonValueSerializer.withResolved(property, valueSerializer, false);

        ObjectNode rootNode = mapper.createObjectNode();
        TestBean testBean = new TestBean();
        testBean.setValue(42);

        contextualSerializer.serialize(testBean, rootNode, new JsonSerializer.DefaultSerializerProvider());
        JsonNode result = rootNode.get("value");

        assertEquals(42, result.intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializerWithNullProperty() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Integer.TYPE, null);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, valueSerializer, false);

        JsonValueSerializer contextualSerializer = jsonValueSerializer.withResolved(null, valueSerializer, false);

        TestBean testBean = new TestBean();
        testBean.setValue(42);

        contextualSerializer.serialize(testBean, null, new JsonSerializer.DefaultSerializerProvider());
    }

    @Test
    public void testJsonValueSerializerWithEnum() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, valueSerializer, false);
        BeanProperty property = new BeanProperty("value", JsonSerializer.EmptyInstance.getClass(), null, null, Color.class);

        JsonValueSerializer contextualSerializer = jsonValueSerializer.withResolved(property, valueSerializer, false);

        EnumTestBean testBean = new EnumTestBean("RED");

        contextualSerializer.serialize(testBean, null, new JsonSerializer.DefaultSerializerProvider());

        JsonNode result = mapper.readTree(new StringReader(contextualSerializer.toString()));

        assertEquals("RED", result.asText());
    }

    /**
     * Enum test bean for testing JsonValueSerializer with Enum types.
     */
    @JsonSerialize(using = ObjectSerializer.class)
    public static class EnumTestBean {
        private Color value;

        public Color getValue() {
            return value;
        }

        public void setValue(Color value) {
            this.value = value;
        }
    }

    public enum Color {
        RED,
        GREEN,
        BLUE
    }
}