package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

  @InjectMocks
  private NullifyingDeserializer nullifyingDeserializer;

  @Test
  public void testDeserialize() {
    // Test when JsonParser is null
    assertNull(nullifyingDeserializer.deserialize(null, null));
    
    // Test when DeserializationContext is null
    assertNull(nullifyingDeserializer.deserialize(null, null));
    
    // Test when JsonParser is not null
    assertNotNull(nullifyingDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)));
  }

  @Test
  public void testDeserializeWithType() {
    // Test when JsonParser is null
    assertNull(nullifyingDeserializer.deserializeWithType(null, null, null));
    
    // Test when DeserializationContext is null
    assertNull(nullifyingDeserializer.deserializeWithType(Mockito.any(JsonParser.class), null, null));
    
    // Test when JsonParser is not null
    assertNotNull(nullifyingDeserializer.deserializeWithType(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class), Mockito.any(TypeDeserializer.class)));
  }
}