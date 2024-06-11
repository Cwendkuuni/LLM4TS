package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.*;

import static org.junit.Assert.*;

public class JdkDeserializersTest {
  
  @Test
  public void testFind() {
    JsonDeserializer<?> jsonDeserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
    assertTrue(jsonDeserializer instanceof UUIDDeserializer);
    
    jsonDeserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
    assertTrue(jsonDeserializer instanceof StackTraceElementDeserializer);
    
    jsonDeserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
    assertTrue(jsonDeserializer instanceof AtomicBooleanDeserializer);
    
    jsonDeserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
    assertTrue(jsonDeserializer instanceof ByteBufferDeserializer);
    
    jsonDeserializer = JdkDeserializers.find(String.class, String.class.getName());
    assertNull(jsonDeserializer);
  }

}