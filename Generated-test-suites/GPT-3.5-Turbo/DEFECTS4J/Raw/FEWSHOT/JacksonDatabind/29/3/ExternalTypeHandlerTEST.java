package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.*;

public class ExternalTypeHandlerTest {

  @Test
  public void testHandleTypePropertyValue() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JSONParser jp = new JSONParser();
    DeserializationContext ctxt = new DeserializationContext(null, null);
    String propName = "propName";
    Object bean = new Object();

    boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

    assertFalse(result);
  }

  @Test
  public void testHandlePropertyValue() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JSONParser jp = new JSONParser();
    DeserializationContext ctxt = new DeserializationContext(null, null);
    String propName = "propName";
    Object bean = new Object();

    boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

    assertFalse(result);
  }

  @Test(expected = IOException.class)
  public void testComplete() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JSONParser p = new JSONParser();
    DeserializationContext ctxt = new DeserializationContext(null, null);
    Object bean = new Object();

    Object result = externalTypeHandler.complete(p, ctxt, bean);

  }

  @Test(expected = IOException.class)
  public void testCompleteShouldThrowIOException() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JSONParser jp = new JSONParser();
    DeserializationContext ctxt = new DeserializationContext(null, null);
    PropertyValueBuffer buffer = new PropertyValueBuffer(ctxt, null);
    PropertyBasedCreator creator = new PropertyBasedCreator(null, null, null);
    Object bean = new Object();

    Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

  }

  @Test
  public void testDeserialize() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JSONParser p = new JSONParser();
    DeserializationContext ctxt = new DeserializationContext(null, null);
    int index = 0;
    String typeId = "typeId";

    Object result = externalTypeHandler.deserialize(p, ctxt, index, typeId);

    assertNotNull(result);
  }

  @Test
  public void testDeserializeAndSet() throws IOException {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
    JSONParser p = new JSONParser();
    DeserializationContext ctxt = new DeserializationContext(null, null);
    Object bean = new Object();
    int index = 0;
    String typeId = "typeId";

    externalTypeHandler.deserializeAndSet(p, ctxt, bean, index, typeId);

  }

  @Test
  public void testBuilder() {
    ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();

    assertNotNull(builder);

    ExternalTypeHandler result = builder.build();

    assertNotNull(result);
  }

}