package com.fasterxml.jackson.databind.deser.test;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Object.class);
        ObjectMapper mapper = new ObjectMapper();

        // Test with null input
        assertNull(valueInstantiator.createUsingDefault(mapper.getDeserializationContext()));

        // Test with String input
        String jsonString = "\"default\"";
        assertEquals("default", valueInstantiator.createFromString(mapper.getDeserializationContext(), jsonString));

        // Test with Number input
        long number = 1L;
        assertNull(valueInstantiator.createFromInt(mapper.getDeserializationContext(), number));
        assertNull(valueInstantiator.createFromLong(mapper.getDeserializationContext(), number));
        assertNull(valueInstantiator.createFromDouble(mapper.getDeserializationContext(), number));
        assertNull(valueInstantiator.createFromBoolean(mapper.getDeserializationContext(), number > 0));

        // Test with Boolean input
        boolean booleanValue = true;
        assertNotNull(valueInstantiator.createFromBoolean(mapper.getDeserializationContext(), booleanValue));

        // Test with Object input
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        Object object = new Object[]{"test"};
        assertNotNull(valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), javaType.getRawClass(), object));
        assertEquals("[test]", valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), javaType, object).toString());

        // Test with missing instantiator
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        String json = "{\"value\": \"test\"}";
        assertNull(mapper.readValue(json, String.class, mapper.getDeserializationContext()));
    }
}