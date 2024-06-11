package com.fasterxml.jackson.databind.ser.std;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

  private DateTimeSerializerBase<Object> dateTimeSerializerBaseTest;

  private final Boolean useTimestamp = true;
  private final DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
  private final AtomicReference<DateFormat> reusedCustomFormat = new AtomicReference<>();

  public void setUp() {
    dateTimeSerializerBaseTest = new DateTimeSerializerBase<Object>(Object.class, useTimestamp, customFormat) {
      @Override
      public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      protected long _timestamp(Object value) {
        return 0;
      }

      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      }

      @Override
      public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
      }

      @Override
      public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
        return null;
      }
    };
  }

  public void testIsEmpty() {
    SerializerProvider serializers = null;
    Object value = null;
    assertFalse(dateTimeSerializerBaseTest.isEmpty(serializers, value));
  }

  public void testSerializeAsString() {
    JsonGenerator g = null;
    SerializerProvider provider = null;
    Date value = new Date();
    dateTimeSerializerBaseTest._serializeAsString(value, g, provider);
  }

  public void testCreateContextual() {
    SerializerProvider serializers = null;
    BeanProperty property = null;
    try {
      dateTimeSerializerBaseTest.createContextual(serializers, property);
      fail("Expected an UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Not yet implemented", e.getMessage());
    }
  }

  public void testGetDateFormatWhenStdDateFormat() {
    SerializerProvider serializers = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    df.setTimeZone(TimeZone.getTimeZone("PST"));

    StdDateFormat stdDateFormat = new StdDateFormat();
    stdDateFormat = stdDateFormat.withTimeZone(TimeZone.getTimeZone("PST"));

    assertFalse(dateTimeSerializerBaseTest._getDateFormat(serializers, stdDateFormat) == df);
  }

  public void testGetDateFormatWhenNotStdDateFormat() {
    SerializerProvider serializers = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    df.setTimeZone(TimeZone.getTimeZone("PST"));
    DateFormat dateFormat = new DateFormat() {
      @Override
      public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return null;
      }

      @Override
      public Date parse(String source, ParsePosition pos) {
        return null;
      }
    };

    assertFalse(dateTimeSerializerBaseTest._getDateFormat(serializers, dateFormat) == df);
  }

  public void testAsTimestampWhenUseTimestampNotNull() {
    assertFalse(dateTimeSerializerBaseTest._asTimestamp(null));
  }

  public void testAsTimestampWhenCustomFormatNull() {
    assertFalse(dateTimeSerializerBaseTest._asTimestamp(new SerializerProvider(null, null)));
  }

  public void testAsTimestampWhenSerializersIsEnabled() {
    SerializerProvider serializers = new SerializerProvider(null, null);
    serializers.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    assertTrue(dateTimeSerializerBaseTest._asTimestamp(serializers));
  }

  public void testAsTimestampWhenNothingMatches() {
    SerializerProvider serializers = new SerializerProvider(null, null);
    assertFalse(dateTimeSerializerBaseTest._asTimestamp(serializers));
  }

}