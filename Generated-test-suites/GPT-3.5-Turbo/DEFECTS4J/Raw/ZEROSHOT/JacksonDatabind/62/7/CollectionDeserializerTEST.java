package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        JavaType collectionType = mock(JavaType.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null);

        deserializer.deserialize(p, ctxt, result);

        verify(p).isExpectedStartArrayToken();
        verify(p).setCurrentValue(result);
        verify(p, times(2)).nextToken();
        verify(p).getValueAsText();

        verify(ctxt).isEnabled(any(DeserializationFeature.class));

        verify(result).add(any(Object.class));

        verifyNoMoreInteractions(p, ctxt, result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<Object> result = mock(Collection.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        deserializer.handleNonArray(p, ctxt, result);

        verify(ctxt).mappingException(any(Class.class));
        verify(ctxt).isEnabled(any(DeserializationFeature.class));

        verify(result).add(any(Object.class));

        verifyNoMoreInteractions(p, ctxt, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        verify(jp).getCurrentToken();

        verify(typeDeserializer).deserializeTypedFromArray(jp, ctxt);

        verifyNoMoreInteractions(jp, ctxt, typeDeserializer);
    }
}