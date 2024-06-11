package com.google.gson.internal.test; // assuming a package com.google.gson.internal exists for testing purposes

import static com.google.gson.stream.JsonReader.JUNK_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstruction() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
    assertNull(adapter.enUsFormat);
    assertNull(adapter.localFormat);

    adapter = new DefaultDateTypeAdapter("yyyy-MM-dd HH:mm:ss");
    assertEquals(Date.class, adapter.dateType);
    assertEquals("yyyy-MM-dd HH:mm:ss", adapter.enUsFormat.toPattern());
    assertEquals("yyyy-MM-dd HH:mm:ss", adapter.localFormat.toPattern());

    adapter = new DefaultDateTypeAdapter(Date.class, Locale.US);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.localFormat);

    adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US), adapter.enUsFormat);
    assertEquals(DateFormat.getDateInstance(DateFormat.SHORT), adapter.localFormat);

    adapter = new DefaultDateTypeAdapter(Timestamp.class);
    assertEquals(Timestamp.class, adapter.dateType);
    assertNotNull(adapter.enUsFormat);
    assertNotNull(adapter.localFormat);

    adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    assertEquals(java.sql.Date.class, adapter.dateType);
    assertNotNull(adapter.enUsFormat);
    assertNotNull(adapter.localFormat);
  }

  @Test
  public void testWrite() throws IOException {
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try (JsonWriter writer = new JsonWriter(System.out)) {
      adapter.write(writer, date);
      writer.flush();
    }

    assertEquals("Mon Jan 01 12:00:00 EST 2023", System.in.readLine().trim());
  }

  @Test
  public void testReadDate() throws IOException {
    String jsonString = "\"Mon Jan 01 12:00:00 EST 2023\"";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try (JsonReader reader = new JsonReader(new JsonParser().parse(jsonString))) {
      Date date = adapter.read(reader);
      assertEquals(jsonString, adapter.toString().trim());
      assertEquals(new Date(), date);
    }
  }

  @Test
  public void testReadDateNotNull() throws IOException {
    String jsonString = "\"Mon Jan 01 12:00:00 EST 2023\"";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try (JsonReader reader = new JsonReader(new JsonParser().parse(jsonString))) {
      JsonElement jsonElement = reader.nextElement();
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      JsonElement dateElement = jsonObject.getAsJsonPrimitive("date");
      Date date = adapter.read(reader).getAsDate();

      assertEquals(jsonString, adapter.toString().trim());
      assertEquals(dateElement.getAsString(), adapter.toString(date));
      assertEquals(new Date(), date);
    }
  }

  @Test
  public void testReadDateNull() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try (JsonReader reader = new JsonReader(new StringReader("null"))) {
      assertNull(adapter.read(reader));
    }
  }

  @Test
  public void testReadDateEmptyString() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try (JsonReader reader = new JsonReader(new StringReader(""))) {
      assertNull(adapter.read(reader));
    }
  }

  @Test(expected = JsonParseException.class)
  public void testReadDateInvalidString() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try (JsonReader reader = new JsonReader(new StringReader("invalid string"))) {
      adapter.read(reader);
    }
  }

  @Test
  public void testReadTimestamp() throws IOException {
    String jsonString = "\"Mon Jan 01 12:00:01.123 EST 2023\"";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    try (JsonReader reader = new JsonReader(new JsonParser().parse(jsonString))) {
      Timestamp timestamp = adapter.read(reader);
      assertEquals(jsonString, adapter.toString().trim());
      assertEquals(new Timestamp(1672496001123L), timestamp);
    }
  }

  @Test
  public void testReadSqlDate() throws IOException {
    String jsonString = "\"2023-01-01\"";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    try (JsonReader reader = new JsonReader(new JsonParser().parse(jsonString))) {
      java.sql.Date sqlDate = adapter.read(reader);
      assertEquals(jsonString, adapter.toString().trim());
      assertEquals(new java.sql.Date(1672486400000L), sqlDate);
    }
  }

  @Test
  public void testDeserializeToDate() throws ParseException {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String dateString = "2023-01-01T12:00:01.123Z";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    Date date = adapter.deserializeToDate(dateString);
    assertEquals(new Date(1672496001123L), date);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateInvalidString() throws IOException, ParseException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    try {
      adapter.deserializeToDate("invalid string");
    } catch (JsonParseException e) {
      throw new AssertionError();
    }

    String message = adapter.toString();
    assertTrue(message.contains("The date should be a string value"));
    assertTrue(message.contains("invalid string"));
  }
}