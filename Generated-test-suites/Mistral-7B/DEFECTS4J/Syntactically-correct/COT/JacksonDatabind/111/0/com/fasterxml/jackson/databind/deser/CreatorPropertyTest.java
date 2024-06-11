package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class CreatorPropertyTest {

 private static final ObjectMapper mapper = new ObjectMapper();
 private static final PropertyName PROPERTY_NAME = PropertyName.construct("testProperty");
 private static final JavaType PROPERTY_TYPE = mapper.getTypeFactory().constructType(String.class);
 private static final JavaType WRAPPER_TYPE = mapper.getTypeFactory().constructType(CreatorProperty.class);

 @org.junit.BeforeClass
 public static void setup() {
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @org.junit.Test
 public void testCreatorPropertyConstruction() {
 CreatorProperty property =new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, null);
 assertEquals(PROPERTY_NAME, property.getName());
 assertNull(property.getFallbackSetter());
 assertNull(property._annotated);
 assertNull(property._injectableValueId);
 assertNull(property._fallbackSetter);
 assertNull(property._creatorIndex);
 assertFalse(property.isIgnorable());
 }

 @org.junit.Test
 public void testCreatorPropertyWithAnnotatedParameter() throws Exception {
 TestClass testClass = new TestClass();
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, testClass.getClass().getDeclaredAnnotations(), testClass.getClass().getDeclaredConstructor().getAnnotations()[0], 0, null, null);
 assertEquals(PROPERTY_NAME, property.getName());
 assertNotNull(property._annotated);
 assertEquals(TestClass.class, property._annotated.getAnnotationType());
 assertNull(property._injectableValueId);
 assertNull(property._fallbackSetter);
 assertNull(property._creatorIndex);
 assertFalse(property.isIgnorable());
 }

 @org.junit.Test
 public void testCreatorPropertyWithInjectValueId() throws Exception {
 TestInjectableClass testClass = new TestInjectableClass();
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, testClass.getClass().getDeclaredAnnotations(), null, 0, "testInjectableValueId", null);
 assertEquals(PROPERTY_NAME, property.getName());
 assertNull(property._annotated);
 assertNull(property._injectableValueId);
 assertNull(property._fallbackSetter);
 assertNull(property._creatorIndex);
 assertFalse(property.isIgnorable());

 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode node = objectMapper.createObjectNode();
 node.put("testProperty", "testValue");
 ObjectNode rootNode = objectMapper.readValue(node.toString(), ObjectNode.class);
 DeserializationContext context = new TestDeserializationContext();
 Object value = property.findInjectableValue(context, null);
 assertNull(value);
 property.inject(context, null);
 assertEquals("testValue", property.get(mapper.readValue(rootNode.toString(), TestClass.class).getFieldByAnnotation(TestInject.class).get(0)));
 }

 @org.junit.Test
 public void testCreatorPropertyWithFallbackSetter() throws Exception {
 TestFallbackClass testClass = new TestFallbackClass();
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, testClass.getClass().getDeclaredField("_testFallbackSetter"));
 assertEquals(PROPERTY_NAME, property.getName());
 assertNull(property._annotated);
 assertNull(property._injectableValueId);
 assertNotNull(property._fallbackSetter);
 assertNull(property._creatorIndex);
 assertFalse(property.isIgnorable());

 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode node = objectMapper.createObjectNode();
 node.put("testProperty", "testValue");
 ObjectNode rootNode = objectMapper.readValue(node.toString(), ObjectNode.class);
 TestFallbackClass testInstance = mapper.readValue(rootNode.toString(), TestFallbackClass.class);
 property.setFallbackSetter(testInstance._testFallbackSetter);
 assertEquals("testValue", property.get(testInstance));
 }

 @org.junit.Test
 public void testCreatorPropertyWithNameChange() throws Exception {
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, null);
 CreatorProperty newProperty = new CreatorProperty(property, new PropertyName("newTestProperty"));
 assertEquals("newTestProperty", newProperty.getName());
 assertEquals(PROPERTY_TYPE, newProperty.getType());
 assertEquals(PROPERTY_NAME, property.getName());
 assertEquals(newProperty, property.withName(newProperty.getName()));
 }

 @org.junit.Test
 public void testCreatorPropertyWithValueDeserializer() throws Exception {
 TestClass testClass = new TestClass();
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, testClass.getClass().getDeclaredAnnotations(), testClass.getClass().getDeclaredConstructor().getAnnotations()[0], 0, null, null);
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {};
 property = new CreatorProperty(property, deserializer, null);
 assertEquals(PROPERTY_NAME, property.getName());
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @org.junit.Test
 public void testCreatorPropertyWithNullProvider() throws Exception {
 TestClass testClass = new TestClass();
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, testClass.getClass().getDeclaredAnnotations(), testClass.getClass().getDeclaredConstructor().getAnnotations()[0], 0, null, null);
 NullValueProvider nullValueProvider = new TestNullValueProvider();
 property = new CreatorProperty(property, null, nullValueProvider);
 assertEquals(PROPERTY_NAME, property.getName());
 assertEquals(nullValueProvider, property.getNullProvider());
 }

 @org.junit.Test
 public void testCreatorPropertyWithFixAccess() throws Exception {
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, null);
 DeserializationContext context = new TestDeserializationContext();
 property.fixAccess(context);
 assertNotNull(property._fallbackSetter);
 }

 @org.junit.Test
 public void testCreatorPropertySetFallbackSetter() throws Exception {
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, null);
 SettableBeanProperty fallbackSetter = new SettableBeanProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null);
 property.setFallbackSetter(fallbackSetter);
 assertEquals(fallbackSetter, property._fallbackSetter);
 }

 @org.junit.Test
 public void testCreatorPropertyMarkAsIgnorable() throws Exception {
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, null);
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());
 }

 @org.junit.Test
 public void testCreatorPropertyWithMissingSetter() throws Exception {
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null, null, 0, null, null);
 assertThrows(IOException.class, () -> property.set(null, "testValue"));;
 }

 @org.junit.Test
 public void testCreatorPropertyWithoutValueDeserializer() throws Exception {
 TestClass testClass = new TestClass();
 CreatorProperty property = new CreatorProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, testClass.getClass().getDeclaredAnnotations(), testClass.getClass().getDeclaredConstructor().getAnnotations()[0], 0, null, null);
 SettableBeanProperty valueDeserializer = new SettableBeanProperty(PROPERTY_NAME, String.class, null, null, null);
 property = new CreatorProperty(property, valueDeserializer, null);
 JsonParser parser = mapper.getFactory().createParser("[\"testValue\"]");
 assertThrows(IOException.class, () -> property.deserializeAndSet(parser, null, null));
 }
}

 class TestClass {
 @JsonCreator
 public TestClass(String testValue) {
 this.testValue = testValue;
 }

 @JsonProperty("testProperty")
 String testValue;

 @JsonProperty(value = "testProperty", index = 0)
 @TestInject
 public SettableBeanProperty fieldByAnnotation(TestInject annotation) {
 return new SettableBeanProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null);
 }
 }

 class TestInjectableClass {
 @JsonCreator
 public TestInjectableClass(String testValue) {
 this.testValue = testValue;
 }

 @JsonProperty("testProperty")
 String testValue;

 @JsonProperty(value = "testProperty", index = 0)
 @TestInject
 public SettableBeanProperty fieldByAnnotation(TestInject annotation) {
 return new SettableBeanProperty(PROPERTY_NAME, PROPERTY_TYPE, null, null, null);
 }

 @TestInject
 Object testInjectableValueId;
 }

 class TestNullValueProvider implements NullValueProvider {
 @Override
 public Object nullValue(DeserializationContext ctxt, JavaType type) {
 return null;
 }
 }

 class TestFallbackClass {
 private SettableBeanProperty _testFallbackSetter;

 public TestFallbackClass() {
 }

 public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
 this._testFallbackSetter = fallbackSetter;
 }

 public SettableBeanProperty getFallbackSetter() {
 return _testFallbackSetter;
 }

 @JsonCreator
 public TestFallbackClass(String testValue) {
 this.testValue = testValue;
 }

 @JsonProperty("testProperty")
 String testValue;
}

 class TestDeserializationContext extends DeserializationContext {
 @Override
 public AnnotationIntrospector _annotationIntrospector() {
 return AnnotationIntrospector.getDefault();
 }

 @Override
 public Object findInjectableValue(Object id, BeanProperty property, Object context) throws IOException {
 return null;
 }

 @Override
 public Object findValueHandler(DeserializationConfig config, BeanProperty property) throws IOException {
 return null;
 }

 @Override
 public Object findValueHandler(DeserializationConfig config, PropertyName propertyName) throws IOException {
 return null;
 }

 @Override
 public Object findValueHandler(DeserializationConfig config, Class<?> type) throws IOException {
 return null;
 }

 @Override
 public Object findValueHandler(DeserializationConfig config, JavaType type) throws IOException {
 return null;
 }
}

 class TestInject {
 }