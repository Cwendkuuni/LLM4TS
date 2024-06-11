package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_withExpectedStartArrayToken_returnsStringArray() throws Exception {
        //Arrange
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonToken jsonToken = JsonToken.START_ARRAY;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        Object[] chunk = new Object[1];
        chunk[0] = "test";
        String[] expected = new String[1];
        expected[0] = "test";

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(jsonToken);
        when(buffer.resetAndStart()).thenReturn(chunk);
        when(buffer.appendCompletedChunk(chunk)).thenReturn(chunk);
        when(buffer.completeAndClearBuffer(chunk, 1, String.class)).thenReturn(expected);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(buffer);

        //Act
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        verify(jsonParser).isExpectedStartArrayToken();
        verify(jsonParser).nextToken();
        verify(buffer).resetAndStart();
        verify(buffer).appendCompletedChunk(chunk);
        verify(buffer).completeAndClearBuffer(chunk, 1, String.class);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_withCustomElementDeserializer_returnsStringArray() throws Exception {
        //Arrange
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        JsonToken jsonToken = JsonToken.START_ARRAY;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        Object[] chunk = new Object[1];
        chunk[0] = "test";
        String[] expected = new String[1];
        expected[0] = "test";

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(jsonToken);
        when(buffer.resetAndStart()).thenReturn(chunk);
        when(buffer.appendCompletedChunk(chunk)).thenReturn(chunk);
        when(buffer.completeAndClearBuffer(chunk, 1, String.class)).thenReturn(expected);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(buffer);
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        //Act
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        verify(jsonParser).isExpectedStartArrayToken();
        verify(jsonParser).nextToken();
        verify(buffer).resetAndStart();
        verify(buffer).appendCompletedChunk(chunk);
        verify(buffer).completeAndClearBuffer(chunk, 1, String.class);
        verify(elementDeserializer).deserialize(jsonParser, deserializationContext);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType_returnsTypedArray() throws Exception {
        //Arrange
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object[] chunk = new Object[1];
        chunk[0] = "test";
        String[] expected = new String[1];
        expected[0] = "test";

        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(expected);

        //Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        //Assert
        verify(typeDeserializer).deserializeTypedFromArray(jsonParser, deserializationContext);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_handleNonArray_withDisabledFeature_returnsStringArrayWithSingleValue() throws Exception {
        //Arrange
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JsonToken jsonToken = JsonToken.VALUE_NULL;

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        //Act
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        verify(jsonParser).isExpectedStartArrayToken();
        verify(jsonParser).getCurrentToken();
        verify(deserializationContext).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserialize_handleNonArray_withEnabledFeature_returnsStringArrayWithSingleValue() throws Exception {
        //Arrange
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JsonToken jsonToken = JsonToken.VALUE_NULL;

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);

        //Act
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        verify(jsonParser).isExpectedStartArrayToken();
        verify(jsonParser).getCurrentToken();
        verify(deserializationContext).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testContextualDeserialization_whenElementDeserializerIsNull_returnsStringArrayDeserializer() throws Exception {
        //Arrange
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        when(deserializationContext.findContextualValueDeserializer(deserializationContext.constructType(String.class), property)).thenReturn(null);

        //Act
        JsonDeserializer<?> result = deserializer.createContextual(deserializationContext, property);

        //Assert
        verify(deserializationContext).findContextualValueDeserializer(deserializationContext.constructType(String.class), property);

        assertEquals(deserializer, result);
    }

    @Test
    public void testContextualDeserialization_whenElementDeserializerIsDefaultDeserializer_returnsStringArrayDeserializer() throws Exception {
        //Arrange
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        when(deserializationContext.findContextualValueDeserializer(deserializationContext.constructType(String.class), property)).thenReturn(elementDeserializer);
        when(deserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);

        //Act
        JsonDeserializer<?> result = deserializer.createContextual(deserializationContext, property);

        //Assert
        verify(deserializationContext).findContextualValueDeserializer(deserializationContext.constructType(String.class), property);
        verify(deserializer).isDefaultDeserializer(elementDeserializer);

        assertEquals(deserializer, result);
    }

    @Test
    public void testContextualDeserialization_whenElementDeserializerIsNotNull_returnsNewStringArrayDeserializer() throws Exception {
        //Arrange
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        StringArrayDeserializer expected = new StringArrayDeserializer(elementDeserializer);

        when(deserializationContext.findContextualValueDeserializer(deserializationContext.constructType(String.class), property)).thenReturn(elementDeserializer);
        when(deserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);

        //Act
        JsonDeserializer<?> result = deserializer.createContextual(deserializationContext, property);

        //Assert
        verify(deserializationContext).findContextualValueDeserializer(deserializationContext.constructType(String.class), property);
        verify(deserializer).isDefaultDeserializer(elementDeserializer);

        assertEquals(expected, result);
    }
}