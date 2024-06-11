package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import java.io.Closeable;

public class JsonMappingExceptionTest {

  @org.junit.Test
  public void testJsonMappingException() {
    String msg = "JsonMappingException message";
    JsonMappingException exception = new JsonMappingException(msg);
    assertEquals(msg, exception.getMessage());
  }

  @org.junit.Test
  public void testJsonMappingExceptionWithCause() {
    String msg = "JsonMappingException message";
    Throwable cause = new IOException("IO exception");
    JsonMappingException exception = new JsonMappingException(msg, cause);
    assertEquals(msg, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @org.junit.Test
  public void testJsonMappingExceptionWithLocation() {
    String msg = "JsonMappingException message";
    JsonLocation location = new JsonLocation(1, 1, "test.json");
    JsonMappingException exception = new JsonMappingException(null, msg, location);
    assertEquals(msg, exception.getMessage());
    assertEquals(location, exception.getLocation());
  }

  @org.junit.Test
  public void testJsonMappingExceptionWithProcessor() {
    String msg = "JsonMappingException message";
    Closeable processor = null; // can be JsonParser or JsonGenerator
    JsonMappingException exception = new JsonMappingException(processor, msg);
    assertEquals(msg, exception.getMessage());
    assertNull(exception.getLocation());
  }

  @org.junit.Test
  public void testJsonMappingExceptionFromJsonParser() {
    String msg = "JsonMappingException message";
    JsonParser parser = null; // can be mocked or a real instance
    JsonMappingException exception = JsonMappingException.from(parser, msg);
    assertEquals(msg, exception.getMessage());
    assertNull(exception.getLocation());
  }

  @org.junit.Test
  public void testJsonMappingExceptionFromJsonGenerator() {
    String msg = "JsonMappingException message";
    JsonGenerator generator = null; // can be mocked or a real instance
    JsonMappingException exception = JsonMappingException.from(generator, msg);
    assertEquals(msg, exception.getMessage());
    assertNull(exception.getLocation());
  }

  @org.junit.Test
  public void testJsonMappingExceptionFromDeserializationContext() {
    String msg = "JsonMappingException message";
    JsonParser parser = null; // can be mocked or a real instance
    JsonMappingException exception = JsonMappingException.from(parser, msg);
    assertEquals(msg, exception.getMessage());
    assertNotNull(exception.getLocation());
  }

  @org.junit.Test
  public void testJsonMappingExceptionFromSerializerProvider() {
    String msg = "JsonMappingException message";
    JsonGenerator generator = null; // can be mocked or a real instance
    JsonMappingException exception = JsonMappingException.from(generator, msg);
    assertEquals(msg, exception.getMessage());
    assertNull(exception.getLocation());
  }

  @org.junit.Test(expected = JsonMappingException.class)
  public void testJsonMappingExceptionWithTooManyReferences() {
    String msg = "JsonMappingException message";
    Closeable processor = null;
    for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
      Reference reference = new JsonMappingException.Reference(processor, i);
      JsonMappingException exception = new JsonMappingException(processor, msg, reference);
    }
  }

  @org.junit.Test
  public void testToString() {
    String msg = "JsonMappingException message";
    JsonMappingException exception = new JsonMappingException(null, msg);
    String expected = "com.fasterxml.jackson.databind.JsonMappingException: " + msg;
    assertEquals(expected, exception.toString());
  }
}