package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for ExternalTypeHandler
 */
public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 ObjectMapper context = new ObjectMapper();
 mapper = context.enable(DesSerializationFeature.UNWRAP_ROOT_VALUE);
 parser = context.getFactory().createParser("{\"foo\": \"bar\", \"type\": \"MyType\"}");
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("foo", String.class), new SimpleTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("type", MyType.class), new SimpleTypeDeserializer(MyType.class))
 .build());
 }

 /**
 * Test handling of external type id property
 */
 @Test
 public void testHandleTypePropertyValue() throws Exception {
 handler.handleTypePropertyValue(parser, mapper.getDeserializationContext(), "type", null);
 assertEquals("MyType", handler._typeIds[0]);
 }

 /**
 * Test handling of external property value
 */
 @Test
 public void testHandlePropertyValue() throws Exception {
 handler.handlePropertyValue(parser, mapper.getDeserializationContext(), "foo", null);
 assertEquals("bar", handler._tokens[0].getText());
 }

 /**
 * Test that all external type ids are handled after JSON Object closes
 */
 @Test
 public void testComplete() throws Exception {
 Object bean = handler.complete(parser, mapper.getDeserializationContext(), null);
 assertEquals("bar", handler.getProperties()[0].getProperty().get(bean));
 assertEquals("MyType", handler.getProperties()[1].getProperty().get(bean));
 }

 /**
 * Test handling of creator properties
 */
 @Test
 public void testCompleteWithCreator() throws Exception {
 PropertyName idName = PropertyName.construct("id");
 PropertyName nameName = PropertyName.construct("name");
 Object idValue = "MyId";
 Object nameValue = "MyName";
 Object bean = null;
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("id", String.class), new SimpleTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("name", MyName.class), new SimpleTypeDeserializer(MyName.class))
 .build());
 handler.addCreator(MyClass.class, new ObjectIdReader(), new MyCreator());
 parser = mapper.getFactory().createParser("{\"name\": \"MyName\", \"id\": \"MyId\"}");
 bean = handler.complete(parser, mapper.getDeserializationContext(), null, null);
 assertEquals(idValue, handler.getProperties()[0].getProperty().get(bean));
 assertEquals(nameValue, handler.getProperties()[1].getProperty().get(bean));
 }

 /**
 * Test handling of missing external type id property
 */
 @Test(expected = MappingException.class)
 public void testMissingTypeIdProperty() throws Exception {
 handler.complete(parser, mapper.getDeserializationContext(), null);
 fail("MissingTypeIdException should have been thrown");
 }

 /**
 * Test handling of missing external property
 */
 @Test(expected = MappingException.class)
 public void testMissingProperty() throws Exception {
 handler.handlePropertyValue(parser, mapper.getDeserializationContext(), "nonExisting", null);
 fail("MissingPropertyException should have been thrown");
 }

 /**
 * Test handling of missing property for type id
 */
 @Test(expected = MappingException.class)
 public void testMissingPropertyForTypeId() throws Exception {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("foo", String.class), new SimpleTypeDeserializer(String.class))
 .build());
 handler.handleTypePropertyValue(parser, mapper.getDeserializationContext(), "type", null);
 handler.handlePropertyValue(parser, mapper.getDeserializationContext(), "nonExisting", null);
 fail("MissingPropertyException should have been thrown");
 }

 /**
 * Test handling of missing both type and property
 */
 @Test(expected = MappingException.class)
 public void testMissingBothTypeAndProperty() throws Exception {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("foo", String.class), new SimpleTypeDeserializer(String.class))
 .build());
 handler.handleTypePropertyValue(parser, mapper.getDeserializationContext(), "nonExisting", null);
 parser.nextToken();
 handler.handlePropertyValue(parser, mapper.getDeserializationContext(), "nonExisting", null);
 fail("MappingException should have been thrown");
 }
}

class MyType {
}

class MyName {
}

class MyClass {
}

class SimpleTypeDeserializer extends TypeDeserializer {
 private final Class<Object> _valueClass;

 public SimpleTypeDeserializer(Class<Object> valueClass) {
 _valueClass = valueClass;
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsToken().getValueAsString();
 }

 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
 throws IOException {
 return p.getValueAsToken().getValueAsString();
 }

 @Override
 public TypeDeserializer forProperty(PropertyName name) {
 return this;
 }

 @Override
 public void resolve(DeserializationContext ctxt) throws JsonProcessingException {
 if (ctxt.getTypeIdResolver() != null) {
 ctxt.getTypeIdResolver().handleUnknownProperty(null, null, name, this);
 }
 }

 @Override
 public String getPropertyName() {
 return null;
 }

 @Override
 public Class<?> getValueClass() {
 return _valueClass;
 }
}

class MyCreator extends TypeDeserializer {
 @Override
 public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
 return new MyClass();
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new MyClass();
 }

 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
 throws IOException {
 return new MyClass();
 }

 @Override
 public TypeDeserializer forProperty(PropertyName name) {
 return this;
 }

 @Override
 public void resolve(DeserializationContext ctxt) throws JsonProcessingException {
 // do nothing
 }

 @Override
 public String getPropertyName() {
 return null;
 }

 @Override
 public Class<?> getValueClass() {
 return MyClass.class;
 }
}