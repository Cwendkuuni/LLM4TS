package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsWrapperTypeDeserializerTest extends BaseTest {

 private ObjectMapper objectMapper;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private TypeDeserializer typeDeserializer;
 private BaseJsonParser baseJsonParser;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(null, null, null, false, Foo.class);
 jsonParser = objectMapper.getFactory().createParser("{\"type\":\"Foo\",\"value\":{\"name\":\"test\"}}");
 deserializationContext = mock(DeserializationContext.class);
 typeDeserializer = mock(TypeDeserializer.class);
 baseJsonParser = mock(BaseJsonParser.class);
 when(baseJsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(baseJsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(baseJsonParser.getText()).thenReturn("type");
 when(deserializationContext.findDeserializerByTypeId(asWrapperTypeDeserializer.getTypeId())).thenReturn(typeDeserializer);
 }

 @Test
 public void testDeserializeTypedFromObject() throws JsonMappingException, IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 assertEquals(Foo.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws JsonMappingException, IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
 assertEquals(Foo.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws JsonMappingException, IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
 assertEquals(Foo.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromAny() throws JsonMappingException, IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
 assertEquals(Foo.class, result.getClass());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"@type\":\"Foo\",\"value\":{\"name\":\"test\"}}");
 when(baseJsonParser.canReadTypeId()).thenReturn(true);
 when(baseJsonParser.getTypeId()).thenReturn("Foo");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 assertEquals(Foo.class, result.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testWrongTokenExceptionWithStartObject() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"Foo\",\"value\":{\"name\":\"test\"}}");
 jsonParser.nextToken();
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 }

 @Test(expected = JsonMappingException.class)
 public void testWrongTokenExceptionWithFieldName() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"Foo\",\"value\":{\"name\":\"test\"}}");
 jsonParser.nextToken();
 jsonParser.nextToken();
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 }

 @Test(expected = JsonMappingException.class)
 public void testWrongTokenExceptionWithUnexpectedValue() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"Foo\",\"value\":42}");
 jsonParser.nextToken();
 jsonParser.nextToken();
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 }

 @Test(expected = JsonMappingException.class)
 public void testWrongTokenExceptionWithUnexpectedObject() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"Foo\",\"value\":{\"name\":\"test\"},\"id\":\"bar\"}");
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 }

 @Test(expected = JsonMappingException.class)
 public void testWrongTokenExceptionWithUnexpectedEndObject() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"Foo\",\"value\":{\"name\":\"test\"}}");
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
 }
}

 class Foo {
 private String name;

 public String getName() {
 return name;
 }
}