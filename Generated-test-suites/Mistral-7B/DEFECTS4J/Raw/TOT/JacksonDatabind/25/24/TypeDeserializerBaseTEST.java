package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase<String> typeDeserializer;
 private ObjectNode testObjectNode;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
 mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
 mapper.configure(DeserializationFeature.FAIL_ON_UNMATCHED_TYPE, false);
 mapper.configure(DeserializationFeature.USE_LOCAL_DATE_FORMATS, false);
 mapper.configure(DeserializationFeature.USE_LOCAL_DATETIME_FORMATS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_OR_NULL_STRING_VALUE, false);
 mapper.configure(SerializationFeature.FAIL_ON_WRITE_DUP_KEYS, false);
 mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
 mapper.setSerializationInclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
 mapper.setSerializationInclusion(JsonTypeInfo.As.WRAPPER_ARRAY_OF_NULLS);
 mapper.setSerializationInclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
 mapper.setSerializationInclusion(JsonTypeInfo.As.PROPERTY);
 mapper.setSerializationInclusion(JsonTypeInfo.As.PROPERTY_ARRAY);
 mapper.setSerializationInclusion(JsonTypeInfo.As.WRAPPER_OBJECT_ARRAY);
 TypeDeserializerBase.TypeIdResolver idResolver = new TypeIdResolverBase();
 Map<String, JsonDeserializer<?>> deserializers = new ConcurrentHashMap<>();
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 String typePropertyName = "type";
 boolean typeIdVisible = true;
 TypeDeserializerBase<String> typeDeserializer = new TypeDeserializerBase<String>(baseType, idResolver, typePropertyName, typeIdVisible, null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.WRAPPER_OBJECT;
 }
 @Override
 protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
 return ctxt.findRootValueDeserializer(String.class);
 }
 @Override
 protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
 TypeIdResolver idResolver, JavaType baseType) throws IOException {
 throw new RuntimeException("Unknown type id: " + typeId);
 }
 };
 testObjectNode = mapper.createObjectNode();
 }

 @Test
 public void testDeserializeWithValidTypeId() throws Exception {
 testObjectNode.put("type", "StringType");
 JsonTypeInfo<String> jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(TypeDeserializerBase.class)
 .property("type")
 .build();
 StringType stringType = mapper.readValue(testObjectNode, jsonTypeInfo.withThrowable(RuntimeException.class).withoutTypeIdVersion(Version.VERSION_2_11));
 assertEquals("StringType", stringType.getTypeId());
 assertEquals("String", stringType.getDefaultImpl());
 }

 @Test
 public void testDeserializeWithNullTypeId() throws Exception {
 testObjectNode.remove("type");
 JsonTypeInfo<String> jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(TypeDeserializerBase.class)
 .property("type")
 .build();
 try {
 mapper.readValue(testObjectNode, jsonTypeInfo.withoutTypeIdVersion(Version.VERSION_2_11));
 fail("Should have thrown an exception");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().contains("No (native) type id found when one was expected for polymorphic type handling"));
 }
 }

 @Test
 public void testDeserializeWithUnknownTypeId() throws Exception {
 testObjectNode.put("type", "UnknownType");
 JsonTypeInfo<String> jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(TypeDeserializerBase.class)
 .property("type")
 .build();
 try {
 mapper.readValue(testObjectNode, jsonTypeInfo.withoutTypeIdVersion(Version.VERSION_2_11));
 fail("Should have thrown an exception");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().contains("Unknown type id: UnknownType"));
 }
 }

 @Test
 public void testDeserializeWithMissingTypeIdProperty() throws Exception {
 testObjectNode.remove("type");
 JsonTypeInfo<String> jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(TypeDeserializerBase.class)
 .property("type")
 .build();
 try {
 mapper.readValue(testObjectNode, jsonTypeInfo.withoutTypeIdVersion(Version.VERSION_2_11));
 fail("Should have thrown an exception");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().contains("No type id property 'type' found"));
 }
 }
}