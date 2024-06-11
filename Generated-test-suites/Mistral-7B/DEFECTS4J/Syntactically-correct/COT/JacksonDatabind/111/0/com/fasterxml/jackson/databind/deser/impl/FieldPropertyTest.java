package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FieldPropertyTest {

 private FieldProperty fieldProperty;
 private AnnotatedField annotatedField;
 private JsonParser jsonParser;
 private DeserializationContext context;
 private Object instance;
 private Object value;
 private NullValueProvider nullValueProvider;
 private TypeDeserializer typeDeserializer;
 private BeanPropertyDefinition propDef;

 @Before
 public void setup() throws Exception {
 Annotation annotation = mock(Annotation.class);
 Field field = new Field("testField", Integer.class, null);
 annotatedField = new AnnotatedField(field, annotation);
 propDef = new BeanPropertyDefinition("testProperty", null, null, null, null);
 nullValueProvider = mock(NullValueProvider.class);
 typeDeserializer = mock(TypeDeserializer.class);
 context = mock(DeserializationContext.class);
 jsonParser = mock(JsonParser.class);
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 when(jsonParser.getValueAsInt()).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });
 when(context.getAnnotationInstaller()).thenReturn(Annotations::getInstance);
 when(context.findValueDeserializer(Integer.class)).thenReturn(new StdSerializer<Integer>(Integer.class) {
 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsInt();
 }
 });
 when(context.findValueDeserializer(null)).thenReturn(null);
 when(context.findValueDeserializer(Integer[].class)).thenReturn(null);
 fieldProperty = new FieldProperty(propDef, Object.class.getType(), typeDeserializer, Annotations.instance, annotatedField);
 instance = new Object();
 }

 @Test
 public void testConstructorWithBeanPropertyDefinition() {
 // Test the constructor with BeanPropertyDefinition
 FieldProperty test = new FieldProperty(propDef, Object.class.getType(), typeDeserializer, Annotations.instance, annotatedField);
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), typeDeserializer);
 assertSame(test.getAnnotations(), Annotations.instance);
 assertSame(test.getAnnotatedMember(), annotatedField);
 }

 @Test
 public void testConstructorWithCopy() throws IOException {
 // Test the copy constructor
 FieldProperty test = new FieldProperty(fieldProperty, null, nullValueProvider);
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), fieldProperty.getTypeDeserializer());
 assertSame(test.getAnnotations(), fieldProperty.getAnnotations());
 assertSame(test.getAnnotatedMember(), annotatedField);
 assertFalse(test._skipNulls);
 }

 @Test
 public void testConstructorWithName() throws IOException {
 // Test the constructor with name
 PropertyName newName = mock(PropertyName.class);
 FieldProperty test = new FieldProperty(fieldProperty, newName);
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), fieldProperty.getTypeDeserializer());
 assertSame(test.getAnnotations(), fieldProperty.getAnnotations());
 assertSame(test.getAnnotatedMember(), annotatedField);
 assertFalse(test._skipNulls);
 assertSame(test.getName(), newName);
 }

 @Test
 public void testConstructorUsedForJDKSerialization() throws Exception {
 // Test the JDK Serialization constructor
 FieldProperty test = new FieldProperty(fieldProperty);
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), fieldProperty.getTypeDeserializer());
 assertSame(test.getAnnotations(), fieldProperty.getAnnotations());
 assertSame(test.getAnnotatedMember(), annotatedField);
 assertFalse(test._skipNulls);
 }

 @Test
 public void testSettableBeanPropertyWithName() throws IOException {
 // Test the SettableBeanProperty withName method
 SettableBeanProperty test = fieldProperty.withName(new PropertyName("testProperty2"));
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), fieldProperty.getTypeDeserializer());
 assertSame(test.getAnnotations(), fieldProperty.getAnnotations());
 assertSame(test.getAnnotatedMember(), annotatedField);
 assertFalse(test._skipNulls);
 assertSame(test.getName(), new PropertyName("testProperty2"));
 }

 @Test
 public void testSettableBeanPropertyWithValueDeserializer() throws IOException {
 // Test the SettableBeanProperty withValueDeserializer method
 JsonDeserializer<Integer> jsonDeserializer = new StdSerializer<Integer>(Integer.class) {
 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsInt() * 2;
 }
 };
 SettableBeanProperty test = fieldProperty.withValueDeserializer(jsonDeserializer);
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), jsonDeserializer);
 assertSame(test.getAnnotations(), fieldProperty.getAnnotations());
 assertSame(test.getAnnotatedMember(), annotatedField);
 assertFalse(test._skipNulls);
 }

 @Test
 public void testSettableBeanPropertyWithNullProvider() throws IOException {
 // Test the SettableBeanProperty withNullProvider method
 NullValueProvider nullValueProvider2 = mock(NullValueProvider.class);
 SettableBeanProperty test = fieldProperty.withNullProvider(nullValueProvider2);
 assertSame(test.getPropertyDefinition(), propDef);
 assertSame(test.getType(), Object.class.getType());
 assertSame(test.getTypeDeserializer(), fieldProperty.getTypeDeserializer());
 assertSame(test.getAnnotations(), fieldProperty.getAnnotations());
 assertSame(test.getAnnotatedMember(), annotatedField);
 assertFalse(test._skipNulls);
 assertSame(test.getNullProvider(), nullValueProvider2);
 }

 @Test
 public void testFixAccess() throws Exception {
 // Test the fixAccess method
 fieldProperty.fixAccess(context);
 assertSame(field.getModifiers(), field.getAccessibleModifiers());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 // Test the getAnnotation method
 assertNotNull(fieldProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 // Test the getMember method
 assertSame(fieldProperty.getMember(), annotatedField);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 // Test the deserializeAndSet method with null value
 fieldProperty.deserializeAndSet(jsonParser, context, instance);
 assertNull(instance);
 when(context.findNullValueProvider()).thenReturn(nullValueProvider);
 fieldProperty.deserializeAndSet(jsonParser, context, instance);
 assertSame(instance, instance);

 // Test the deserializeAndSet method with a value
 when(jsonParser.getValueAsInt()).thenReturn(43);
 fieldProperty.deserializeAndSet(jsonParser, context, instance);
 assertSame(instance.getClass(), Object.class);
 try {
 Field f = field.get(instance);
 assertSame(f.get(instance), 43);
 } catch (IllegalAccessException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 // Test the deserializeSetAndReturn method with null value
 Object result = fieldProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertSame(result, instance);
 when(context.findNullValueProvider()).thenReturn(nullValueProvider);
 result = fieldProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertSame(result, instance);

 // Test the deserializeSetAndReturn method with a value
 when(jsonParser.getValueAsInt()).thenReturn(44);
 result = fieldProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertSame(instance.getClass(), Object.class);
 try {
 Field f = field.get(instance);
 assertSame(f.get(instance), 44);
 } catch (IllegalAccessException e) {
 // expected
 }
 assertSame(instance, result);
 }

 @Test
 public void testSet() throws Exception {
 // Test the set method with a null value
 fieldProperty.set(instance, null);
 assertNull(field.get(instance));
 when(context.findNullValueProvider()).thenReturn(nullValueProvider);
 fieldProperty.set(instance, null);
 assertNull(field.get(instance));

 // Test the set method with a value
 value = new Integer(45);
 fieldProperty.set(instance, value);
 try {
 Field f = field.get(instance);
 assertSame(f.get(instance), value);
 } catch (IllegalAccessException e) {
 // expected
 }
 }

 @Test
 public void testSetAndReturn() throws Exception {
 // Test the setAndReturn method with a null value
 Object result = fieldProperty.setAndReturn(instance, null);
 assertSame(result, instance);
 when(context.findNullValueProvider()).thenReturn(nullValueProvider);
 result = fieldProperty.setAndReturn(instance, null);
 assertSame(result, instance);

 // Test the setAndReturn method with a value
 value = new Integer(46);
 result = fieldProperty.setAndReturn(instance, value);
 assertSame(instance.getClass(), Object.class);
 try {
 Field f = field.get(instance);
 assertSame(f.get(instance), value);
 } catch (IllegalAccessException e) {
 // expected
 }
 assertSame(instance, result);
 }

 @Test(expected = IOException.class)
 public void testThrowAsIOE() throws Exception {
 // Test the throwAsIOE method
 IOException e = new IOException("Test IOException");
 fieldProperty.deserializeAndSet(jsonParser, context, instance);
 fieldProperty._throwAsIOE(jsonParser, e, value);
 }
}