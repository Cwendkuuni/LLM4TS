package com.example; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {

    

    @org.junit.Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        _jsonFactory = objectMapper.getFactory();
        _valueDeserializer = mock(JsonDeserializer.class);
        _valueInstantiator = mock(ValueInstantiator.class);
        _delegateDeserializer = mock(JsonDeserializer.class);
    }

    @org.junit.Test
    public void testStringCollectionDeserializerConstructor() {
        JavaType collectionType = mock(JavaType.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, _valueDeserializer, _valueInstantiator, _delegateDeserializer, null, null);
        assertNotNull(stringCollectionDeserializer);
    }

    @org.junit.Test
    public void testIsCachable() {
        boolean result = new StringCollectionDeserializer(null, null, null, null, null, null).isCachable();
        assertFalse(result);
    }

    @org.junit.Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        List<String> result = new ArrayList<>();
        when(context.findContextualValueDeserializer(JavaType.getTypeCollectionFrom(String.class), property)).thenReturn(_valueDeserializer);

        JsonDeserializer<Object> delegate = new StringCollectionDeserializer(null, null, _valueDeserializer, null, null, null);
        JsonDeserializer<Object> actual = delegate.createContextual(context, property);
        assertEquals(delegate, actual);
    }

    @org.junit.Test
    public void testGetContentDeserializer() {
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        stringCollectionDeserializer._valueDeserializer = jsonDeserializer;
        JsonDeserializer<Object> actual = stringCollectionDeserializer.getContentDeserializer();
        assertEquals(jsonDeserializer, actual);
    }

    @org.junit.Test
    public void testDeserialize() throws JsonProcessingException, IOException {
        JsonParser jsonParser = _jsonFactory.createParser("[\"hello\", \"world\" ]");
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> expected = new ArrayList<>();
        expected.add("hello");
        expected.add("world");

        when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn("hello");
        when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn("world");

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Collection<String> actual = stringCollectionDeserializer.deserialize(jsonParser, context);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testDeserializeWithType() throws JsonProcessingException, IOException {
        JsonParser jsonParser = _jsonFactory.createParser("[1, 2, 3]");
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(typeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn(Collections.singletonList(1));

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        List<String> actual = (List<String>) stringCollectionDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        assertEquals(Collections.singletonList("1"), actual);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValueProvider() throws JsonProcessingException, IOException {
        JsonParser jsonParser = _jsonFactory.createParser("[null]");
        DeserializationContext context = mock(DeserializationContext.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, nullValueProvider, null);
        stringCollectionDeserializer.deserialize(jsonParser, context);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullDelegateDeserializer() throws JsonProcessingException, IOException {
        JsonParser jsonParser = _jsonFactory.createParser("[\"hello\", \"world\" ]");
        DeserializationContext context = mock(DeserializationContext.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        stringCollectionDeserializer._delegateDeserializer = null;
        stringCollectionDeserializer.deserialize(jsonParser, context);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeWithValueDeserializerNull() throws JsonProcessingException, IOException {
        JsonParser jsonParser = _jsonFactory.createParser("[\"hello\", \"world\" ]");
        DeserializationContext context = mock(DeserializationContext.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        stringCollectionDeserializer._valueDeserializer = null;
        stringCollectionDeserializer.deserialize(jsonParser, context);
    }

    @org.junit.Test
    public void testHandleNonArray() throws JsonProcessingException, IOException {
        JsonParser jsonParser = _jsonFactory.createParser("hello");
        DeserializationContext context = mock(DeserializationContext.class);
        List<String> result = new ArrayList<>();

        when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn("hello");

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Collection<String> actual = stringCollectionDeserializer.handleNonArray(jsonParser, context, result);
        assertEquals(Collections.singletonList("hello"), actual);
    }

    

    private static JsonFactory _jsonFactory;
    private static JsonDeserializer<String> _valueDeserializer;
    private static ValueInstantiator _valueInstantiator;
    private static JsonDeserializer<Object> _delegateDeserializer;
}