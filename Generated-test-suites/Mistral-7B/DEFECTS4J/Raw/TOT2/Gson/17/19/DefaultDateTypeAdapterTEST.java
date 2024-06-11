package com.example;

import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultDateTypeAdapterTest {

  private JsonReader jsonReader;
  private JsonWriter jsonWriter;

  @Before
  public void setUp() {
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
  }

  @Test
  public void testWriteMethod() throws IOException {
    when(jsonWriter.value(anyString())).thenReturn(this);

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();

    adapter.write(jsonWriter, date);

    verify(jsonWriter, times(1)).value(anyString());
  }

  @Test
  public void testReadMethod() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("2023-01-01");

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(jsonReader);

    assertEquals("01-Jan-2023", date.toString());
  }

  @Test(expected = IOException.class)
  public void testWriteNullInput() throws IOException {
    when(jsonWriter.value(anyString())).thenReturn(this);

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    adapter.write(jsonWriter, null);
  }

  @Test
  public void testWriteDifferentDateFormats() throws IOException {
    when(jsonWriter.value(anyString())).thenReturn(this);

    Date date = new Date(1650860800000L); // Fri Oct 22 12:00:00 UTC 2021
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    adapter.write(jsonWriter, date);

    verify(jsonWriter, times(1)).value("2021-10-22");
  }

  @Test(expected = JsonParseException.class)
  public void testReadNullInput() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidDateString() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("invalid-date-string");

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    adapter.read(jsonReader);
  }

  @Test
  public void testReadDifferentDateTypes() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("2023-01-01T12:00:00.123Z");

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = adapter.read(jsonReader);

    assertEquals("01-Jan-2023 12:00:01.123", timestamp.toString());
  }

  @Test(expected = AssertionError.class)
  public void testUnexpectedDateType() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("2023-01-01");

    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(String.class);

    adapter.read(jsonReader);
  }

  @Test
  public void testToStringMethod() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    assertEquals("DefaultDateTypeAdapter(US: DateTimeFormatter{Format Style='Medium', Locale=en_US}, US: DateTimeFormatter{Format Style='Medium', Locale=en_US})", adapter.toString());
  }
}