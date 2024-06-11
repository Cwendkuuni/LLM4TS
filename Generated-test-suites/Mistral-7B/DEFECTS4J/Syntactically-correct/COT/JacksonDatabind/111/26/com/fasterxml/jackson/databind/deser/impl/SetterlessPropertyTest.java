package com.example.junit.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class SetterlessPropertyTest {

 private static final Object INSTANCE = mock(Object.class);
 private static final Object VALUE = mock(Object.class);
 private static final Method GETTER = mock(Method.class);
 private static final TypeDeserializer TYPE_DESERIALIZER = mock(TypeDeserializer.class);
 private static final JsonDeserializer<?> VALUE_DESERIALIZER = mock(JsonDeserializer.class);
 private static final NullValueProvider NULL_VALUE_PROVIDER = mock(NullValueProvider.class);
 private static final BeanPropertyDefinition PROP_DEF = mock(BeanPropertyDefinition.class);
 private static final Annotations CONTEXT_ANNOTATIONS = mock(Annotations.class);
 private static final AnnotatedMethod ANNOTATED_METHOD = mock(AnnotatedMethod.class);
 private static final PropertyName NEW_NAME = mock(PropertyName.class);

 @Test
 public void testConstructor() throws Exception {
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 assertNotNull(property);
 }

 @Test
 public void testWithName() throws Exception {
 SetterlessProperty property = new SetterlessProperty(property, NEW_NAME);
 assertInstanceOf(SetterlessProperty.class, property);
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 SetterlessProperty property = new SetterlessProperty(property, VALUE_DESERIALIZER, NULL_VALUE_PROVIDER);
 assertInstanceOf(SetterlessProperty.class, property);
 }

 @Test
 public void testWithNullProvider() throws Exception {
 SetterlessProperty property = new SetterlessProperty(property, NULL_VALUE_PROVIDER);
 assertInstanceOf(SetterlessProperty.class, property);
 }

 @Test
 public void testFixAccess() throws Exception {
 SetterlessProperty property = new SetterlessProperty(property, null);
 property.fixAccess(null);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 when(ANNOTATED_METHOD.getAnnotation(Mockito.any(Annotation.class))).thenReturn(mock(Annotation.class));
 Annotation annotation = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD).getAnnotation(Mockito.any(Annotation.class));
 assertNotNull(annotation);
 }

 @Test
 public void testGetMember() throws Exception {
 when(ANNOTATED_METHOD.getMember()).thenReturn(INSTANCE);
 AnnotatedMember member = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD).getMember();
 assertInstanceOf(AnnotatedMember.class, member);
 assertSame(INSTANCE, member.getMember());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.set(INSTANCE, VALUE);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws Exception {
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.setAndReturn(INSTANCE, VALUE);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("testValue");
 when(GETTER.invoke(INSTANCE, (Object[]) null)).thenReturn(VALUE);
 when(_getter.invoke(INSTANCE, (Object[]) null)).thenReturn(VALUE);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetNullValue() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetTypeMismatch() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, String.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("testValue");
 when(_getter.invoke(INSTANCE, (Object[]) null)).thenReturn(VALUE);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 Object result = property.deserializeSetAndReturn(parser, null, INSTANCE);
 assertSame(INSTANCE, result);
 }

 @Test
 public void testValueDeserializerDeserialize() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("testValue");
 when(_valueDeserializer.deserialize(parser, null, VALUE)).thenReturn(VALUE);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test(expected = IOException.class)
 public void testValueDeserializerDeserializeNullValue() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test(expected = IOException.class)
 public void testValueDeserializerDeserializeTypeMismatch() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, String.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test(expected = InvocationTargetException.class)
 public void testGetterException() throws Exception {
 when(_getter.invoke(INSTANCE, (Object[]) null)).thenThrow(new InvocationTargetException(new RuntimeException()));
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test
 public void testDeserializeAndSetTypeDeserializerNotNull() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("testValue");
 when(_valueTypeDeserializer != null).thenReturn(TYPE_DESERIALIZER);
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, TYPE_DESERIALIZER, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetTypeDeserializerNull() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("testValue");
 SetterlessProperty property = new SetterlessProperty(PROP_DEF, Object.class, null, CONTEXT_ANNOTATIONS, ANNOTATED_METHOD);
 property.deserializeAndSet(parser, null, INSTANCE);
 }
}