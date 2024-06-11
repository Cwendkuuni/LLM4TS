import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader.Closeable;
import com.google.gson.stream.JsonWriter.CloseWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() {
    // Test default constructor
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
    assertNotNull(adapter.enUsFormat);
    assertNotNull(adapter.localFormat);

    // Test constructor with DatePattern
    adapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd HH:mm:ss");
    assertEquals(Date.class, adapter.dateType);
    assertNotNull(adapter.enUsFormat);
    assertNotNull(adapter.localFormat);
    assertEquals("yyyy-MM-dd HH:mm:ss", adapter.enUsFormat.toPattern());
    assertEquals("yyyy-MM-dd HH:mm:ss", adapter.localFormat.toPattern());

    // Test constructor with dateStyle and timeStyle
    adapter = new DefaultDateTypeAdapter(new int[] { DateFormat.SHORT, DateFormat.SHORT });
    assertEquals(Date.class, adapter.dateType);
    assertNotNull(adapter.enUsFormat);
    assertNotNull(adapter.localFormat);

    // Test write method
    Gson gson = new Gson();
    JsonWriter writer = mock(JsonWriter.class);
    when(writer.value(anyString())).thenAnswer(i -> writer);

    Date date = new Date();
    adapter.write(writer, date);

    String expectedJsonString = adapter.enUsFormat.format(date);
    verify(writer).value(expectedJsonString);

    // Test read method
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("2022-12-31 23:59:59");

    Date actualDate = adapter.read(reader);

    Date expectedDate = new Date(1672492799000L);
    assertEquals(expectedDate, actualDate);

    // Test deserializeToDate method with valid date strings
    String dateString1 = "2022-12-31 23:59:59";
    String dateString2 = "2022-12-31";
    String dateString3 = "2022-12-31T23:59:59";

    Date actualDate1 = adapter.deserializeToDate(dateString1);
    Date actualDate2 = adapter.deserializeToDate(dateString2);
    Date actualDate3 = adapter.deserializeToDate(dateString3);

    Date expectedDate1 = new Date(1672492799000L);
    Date expectedDate2 = new Date(1672476800000L);
    Date expectedDate3 = new Date(1672492799000L);

    assertEquals(expectedDate1, actualDate1);
    assertEquals(expectedDate2, actualDate2);
    assertEquals(expectedDate3, actualDate3);

    // Test deserializeToDate method with invalid date strings
    String invalidDateString1 = "2022-12-32";
    String invalidDateString2 = "2022-12-31 25:59:59";

    try {
      adapter.deserializeToDate(invalidDateString1);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      adapter.deserializeToDate(invalidDateString2);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Test deserializeToDate method with null String
    try {
      adapter.deserializeToDate(null);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testTimestampTypeAdapter() {
    // Test DefaultDateTypeAdapter with Timestamp class
    DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
    assertEquals(Timestamp.class, adapter.dateType);

    // Test write method
    Gson gson = new Gson();
    JsonWriter writer = mock(JsonWriter.class);
    when(writer.value(anyString())).thenAnswer(i -> writer);

    Timestamp timestamp = new Timestamp(1672492799000L);
    adapter.write(writer, timestamp);

    String expectedJsonString = adapter.enUsFormat.format(new Date(timestamp.getTime()));
    verify(writer).value(expectedJsonString);

    // Test read method
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("2022-12-31 23:59:59.000");

    Timestamp actualTimestamp = adapter.read(reader);

    Timestamp expectedTimestamp = new Timestamp(1672492799000L);
    assertEquals(expectedTimestamp, actualTimestamp);

    // Test deserializeToDate method with valid date strings
    String dateString1 = "2022-12-31 23:59:59.000";
    String dateString2 = "2022-12-31 23:59:59";

    Timestamp actualTimestamp1 = adapter.deserializeToDate(dateString1);
    Timestamp actualTimestamp2 = adapter.deserializeToDate(dateString2);

    Timestamp expectedTimestamp1 = new Timestamp(1672492799000L);
    Timestamp expectedTimestamp2 = new Timestamp(1672492799000L);

    assertEquals(expectedTimestamp1, actualTimestamp1);
    assertEquals(expectedTimestamp2, actualTimestamp2);

    // Test deserializeToDate method with invalid date strings
    String invalidDateString1 = "2022-12-32 00:00:00.000";
    String invalidDateString2 = "2022-12-31 25:59:59.000";

    try {
      adapter.deserializeToDate(invalidDateString1);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      adapter.deserializeToDate(invalidDateString2);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Test deserializeToDate method with null String
    try {
      adapter.deserializeToDate(null);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testSqlDateTypeAdapter() {
    // Test DefaultDateTypeAdapter with java.sql.Date class
    DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
    assertEquals(java.sql.Date.class, adapter.dateType);

    // Test write method
    Gson gson = new Gson();
    JsonWriter writer = mock(JsonWriter.class);
    when(writer.value(anyString())).thenAnswer(i -> writer);

    java.sql.Date sqlDate = new java.sql.Date(1672492799000L);
    adapter.write(writer, sqlDate);

    String expectedJsonString = adapter.enUsFormat.format(sqlDate);
    verify(writer).value(expectedJsonString);

    // Test read method
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("2022-12-31");

    java.sql.Date actualSqlDate = adapter.read(reader);

    java.sql.Date expectedSqlDate = new java.sql.Date(1672476800000L);
    assertEquals(expectedSqlDate, actualSqlDate);

    // Test deserializeToDate method with valid date strings
    String dateString1 = "2022-12-31";
    String dateString2 = "2022-12-31 23:59:59";

    java.sql.Date actualSqlDate1 = adapter.deserializeToDate(dateString1);
    java.sql.Date actualSqlDate2 = adapter.deserializeToDate(dateString2);

    java.sql.Date expectedSqlDate1 = new java.sql.Date(1672476800000L);
    java.sql.Date expectedSqlDate2 = new java.sql.Date(1672492799000L);

    assertEquals(expectedSqlDate1, actualSqlDate1);
    assertEquals(expectedSqlDate2, actualSqlDate2);

    // Test deserializeToDate method with invalid date strings
    String invalidDateString1 = "2022-12-32";
    String invalidDateString2 = "2022-12-31 25:59:59";

    try {
      adapter.deserializeToDate(invalidDateString1);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      adapter.deserializeToDate(invalidDateString2);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Test deserializeToDate method with null String
    try {
      adapter.deserializeToDate(null);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }
}