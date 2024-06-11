package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 typeDeserializerBase = new TypeDeserializerBaseBase(
 mapper.getTypeFactory().constructType(TypeDeserializerBase.class),
 mapper.getTypeFactory().constructType(TypeIdResolver.class),
 "typePropertyName", true, mapper.getTypeFactory().constructType(String.class));
 }

 /**
 * Test for the forProperty method.
 * @throws IOException
 */
 @Test
 public void testForProperty() throws IOException {
 BeanProperty property = mapper.findProperty("testProperty", String.class, Object.class);
 TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(property);
 assertEquals(String.class, newTypeDeserializerBase.baseType().getRawClass());
 }

 /**
 * Test for getTypeInclusion method.
 * @throws IOException
 */
 @Test
 public void testgetTypeInclusion() throws IOException {
 JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
 assertTrue(typeInclusion == JsonTypeInfo.As.EXISTING_PROPERTY);
 }

 /**
 * Test for baseTypeName method.
 * @throws IOException
 */
 @Test
 public void testBaseTypeName() throws IOException {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("TypeDeserializerBase", baseTypeName);
 }

 /**
 * Test for getPropertyName method.
 * @throws IOException
 */
 @Test
 public void testgetPropertyName() throws IOException {
 String propertyName = typeDeserializerBase.getPropertyName();
 assertEquals("typePropertyName", propertyName);
 }

 /**
 * Test for getTypeIdResolver method.
 * @throws IOException
 */
 @Test
 public void testGetTypeIdResolver() throws IOException {
 TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
 assertNotNull(typeIdResolver);
 }

 /**
 * Test for getDefaultImpl method.
 * @throws IOException
 */
 @Test
 public void testgetDefaultImpl() throws IOException {
 JavaType defaultImpl = typeDeserializerBase.getDefaultImpl();
 assertEquals(String.class, defaultImpl.getRawClass());
 }

 /**
 * Test for findDeserializer method with known type id.
 * @throws IOException
 */
 @Test
 public void testFindDeserializerWithKnownTypeId() throws IOException {
 String jsonString = "{\"typeId\": \"StringDeserializer\"}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonDeserializer<Object> deserializer = typeDeserializerBase.findDeserializer(mapper.getDeserializationContext(), "StringDeserializer");
 assertEquals(StringDeserializer.class, deserializer.getClass());
 }

 /**
 * Test for findDeserializer method with unknown type id.
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerWithUnknownTypeId() throws IOException {
 String jsonString = "{\"typeId\": \"UnknownDeserializer\"}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 typeDeserializerBase.findDeserializer(mapper.getDeserializationContext(), "UnknownDeserializer");
 }

 /**
 * Test for findDefaultImplDeserializer method.
 * @throws IOException
 */
 @Test
 public void testFindDefaultImplDeserializer() throws IOException {
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase.findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNotNull(defaultImplDeserializer);
 }

 /**
 * Test for deserializeWithNativeTypeId method with known type id.
 * @throws IOException
 */
 @Test
 public void testDeserializeWithNativeTypeIdWithKnownTypeId() throws IOException {
 String jsonString = "{\"typeId\": \"StringDeserializer\"}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 Object deserializedObject = typeDeserializerBase.deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
 assertEquals(String.class, deserializedObject.getClass());
 }

 /**
 * Test for deserializeWithNativeTypeId method with unknown type id.
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdWithUnknownTypeId() throws IOException {
 String jsonString = "{\"typeId\": \"UnknownDeserializer\"}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 typeDeserializerBase.deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
 }

 /**
 * Test for handleUnknownTypeId method.
 * @throws IOException
 */
 @Test
 public void testHandleUnknownTypeId() throws IOException {
 String jsonString = "{\"typeId\": \"UnknownDeserializer\"}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 try {
 typeDeserializerBase.handleUnknownTypeId(mapper.getDeserializationContext(), "UnknownDeserializer");
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}