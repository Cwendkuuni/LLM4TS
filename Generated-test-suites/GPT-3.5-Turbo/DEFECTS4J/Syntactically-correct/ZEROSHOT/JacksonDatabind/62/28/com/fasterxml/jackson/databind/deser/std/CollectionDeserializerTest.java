package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        // Arrange
        JavaType collectionType = Mockito.mock(CollectionType.class);
        JsonDeserializer<Object> valueDeser = Mockito.mock(JsonDeserializer.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Collection<Object> result = Mockito.mock(Collection.class);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(collectionType, valueDeser, null, null);
        when(collectionDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .thenCallRealMethod();
        when(collectionDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class), Mockito.any(Collection.class)))
                .thenReturn(result);

        // Act
        collectionDeserializer.deserialize(p, ctxt);

        // Assert
        verify(p, times(1)).isExpectedStartArrayToken();
        verify(p, times(1)).setCurrentValue(result);
        verify(p, times(1)).nextToken();
        verify(p, never()).getText();
        verify(valueDeser, times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        // Arrange
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, null, null, null);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        // Act
        collectionDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        // Assert
        verify(typeDeserializer, times(1)).deserializeTypedFromArray(jp, ctxt);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        // Arrange
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, null, null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Collection<Object> result = Mockito.mock(Collection.class);

        // Act & Assert
        collectionDeserializer.handleNonArray(p, ctxt, result);
    }
}