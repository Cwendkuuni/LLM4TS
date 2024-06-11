package com.yourpackage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        JavaType javaType = mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(Object.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testElement");

        when(valueDeserializer.getNullValue(deserializationContext)).thenReturn(null);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        collectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        verify(jsonParser, times(1)).nextToken();
        verify(valueDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
        assertEquals(1, result.size());
        assertEquals("testElement", result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerInvalidArray() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, valueDeserializer, null, null, null, null);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("testString");

        collectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        verify(jsonParser, times(1)).nextToken();
        verify(jsonParser, times(1)).getText();
        verify(jsonParser, times(1)).getCurrentLocation();
        throw new JsonMappingException("", jsonParser);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerMissingValueDeserializer() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, null, null, null, null, null);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        collectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        verify(jsonParser, times(1)).nextToken();
        throw new JsonMappingException("", jsonParser);
    }

    @Test
    public void testCollectionDeserializerDelegateDeserializer() throws IOException {
        JavaType javaType = mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(Object.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);

        when(valueDeserializer.deserializeWithDelegate(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("testElement");

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        collectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        verify(jsonParser, times(1)).nextToken();
        verify(delegateDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
        assertEquals(1, result.size());
        assertEquals("testElement", result.iterator().next());
    }

    @Test
    public void testCollectionDeserializerValueTypeDeserializer() throws IOException {
        JavaType javaType = mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(Integer.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);

        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), valueTypeDeserializer)).thenReturn(42);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        collectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        verify(jsonParser, times(1)).nextToken();
        verify(valueDeserializer, times(1)).deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
        assertEquals(1, result.size());
        assertEquals(42, result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerUnresolvedForwardReference() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, valueDeserializer, null, null, null, null);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        Object id = new Object();

        when(jsonParser.getValueAsInt()).thenReturn(1);
        when(jsonParser.getValueAsString()).thenReturn("testString");

        collectionDeserializer.deserialize(jsonParser, deserializationContext, result);

        verify(jsonParser, times(1)).nextToken();
        verify(jsonParser, times(1)).getValueAsInt();
        verify(jsonParser, times(1)).getValueAsString();
        verify(jsonParser, times(1)).getCurrentLocation();
        verify(deserializationContext, times(1)).handleUnresolvedForwardReference(any(Object.class));
        throw new JsonMappingException("", jsonParser);
    }
}