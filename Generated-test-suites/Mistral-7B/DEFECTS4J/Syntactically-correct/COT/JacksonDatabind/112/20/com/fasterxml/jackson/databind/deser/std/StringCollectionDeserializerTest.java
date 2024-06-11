package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {
    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        stringCollectionDeserializer = new StringCollectionDeserializer(
                ObjectMapper.valueType(Collection.class, String.class), null, null, null, null, null);
    }

    @Test
    public void testStringCollectionDeserializerConstruction() {
        assertNotNull(stringCollectionDeserializer);
    }

    @Test
    public void testIsCachable() {
        assertTrue(stringCollectionDeserializer.isCachable());
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonDeserializer<String> stringDeserializer = contextualDeserializer(String.class);
        JsonDeserializer<Collection<String>> collectionDeserializer =
                stringCollectionDeserializer.createContextual(mockContext(), null);

        assertEquals(StringCollectionDeserializer.class, collectionDeserializer.getClass());
        assertTrue(collectionDeserializer instanceof ContextualDeserializer);
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<String> stringDeserializer = contextualDeserializer(String.class);
        JsonDeserializer<Collection<String>> collectionDeserializer =
                stringCollectionDeserializer.getContentDeserializer();

        assertEquals(stringDeserializer.getClass(), collectionDeserializer.getClass());
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[\"Hello\", \"World\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullParser() throws Exception {
        JsonParser jsonParser = null;
        stringCollectionDeserializer.deserialize(jsonParser, mockContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnexpectedToken() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("{ \"name\": \"John\" }");
        stringCollectionDeserializer.deserialize(jsonParser, mockContext());
    }

    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithNullValues() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[null]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithCustomValueDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = contextualDeserializer(String.class);
        stringCollectionDeserializer =
                new StringCollectionDeserializer(ObjectMapper.valueType(Collection.class, String.class),
                        null, null, stringDeserializer, null, null);

        JsonParser jsonParser = jsonFactory.createParser("[\"Hello\", \"World\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidContentDeserializer() throws Exception {
        JsonDeserializer<Collection<String>> invalidContentDeserializer = mock(CollectionDeserializer.class);
        when(invalidContentDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenThrow(new JsonMappingException("Test error"));

        stringCollectionDeserializer = new StringCollectionDeserializer(
                ObjectMapper.valueType(Collection.class, String.class), null, null, null, null,
                invalidContentDeserializer);

        JsonParser jsonParser = jsonFactory.createParser("[\"Hello\", \"World\"]");
        stringCollectionDeserializer.deserialize(jsonParser, mockContext());
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws Exception {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class)))
                .thenReturn("NULL_VALUE");

        stringCollectionDeserializer = new StringCollectionDeserializer(
                ObjectMapper.valueType(Collection.class, String.class), null, null, null, null, nullValueProvider);

        JsonParser jsonParser = jsonFactory.createParser("[null]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertEquals(1, result.size());
        assertEquals("NULL_VALUE", result.get(0));
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws Exception {
        JsonDeserializer<Object> delegateDeserializer = contextualDeserializer(Object.class);
        StringCollectionDeserializer stringCollectionDeserializer =
                new StringCollectionDeserializer(ObjectMapper.valueType(Collection.class, String.class),
                        null, delegateDeserializer, null, null, null);

        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertEquals(3, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(String.valueOf(i + 1), result.get(i));
        }
    }

    @Test
    public void testDeserializeWithEmptyDelegateDeserializer() throws Exception {
        JsonDeserializer<Object> emptyDelegateDeserializer = mock(JsonDeserializer.class);

        StringCollectionDeserializer stringCollectionDeserializer =
                new StringCollectionDeserializer(ObjectMapper.valueType(Collection.class, String.class),
                        null, emptyDelegateDeserializer, null, null, null);

        JsonParser jsonParser = jsonFactory.createParser("[\"Hello\", \"World\"]");
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, mockContext());

        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(new ArrayList<>());

        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        Collection<String> result = stringCollectionDeserializer.deserializeWithType(jsonParser, mockContext(), typeDeserializer);

        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithJsonMappingException() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextValue())
                .thenThrow(new JsonMappingException("Test error"));

        StringCollectionDeserializer stringCollectionDeserializer =
                new StringCollectionDeserializer(ObjectMapper.valueType(Collection.class, String.class), null, null, null, null, null);

        try {
            stringCollectionDeserializer.deserialize(jsonParser, mockContext());
            fail();
        } catch (JsonProcessingException e) {
            assertTrue(e.getCause() instanceof JsonMappingException);
        }
    }

    private JsonDeserializer<String> contextualDeserializer(Class<String> stringClass) throws Exception {
        return objectMapper.findValueDeserializer(stringClass, null, null);
    }

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }
}