import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
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
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader.CloseSilently;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  @Test
  public void testDefaultDateTypeAdapterConstruction() {
    // Test DefaultDateTypeAdapter constructor that takes no arguments
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, defaultDateTypeAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), defaultDateTypeAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), defaultDateTypeAdapter.localFormat);

    // Test DefaultDateTypeAdapter constructor that takes a datePattern string
    DefaultDateTypeAdapter defaultDateTypeAdapterWithPattern = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
    assertEquals(Date.class, defaultDateTypeAdapterWithPattern.dateType);
    assertEquals(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US), defaultDateTypeAdapterWithPattern.enUsFormat);
    assertEquals(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"), defaultDateTypeAdapterWithPattern.localFormat);

    // Test DefaultDateTypeAdapter constructor that takes a dateStyle and timeStyle
    DefaultDateTypeAdapter defaultDateTypeAdapterWithStyle = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY);
    assertEquals(Date.class, defaultDateTypeAdapterWithStyle.dateType);
    assertEquals(DateFormat.getDateInstance(Date.DATE, Locale.US), defaultDateTypeAdapterWithStyle.enUsFormat);
    assertEquals(DateFormat.getDateInstance(Date.DATE), defaultDateTypeAdapterWithStyle.localFormat);

    // Test DefaultDateTypeAdapter constructor that takes a DateFormat object for enUS and local format
    DateFormat enUsFormatMock = mock(DateFormat.class);
    DateFormat localFormatMock = mock(DateFormat.class);
    when(enUsFormatMock.getClass()).thenReturn(DateFormat.class);
    when(localFormatMock.getClass()).thenReturn(DateFormat.class);
    DefaultDateTypeAdapter defaultDateTypeAdapterWithCustomFormats = new DefaultDateTypeAdapter(Date.class, enUsFormatMock, localFormatMock);
    assertEquals(Date.class, defaultDateTypeAdapterWithCustomFormats.dateType);
    assertEquals(enUsFormatMock, defaultDateTypeAdapterWithCustomFormats.enUsFormat);
    assertEquals(localFormatMock, defaultDateTypeAdapterWithCustomFormats.localFormat);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapterWithInvalidDateType() {
    new DefaultDateTypeAdapter(String.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadNullJsonString() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    defaultDateTypeAdapter.read(jsonReader);
  }

  @Test
  public void testWriteReadDate() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();

    String jsonString = "2023-03-15T14:30:23Z";
    Date expectedDate = new Date(ISO8601Utils.parse(jsonString, new ParsePosition(0)).getTime());

    // Test write method
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(stringWriter), new GsonBuilder().setLenient().create());
    defaultDateTypeAdapter.write(jsonWriter, expectedDate);
    jsonWriter.flush();
    String actualJsonString = stringWriter.toString();
    assertEquals(jsonString, actualJsonString);

    // Test read method
    JsonElement jsonElement = new JsonParser().parse(actualJsonString);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonElement.getAsString()));
    Date actualReadDate = defaultDateTypeAdapter.read(jsonReader);
    assertEquals(expectedDate, actualReadDate);
  }

  @Test
  public void testWriteReadTimestamp() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    String jsonString = "2023-03-15T14:30:23.123Z";
    Timestamp expectedTimestamp = new Timestamp(ISO8601Utils.parse(jsonString, new ParsePosition(0)).getTime());

    // Test write method
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(stringWriter), new GsonBuilder().setLenient().create());
    defaultDateTypeAdapter.write(jsonWriter, expectedTimestamp);
    jsonWriter.flush();
    String actualJsonString = stringWriter.toString();
    assertEquals(jsonString, actualJsonString);

    // Test read method
    JsonElement jsonElement = new JsonParser().parse(actualJsonString);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonElement.getAsString()));
    Timestamp actualReadTimestamp = defaultDateTypeAdapter.read(jsonReader);
    assertEquals(expectedTimestamp, actualReadTimestamp);
  }

  @Test
  public void testWriteReadSqlDate() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

    String jsonString = "2023-03-15";
    java.sql.Date expectedSqlDate = new java.sql.Date(ISO8601Utils.parse("2023-03-15".getBytes()).getTime());

    // Test write method
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(stringWriter), new GsonBuilder().setLenient().create());
    defaultDateTypeAdapter.write(jsonWriter, expectedSqlDate);
    jsonWriter.flush();
    String actualJsonString = stringWriter.toString();
    assertEquals(jsonString, actualJsonString);

    // Test read method
    JsonElement jsonElement = new JsonParser().parse(actualJsonString);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonElement.getAsString()));
    java.sql.Date actualReadSqlDate = defaultDateTypeAdapter.read(jsonReader);
    assertEquals(expectedSqlDate, actualReadSqlDate);
  }

  @Test(expected = AssertionError.class)
  public void testReadInvalidDateString() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("invalid date"));
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    defaultDateTypeAdapter.read(jsonReader);
  }
}

class StringReader extends java.io.Reader {
  private final String string;

  public StringReader(String string) {
    this.string = string;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    return string.substring(0, len).getBytes().length;
  }

  @Override
  public void close() throws IOException {
    throw new CloseSilently();
  }
}

class StringWriter extends java.io.Writer {
  private final StringBuilder stringBuilder;

  public StringWriter() {
    this.stringBuilder = new StringBuilder();
  }

  @Override
  public void write(int c) throws IOException {
    stringBuilder.append((char) c);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    stringBuilder.append(new String(cbuf, off, len));
  }

  @Override
  public void flush() throws IOException {
  }

  @Override
  public void close() throws IOException {
  }

  public String toString() {
    return stringBuilder.toString();
  }
}