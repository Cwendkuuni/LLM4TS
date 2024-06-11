package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
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
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] expectedResult = null;
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] expectedResult = null;
        String[] result = stringArrayDeserializer._deserializeCustom(jp, ctxt);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        Object expectedResult = null;
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] expectedResult = null;
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        JsonDeserializer expectedResult = null;
        JsonDeserializer result = stringArrayDeserializer.createContextual(ctxt, property);
        assertEquals(expectedResult, result);
    }
}