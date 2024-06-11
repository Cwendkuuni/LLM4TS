package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

 private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

 @org.junit.BeforeClass
 public static void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(String[].class, new StringArrayDeserializer()));
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 OBJECT_MAPPER.setDefaultTypedValue("testValue", String.class);
 OBJECT_MAPPER.setDefaultDeserializer(String.class, mock(JsonDeserializer.class));
 OBJECT_MAPPER.setDefaultContextualValueDeserializer(String.class, mock(JsonDeserializer.class));
 OBJECT_MAPPER.setDefaultContextualDeserializer(String[].class, serializer);
 }

 @org.junit.Test
 public void testDeserializeNullValue() throws IOException, JsonProcessingException {
 String[] result = StringArrayDeserializer.instance.deserialize(mockJsonParser(), mockContext());
 assertNull(result);
 }

 @org.junit.Test
 public void testDeserializeEmptyArray() throws IOException, JsonProcessingException {
 String[] result = StringArrayDeserializer.instance.deserialize(mockJsonParser(), mockContext());
 assertArrayEquals(new String[0], result);
 }

 @org.junit.Test
 public void testDeserializeSingleValue() throws IOException, JsonProcessingException {
 String[] result = StringArrayDeserializer.instance.deserialize(mockJsonParserWithValueString("testValue"), mockContext());
 assertArrayEquals(new String[] {"testValue"}, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithNullValue() throws IOException, JsonProcessingException {
 String[] result = StringArrayDeserializer.instance.deserialize(mockJsonParserWithValueString("null,testValue"), mockContext());
 assertArrayEquals(new String[] {"null", "testValue"}, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithCustomValueDeserializer() throws IOException, JsonProcessingException {
 String[] expected = {"testValue1", "testValue2"};
 JsonDeserializer<String> mockCustomDeserializer = mock(JsonDeserializer.class);
 when(mockCustomDeserializer.deserialize(mock(JsonParser.class), mockContext()))
 .thenReturn("testValue1");
 when(mockCustomDeserializer.getNullValue()).thenReturn(null);
 StringArrayDeserializer serializerWithCustomDeserializer = new StringArrayDeserializer(mockCustomDeserializer);
 String[] result = serializerWithCustomDeserializer.deserialize(mockJsonParserWithValueString("null,testValue2"), mockContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithContentConverter() throws IOException, JsonProcessingException {
 String[] expected = {"testValue1Base64", "testValue2Base64"};
 JsonDeserializer<String> mockCustomDeserializer = mock(JsonDeserializer.class);
 when(mockCustomDeserializer.deserialize(mock(JsonParser.class), mockContext()))
 .thenReturn("testValue1");
 when(mockCustomDeserializer.getNullValue()).thenReturn(null);
 when(mockCustomDeserializer.getContentConverter())
 .thenReturn(mock(ContentConverter.class));
 StringArrayDeserializer serializerWithCustomDeserializer = new StringArrayDeserializer(mockCustomDeserializer);
 String[] result = serializerWithCustomDeserializer.deserialize(mockJsonParserWithValueString("null,testValue2Base64"), mockContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithContextualDeserializer() throws IOException, JsonProcessingException {
 String[] expected = {"testValue1", "testValue2"};
 JsonDeserializer<String> mockCustomDeserializer = mock(JsonDeserializer.class);
 when(mockCustomDeserializer.deserialize(mock(JsonParser.class), mockContext()))
 .thenReturn("testValue1");
 when(mockCustomDeserializer.getNullValue()).thenReturn(null);
 DeserializationContext context = mockContext();
 when(context.findContextualValueDeserializer(eq(String.class), any(BeanProperty.class)))
 .thenReturn(mockCustomDeserializer);
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 String[] result = serializer.createContextual(context, mockProperty())
 .deserialize(mockJsonParserWithValueString("testValue2"), context);
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithDefaultStringDeserializer() throws IOException, JsonProcessingException {
 String[] expected = {"testValue1", "testValue2"};
 StringArrayDeserializer serializerWithCustomDeserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));
 String[] result = serializerWithCustomDeserializer.deserialize(mockJsonParserWithValueStrings("testValue1,testValue2"), mockContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testDeserializeNonArray() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 serializer.handleNonArray(mockJsonParser(), mockContext());
 }

 @org.junit.Test
 public void testCreateContextualWithNullValueDeserializer() {
 StringArrayDeserializer serializer = new StringArrayDeserializer(null);
 assertNotNull(serializer.createContextual(mockContext(), mockProperty()));
 }

 private JsonParser mockJsonParser() {
 return mock(JsonParser.class);
 }

 private JsonParser mockJsonParserWithValueString(String value) {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser().getText()).thenReturn(value);
 return mockJsonParser();
 }

 private JsonParser mockJsonParserWithValueStrings(String... values) {
 JsonParser jsonParser = mockJsonParser();
 ObjectBuffer buffer = new ObjectBuffer(16);
 Object[] chunk = buffer.resetAndStart();
 for (String value : values) {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser().getText()).thenReturn(value);
 if (chunk.length <= buffer.position()) {
 chunk = buffer.appendCompletedChunk(chunk);
 }
 chunk[buffer.position()] = value;
 buffer.position++ ;
 }
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.END_ARRAY);
 return jsonParser;
 }

 private DeserializationContext mockContext() {
 return mock(DeserializationContext.class);
 }

 private BeanProperty mockProperty() {
 return mock(BeanProperty.class);
 }
}