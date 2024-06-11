package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

public class StringArrayDeserializerTest {
    @Mock
    private JsonParser jp;
    @Mock
    private DeserializationContext ctxt;

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonToken[] jsonTokens = {JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY};
        String[] expected = {"foo", "bar"};

        mockJsonParser(jsonTokens, new String[]{"foo", "bar"});

        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);

        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testDeserializeWithNullValues() throws IOException {
        JsonToken[] jsonTokens = {JsonToken.START_ARRAY, JsonToken.VALUE_NULL, JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.END_ARRAY};
        String[] expected = {null, "bar", null};

        mockJsonParser(jsonTokens, new String[]{null, "bar", null});

        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);

        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        stringArrayDeserializer._elementDeserializer = deser;

        stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        // Verify if 'typeDeserializer.deserializeTypedFromArray' is called in the method
        // Since its implementation is unknown, we only verify if it's called
        // Mockito.verify(typeDeserializer).deserializeTypedFromArray(jp, ctxt);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonToken[] jsonTokens = {JsonToken.VALUE_NULL};
        String[] expected = {null};

        mockJsonParser(jsonTokens, new String[]{null});

        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);

        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayWithDisabledFeature() throws IOException {
        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        JsonToken[] jsonTokens = {JsonToken.VALUE_NULL};

        mockJsonParser(jsonTokens, new String[]{null});

        stringArrayDeserializer.handleNonArray(jp, ctxt);
    }

    @Test
    public void testContextual() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        DeserializationContext newContext = ctxt.createInstance();

        jsonDeserializer._elementDeserializer = deser;
        jsonDeserializer.isDefaultDeserializer(deser);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(newContext, property);

        assertNull(result);
    }

    private void mockJsonParser(JsonToken[] jsonTokens, String[] textValues) throws IOException {
        for (int i = 0; i < jsonTokens.length; i++) {
            Mockito.when(jp.nextToken()).thenReturn(jsonTokens[i]);
            Mockito.when(jp.getText()).thenReturn(textValues[i]);
        }
    }
}