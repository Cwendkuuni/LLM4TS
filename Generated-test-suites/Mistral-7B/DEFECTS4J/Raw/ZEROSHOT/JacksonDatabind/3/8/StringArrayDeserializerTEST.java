package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.StdDateAccessors;

/**
 * Test class for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

 private ObjectBuffer objectBuffer;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private StringArrayDeserializer stringArrayDeserializer;
 private JsonDeserializer<String> jsonDeserializer;

 @Before
 public void setUp() {
 objectBuffer = new ObjectBuffer();
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
 stringArrayDeserializer = new StringArrayDeserializer();
 jsonDeserializer = mock(JsonDeserializer.class);
 }

 @Test
 public void testDeserializeNullElementDeserializer() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertEquals(new String[0], result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 JsonToken jsonToken = JsonToken.START_ARRAY;
 when(jsonParser.nextToken()).thenReturn(jsonToken);
 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 verify(jsonParser).nextToken();
 assertEquals(new String[0], stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeSingleElement() throws IOException {
 String testString = "testString";
 JsonToken jsonToken = JsonToken.VALUE_STRING;
 when(jsonParser.nextToken()).thenReturn(jsonToken);
 when(jsonParser.getText()).thenReturn(testString);
String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertEquals(new String[] { testString }, result);
 }

 @Test
 public void testDeserializeMultipleElements() throws IOException {
 String testString1 = "testString1";
 String testString2 = "testString2";
 String testString3 = "testString3";
 Object[] chunk = new Object[3];
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn(testString1);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn(testString2);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn(testString3);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertEquals(new String[] { testString1, testString2, testString3 }, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws IOException {
 String testString = "testString";
 JsonDeserializer<String> jsonDeserializerSpy = mock(JsonDeserializer.class);
 when(jsonDeserializerSpy.deserialize(jsonParser, deserializationContext)).thenReturn(testString);
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext, jsonDeserializerSpy);
 assertEquals(new String[] { testString }, result);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new String[] { "testString" });
 String[] result = (String[]) stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertEquals(new String[] { "testString" }, result);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 StringArrayDeserializer contextualStringArrayDeserializer = stringArrayDeserializer.createContextual(deserializationContext, null);
 assertEquals(stringArrayDeserializer, contextualStringArrayDeserializer);
 }

 @Test
 public void testCreateContextualWithJsonDeserializer() throws JsonMappingException {
 StringArrayDeserializer contextualStringArrayDeserializer = stringArrayDeserializer.createContextual(deserializationContext, null);
 JsonDeserializer<String> jsonDeserializerSpy = mock(JsonDeserializer.class);
 contextualStringArrayDeserializer = new StringArrayDeserializer(jsonDeserializerSpy);
assertEquals(contextualStringArrayDeserializer, stringArrayDeserializer.createContextual(deserializationContext, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualWithNullJsonDeserializer() throws JsonMappingException {
 StringArrayDeserializer contextualStringArrayDeserializer = stringArrayDeserializer.createContextual(deserializationContext, null);
 contextualStringArrayDeserializer = new StringArrayDeserializer(null);
 stringArrayDeserializer.createContextual(deserializationContext, null);
 }
}