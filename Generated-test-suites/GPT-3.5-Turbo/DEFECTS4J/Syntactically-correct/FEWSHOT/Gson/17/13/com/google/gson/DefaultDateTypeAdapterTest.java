package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
  
  @Test
  public void testWrite_NullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    try {
      adapter.write(writer, null);
      assertNull(writer.getValue());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testWrite_ValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    try {
      adapter.write(writer, date);
      assertEquals(writer.getValue(), adapter.localFormat.format(date));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testRead_InvalidJsonToken() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    try {
      adapter.read(reader);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // validate if the exception is thrown
  }
  
  @Test
  public void testRead_ValidJsonToken_DateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    reader.addToken(JsonToken.STRING, "2019-01-01");
    try {
      Date date = adapter.read(reader);
      assertEquals(adapter.localFormat.parse("2019-01-01"), date);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testRead_ValidJsonToken_TimestampType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader(null);
    reader.addToken(JsonToken.STRING, "2019-01-01");
    try {
      Date date = adapter.read(reader);
      Timestamp timestamp = new Timestamp(date.getTime());
      assertEquals(new Timestamp(adapter.localFormat.parse("2019-01-01").getTime()), timestamp);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testRead_ValidJsonToken_SqlDateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader(null);
    reader.addToken(JsonToken.STRING, "2019-01-01");
    try {
      Date date = adapter.read(reader);
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      assertEquals(new java.sql.Date(adapter.localFormat.parse("2019-01-01").getTime()), sqlDate);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}