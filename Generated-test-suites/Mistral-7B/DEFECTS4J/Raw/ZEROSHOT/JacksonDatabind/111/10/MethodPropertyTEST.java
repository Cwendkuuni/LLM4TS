package com.yourpackage.junit;

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
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class MethodPropertyTest {

 private static class DummyClass {
 private String _name;

 @JsonProperty("name")
 public void setName(String value) {
 _name = value;
 }
 }

 @org.junit.Test
 public void testMethodProperty() throws IOException {
 // Given
 DummyClass instance = new DummyClass();
 ObjectIdReader idReader = mock(ObjectIdReader.class);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", String.class);
 TypeDeserializer typeDeser = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getAnnotated()).thenReturn(idReader);
 when(method.getName()).thenReturn("setName");
 when(method.getParameterTypes()).thenReturn(new Class[] { String.class });
 when(method.getDeclaringClass()).thenReturn(DummyClass.class);
 MethodProperty property = new MethodProperty(propDef, factory.constructType(DummyClass.class), typeDeser, contextAnnotations, method);

 // When
 JsonParser parser = factory.createParser("{\"name\": \"John Doe\"}");
 property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);

 // Then
 assertEquals("John Doe", instance._name);

 parser = factory.createParser("{\"name\": null}");
 property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
 assertNull(instance._name);

 parser = factory.createParser("{}"); // empty JSON
 assertNull(instance._name);
 }

 @org.junit.Test
 public void testSetAndReturn() throws IOException {
 // Given
 DummyClass instance = new DummyClass();
 ObjectIdReader idReader = mock(ObjectIdReader.class);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper();
 ObjectIdReader nullValueProvider = mock(ObjectIdReader.class);
 when(nullValueProvider.getIdFromValue(String.class, "null")).thenReturn(null);
 NullValueProvider nva = new NullValueProviderDefault(nullValueProvider);
 Annotations contextAnnotations = mock(Annotations.class);
 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getAnnotated()).thenReturn(idReader);
 when(method.getName()).thenReturn("setName");
 when(method.getParameterTypes()).thenReturn(new Class[] { String.class });
 when(method.getDeclaringClass()).thenReturn(DummyClass.class);
 MethodProperty property = new MethodProperty(new BeanPropertyDefinition("name", String.class),
 factory.constructType(DummyClass.class), null, contextAnnotations, method);
 property = new MethodProperty(property, mapper.getValueDeserializer(String.class), nva);

 // When
 String value = "John Doe";
 Object result = property.setAndReturn(instance, value);

 // Then
 assertEquals(value, result);
 assertEquals(value, instance._name);
 }
}

class NullValueProviderDefault extends NullValueProvider {
 private final NullValueProvider _provider;

 public NullValueProviderDefault(NullValueProvider provider) {
 super();
 _provider = provider;
 }

 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return _provider.getNullValue(ctxt);
 }
}