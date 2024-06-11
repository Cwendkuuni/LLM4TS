package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
  
  @Test
  public void testSerializeWithDate() throws IOException {
    StdKeySerializer serializer = new StdKeySerializer();
    JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
    SerializerProvider provider = Mockito.mock(SerializerProvider.class);
    Date value = new Date();
    serializer.serialize(value, jgen, provider);
    Mockito.verify(provider).defaultSerializeDateKey(value, jgen);
  }
  
  @Test
  public void testSerializeWithString() throws IOException {
    StdKeySerializer serializer = new StdKeySerializer();
    JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
    SerializerProvider provider = Mockito.mock(SerializerProvider.class);
    String value = "test";
    serializer.serialize(value, jgen, provider);
    Mockito.verify(jgen).writeFieldName(value);
  }

  @Test
  public void testGetSchema() throws JsonMappingException {
    StdKeySerializer serializer = new StdKeySerializer();
    SerializerProvider provider = Mockito.mock(SerializerProvider.class);
    Type typeHint = Mockito.mock(Type.class);
    JsonNode node = serializer.getSchema(provider, typeHint);
    assertNotNull(node);
    assertEquals("string", node.asText());
  }

  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    StdKeySerializer serializer = new StdKeySerializer();
    JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
    JavaType typeHint = Mockito.mock(JavaType.class);
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
    Mockito.verify(visitor).expectStringFormat(typeHint);
  }
}