package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {

 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private ObjectMapper objectMapper;
 private TypeDeserializer typeDeserializer;
 private StringArrayDeserializer stringArrayDeserializer;

 @Before
 public void setup() {
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 objectMapper = new ObjectMapper();
 typeDeserializer = mock(TypeDeserializer.class);
 stringArrayDeserializer = new StringArrayDeserializer();
 }

 @Test
 public void testDeserializeStandard() throws IOException {
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("test");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("test2");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 ObjectBuffer objectBuffer = objectMapper.getFactory().createObjectBuffer(null, 16, 16);
 Object[] chunk = objectBuffer.resetAndStart();

 when(mockContext.leaseObjectBuffer()).thenReturn(objectBuffer);

 String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockContext);

 assertArrayEquals(new String[]{"test", "test2"}, result);
 }

 @Test
 public void testDeserializeCustom() throws IOException {
 JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("test3");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("test4");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 when(mockContext.findContextualValueDeserializer(TypeReference.collectionValueType(String.class), null)).thenReturn(customDeserializer);

 String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockContext);

 assertArrayEquals(new String[]{"null", "test3", null, "test4"}, result);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException {
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 when(mockJsonParser.nextIntValue()).thenReturn(42);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("test");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 when(typeDeserializer.deserializeTypedFromArray(mockJsonParser, mockContext)).thenReturn(new Object[]{"42", "test", null});

 String[] result = stringArrayDeserializer.deserializeWithType(mockJsonParser, mockContext, typeDeserializer);

 assertArrayEquals(new String[]{"42", "test", null}, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleNonArray() throws IOException {
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("test");

 String[] result = stringArrayDeserializer.handleNonArray(mockJsonParser, mockContext);

 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test
 public void testCreateContextual() {
 // This test is not comprehensive as it would require
 // more complex setup for testing the contextualization
 // logic, which depends on the deserializer chain.
 // However, it can be used as a sanity check to verify
 // that the passed deserializer is correctly returned.
 JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);

 StringArrayDeserializer contextualStringArrayDeserializer = stringArrayDeserializer.createContextual(mockContext, null);

 when(mockContext.findContextualValueDeserializer(TypeReference.collectionValueType(String.class), null)).thenReturn(customDeserializer);

 assertSame(new StringArrayDeserializer(customDeserializer), contextualStringArrayDeserializer);
 }
}