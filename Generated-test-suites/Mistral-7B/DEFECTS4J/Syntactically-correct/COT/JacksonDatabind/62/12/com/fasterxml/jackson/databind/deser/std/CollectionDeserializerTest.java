package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);

        collectionDeserializer = new CollectionDeserializer(javaType, jsonDeserializer, typeDeserializer, valueInstantiator, null, null);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        objectMapper = mock(ObjectMapper.class);
    }

    @Test
    public void testEmptyArray() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleValue() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Collections.singletonList("test"), result);
    }

    @Test(expected = IOException.class)
    public void testNonArray() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        collectionDeserializer.handleNonArray(jsonParser, deserializationContext, result);
    }

    @Test
    public void testMultipleValues() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Arrays.asList("test1", "test2"), result);
    }

    @Test
    public void testNullValue() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Collections.singleton(null), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnresolvedForwardReference() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        UnresolvedForwardReference forwardReference = mock(UnresolvedForwardReference.class);
        when(forwardReference.getRoid()).thenReturn("testId");

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        collectionDeserializer.handleNonArray(jsonParser, deserializationContext, result);
        collectionDeserializer.resolveForwardReference(forwardReference.getId(), "test3");

        collectionDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullTypeDeserializer() throws Exception {
        TypeDeserializer typeDeserializer = null;
        when(objectMapper.getTypeFactory().constructType(List.class)).thenReturn(new TypeReference<List<String>>() {}.getType());

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(new TypeReference<List<String>>() {}.getType(), null, typeDeserializer, null, null, null);
        collectionDeserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueDeserializer() throws Exception {
        JsonDeserializer<Object> jsonDeserializer = null;
        when(objectMapper.getTypeFactory().constructType(List.class)).thenReturn(new TypeReference<List<String>>() {}.getType());

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(new TypeReference<List<String>>() {}.getType(), jsonDeserializer, null, null, null, null);
        collectionDeserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = null;
        when(objectMapper.getTypeFactory().constructType(List.class)).thenReturn(new TypeReference<List<String>>() {}.getType());

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(new TypeReference<List<String>>() {}.getType(), null, null, valueInstantiator, null, null);
        collectionDeserializer.deserialize(jsonParser, deserializationContext);
    }
}