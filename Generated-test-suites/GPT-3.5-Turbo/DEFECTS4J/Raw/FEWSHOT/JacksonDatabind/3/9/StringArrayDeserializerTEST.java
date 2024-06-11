package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        // Test case 1: input is an array of strings
        JsonParser jp1 = null; // Add appropriate initialization here
        DeserializationContext ctxt1 = null; // Add appropriate initialization here
        String[] expectedResult1 = null; // Add appropriate initialization here
        String[] result1 = StringArrayDeserializer.instance.deserialize(jp1, ctxt1);
        assertArrayEquals(expectedResult1, result1);

        // Test case 2: input is an array of integers
        JsonParser jp2 = null; // Add appropriate initialization here
        DeserializationContext ctxt2 = null; // Add appropriate initialization here
        String[] expectedResult2 = null; // Add appropriate initialization here
        String[] result2 = StringArrayDeserializer.instance.deserialize(jp2, ctxt2);
        assertArrayEquals(expectedResult2, result2);
    }

    @Test
    public void testDeserializeWithType() {
        // Test case 1: input is an array of strings
        JsonParser jp1 = null; // Add appropriate initialization here
        DeserializationContext ctxt1 = null; // Add appropriate initialization here
        TypeDeserializer typeDeserializer1 = null; // Add appropriate initialization here
        Object expectedResult1 = null; // Add appropriate initialization here
        Object result1 = StringArrayDeserializer.instance.deserializeWithType(jp1, ctxt1, typeDeserializer1);
        assertEquals(expectedResult1, result1);

        // Test case 2: input is an array of integers
        JsonParser jp2 = null; // Add appropriate initialization here
        DeserializationContext ctxt2 = null; // Add appropriate initialization here
        TypeDeserializer typeDeserializer2 = null; // Add appropriate initialization here
        Object expectedResult2 = null; // Add appropriate initialization here
        Object result2 = StringArrayDeserializer.instance.deserializeWithType(jp2, ctxt2, typeDeserializer2);
        assertEquals(expectedResult2, result2);
    }

    @Test
    public void testHandleNonArray() {
        // Test case 1: current token is VALUE_NULL
        JsonParser jp1 = null; // Add appropriate initialization here
        DeserializationContext ctxt1 = null; // Add appropriate initialization here
        String[] expectedResult1 = null; // Add appropriate initialization here
        String[] result1 = StringArrayDeserializer.instance.handleNonArray(jp1, ctxt1);
        assertArrayEquals(expectedResult1, result1);

        // Test case 2: current token is VALUE_STRING and ACCEPT_EMPTY_STRING_AS_NULL_OBJECT enabled
        JsonParser jp2 = null; // Add appropriate initialization here
        DeserializationContext ctxt2 = null; // Add appropriate initialization here
        String[] expectedResult2 = null; // Add appropriate initialization here
        String[] result2 = StringArrayDeserializer.instance.handleNonArray(jp2, ctxt2);
        assertArrayEquals(expectedResult2, result2);

        // Test case 3: current token is VALUE_STRING and ACCEPT_EMPTY_STRING_AS_NULL_OBJECT disabled
        JsonParser jp3 = null; // Add appropriate initialization here
        DeserializationContext ctxt3 = null; // Add appropriate initialization here
        String[] expectedResult3 = null; // Add appropriate initialization here
        String[] result3 = StringArrayDeserializer.instance.handleNonArray(jp3, ctxt3);
        assertArrayEquals(expectedResult3, result3);

        // Test case 4: current token is not VALUE_NULL or VALUE_STRING and ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
        JsonParser jp4 = null; // Add appropriate initialization here
        DeserializationContext ctxt4 = null; // Add appropriate initialization here
        String[] expectedResult4 = null; // Add appropriate initialization here
        String[] result4 = StringArrayDeserializer.instance.handleNonArray(jp4, ctxt4);
        assertArrayEquals(expectedResult4, result4);

        // Test case 5: current token is not VALUE_NULL or VALUE_STRING and ACCEPT_SINGLE_VALUE_AS_ARRAY disabled
        JsonParser jp5 = null; // Add appropriate initialization here
        DeserializationContext ctxt5 = null; // Add appropriate initialization here
        String[] expectedResult5 = null; // Add appropriate initialization here
        String[] result5 = StringArrayDeserializer.instance.handleNonArray(jp5, ctxt5);
        assertArrayEquals(expectedResult5, result5);
    }

    @Test
    public void test_deserializeCustom() {
        // Test case 1: input is an array of strings
        JsonParser jp1 = null; // Add appropriate initialization here
        DeserializationContext ctxt1 = null; // Add appropriate initialization here
        String[] expectedResult1 = null; // Add appropriate initialization here
        String[] result1 = StringArrayDeserializer.instance._deserializeCustom(jp1, ctxt1);
        assertArrayEquals(expectedResult1, result1);

        // Test case 2: input is an array of integers
        JsonParser jp2 = null; // Add appropriate initialization here
        DeserializationContext ctxt2 = null; // Add appropriate initialization here
        String[] expectedResult2 = null; // Add appropriate initialization here
        String[] result2 = StringArrayDeserializer.instance._deserializeCustom(jp2, ctxt2);
        assertArrayEquals(expectedResult2, result2);
    }

    @Test
    public void testCreateContextual() {
        // Test case 1: _elementDeserializer is null
        DeserializationContext ctxt1 = null; // Add appropriate initialization here
        BeanProperty property1 = null; // Add appropriate initialization here
        JsonDeserializer<?> expectedResult1 = null; // Add appropriate initialization here
        JsonDeserializer<?> result1 = StringArrayDeserializer.instance.createContextual(ctxt1, property1);
        assertEquals(expectedResult1, result1);

        // Test case 2: _elementDeserializer is not null, and is default deserializer
        DeserializationContext ctxt2 = null; // Add appropriate initialization here
        BeanProperty property2 = null; // Add appropriate initialization here
        JsonDeserializer<?> expectedResult2 = null; // Add appropriate initialization here
        JsonDeserializer<?> result2 = StringArrayDeserializer.instance.createContextual(ctxt2, property2);
        assertEquals(expectedResult2, result2);

        // Test case 3: _elementDeserializer is not null, and is not default deserializer
        DeserializationContext ctxt3 = null; // Add appropriate initialization here
        BeanProperty property3 = null; // Add appropriate initialization here
        JsonDeserializer<?> expectedResult3 = null; // Add appropriate initialization here
        JsonDeserializer<?> result3 = StringArrayDeserializer.instance.createContextual(ctxt3, property3);
        assertEquals(expectedResult3, result3);
    }

}