package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 /**
 * Test for basic functionality.
 */
 @Test
 public void testDeserializeWithKnownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, "", false, null);
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("typeId", "MyCustomType");
 node.put("value", 123);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 MyCustomType result = typeDeserializer.deserialize(objectMapper.readTree(node), null, null);
 assertEquals("Expected MyCustomType", result.getClass(), MyCustomType.class);
 assertEquals("Expected value 123", result.getValue(), 123);
 }

 /**
 * Test for deserialization with missing type id.
 */
 @Test(expected = IOException.class)
 public void testDeserializeWithMissingTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, "", false, null);
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("value", 123);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 typeDeserializer.deserialize(objectMapper.readTree(node), null, null);
 fail("Expected IOException but got null");
 }

 /**
 * Test for deserialization with unknown type id.
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, "", false, null);
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("typeId", "UnknownTypeId");
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 typeDeserializer.deserialize(objectMapper.readTree(node), null, null);
 fail("Expected JsonMappingException but got null");
 }

 /**
 * Test for deserialization with default type.
 */
 @Test
 public void testDeserializeWithDefaultType() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, "", false, null);
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("value", 123);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 Object result = typeDeserializer.deserialize(objectMapper.readTree(node), null, null);
 assertEquals("Expected Object", result.getClass(), Object.class);
 }

 /**
 * Test for deserialization with null.
 */
 @Test
 public void testDeserializeWithNull() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, "", false, null);
 JsonNode node = JsonNodeFactory.instance.nullNode();
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 Object result = typeDeserializer.deserialize(objectMapper.readTree(node), null, null);
 assertNull("Expected null", result);
 }

 /**
 * Test for deserialization with unsupported type.
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnsupportedType() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, "", false, null);
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("value", new Object());
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
 typeDeserializer.deserialize(objectMapper.readTree(node), null, null);
 fail("Expected JsonMappingException but got null");
 }
}

class BaseTypeDeserializer extends TypeDeserializerBase {
 private static final long serialVersionUID = 1;

 BaseTypeDeserializer(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
 }

 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }

 /**
 * Stub implementation for testing.
 */
 public MyCustomType createMyCustomType(int value) {
 return new MyCustomType(value);
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 Object value = p.getValueAsToken();
 if (value.isNumber()) {
 JsonNode node = p.getCodec().readTree(p);
 return createMyCustomType(node.getIntValue());
 }
 return null;
 }
}

class MyCustomType {
 private int value;

 MyCustomType(int value) {
 this.value = value;
 }

 int getValue() {
 return value;
 }
}