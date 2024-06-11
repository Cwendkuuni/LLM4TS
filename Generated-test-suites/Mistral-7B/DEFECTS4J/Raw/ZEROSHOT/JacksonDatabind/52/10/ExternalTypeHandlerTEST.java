package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.TypeDeserializers.MapDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ExternalTypeHandlerTest {

 private static class TestBean {
 private String typeId;
 private Map<String, String> map;
 }

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext ctxt;
 private TestBean bean;

 @Before
 public void setup() throws IOException {
 bean = new TestBean();
 handler = new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("typeId", new None()), null)
 .addExternal(new SettableBeanProperty("map"), new MapDeserializer(TypeFactory.defaultInstance())).build();
 parser = mock(JsonParser.class);
 ctxt = mock(DeserializationContext.class);
 }

 @Test
 public void testStart() throws IOException {
 handler = handler.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleTypePropertyValueWithoutBean() throws IOException {
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleTypePropertyValueWithoutTypeId() throws IOException {
 handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 parser.nextToken(); // consume START_OBJECT
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleTypePropertyValueWithoutMatchingProperty() throws IOException {
 handler.handleTypePropertyValue(parser, ctxt, "notMatching", bean);
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 parser.nextToken(); // consume START_OBJECT
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);

 assertEquals("expectedTypeId", bean.getTypeId(), handler.getTypeIdAt(0));
 assertEquals("expectedTypeId", handler.getTypeIdAt(0), handler.getTypeIdAt(1));
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 handler.handlePropertyValue(parser, ctxt, "map", bean);

 assertEquals(Arrays.asList("key1", "value1", "key2", "value2"), handler.getPropertyValuesAt(1));
 }

 @Test
 public void testComplete() throws IOException {
 handler.complete(parser, ctxt, bean);

 assertEquals("mapValue1", bean.getMap().get("key1"));
 assertEquals("mapValue2", bean.getMap().get("key2"));
 }

 @Test
 public void testCompleteWithCreator() throws IOException {
 BeanDescription desc = mock(BeanDescription.class);
 when(ctxt.findBeanDescription(TestBean.class)).thenReturn(desc);
 when(desc.getBeanProperty(PropertyName.construct("map"))).thenReturn(new SettableBeanProperty("map", new MapDeserializer(TypeFactory.defaultInstance())));
 when(desc.getBeanProperty(PropertyName.construct("map"))).thenReturn(new SettableBeanProperty("mapCreator", new MapDeserializer(TypeFactory.defaultInstance())));

 PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
 PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
 handler.complete(parser, ctxt, buffer, creator);

 assertEquals(Arrays.asList("key1", "value1", "key2", "value2"), buffer.getParameters());
 }

 @Test
 public void testCompleteWithNullTypeId() throws IOException {
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 parser.nextToken(); // consume START_OBJECT
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 handler.handlePropertyValue(parser, ctxt, "map", bean);

 handler.complete(parser, ctxt, bean);
 assertEquals(null, bean.getTypeId());
 }

 @Test
 public void testCompleteWithMissingProperty() throws IOException {
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handlePropertyValue(parser, ctxt, "missingProperty", bean);

 handler.complete(parser, ctxt, bean);
 }

 @Test
 public void testCompleteWithMissingTypeId() throws IOException {
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 handler.handlePropertyValue(parser, ctxt, "typeId", bean); // consume second typeId
 handler.handlePropertyValue(parser, ctxt, "map", bean); // consume map
 handler.handlePropertyValue(parser, ctxt, "missingTypeId", bean);

 handler.complete(parser, ctxt, bean);
 }

 @Test
 public void testGetTypeIdAt() throws IOException {
 handler.handleTypePropertyValue(parser, ctxt, "typeId", bean);
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handlePropertyValue(parser, ctxt, "typeId", bean);
 handler.handlePropertyValue(parser, ctxt, "map", bean);

 assertEquals("expectedTypeId", handler.getTypeIdAt(0), handler.getTypeIdAt(1));
 }

 @Test
 public void testGetPropertyValuesAt() throws IOException {
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handlePropertyValue(parser, ctxt, "map", bean);
 handler.handlePropertyValue(parser, ctxt, "map", bean);

 assertEquals(Arrays.asList("key1", "value1", "key2", "value2"), handler.getPropertyValuesAt(1));
 }
}