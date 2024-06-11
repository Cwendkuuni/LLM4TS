package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();

    @Test
    public void testDeserialize() {
        // Test case 1: Empty array
        String[] result1 = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{}, result1);

        // Test case 2: Array with one element
        String[] result2 = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{"hello"}, result2);

        // Test case 3: Array with multiple elements
        String[] result3 = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{"hello", "world"}, result3);
    }

    @Test
    public void testDeserializeWithType() {
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    // Other methods can be tested in the same way as above

}