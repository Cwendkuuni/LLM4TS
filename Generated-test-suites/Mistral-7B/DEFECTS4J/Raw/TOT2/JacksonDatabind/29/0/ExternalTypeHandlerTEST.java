package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;
 private SettableBeanProperty mockProperty;
 private TypeDeserializer mockTypeDeserializer;
 private String mockTypeId;

 @Before
 public void setup() throws IOException {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 mockProperty = mock(SettableBeanProperty.class);
 mockTypeDeserializer = mock(TypeDeserializer.class);

 handler = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(mockProperty, mockTypeDeserializer)
 .build()
 );
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String propName = "testProperty";
 String typeId = "TestType";
 when(mockParser.getText()).thenReturn(typeId);
 when(mockTypeDeserializer.getPropertyName()).thenReturn(propName);
 when(mockBean != null).thenReturn(true);
 when(mockTypeDeserializer != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(true);

 handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);

 verify(mockParser).nextToken();
 verify(mockParser).skipChildren();
 verify(mockTypeDeserializer).deserialize(eq(mockParser), eq(mockContext), anyObject());

 assertEquals(typeId, _typeIds[0]);
 assertEquals(propName, _properties[0].getTypePropertyName());
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_MissingProperty() throws IOException {
 String propName = "nonExistentProperty";
 when(mockParser.getText()).thenReturn(propName);
 when(mockBean != null).thenReturn(true);
 when(mockTypeDeserializer != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(false);

 handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_NullBean() throws IOException {
 String propName = "testProperty";
 String typeId = "TestType";
 when(mockParser.getText()).thenReturn(typeId);
 when(mockTypeDeserializer.getPropertyName()).thenReturn(propName);
 when(mockBean == null).thenReturn(true);
 when(mockTypeDeserializer != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(true);

 handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_NullTypeDeserializer() throws IOException {
 String propName = "testProperty";
 String typeId = "TestType";
 when(mockParser.getText()).thenReturn(typeId);
 when(mockTypeDeserializer == null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(true);

 handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);
 }

 @Test
 public void testHandlePropertyValue_ScalarValue() throws IOException {
 String propName = "testProperty";
 String value = "testValue";
 when(mockParser.getText()).thenReturn(value);
 when(mockBean != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(false);

 handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);

 verify(mockParser).nextToken();
 verify(mockParser).skipChildren();
 //TODO: implement test for deserializing and setting value
 }

 @Test
 public void testHandlePropertyValue_MissingTypeId() throws IOException {
 String propName = "testProperty";
 when(mockParser.getText()).thenReturn(propName);
 when(mockBean != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(false);

 handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);
 //TODO: implement test for setting null value
 }

 @Test
 public void testComplete_NoMissingData() throws IOException {
 handler.complete(mockParser, mockContext, mockBean);
 verifyNoMoreInteractions(mockParser);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingExternalTypeId() throws IOException {
 String propName = "testProperty";
 when(mockParser.getText()).thenReturn(propName);
 when(mockBean != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(true);
 when(_tokens[0] == null).thenReturn(true);

 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingProperty() throws IOException {
 String propName = "nonExistentProperty";
 when(mockParser.getText()).thenReturn(propName);
 when(mockBean != null).thenReturn(true);
 when(_properties[0].hasTypePropertyName(propName)).thenReturn(false);

 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testComplete_NoTypeProperties() throws IOException {
 when(mockBean != null).thenReturn(true);

 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testComplete_NullBean() throws IOException {
 handler.complete(mockParser, mockContext, null);
 }

 @Test
 public void testComplete_CreatorProperties() throws IOException {
 handler.complete(mockParser, mockContext, null, mockTypeDeserializer);
 //TODO: implement test for creating bean using creator properties
 }
}