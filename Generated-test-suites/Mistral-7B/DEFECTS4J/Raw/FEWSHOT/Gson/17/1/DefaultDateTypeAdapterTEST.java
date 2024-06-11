package com.google.gson.internal.reflect; // Assuming DefaultDateTypeAdapter is inside this package

import static com.google.gson.stream.JsonReader.NullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  @Test
  public void testDefaultDateTypeAdapter_DefaultConstructor() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    Class<?> dateType = defaultDateTypeAdapter.dateType;
    assertEquals(Date.class, dateType);

    DateFormat enUsFormat = defaultDateTypeAdapter.enUsFormat;
    assertNotNull(enUsFormat);

    DateFormat localFormat = defaultDateTypeAdapter.localFormat;
    assertNotNull(localFormat);

    assertEquals(Locale.US, enUsFormat.getLocale());
    assertEquals(Locale.US, localFormat.getLocale());
  }

  @Test
  public void testDefaultDateTypeAdapter_DatePatternConstructor() {
    String datePattern = "yyyy-MM-dd HH:mm:ss";
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);

    Class<?> dateType = defaultDateTypeAdapter.dateType;
    assertEquals(Date.class, dateType);

    DateFormat enUsFormat = defaultDateTypeAdapter.enUsFormat;
    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) enUsFormat;
    assertEquals(datePattern, simpleDateFormat.toPattern());

    DateFormat localFormat = defaultDateTypeAdapter.localFormat;
    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) localFormat;
    assertEquals(datePattern, simpleDateFormat.toPattern());
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleConstructor() {
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.SHORT;
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);

    Class<?> dateType = defaultDateTypeAdapter.dateType;
    assertEquals(Date.class, dateType);

    DateFormat enUsFormat = defaultDateTypeAdapter.enUsFormat;
    DateFormat localFormat = defaultDateTypeAdapter.localFormat;

    assertEquals(dateStyle, enUsFormat.getDateFormatSymbols().getShortMonths()[0]);
    assertEquals(timeStyle, enUsFormat.getDateFormatSymbols().getShortWeekdays()[0]);

    assertEquals(dateStyle, localFormat.getDateFormatSymbols().getShortMonths()[0]);
    assertEquals(timeStyle, localFormat.getDateFormatSymbols().getShortWeekdays()[0]);
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleIntConstructor() {
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.SHORT;
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);

    Class<?> dateType = defaultDateTypeAdapter.dateType;
    assertEquals(Date.class, dateType);

    DateFormat enUsFormat = defaultDateTypeAdapter.enUsFormat;
    DateFormat localFormat = defaultDateTypeAdapter.localFormat;

    assertEquals(dateStyle, enUsFormat.getDateFormatSymbols().getShortMonths()[0]);
    assertEquals(timeStyle, enUsFormat.getDateFormatSymbols().getShortWeekdays()[0]);

    assertEquals(dateStyle, localFormat.getDateFormatSymbols().getShortMonths()[0]);
    assertEquals(timeStyle, localFormat.getDateFormatSymbols().getShortWeekdays()[0]);
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleLocalFormatConstructor() {
    DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, null, localFormat);

    Class<?> dateType = defaultDateTypeAdapter.dateType;
    assertEquals(Date.class, dateType);

    DateFormat enUsFormat = defaultDateTypeAdapter.enUsFormat;
    assertNull(enUsFormat);

    DateFormat localFormatTest = defaultDateTypeAdapter.localFormat;
    assertEquals(localFormat, localFormatTest);
  }

  @Test
  public void testDefaultDateTypeAdapter_Serialization() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateDateTypeAdapter(Date.class);
    Date date = new Date();

    String json = "{\"date\":\"2022-12-31 11:59:59\"}";
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("date", jsonObject.keySet().iterator().next());

    Date deserializedDate = defaultDateTypeAdapter.read(new JsonReader(new StringReader(json)));
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Deserialization_Null() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    assertNull(defaultDateTypeAdapter.read(new JsonReader(new StringReader("null"))));
  }

  @Test
  public void testDefaultDateTypeAdapter_Deserialization_InvalidFormat() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    String json = "{\"date\":\"invalid\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    try {
      defaultDateTypeAdapter.read(jsonReader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapter_Deserialization_ISO8601() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    String json = "{\"date\":\"2022-12-31T23:59:59Z\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    Date deserializedDate = defaultDateTypeAdapter.read(jsonReader);
    Date expectedDate = new Date(ISO8601Utils.parse("2022-12-31T23:59:59Z").getTime());
    assertEquals(expectedDate, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Serialization_Timestamp() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    String json = "{\"timestamp\":\"2022-12-31 11:59:59.123456\"}";
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("timestamp", jsonObject.keySet().iterator().next());

    Timestamp deserializedTimestamp = defaultDateTypeAdapter.read(new JsonReader(new StringReader(json)));
    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testDefaultDateTypeAdapter_Serialization_SQLDate() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

    String json = "{\"date\":\"2022-12-31\"}";
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("date", jsonObject.keySet().iterator().next());

    java.sql.Date deserializedDate = defaultDateTypeAdapter.read(new JsonReader(new StringReader(json)));
    assertEquals(sqlDate, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_ToString() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    StringBuilder sb = defaultDateTypeAdapter.toString();
    String expectedName = SIMPLE_NAME + "(Date)";

    assertTrue(sb.toString().startsWith(expectedName));
  }

  @Test
  public void testDefaultDateTypeAdapter_ToString_CustomEnUsFormat() {
    String datePattern = "dd-MM-yyyy HH:mm:ss";
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);

    StringBuilder sb = defaultDateTypeAdapter.toString();
    String expectedName =
        SIMPLE_NAME + "(Date, " + SimpleDateFormat.class.getSimpleName() + "(\"" + datePattern + "\", US_locale))";

    assertTrue(sb.toString().startsWith(expectedName));
  }

  @Test
  public void testDefaultDateTypeAdapter_ToString_CustomLocalFormat() {
    DateFormat localFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, null, localFormat);

    StringBuilder sb = defaultDateTypeAdapter.toString();
    String expectedName = SIMPLE_NAME + "(Date, " + DateFormat.class.getSimpleName() + "(\"" + localFormat.toPattern() + "\"))";

    assertTrue(sb.toString().startsWith(expectedName));
  }
}