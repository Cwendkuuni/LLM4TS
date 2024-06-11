package com.yourpackage.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.NullsConstantProvider;

import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {

 private static final String TEST_CLASS_NAME = "com.fasterxml.jackson.databind.deser.impl.MethodProperty";
 private static final String TEST_SETTER_METHOD_NAME = "setTestProperty";
 private static final String TEST_GETTER_METHOD_NAME = "getTestProperty";
 private static final String TEST_PROPERTY_NAME = "testProperty";

 private BeanPropertyDefinition _beanPropertyDefinition;
 private JavaType _type;
 private TypeDeserializer _typeDeserializer;
 private Annotations _contextAnnotations;
 private AnnotatedMethod _annotatedMethod;
 private Method _setterMethod;
 private MethodProperty _methodProperty;
 private Object _instance;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 _beanPropertyDefinition = mock(BeanPropertyDefinition.class);
 _type = mock(JavaType.class);
 _typeDeserializer = mock(TypeDeserializer.class);
 _contextAnnotations = mock(Annotations.class);
 _annotatedMethod = mock(AnnotatedMethod.class);
 _setterMethod = mockMethod(TEST_CLASS_NAME, TEST_SETTER_METHOD_NAME);
 _methodProperty = new MethodProperty(_beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod);
 _instance = mock(Object.class);
 }

 @Test
 public void testConstructor() {
 MethodProperty methodProperty = new MethodProperty(_beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod);
 assertSame(methodProperty, new MethodProperty(methodProperty, _beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod));
 }

 @Test
 public void testConstructorWithDeserializer() {
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 MethodProperty methodProperty = new MethodProperty(_beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod, deserializer);
 assertSame(methodProperty, new MethodProperty(methodProperty, _beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod, deserializer));
 }

 @Test
 public void testConstructorWithName() {
 PropertyName newName = mock(PropertyName.class);
 MethodProperty methodProperty = new MethodProperty(_beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod, newName);
 assertSame(methodProperty, new MethodProperty(methodProperty, _beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, _annotatedMethod, newName));
 }

 @Test
 public void testConstructorWithMethod() {
 Method method = mock(Method.class);
 MethodProperty methodProperty = new MethodProperty(_beanPropertyDefinition, _type, _typeDeserializer, _contextAnnotations, method);
 assertSame(methodProperty, new MethodProperty(methodProperty, _beanPropertyDefinition, _type, _typeDeserializer, method));
 }

 @Test
 public void testGetAnnotation() {
 Object annotation = mock(Annotation.class);
 when(_annotatedMethod.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertSame(annotation, _methodProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertSame(_annotatedMethod, _methodProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object value = new Object();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("test");
 when(_valueDeserializer.deserialize(jsonParser, _deserializationContext)).thenReturn("testValue");
 when(_setterMethod.invoke(_instance, (Object) anyObject())).thenReturn(null);
 _methodProperty.deserializeAndSet(jsonParser, _deserializationContext, _instance);
 assertNull(_methodProperty.getValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object value = new Object();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("test");
 when(_valueDeserializer.deserialize(jsonParser, _deserializationContext)).thenReturn("testValue");
 when(_setterMethod.invoke(_instance, (Object) anyObject())).thenReturn("testResult");
 Object result = _methodProperty.deserializeSetAndReturn(jsonParser, _deserializationContext, _instance);
 assertSame("testResult", result);
 }

 @Test
 public void testSet() throws IOException {
 Object value = new Object();
 when(_setterMethod.invoke(_instance, (Object) anyObject())).thenReturn(null);
 _methodProperty.set(_instance, value);
 assertNull(_methodProperty.getValue());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object value = new Object();
 when(_setterMethod.invoke(_instance, (Object) anyObject())).thenReturn("testResult");
 Object result = _methodProperty.setAndReturn(_instance, value);
 assertSame("testResult", result);
 }

 @Test
 public void testReadResolve() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 Object resolvedObject = new Object();
 when(_annotatedMethod.getAnnotated()).thenReturn(resolvedObject);
 Object result = _methodProperty.readResolve();
 assertSame(new MethodProperty(_methodProperty, resolvedObject), result);
 }

 private Method mockMethod(String className, String methodName) throws NoSuchMethodException {
 Method method = ReflectionTestUtils.findMethod(Class.forName(className), methodName, Object.class, Object.class);
 return method;
 }

}