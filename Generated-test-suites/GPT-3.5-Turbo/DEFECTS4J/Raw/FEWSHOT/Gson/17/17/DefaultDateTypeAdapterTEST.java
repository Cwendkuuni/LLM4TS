package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
  
  @Test
  public void testConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithPattern() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd");
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.MEDIUM);
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.MEDIUM);
    assertNotNull(adapter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithInvalidDateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Object.class);
  }
  
  @Test
  public void testWriteNullDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = null;
    try {
      writer = new JsonWriter(null);
      adapter.write(writer, null);
      assertNull(writer.getOutput());
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
  
  @Test
  public void testWriteDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = null;
    try {
      writer = new JsonWriter(null);
      Date date = new Date();
      adapter.write(writer, date);
      assertEquals(date.getTime(), Long.parseLong(writer.getOutput()));
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
  
  @Test
  public void testReadInvalidJsonToken() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = null;
    try {
      reader = new JsonReader(null);
      reader.beginObject();
      boolean exceptionThrown = false;
      try {
        adapter.read(reader);
      } catch (JsonParseException e) {
        exceptionThrown = true;
        assertTrue(e.getMessage().contains("string value"));
      }
      assertTrue(exceptionThrown);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
  
  @Test
  public void testReadDateTypeDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = null;
    try {
      reader = new JsonReader(null);
      reader.beginObject();
      reader.nextName();
      reader.skipValue();
      Date date = adapter.read(reader);
      assertNull(date);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
  
  @Test
  public void testReadDateTypeTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = null;
    try {
      reader = new JsonReader(null);
      reader.beginObject();
      reader.nextName();
      reader.skipValue();
      Timestamp timestamp = adapter.read(reader);
      assertNull(timestamp);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
  
  @Test
  public void testReadDateTypeSqlServerDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = null;
    try {
      reader = new JsonReader(null);
      reader.beginObject();
      reader.nextName();
      reader.skipValue();
      java.sql.Date sqlDate = adapter.read(reader);
      assertNull(sqlDate);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
  
  @Test(expected = AssertionError.class)
  public void testReadDateTypeUnknown() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Object.class);
    JsonReader reader = null;
    try {
      reader = new JsonReader(null);
      reader.beginObject();
      reader.nextName();
      reader.skipValue();
      adapter.read(reader);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
  
  @Test
  public void testDeserializeToDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertNotNull(adapter.deserializeToDate("2022-01-01"));
  }
  
  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateInvalidFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.deserializeToDate("2022/01/01");
  }
  
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}