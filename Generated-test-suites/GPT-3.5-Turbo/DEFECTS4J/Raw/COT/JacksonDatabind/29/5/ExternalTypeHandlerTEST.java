
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {
  
  @Mock
  private JsonParser jp;

  @Mock
  private DeserializationContext ctxt;

  private ExternalTypeHandler externalTypeHandler;

  @Test
  public void handleTypePropertyValue_shouldReturnFalse_whenGivenPropertyDoesNotExist() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, "propName", null);
    // assertion here
  }

  @Test
  public void handleTypePropertyValue_shouldReturnFalse_whenGivenPropertyDoesNotHaveTypePropertyName() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, "propName", null);
    // assertion here
  }

  @Test
  public void handleTypePropertyValue_shouldSetTypeIds_whenGivenPropertyHasTypePropertyName() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, "propName", null);
    // assertion here
  }

  @Test
  public void handlePropertyValue_shouldReturnFalse_whenGivenPropertyDoesNotExist() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, "propName", null);
    // assertion here
  }

  @Test
  public void handlePropertyValue_shouldReturnFalse_whenGivenPropertyDoesNotHaveTypePropertyName() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, "propName", null);
    // assertion here
  }

  @Test
  public void handlePropertyValue_shouldBufferToken_whenGivenPropertyHasTypePropertyName() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, "propName", null);
    // assertion here
  }

  @Test
  public void complete_shouldNotThrowException_whenGivenTypeIdsAndTokensAreNull() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), null, null);
    Object bean = new Object();
    externalTypeHandler.complete(jp, ctxt, bean);
    // assertion here
  }

  @Test
  public void complete_shouldThrowException_whenGivenTypeIdsIsNullAndTokenIsNotNull() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), null, new TokenBuffer[0]);
    Object bean = new Object();
    externalTypeHandler.complete(jp, ctxt, bean);
    // assertion here
  }

  @Test
  public void complete_shouldThrowException_whenGivenTypeIdsIsNotNullAndTokenIsNull() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], null);
    Object bean = new Object();
    externalTypeHandler.complete(jp, ctxt, bean);
    // assertion here
  }

  @Test
  public void complete_shouldDeserializeAndSetToken_whenGivenTypeIdsAndTokenAreNotNull() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    Object bean = new Object();
    externalTypeHandler.complete(jp, ctxt, bean);
    // assertion here
  }

  @Test
  public void complete_shouldReturnNull_whenGivenTypeIdsAndTokenAreNotNull() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    Object bean = new Object();
    Object result = externalTypeHandler.complete(jp, ctxt, bean);
    // assertion here
  }

  @Test
  public void complete_shouldNotThrowException_whenGivenPropertyValueBufferIsEmpty() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    PropertyValueBuffer buffer = new PropertyValueBuffer(ctxt, true);
    PropertyBasedCreator creator = new PropertyBasedCreator(new ObjectMapper(), null, null, false, null, null, null);
    externalTypeHandler.complete(jp, ctxt, buffer, creator);
    // assertion here
  }

  @Test
  public void complete_shouldBuildBean_whenGivenPropertyValueBufferIsNotEmpty() {
    ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);
    PropertyValueBuffer buffer = new PropertyValueBuffer(ctxt, true);
    PropertyBasedCreator creator = new PropertyBasedCreator(new ObjectMapper(), null, null, false, null, null, null);
    externalTypeHandler.complete(jp, ctxt, buffer, creator);
    // assertion here
  }
}
