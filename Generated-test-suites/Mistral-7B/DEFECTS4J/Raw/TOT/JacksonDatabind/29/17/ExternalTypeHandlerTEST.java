package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private static class TestBean {
 private String typeId1;
 private String typeId2;
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{\"typeId1\":\"StringType\"}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 assertTrue(handler.handleTypePropertyValue(jp, ctxt, "typeId1", bean));
 assertEquals("StringType", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_MissingTypeId() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{\"typeId1\":null}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 assertFalse(handler.handleTypePropertyValue(jp, ctxt, "typeId1", bean));
 }

 @Test
 public void testHandleTypePropertyValue_InvalidTypeId() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{\"typeId1\":\"IntType\"}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 assertFalse(handler.handleTypePropertyValue(jp, ctxt, "typeId1", bean));
 }

 @Test
 public void testHandlePropertyValue() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{\"typeId1\":\"StringType\", \"value\":\"Test\"}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new TestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 assertTrue(handler.handlePropertyValue(jp, ctxt, "typeId1", bean));
 assertEquals("Test", bean.getTypeId1());
 }

 @Test
 public void testHandlePropertyValue_MissingProperty() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{\"value\":\"Test\"}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new TestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 assertFalse(handler.handlePropertyValue(jp, ctxt, "nonexistent", bean));
 }

 @Test
 public void testHandlePropertyValue_MissingTypeId() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{\"typeId2\":\"StringType\", \"value\":\"Test\"}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new TestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 assertFalse(handler.handlePropertyValue(jp, ctxt, "typeId2", bean));
 }

 @Test
 public void testComplete() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{" +
 "\"typeId1\":\"StringType\"," +
 "\"typeId2\":\"IntegerType\"," +
 "\"value1\":\"Test1\"," +
 "\"value2\":42" +
 "}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new TestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer()),
 new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId2"), new IntegerTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 handler.complete(jp, ctxt, bean);
 assertEquals("Test1", bean.getTypeId1());
 assertEquals(42, bean.getTypeId2());
 }

 @Test
 public void testComplete_MissingTypeId() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{" +
 "\"value1\":\"Test1\"," +
 "\"value2\":42" +
 "}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new TestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer()),
 new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId2"), new IntegerTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 try {
 handler.complete(jp, ctxt, bean);
 fail();
 } catch (MappingException e) {
 assertEquals("Missing property 'typeId1' for external type id 'typeId1'", e.getMessage());
 }
 }

 @Test
 public void testComplete_MissingTypeId_DefaultType() throws IOException, JsonProcessingException {
 JsonParser jp = new JsonFactory().createParser("{" +
 "\"value1\":\"Test1\"," +
 "\"value2\":null" +
 "}");
 DeserializationContext ctxt = new StandardDeserializationContext();
 Object bean = new TestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(
 new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId1"), new SimpleTypeDeserializer()),
 new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "typeId2"), new SimpleTypeDeserializer())},
 new HashMap<>(), new String[]{}, new TokenBuffer[0]);
 handler.start();
 handler.complete(jp, ctxt, bean);
 assertNull(bean.getTypeId2());
 }
}