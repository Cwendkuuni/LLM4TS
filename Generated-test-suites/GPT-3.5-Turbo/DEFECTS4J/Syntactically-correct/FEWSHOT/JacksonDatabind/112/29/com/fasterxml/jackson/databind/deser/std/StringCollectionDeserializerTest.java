package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class StringCollectionDeserializerTest {

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private ValueInstantiator valueInstantiator;

    private StringCollectionDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringCollectionDeserializer(null, valueInstantiator, null, null, null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        Collection<String> result = new ArrayList<>();
        result.add("foo");
        result.add("bar");

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("foo", "bar", null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> deserialized = deserializer.deserialize(parser, ctxt, new ArrayList<>());

        assertEquals(result, deserialized);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        Collection<String> result = new ArrayList<>();
        result.add("foo");
        result.add(null);

        deserializer = new StringCollectionDeserializer(null, valueInstantiator, null, null, null, null);
        deserializer.setNullValueProvider(NullValueProvider.forValue(null));

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("foo", null, null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> deserialized = deserializer.deserialize(parser, ctxt, new ArrayList<>());

        assertEquals(result, deserialized);
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        Collection<String> result = new ArrayList<>();
        result.add("foo");
        result.add("bar");

        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("foo", "bar");
        deserializer = deserializer.withResolved(null, valueDeserializer, null, null);

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn(null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> deserialized = deserializer.deserialize(parser, ctxt, new ArrayList<>());

        assertEquals(result, deserialized);
        verify(valueDeserializer, times(2)).deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(parser, ctxt)).thenReturn(new ArrayList<>());

        Object deserialized = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);

        assertNull(deserialized);
        verify(typeDeserializer).deserializeTypedFromArray(parser, ctxt);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        Collection<String> result = new ArrayList<>();
        result.add("foo");

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("foo");

        Collection<String> deserialized = deserializer.handleNonArray(parser, ctxt, new ArrayList<>());

        assertEquals(result, deserialized);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayCannotWrap() throws IOException {
        deserializer.setUnwrapSingle(Boolean.FALSE);

        deserializer.handleNonArray(parser, ctxt, new ArrayList<>());
    }
}