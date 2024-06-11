package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.apache.commons.cli.Util;

public class StringCollectionDeserializerTest {
    private JsonParser p;
    private DeserializationContext ctxt;
    private Collection<String> result;

    private JsonDeserializer<String> valueDeser;
    private Collection<String> collection;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeser;
    private NullValueProvider nuller;
    private Boolean unwrapSingle;

    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setup() {
        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        result = new ArrayList<String>();

        valueDeser = mock(JsonDeserializer.class);
        collection = new ArrayList<String>();
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeser = mock(JsonDeserializer.class);
        nuller = mock(NullValueProvider.class);
        unwrapSingle = false;

        stringCollectionDeserializer = new StringCollectionDeserializer(JavaType,
                valueDeser, valueInstantiator);
    }

    @Test
    public void testDeserialize() throws IOException {
        when(p.getCurrentToken()).thenReturn(Token.VALUE_STRING);
        when(p.nextTextValue()).thenReturn("foo").thenReturn(null);
        when(p.isExpectedStartArrayToken()).thenReturn(false);
        when(valueDeser.deserialize(p, ctxt)).thenReturn("foo");

        Collection<String> result = stringCollectionDeserializer.deserialize(p, ctxt);
        collection.add("foo");

        assertEquals(collection, result);

        verify(p, times(2)).nextTextValue();
        verify(p).getCurrentToken();
        verify(p).isExpectedStartArrayToken();
        verify(valueDeser, times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        stringCollectionDeserializer.deserializeWithType(p, ctxt, typeDeserializer);

        verify(typeDeserializer).deserializeTypedFromArray(p, ctxt);
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        when(p.nextTextValue()).thenReturn("foo").thenReturn(null);
        when(valueDeser.deserialize(p, ctxt)).thenReturn("foo");

        stringCollectionDeserializer.deserializeUsingCustom(p, ctxt, result, valueDeser);

        collection.add("foo");

        assertEquals(collection, result);

        verify(p, times(2)).nextTextValue();
        verify(valueDeser, times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeUsingCustomNullValue() throws IOException {
        when(p.nextTextValue()).thenReturn(null);
        when(p.getCurrentToken()).thenReturn(Token.VALUE_NULL);
        when(_skipNullValues).thenReturn(true);
        when(_nullProvider.getNullValue(ctxt)).thenReturn(null);

        stringCollectionDeserializer.deserializeUsingCustom(p, ctxt, result, valueDeser);

        verify(p, times(1)).nextTextValue();
        verify(_skipNullValues, times(1)).thenReturn(true);
        verify(_nullProvider, times(1)).getNullValue(ctxt);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        when(p.getCurrentToken()).thenReturn(Token.VALUE_STRING);
        when(p.nextTextValue()).thenReturn("foo").thenReturn(null);
        when(_unwrapSingle).thenReturn(true);
        when(_parseString(p, ctxt)).thenReturn("foo");

        stringCollectionDeserializer.handleNonArray(p, ctxt, result);

        collection.add("foo");

        assertEquals(collection, result);

        verify(p, times(2)).nextTextValue();
        verify(p).getCurrentToken();
        verify(_unwrapSingle).thenReturn(true);
        verify(_parseString(p, ctxt)).thenReturn("foo");
    }

    @Test
    public void testHandleNonArrayNullValue() throws IOException {
        when(p.getCurrentToken()).thenReturn(Token.VALUE_NULL);
        when(_unwrapSingle).thenReturn(false);
        when(_skipNullValues).thenReturn(true);
        when(_nullProvider.getNullValue(ctxt)).thenReturn(null);

        stringCollectionDeserializer.handleNonArray(p, ctxt, result);

        verify(p).getCurrentToken();
        verify(_unwrapSingle).thenReturn(false);
        verify(_skipNullValues).thenReturn(true);
        verify(_nullProvider).getNullValue(ctxt);
    }
}