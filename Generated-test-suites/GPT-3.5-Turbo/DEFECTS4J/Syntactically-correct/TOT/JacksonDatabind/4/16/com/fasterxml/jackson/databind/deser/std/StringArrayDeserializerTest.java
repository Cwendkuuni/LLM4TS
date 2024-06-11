package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

  private StringArrayDeserializer stringArrayDeserializer;

  @Test
  public void testDeserialize_withStringValue() throws IOException {

    // Arrange
    String[] expected = { "value1", "value2", "value3" };
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonParser jsonParser = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);

    // Act
    when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
    when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
        JsonToken.END_ARRAY);
    when(jsonParser.getText()).thenReturn(expected[0], expected[1], expected[2]);

    String[] actual = stringArrayDeserializer.deserialize(jsonParser, ctxt);

    // Assert
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testDeserialize_withNullValue() throws IOException {

    // Arrange
    String[] expected = { null, null, null };
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonParser jsonParser = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);

    // Act
    when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
    when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.VALUE_NULL, JsonToken.VALUE_NULL,
        JsonToken.END_ARRAY);

    String[] actual = stringArrayDeserializer.deserialize(jsonParser, ctxt);

    // Assert
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testDeserialize_withNonStringValue() throws IOException {

    // Arrange
    String[] expected = { "value1", "value2", "value3" };
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonParser jsonParser = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
    JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

    // Act
    when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
    when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_INT,
        JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);

    when(jsonDeserializer.deserialize(jsonParser, ctxt)).thenReturn("value1", "value2", "value3");
    when(jsonDeserializer.getNullValue()).thenReturn(null);

    ObjectBuffer buffer = mock(ObjectBuffer.class);
    Object[] chunk = { "value1", "value2", "value3" };

    when(buffer.resetAndStart()).thenReturn(chunk);
    when(buffer.appendCompletedChunk(chunk)).thenReturn(chunk);

    when(buffer.completeAndClearBuffer(chunk, 3, String.class)).thenReturn(expected);
    when(ctxt.leaseObjectBuffer()).thenReturn(buffer);

    String[] actual = stringArrayDeserializer.deserialize(jsonParser, ctxt);

    // Assert
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testDeserialize_withNonArray() throws IOException {

    // Arrange
    String[] expected = { "value1" };
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonParser jsonParser = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);

    // Act
    when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
    when(jsonParser.getText()).thenReturn(expected[0]);

    when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

    try {
      stringArrayDeserializer.deserialize(jsonParser, ctxt);
    } catch (JsonMappingException e) {
      assertEquals(e.getMessage(), "Cannot map non-array value to String[]");
    }
  }

  @Test
  public void testDeserialize_withEmptyStringToNull() throws IOException {

    // Arrange
    String[] expected = null;
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonParser jsonParser = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);

    // Act
    when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
    when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
        JsonToken.END_ARRAY);
    when(jsonParser.getText()).thenReturn("");

    when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
    when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);

    String[] actual = stringArrayDeserializer.deserialize(jsonParser, ctxt);

    // Assert
    assertNull(actual);
  }

  @Test
  public void testDeserializeWithType()
      throws IOException {
    // Arrange
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonParser jsonParser = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
    // Act
    Object actual = stringArrayDeserializer.deserializeWithType(jsonParser, ctxt, typeDeserializer);

    // Assert
    assertNull(actual);
  }

  @Test
  public void testCreateContextual() throws JsonMappingException {
    // Arrange
    StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
    JsonDeserializer<?> deser = mock(JsonDeserializer.class);
    // Act
    JsonDeserializer<?> actual = stringArrayDeserializer.createContextual(null, null, null, deser);

    // Assert
    assertNull(actual);
  }

}