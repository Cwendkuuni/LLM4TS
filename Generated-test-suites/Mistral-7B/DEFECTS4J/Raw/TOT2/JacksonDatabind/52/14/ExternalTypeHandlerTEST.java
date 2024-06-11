package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class ExternalTypeHandlerTest {

 private static JsonFactory factory = new JsonFactory();
 private ObjectMapper mapper;

 @Before
 public void init() throws JsonProcessingException {
 mapper = new ObjectMapper();
 }

 @Test
 public void testStart() throws IOException {
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 ExternalTypeHandler initializedHandler = handler.start();
 assertNotNull(initializedHandler);
 }

 @Test
 public void testHandleTypePropertyValueHappyPath() throws IOException {
 String json = "{\"typeProperty\":\"String\",\"typeId\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handleTypePropertyValue(parser, ctxt, "typeProperty", bean);
 assertTrue(result);
 assertEquals("StringType", bean.getClass().getName());
 }

 @Test
 public void testHandleTypePropertyValueNotFound() throws IOException {
 String json = "{\"property\":\"typeProperty\",\"typeId\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handleTypePropertyValue(parser, ctxt, "property", bean);
 assertFalse(result);
 }

 @Test
 public void testHandleTypePropertyValueNullBean() throws IOException {
 String json = "{\"typeProperty\":\"String\",\"typeId\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handleTypePropertyValue(parser, ctxt, "typeProperty", null);
 assertFalse(result);
 }

 @Test
 public void testHandleTypePropertyValueTokenNull() throws IOException {
 String json = "{\"typeProperty\":\"String\",\"typeId\":null}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handleTypePropertyValue(parser, ctxt, "typeProperty", bean);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValueHappyPath() throws IOException {
 String json = "{\"property\":\"stringProperty\",\"typeId\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new StringObject();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handlePropertyValue(parser, ctxt, "property", bean);
 assertTrue(result);
 assertEquals("StringType", bean.getClass().getName());
 }

 @Test
 public void testHandlePropertyValueNotFound() throws IOException {
 String json = "{\"property\":\"property\",\"typeId\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new StringObject();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handlePropertyValue(parser, ctxt, "property", bean);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValueNullBean() throws IOException {
 String json = "{\"property\":\"stringProperty\",\"typeId\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handlePropertyValue(parser, ctxt, "property", null);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValueTokenNull() throws IOException {
 String json = "{\"property\":\"stringProperty\",\"typeId\":null}";
 JsonParser parser = factory.createParser(json);
 Object bean = new StringObject();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 boolean result = handler.handlePropertyValue(parser, ctxt, "property", bean);
 assertFalse(result);
 }

 @Test
 public void testCompleteHappyPath() throws IOException {
 String json = "{\"stringProperty\":\"test\",\"intProperty\":1,\"typeProperty\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 handler.handlePropertyValue(parser, ctxt, "stringProperty", bean);
 handler.handlePropertyValue(parser, ctxt, "intProperty", bean);
 handler.handleTypePropertyValue(parser, ctxt, "typeProperty", bean);
 Object result = handler.complete(parser, ctxt, bean);
 assertEquals(bean.getClass().getName(), result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testCompleteMissingProperty() throws IOException {
 String json = "{\"intProperty\":1}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 handler.handlePropertyValue(parser, ctxt, "intProperty", bean);
 handler.complete(parser, ctxt, bean);
 }

 @Test(expected = IOException.class)
 public void testCompleteMissingTypeId() throws IOException {
 String json = "{\"typeProperty\":\"StringType\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 handler.handleTypePropertyValue(parser, ctxt, "typeProperty", bean);
 handler.complete(parser, ctxt, bean);
 }

 @Test(expected = IOException.class)
 public void testCompleteInvalidTypeId() throws IOException {
 String json = "{\"typeProperty\":\"InvalidTypeId\"}";
 JsonParser parser = factory.createParser(json);
 Object bean = new Object();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 handler.handleTypePropertyValue(parser, ctxt, "typeProperty", bean);
 handler.complete(parser, ctxt, bean);
 }

 @Test
 public void testBuilder() {
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(new MyClass().getProperty("name"), MyClassDeserializer.getInstance());
 builder.addExternal(new MyClass().getProperty("type"), MyTypeDeserializer.getInstance());
 ExternalTypeHandler handler = builder.build();
 assertEquals(2, handler._properties.size());
 }

 @Test
 public void testCompletePropertyBasedCreator() throws IOException {
 JsonParser parser = factory.createParser("{\"name\":\"John\",\"typeId\":\"EmployeeType\"}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new EmployeeCreator();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 DeserializationContext ctxt = mapper.getContext();
 Object result = handler.complete(parser, ctxt, buffer, creator);
 Employee employee = (Employee) result;
 assertEquals("John", employee.getName());
 }

}

class StringObject {
 private String value;

 public String getTypePropertyName() { return "StringType"; }

 public SettableBeanProperty getProperty() {
 return new SettableBeanProperty(String.class, "value");
 }

 public void setTypeId(String typeId) {
 this.typeId = typeId;
 }

 public String getTypeId() {
 return typeId;
 }

 public String getValue() {
 return value;
 }

 public void setValue(String value) {
 this.value = value;
 }
}

class MyClass {
 private String name;
 private MyType type;

 public String getName() { return name; }

 public void setName(String name) { this.name = name; }

 public MyType getType() { return type; }

 public void setType(MyType type) { this.type = type; }

 public static SettableBeanProperty getProperty(String name) {
 return new SettableBeanProperty(MyClass.class, name);
 }

 public static TypeDeserializer getDeserializer() {
 return new MyClassDeserializer();
 }
}

class MyType {
 // ...
}

class Employee {
 private String name;

 public String getName() { return name; }

 public void setName(String name) { this.name = name; }

 public static TypeDeserializer getDeserializer() {
 return new EmployeeDeserializer();
 }

 public static SettableBeanProperty getNameProperty() {
 return new SettableBeanProperty(Employee.class, "name");
 }
}

class EmployeeCreator extends CreatorBase {
 @Override
 public Object createUsingDefaults() {
 return new Employee();
 }

 @Override
 public Object createFromValues(PropertyValueBuffer pvb) {
 return new Employee();
 }
}

class MyClassDeserializer extends JsonDeserializer<MyClass> {
 public static MyClassDeserializer getInstance() { return instance; }

 private static final MyClassDeserializer instance = new MyClassDeserializer();

 @Override
 public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 MyClass result = new MyClass();
 result.setName(p.getValueAsString());
 result.setType(ctxt.findValueDeserializer(result.getTypePropertyName(), p, ctxt));
 return result;
 }
}

class MyTypeDeserializer extends JsonDeserializer<MyType> {
 @Override
 public MyType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new MyType();
 }
}

class EmployeeDeserializer extends JsonDeserializer<Employee> {
 @Override
 public Employee deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 Employee result = new Employee();
 result.setName(p.getValueAsString());
 return result;
 }
}