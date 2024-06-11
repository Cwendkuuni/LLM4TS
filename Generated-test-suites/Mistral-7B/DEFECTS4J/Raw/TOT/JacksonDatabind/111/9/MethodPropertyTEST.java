package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodPropertyTest {

 private MethodProperty property;
 private ObjectMapper mapper;
 private DeserializationContext context;
 private JsonDeserializer<String> stringDeserializer;
 private SerializerProvider serializerProvider;
 private TypeDeserializer typeDeserializer;
 private TypeSerializer typeSerializer;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 context = mock(DeserializationContext.class);
 stringDeserializer = mock(JsonDeserializer.class);
 serializerProvider = mock(SerializerProvider.class);
 typeDeserializer = mock(TypeDeserializer.class);
 typeSerializer = mock(TypeSerializer.class);
 property = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedMember(new JsonProperty(), "test"), null),
 mapper.getTypeFactory().constructType(String.class),
 typeDeserializer,
 Annotations.empty(),
 new AnnotatedMethod(new MethodTest().getClass().getMethod("setTest", String.class))
 );
 when(context.findDeserializerForProperty(property)).thenReturn(stringDeserializer);
 when(context.getSerializerProvider()).thenReturn(serializerProvider);
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object instance = new Object();
 String value = "test";
 property.deserializeAndSet(mapper.getFactory().createParser("{\"test\":\"" + value + "\"}"), context, instance);
 assertEquals(value, property.get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException {
 Object instance = new Object();
 String value = "test";
 assertEquals(instance, property.deserializeSetAndReturn(mapper.getFactory().createParser("{\"test\":\"" + value + "\"}"), context, instance));
 assertEquals(value, property.get(instance));
 }

 @Test
 public void testDeserializeNull() throws JsonProcessingException {
 property.deserializeAndSet(mapper.getFactory().createParser("null"), context, new Object());
 assertNull(property.get(new Object()));
 }

 @Test
 public void testInvokeSetterWithNull() throws Exception {
 Object instance = new Object();
 when(stringDeserializer.deserialize(mapper.getFactory().createParser("null"), context)).thenReturn(null);
 property.deserializeAndSet(mapper.getFactory().createParser("null"), context, instance);
 assertNull(property.get(instance));
 }

 @Test
 public void testInvokeSetterWithValue() throws Exception {
 Object instance = new Object();
 String value = "test";
 when(stringDeserializer.deserialize(mapper.getFactory().createParser("\"test\""), context)).thenReturn(value);
 property.deserializeAndSet(mapper.getFactory().createParser("{\"test\":\"test\"}"), context, instance);
 assertEquals(value, property.get(instance));
 }

 @Test
 public void testInvokeSetterWithValueDeserializer() throws Exception {
 Object instance = new Object();
 JsonDeserializer<Integer> intDeserializer = mock(JsonDeserializer.class);
 property = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedMember(new JsonProperty(), "test"), null),
 mapper.getTypeFactory().constructType(Integer.class),
 intDeserializer,
 Annotations.empty(),
 new AnnotatedMethod(new MethodTest().getClass().getMethod("setTest", Integer.class))
 );
 when(intDeserializer.deserialize(mapper.getFactory().createParser("123"), context)).thenReturn(123);
 property.deserializeAndSet(mapper.getFactory().createParser("{\"test\":123}"), context, instance);
 assertEquals(123, property.get(instance));
 }

 @Test
 public void testInvokeSetterWithValueDeserializerAndTypeDeserializer() throws Exception {
 Object instance = new Object();
 JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
 TypeDeserializer intTypeDeserializer = mock(TypeDeserializer.class);
 property = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedMember(new JsonProperty(), "test"), null),
 mapper.getTypeFactory().constructType(Integer.class),
 intTypeDeserializer,
 Annotations.empty(),
 new AnnotatedMethod(new MethodTest().getClass().getMethod("setTest", Integer.class))
 );
 when(stringDeserializer.deserialize(mapper.getFactory().createParser("\"123\""), context)).thenReturn("123");
 when(intTypeDeserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"test\":123}"), context, mapper.getTypeFactory().constructType(Integer.class)))
 .thenReturn(123);
 property.deserializeAndSet(mapper.getFactory().createParser("{\"test\":\"123\"}"), context, instance);
 assertEquals(123, property.get(instance));
 }

 @Test
 public void testInvokeSetterWithTypeSerializer() throws Exception {
 Object instance = new Object();
 TypeSerializer intTypeSerializer = mock(TypeSerializer.class);
 property = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedMember(new JsonProperty(), "test"), null),
 mapper.getTypeFactory().constructType(Integer.class),
 null,
 Annotations.empty(),
 new AnnotatedMethod(new MethodTest().getClass().getMethod("setTest", Integer.class))
 );
 property.fixAccess(mapper.getConfig());
 when(intTypeSerializer.serializeValue(123, serializerProvider, null)).thenReturn("123");
 when(serializerProvider.getSerializerForClass(Integer.class)).thenReturn(intTypeSerializer);
 property.deserializeAndSet(mapper.getFactory().createParser("{\"test\":123}"), context, instance);
 assertEquals(123, property.get(instance));
 }

 @Test
 public void testInvokeSetterWithTypeSerializerAndTypeDeserializer() throws Exception {
 Object instance = new Object();
 TypeSerializer intTypeSerializer = mock(TypeSerializer.class);
 TypeDeserializer intTypeDeserializer = mock(TypeDeserializer.class);
 property = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedMember(new JsonProperty(), "test"), null),
 mapper.getTypeFactory().constructType(Integer.class),
 intTypeDeserializer,
 Annotations.empty(),
 new AnnotatedMethod(new MethodTest().getClass().getMethod("setTest", Integer.class))
 );
 property.fixAccess(mapper.getConfig());
 when(intTypeSerializer.serializeValue(123, serializerProvider, null)).thenReturn("123");
 when(serializerProvider.getSerializerForClass(Integer.class)).thenReturn(intTypeSerializer);
 when(intTypeDeserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"test\":123}"), context, mapper.getTypeFactory().constructType(Integer.class)))
 .thenReturn(123);
 property.deserializeAndSet(mapper.getFactory().createParser("{\"test\":\"123\"}"), context, instance);
 assertEquals(123, property.get(instance));
 }
}