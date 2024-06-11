
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private JavaType stringCollectionType;

    @Before
    public void setUp() {
        stringCollectionType = mock(JavaType.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        JsonDeserializer<?> delegateDeserializer = mock(JsonDeserializer.class);
        deserializer = new StringCollectionDeserializer(stringCollectionType, valueDeserializer, valueInstantiator);
        deserializer._delegateDeserializer = delegateDeserializer;
    }

    @Test
    public void testDeserialize_WhenDelegateDeserializerNonNull_CallsDelegateDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = mock(Collection.class);

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(deserializer.deserializeUsingCustom(any(JsonParser.class), any(DeserializationContext.class),
                any(Collection.class), any(JsonDeserializer.class))).thenReturn(result);

        Collection<String> deserializedCollection = deserializer.deserialize(p, ctxt);

        verify(deserializer, never()).deserialize(p, ctxt, result);
        verify(result, never()).add(anyString());
        Assert.assertEquals(result, deserializedCollection);
    }

    @Test
    public void testDeserialize_WhenDelegateDeserializerNull_CallsDeserializeWithDefault() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = mock(Collection.class);

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(deserializer.handleNonArray(p, ctxt, result)).thenReturn(result);

        Collection<String> deserializedCollection = deserializer.deserialize(p, ctxt);

        verify(deserializer, times(1)).handleNonArray(p, ctxt, result);
        verify(result, never()).add(anyString());
        Assert.assertEquals(result, deserializedCollection);
    }

    @Test
    public void testDeserialize_WhenDelegateDeserializerNullAndStartArrayToken_CallsDeserializeWithDefault() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        result.add("test");

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(deserializer.deserializeUsingCustom(any(JsonParser.class), any(DeserializationContext.class),
                any(Collection.class), any(JsonDeserializer.class))).thenReturn(result);

        Collection<String> deserializedCollection = deserializer.deserialize(p, ctxt);

        verify(deserializer, never()).deserializeUsingCustom(any(JsonParser.class), any(DeserializationContext.class),
                any(Collection.class), any(JsonDeserializer.class));
        verify(result, times(1)).add(anyString());
        Assert.assertEquals(result, deserializedCollection);
    }

    @Test
    public void testDeserialize_WhenStringIsNull_NullProviderIsUsed() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(p.nextTextValue()).thenReturn(null);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonParser.Token.VALUE_NULL);
        Mockito.when(deserializer._skipNullValues).thenReturn(false);
        Mockito.when(deserializer._nullProvider.getNullValue(ctxt)).thenReturn(null);

        Collection<String> deserializedCollection = deserializer.deserialize(p, ctxt, result);

        verify(deserializer, never()).deserializeUsingCustom(any(JsonParser.class), any(DeserializationContext.class),
                any(Collection.class), any(JsonDeserializer.class));
        Assert.assertNull(deserializedCollection.iterator().next());
    }

    @Test
    public void testDeserialize_WhenStringIsNotNull_AndValueDeserializerIsNull_StringIsAdded() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(p.nextTextValue()).thenReturn("test");
        Mockito.when(p.getCurrentToken()).thenReturn(JsonParser.Token.VALUE_STRING);
        Mockito.when(deserializer._valueDeserializer).thenReturn(null);

        Collection<String> deserializedCollection = deserializer.deserialize(p, ctxt, result);

        verify(deserializer, never()).deserializeUsingCustom(any(JsonParser.class), any(DeserializationContext.class),
                any(Collection.class), any(JsonDeserializer.class));
        Assert.assertEquals("test", deserializedCollection.iterator().next());
    }

    @Test
    public void testDeserialize_WhenStringIsNotNull_AndValueDeserializerIsNotNull_CustomDeserializerIsUsed() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(p.nextTextValue()).thenReturn("test");
        Mockito.when(p.getCurrentToken()).thenReturn(JsonParser.Token.VALUE_STRING);
        Mockito.when(deserializer._valueDeserializer).thenReturn(valueDeserializer);

        Collection<String> deserializedCollection = deserializer.deserialize(p, ctxt, result);

        verify(deserializer, times(1)).deserializeUsingCustom(any(JsonParser.class), any(DeserializationContext.class),
                any(Collection.class), any(JsonDeserializer.class));
        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
        Assert.assertNull(deserializedCollection.iterator().next());
    }

    @Test
    public void testDeserialize_WhenValueDeserializerIsNotNull_ContentIsDeserializedUsingCustomDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = mock(Collection.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(valueDeserializer.deserialize(p, ctxt)).thenReturn("test");
        Mockito.when(deserializer._valueDeserializer).thenReturn(valueDeserializer);

        Collection<String> deserializedCollection = deserializer.deserializeUsingCustom(p, ctxt, result, valueDeserializer);

        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
        Assert.assertEquals("test", deserializedCollection.iterator().next());
    }
}
