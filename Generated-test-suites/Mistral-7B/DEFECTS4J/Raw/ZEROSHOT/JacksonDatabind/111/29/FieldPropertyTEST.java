package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

 private static class TestBean {
 private int field;
 }

 @SuppressWarnings("unchecked")
 @org.junit.BeforeClass
 public static void setup() throws Exception {
 JsonMapper mapper = new JsonMapper();
 ObjectCodec oc = mapper.getSerializationConfig().getDefaultCodec();
 JsonFactory jf = oc.getJsonFactory();
 ObjectReader or = mapper.readerFor(TestBean.class);
 AnnotatedField testFieldAnnotation = Annotations.findAnnotation(TestBean.class.getDeclaredField("field"), Annotation.class);
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(testFieldAnnotation);
 FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new String[]{"field"}, TestBean.class, null), new SimpleType(Integer.class), null, Annotations.empty(), annotatedField);
 }

 @org.junit.Test
 public void testDeserializeAndSet() throws Exception {
 TestBean testBean = new TestBean();
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getCurrentName()).thenReturn("field");
 when(mockParser.getValueAsInt()).thenReturn(42);
 JsonParser parser = mockParser;
 Object instance = testBean;
 DeserializationContext context = mock(DeserializationContext.class);
 Object value = 42;
 doAnswer(invocationOnMock -> {
 try {
 fieldProperty.deserializeAndSet(parser, context, instance);
 } catch (IOException e) {
 e.printStackTrace();
 }
 return null;
 }).when(fieldProperty).deserializeAndSet(parser, context, instance);
 fieldProperty.deserializeAndSet(parser, context, instance);
 assertEquals(42, testBean.field);
 }

 @org.junit.Test
 public void testDeserializeSetAndReturn() throws Exception {
 TestBean testBean = new TestBean();
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getCurrentName()).thenReturn("field");
 when(mockParser.getValueAsInt()).thenReturn(42);
 JsonParser parser = mockParser;
 Object instance = testBean;
 DeserializationContext context = mock(DeserializationContext.class);
 Object value = 42;
 doAnswer(invocationOnMock -> {
 try {
 fieldProperty.deserializeSetAndReturn(parser, context, instance);
 } catch (IOException e) {
 e.printStackTrace();
 }
 return instance;
 }).when(fieldProperty).deserializeSetAndReturn(parser, context, instance);
 Object result = fieldProperty.deserializeSetAndReturn(parser, context, instance);
 assertEquals(testBean, result);
 }

 @org.junit.Test
 public void testSet() throws Exception {
 TestBean testBean = new TestBean();
 Object value = 42;
 doAnswer(invocationOnMock -> {
 try {
 fieldProperty.set(testBean, value);
 } catch (Exception e) {
 e.printStackTrace();
 }
 return null;
 }).when(fieldProperty).set(testBean, value);
 fieldProperty.set(testBean, value);
 assertEquals(42, testBean.field);
 }

 @org.junit.Test
 public void testSetAndReturn() throws Exception {
 TestBean testBean = new TestBean();
 Object value = 42;
 doAnswer(invocationOnMock -> {
 try {
 fieldProperty.setAndReturn(testBean, value);
 } catch (Exception e) {
 e.printStackTrace();
 }
 return testBean;
 }).when(fieldProperty).setAndReturn(testBean, value);
 Object result = fieldProperty.setAndReturn(testBean, value);
 assertEquals(testBean, result);
 }

 @org.junit.Test
 public void testSkipNullValue() throws Exception {
 TestBean testBean = new TestBean();JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getCurrentName()).thenReturn("field");
 when(mockParser.getValueAsInt()).thenReturn(null);
 JsonParser parser = mockParser;
 Object instance = testBean;
 DeserializationContext context = mock(DeserializationContext.class);
 doAnswer(invocationOnMock -> {
 if (fieldProperty._skipNulls) {
 return null;
 } else {
 throw new IOException("Should not be reached");
 }
 }).when(fieldProperty).deserializeAndSet(parser, context, instance);
 fieldProperty.deserializeAndSet(parser, context, instance);
 assertNull(testBean.field);
 }
}