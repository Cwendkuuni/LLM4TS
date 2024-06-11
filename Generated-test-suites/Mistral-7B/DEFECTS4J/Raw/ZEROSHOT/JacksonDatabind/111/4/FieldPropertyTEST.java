package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

 private static class TestBean {
 private int field;
 }

 @org.junit.BeforeClass
 public static void setup() {
 try {
 Field field = TestBean.class.getDeclaredField("field");
 field.setAccessible(true);

 FieldProperty fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(new PropertyName("field"), Object.class, null, null, null, null, null),
 ObjectTypeFactory.defaultInstance.constructType(int.class), null, null,
 Annotations.empty(), new AnnotatedField(new AnnotatedMember(field), null), field);

 TestFieldProperty testFieldProperty = new TestFieldProperty(fieldProperty);
 System.out.println("FieldProperty created.");
 } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @org.junit.Test
 public void testGetAnnotation() {
 TestBean testBean = new TestBean();
 Annotation annotation = mock(Annotation.class);
 when(testFieldProperty.getMember().getAnnotated()).thenReturn(annotation);
 assertEquals(annotation, testFieldProperty.getAnnotation(annotation.getClass()));
 }

 @org.junit.Test
 public void testSetValueAndGetValue() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsInt()).thenReturn(42);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object target = new TestBean();

 testFieldProperty.deserializeAndSet(jsonParser, deserializationContext, target);
 assertEquals(42, target.field);

 Object originalValue = target.field;
 testFieldProperty.set(target, "newValue");
 assertEquals("newValue", target.field);
 assertEquals(originalValue, target.field);
 }

 @org.junit.Test
 public void testSetValueAndReturnInstance() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsInt()).thenReturn(42);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object target = new TestBean();

 Object result = testFieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, target);
 assertEquals(target, result);
 }

 @org.junit.Test
 public void testDeserializeAndSetNull() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object target = new TestBean();

 testFieldProperty.deserializeAndSet(jsonParser, deserializationContext, target);
 assertNull(target.field);
 }

 @org.junit.Test
 public void testDeserializeAndSetNullWithSkipNullsFalse() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object target = new TestBean();
 when(deserializationContext.getConfig().isEnabled(MapperFeature.AUTO_DETECT_NULLS)).thenReturn(false);

 testFieldProperty.deserializeAndSet(jsonParser, deserializationContext, target);
 assertNull(target.field);
 }

 @org.junit.Test
 public void testDeserializeAndSetNullWithSkipNullsTrue() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object target = new TestBean();
 when(deserializationContext.getConfig().isEnabled(MapperFeature.AUTO_DETECT_NULLS)).thenReturn(true);
 when(deserializationContext.findNullValueProvider()).thenReturn(new NullValueProviderDefault());

 testFieldProperty.deserializeAndSet(jsonParser, deserializationContext, target);
 assertEquals(0, target.field);
 }

 @org.junit.Test
 public void testSetValue() throws IOException {
 Object target = new TestBean();
 testFieldProperty.set(target, 42);
 assertEquals(42, target.field);
 }

 @org.junit.Test
 public void testSetValueWithNull() throws IOException {
 Object target = new TestBean();
 testFieldProperty.set(target, null);
 assertNull(target.field);
 }

 @org.junit.Test(expected = IOException.class)
 public void testSetValueWithException() throws IOException {
 Object target = new TestBean();
 when(jsonParser.getValueAsInt()).thenThrow(new IOException());
 testFieldProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), target);
 }

 @org.junit.Test(expected = IOException.class)
 public void testSetValueWithExceptionAndReturn() throws IOException {
 Object target = new TestBean();
 when(jsonParser.getValueAsInt()).thenThrow(new IOException());
 testFieldProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), target);
 }
}

class TestFieldProperty extends FieldProperty {
 TestFieldProperty(SettableBeanProperty fieldProperty) {
 super(fieldProperty);
 }

 @Override
 void _throwAsIOE(Object source, IOException e, Object value) throws IOException {
 throw e;
 }
}

class NullValueProviderDefault extends NullValueProvider.Base {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return 0;
 }
}