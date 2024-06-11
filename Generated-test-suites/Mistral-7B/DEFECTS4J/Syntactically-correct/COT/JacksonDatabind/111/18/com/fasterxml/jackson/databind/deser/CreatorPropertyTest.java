package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CreatorPropertyTest {

 private static final Class<?> CLASS_UNDER_TEST = CreatorProperty.class;
 private static final PropertyName NAME = PropertyName.construct("name");
 private static final JavaType TYPE = new TypeReference<Integer>() {}.getType();
 private static final PropertyMetadata METADATA = new PropertyMetadata(new AnnotatedMember(null), null, null);
 private CreatorProperty property;
 private AnnotatedParameter parameter;
 private Object injectableValueId;
 private DeserializationContext context;
 private Object beanInstance;

 @Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);
 context = mock(DeserializationContext.class);
 beanInstance = new Object();
 parameter = mock(AnnotatedParameter.class);
 injectableValueId = new Object();
 property = new CreatorProperty(NAME, TYPE, null, null, null, parameter, 0, injectableValueId, METADATA);
 }

 @Test
 public void testCreatorPropertyConstruction() {
 assertSame(NAME, property.getName());
 assertSame(TYPE, property.getType());
 assertNull(property._fallbackSetter);
 assertSame(0, property._creatorIndex);
 assertFalse(property._ignorable);
 assertSame(injectableValueId, property._injectableValueId);
 assertSame(parameter, property._annotated);
 }

 @Test
 public void testCreatorPropertyWithName() throws IOException {
 CreatorProperty newProperty = property.withName(new PropertyName("newName"));
 assertSame(new PropertyName("newName"), newProperty.getName());
 }

 @Test
 public void testCreatorPropertyWithValueDeserializer() throws IOException {
 JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
 CreatorProperty newProperty = property.withValueDeserializer(deserializer);
 assertSame(deserializer, newProperty._valueDeserializer);
 }

 @Test
 public void testCreatorPropertyWithNullProvider() throws IOException {
 NullValueProvider nva = mock(NullValueProvider.class);
 CreatorProperty newProperty = property.withNullProvider(nva);
 assertSame(nva, newProperty._nullProvider);
 }

 @Test
 public void testSetFallbackSetter() throws IOException {
 SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
 property.setFallbackSetter(fallbackSetter);
 assertSame(fallbackSetter, property._fallbackSetter);
 }

 @Test
 public void testMarkAsIgnorable() throws IOException {
 property.markAsIgnorable();
 assertTrue(property._ignorable);
 }

 @Test
 public void testIsIgnorable() throws IOException {
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingSetter() throws IOException {
 property.deserializeAndSet(mock(JsonParser.class), context, beanInstance);
 }

 @Test
 public void testFindInjectableValue() throws IOException {
 when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn("injectedValue");
 Object result = property.findInjectableValue(context, beanInstance);
 assertEquals("injectedValue", result);
 }

 @Test
 public void testInject() throws IOException {
 String expectedValue = "expectedValue";
 when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(expectedValue);
 property.inject(context, beanInstance);
 assertEquals(expectedValue, property.getValue(beanInstance));
 }

 @Test
 public void testSetGet() throws IOException {
 int expectedValue = 42;
 Object value = Integer.valueOf(expectedValue);
 property.setValue(beanInstance, value);
 assertEquals(expectedValue, property.getValue(beanInstance).intValue());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 IntDeserializer deserializer = mock(IntDeserializer.class);
 when(context.findValueDeserializer(TYPE, null)).thenReturn(deserializer);
 when(deserializer.deserialize(mock(JsonParser.class), context)).thenReturn(expectedValue);
 property.deserializeAndSet(mock(JsonParser.class), context, beanInstance);
 assertEquals(expectedValue, property.getValue(beanInstance).intValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 IntDeserializer deserializer = mock(IntDeserializer.class);
 when(context.findValueDeserializer(TYPE, null)).thenReturn(deserializer);
 when(deserializer.deserialize(mock(JsonParser.class), context)).thenReturn(expectedValue);
 Object result = property.deserializeSetAndReturn(mock(JsonParser.class), context, beanInstance);
 assertEquals(expectedValue, result);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 int expectedValue = 42;
 Object value = Integer.valueOf(expectedValue);
 Object result = property.setAndReturn(beanInstance, value);
 assertEquals(expectedValue, result.intValue());
 }

 @Test
 public void testGetInjectableValueId() {
 assertEquals(injectableValueId, property.getInjectableValueId());
 }

 @Test
 public void testToString() {
 assertEquals("[creator property, name 'name'; inject id '"+injectableValueId+"']", property.toString());
 }

 @Test(expected = IOException.class)
 public void testVerifySetterMissing() throws IOException {
 property._verifySetter();
 }

 @Test(expected = IOException.class)
 public void testVerifySetterNullContext() throws IOException {
 property._verifySetter();
 }
}