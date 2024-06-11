package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.test.BaseTest;

public class MethodPropertyTest extends BaseTest {

 private static final Class<?> CLASS_UNDER_TEST = MethodProperty.class;
 private static final MethodProperty property;

 static {
 try {
 property = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void beforeClass() {
 property._valueDeserializer = mock(JsonDeserializer.class);
 property._nullProvider = mock(NullValueProvider.class);
 property._valueTypeDeserializer = mock(TypeDeserializer.class);
 property._annotated = mock(AnnotatedMethod.class);
 when(property._annotated.getAnnotated()).thenReturn(new Object());
 }

 @After
 public void after() throws Exception {
 BeanUtil.cleanSlate(property);
 }

 @Test
 public void testConstructorNull() {
 try {
 new MethodProperty(null, null, null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConstructor() {
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType type = mock(JavaType.class);
 TypeDeserializer typeDeser = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 AnnotatedMethod method = mock(AnnotatedMethod.class);
 MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
 assertEquals(propDef, property.getPropertyDefinition());
 assertEquals(type, property.getType());
 assertEquals(typeDeser, property.getTypeDeserializer());
 assertEquals(contextAnnotations, property.getContextAnnotations());
 assertEquals(method, property.getAnnotated());
 }

 @Test
 public void testWithName() {
 PropertyName newName = mock(PropertyName.class);
 MethodProperty property = new MethodProperty(property, newName);
 assertEquals(newName, property.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> deser = mock(JsonDeserializer.class);
 MethodProperty property = new MethodProperty(property, deser, _nullProvider);
 assertEquals(deser, property.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = mock(NullValueProvider.class);
 MethodProperty property = new MethodProperty(property, _valueDeserializer, nva);
 assertEquals(nva, property.getNullProvider());
 }

 @Test
 public void testFixAccess() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 property.fixAccess(config);
 verify(property._annotated).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = mock(Annotation.class);
 when(property._annotated.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertEquals(annotation, property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(property._annotated, property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
when(p.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
when(p.getValueAsString()).thenReturn("test");
when(property._valueDeserializer.deserialize(p, ctxt)).thenReturn("testValue");
Object value = property.deserializeAndSet(p, ctxt, instance);
assertEquals("testValue", value);
assertEquals(instance, property.getMember().get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = new Object();
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
when(p.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
when(p.getValueAsString()).thenReturn("test");
when(property._valueDeserializer.deserialize(p, ctxt)).thenReturn("testValue");
Object result = property.deserializeSetAndReturn(p, ctxt, instance);
assertEquals(instance, result);
 assertEquals("testValue", property.getMember().get(instance));
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 property.set(instance, value);
 assertEquals(value, property.getMember().get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 Object result = property.setAndReturn(instance, value);
 assertEquals(instance, result);
 assertEquals(value, property.getMember().get(instance));
 }

 @Test
 public void testReadResolve() throws Exception {
 MethodProperty property = (MethodProperty) property.readResolve();
 assertEquals(property._annotated, property.getAnnotated());
 }
}