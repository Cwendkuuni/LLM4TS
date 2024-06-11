package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 testee = new BaseTypeDeserializer(mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(Integer.class));
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = mapper.findBeanProperty("testPropertyName", String.class);
 TypeDeserializer deserializer = testee.forProperty(property);
 assertEquals(String.class, deserializer.getType());
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, testee.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 assertEquals("java.lang.String", testee.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws Exception {
 assertEquals("typePropertyName", testee.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() throws Exception {
 assertEquals(mapper.getTypeIdResolver(), testee.getTypeIdResolver());
 }

 @Test
 public void testDefaultImpl() throws Exception {
 assertEquals(Integer.class, testee.getDefaultImpl().getRawClass());
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver:com.fasterxml.jackson.core.type.Types$StandardTypeResolver@1646362433]";
 assertEquals(expected, testee.toString());
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithMissingTypeId() throws Exception {
 testee.findDeserializer(mapper.getDeserializationContext(), "missingTypeId");
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithNullTypeId() throws Exception {
 testee.findDeserializer(mapper.getDeserializationContext(), null);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithInvalidTypeId() throws Exception {
 testee.findDeserializer(mapper.getDeserializationContext(), "invalidTypeId");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> deserializer = testee.findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNotNull(deserializer);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"java.lang.Integer\", \"value\":42}");
 Object result = testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 assertEquals(Integer.TYPE, result.getClass());
 assertEquals(42, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithUnknownTypeId() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"unknownTypeId\", \"value\":42}");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithMissingTypeId() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"value\":42}");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNullValue() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("null");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithEmptyString() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("\"\"");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithEmptyJsonObject() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{}");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithEmptyJsonArray() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[ ]");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidJson() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"invalidJson\":42}");
 testee.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 }
}