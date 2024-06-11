package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializationWithConverter() throws JsonProcessingException {
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return objectMapper.findValueSerializer(String.class);
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 String expectedResult = "expected result";
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializationWithClassBasedConstructor() throws JsonProcessingException {
 Class<String> stringClass = String.class;
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(stringClass,
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return objectMapper.findValueSerializer(String.class);
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 String expectedResult = "expected result";
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializationWithAllConstructor() throws JsonProcessingException {
 Converter<Object, String> converter = new CustomConverter<>();
 JavaType delegateType = TypeFactory.defaultInstance().constructType(String.class);
 JsonSerializer<String> delegateSerializer = objectMapper.findValueSerializer(String.class);
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return delegateSerializer;
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 String expectedResult = "expected result";
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException {
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return objectMapper.findValueSerializer(String.class);
 }
 };
 Object valueToSerialize = null;
 String expectedResult = null;
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializationWithImproperType() throws JsonProcessingException {
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return objectMapper.findValueSerializer(Integer.class);
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 }

 @Test
 public void testSerializationWithDelegateSerializerThatThrowsException() throws JsonProcessingException {
 JsonSerializer<Object> delegateSerializer = new CustomJsonSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 throw new IOException("Expected exception");
 }
 };
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return delegateSerializer;
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 try {
 serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testSerializationWithDelegateSerializerThatReturnsEmptyString() throws JsonProcessingException {
 JsonSerializer<Object> delegateSerializer = new CustomJsonSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("");
 }
 };
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return delegateSerializer;
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertEquals("", actualResult);
 }

 @Test
 public void testSerializationWithDelegateSerializerThatReturnsNull() throws JsonProcessingException {
 JsonSerializer<Object> delegateSerializer = new CustomJsonSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 // do nothing
 }
 };
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return delegateSerializer;
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertNull(actualResult);
 }

 @Test
 public void testSerializationWithDelegateSerializerThatReturnsCustomObject() throws JsonProcessingException {
 JsonSerializer<Object> delegateSerializer = new CustomJsonSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString("customObject");
 gen.writeEndObject();
 }
 };
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(
 new CustomConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return delegateSerializer;
 }
 };
 Object valueToSerialize = new Object() {
 // some fields here
 };
 String actualResult = serializer.serializeValueAsString(valueToSerialize, objectMapper.getSerializerProvider());
 assertEquals("{\"name\":\"customObject\"}", actualResult);
 }
}