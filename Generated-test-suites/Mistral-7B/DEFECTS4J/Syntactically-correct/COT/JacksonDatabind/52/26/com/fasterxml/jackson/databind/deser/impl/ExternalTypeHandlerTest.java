package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private static class TestBean {
 String prop1;
 String prop2;
 }

 @SuppressWarnings("unchecked")
 @org.junit.BeforeClass
 public static void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 // Create a builder and add some external properties
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 SettableBeanProperty prop1 = new SettableBeanProperty("prop1", String.class);
 SettableBeanProperty prop2 = new SettableBeanProperty("prop2", Integer.class);
 TypeDeserializer typeDeserializer1 = mapper.getTypeDeserializerInstance(String.class);
 TypeDeserializer typeDeserializer2 = mapper.getTypeDeserializerInstance(Integer.class);
 builder.addExternal(prop1, typeDeserializer1);
 builder.addExternal(prop2, typeDeserializer2);
 // Build ExternalTypeHandler
 ExternalTypeHandler handler = builder.build();
 // Set up test data
 TestBean bean = new TestBean();
 }

 @org.junit.Test
 public void testStart() throws Exception {
 ExternalTypeHandler handler = new ExternalTypeHandler(handler._properties, handler._nameToPropertyIndex, null, null).start();
 assertTrue(handler instanceof ExternalTypeHandler);
 }

 @org.junit.Test
 public void testHandleTypePropertyValue() throws Exception {
 String typeId = "StringType";
 JsonParser parser = createTestParser();
 DeserializationContext context = createTestContext();
 handler.handleTypePropertyValue(parser, context, "prop1", bean);
 assertEquals("StringType", handler._typeIds[0]);
 }

 @org.junit.Test
 public void testHandleTypePropertyValue_NullBean() throws Exception {
 JsonParser parser = createTestParser();
 DeserializationContext context = createTestContext();
 handler.handleTypePropertyValue(parser, context, "prop1", null);
 assertNull(handler._typeIds);
 }

 @org.junit.Test
 public void testHandleTypePropertyValue_NoMatchingProperty() throws Exception {
 JsonParser parser = createTestParser();
 DeserializationContext context = createTestContext();
 handler.handleTypePropertyValue(parser, context, "nonExistentProperty", bean);
 assertFalse(handler.handleTypePropertyValue(parser, context, "prop1", bean));
 }

 @org.junit.Test
 public void testHandlePropertyValue() throws Exception {
 String typeId = "IntegerType";
 JsonParser parser = createTestParser();
 DeserializationContext context = createTestContext();
 handler.handlePropertyValue(parser, context, "prop2", bean);
 assertEquals("IntegerType", handler._typeIds[1]);
 }

 @org.junit.Test
 public void testHandlePropertyValue_NullBean() throws Exception {
 JsonParser parser = createTestParser();
 DeserializationContext context = createTestContext();
 handler.handlePropertyValue(parser, context, "prop2", null);
 assertNull(handler._typeIds);
 }

 @org.junit.Test
 public void testHandlePropertyValue_MissingTypeIdProperty() throws Exception {
 JsonParser parser = createTestParser();
 DeserializationContext context = createTestContext();
 parser.nextToken(); // skip START_OBJECT
 parser.nextToken(); // skip PROPERTY_NAME "prop2"
 parser.nextToken(); // skip VALUE_NULL
 parser.nextToken(); // skip END_OBJECT
 handler.handlePropertyValue(parser, context, "prop2", bean);
 assertFalse(handler.handleTypePropertyValue(parser, context, "prop2", bean));
 }

 @org.junit.Test
 public void testComplete() throws Exception {
 String jsonString = "{\"prop1\":\"StringType\", \"prop2\":null}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object result = handler.complete(parser, context, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertEquals("", bean.prop1);
 assertNull(bean.prop2);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testComplete_MissingTypeIdProperty() throws Exception {
 String jsonString = "{\"prop2\":null}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 handler.complete(parser, context, bean);
 fail("Expected JsonMappingException when completing JSON without type id property");
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testComplete_MissingProperty() throws Exception {
 String jsonString = "{\"StringType\":null}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 handler.complete(parser, context, bean);
 fail("Expected JsonMappingException when completing JSON without required property");
 }

 @org.junit.Test
 public void testComplete_NullBean() throws Exception {
 JsonParser parser = createTestParser();
 Object result = handler.complete(parser, context, null);
 assertNull(result);
 }

 @org.junit.Test
 public void testComplete_NullValue() throws Exception {
 String jsonString = "{\"prop1\":null,\"prop2\":null}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object result = handler.complete(parser, context, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertNull(bean.prop1);
 assertNull(bean.prop2);
 }

 @org.junit.Test
 public void testComplete_ValueWithTypeId() throws Exception {
 String jsonString = "{\"prop1\":\"StringType\",\"prop2\":{\"@type\":\"IntegerType\"}}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object result = handler.complete(parser, context, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertEquals("", bean.prop1);
 assertTrue(bean.prop2 instanceof Number);
 assertEquals(0, ((Number) bean.prop2).intValue());
 }

 @org.junit.Test
 public void testComplete_ValueWithoutTypeId() throws Exception {
 String jsonString = "{\"prop1\":\"StringType\",\"prop2\":42}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object result = handler.complete(parser, context, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertEquals("", bean.prop1);
 assertEquals(42, bean.prop2.hashCode());
 }

 @org.junit.Test
 public void testComplete_ValueWithDefaultType() throws Exception {
 String jsonString = "{\"prop1\":\"StringType\",\"prop2\":null}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object result = handler.complete(parser, context, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertEquals("", bean.prop1);
 assertNull(bean.prop2);
 }

 @org.junit.Test
 public void testComplete_ValueWithCreatorProperties() throws Exception {
 String jsonString = "{\"prop1\":\"StringType\",\"creatorProperty\":{\"@type\":\"CreatorType\"}}";
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer valueBuffer) throws IOException {
 return new CreatorClass();
 }
 };
 Object result = handler.complete(parser, context, creator, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertEquals("", bean.prop1);
 assertTrue(result instanceof CreatorClass);
 }

 @org.junit.Test
 public void testComplete_ValueWithTypeIdAndCreatorProperties() throws Exception {
 String jsonString = "{\"prop1\":\"StringType\",\"creatorProperty\":{\"@type\":\"CreatorType\", \"creatorPropertyTypeId\":\"CreatorTypeId\"}}";
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer valueBuffer) throws IOException {
 return new CreatorClass(valueBuffer.getValueAsString("creatorPropertyTypeId"));
 }
 };
 Object result = handler.complete(parser, context, creator, bean);
 assertEquals(bean.getClass(), result.getClass());
 assertEquals("", bean.prop1);
 assertTrue(result instanceof CreatorClass);
 }

 private JsonParser createTestParser() throws IOException {
 return mapper.getFactory().createParser("{}");
 }

 private DeserializationContext createTestContext() {
 return mapper.getDeserializationContext();
 }

}

class CreatorClass {
 String creatorPropertyTypeId;

 public CreatorClass(String creatorPropertyTypeId) {
 this.creatorPropertyTypeId = creatorPropertyTypeId;
 }

}

class CreatorClass implements PropertyBasedCreator {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer valueBuffer) throws IOException {
 return new CreatorClass();
 }
}