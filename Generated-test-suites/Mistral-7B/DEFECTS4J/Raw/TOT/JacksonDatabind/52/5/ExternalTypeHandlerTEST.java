package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class ExternalTypeHandlerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testHandleTypePropertyValue() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"type\":\"String\", \"age\":42}");
 DeserializationContext ctxt = new TestDeserializationContext();

 assertTrue(handler.handleTypePropertyValue(p, ctxt, "name", null));
 assertEquals("John", handler.getProperty(handler, 0).get(null));
 assertTrue(handler.handleTypePropertyValue(p, ctxt, "age", new Object()));
 assertNull(handler.getProperty(handler, 1).get(new Object()));
 p.nextToken(); // consume root object end token
 handler.complete(p, ctxt, new Object());
 assertEquals("John", handler.getProperty(handler, 0).get(new Object()));
 assertNull(handler.getProperty(handler, 1).get(new Object()));
 }

 @Test
 public void testHandleTypePropertyValue_MissingTypeProperty() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"invalid\":\"String\", \"age\":42}");
 DeserializationContext ctxt = new TestDeserializationContext();

 assertFalse(handler.handleTypePropertyValue(p, ctxt, "name", null));
 assertFalse(handler.handleTypePropertyValue(p, ctxt, "age", null));
 }

 @Test
 public void testHandleTypePropertyValue_MissingProperty() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"invalid\":\"String\", \"age\":42}");
 DeserializationContext ctxt = new TestDeserializationContext();

 assertFalse(handler.handleTypePropertyValue(p, ctxt, "name", null));
 assertFalse(handler.handleTypePropertyValue(p, ctxt, "age", null));
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"age\":42}");
 DeserializationContext ctxt = new TestDeserializationContext();

 assertTrue(handler.handlePropertyValue(p, ctxt, "name", null));
 assertEquals("John", handler.getProperty(handler, 0).get(null));
 assertNull(handler.getProperty(handler, 1).get(null));
 p.nextToken(); // consume root object end token
 handler.complete(p, ctxt, new Object());
 assertEquals("John", handler.getProperty(handler, 0).get(new Object()));
 assertNull(handler.getProperty(handler, 1).get(new Object()));
 }

 @Test
 public void testHandlePropertyValue_MissingProperty() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"invalid\":\"String\"}");
 DeserializationContext ctxt = new TestDeserializationContext();

 assertFalse(handler.handlePropertyValue(p, ctxt, "name", null));
 assertFalse(handler.handlePropertyValue(p, ctxt, "age", null));
 }

 @Test
 public void testComplete_NullTypeIds() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\"}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handlePropertyValue(p, ctxt, "name", null);
handler.handlePropertyValue(p, ctxt, "age", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertEquals(null, bean);
 }

 @Test
 public void testComplete_NullProperties() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"type\":\"String\"}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handleTypePropertyValue(p, ctxt, "type", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertEquals(null, bean);
 }

 @Test
 public void testComplete_NullValues() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":null, \"age\":null}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handlePropertyValue(p, ctxt, "name", null);
 handler.handlePropertyValue(p, ctxt, "age", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertEquals(null, bean);
 }

 @Test
 public void testComplete_MissingTypeIds() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\"}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handlePropertyValue(p, ctxt, "name", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertNotNull(bean);
 assertEquals("John", handler.getProperty(handler, 0).get(bean));
 assertNull(handler.getProperty(handler, 1).get(bean));
 }

 @Test
 public void testComplete_MissingProperties() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"type\":\"String\"}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handleTypePropertyValue(p, ctxt, "type", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertNotNull(bean);
 assertNull(handler.getProperty(handler, 0).get(bean));
 assertNull(handler.getProperty(handler, 1).get(bean));
 }

 @Test
 public void testComplete_MissingValues() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"type\":\"String\"}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handleTypePropertyValue(p, ctxt, "type", null);
 handler.handlePropertyValue(p, ctxt, "name", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertNotNull(bean);
 assertEquals("John", handler.getProperty(handler, 0).get(bean));
 assertNull(handler.getProperty(handler, 1).get(bean));
 }

 @Test
 public void testComplete_TypeIds() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"type\":\"String\", \"age\":{\"type\":\"Number\", \"value\":42}}");
 DeserializationContext ctxt = new TestDeserializationContext();

 handler.handleTypePropertyValue(p, ctxt, "type.0", null);
 handler.handleTypePropertyValue(p, ctxt, "type.1", null);
 handler.handlePropertyValue(p, ctxt, "name", null);
 handler.handlePropertyValue(p, ctxt, "age.type", null);
 handler.handlePropertyValue(p, ctxt, "age.value", null);
 p.nextToken(); // consume root object end token
 Object bean = handler.complete(p, ctxt, new Object());
 assertNotNull(bean);
 assertEquals("John", handler.getProperty(handler, 0).get(bean));
 assertEquals(42, handler.getProperty(handler, 1).get(bean));
 }

 @Test
 public void testComplete_NonCreatorProperties() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"age\":42}");
 DeserializationContext ctxt = new TestDeserializationContext();
 PropertyValueBuffer buffer = new PropertyValueBuffer();

 handler.handlePropertyValue(p, ctxt, "name", null);
 handler.handlePropertyValue(p, ctxt, "age", null);
 p.nextToken(); // consume root object end token
 handler.complete(p, ctxt, buffer, null);
 Object bean = handler.getProperty(handler, 0).get(buffer.getBuffer());
 assertEquals("John", bean);
 }

 @Test
 public void testComplete_CreatorProperties() throws IOException {
 ExternalTypeHandler handler = setupHandler(new String[]{"String", "Number"}, new String[]{"name", "age"});
 JsonParser p = MAPPER.getFactory().createParser("{\"type\":\"StringCreator\", \"name\":[\"John\"], \"age\":{\"type\":\"NumberCreator\", \"value\":42}}");
 DeserializationContext ctxt = new TestDeserializationContext();
 PropertyValueBuffer buffer = new PropertyValueBuffer();

 handler.handleTypePropertyValue(p, ctxt, "type.0", null);
 handler.handleTypePropertyValue(p, ctxt, "type.1", null);
 handler.handlePropertyValue(p, ctxt, "name[0]", null);
 handler.handlePropertyValue(p, ctxt, "age.type", null);
 handler.handlePropertyValue(p, ctxt, "age.value", null);
 p.nextToken(); // consume root object end token
 handler.complete(p, ctxt, buffer, new CreatorTestClass());
 Object bean = handler.getProperty(handler, 0).get(buffer.getBuffer());
 assertEquals("John", bean);
 assertEquals(42, handler.getProperty(handler, 1).get(bean));
 }

 private ExternalTypeHandler setupHandler(String[] expectedTypes, String[] propertyNames) {
 SettableBeanProperty nameProp = new SimplePojo("name");
 SettableBeanProperty ageProp = new SimplePojo("age");
 TypeDeserializer nameDeser = new StringDeserializer();
 TypeDeserializer numberDeser = new NumberDeserializer();
 ExtTypeHandler handler = new ExternalTypeHandler.Builder()
 .addExternal(nameProp, nameDeser)
 .addExternal(ageProp, numberDeser)
 .build();
 return handler;
 }

 static class CreatorTestClass {
 public void create(Object t, Object n) {}
 }

 static class SimplePojo {
 private String name;

 @JsonProperty
 public String getName() {
 return name;
 }

 @JsonProperty
 public void setName(String name) {
 this.name = name;
 }
 }
}