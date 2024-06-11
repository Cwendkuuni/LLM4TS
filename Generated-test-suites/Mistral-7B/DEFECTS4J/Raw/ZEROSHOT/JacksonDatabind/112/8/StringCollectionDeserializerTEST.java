package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonFormat(features = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class StringCollectionDeserializerTest {

    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeCollectionEmpty() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null,
                null, null, null);
        List<String> result = deserializer.deserialize(mockParser, mockContext);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeCollectionWithElements() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null,
                null, null, null);

        when(mockParser.nextTextValue()).thenReturn("element1");
        when(mockParser.nextTextValue()).thenReturn("element2");
        when(mockParser.nextTextValue()).thenReturn(null);

        List<String> result = deserializer.deserialize(mockParser, mockContext);
        assertEquals(Arrays.asList("element1", "element2"), result);
    }

    @Test
    public void testDeserializeCollectionWithNullValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null,
                null, null, null);

        when(mockParser.nextTextValue()).thenReturn("element1");
        when(mockParser.nextTextValue()).thenReturn(null);

        List<String> result = deserializer.deserialize(mockParser, mockContext);
        assertNull(result.get(1));
        assertEquals(1, result.size());
    }

    @Test
    public void testDeserializeCollectionWithCustomDeserializer() throws IOException {
        JsonDeserializer<String> mockDeserializer = mock(JsonDeserializer.class);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn("mockValue");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null,
                mockDeserializer, null, null);

        when(mockParser.nextTextValue()).thenReturn("element1");
        when(mockParser.nextTextValue()).thenReturn("element2");

        List<String> result = deserializer.deserialize(mockParser, mockContext);
        assertEquals(Arrays.asList("mockValue", "mockValue"), result);
    }

    @Test
    public void testDeserializeCollectionWithTypeDeserializer() throws IOException {
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
        when(mockTypeDeserializer.deserializeTypedFromArray(mockParser, mockContext)).thenReturn("mockedValue");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null,
                null, mockTypeDeserializer, null);

        when(mockParser.nextTextValue()).thenReturn("element1");

        List<String> result = deserializer.deserialize(mockParser, mockContext);
        assertEquals(Collections.singletonList("mockedValue"), result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeCollectionWithUnexpectedToken() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null,
                null, null, null);

        when(mockParser.nextTextValue()).thenReturn("element1");
        when(mockParser.nextValue()).thenReturn(new NumberInt(1));

        deserializer.deserialize(mockParser, mockContext);
    }
}