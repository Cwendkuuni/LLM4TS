import static org.junit.Assert.*;
import static com.google.gson.stream.JsonReader.JUNK_STRING;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonToken;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructionWithDateClass() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(Date.class)", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructionWithDateStringPattern() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss");
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat[dd-MM-yyyy HH:mm:ss])", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructionWithDateStyles() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals("DefaultDateTypeAdapter(Date.class, DateFormat[Short date, Short time])", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructionWithDateStylesAndDatePattern() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT, "dd-MM-yyyy HH:mm:ss");
    assertEquals("DefaultDateTypeAdapter(Date.class, DateFormat[Short date, Short time], SimpleDateFormat[dd-MM-yyyy HH:mm:ss])", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteNullDate() throws IOException {
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    dateAdapter.write(writer, null);
    writer.flush();

    assertEquals("null", writer.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteDate() throws IOException {
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();
    dateAdapter.write(writer, date);
    writer.flush();

    String jsonString = writer.toString();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    Date jsonDate = dateFormat.parse(jsonString);
    assertEquals(date.getTime(), jsonDate.getTime());
  }

  @Test
  public void testDefaultDateTypeAdapterReadNullJsonString() throws IOException {
    Gson gson = new Gson();
    JsonReader reader = new JsonReader(new StringReader(JUNK_STRING));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    try {
      Date date = dateAdapter.read(reader);
      fail("Should have thrown JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapterReadJsonStringWithDate() throws IOException {
    Gson gson = new Gson();
    Date date = new Date();
    String jsonString = gson.toJson(date);
    JsonReader reader = new JsonReader(new StringReader(jsonString));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date readDate = dateAdapter.read(reader);
    assertEquals(date.getTime(), readDate.getTime());
  }

  @Test
  public void testDefaultDateTypeAdapterConvertDateToTimestamp() throws IOException, ParseException {
    Gson gson = new Gson();
    Date date = new Date();
    String jsonString = gson.toJson(date);
    JsonReader reader = new JsonReader(new StringReader(jsonString));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);

    Timestamp timestamp = dateAdapter.read(reader);
    Date readDate = dateAdapter.read(reader);
    assertEquals(date.getTime(), readDate.getTime());
    assertEquals(timestamp.getTime(), date.getTime());
  }

  @Test
  public void testDefaultDateTypeAdapterConvertDateToSqlDate() throws IOException, ParseException, SQLException {
    Gson gson = new Gson();
    Date date = new Date();
    String jsonString = gson.toJson(date);
    JsonReader reader = new JsonReader(new StringReader(jsonString));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    java.sql.Date sqlDate = dateAdapter.read(reader);
    Date readDate = dateAdapter.read(reader);
    assertEquals(date.getTime(), readDate.getTime());
    assertEquals(sqlDate.getTime(), date.getTime());
  }

  @Test(expected = AssertionError.class)
  public void testDefaultDateTypeAdapterInvalidDateType() throws IOException {
    Gson gson = new Gson();
    String jsonString = "{\"type\":\"InvalidType\"}";
    JsonReader reader = new JsonReader(new StringReader(jsonString));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Integer.class);

    dateAdapter.read(reader);
  }

  @Test
  public void testDefaultDateTypeAdapterParseInvalidJsonString() throws IOException, ParseException {
    Gson gson = new Gson();
    String jsonString = "{\"type\":\"Date\",\"invalidJson\":\"\"}";
    JsonReader reader = new JsonReader(new StringReader(jsonString));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    try {
      Date date = dateAdapter.read(reader);
      fail("Should have thrown JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      assertTrue(e.getMessage().contains("invalidJson"));
    }
  }
}