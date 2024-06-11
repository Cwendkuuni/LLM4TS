package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser.NumberType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class MethodPropertyTest {

 private MethodProperty property;
 private AnnotatedMethod annotated;
 private JsonParser parser;
 private DeserializationContext context;
 private Object instance;
 private JsonDeserializer<Object> deserializer;
 private TypeDeserializer typeDeserializer;
 private NullValueProvider nullValueProvider;

 @Before
 public void setup() throws Exception {
 annotated = mock(AnnotatedMethod.class);
 parser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 instance = mock(Object.class);
 deserializer = mock(JsonDeserializer.class);
 typeDeserializer = mock(TypeDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);

 property = new MethodProperty(
 new BeanPropertyDefinition(new PropertyMetadata(), null, null),
 mock(JavaType.class), typeDeserializer, Annotations.empty(), annotated);
 }

 @Test
 public void testConstructorWithName() {
 property = property.withName(new PropertyName("testName"));
 assertSame(new PropertyName("testName"), property.getName());
 }

 @Test
 public void testConstructorWithValueDeserializer() throws Exception {
 JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
 property = property.withValueDeserializer(valueDeserializer);
 assertSame(valueDeserializer, property.getValueDeserializer());
 }

 @Test
 public void testConstructorWithNullProvider() throws Exception {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 property = property.withNullProvider(nullValueProvider);
 assertSame(nullValueProvider, property.getNullProvider());
 }

 @Test
 public void testFixAccess() throws Exception {
 property.fixAccess(context);
 assertSame(Annotations.findAnnotation(annotated, JsonProperty.class), annotated.getAnnotations());
 }

 @Test
 public void testGetAnnotation() {
 JsonProperty jsonProperty = new JsonProperty("test");
 when(annotated.getAnnotations()).thenReturn(new Annotation[0]);
 when(annotated.getAnnotation(JsonProperty.class)).thenReturn(jsonProperty);
 assertSame(jsonProperty, property.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testGetMember() {
 assertSame(annotated, property.getMember());
 }

 @Test(expected = IllegalAccessException.class)
 public void testDeserializeAndSetNullValue() throws Exception {
 property.deserializeAndSet(parser, context, instance);
 assertNull(instance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeAndSetNullValueDeserializer() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.getValueAsString()).thenReturn("null");
 property.deserializeAndSet(parser, context, instance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeAndSetValueDeserializerThrowsIOException() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("test");
 when(valueDeserializer.deserialize(parser, context)).thenThrow(IOException.class);
 property = new MethodProperty(property, deserializer, nullValueProvider);
 property.deserializeAndSet(parser, context, instance);
 }

 @Test
 public void testDeserializeAndSetValueDeserializerWithType() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("test");
 when(valueDeserializer.deserializeWithType(parser, context, typeDeserializer)).thenReturn(new Integer(1));
 property = new MethodProperty(property, deserializer, typeDeserializer, nullValueProvider);
 property.deserializeAndSet(parser, context, instance);
 assertSame(new Integer(1), instance);
 }

 @Test
 public void testDeserializeSetAndReturnNullValue() throws Exception {
 assertSame(instance, property.deserializeSetAndReturn(parser, context, instance));
 }

 @Test
 public void testDeserializeSetAndReturnValueDeserializerThrowsIOException() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("test");
 when(valueDeserializer.deserialize(parser, context)).thenThrow(IOException.class);
 assertNull(property.deserializeSetAndReturn(parser, context, instance));
 }

 @Test
 public void testDeserializeSetAndReturnValueDeserializerWithType() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("test");
 when(valueDeserializer.deserializeWithType(parser, context, typeDeserializer)).thenReturn(new Integer(1));
 assertSame(new Integer(1), property.deserializeSetAndReturn(parser, context, instance));
 }

 @Test
 public void testSet() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("test");
 property.set(instance, parser.getValueAsString());
 assertSame("test", instance.getClass().getDeclaredField("test").get(instance));
 }

 @Test
 public void testSetValueDeserializerThrowsIOException() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("test");
 when(valueDeserializer.deserialize(parser, context)).thenThrow(IOException.class);
 property = new MethodProperty(property, deserializer, nullValueProvider);
 property.set(instance, parser.getValueAsString());
 assertSame(instance, instance);
 }

 @Test
 public void testSetValueDeserializerWithType() throws Exception {
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("1");
 when(valueDeserializer.deserializeWithType(parser, context, typeDeserializer)).thenReturn(new Integer(1));
 property = new MethodProperty(property, deserializer, typeDeserializer, nullValueProvider);
 property.set(instance, parser.getValueAsString());
 assertSame(new Integer(1), instance);
 }

 @Test
 public void testReadResolve() throws Exception {
 property = property.withName(new PropertyName("test"));
 property = property.withValueDeserializer(deserializer);
 property = property.withNullProvider(nullValueProvider);
 Object resolved = property.readResolve();
 assertSame(property, resolved);
 }
}