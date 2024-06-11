package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        // Test case 1: Empty array
        String[] result1 = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{}, result1);

        // Test case 2: Array with null values
        String[] result2 = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{null, null, null}, result2);

        // Test case 3: Array with string values
        String[] result3 = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{"string1", "string2", "string3"}, result3);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        // Test case 1: Empty array
        Object result1 = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result1);

        // Test case 2: Array with null values
        Object result2 = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertArrayEquals(new String[]{null, null, null}, (String[]) result2);

        // Test case 3: Array with string values
        Object result3 = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertArrayEquals(new String[]{"string1", "string2", "string3"}, (String[]) result3);
    }

    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setUpMocks() {
        jsonParser = new JsonParser();
        deserializationContext = new DeserializationContext();
        typeDeserializer = new TypeDeserializer();
    }
}