package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
  
  private StringArrayDeserializer stringArrayDeserializer;

  @Before
  public void setUp() {
    stringArrayDeserializer = StringArrayDeserializer.instance;
  }

  @Test
  public void testDeserialize() throws IOException {
    JsonParser jp = new JsonFactory().createParser("[\"foo\",\"bar\"]");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    String[] expectedResult = {"foo", "bar"};

    assertArrayEquals(expectedResult, stringArrayDeserializer.deserialize(jp, ctxt));
  }

  @Test
  public void testDeserializeWithNullValue() throws IOException {
    JsonParser jp = new JsonFactory().createParser("[\"foo\",null,\"bar\"]");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    String[] expectedResult = {"foo", null, "bar"};

    assertArrayEquals(expectedResult, stringArrayDeserializer.deserialize(jp, ctxt));
  }

  @Test
  public void testDeserializeWithEmptyArray() throws IOException {
    JsonParser jp = new JsonFactory().createParser("[]");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    String[] expectedResult = {};

    assertArrayEquals(expectedResult, stringArrayDeserializer.deserialize(jp, ctxt));
  }

  @Test
  public void testDeserializeWithSingleValue() throws IOException {
    JsonParser jp = new JsonFactory().createParser("\"foo\"");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    String[] expectedResult = {"foo"};

    assertArrayEquals(expectedResult, stringArrayDeserializer.deserialize(jp, ctxt));
  }

  @Test
  public void testDeserializeWithEmptyStringAsNullObject() throws IOException {
    JsonParser jp = new JsonFactory().createParser("\"\"");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    String[] expectedResult = null;

    assertArrayEquals(expectedResult, stringArrayDeserializer.deserialize(jp, ctxt));
  }

  @Test
  public void testDeserializeWithType() throws IOException {
    JsonParser jp = new JsonFactory().createParser("[\"foo\",\"bar\"]");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    TypeDeserializer typeDeserializer = new YourTypeDeserializer();

    assertArrayEquals(expectedResult, stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer));
  }
}