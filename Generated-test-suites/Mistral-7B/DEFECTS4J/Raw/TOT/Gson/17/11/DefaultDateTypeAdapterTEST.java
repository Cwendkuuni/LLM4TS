package com.google.gson.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.google.gson.adapter.DefaultDateTypeAdapter;

public class DefaultDateTypeAdapterTest {

  private static final Class<DefaultDateTypeAdapter> DEFAULT_DATE_TYPE_ADAPTER = DefaultDateTypeAdapter.class;

  @Test
  public void testDefaultDateTypeAdapter_DefaultConstructor() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateTypeAdapter.dateType);
    assertNull(dateTypeAdapter.enUsFormat);
    assertNull(dateTypeAdapter.localFormat);
    assertEquals(Date.class, dateTypeAdapter.read(new JsonReader(new StringReader("\"2022-12-31\""))).getClass());
  }

  @Test
  public void testDefaultDateTypeAdapter_StringDatePatternConstructor() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter("dd-MM-yyyy", Date.class);
    assertEquals(Date.class, dateTypeAdapter.dateType);
    assertNotNull(dateTypeAdapter.enUsFormat);
    assertNotNull(dateTypeAdapter.localFormat);
    assertEquals("31-12-2022", dateTypeAdapter.enUsFormat.format(new Date()));
    assertEquals("31-12-2022", dateTypeAdapter.localFormat.format(new Date()));
    assertEquals(new Date(System.currentTimeMillis()), dateTypeAdapter.read(new JsonReader(new StringReader("\"31-12-2022\""))));
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleAndTimeStyleConstructor() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT);
    assertEquals(Date.class, dateTypeAdapter.dateType);
    assertNotNull(dateTypeAdapter.enUsFormat);
    assertNotNull(dateTypeAdapter.localFormat);
    assertEquals("Dec 31, 2022", dateTypeAdapter.enUsFormat.format(new Date()));
    assertEquals("Dec 31, 2022", dateTypeAdapter.localFormat.format(new Date()));
    assertEquals(new Date(System.currentTimeMillis()), dateTypeAdapter.read(new JsonReader(new StringReader("\"Dec 31, 2022\""))));
  }

  @Test
  public void testDefaultDateTypeAdapter_CustomFormatterConstructor() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, new DateFormat() {
      @Override
      public String format(Date date) {
        return "Custom Format " + date.getTime();
      }

      @Override
      public Date parse(String source) throws ParseException {
        return new Date(Long.parseLong(source.substring(12).replaceAll("[^0-9]", "")));
      }
    });
    assertEquals(Date.class, dateTypeAdapter.dateType);
    assertNotNull(dateTypeAdapter.enUsFormat);
    assertNotNull(dateTypeAdapter.localFormat);
    assertEquals("Custom Format " + System.currentTimeMillis(), dateTypeAdapter.enUsFormat.format(new Date()));
    assertEquals("Custom Format " + System.currentTimeMillis(), dateTypeAdapter.localFormat.format(new Date()));
    assertEquals(new Date(System.currentTimeMillis()), dateTypeAdapter.read(new JsonReader(new StringReader("\"Custom Format 1672502184000\"\""))));
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteNullValue() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    dateTypeAdapter.write(jsonWriter, null);
    assertEquals("null", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteDate() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    Date date = new Date();
    dateTypeAdapter.write(jsonWriter, date);
    assertEquals("\"2022-12-31T15:15:15.000Z\"", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadDate() throws IOException, JsonParseException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonElement jsonElement = new JsonParser().parse("{\"date\":\"2022-12-31T15:15:15.000Z\"}");
    Date date = dateTypeAdapter.read(new JsonReader(new StringReader(jsonElement.getAsJsonObject().get("date").getAsString()))).getTime();
    assertEquals(new Date(System.currentTimeMillis()), new Date(date.getTime()));
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_InvalidStringFormat() throws IOException, JsonSyntaxException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    dateTypeAdapter.read(new JsonReader(new StringReader("InvalidStringFormat")));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_InvalidDateFormat() throws IOException, JsonParseException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    dateTypeAdapter.read(new JsonReader(new StringReader("2022-12-32T15:15:15.000Z")));
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadNullValue() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(null, dateTypeAdapter.read(new JsonReader(new StringReader("null"))));
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteAndReadTimestamp() throws SQLException, IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    Date date = new Date();
    java.sql.Timestamp timestamp = new Timestamp(date.getTime());
    dateTypeAdapter.write(jsonWriter, timestamp);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    assertEquals(timestamp, dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteAndReadSqlDate() throws SQLException, IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    dateTypeAdapter.write(jsonWriter, sqlDate);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    assertEquals(sqlDate, dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadInvalidJsonFormat() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertThrows(JsonSyntaxException.class, () -> dateTypeAdapter.read(new JsonReader(new StringReader("InvalidJsonFormat"))));
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteAndReadZonedDateTime() throws JsonParseException, IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(ZonedDateTime.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault());
    dateTypeAdapter.write(jsonWriter, zonedDateTime);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    assertEquals(zonedDateTime, dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteAndReadOffsetDateTime() throws JsonParseException, IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(OffsetDateTime.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    dateTypeAdapter.write(jsonWriter, offsetDateTime);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    assertEquals(offsetDateTime, dateTypeAdapter.read(jsonReader));
  }
}