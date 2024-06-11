package com.example.test.jackson;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SetterlessPropertyTest {

 private JsonDeserializer<Object> valueDeserializerMock;
 private TypeDeserializer typeDeserializerMock;
 private SetterlessProperty setterlessProperty;
 private AnnotatedMethod annotatedMethodMock;

 @Before
 public void setup() {
 valueDeserializerMock = Mockito.mock(JsonDeserializer.class);
 typeDeserializerMock = Mockito.mock(TypeDeserializer.class);
 annotatedMethodMock = Mockito.mock(AnnotatedMethod.class);
 setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class),
 Mockito.mock(JavaType.class),
 typeDeserializerMock,
 Annotations.EMPTY_ANNOTATIONS,
 annotatedMethodMock);
 }

 @Test
 public void testConstructor() {
 assertNotNull(setterlessProperty);
 }

 @Test
 public void testWithName() {
 SettableBeanProperty result = setterlessProperty.withName(Mockito.mock(PropertyName.class));
 assertEquals(setterlessProperty.getClass(), result.getClass());
 }

 @Test
 public void testWithValueDeserializer() {
 SettableBeanProperty result = setterlessProperty.withValueDeserializer(valueDeserializerMock);
 assertEquals(valueDeserializerMock, result._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 SettableBeanProperty result = setterlessProperty.withNullProvider(Mockito.mock(NullValueProvider.class));
 assertEquals(Mockito.mock(NullValueProvider.class), result._nullProvider);
 }

 @Test
 public void testFixAccess() {
 setterlessProperty.fixAccess(Mockito.mock(DeserializationConfig.class));
 setterlessProperty.getAnnotation(Mockito.mock(Annotation.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 setterlessProperty.set(Mockito.mock(Object.class), Mockito.mock(Object.class));
 fail("Should not call `set()` on setterless property");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException {
 setterlessProperty.setAndReturn(Mockito.mock(Object.class), Mockito.mock(Object.class));
 fail("Should not call `set()` on setterless property");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = Mockito.mock(Object.class);
 JsonParser jsonParserMock = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContextMock = Mockito.mock(DeserializationContext.class);

 Mockito.when(annotatedMethodMock.getAnnotated()).thenReturn(instance);
 Mockito.when(annotatedMethodMock.getMethod()).thenReturn(Mockito.mock(Method.class));

 when(valueDeserializerMock.deserialize(jsonParserMock, deserializationContextMock, anyObject()))
 .thenReturn(Mockito.mock(Object.class));

 setterlessProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance);

 verify(instance).invoke(Mockito.eq(setterlessProperty._getter), (Object[]) eq(null));
 verify(instance).getClass();
 verify(valueDeserializerMock).deserialize(jsonParserMock, deserializationContextMock, anyObject());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = Mockito.mock(Object.class);
 JsonParser jsonParserMock = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContextMock = Mockito.mock(DeserializationContext.class);

 when(valueDeserializerMock.deserializeWithType(jsonParserMock, deserializationContextMock, anyObject()))
 .thenReturn(Mockito.mock(Object.class));

 SettableBeanProperty settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 when(setterlessProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance))
 .thenReturn(settableBeanProperty);

 assertEquals(instance, settableBeanProperty.getInstance());
 }
}