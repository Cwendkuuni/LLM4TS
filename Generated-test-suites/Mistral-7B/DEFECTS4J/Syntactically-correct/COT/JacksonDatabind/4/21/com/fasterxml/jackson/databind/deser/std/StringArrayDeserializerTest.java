package com.example.junit.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import java.io.IOException;
import java.util.Arrays;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private JsonParser parser;
    private DeserializationContext context;
    private ObjectBuffer buffer;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        buffer = mock(ObjectBuffer.class);
    }

    @Test
    public void testDeserialize() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.getText()).thenReturn("value1,value2,value3");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, context);

        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(context.leaseObjectBuffer()).thenReturn(buffer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));

        Object[] chunk = new Object[3];
        String[] expectedResult = new String[]{"value1", "value2", "value3"};

        deserializer.deserialize(parser, context);

        assertArrayEquals(expectedResult, Arrays.copyOf((Object[])chunk, expectedResult.length));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn(new String[]{"value1", "value2"});

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(parser, context, typeDeserializer);

        assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("value");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(parser, context);

        assertArrayEquals(new String[]{"value"}, result);
        assertNull(result[0]);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWhenExpectedStartArrayTokenIsMissing() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(parser, context);

        fail("Expected IOException but got: " + deserializer.getClass().getName());
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        when(context.findContextualValueDeserializer(Object.class, null)).thenReturn(jsonDeserializer);

        JsonDeserializer<?> result = deserializer.createContextual(context, null);

        assertSame(new StringArrayDeserializer(jsonDeserializer), result);
    }
}