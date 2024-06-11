package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext context;
 private Object testObject;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForClass(ExternalTypeHandler.class);
 handler = reader.readValue(mapper.createParser("[]"), ExternalTypeHandler.class);
 testObject = new Object();
 parser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 when(parser.getCodec()).thenReturn(mapper);
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String propertyName = "typeProperty";
 String typeId = "TypeId1";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getType()).thenReturn(String.class);
 when(property.getName()).thenReturn(propertyName);
 when(property.getPropertyName()).thenReturn("typePropertyName");
 when(property.hasTypePropertyName(propertyName)).thenReturn(true);
 handler._nameToPropertyIndex.put(propertyName, 0);
 handler._nameToPropertyIndex.put("typePropertyName", 0);
 handler._typeIds = new String[1];
 handler._properties = new ExtTypedProperty[1];
 handler._properties[0] = new ExtTypedProperty(property, stringDeserializer);

 when(parser.getText()).thenReturn(typeId);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("TestValue");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.handleTypePropertyValue(parser, context, testObject);
 verify(parser).getText();
 verify(parser).nextToken();
 verify(parser).nextToken();
 verify(parser).getText();
 verify(parser).nextToken();
 verify(parser).nextToken();
 verify(parser).nextToken();
 assertEquals("TestValue", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String propertyName = "property";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getType()).thenReturn(String.class);
 when(property.getName()).thenReturn(propertyName);
 when(property.getPropertyName()).thenReturn("propertyName");
 handler._nameToPropertyIndex.put(propertyName, 0);
 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties = new ExtTypedProperty[1];
 handler._properties[0] = new ExtTypedProperty(property, null);
 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];

 when(parser.getText()).thenReturn("TestValue");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("TestValue");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.handlePropertyValue(parser, context, testObject, propertyName);
 verify(parser).getText();
 verify(parser).nextToken();
 verify(parser).nextToken();
 verify(parser).getText();
 verify(parser).nextToken();
 verify(parser).nextToken();
 verify(parser).nextToken();
 assertEquals("TestValue", handler._typeIds[0]);
 }

 @Test(expected = JsonProcessingException.class)
 public void testHandlePropertyValueWithNoTypeId() throws Exception {
 String propertyName = "property";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getType()).thenReturn(String.class);
 when(property.getName()).thenReturn(propertyName);
 when(property.getPropertyName()).thenReturn("propertyName");
 handler._nameToPropertyIndex.put(propertyName, 0);
 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties = new ExtTypedProperty[1];
 handler._properties[0] = new ExtTypedProperty(property, null);
 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];

 when(parser.getText()).thenReturn("TestValue");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.handlePropertyValue(parser, context, testObject, propertyName);
 }

 @Test(expected = JsonProcessingException.class)
 public void testHandleTypePropertyValueWithNoTypeIdProperty() throws Exception {
 String propertyName = "typeProperty";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getType()).thenReturn(String.class);
 when(property.getName()).thenReturn(propertyName);
 when(property.getPropertyName()).thenReturn("typePropertyName");
 handler._nameToPropertyIndex.put(propertyName, 0);
 handler._nameToPropertyIndex.put("typePropertyName", 0);
 handler._properties = new ExtTypedProperty[1];
 handler._properties[0] = new ExtTypedProperty(property, null);
 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];

 when(parser.getText()).thenReturn(propertyName);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("TestTypeId");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("nonExistentPropertyName");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.handleTypePropertyValue(parser, context, testObject);
 }

 @Test(expected = JsonProcessingException.class)
 public void testHandlePropertyValueWithNoProperty() throws Exception {
 String propertyName = "property";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getType()).thenReturn(String.class);
 when(property.getName()).thenReturn(propertyName);
 when(property.getPropertyName()).thenReturn("propertyName");
 handler._nameToPropertyIndex.put(propertyName, 0);
 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties = new ExtTypedProperty[1];
 handler._properties[0] = new ExtTypedProperty(property, null);
 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];

 when(parser.getText()).thenReturn("TestValue");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.handlePropertyValue(parser, context, testObject, propertyName);
 }

 @Test
 public void testComplete() throws Exception {
 String typeId1 = "TypeId1";
 String typeId2 = "TypeId2";
 String propertyName1 = "property1";
 String propertyName2 = "property2";
 SettableBeanProperty property1 = mock(SettableBeanProperty.class);
 SettableBeanProperty property2 = mock(SettableBeanProperty.class);
 when(property1.getType()).thenReturn(String.class);
 when(property1.getName()).thenReturn(propertyName1);
 when(property1.getPropertyName()).thenReturn("property1Name");
 when(property1.hasTypePropertyName(propertyName1)).thenReturn(true);
 handler._nameToPropertyIndex.put(propertyName1, 0);
 handler._nameToPropertyIndex.put("property1Name", 0);
 handler._properties = new ExtTypedProperty[2];
 handler._properties[0] = new ExtTypedProperty(property1, null);
 handler._properties[1] = new ExtTypedProperty(property2, null);
 handler._typeIds = new String[2];
 handler._typeIds[0] = typeId1;
 handler._typeIds[1] = typeId2;
 handler._tokens = new TokenBuffer[2];

 when(parser.getText()).thenReturn(typeId1);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("TestValue1");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName1);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(parser.getText()).thenReturn(typeId2);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("TestValue2");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName2);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(parser, context, testObject);
 verify(property1).set(testObject, "TestValue1");
 verify(property2).set(testObject, "TestValue2");
 }

 @Test
 public void testCompleteWithNulls() throws Exception {
 String propertyName = "property";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getType()).thenReturn(String.class);
 when(property.getName()).thenReturn(propertyName);
 when(property.getPropertyName()).thenReturn("propertyName");
 handler._nameToPropertyIndex.put(propertyName, 0);
 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties = new ExtTypedProperty[1];
 handler._properties[0] = new ExtTypedProperty(property, null);
 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];

 when(parser.getText()).thenReturn("TestValue");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn(propertyName);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(parser, context, testObject);
 assertNull(property.get(testObject));
 }
}