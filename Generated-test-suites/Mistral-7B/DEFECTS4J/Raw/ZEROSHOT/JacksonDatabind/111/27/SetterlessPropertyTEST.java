package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

 private static class TestBean {
 private List<String> list;

 @JsonProperty("list")
 @JsonGetter
 public List<String> getList() {
 return list;
 }
 }

 @org.junit.Test
 public void testSetterlessProperty() throws IOException {
 // Mocks
 JsonParser parser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 Method getter = mock(Method.class);
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 SettableBeanProperty property = new SetterlessProperty(propDef, mapper.getTypeFactory().constructCollectionType(List.class, String.class), typeDeserializer, contextAnnotations, annotatedMethod);
 property._getter = getter;
 property._annotated = annotatedMethod;

 // Test deserializeAndSet()
 when(getter.invoke(bean, (Object[]) null)).thenReturn(new ArrayList<>());
 when(typeDeserializer.deserializeValue(parser, context, String.class)).thenReturn("test");
 property.deserializeAndSet(parser, context, bean);
 assertEquals("test", bean.getList().get(0));

 // Test deserializeSetAndReturn()
 when(getter.invoke(bean, (Object[]) null)).thenReturn(new ArrayList<>());
 when(typeDeserializer.deserializeValue(parser, context, String.class)).thenReturn("test");
 assertEquals(bean, property.deserializeSetAndReturn(parser, context, bean));

 // Test set() and setAndReturn()
 when(getter.invoke(bean, (Object[]) null)).thenReturn(new ArrayList<>());
 when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.nextValue()).thenReturn("test");
 property.set(bean, "test");
 assertEquals("test", bean.getList().get(0));
 assertEquals(bean, property.setAndReturn(bean, "test"));

 // Test null value
 when(getter.invoke(bean, (Object[]) null)).thenReturn(null);
 when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 property.deserializeAndSet(parser, context, bean);
 assertNull(bean.getList());
 }
}