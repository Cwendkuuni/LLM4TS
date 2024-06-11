package com.google.gson.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonParser;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDatePattern() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", adapter.enUsFormat.toPattern());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateStyles() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testWriteNullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(new JsonWriter.OutputTypeAdapter(writer));
    adapter.write(jsonWriter, null);
    jsonWriter.flush();
    assertEquals("null", writer.toString());
  }

  @Test
  public void testWriteValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(new JsonWriter.OutputTypeAdapter(writer));
    Date date = new Date();
    adapter.write(jsonWriter, date);
    jsonWriter.flush();
    assertEquals(adapter.toString() + "(java.text.SimpleDateFormat)", writer.toString().trim());
  }

  @Test
  public void testWriteInvalidDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(new JsonWriter.OutputTypeAdapter(writer));
    jsonWriter.serializeValue("invalid_date_format", adapter);
    jsonWriter.flush();
    assertEquals("{\"name\":\"invalid_date_format\",\"message\":\"The date should be a string value\"}", writer.toString().trim());
  }

  @Test
  public void testReadNullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    try {
      adapter.read(jsonReader);
      fail("Expected JsonParseException for null value");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testReadValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("2022-12-31T23:59:59.999Z"));
    Date date = adapter.read(jsonReader);
    assertEquals("Sat Dec 31 23:59:59 GMT 2022", date.toString());
  }

  @Test
  public void testReadInvalidDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("invalid_date_format"));
    try {
      adapter.read(jsonReader);
      fail("Expected JsonParseException for invalid date format");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testReadInvalidJsonSyntaxException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("{\"invalid\":\"json\"}"));
    try {
      adapter.read(jsonReader);
      fail("Expected JsonSyntaxException for invalid JSON");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testReadInvalidJsonToken() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("[]"));
    try {
      adapter.read(jsonReader);
      fail("Expected JsonParseException for invalid JsonToken");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testReadNullJsonReader() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.read(null);
      fail("Expected IOException for null JsonReader");
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testReadInvalidClassException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class, DateFormat.getDateTimeInstance(Date.SHORT, Date.SHORT), DateFormat.getDateTimeInstance(Date.SHORT, Date.SHORT));
    try {
      adapter.read(new JsonReader(new StringReader("2022-12-31T23:59:59.999Z")));
      fail("Expected IllegalArgumentException for invalid class type");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDeserializeToDateWithValidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.deserializetoDate("2022-12-31T23:59:59.999Z");
    assertEquals("Sat Dec 31 23:59:59 GMT 2022", date.toString());
  }

  @Test
  public void testDeserializeToDateWithInvalidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.deserializetoDate("invalid_date_format");
      fail("Expected ParseException for invalid date string");
    } catch (ParseException e) {
      // Expected
    }
  }

  @Test
  public void testDeserializeToDateWithEmptyString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.deserializetoDate("");
      fail("Expected JsonParseException for empty string");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testDeserializeToDateWithNullString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.deserializetoDate(null);
      fail("Expected JsonParseException for null string");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testDeserializeToDateWithEmptyJsonReader() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    try {
      adapter.deserializetoDate(jsonReader);
      fail("Expected JsonParseException for empty JsonReader");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testDeserializeToDateWithNullJsonReader() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.deserializetoDate(null);
      fail("Expected IOException for null JsonReader");
    } catch (IOException e) {
      // Expected
    }
  }
}