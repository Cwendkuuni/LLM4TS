package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext context;

    @BeforeClass
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserializeStandard() throws JsonProcessingException, IOException {
        String[] expected = new String[]{"hello", "world"};
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("hello");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("world");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeCustom() throws JsonProcessingException, IOException {
        String[] expected = new String[]{"foo", "bar"};
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("foo");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("bar");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("baz");

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException, IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[0], actual);
    }

    @Test
    public void testDeserializeNull() throws JsonProcessingException, IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertNull(actual);
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException, IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[0], actual);

        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        String[] nullResult = deserializer.handleNonArray(jsonParser, context);
        assertNull(nullResult);
    }

    @Test
    public void testDeserializeCustomWithNull() throws JsonProcessingException, IOException {
        String[] expected = new String[]{"foo", null};
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("foo");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("baz");

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeCustomWithEmptyArray() throws JsonProcessingException, IOException {
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, context)).thenThrow(JsonMappingException.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] actual = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[0], actual);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeInvalid() throws JsonProcessingException, IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jsonParser, context);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeWithType() throws JsonProcessingException, IOException {
        ObjectFactory objectFactory = mock(ObjectFactory.class);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return objectFactory.constructArray(String[].class, Collections.singletonList(new String[]{"foo"}));
            }
        };

        String[] expected = new String[]{"foo"};
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("foo");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object actual = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        assertTrue(actual instanceof String[]);
        assertEquals(Arrays.asList((String[]) actual), Arrays.asList(expected));
    }
}