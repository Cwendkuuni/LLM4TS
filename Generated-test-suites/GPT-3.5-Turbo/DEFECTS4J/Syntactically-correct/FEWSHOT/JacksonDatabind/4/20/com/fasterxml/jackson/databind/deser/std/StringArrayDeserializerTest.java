package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        // Test case 1
        String[] expected1 = {"foo", "bar"};
        assertArrayEquals(expected1, StringArrayDeserializer.instance.deserialize(jsonParser1, deserializationContext1));

        // Test case 2
        String[] expected2 = null;
        assertArrayEquals(expected2, StringArrayDeserializer.instance.deserialize(jsonParser2, deserializationContext2));

        // Test case 3
        String[] expected3 = {"baz"};
        assertArrayEquals(expected3, StringArrayDeserializer.instance.deserialize(jsonParser3, deserializationContext3));
    }

    @Test
    public void testDeserializeWithType() {
        Object expected = null;
        assertEquals(expected, StringArrayDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testHandleNonArray() {
        // Test case 1
        String[] expected1 = {"qux"};
        assertArrayEquals(expected1, StringArrayDeserializer.instance.handleNonArray(jsonParser1, deserializationContext1));

        // Test case 2
        String[] expected2 = null;
        assertArrayEquals(expected2, StringArrayDeserializer.instance.handleNonArray(jsonParser2, deserializationContext2));
    }

    @Test
    public void testCreateContextual() {
        // Test case 1
        JsonDeserializer<?> expected1 = null;
        assertEquals(expected1, StringArrayDeserializer.instance.createContextual(deserializationContext1, beanProperty1));

        // Test case 2
        JsonDeserializer<?> expected2 = null;
        assertEquals(expected2, StringArrayDeserializer.instance.createContextual(deserializationContext2, beanProperty2));
    }
}