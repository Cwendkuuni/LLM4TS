package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AsWrapperTypeDeserializerTest extends TestBase {

 private AsWrapperTypeDeserializer deserializer;

 @BeforeMethod
 public void setup() {
 TypeIdResolver idResolver = new StandardTypeIdResolver();
 JavaType javaType = mapper().getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
 deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, Object.class);
 }

 @Test
 public void testDeserializeObject() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "com.example.MyClass");
 input.put("value", mapper().createObjectNode().putPOJOProperty("name", "John Doe"));

 ObjectNode result = mapper().readValue(input.toString(), Object.class, deserializer);
 Assert.assertEquals(result.getClass(), MyClass.class);
 Assert.assertEquals(result.get("name"), "John Doe");
 }

 @Test
 public void testDeserializeNullTypeId() throws Exception {
 JsonParser jp = mapper().getFactory().createParser("{}");
 Object result = deserializer.deserializeTypedFromObject(jp, mapper().getDeserializationContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeInvalidTypeId() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "com.invalid.class");
 input.put("value", mapper().createObjectNode().putPOJOProperty("name", "John Doe"));
 ObjectNode result = mapper().readValue(input.toString(), Object.class, deserializer);
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeMissingTypeId() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("value", mapper().createObjectNode().putPOJOProperty("name", "John Doe"));
 Object result = deserializer.deserializeTypedFromObject(mapper().createParser(input.toString()), mapper().getDeserializationContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeMissingValue() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "com.example.MyClass");
 Object result = deserializer.deserializeTypedFromObject(mapper().createParser(input.toString()), mapper().getDeserializationContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeEmptyTypeId() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "");
 input.put("value", mapper().createObjectNode().putPOJOProperty("name", "John Doe"));
 Object result = deserializer.deserializeTypedFromObject(mapper().createParser(input.toString()), mapper().getDeserializationContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeEmptyValue() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "com.example.MyClass");
 Object result = deserializer.deserializeTypedFromObject(mapper().createParser(input.toString()), mapper().getDeserializationContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeInvalidJson() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "com.example.MyClass");
 input.put("value", mapper().createObjectNode().putPOJOProperty("name", "John Doe"));
 input.put("invalid", "data");
 Object result = deserializer.deserializeTypedFromObject(mapper().createParser(input.toString()), mapper().getDeserializationContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeComplexJson() throws Exception {
 ObjectNode input = mapper().createObjectNode();
 input.put("type", "com.example.MyClassWithNestedObjects");
 input.put("value", mapper().createObjectNode()
 .put("name", "John Doe")
 .set("address", mapper().createObjectNode()
 .putPOJOProperty("street", new SimpleValue("Main St"))
 .putPOJOProperty("city", new SimpleValue("Anytown"))
 .putPOJOProperty("state", new SimpleValue("CA"))
 .putPOJOProperty("zip", new SimpleValue("12345"))));
 Object result = deserializer.deserializeTypedFromObject(mapper().createParser(input.toString()), mapper().getDeserializationContext());
 MyClassWithNestedObjects obj = (MyClassWithNestedObjects) result;
 Assert.assertEquals(obj.getName(), "John Doe");
 Assert.assertEquals(obj.getAddress().getStreet(), "Main St");
 Assert.assertEquals(obj.getAddress().getCity(), "Anytown");
 Assert.assertEquals(obj.getAddress().getState(), "CA");
 Assert.assertEquals(obj.getAddress().getZip(), "12345");
 }
}