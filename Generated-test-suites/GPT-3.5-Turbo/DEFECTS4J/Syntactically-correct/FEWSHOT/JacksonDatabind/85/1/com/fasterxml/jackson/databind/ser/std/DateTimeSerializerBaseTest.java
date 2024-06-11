package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
  private DateTimeSerializerBase<DateTime> serializer;

  @Mock
  private SerializerProvider serializers;

  @Mock
  private BeanProperty property;

  @Mock
  private JsonFormatVisitorWrapper visitor;

  @Mock
  private JavaType typeHint;

  @Mock
  private JsonGenerator generator;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    serializer = new DateTimeSerializerBase<TestDateTime>(TestDateTime.class,
        Boolean.TRUE, null) {
      private static final long serialVersionUID = 1L;

      @Override
      public void serialize(TestDateTime value, JsonGenerator gen,
          SerializerProvider serializers) throws IOException {
      }

      @Override
      public DateTimeSerializerBase<TestDateTime> withFormat(Boolean timestamp,
          DateFormat customFormat) {
        return null;
      }

      @Override
      protected long _timestamp(TestDateTime value) {
        return 0;
      }

      @Override
      public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor,
          JavaType typeHint) throws JsonMappingException {
      }
    };
  }

  @Test
  public void testConstructor() {
    assertEquals(TestDateTime.class, serializer.handledType());
    assertEquals(Boolean.TRUE, serializer._useTimestamp);
    assertNull(serializer._customFormat);
  }

  @Test
  public void testCreateContextual() throws JsonMappingException {
    when(property.getType()).thenReturn(typeHint);
    when(serializers.getLocale()).thenReturn(Locale.US);
    when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));

    assertNull(serializer.createContextual(serializers, property));

    JsonFormat.Value format = new JsonFormat.Value();
    format.setShape(JsonFormat.Shape.STRING);
    format.setPattern("yyyy-MM-dd");

    when(property.findPropertyFormat(serializers.getConfig(), handledType()))
        .thenReturn(format);

    assertNull(serializer.createContextual(serializers, property));

    format = new JsonFormat.Value();
    format.setShape(JsonFormat.Shape.STRING);
    format.setLocale(Locale.US);
    format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

    assertEquals(serializer,
        serializer.createContextual(serializers, property));

    format = new JsonFormat.Value();
    format.setShape(JsonFormat.Shape.STRING);
    format.setLocale(Locale.US);

    assertEquals(serializer,
        serializer.createContextual(serializers, property));

    format = new JsonFormat.Value();
    format.setShape(JsonFormat.Shape.STRING);
    format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

    assertEquals(serializer,
        serializer.createContextual(serializers, property));

    format = new JsonFormat.Value();
    format.setShape(JsonFormat.Shape.STRING);

    assertEquals(serializer,
        serializer.createContextual(serializers, property));
  }

  @Test
  public void testIsEmpty() {
    assertNull(serializer.isEmpty(serializers, null));
    assertFalse(serializer.isEmpty(serializers, new TestDateTime()));
  }

  @Test
  public void testGetSchema() {
    assertNotNull(serializer.getSchema(serializers, TestDateTime.class));
  }

  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
    verify(serializer).acceptJsonFormatVisitor(visitor, typeHint);
  }

  private static class TestDateTime extends DateTime {
    private static final long serialVersionUID = 1L;
  }
}