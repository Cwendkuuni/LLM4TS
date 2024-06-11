package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DefaultDateTypeAdapterTest {

  @Mock
  private JsonWriter mockJsonWriter;

  @Mock
  private JsonReader mockJsonReader;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testWriteWithNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(mockJsonWriter, null);
    verify(mockJsonWriter).nullValue();
  }

  @Test
  public void testWriteWithValidDate() throws IOException, ParseException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date validDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
    adapter.write(mockJsonWriter, validDate);

    verify(mockJsonWriter).value("Jan 1, 2020");
  }

  @Test(expected = IOException.class)
  public void testWriteWithIOException() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    doThrow(new IOException()).when(mockJsonWriter).value(anyString());

    adapter.write(mockJsonWriter, new Date());
  }

  @Test(expected = JsonParseException.class)
  public void testReadWithNonStringValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(mockJsonReader.peek()).thenReturn(JsonToken.NUMBER);

    adapter.read(mockJsonReader);
  }

  @Test
  public void testReadWithNullStringValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(mockJsonReader.peek()).thenReturn(JsonToken.STRING);
    when(mockJsonReader.nextString()).thenReturn(null);

    Date date = adapter.read(mockJsonReader);
    assertNull(date);
  }

  @Test
  public void testReadWithValidDateString() throws IOException, ParseException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(mockJsonReader.peek()).thenReturn(JsonToken.STRING);
    when(mockJsonReader.nextString()).thenReturn("Jan 1, 2020");

    Date date = adapter.read(mockJsonReader);

    assertNotNull(date);
    DateFormat expectedFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
    assertEquals(expectedFormat.parse("Jan 1, 2020"), date);
  }

  @Test(expected = JsonParseException.class)
  public void testReadWithInvalidDateString() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(mockJsonReader.peek()).thenReturn(JsonToken.STRING);
    when(mockJsonReader.nextString()).thenReturn("Invalid Date");

    adapter.read(mockJsonReader);
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}