package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer instance = StringArrayDeserializer.instance;

        // Test when jp.isExpectedStartArrayToken() returns true
        jp = mock(JsonParser.class);
        jp.isExpectedStartArrayToken();
        jp.nextToken(JsonToken.START_ARRAY);

        instance.deserialize(jp, ctxt);

        // Test when jp.isExpectedStartArrayToken() returns false
        jp = mock(JsonParser.class);
        jp.isExpectedStartArrayToken();
        jp.nextToken(JsonToken.VALUE_STRING);
        jp.getText();

        instance.deserialize(jp, ctxt);

    }

    @Test
    public void test_deserializeCustom() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer instance = StringArrayDeserializer.instance;

        // Test when jp.nextToken() returns JsonToken.END_ARRAY
        jp = mock(JsonParser.class);
        jp.nextToken();
        jp.nextToken(JsonToken.END_ARRAY);

        instance._deserializeCustom(jp, ctxt);

        // Test when jp.nextToken() returns other than JsonToken.END_ARRAY
        jp = mock(JsonParser.class);
        jp.nextToken();
        jp.nextToken(JsonToken.VALUE_NULL);

        instance._deserializeCustom(jp, ctxt);

    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer instance = StringArrayDeserializer.instance;

        instance.deserializeWithType(jp, ctxt, typeDeserializer);

    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer instance = StringArrayDeserializer.instance;

        // Test when ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY) returns false
        ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        instance.handleNonArray(jp, ctxt);

        // Test when ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY) returns true and jp.getCurrentToken() returns JsonToken.VALUE_STRING
        ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        jp.getCurrentToken();
        jp.getText();

        instance.handleNonArray(jp, ctxt);

    }

    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        StringArrayDeserializer instance = StringArrayDeserializer.instance;

        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        instance.createContextual(ctxt, property);

        JsonDeserializer deserializer2 = instance.createContextual(ctxt, property);

    }
}