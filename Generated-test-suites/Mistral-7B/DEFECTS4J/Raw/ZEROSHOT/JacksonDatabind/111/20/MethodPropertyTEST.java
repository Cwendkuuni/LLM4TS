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
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NullValueProvider;

public class MethodPropertyTest {

 private MethodProperty methodProperty;
 private Object instance;
 private Method method;
 private AnnotatedMethod annotatedMethod;
 private JsonParser jsonParser;
 private DeserializationContext context;
 private TypeDeserializer typeDeserializer;
 private NullValueProvider nullValueProvider;
 private JsonDeserializer<?> valueDeserializer;
 private Object nullValue;

 @Before
 public void setUp() throws Exception {
 Class<?> clazz = MethodProperty.class;
 method = clazz.getDeclaredMethod("setAndReturn", Object.class);
 annotatedMethod = mock(AnnotatedMethod.class);
 when(annotatedMethod.getAnnotated()).thenReturn(method);
 instance = mock(Object.class);
 typeDeserializer = mock(TypeDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 context = mock(DeserializationContext.class);
 jsonParser = mock(JsonParser.class);
 valueDeserializer = mock(JsonDeserializer.class);
 methodProperty = new MethodProperty(null, null, typeDeserializer, null, annotatedMethod);
 }

 @Test
 public void testConstructorWithAnnotatedMethod() {
 MethodProperty actual = new MethodProperty(null, null, typeDeserializer, null, annotatedMethod);
 MethodProperty expected = methodProperty;
 assertSame(expected, actual);
 }

 @Test
 public void testConstructorWithSameMethod() {
 MethodProperty actual = new MethodProperty(methodProperty, method);
 MethodProperty expected = methodProperty;
 assertSame(expected, actual);
 }

 @Test
 public void testConstructorWithNewName() {
 PropertyName newName = mock(PropertyName.class);
 MethodProperty actual = new MethodProperty(methodProperty, newName);
 MethodProperty expected = methodProperty;
 assertSame(expected, actual);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<Annotation> annotationType = mock(Annotation.class);
 when(annotatedMethod.getAnnotation(annotationType)).thenReturn(mock(Annotation.class));
 Annotation actual = methodProperty.getAnnotation(annotationType);
 Annotation expected = mock(Annotation.class);
 assertSame(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetterNull() {
 methodProperty._setter = null;
 methodProperty.set(instance, "value");
 }

 @Test
 public void testSetter() throws Exception {
 Object value = new Object();
 methodProperty.set(instance, value);
 verify(instance).invoke(method, value);
 }

 @Test
 public void testSetterAndReturnNull() throws Exception {
 when(method.invoke(instance, anyObject())).thenReturn(null);
 assertNull(methodProperty.setAndReturn(instance, "value"));
 }

 @Test
 public void testSetterAndReturnInstance() throws Exception {
 Object result = new Object();
 when(method.invoke(instance, anyObject())).thenReturn(result);
 assertSame(result, methodProperty.setAndReturn(instance, "value"));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 Object value = new Object();
 when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(value);
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 verify(instance).invoke(method, value);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(null);
 assertNull(methodProperty.deserializeSetAndReturn(jsonParser, context, instance));
 }

 @Test
 public void testDeserializeSetAndReturnInstance() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 Object result = new Object();
 when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(result);
 assertSame(result, methodProperty.deserializeSetAndReturn(jsonParser, context, instance));
 }

 @Test
 public void testFixAccess() throws Exception {
 methodProperty.fixAccess(context);
 verify(annotatedMethod).fixAccess(context.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testReadResolve() throws Exception {
 MethodProperty expected = new MethodProperty(methodProperty, method);
 MethodProperty actual = (MethodProperty) methodProperty.readResolve();
 assertSame(expected, actual);
 }
}