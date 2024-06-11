package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DefaultDateTypeAdapterTest {
  
  @Test
  public void testWriteNullDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null); //replace with your own JsonWriter instance
    adapter.write(writer, null);
    assertEquals(JsonToken.NULL, writer.peek());
  }
  
  @Test
  public void testWriteDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null); //replace with your own JsonWriter instance
    Date date = new Date();
    adapter.write(writer, date);
    assertEquals(date.toString(), writer.peek().toString());
  }
  
  @Test
  public void testReadInvalidJsonToken() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null); //replace with your own JsonReader instance
    reader.beginObject();
    reader.name("date");
    reader.value("null");
    reader.endObject();
    try {
      adapter.read(reader);
      assertTrue(false); // should not reach this point
    } catch (JsonParseException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testReadValidDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null); //replace with your own JsonReader instance
    reader.beginObject();
    reader.name("date");
    reader.value("2022-04-01T10:30:00Z");
    reader.endObject();
    Date date = adapter.read(reader);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    Date expectedDate = dateFormat.parse("2022-04-01T10:30:00Z");
    assertEquals(expectedDate.getTime(), date.getTime());
  }
  
  @Test
  public void testDeserializeToDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-04-01T10:30:00Z";
    Date date = adapter.deserializeToDate(dateString);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    Date expectedDate = dateFormat.parse(dateString);
    assertEquals(expectedDate.getTime(), date.getTime());
  }
  
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
    assertEquals(expectedString, adapter.toString());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testInvalidDateType() {
    Class<? extends Date> dateType = String.class;
    new DefaultDateTypeAdapter(dateType);
  }
  
}