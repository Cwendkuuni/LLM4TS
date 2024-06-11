package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.*;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {
  
  @Test
  public void testStart() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    ExternalTypeHandler handler = externalTypeHandler.start();
    assertNotNull(handler);
  }
  
  @Test
  public void testHandleTypePropertyValue() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    String propName = null;
    Object bean = null;
    boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
    assertFalse(result);
  }
  
  @Test
  public void testHandlePropertyValue() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    String propName = null;
    Object bean = null;
    boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
    assertFalse(result);
  }
  
  @Test
  public void testComplete() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object bean = null;
    Object result = externalTypeHandler.complete(p, ctxt, bean);
    assertNull(result);
  }
  
  @Test
  public void testCompleteWithPropertyValueBuffer() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    PropertyValueBuffer buffer = null;
    PropertyBasedCreator creator = null;
    Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);
    assertNull(result);
  }
  
  @Test
  public void testDeserialize() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    int index = 0;
    String typeId = "typeId";
    Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
    assertNull(result);
  }
  
  @Test
  public void testDeserializeAndSet() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object bean = null;
    int index = 0;
    String typeId = "typeId";
    externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
  }
  
  @Test
  public void testBuilder() {
    ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
    assertNotNull(builder);
  }
  
  @Test
  public void testExtTypedProperty() {
    SettableBeanProperty property = null;
    TypeDeserializer typeDeser = null;
    ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
    assertNotNull(extTypedProperty);
    extTypedProperty.linkTypeProperty(property);
  }
}