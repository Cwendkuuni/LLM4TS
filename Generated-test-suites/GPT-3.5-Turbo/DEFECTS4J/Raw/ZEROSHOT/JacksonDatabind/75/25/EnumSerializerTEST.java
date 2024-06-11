package com.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class EnumSerializerTest {

  @Test
  public void testSerialize() throws Exception {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null));
    Enum en = Mockito.mock(Enum.class);
    JsonGenerator gen = Mockito.mock(JsonGenerator.class);
    SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
    enumSerializer.serialize(en, gen, serializers);
    Mockito.verify(gen).writeString(Mockito.anyString());
  }

  @Test
  public void testCreateContextual() throws Exception {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null));
    SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
    BeanProperty property = Mockito.mock(BeanProperty.class);
    enumSerializer.createContextual(serializers, property);
    Mockito.verify(serializers).isEnabled(Mockito.any(SerializationFeature.class));
    Mockito.verify(serializers).constructType(Mockito.any(Type.class));
  }

  @Test
  public void testGetSchema() throws Exception {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null));
    SerializerProvider provider = Mockito.mock(SerializerProvider.class);
    Type typeHint = Mockito.mock(Type.class);
    enumSerializer.getSchema(provider, typeHint);
    Mockito.verify(provider).constructType(Mockito.any(Type.class));
  }
  
  @Test
  public void testAcceptJsonFormatVisitor() throws Exception {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null));
    JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
    JavaType typeHint = Mockito.mock(JavaType.class);
    enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    Mockito.verify(visitor).getProvider();
    Mockito.verify(visitor).expectStringFormat(Mockito.any(JavaType.class));
  }

  @Test
  public void test_serializeAsIndex() throws Exception {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null));
    SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
    enumSerializer._serializeAsIndex(serializers);
    Mockito.verify(serializers).isEnabled(Mockito.any(SerializationFeature.class));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void test_isShapeWrittenUsingIndex_withUnsupportedShape() throws Exception {
    EnumSerializer._isShapeWrittenUsingIndex(Enum.class, null, false);
  }

}