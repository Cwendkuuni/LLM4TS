package com.example.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonDeserializer<String> stringDeserializer;
    private JsonDeserializer<Collection<String>> stringCollectionDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.stringDeserializer = mock(JsonDeserializer.class);
        this.stringCollectionDeserializer = new StringCollectionDeserializer(
                null, null, null, this.stringDeserializer, null, null);
        this.jsonParser = mock(JsonParser.class);
        this.deserializationContext = mock(DeserializationContext.class);
        this.typeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testStringCollectionDeserializationWithStandardValueDeserializer() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextTextValue()).thenReturn("hello");
        when(this.jsonParser.nextTextValue()).thenReturn("world");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(this.stringDeserializer.deserialize(this.jsonParser, this.deserializationContext)).thenReturn("test");

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(List.of("hello", "world", "test"), result);
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializer() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextTextValue()).thenReturn("1");
        when(this.jsonParser.nextTextValue()).thenReturn("2");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(this.stringDeserializer.deserialize(this.jsonParser, this.deserializationContext)).thenReturn(1);

        when(this.jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(this.jsonParser.nextTextValue()).thenReturn("three");

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(List.of("1", "2", "three"), result);
    }

    @Test(expected = IOException.class)
    public void testStringCollectionDeserializationWithNullValues() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextTextValue()).thenReturn("hello");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(this.jsonParser.nextTextValue()).thenReturn("world");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(List.of("hello", null, "world"), result);
    }

    @Test(expected = IOException.class)
    public void testStringCollectionDeserializationWithEmptyArray() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(new ArrayList<>(), result);
    }

    @Test(expected = IOException.class)
    public void testStringCollectionDeserializationWithUnexpectedToken() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        throw new AssertionError("Should throw IOException");
    }

    @Test
    public void testStringCollectionDeserializationWithNullValueProvider() throws IOException {
        this.stringCollectionDeserializer = new StringCollectionDeserializer(
                null, null, null, this.stringDeserializer, null, null) {
            @Override
            protected NullValueProvider _nullProvider = null;
        };

        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextTextValue()).thenReturn("hello");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(this.jsonParser.nextTextValue()).thenReturn("world");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(List.of("hello", null, "world"), result);
    }

    @Test
    public void testStringCollectionDeserializationWithEmptyStringCollection() throws IOException {
        ObjectNode jsonNode = ObjectMapper.factory.createObjectNode();
        ArrayNode arrayNode = jsonNode.putArray("value");
        arrayNode.addNull();

        JsonNode json = this.objectMapper.readTree(jsonNode.toString());

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.objectMapper.treeToParser(json), this.deserializationContext);

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testStringCollectionDeserializationWithTypeDeserializer() throws IOException {
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(this.jsonParser.nextTextValue()).thenReturn("1");
        when(this.jsonParser.nextTextValue()).thenReturn("2");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(this.jsonParser.nextFieldName()).thenReturn("type");
        when(this.jsonParser.nextTextValue()).thenReturn("java.util.ArrayList");
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        when(this.jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(this.typeDeserializer.deserializeTypedFromArray(this.jsonParser, this.deserializationContext))
                .thenReturn(List.class);

        Collection<String> result = this.stringCollectionDeserializer.deserialize(
                this.jsonParser, this.deserializationContext);

        assertEquals(List.of("1", "2"), result);
    }
}