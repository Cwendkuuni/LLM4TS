package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class FieldPropertyTest {

 private static final FieldProperty PROPERTY = new FieldProperty(mock(BeanPropertyDefinition.class), mock(JavaType.class), mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedField.class)) {
 {
 _skipNulls = true;
 };
 };

 @org.junit.Before
 public void setup() {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentName()).thenReturn("test");
 when(parser.getCurrentName()).thenReturn(null);

 DeserializationContext context = mock(DeserializationContext.class);
 when(context.constructValue(PROPERTY.getType(), null)).thenReturn(new Object());
 when(context.getRootValueInstance()).thenReturn(new Object());
 when(context.findBeanDescription(PROPERTY.getName(), PropertyName.class)).thenReturn(new BeanDescription(null));

 JsonNode node = mock(JsonNode.class);
 when(node.asText()).thenReturn("value");
 when(parser.getValueAsToken()).thenReturn(node);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

 PROPERTY.fixAccess(context);
 }

 @org.junit.Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 PROPERTY.deserializeAndSet(parser, context, instance);
 assertSame("value", PROPERTY.getField().get(instance));
 }

 @org.junit.Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = new Object();
 Object result = PROPERTY.deserializeSetAndReturn(parser, context, instance);
 assertSame("value", PROPERTY.getField().get(instance));
 assertSame(instance, result);
 }

 @org.junit.Test
 public void testSet() throws IOException {
 Object instance = new Object();
 PROPERTY.set(instance, "value");
 assertSame("value", PROPERTY.getField().get(instance));
 }

 @org.junit.Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object result = PROPERTY.setAndReturn(instance, "value");
 assertSame("value", PROPERTY.getField().get(instance));
 assertSame(instance, result);
 }

 @org.junit.Test
 public void testDeserializeWithNullValue() throws IOException {
 Object instance = new Object();
 PROPERTY.deserializeAndSet(parser, context, instance);
 assertNull(PROPERTY.getField().get(instance));
 }

 @org.junit.Test
 public void testDeserializeWithNullTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = null;
 Object instance = new Object();
 PropertyProperty property = new PropertyProperty(mock(BeanPropertyDefinition.class), mock(JavaType.class), typeDeserializer, mock(Annotations.class), mock(AnnotatedField.class)) {
 {
 _skipNulls = true;
 };
 };
 property.deserializeAndSet(parser, context, instance);
 assertNull(property.getField().get(instance));
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidToken() throws IOException {
 Object instance = new Object();
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 PROPERTY.deserializeAndSet(parser, context, instance);
 }

 @org.junit.Test
 public void testFixAccess() throws IOException {
 Class<?> clazz = Object.class;
 Field field = new Field() {
 private static final long serialVersionUID = 1L;
 String name = "test";
 };
 when(PROPERTY.getName()).thenReturn(new PropertyName(field.getName()));
 when(ClassUtil.getRawClass(PROPERTY.getType())).thenReturn(clazz);
 when(PROPERTY.getAnnotated().getAnnotated()).thenReturn(field);
 PROPERTY.fixAccess(context);
 }
}