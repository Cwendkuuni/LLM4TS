package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class StringArrayDeserializerTest {

    @InjectMocks
    private StringArrayDeserializer stringArrayDeserializer;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonDeserializer<String> elementDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Test
    public void testDeserialize() throws IOException {
        // Test input
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        Mockito.when(jsonParser.getText()).thenReturn("test");

        // Performing the test
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        // Verification
        Assert.assertNotNull(result);
        Assert.assertEquals("test", result[0]);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        // Test input
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getText()).thenReturn("");

        // Performing the test
        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);

        // Verification
        Assert.assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithException() throws IOException {
        // Test input
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        // Performing the test
        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }
}