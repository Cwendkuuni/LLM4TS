package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ObjectMapper mapper;
 private ExternalTypeHandler handler;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();

 // create test object with two properties
 Builder builder = new Builder();
 builder.addExternal(new SettableBeanProperty("prop1", Person.class), new PersonTypeDeserializer());
 builder.addExternal(new SettableBeanProperty("prop2", IntTypeDeserializer.getInstance()),
 IntTypeDeserializer.class);
 handler = builder.build();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 // test handleTypePropertyValue with valid type id
 JsonParser p = mapper.getFactory().createParser("{\"typeId\": \"Person\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = handler.handleTypePropertyValue(p, ctxt, "typeId", null);
 assertTrue(result instanceof Person);

 // test handleTypePropertyValue with invalid type id
 p = mapper.getFactory().createParser("{\"typeId\": \"InvalidType\"}");
 ctxt = mapper.getDeserializationContext();
 result = handler.handleTypePropertyValue(p, ctxt, "typeId", null);
 assertNull(result);

 // test handleTypePropertyValue with missing type property name
 p = mapper.getFactory().createParser("{\"prop1\": \"Person\"}");
 ctxt = mapper.getDeserializationContext();
 result = handler.handleTypePropertyValue(p, ctxt, "prop1", null);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 // test handlePropertyValue with valid property and value
 p = mapper.getFactory().createParser("{\"prop1\": 123}");
 ctxt = mapper.getDeserializationContext();
 Object result = handler.handlePropertyValue(p, ctxt, "prop1", null);
 assertEquals(123, result);

 // test handlePropertyValue with missing property name
 p = mapper.getFactory().createParser("{123: 123}");
 ctxt = mapper.getDeserializationContext();
 result = handler.handlePropertyValue(p, ctxt, "invalidPropName", null);
 assertNull(result);
 }

 @Test
 public void testCompleteWithObjects() throws Exception {
 // test complete with objects, no missing properties
 p = mapper.getFactory().createParser("{\"typeId\": \"Person\", \"prop1\": {}}");
 ctxt = mapper.getDeserializationContext();
 Object result = handler.complete(p, ctxt, null);
 assertTrue(result instanceof Person);

 // test complete with objects, missing external property
 p = mapper.getFactory().createParser("{\"typeId\": \"Person\"}");
 ctxt = mapper.getDeserializationContext();
 result = handler.complete(p, ctxt, null);
 assertEquals("Missing property 'prop1' for external type id 'Person'",
 ctxt.getParsingConfig().getParserErrors().getText(0));

 // test complete with objects, missing type id
 p = mapper.getFactory().createParser("{\"prop1\": {}}");
 ctxt = mapper.getDeserializationContext();
 result = handler.complete(p, ctxt, null);
 assertEquals("Missing external type id property 'typeId'",
 ctxt.getParsingConfig().getParserErrors().getText(0));
 }

 @Test
 public void testCompleteWithPropertyValueBuffer() throws Exception {
 // test complete with PropertyValueBuffer, no missing properties
 JsonParser p = mapper.getFactory().createParser("{\"typeId\": \"Person\", \"prop1\": 123}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 PropertyValueBuffer buffer = new PropertyValueBuffer(mapper);
 Object result = handler.complete(p, ctxt, buffer, null);
 assertTrue(result instanceof Person);
 assertEquals(123, result.getClass().getDeclaredField("prop1").get(result));

 // test complete with PropertyValueBuffer, missing external property
 p = mapper.getFactory().createParser("{\"typeId\": \"Person\"}");
 ctxt = mapper.getDeserializationContext();
 result = handler.complete(p, ctxt, buffer, null);
 assertEquals("Missing property 'prop1' for external type id 'Person'",
 ctxt.getParsingConfig().getParserErrors().getText(0));
 }

 @Test
 public void testDeserialize() throws Exception {
 // test deserialize with valid type id
 p = mapper.getFactory().createParser("{\"typeId\": \"Person\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String typeId = handler.deserialize(p, ctxt, 0).getClass().getDeclaredField("typeId").get(handler.deserialize(p, ctxt, 0));
 assertEquals("Person", typeId);

 // test deserialize with missing type id
 p = mapper.getFactory().createParser("{123: 123}");
 ctxt = mapper.getDeserializationContext();
 List<Object> results = new ArrayList<Object>();
 for (int i = 0; i < handler.getPropertyCount(); i++) {
 results.add(handler.deserialize(p, ctxt, i));
 }
 assertEquals(0, results.size());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 // test deserializeAndSet with valid type id
 p = mapper.getFactory().createParser("{\"typeId\": \"Person\", \"prop1\": 123}");
 ctxt = mapper.getDeserializationContext();
 handler.deserializeAndSet(p, ctxt, 0, "prop1", new Person());
 assertEquals(123, ((Person) handler.getBean()).getProp1());

 // test deserializeAndSet with missing type id
 p = mapper.getFactory().createParser("{123: 123}");
 ctxt = mapper.getDeserializationContext();
 handler.deserializeAndSet(p, ctxt, 0, "prop1", new Person());
 assertEquals(0, ((Person) handler.getBean()).getProp1());
 }
}

class Person {
 private int prop1;

 public int getProp1() {
 return prop1;
 }

 public void setProp1(int prop1) {
 this.prop1 = prop1;
 }
}

class PersonTypeDeserializer extends JsonDeserializer<Person> {
 @Override
 public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new Person();
 }
}

class IntTypeDeserializer extends JsonDeserializer<Integer> {
 private static final IntTypeDeserializer instance = new IntTypeDeserializer();

 public static IntTypeDeserializer getInstance() {
 return instance;
 }

 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getIntValue();
 }
}