package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.Collections;

public class AsWrapperTypeDeserializerTest extends BaseTest {

 private static final String CLASS_NAME = "com.custom.test.Person";
 private static final String FIELD_NAME = "name";
 private static final String TYPE_ID = "typeId";
 private static final String TYPE_ID_VALUE = "personType";
 private static final String VALUE = "John Doe";

 private ObjectMapper objectMapper;
 private JsonFactory jsonFactory;
 private TypeIdResolver typeIdResolver;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;
 private TokenBuffer tokenBuffer;

 @Override
 public void setUp() throws Exception {
 super.setUp();
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 typeIdResolver = mock(TypeIdResolver.class);
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 ObjectMapperUtil.constructType(CLASS_NAME), typeIdResolver, "_typeProperty", true, Object.class);
 tokenBuffer = new TokenBuffer(null, false);
 }

 @org.junit.Test
 public void testDeserializeTypedFromObject() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser(new ObjectReader(objectMapper.readerForValueType(CLASS_NAME))
 .readValueAs(JsonNode.class));
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.setValueAsString(TYPE_ID);
 jsonParser.nextToken();
 jsonParser.setValueAsString(TYPE_ID_VALUE);
 jsonParser.nextToken();
 jsonParser.setValueAsString(VALUE);
 jsonParser.nextToken();
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(typeIdResolver.resolveVisibleType(eq(TYPE_ID), any(), any()))
 .thenReturn(ObjectMapperUtil.constructType(CLASS_NAME + "Inner"));
 when(deserializer.deserialize(jsonParser, any())).thenReturn(new Object());

 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 verify(deserializer).deserialize(jsonParser, eq(mock(DeserializationContext.class)));
 assertEquals(VALUE, getValueFromObject(deserializer.deserialize(jsonParser, mock(DeserializationContext.class))));
 }

 @org.junit.Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser(new ObjectReader(objectMapper.readerForValueType(Object[].class))
 .readValueAs(JsonNode.class));
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.setValueAsString(TYPE_ID);
 jsonParser.nextToken();
 jsonParser.setValueAsString(TYPE_ID_VALUE);
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.setValueAsString(VALUE);
 jsonParser.nextToken();
 jsonParser.nextToken();

 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(typeIdResolver.resolveVisibleType(eq(TYPE_ID), any(), any()))
 .thenReturn(ObjectMapperUtil.constructType(CLASS_NAME + "InnerArray"));
 when(deserializer.deserialize(jsonParser, any())).thenReturn(new Object[0]);

 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 verify(deserializer).deserialize(jsonParser, eq(mock(DeserializationContext.class)));
 assertArrayEquals(new Object[0], deserializer.deserialize(jsonParser, mock(DeserializationContext.class)));
 }

 @org.junit.Test
 public void testDeserializeTypedFromScalar() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"" + TYPE_ID_VALUE + "\",\"value\":\"" + VALUE + "\"}");
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(typeIdResolver.resolveVisibleType(eq(TYPE_ID), any(), any()))
 .thenReturn(ObjectMapperUtil.constructType(CLASS_NAME + "Scalar"));
 when(deserializer.deserialize(jsonParser, any())).thenReturn(new Object());

 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 verify(deserializer).deserialize(jsonParser, eq(mock(DeserializationContext.class)));
 assertEquals(VALUE, getValueFromObject(deserializer.deserialize(jsonParser, mock(DeserializationContext.class))));
 }

 @org.junit.Test
 public void testDeserializeTypedFromAny() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"notExistingTypeId\",\"value\":\"" + VALUE + "\"}");
 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 assertNull(getValueFromObject(asWrapperTypeDeserializer.deserializationContext().getCurrentValue()));
 }

 @org.junit.Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"@type\":\"" + CLASS_NAME + "\",\"name\":\"" + VALUE + "\"}");
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(deserializer.deserialize(jsonParser, any())).thenReturn(new Object());

 when(typeIdResolver.resolveDeserializer(eq(Object.class), any()))
 .thenReturn(deserializer);

 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 verify(deserializer).deserialize(jsonParser, eq(mock(DeserializationContext.class)));
 assertEquals(VALUE, getValueFromObject(deserializer.deserialize(jsonParser, mock(DeserializationContext.class))));
 }

 @org.junit.Test
 public void testMergeTypeIdInObject() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"typeIdValue\",\"_typeProperty\":{\"typeId\":\"personType\",\"name\":\"" + VALUE + "\"}}");
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(deserializer.deserialize(jsonParser, any()))
 .thenReturn(new Object() {
 @Override
 public String toString() {
 return VALUE;
 }
 });

 when(typeIdResolver.resolveDeserializer(eq(Object.class), any()))
 .thenReturn(deserializer);

 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 assertEquals(VALUE, getValueFromObject(asWrapperTypeDeserializer.deserializationContext().getCurrentValue()));
 }

 @org.junit.Test(expected = IOException.class)
 public void testInvalidJsonStructure() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"personType\",\"name\":\"" + VALUE + "\"}");
 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 }

 @org.junit.Test(expected = IOException.class)
 public void testMissingTypeId() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"" + VALUE + "\"}");
 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 }

 @org.junit.Test(expected = IOException.class)
 public void testMissingValue() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"personType\"}");
 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 }

 @org.junit.Test(expected = IOException.class)
 public void testEmptyObject() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{}");
 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 }

 @org.junit.Test
 public void testDeserializeWithDefaultImpl() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"DefaultType\",\"value\":\"" + VALUE + "\"}");
 when(typeIdResolver.resolveVisibleType(eq(TYPE_ID), any(), any()))
 .thenThrow(new IOException("Type resolution failed"));

 when(typeIdResolver.resolveDeserializer(eq(Object.class), any()))
 .thenReturn(new StdDeserializer.StdValueDeserializer(ObjectMapperUtil.constructType(String.class), false, null, null, Collections.emptyList()));

 asWrapperTypeDeserializer._deserialize(jsonParser, mock(DeserializationContext.class));
 assertEquals(VALUE, asWrapperTypeDeserializer.deserializationContext().getCurrentValue());
 }

 private Object getValueFromObject(Object object) {
 if (object instanceof ObjectNode) {
 return ((ObjectNode) object).path(FIELD_NAME).asText();
 } else if (object instanceof ArrayNode) {
 ObjectNode firstNode = ((ArrayNode) object).get(0);
 return getValueFromObject(firstNode.elements());
 } else {
 return object.toString();
 }
 }
}