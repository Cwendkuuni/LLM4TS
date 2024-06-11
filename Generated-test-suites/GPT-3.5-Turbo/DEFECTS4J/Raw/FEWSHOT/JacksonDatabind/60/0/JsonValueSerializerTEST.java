package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class JsonValueSerializerTest {
    private JsonValueSerializer serializer;

    @Before
    public void setup() {
        serializer = new JsonValueSerializer(null, null);
    }

    @Test
    public void serialize_nullObject_shouldSerializeNull() {
        // Arrange
        SimpleBean bean = new SimpleBean(null);

        // Act
        JsonNode result = serializer.serialize(bean, null, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void serialize_Object_shouldSerializeValue() {
        // Arrange
        SimpleBean bean = new SimpleBean("value");

        // Act
        JsonNode result = serializer.serialize(bean, null, null);

        // Assert
        assertEquals("value", result);
    }

    private static class SimpleBean {
        private final String value;

        public SimpleBean(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}