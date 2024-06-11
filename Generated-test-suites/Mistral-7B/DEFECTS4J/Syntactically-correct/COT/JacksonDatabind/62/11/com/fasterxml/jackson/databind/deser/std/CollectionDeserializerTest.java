package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Tests for {@link CollectionDeserializer} class.
 */
public class CollectionDeserializerTest {

    private static final String JSON_ARRAY = "[]";
    private static final String JSON_EMPTY_STRING = "\"\"";
    private static final String JSON_NULL = "null";
    private static final String JSON_SINGLE_VALUE = "1";
    private static final String JSON_SINGLE_VALUE_WITH_TYPE = "[1]";
    private static final String JSON_WRAPPED_SINGLE_VALUE = "[1]";
    private static final String JSON_OBJECT = "{\"key\":1}";

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        collectionDeserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, Object.class),
                mock(JsonDeserializer.class),
                null,
                mock(ValueInstantiator.class),
                null,
                null);
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_ARRAY.getBytes());
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, null);
        assertArrayEquals(Collections.emptyList().toArray(), result.toArray());
    }

    @Test
    public void testEmptyString() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_EMPTY_STRING.getBytes());
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, null);
        assertArrayEquals(Collections.emptyList().toArray(), result.toArray());
    }

    @Test
    public void testNull() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_NULL.getBytes());
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, null);
        assertNull(result);
    }

    @Test
    public void testSingleValue() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_SINGLE_VALUE.getBytes());
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(jsonParser, null)).thenReturn(1);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, Object.class),
                valueDeserializer,
                valueTypeDeserializer,
                mock(ValueInstantiator.class),
                null,
                null);
        Collection<Object> result = deserializer.deserialize(jsonParser, null);
        assertSame(1, result.iterator().next());
    }

    @Test
    public void testSingleValueWithType() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_SINGLE_VALUE_WITH_TYPE.getBytes());
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserializeWithType(jsonParser, null, any(TypeDeserializer.class)))
                .thenReturn(1);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, Object.class),
                valueDeserializer,
                valueTypeDeserializer,
                mock(ValueInstantiator.class),
                null,
                null);
        Collection<Object> result = deserializer.deserialize(jsonParser, null);
        assertSame(1, result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_OBJECT.getBytes());
        CollectionDeserializer deserializer = collectionDeserializer;
        deserializer.deserialize(jsonParser, null);
    }

    @Test(expected = IOException.class)
    public void testExceptionDuringDeserialization() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(new byte[0]);
        doThrow(IOException.class).when(mock(JsonDeserializer.class)).deserialize(any(), any());
        CollectionDeserializer deserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, Object.class),
                mock(JsonDeserializer.class),
                null,
                mock(ValueInstantiator.class),
                null,
                null);
        deserializer.deserialize(jsonParser, null);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testUnresolvedForwardReference() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(JSON_WRAPPED_SINGLE_VALUE.getBytes());
        CollectionDeserializer deserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, Object.class),
                mock(JsonDeserializer.class),
                null,
                mock(ValueInstantiator.class),
                null,
                null);
        Object id = new Object();
        deserializer.handleNonArray(jsonParser, null, new ArrayList<Object>());
        deserializer.resolveForwardReference(id, 1);
    }
}