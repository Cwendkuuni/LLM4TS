
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.*;
import static org.mockito.Mockito.*;

public class EnumSerializerTest {

   @Test
   public void testSerializeAsIndex() throws IOException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      SerializerProvider serializers = mock(SerializerProvider.class);

      enumSerializer.serialize(en, gen, serializers);

      verify(gen, times(1)).writeNumber(en.ordinal());
   }

   @Test
   public void testSerializeUsingToString() throws IOException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      SerializerProvider serializers = mock(SerializerProvider.class);

      when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

      enumSerializer.serialize(en, gen, serializers);

      verify(gen, times(1)).writeString(en.toString());
   }

   @Test
   public void testSerializeUsingName() throws IOException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
      Enum<?> en = mock(Enum.class);
      JsonGenerator gen = mock(JsonGenerator.class);
      SerializerProvider serializers = mock(SerializerProvider.class);

      when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

      enumSerializer.serialize(en, gen, serializers);

      verify(enumValues, times(1)).serializedValueFor(en);
   }

   @Test
   public void testCreateContextualWithDifferentSerializeAsIndex() throws JsonMappingException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
      SerializerProvider serializers = mock(SerializerProvider.class);
      BeanDescription beanDesc = mock(BeanDescription.class);
      BeanProperty property = mock(BeanProperty.class);
      JsonFormat.Value format = mock(JsonFormat.Value.class);

      when(findFormatOverrides(serializers, property, enumSerializer.handledType())).thenReturn(format);
      when(format.getShape()).thenReturn(Shape.STRING);
      when(_isShapeWrittenUsingIndex(property.getType().getRawClass(), format, false)).thenReturn(false);

      JsonSerializer<?> result = enumSerializer.createContextual(serializers, property);

      Assert.assertEquals(enumSerializer, result);
   }

   @Test
   public void testCreateContextualWithSameSerializeAsIndex() throws JsonMappingException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
      SerializerProvider serializers = mock(SerializerProvider.class);
      BeanDescription beanDesc = mock(BeanDescription.class);
      BeanProperty property = mock(BeanProperty.class);
      JsonFormat.Value format = mock(JsonFormat.Value.class);

      when(findFormatOverrides(serializers, property, enumSerializer.handledType())).thenReturn(format);
      when(format.getShape()).thenReturn(Shape.STRING);
      when(_isShapeWrittenUsingIndex(property.getType().getRawClass(), format, false)).thenReturn(false);

      JsonSerializer<?> result = enumSerializer.createContextual(serializers, property);

      Assert.assertEquals(enumSerializer, result);
   }

   @Test
   public void testGetSchemaAsIndex() {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
      SerializerProvider provider = mock(SerializerProvider.class);
      Type typeHint = mock(Type.class);

      JsonNode result = enumSerializer.getSchema(provider, typeHint);

      Assert.assertEquals("integer", result.get("type").textValue());
   }

   @Test
   public void testGetSchemaAsString() {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
      SerializerProvider provider = mock(SerializerProvider.class);
      Type typeHint = mock(Type.class);
      JavaType javaType = mock(JavaType.class);

      when(provider.constructType(typeHint)).thenReturn(javaType);
      when(javaType.isEnumType()).thenReturn(true);

      ObjectNode result = (ObjectNode) enumSerializer.getSchema(provider, typeHint);

      ArrayNode enumNode = (ArrayNode) result.get("enum");
      Assert.assertEquals(enumValues.size(), enumNode.size());
   }

   @Test
   public void testAcceptJsonFormatVisitorAsIndex() throws JsonMappingException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
      JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
      JavaType typeHint = mock(JavaType.class);

      enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

      verify(visitor, times(1)).expectIntFormat(typeHint, JsonParser.NumberType.INT);
   }

   @Test
   public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
      EnumValues enumValues = mock(EnumValues.class);
      EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
      JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
      JavaType typeHint = mock(JavaType.class);
      JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
      Set<String> enums = new LinkedHashSet<String>();

      when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);
      when(_values.enums()).thenReturn(enums);

      enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

      verify(stringVisitor, times(1)).enumTypes(enums);
   }

   protected JsonFormat.Value findFormatOverrides(SerializerProvider serializers, BeanProperty property, Class<?> enumClass) {
      return null;
   }

   protected boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass) {
      return false;
   }

}
