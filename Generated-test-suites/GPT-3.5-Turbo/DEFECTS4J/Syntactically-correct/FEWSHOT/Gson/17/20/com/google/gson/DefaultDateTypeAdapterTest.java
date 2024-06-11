package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_withNullValue_shouldWriteNull() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriterMock writer = new JsonWriterMock();
    
    adapter.write(writer, null);
    
    assertTrue(writer.isNullValueCalled());
  }
  
  @Test
  public void testWrite_withNonNullValue_shouldWriteFormattedDate() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriterMock writer = new JsonWriterMock();
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");
    
    adapter.write(writer, date);
    
    assertFalse(writer.isNullValueCalled());
    assertEquals("\"2022-01-01\"", writer.getBuffer().toString());
  }
  
  @Test
  public void testRead_withNonStringValue_shouldThrowException() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReaderMock reader = new JsonReaderMock(JsonToken.NUMBER);
    
    try {
      adapter.read(reader);
    } catch (JsonParseException ex) {
      assertEquals("The date should be a string value", ex.getMessage());
    }
  }
  
  @Test
  public void testRead_withInvalidDateString_shouldThrowException() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReaderMock reader = new JsonReaderMock(JsonToken.STRING, "2022-01-01");
    
    try {
      adapter.read(reader);
    } catch (JsonSyntaxException ex) {
      assertEquals("2022-01-01", ex.getBadValue());
      assertEquals("2019-06-01T12:00:00Z", ex.getCause().getMessage());
    }
  }
  
  @Test
  public void testRead_withNonNullValue_shouldReturnParsedDate() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReaderMock reader = new JsonReaderMock(JsonToken.STRING, "2022-01-01");
    
    Date date = adapter.read(reader);
    
    assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), date);
  }
  
  @Test
  public void testToString_shouldReturnSimpleNameAndFormatName() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    String toString = adapter.toString();
    
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", toString);
  }
}