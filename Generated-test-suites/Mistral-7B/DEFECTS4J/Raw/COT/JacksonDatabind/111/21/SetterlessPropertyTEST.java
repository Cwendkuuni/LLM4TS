package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SetterlessProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;

public class SetterlessPropertyTest {

 private static final Object INSTANCE = new Object();
 private static final JsonParser PARSER = new JsonFactory().createParser(new StringReader("{}"));

 @Test
 public void testConstructor() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 assertNull(property._annotated);
 assertNull(property._getter);
 }

 @Test
 public void testWithName() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 SetterlessProperty newProperty = property.withName(new PropertyName("test"));
 assertSame(property, newProperty);
 assertSame("test", newProperty.getName().getShortName());
 }

 @Test
 public void testWithValueDeserializer() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 SetterlessProperty newProperty = property.withValueDeserializer(new StubJsonDeserializer<>());
 assertSame(property, newProperty);
 assertNotSame(property._valueDeserializer, newProperty._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 SetterlessProperty newProperty = property.withNullProvider(new StubNullValueProvider());
 assertSame(property, newProperty);
 assertNotSame(property._nullProvider, newProperty._nullProvider);
 }

 @Test
 public void testFixAccess() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 AnnotatedMethod annotatedMethod = new AnnotatedMethod(SetterlessProperty.class.getDeclaredMethod("deserializeAndSet"), null);
 property.fixAccess(new DeserializationConfig().without(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 assertFalse(annotatedMethod.isVisible());
 }

 @Test
 public void testGetAnnotation() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(Object.class.getDeclaredMethod("test"), null));
 assertEquals(AnnotationTypes.TEST.getClass(), property.getAnnotation(AnnotationTypes.TEST.class));
 }

 @Test
 public void testGetMember() {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(Object.class.getDeclaredMethod("test"), null));
 assertEquals(new AnnotatedMethod(Object.class.getDeclaredMethod("test"), null), property.getMember());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 property.set(INSTANCE, new Object());
 fail("Should not call `set()` on setterless property");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, new AnnotatedMethod(Object.class.getDeclaredMethod("test"), null));
 property.deserializeAndSet(PARSER, new DeserializationContext(), INSTANCE);
 assertNotNull(INSTANCE);
 // TODO: add assertions for the modified instance
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, new AnnotatedMethod(Object.class.getDeclaredMethod("test"), null));
 Object result = property.deserializeSetAndReturn(PARSER, new DeserializationContext(), INSTANCE);
 assertEquals(INSTANCE, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
 property.setAndReturn(INSTANCE, new Object());
 fail("Should not call `set()` on setterless property");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvokeGetterWithArguments() throws IOException {
 SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(Object.class.getDeclaredMethod("test", int.class), null));
 property.deserializeAndSet(PARSER, new DeserializationContext(), INSTANCE);
 try {
 property.getter.invoke(INSTANCE, new Object[]{1});
 fail("Should throw an IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, new AnnotatedMethod(Integer.class.getDeclaredMethod("valueOf", String.class), null));
 TypeDeserializer typeDeserializer = new StubTypeDeserializer<>();
 JsonDeserializer<?> valueDeserializer = new StubJsonDeserializer<>();
 JsonDeserializer<?> valueTypeDeserializer = new StubJsonDeserializer<>();
 property.deserializeAndSet(PARSER, new DeserializationContext().withValueDeserializer(valueDeserializer).withTypeDeserializers(new TypeDeserializer[] { valueTypeDeserializer }), INSTANCE);
 assertEquals(42, (Integer) INSTANCE);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullValue() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, null);
 property.deserializeAndSet(PARSER, new DeserializationContext(), INSTANCE);
 PARSER.nextToken(); // Consume the null token
 assertEquals(null, INSTANCE);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidType() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, new AnnotatedMethod(Integer.class.getDeclaredMethod("valueOf", Double.class), null));
 property.deserializeAndSet(PARSER, new DeserializationContext(), INSTANCE);
 fail("Should throw a JsonMappingException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJson() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, null);
 property.deserializeAndSet(PARSER, new DeserializationContext(), INSTANCE);
 PARSER.nextToken(); // Consume the START_OBJECT token
 PARSER.nextToken(); // Consume the STRING token
 PARSER.nextToken(); // Consume the END_OBJECT token
 fail("Should throw an IOException");
 }

 @Test
 public void testDeserializeWithEmptyJson() throws IOException {
 SetterlessProperty property = new SetterlessProperty(new PropertyName("test"), null, null, null, null);
 property.deserializeAndSet(PARSER, new DeserializationContext(), INSTANCE);
 PARSER.nextToken(); // Consume the START_OBJECT token
 PARSER.nextToken(); // Consume the END_OBJECT token
 // TODO: add assertions for the empty instance
 }
}