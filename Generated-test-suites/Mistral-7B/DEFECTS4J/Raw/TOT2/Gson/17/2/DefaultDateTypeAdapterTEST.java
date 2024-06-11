import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.JsonObject;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonToken;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import static com.google.gson.stream.JsonReader.nullJsonReader;

public class DefaultDateTypeAdapterTest {

  private static final String JSON_DATE = "{\"date\":\"2022-12-31T12:00:00Z\"}";
  private static final String JSON_TIMESTAMP = "{\"timestamp\":\"2022-12-31T12:00:00.123Z\"}";
  private static final String JSON_SQL_DATE = "{\"date\":\"2022-12-31\"}";

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    // Testing null value
    assertNull(adapter.read(nullJsonReader));
  }

  @Test
  public void testConstructorWithDateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Testing normal date
    Date date = new Date();
    Date deserializedDate = adapter.read(jsonReaderWithDate);
    assertEquals(date, deserializedDate);
    // Testing null value
    assertNull(adapter.read(nullJsonReader));
  }

  @Test
  public void testConstructorWithDatePattern() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    Date date = new Date(System.currentTimeMillis());
    Date deserializedDate = adapter.read(jsonReaderWithDate);
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testConstructorWithDateStyles() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(DateFormat.LONG, DateFormat.LONG);
    Date date = new Date();
    Date deserializedDate = adapter.read(jsonReaderWithDate);
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testWriteMethod() throws IOException {
    JsonWriter writer = Mockito.mock(JsonWriter.class);
    Date date = new Date();
    adapter.write(writer, date);
    Mockito.verify(writer).value("2022-12-31T12:00:04.218-05:00");
  }

  @Test(expected = JsonParseException.class)
  public void testReadMethodWithNullInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(nullJsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadMethodWithInvalidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReaderWithInvalidDateString);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadMethodWithEmptyString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReaderWithEmptyString);
  }

  @Test
  public void testDeserializeToDateWithValidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date deserializedDate = adapter.deserializeToDate("2022-12-31T12:00:04.218-05:00");
    Date expectedDate = new Date(System.currentTimeMillis());
    assertEquals(expectedDate, deserializedDate);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateWithInvalidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.deserializeToDate("InvalidDateString");
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateWithEmptyString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.deserializeToDate("");
  }

  @Test
  public void testDeserializeToTimestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Timestamp deserializedTimestamp = adapter.deserializeToDate(jsonReaderWithTimestamp);
    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToTimestampWithInvalidTimestampString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    adapter.deserializeToDate("InvalidTimestampString");
  }

  @Test
  public void testDeserializeToSqlDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    java.sql.Date deserializedSqlDate = adapter.deserializeToDate(jsonReaderWithSqlDate);
    assertEquals(sqlDate, deserializedSqlDate);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToSqlDateWithInvalidSqlDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    adapter.deserializeToDate("InvalidSqlDateString");
  }

  private JsonReader jsonReaderWithDate = Mockito.mock(JsonReader.class);
  private JsonReader jsonReaderWithTimestamp = Mockito.mock(JsonReader.class);
  private JsonReader jsonReaderWithInvalidDateString = Mockito.mock(JsonReader.class);
  private JsonReader jsonReaderWithEmptyString = Mockito.mock(JsonReader.class);

  static {
    when(jsonReaderWithDate.nextString()).thenReturn("2022-12-31T12:00:04.218-05:00");
    when(jsonReaderWithTimestamp.nextString()).thenReturn("2022-12-31T12:00:04.218.123Z");
    when(jsonReaderWithInvalidDateString.nextString()).thenReturn("InvalidDateString");
    when(jsonReaderWithEmptyString.nextString()).thenReturn("");
  }

}

//