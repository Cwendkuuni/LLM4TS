package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

  @Mock
  private SerializerProvider serializerProvider;

  @Mock
  private JsonGenerator jsonGenerator;

  @Mock
  private BeanProperty beanProperty;

  @Mock
  private Annotated annotated;

  @Mock
  private JsonFormatVisitorWrapper jsonFormatVisitorWrapper;

  @Mock
  private JavaType javaType;

  @Mock
  private ContextualSerializer contextualSerializer;

  @Test
  public void testSerialize() throws IOException {
    // Arrange
    DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value);
      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }
    };
    String value = "testValue";

    // Act
    dateTimeSerializerBase.serialize(value, jsonGenerator, serializerProvider);

    // Assert
    verify(jsonGenerator).writeString(eq(value));
  }

  @Test
  public void testGetSchema() throws IOException {
    // Arrange
    final String schemaType = "number";
    final boolean schemaInMillis = true;
    final String format = JsonValueFormat.UTC_MILLISEC.toString();
    final String stringDateFormat = "ISO8601";
    when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getDefault());
    when(jsonFormatVisitorWrapper.getProvider()).thenReturn(serializerProvider);
    when(jsonFormatVisitorWrapper.expectStringFormat(javaType)).thenReturn(true);
    when(jsonFormatVisitorWrapper.expectNumberFormat(javaType)).thenReturn(false);
    when(javaType.getContentType()).thenReturn(null);
    final DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType, JsonValueFormat formatHint) throws JsonMappingException {
        if (visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
          visitor.expectNumberFormat(javaType);
        } else {
          visitor.expectStringFormat(javaType);
        }
      }

      @Override
      protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat formatHint) throws JsonMappingException {
        if (!visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
          JavaType javaType = (typeHint.isArrayType()) ? typeHint.getContentType() : null;
          if (javaType == null) {
            visitor.expectStringFormat(typeHint);
          } else {
            visitor.expectStringFormat(javaType);
          }
        } else {
          JavaType javaType = (typeHint.isArrayType()) ? typeHint.getContentType() : null;
          if (javaType == null) {
            visitor.expectNumberFormat(typeHint);
          } else {
            visitor.expectNumberFormat(javaType);
          }
        }
      }
    };

    // Act
    dateTimeSerializerBase.getSchema(serializerProvider, javaType);

    // Assert
    verify(jsonFormatVisitorWrapper).expectStringFormat(eq(javaType));
  }

  @Test
  public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
    // Arrange
    final boolean asNumber = true;
    final JsonValueFormat formatHint = JsonValueFormat.UTC_MILLISEC;
    when(jsonFormatVisitorWrapper.getProvider()).thenReturn(serializerProvider);

    final DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType, JsonValueFormat formatHint) throws JsonMappingException {
        verify(visitor).expectIntFormat(eq(typeHint), eq(numberType));
      }
    };

    // Act
    dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

    // Assert
    verify(jsonFormatVisitorWrapper).expectIntFormat(eq(javaType), Mockito.any(JsonParser.NumberType.class));
  }

  @Test
  public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
    // Arrange
    final boolean asNumber = false;
    final JsonValueFormat formatHint = JsonValueFormat.UTC_MILLISEC;
    final boolean expected = false;
    when(jsonFormatVisitorWrapper.getProvider()).thenReturn(serializerProvider).thenReturn(serializerProvider);
    when(jsonFormatVisitorWrapper.expectNumberFormat(javaType)).thenReturn(expected);

    final DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat formatHint) throws JsonMappingException {
        visitor.expectIntFormat(typeHint, JsonParser.NumberType.LONG);
      }
    };

    // Act
    dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

    // Assert
    verify(jsonFormatVisitorWrapper).expectIntFormat(eq(javaType), Mockito.any(JsonParser.NumberType.class));
  }

  @Test
  public void testAcceptJsonFormatVisitor_AsTimestamp_NumberType() throws JsonMappingException {
    // Arrange
    final boolean asNumber = true;
    final JsonValueFormat formatHint = JsonValueFormat.UTC_MILLISEC;
    when(jsonFormatVisitorWrapper.getProvider()).thenReturn(serializerProvider);
    when(jsonFormatVisitorWrapper.expectNumberFormat(javaType)).thenReturn(true);

    final DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType, JsonValueFormat formatHint) throws JsonMappingException {
        visitor.expectIntFormat(typeHint, numberType);
      }
    };

    // Act
    dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

    // Assert
    verify(jsonFormatVisitorWrapper).expectIntFormat(eq(javaType), Mockito.any(JsonParser.NumberType.class));
  }

  @Test
  public void testAcceptJsonFormatVisitor_AsString_NumberType() throws JsonMappingException {
    // Arrange
    final boolean asNumber = false;
    final JsonValueFormat formatHint = JsonValueFormat.UTC_MILLISEC;
    final boolean expected = false;
    when(jsonFormatVisitorWrapper.getProvider()).thenReturn(serializerProvider).thenReturn(serializerProvider);
    when(jsonFormatVisitorWrapper.expectStringFormat(javaType)).thenReturn(expected);

    final DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat formatHint) throws JsonMappingException {
        visitor.expectStringFormat(javaType);
      }
    };

    // Act
    dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

    // Assert
    verify(jsonFormatVisitorWrapper).expectStringFormat(eq(javaType));
  }

  @Test
  public void testIsNotEmpty_NotNullTimestamp() {
    // Mock

    // Arrange
    final Long value = 1L;
    final Boolean useTimestamp = true;
    final DateFormat customFormat = null;
    final DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.class, useTimestamp, customFormat) {
      @Override
      public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(Long value) {
        return 0;
      }
    };

    // Act
    final boolean result = dateTimeSerializerBase.isEmpty(value);

    // Assert
    assert result == false;
  }

  @Test
  public void testIsNotEmpty_NullTimestamp() {
    // Mock

    // Arrange
    final Long value = 0L;
    final Boolean useTimestamp = false;
    final DateFormat customFormat = null;
    final DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.class, useTimestamp, customFormat) {
      @Override
      public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(Long value) {
        return 0;
      }
    };

    // Act
    final boolean result = dateTimeSerializerBase.isEmpty(value);

    // Assert
    assert result == true;
  }

  @Test
  public void testIsEmpty_Null() {
    // Mock

    // Arrange
    final Long value = null;
    final Boolean useTimestamp = true;
    final DateFormat customFormat = null;
    final DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.class, useTimestamp, customFormat) {
      @Override
      public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(Long value) {
        return 0;
      }
    };

    // Act
    final boolean result = dateTimeSerializerBase.isEmpty(value);

    // Assert
    assert result == true;
  }

  @Test
  public void testWithFormat() {
    // Mock
    final Boolean timestamp = true;
    final DateFormat customFormat = null;

    // Arrange
    final DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, customFormat) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {
          @Override
          public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
          }

          @Override
          public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

          }

          @Override
          protected long _timestamp(String value) {
            return 0;
          }
        };
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }
    };

    // Act
    dateTimeSerializerBase.withFormat(timestamp, customFormat);

    // Assert
    // Method throws no exception
  }
}