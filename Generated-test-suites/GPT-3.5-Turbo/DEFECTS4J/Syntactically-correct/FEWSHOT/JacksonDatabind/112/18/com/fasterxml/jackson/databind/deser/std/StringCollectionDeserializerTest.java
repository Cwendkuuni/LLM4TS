package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class StringCollectionDeserializerTest {
    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ValueInstantiator valueInstantiator;

    @Mock
    private JsonDeserializer<?> delegateDeserializer;

    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stringCollectionDeserializer = createDeserializer();
    }

    private StringCollectionDeserializer createDeserializer() {
        return new StringCollectionDeserializer(null, null, null, null, null, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(stringCollectionDeserializer);
    }

    @Test
    public void testIsCachableWithoutCustomDeserializers() {
        assertEquals(true, stringCollectionDeserializer.isCachable());
    }

    @Test
    public void testIsCachableWithCustomDeserializers() {
        StringCollectionDeserializer customDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertEquals(false, customDeserializer.isCachable());
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<Object> contentDeserializer = stringCollectionDeserializer.getContentDeserializer();
        assertNull(contentDeserializer);
    }

    @Test
    public void testGetValueInstantiator() {
        ValueInstantiator valueInstantiator = stringCollectionDeserializer.getValueInstantiator();
        assertNull(valueInstantiator);
    }

    @Test
    public void testDeserialize() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jsonParser.nextTextValue()).thenReturn("value1", "value2", null);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        stringCollectionDeserializer.deserialize(jsonParser, deserializationContext, result);
        assertEquals(Arrays.asList("value1", "value2", null), result);
    }

    @Test(expected = NullPointerException.class)
    public void testDeserializeWithException() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jsonParser.getCurrentToken()).thenThrow(new IOException());
        stringCollectionDeserializer.deserialize(jsonParser, deserializationContext, result);
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.nextTextValue()).thenReturn("value1", "value2", null);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        stringCollectionDeserializer.deserializeUsingCustom(jsonParser, deserializationContext, result, valueDeserializer);
        assertEquals(Arrays.asList("value1", "value2", null), result);
    }

    @Test
    public void testDeserializeUsingCustomWhenValueDeserializerIsNull() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.nextTextValue()).thenReturn("value1", "value2", null);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        stringCollectionDeserializer.deserializeUsingCustom(jsonParser, deserializationContext, result, null);
        assertEquals(Arrays.asList("value1", "value2", null), result);
    }

    @Test
    public void testDeserializeUsingCustomWhenTextValueIsNull() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.nextTextValue()).thenReturn(null);
        stringCollectionDeserializer.deserializeUsingCustom(jsonParser, deserializationContext, result, null);
        assertEquals(Arrays.asList(), result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Object result = stringCollectionDeserializer.deserializeWithType(jsonParser, deserializationContext, Mockito.mock(JsonDeserializer.class));
        assertNull(result);
    }

    @Test
    public void testHandleNonArrayWithUnwrapSingle() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        stringCollectionDeserializer.handleNonArray(jsonParser, deserializationContext, result);
        assertEquals(Arrays.asList(null), result);
    }

    @Test
    public void testHandleNonArrayWithoutUnwrapSingle() throws IOException {
        Collection<String> result = new ArrayList<>();
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        stringCollectionDeserializer.handleNonArray(jsonParser, deserializationContext, result);
        assertEquals(Arrays.asList(), result);
    }
}