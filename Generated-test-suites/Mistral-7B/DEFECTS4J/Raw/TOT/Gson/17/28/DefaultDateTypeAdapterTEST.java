package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class);

    // Test serialization of a Date object
    Date date = new Date();
    JsonWriter writer = new JsonWriter(System.out);
    dateAdapter.write(writer, date);
    assertEquals("expected a string representation of the Date object", writer.toString(), date.toString());

    // Test deserialization of a Date string
    JsonReader reader = new JsonReader(new StringReader(date.toString()));
    Date deserializedDate = dateAdapter.read(reader);
    assertEquals(date, deserializedDate);

    // Test serialization of a Timestamp object
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    writer = new JsonWriter(System.out);
    dateAdapter.write(writer, timestamp);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    assertEquals("expected a string representation of the Timestamp object", dateFormat.format(timestamp), writer.toString());

    // Test deserialization of a Timestamp string
    String timestampString = dateFormat.format(timestamp);
    reader = new JsonReader(new StringReader(timestampString));
    DeserializationTestHelper.assertDeserializedValue(reader, dateAdapter, timestamp);

    // Test serialization of a java.sql.Date object
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    writer = new JsonWriter(System.out);
    dateAdapter.write(writer, sqlDate);
    assertEquals("expected a string representation of the java.sql.Date object", sqlDate.toString(), writer.toString());

    // Test deserialization of a java.sql.Date string
    String sqlDateString = sqlDate.toString();
    reader = new JsonReader(new StringReader(sqlDateString));
    DeserializationTestHelper.assertDeserializedValue(reader, dateAdapter, sqlDate);

    // Test failure scenarios
    reader = new JsonReader(new StringReader("null"));
    assertNull("expected null when the JSON value is null", dateAdapter.read(reader));

    reader = new JsonReader(new StringReader("2022-12-31-12:59:59.999"));
    assertThrows(JsonSyntaxException.class, () -> dateAdapter.read(reader));

    reader = new JsonReader(new StringReader("2022-12-31T23:59:59.999+00:00"));
    assertThrows(JsonParseException.class, () -> dateAdapter.read(reader));

    reader = new JsonReader(new StringReader("InvalidDate"));
    assertThrows(JsonParseException.class, () -> dateAdapter.read(reader));

    reader = new JsonReader(new StringReader("2022-12-31T23:59:59.999-10:00"));
    assertThrows(SQLException.class, () -> dateAdapter.read(reader));
  }
}

class DeserializationTestHelper {
  static void assertDeserializedValue(JsonReader reader, TypeAdapter<Date> adapter, Date expected) throws IOException {
    Date deserializedValue = adapter.read(reader);
    assertEquals("expected the deserialized value to match the expected value", expected, deserializedValue);
  }
}