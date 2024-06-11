package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
  
  @Test
  public void testWriteWithNullValue() {
    JsonWriter out = new JsonWriter(null);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.write(out, null);
      assertNull(out.getData());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWriteWithNonNullValue() {
    JsonWriter out = new JsonWriter(null);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();
    try {
      adapter.write(out, date);
      assertEquals(adapter.getEnUsFormat().format(date), out.getData());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testReadWithValidDateString() {
    JsonReader in = new JsonReader(null);
    in.setString("\"2021-01-01\"");
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      Date expectedDate = adapter.deserializeToDate("2021-01-01");
      Date date = adapter.read(in);
      assertEquals(expectedDate, date);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = JsonParseException.class)
  public void testReadWithInvalidDateString() throws JsonParseException {
    JsonReader in = new JsonReader(null);
    in.setString("\"invalid-date\"");
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(in);
  }

  @Test
  public void testReadWithDateTypeAsDate() {
    JsonReader in = new JsonReader(null);
    in.setString("\"2021-01-01\"");
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      Date expectedDate = adapter.deserializeToDate("2021-01-01");
      Date date = adapter.read(in);
      assertEquals(expectedDate, date);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testReadWithDateTypeAsTimestamp() {
    JsonReader in = new JsonReader(null);
    in.setString("\"2021-01-01\"");
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    try {
      Date expectedDate = adapter.deserializeToDate("2021-01-01");
      Timestamp timestamp = (Timestamp) adapter.read(in);
      assertEquals(new Timestamp(expectedDate.getTime()), timestamp);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testReadWithDateTypeAsSqlDate() {
    JsonReader in = new JsonReader(null);
    in.setString("\"2021-01-01\"");
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    try {
      Date expectedDate = adapter.deserializeToDate("2021-01-01");
      java.sql.Date sqlDate = (java.sql.Date) adapter.read(in);
      assertEquals(new java.sql.Date(expectedDate.getTime()), sqlDate);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}