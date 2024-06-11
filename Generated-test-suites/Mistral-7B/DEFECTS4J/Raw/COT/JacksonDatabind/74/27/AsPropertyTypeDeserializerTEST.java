package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testAsPropertyTypeDeserializer_Constructor_ValidArgs() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertEquals("typePropertyName", deserializer.getTypePropertyName());
 assertEquals(String.class, deserializer.getBaseType());
 assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
 }

 @Test
 public void testForProperty_SameProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 BeanProperty property = new BeanProperty("testProperty", String.class);
 AsPropertyTypeDeserializer deserializerForProperty = deserializer.forProperty(property);

 assertEquals(deserializer, deserializerForProperty);
 }

 @Test
 public void testDeserializeTypedFromObject_ValidJsonObject() throws Exception {
 String json = "{\"typePropertyName\":\"String\",\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object readValue = MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 assertEquals("Hello, World!", readValue);
 }

 @Test
 public void testDeserializeTypedFromObject_MissingTypeProperty() throws Exception {
 String json = "{\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertNull(MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer));
 }

 @Test
 public void testDeserializeTypedFromObject_InvalidTypePropertyName() throws Exception {
 String json = "{\"invalidTypePropertyName\":\"String\",\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertNull(MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer));
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_MissingStartObject() throws Exception {
 String json = "{\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 }

 @Test
 public void testDeserializeTypedFromObject_MissingFieldName() throws Exception {
 String json = "{\"START_OBJECT\":{},\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertNull(MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer));
 }

 @Test
 public void testDeserializeTypedFromObject_MissingValue() throws Exception {
 String json = "{\"typePropertyName\":\"String\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertNull(MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer));
 }

 @Test
 public void testDeserializeTypedFromObject_InvalidJson() throws Exception {
 String json = "{\"typePropertyName\":\"String\"";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 try {
 MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeWithNativeTypeId_ValidJsonObject() throws Exception {
 String json = "{\"@type\":\"String\",\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object readValue = MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 assertEquals("Hello, World!", readValue);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_MissingStartObject() throws Exception {
 String json = "{\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_MissingTypeId() throws Exception {
 String json = "{\"@type\":\"\",\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_InvalidTypeId() throws Exception {
 String json = "{\"@type\":\"InvalidType\",\"value\":\"Hello, World!\"}";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 MAPPER.readValue(new JsonFactory(), JsonParserFactory.getParser(json), deserializer);
 }

 @Test
 public void testFindDeserializer_NullContext() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 null,
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertNull(deserializer.findDeserializer(null, "TypeName"));
 }

 @Test
 public void testFindDeserializer_NullTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 assertNull(deserializer.findDeserializer(MAPPER.getContext(), null));
 }

 @Test
 public void testFindDeserializer_ValidTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 JsonDeserializer<String> deserializerForTypeId = Mockito.mock(JsonDeserializer.class);
 when(MAPPER.getTypeIdResolver().resolveTypeId(eq(MAPPER.getContext()), anyString())).thenReturn("TypeName");
 when(MAPPER.getTypeIdResolver().deserializeTypedFromObject(eq(MAPPER.getContext()), any(JsonParser.class), eq(deserializer), any(TypeDeserializer.Context.class)))
     .thenReturn(deserializerForTypeId);

 assertEquals(deserializerForTypeId, deserializer.findDeserializer(MAPPER.getContext(), "TypeName"));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializer_UnknownTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MAPPER.getTypeFactory().constructType(String.class),
 MAPPER.getTypeIdResolver(),
 "typePropertyName",
 true,
 MAPPER.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 when(MAPPER.getTypeIdResolver().resolveTypeId(eq(MAPPER.getContext()), anyString())).thenReturn("UnknownTypeId");

 try {
 deserializer.findDeserializer(MAPPER.getContext(), "UnknownTypeId");
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}