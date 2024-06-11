package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InnerClassPropertyTest {

 private static final Class<InnerClassProperty> INNER_CLASS_PROPERTY_CLASS = InnerClassProperty.class;
 private static final ObjectNode TEST_JSON_NODE;

 static {
 try {
 TEST_JSON_NODE = new ObjectMapper().createObjectNode();
 } catch (JsonProcessingException e) {
 throw new RuntimeException("Failed to create test JSON node", e);
 }
 }

 private JsonFactory jsonFactory;
 private DeserializationContext context;
 private InnerClassProperty innerClassProperty;

 @Before
 public void setup() throws Exception {
 jsonFactory = new ObjectMapper().getFactory();
 context = jsonFactory.createContextFinder()
 .findContext(InnerClassProperty.class);
 innerClassProperty = new InnerClassProperty(new SimpleBeanProperty(new SimpleBeanProperty.Std("name", String.class)),
 new SimpleInnerClassConstructor());
 }

 @After
 public void teardown() throws Exception {
 if (innerClassProperty != null) {
 innerClassProperty = null;
 }
 }

 @Test
 public void testNormalCase() throws Exception {
 Object expected = new SimpleInnerClass();
 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.toString());
 InnerClassProperty property = innerClassProperty;
 Object bean = new SimpleBean();
 property.deserializeAndSet(jp, context, bean);
 assertEquals(expected, bean.getInnerClassProperty());
 }

 @Test
 public void testValueNull() throws Exception {
 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.putNull("innerClassProperty"));
 InnerClassProperty property = innerClassProperty;
 Object bean = new SimpleBean();
 property.deserializeAndSet(jp, context, bean);
 assertNull(bean.getInnerClassProperty());
 }

 @Test
 public void testValueTypeDeserializer() throws Exception {
 SimpleInnerClassTypeDeserializer valueTypeDeserializer = new SimpleInnerClassTypeDeserializer();
 TEST_JSON_NODE.put("innerClassProperty", TEST_JSON_NODE.textValue("\"TestValue\""));
 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.toString());
 InnerClassProperty property = innerClassProperty;
 Object bean = new SimpleBean();
 property.deserializeAndSet(jp, context, bean, valueTypeDeserializer);
 assertEquals("TestValue", bean.getInnerClassProperty().getValue());
 }

 @Test(expected = IOException.class)
 public void testInvalidConstructor() throws Exception {
 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.toString());
 InnerClassProperty property = new InnerClassProperty(innerClassProperty, null);
 Object bean = new SimpleBean();
 property.deserializeAndSet(jp, context, bean);
 fail("Expected IOException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrokenSerialization() throws Exception {
 innerClassProperty = new InnerClassProperty(innerClassProperty, null);
 Object instance = innerClassProperty;
 assertEquals(instance, instance.readResolve());
 instance = instance.writeReplace();
 assertNotSame(instance, innerClassProperty);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 SimpleInnerClass expected = new SimpleInnerClass();
 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.toString());
 InnerClassProperty property = innerClassProperty;
 Object bean = new SimpleBean();
 Object value = property.deserializeSetAndReturn(jp, context, bean);
 assertEquals(expected, value);
 assertEquals(expected, bean.getInnerClassProperty());
 }

 @Test
 public void testSet() throws Exception {
 SimpleInnerClass expected = new SimpleInnerClass();
 Object bean = new SimpleBean();
 property.set(bean, expected);
 assertEquals(expected, bean.getInnerClassProperty());
 }

 @Test
 public void testInnerClassWithAnnotations() throws Exception {
 @JsonDeserialize(using = SimpleInnerClassAnnotatedDeserializer.class)
 class AnnotatedSimpleBean {
 private SimpleInnerClass innerClassProperty;
 // getter and setter omitted for brevity
 }

 AnnotatedSimpleBean bean = new AnnotatedSimpleBean();
 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.toString());
 AnnotatedClass annotatedClass = context.findClass(AnnotatedSimpleBean.class);
 AnnotatedConstructor annotatedConstructor = annotatedClass.getDeclaredConstructor();
 InnerClassProperty property = new InnerClassProperty(new SimpleBeanProperty(new SimpleBeanProperty.Std("innerClassProperty", AnnotatedSimpleBean.class)),
 annotatedConstructor);
 property.deserializeAndSet(jp, context, bean);
 SimpleInnerClass expected = new SimpleInnerClass();
 assertEquals(expected, bean.getInnerClassProperty());
 }

 @Test
 public void testNestedValueDeserializer() throws Exception {
 class NestedJsonDeserializer extends StdSerializer<SimpleInnerClass> {
 protected NestedJsonDeserializer() {
 this(null);
 }

 protected NestedJsonDeserializer(Class<SimpleInnerClass> t) {
 super(t);
 }

 @Override
 public SimpleInnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new SimpleInnerClass();
 }
 }

 JsonParser jp = jsonFactory.createParser(TEST_JSON_NODE.toString());
 JsonDeserializer<SimpleInnerClass> valueDeserializer = new NestedJsonDeserializer();
 InnerClassProperty property = new InnerClassProperty(innerClassProperty, valueDeserializer);
 Object bean = new SimpleBean();
 property.deserializeAndSet(jp, context, bean);
 SimpleInnerClass expected = new SimpleInnerClass();
 assertEquals(expected, bean.getInnerClassProperty());
 }
}

class SimpleBean {
 private SimpleInnerClass innerClassProperty;
 // getter and setter omitted for brevity
}

class SimpleInnerClass {
 private String value;
 // getter and setter omitted for brevity
}

class SimpleBeanProperty extends BeanPropertyDefinition {
 public SimpleBeanProperty(PropertyDefinition p, Class<?> propertyType) {
 super(p, propertyType);
 }

 @Override
 public BeanPropertyDefinition withName(PropertyName newName) {
 return new SimpleBeanProperty(this, newName);
 }

 @Override
 public BeanPropertyDefinition withValueDeserializer(JsonDeserializer<?> deser) {
 return new SimpleBeanProperty(this, deser);
 }
}

class SimpleInnerClassConstructor implements Constructor<SimpleInnerClass> {
 public SimpleInnerClassConstructor() {
 try {
 this.constructor = SimpleInnerClass.class.getDeclaredConstructor();
 constructor.setAccessible(true);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException("Failed to find constructor for SimpleInnerClass", e);
 }
 }

 @Override
 public SimpleInnerClass newInstance(Object... initargs) throws InstantiationException, IllegalAccessException, InvocationTargetException {
 return constructor.newInstance();
 }

 private Constructor<SimpleInnerClass> constructor;
}

class SimpleInnerClassAnnotatedDeserializer extends JsonDeserializer<SimpleInnerClass> {
 @Override
 public SimpleInnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return new SimpleInnerClass();
 }
}

class SimpleInnerClassTypeDeserializer extends JsonDeserializer<String> {
 @Override
 public SimpleInnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return new SimpleInnerClass();
 }
}