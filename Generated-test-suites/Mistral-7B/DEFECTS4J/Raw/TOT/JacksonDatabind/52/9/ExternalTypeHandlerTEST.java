package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private static class TestPerson {
 private String name;
 private String typeId;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public String getTypeId() {
 return typeId;
 }

 public void setTypeId(String typeId) {
 this.typeId = typeId;
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testHandleTypePropertyValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"name\":\"John\",\"typeId\":\"PersonType\"}";
 JsonParser p = mapper.getFactory().createParser(json);

 TestPerson bean = new TestPerson();
 handler.handleTypePropertyValue(p, null, "name", bean);
 assertEquals("PersonType", bean.getTypeId());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testHandleTypePropertyValue_MissingTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"name\":\"John\"}";
 JsonParser p = mapper.getFactory().createParser(json);

 TestPerson bean = new TestPerson();
 handler.handleTypePropertyValue(p, null, "name", bean);
 assertNull(bean.getTypeId());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testHandleTypePropertyValue_MissingProperty() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"nonExistentProperty\":\"PersonType\"}";
 JsonParser p = mapper.getFactory().createParser(json);

 TestPerson bean = new TestPerson();
 handler.handleTypePropertyValue(p, null, "nonExistentProperty", bean);
 assertNull(bean.getTypeId());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testHandlePropertyValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"name\":\"John\"}";
 JsonParser p = mapper.getFactory().createParser(json);

 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 assertNull(bean.getName());
 assertNull(bean.getTypeId());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testHandlePropertyValue_TypeIdPresent() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class)))
 .thenReturn(new TestType());
 handler.addExternal(new SettableBeanProperty("typeId", typeDeserializer), typeDeserializer);

 String json = "{\"name\":\"John\",\"typeId\":\"TypeA\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 assertEquals("TypeA", bean.getTypeId());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testComplete() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"name\":\"John\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 assertEquals(0, handler.getSize());

 handler.complete(p, null, bean);
 assertEquals(1, handler.getSize());
 assertEquals("null", handler.getToken(0).getText());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testComplete_MissingTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"name\":\"John\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 handler.complete(p, null, bean);
 assertEquals(1, handler.getSize());
 assertNull(handler.getToken(0).getText());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testComplete_MissingProperty() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);

 String json = "{\"typeId\":\"TypeA\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handleTypePropertyValue(p, null, "typeId", bean);
 handler.complete(p, null, bean);
 assertEquals(1, handler.getSize());
 assertNull(bean.getName());
 assertNotNull(bean.getTypeId());
 assertEquals("TypeA", bean.getTypeId());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testComplete_TypeIdPresent() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class)))
 .thenReturn(new TestType());
 handler.addExternal(new SettableBeanProperty("typeId", typeDeserializer), typeDeserializer);

 String json = "{\"name\":\"John\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 handler.complete(p, null, bean);
 assertEquals(1, handler.getSize());
 assertNull(handler.getToken(0).getText());

 p.nextToken(); // consume '}'
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testComplete_TypeIdAndPropertyPresent() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class)))
 .thenReturn(new TestType());
 handler.addExternal(new SettableBeanProperty("typeId", typeDeserializer), typeDeserializer);
 handler.addExternal(new SettableBeanProperty("name", null), null);

 String json = "{\"name\":\"John\",\"typeId\":\"TypeA\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 handler.handlePropertyValue(p, null, "typeId", bean);
 handler.complete(p, null, bean);
 assertEquals(2, handler.getSize());
 assertEquals("null", handler.getToken(0).getText());
 assertEquals("TypeA", handler.getToken(1).getText());

 p.nextToken(); // consume '}'
 }

 private static class TestType {
 // empty class used only for testing
 }

 private static class TestBuilder extends ExternalTypeHandler.Builder {
 @Override
 public ExternalTypeHandler build() {
 return new ExternalTypeHandler(this.toArray(new ExtTypedProperty[0]),
 this.getNameToPropertyIndex(), null, null);
 }
 }

 @org.junit.Test
 public void testBuilder() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBuilder builder = new TestBuilder();
 builder.addExternal(new SettableBeanProperty("typeId", null), null);
 builder.addExternal(new SettableBeanProperty("name", null), null);
 ExternalTypeHandler handler = builder.build();

 String json = "{\"name\":\"John\",\"typeId\":\"TypeA\"}";
 JsonParser p = mapper.getFactory().createParser(json);
 TestPerson bean = new TestPerson();
 handler.handlePropertyValue(p, null, "name", bean);
 handler.handlePropertyValue(p, null, "typeId", bean);
 handler.complete(p, null, bean);
 assertEquals(2, handler.getSize());
 assertEquals("null", handler.getToken(0).getText());
 assertEquals("TypeA", handler.getToken(1).getText());

 p.nextToken(); // consume '}'
 }
}