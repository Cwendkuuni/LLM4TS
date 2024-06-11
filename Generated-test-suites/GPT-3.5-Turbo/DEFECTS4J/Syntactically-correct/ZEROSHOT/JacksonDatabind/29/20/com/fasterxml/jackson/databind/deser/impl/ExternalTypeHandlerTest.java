package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {
  
  @Test
  public void testHandleTypePropertyValue() throws IOException {
    ExternalTypeHandler externalTypeHandler = createExternalTypeHandler();
    String propName = "type";
    Object bean = new Object();
    JsonParser jp = null; // Pass valid JsonParser object here
    
    boolean actual = externalTypeHandler.handleTypePropertyValue(jp, null, propName, bean);
    
    Assert.assertEquals(true, actual);
  }
  
  @Test
  public void testHandlePropertyValue() throws IOException {
    ExternalTypeHandler externalTypeHandler = createExternalTypeHandler();
    String propName = "prop";
    Object bean = new Object();
    JsonParser jp = null; // Pass valid JsonParser object here
    
    boolean actual = externalTypeHandler.handlePropertyValue(jp, null, propName, bean);
    
    Assert.assertEquals(true, actual);
  }
  
  @Test
  public void testComplete() throws IOException {
    ExternalTypeHandler externalTypeHandler = createExternalTypeHandler();
    JsonParser jp = null; // Pass valid JsonParser object here
    DeserializationContext ctxt = null; // Pass valid DeserializationContext object here
    Object bean = new Object();
    
    Object actual = externalTypeHandler.complete(jp, ctxt, bean);
    
    Assert.assertNotNull(actual);
  }
  
  @Test
  public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws IOException {
    ExternalTypeHandler externalTypeHandler = createExternalTypeHandler();
    JsonParser jp = null; // Pass valid JsonParser object here
    DeserializationContext ctxt = null; // Pass valid DeserializationContext object here
    PropertyValueBuffer buffer = null; // Pass valid PropertyValueBuffer object here
    PropertyBasedCreator creator = null; // Pass valid PropertyBasedCreator object here
    
    Object actual = externalTypeHandler.complete(jp, ctxt, buffer, creator);
    
    Assert.assertNotNull(actual);
  }
  
  private ExternalTypeHandler createExternalTypeHandler() {
    ExtTypedProperty property1 = new ExtTypedProperty(null, null);
    ExtTypedProperty property2 = new ExtTypedProperty(null, null);
    ExtTypedProperty[] properties = { property1, property2 };
    
    HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
    nameToPropertyIndex.put("prop1", 0);
    nameToPropertyIndex.put("prop2", 1);
    
    String[] typeIds = { "typeId1", "typeId2" };
    
    TokenBuffer[] tokens = { null, null };
    
    return new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
  }
}