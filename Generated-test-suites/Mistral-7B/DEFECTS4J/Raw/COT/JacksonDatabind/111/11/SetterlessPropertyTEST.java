package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;

public class SetterlessPropertyTest {

 private static final Class<SetterlessProperty> SETTERLESS_PROPERTY_CLASS = SetterlessProperty.class;

 @Test
 public void testConstructor() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 assertNotNull(setterlessProperty);
 }

 @Test
 public void testWithName() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 SettableBeanProperty settableBeanProperty = setterlessProperty.withName(null);
 assertSame(setterlessProperty, settableBeanProperty);
 }

 @Test
 public void testWithValueDeserializer() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 JsonDeserializer<?> deserializer = null;
 SettableBeanProperty settableBeanProperty = setterlessProperty.withValueDeserializer(deserializer);
 assertSame(setterlessProperty, settableBeanProperty);
 }

 @Test
 public void testWithNullProvider() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 NullValueProvider nullValueProvider = null;
 SettableBeanProperty settableBeanProperty = setterlessProperty.withNullProvider(nullValueProvider);
 assertSame(setterlessProperty, settableBeanProperty);
 }

 @Test
 public void testFixAccess() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 DeserializationConfig config = new DeserializationConfig();
 setterlessProperty.fixAccess(config);
 assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testGetAnnotation() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 Class<Annotation> annotationClass = Annotation.class;
 Annotation annotation = setterlessProperty.getAnnotation(annotationClass);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 AnnotatedMember annotatedMember = setterlessProperty.getMember();
 assertNotNull(annotatedMember);
 }

 @Test
 public void testDeserializeAndSetNull() throws Exception {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 JsonParser p = createJsonParser(new ObjectMapper(), "null");
 setterlessProperty.deserializeAndSet(p, new DeserializationContext(), instance);
 assertNull(getInstanceValue(instance, setterlessProperty));
 }

 @Test
 public void testDeserializeAndSetNonNull() throws Exception {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 JsonParser p = createJsonParser(new ObjectMapper(), "[1]");
 setterlessProperty.deserializeAndSet(p, new DeserializationContext(), instance);
 assertNotNull(getInstanceValue(instance, setterlessProperty));
 assertEquals(1, getListSize(getInstanceValue(instance, setterlessProperty)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 setterlessProperty.set(instance, new Object());
 fail();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);Object value = new Object();
 Object result = setterlessProperty.setAndReturn(instance, value);
 fail();
 }

 @Test(expected = IOException.class)
 public void testThrowIOException() throws Exception {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 JsonParser p = createJsonParser(new ObjectMapper(), "invalidJson");
 setterlessProperty.deserializeAndSet(p, new DeserializationContext(), instance);
 fail();
 }

 @Test(expected = IllegalAccessException.class)
 public void testInvokeGetMethodFails() throws Exception {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 Method getter = ReflectionTestUtils.getMethod(instance.getClass(), setterlessProperty.getName());
 ReflectionTestUtils.invokeMethod(getter, instance);
 fail();
 }

 private JsonParser createJsonParser(ObjectMapper mapper, String json) throws IOException {
 ObjectReader reader = mapper.readerForElement(JsonToken.START_OBJECT);
 JsonParser p = reader.readValue(new ByteArrayInputStream(json.getBytes()));
 return p;
 }

 private Object getInstanceValue(Object instance, SetterlessProperty setterlessProperty) {
 try {
 return setterlessProperty.getter.invoke(instance, (Object[]) null);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 private int getListSize(Object object) {
 if (object instanceof Collection) {
 return ((Collection) object).size();
 } else if (object instanceof Map) {
 return ((Map) object).size();
 } else {
 throw new IllegalArgumentException("Unexpected type: " + object.getClass().getName());
 }
 }
}