package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private static final Object TEST_INSTANCE = new Object() {{}};

 @Test
 public void testSettableAnyPropertyConstructor() {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, null, null);
 assertNull(property._property);
 assertNull(property._setter);
 assertFalse(property._setterIsField);
 assertNull(property._type);
 assertNull(property._valueDeserializer);
 assertNull(property._valueTypeDeserializer);
 assertNull(property._keyDeserializer);
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializer() {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, new StubJsonDeserializer<>(), null);
 assertTrue(property.hasValueDeserializer());
 }

 @Test
 public void testSettableAnyPropertyWithoutValueDeserializer() {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, null, null);
 assertFalse(property.hasValueDeserializer());
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializerSetter() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), new AnnotatedMethod(null), null, null, new StubJsonDeserializer<>(), null);
 property.fixAccess(null);
 Object value = new Object();
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 property.deserializeAndSet(parser, context, TEST_INSTANCE, "propertyName");
 assertEquals(value, TEST_INSTANCE.getClass().getDeclaredField("propertyName").get(TEST_INSTANCE));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyUnresolvedForwardReference() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, new StubJsonDeserializer<Object>(), null);
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 property.deserializeAndSet(parser, context, TEST_INSTANCE, "propertyName");
}

 @Test
 public void testSettableAnyPropertyWithKeyDeserializer() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, new StubKeyDeserializer<>(), null, null);
 Object value = new Object();
 Object key = new Object();
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 parser.setValue(key);
 parser.nextToken();
 property.deserializeAndSet(parser, context, TEST_INSTANCE, null);
 assertEquals(key, TEST_INSTANCE.getClass().getDeclaredField("propertyName").get(TEST_INSTANCE));
 assertEquals(value, TEST_INSTANCE.getClass().getDeclaredField("propertyValue").get(TEST_INSTANCE));
 }

 @Test
 public void testSettableAnyPropertyWithTypeDeserializer() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, new ClassType(String.class), null, null, new StubTypeDeserializer<>());
 Object value = "test";
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 property.deserializeAndSet(parser, context, TEST_INSTANCE, "propertyName");
 assertEquals("test", TEST_INSTANCE.getClass().getDeclaredField("propertyName").get(TEST_INSTANCE));
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializerAndTypeDeserializer() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, new StubJsonDeserializer<Object>(), new StubTypeDeserializer<>());
 Object key = new Object();
 Object value = new Object();
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 parser.setValue(key);
 parser.nextToken();
 property.deserializeAndSet(parser, context, TEST_INSTANCE, null);
 assertEquals(key, TEST_INSTANCE.getClass().getDeclaredField("propertyName").get(TEST_INSTANCE));
 assertEquals(value, TEST_INSTANCE.getClass().getDeclaredField("propertyValue").get(TEST_INSTANCE));
 }

 @Test
 public void testSettableAnyPropertyWithField() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), new AnnotatedField(null), null, null, null, null);
 Map<Object, Object> map = new HashMap<>();
 Object value = new Object();
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 parser.nextValue();
 property.set(TEST_INSTANCE, "propertyName", value);
 assertEquals(value, map.get("propertyName"));
 }

 @Test
 public void testThrowAsIOE() {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, null, null);
 try {
 property.set(TEST_INSTANCE, "propertyName", null);
 } catch (JsonMappingException e) {
 String expectedMessage = "Problem deserializing \"any\" property 'propertyName' of class com.fasterxml.jackson.databind.deser.test.TestInstance (expected type: class java.lang.String; actual type: null)";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testThrowAsIOEWithWrongType() throws JsonProcessingException {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, new StubJsonDeserializer<Object>(), null);
 Object value = new Object();
 JsonParser parser = createMockParser();
 DeserializationContext context = createMockContext();
 parser.nextValue();
 try {
 property.set(TEST_INSTANCE, "propertyName", value);
 fail();
 } catch (JsonMappingException e) {
 String expectedMessage = "Problem deserializing \"any\" property 'propertyName' of class com.fasterxml.jackson.databind.deser.test.TestInstance (expected type: class java.lang.String; actual type: class com.fasterxml.jackson.databind.deser.SettableAnyPropertyTest$TestInstance)";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testToString() {
 SettableAnyProperty property = new SettableAnyProperty(new BeanProperty(null), null, null, null, null, null);
 assertEquals("[any property on class com.fasterxml.jackson.databind.deser.SettableAnyPropertyTest$TestInstance]", property.toString());
 }

 private DeserializationContext createMockContext() {
 return mock(DeserializationContext.class);
 }

 private JsonParser createMockParser() {
 return mock(JsonParser.class);
 }

 private static class StubJsonDeserializer<T> extends JsonDeserializer<T> {
 @Override
 public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 }

 private static class StubKeyDeserializer extends KeyDeserializer {
 @Override
 public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
 return null;
 }
 }

 private static class StubTypeDeserializer extends TypeDeserializer {
 @Override
 public void deserializeType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
 }
 }

 private static class ClassType extends JavaType {
 private final Class<?> _rawClass;

 public ClassType(Class<?> clazz) {
 super(null, null, null);
 _rawClass = clazz;
 }

 @Override
 public Class<?> getRawClass() {
 return _rawClass;
 }

 @Override
 public String getClassName() {
 return _rawClass.getName();
 }
 }
}