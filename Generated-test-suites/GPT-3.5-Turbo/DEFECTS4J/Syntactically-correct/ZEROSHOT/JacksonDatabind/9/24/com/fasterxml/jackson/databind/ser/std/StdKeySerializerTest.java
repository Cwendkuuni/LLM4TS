package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class StdKeySerializerTest {

  @Test
  public void testSerialize_DateValue_ShouldSerializeAsDefaultDateKey() throws IOException {
    // Arrange
    StdKeySerializer serializer = new StdKeySerializer();
    JsonGenerator jgen = new JsonGenerator(null,0,null,null);

    Date date = new Date();

    // Act
    serializer.serialize(date, jgen, null);

    // Assert
    assertEquals("Expected output date key", date.toString(), jgen.getCurrentName());
  }

  @Test
  public void testSerialize_NonDateValue_ShouldSerializeAsString() throws IOException {
    // Arrange
    StdKeySerializer serializer = new StdKeySerializer();
    JsonGenerator jgen = new JsonGenerator(null,0,null,null);

    Object value = new Object();

    // Act
    serializer.serialize(value, jgen, null);

    // Assert
    assertEquals("Expected output string key", value.toString(), jgen.getCurrentName());
  }

  @Test
  public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
    // Arrange
    StdKeySerializer serializer = new StdKeySerializer();
    SerializerProvider provider = null;
    Type typeHint = null;

    // Act
    JsonNode result = serializer.getSchema(provider, typeHint);

    // Assert
    assertNotNull("Schema node should not be null", result);
    assertEquals("Expected schema node type", JsonNodeFactory.instance.textNode("string"), result);
  }

  @Test
  public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
    // Arrange
    StdKeySerializer serializer = new StdKeySerializer();
    JsonFormatVisitorWrapper visitor = null;
    JavaType typeHint = null;

    // Act
    serializer.acceptJsonFormatVisitor(visitor, typeHint);

    // Assert
    // No assertion needed, should not throw any exception
  }
}