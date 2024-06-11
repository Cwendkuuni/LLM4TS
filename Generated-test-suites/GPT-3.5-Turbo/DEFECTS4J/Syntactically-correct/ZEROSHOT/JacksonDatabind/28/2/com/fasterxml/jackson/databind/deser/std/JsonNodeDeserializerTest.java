package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {
  
  @Test
  public void testGetDeserializer() {
    assertEquals(ObjectDeserializer.class, JsonNodeDeserializer.getDeserializer(ObjectNode.class).getClass());
    assertEquals(ArrayDeserializer.class, JsonNodeDeserializer.getDeserializer(ArrayNode.class).getClass());
    assertEquals(JsonNodeDeserializer.class, JsonNodeDeserializer.getDeserializer(JsonNode.class).getClass());
  }
  
  @Test
  public void testGetNullValue(DeserializationContext ctxt) {
    JsonNodeDeserializer jnd = new JsonNodeDeserializer();
    assertEquals(NullNode.getInstance(), jnd.getNullValue(ctxt));
  }
  
  @Test
  public void testGetNullValue() {
    JsonNodeDeserializer jnd = new JsonNodeDeserializer();
    assertEquals(NullNode.getInstance(), jnd.getNullValue());
  }
  
  @Test
  public void testDeserialize() {
    JsonNodeDeserializer jnd = new JsonNodeDeserializer();
    String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode expected = (ObjectNode) mapper.readTree(json);
    JsonFactory jsonFactory = new JsonFactory();
    JsonParser p = jsonFactory.createParser(json);
    DeserializationContext ctxt = mapper.getDeserializationContext();
    try {
      assertEquals(expected, jnd.deserialize(p, ctxt));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testDeserializeObject() {
    JsonNodeDeserializer jnd = new JsonNodeDeserializer();
    ObjectMapper mapper = new ObjectMapper();
    String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
    ObjectNode expected = (ObjectNode) mapper.readTree(json);
    JsonFactory jsonFactory = new JsonFactory();
    JsonParser p = jsonFactory.createParser(json);
    DeserializationContext ctxt = mapper.getDeserializationContext();
    try {
      assertEquals(expected, jnd.deserializeObject(p, ctxt, ctxt.getNodeFactory()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testDeserializeArray() {
    JsonNodeDeserializer jnd = new JsonNodeDeserializer();
    ObjectMapper mapper = new ObjectMapper();
    String json = "[{\"name\":\"John\",\"age\":30,\"city\":\"New York\"},{\"name\":\"Bob\",\"age\":25,\"city\":\"Los Angeles\"}]";
    ArrayNode expected = (ArrayNode) mapper.readTree(json);
    JsonFactory jsonFactory = new JsonFactory();
    JsonParser p = jsonFactory.createParser(json);
    DeserializationContext ctxt = mapper.getDeserializationContext();
    try {
      assertEquals(expected, jnd.deserializeArray(p, ctxt, ctxt.getNodeFactory()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testDeserializeAny() {
    JsonNodeDeserializer jnd = new JsonNodeDeserializer();
    ObjectMapper mapper = new ObjectMapper();
    String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
    ObjectNode expected = (ObjectNode) mapper.readTree(json);
    JsonFactory jsonFactory = new JsonFactory();
    JsonParser p = jsonFactory.createParser(json);
    DeserializationContext ctxt = mapper.getDeserializationContext();
    try {
      assertEquals(expected, jnd.deserializeAny(p, ctxt, ctxt.getNodeFactory()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Add more tests for other methods in JsonNodeDeserializer
  
}