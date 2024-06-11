package com.google.gson;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullDate_WritesNullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    
    try {
      adapter.write(writer, null);
      writer.flush();
      assertEquals("null", writer.toString());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testWrite_Date_WritesFormattedDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    
    try {
      adapter.write(writer, date);
      writer.flush();
      assertNotNull(writer.toString());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testRead_InvalidJson_ThrowsJsonParseException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    
    try {
      adapter.read(reader);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testRead_ValidJson_ReturnsDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    
    try {
      reader.beginObject();
      while (reader.hasNext()) {
        String name = reader.nextName();
        if (name.equals("date")) {
          Date date = adapter.read(reader);
          assertNotNull(date);
        } else {
          reader.skipValue();
        }
      }
      reader.endObject();
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }
}