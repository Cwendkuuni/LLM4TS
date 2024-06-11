package com.google.gson.internal.bind.adapter;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() {
    // Test default constructor
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, defaultDateTypeAdapter.dateType);
    assertNotNull(defaultDateTypeAdapter.enUsFormat);
    assertNotNull(defaultDateTypeAdapter.localFormat);

    // Test constructor with DatePattern
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
    DefaultDateTypeAdapter datePatternAdapter = new DefaultDateTypeAdapter(Date.class, dateFormat.toPattern());
    assertEquals(Date.class, datePatternAdapter.dateType);
    assertEquals(dateFormat, datePatternAdapter.enUsFormat);
    assertEquals(dateFormat, datePatternAdapter.localFormat);

    // Test constructor with dateStyle and timeStyle
    DefaultDateTypeAdapter dateStyleAdapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.SHORT);
    assertEquals(Date.class, dateStyleAdapter.dateType);
    assertEquals(DateFormat.getDateInstance(Date.DATE, Locale.US), dateStyleAdapter.enUsFormat);
    assertEquals(DateFormat.getDateInstance(Date.DATE, Locale.US), dateStyleAdapter.localFormat);

    // Test write method
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    Date date = new Date();
    defaultDateTypeAdapter.write(jsonWriter, date);
    assertEquals("Mon Aug 08 15:21:51 EDT 2022", stringWriter.toString());

    // Test read method with Date
    StringReader stringReader = new StringReader("\"Mon Aug 08 15:21:51 EDT 2022\"");
    JsonReader jsonReader = new JsonReader(stringReader);
    JsonElement jsonElement = JsonParser.parseReader(jsonReader);
    Date readDate = defaultDateTypeAdapter.read(jsonReader).getAsDate();
    assertEquals(date, readDate);

    // Test read method with Timestamp
    DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonElement jsonTimestampElement = JsonParser.parseString("\"2022-08-08 15:21:51.123\"");
    Timestamp readTimestamp = timestampAdapter.read(jsonReader).getAsJson().getAsJsonObject().get("date").getAsJsonPrimitive().getAsLong();
    assertEquals(1659853011123L, readTimestamp.getTime());

    // Test read method with java.sql.Date
    DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonElement jsonSqlDateElement = JsonParser.parseString("\"1970-01-01\"");
    java.sql.Date readSqlDate = sqlDateAdapter.read(jsonReader).getAsJson().getAsJsonObject().get("date").getAsJsonPrimitive().getAsLong();
    assertEquals(new java.sql.Date(1L), readSqlDate);

    // Test deserializeToDate method with invalid format
    try {
      DefaultDateTypeAdapter.DefaultDateTypeAdapter innerAdapter = new DefaultDateTypeAdapter.DefaultDateTypeAdapter(Date.class, DateFormat.getDateInstance(Date.DATE, Locale.US), DateFormat.getDateInstance(Date.DATE, Locale.US)) {
        @Override
        synchronized Date deserializeToDate(String s) {
          throw new RuntimeException("Should not reach this point");
        }
      };
      StringReader stringReader1 = new StringReader("\"Invalid Date Format\"");
      JsonReader jsonReader1 = new JsonReader(stringReader1);
      assertThrows(JsonSyntaxException.class, () -> innerAdapter.read(jsonReader1).getAsDate());
    } catch (JsonSyntaxException e) {
      assertEquals("Invalid Date Format", e.getMessage());
    }
  }
}