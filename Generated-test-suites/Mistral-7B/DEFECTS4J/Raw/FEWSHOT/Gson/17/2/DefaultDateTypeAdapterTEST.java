package com.google.gson.internal.bind.util; // for ISO8601Utils

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  private DateFormat enUsFormat;
  private DateFormat localFormat;
  private DateFormat isoFormat;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;
  private Gson gson;

  @Before
  public void setUp() {
    enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    jsonReader = new JsonReader(new StringBuilder());
    jsonWriter = new JsonWriter(new StringBuilder());
    gson = new Gson();
  }

  @After
  public void tearDown() {
    jsonReader = null;
    jsonWriter = null;
  }

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(Date.class, adapter.enUsFormat.getClass());
    assertEquals(Date.class, adapter.localFormat.getClass());
  }

  @Test
  public void testConstructorWithDatePattern() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd HH:mm:ss.SSS");
    assertEquals(Date.class, adapter.dateType);
    assertEquals(SimpleDateFormat.class, adapter.enUsFormat.getClass());
    assertEquals(SimpleDateFormat.class, adapter.localFormat.getClass());
    SimpleDateFormat formatter = (SimpleDateFormat) adapter.enUsFormat;
    assertEquals("yyyy-MM-dd HH:mm:ss.SSS", formatter.toPattern());
  }

  @Test
  public void testConstructorWithDateStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Date.DATE);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.class, adapter.enUsFormat.getClass());
    assertEquals(DateFormat.class, adapter.localFormat.getClass());
    DateFormat formatter = (DateFormat) adapter.enUsFormat;
    assertEquals(DateFormat.DATE, formatter.getCalendar().getMinimum(Calendar.DAY_OF_MONTH));
  }

  @Test
  public void testConstructorWithDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.class, adapter.enUsFormat.getClass());
    assertEquals(DateFormat.class, adapter.localFormat.getClass());
    DateFormat formatter = (DateFormat) adapter.enUsFormat;
    assertEquals(DateFormat.DATE, formatter.getCalendar().getMinimum(Calendar.DAY_OF_MONTH));
    assertEquals(DateFormat.HOUR_OF_DAY, formatter.getCalendar().getMinimum(Calendar.HOUR_OF_DAY));
  }

  @Test
  public void testWriteNull() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.beginObject();
    jsonWriter.name("date");
    adapterWriteDate(jsonWriter, null);
    jsonWriter.endObject();
    String jsonString = jsonWriter.toString();
    assertEquals("{\"date\":null}", jsonString);
  }

  @Test
  public void testWriteEmptyString() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.beginObject();
    jsonWriter.name("date");
    adapterWriteDate(jsonWriter, "");
    jsonWriter.endObject();
    String jsonString = jsonWriter.toString();
    assertEquals("{\"date\":null}", jsonString);
  }

  @Test
  public void testWriteEmptyDate() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.beginObject();
    jsonWriter.name("date");
    adapterWriteDate(jsonWriter, new Date(0));
    jsonWriter.endObject();
    String jsonString = jsonWriter.toString();
    assertEquals("{\"date\":\"\"}", jsonString);
  }

  @Test
  public void testWriteLocalDate() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.beginObject();
    jsonWriter.name("date");
    adapterWriteDate(jsonWriter, new Date(System.currentTimeMillis()));
    jsonWriter.endObject();
    String jsonString = jsonWriter.toString();
    assertEquals("{\"date\":\"" + isoFormat.format(new Date(System.currenttimeMillis())) + "\"}", jsonString);
  }

  @Test
  public void testWriteTimestamp() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.beginObject();
    jsonWriter.name("date");
    adapterWriteDate(jsonWriter, new Timestamp(System.currentTimeMillis()));
    jsonWriter.endObject();
    String jsonString = jsonWriter.toString();
    assertEquals("{\"date\":\"" + isoFormat.format(new Date(System.currentTimeMillis())) + "Z\"}", jsonString);
  }

  @Test
  public void testWriteSqlDate() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.beginObject();
    jsonWriter.name("date");
    adapterWriteDate(jsonWriter, new java.sql.Date(System.currentTimeMillis()));
    jsonWriter.endObject();
    String jsonString = jsonWriter.toString();
    assertEquals("{\"date\":\"" + isoFormat.format(new Date(System.currentTimeMillis())) + "\"}", jsonString);
  }

  @Test
  public void testReadNull() throws IOException {
    jsonReader.setLenient(true);
    jsonReader.parse("{\"date\":null}");
    assertNull(adapterReadDate(jsonReader));
  }

  @Test
  public void testReadEmptyJsonString() throws IOException {
    jsonReader.setLenient(true);
    jsonReader.parse("{\"date\":\"\"}");
    assertNull(adapterReadDate(jsonReader));
  }

  @Test
  public void testReadEmptyString() throws IOException {
    jsonReader.setLenient(true);
    jsonReader.parse("{\"date\":\"\"}");
    assertNull(adapterReadDate(jsonReader));
  }

  @Test
  public void testReadLocalDate() throws IOException {
    String jsonString = "{\"date\":\"" + isoFormat.format(new Date(System.currentTimeMillis())) + "\"}";
    jsonReader.setLenient(true);
    jsonReader.parse(jsonString);
    Date date = adapterReadDate(jsonReader);
    assertEquals(new Date(System.currentTimeMillis()), date);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    String jsonString = "{\"date\":\"" + isoFormat.format(new Date(System.currentTimeMillis())) + "Z\"}";
    jsonReader.setLenient(true);
    jsonReader.parse(jsonString);
    Date date = adapterReadDate(jsonReader);
    assertEquals(new Timestamp(System.currentTimeMillis()), date);
  }

  @Test
  public void testReadSqlDate() throws IOException {
    String jsonString = "{\"date\":\"" + isoFormat.format(new Date(System.currenttimeMillis())) + "\"}";
    jsonReader.setLenient(true);
    jsonReader.parse(jsonString);
    Date date = adapterReadDate(jsonReader);
    assertEquals(new java.sql.Date(System.currenttimeMillis()), date);
  }

  @Test
  public void testReadDateTimeWithTimeZoneOffset() throws IOException {
    String jsonString = "{\"date\":\"2022-12-11T12:34:56.789-05:00\"}";
    jsonReader.setLenient(true);
    jsonReader.parse(jsonString);
    Date date = adapterReadDate(jsonReader);
    Date expectedDate = new Date(System.currentTimeMillis() - 5 * 60 * 60 * 1000);
    assertEquals(expectedDate, date);
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidString() throws IOException {
    jsonReader.setLenient(true);
    jsonReader.parse("{\"date\":\"invalid\"}");
    adapterReadDate(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadParseException() throws IOException {
    jsonReader.setLenient(true);
    jsonReader.parse("{\"date\":\"2022-12-11T12:34:56\"}");
    adapterReadDate(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeWithInvalidFormat() throws IOException {
    String jsonString = "{\"date\":\"invalidFormat\"}";
    jsonReader.setLenient(true);
    jsonReader.parse(jsonString);
    adapterReadDate(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeWithNullFormat() throws IOException {
    String jsonString = "{\"date\":null}";
    jsonReader.setLenient(true);
    jsonReader.parse(jsonString);
    adapterReadDate(jsonReader);
  }

  private void adapterWriteDate(JsonWriter writer, Date date) throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    writer.beginObject();
    writer.name("date");
    adapter.write(writer, date);
    writer.endObject();
  }

  private Date adapterReadDate(JsonReader reader) throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonElement jsonElement = reader.nextElement();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    return adapter.read(jsonReader);
  }
}