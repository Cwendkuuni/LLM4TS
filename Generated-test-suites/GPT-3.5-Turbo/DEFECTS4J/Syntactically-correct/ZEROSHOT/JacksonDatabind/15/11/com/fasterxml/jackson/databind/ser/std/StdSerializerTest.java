package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

@JacksonStdImpl
public class StdSerializerTest {

  @Test
  public void testSerialize() throws IOException, JsonGenerationException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {

      }
    };
    JsonGenerator jgen = null;
    SerializerProvider provider = null;

    // Act
    serializer.serialize(null, jgen, provider);
  }

  @Test
  public void testGetSchema() throws JsonMappingException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public Object getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return null;
      }
    };
    SerializerProvider provider = null;
    Type typeHint = null;

    // Act
    Object result = serializer.getSchema(provider, typeHint);

    // Assert
    assertNull(result);
  }

  @Test
  public void testGetSchema2() throws JsonMappingException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public Object getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonMappingException {
        return null;
      }
    };
    SerializerProvider provider = null;
    Type typeHint = null;
    boolean isOptional = false;

    // Act
    Object result = serializer.getSchema(provider, typeHint, isOptional);

    // Assert
    assertNull(result);
  }

  @Test
  public void testCreateObjectNode() {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public ObjectNode createObjectNode() {
        return null;
      }
    };

    // Act
    ObjectNode result = serializer.createObjectNode();

    // Assert
    assertNull(result);
  }

  @Test
  public void testCreateSchemaNode() {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public ObjectNode createSchemaNode(String type) {
        return null;
      }
    };
    String type = null;

    // Act
    ObjectNode result = serializer.createSchemaNode(type);

    // Assert
    assertNull(result);
  }

  @Test
  public void testCreateSchemaNode2() {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public ObjectNode createSchemaNode(String type, boolean isOptional) {
        return null;
      }
    };
    String type = null;
    boolean isOptional = false;

    // Act
    ObjectNode result = serializer.createSchemaNode(type, isOptional);

    // Assert
    assertNull(result);
  }

  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

      }
    };
    JsonFormatVisitorWrapper visitor = null;
    JavaType typeHint = null;

    // Act
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
  }

  @Test
  public void testWrapAndThrow() throws IOException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, String fieldName) throws IOException {

      }
    };
    SerializerProvider provider = null;
    Throwable t = null;
    Object bean = null;
    String fieldName = null;

    // Act
    try {
      serializer.wrapAndThrow(provider, t, bean, fieldName);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Assert
      assertEquals("Expected IOException message", e.getMessage(), null);
      assertEquals("Expected IOException cause", e.getCause(), t);
    }
  }

  @Test
  public void testWrapAndThrow2() throws IOException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, int index) throws IOException {

      }
    };
    SerializerProvider provider = null;
    Throwable t = null;
    Object bean = null;
    int index = 0;

    // Act
    try {
      serializer.wrapAndThrow(provider, t, bean, index);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Assert
      assertEquals("Expected IOException message", e.getMessage(), null);
      assertEquals("Expected IOException cause", e.getCause(), t);
    }
  }

  @Test
  public void testIsDefaultSerializer() {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      protected boolean isDefaultSerializer(JsonSerializer<?> serializer) {
        return true;
      }
    };
    JsonSerializer serializer1 = null;

    // Act
    boolean result = serializer.isDefaultSerializer(serializer1);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testFindConvertingContentSerializer() throws JsonMappingException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      protected Object findConvertingContentSerializer(SerializerProvider provider, BeanProperty prop, JsonSerializer<?> existingSerializer) throws JsonMappingException {
        return null;
      }
    };
    SerializerProvider provider = null;
    BeanProperty prop = null;
    JsonSerializer existingSerializer = null;

    // Act
    Object result = serializer.findConvertingContentSerializer(provider, prop, existingSerializer);

    // Assert
    assertNull(result);
  }

  @Test
  public void testFindPropertyFilter() throws JsonMappingException {
    // Arrange
    StdSerializer serializer = new StdSerializer(StdSerializerTest.class) {
      @Override
      protected PropertyFilter findPropertyFilter(SerializerProvider provider, Object filterId, Object valueToFilter) throws JsonMappingException {
        return null;
      }
    };
    SerializerProvider provider = null;
    Object filterId = null;
    Object valueToFilter = null;

    // Act
    PropertyFilter result = serializer.findPropertyFilter(provider, filterId, valueToFilter);

    // Assert
    assertNull(result);
  }
}