import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateAdapter;
  private DateFormat enUsFormatMock;
  private DateFormat localFormatMock;
  private JsonSerializationContext jsonSerializationContext;
  private Gson gson;

  @Before
  public void setUp() {
    enUsFormatMock = mock(DateFormat.class);
    localFormatMock = mock(DateFormat.class);
    when(enUsFormatMock.format(any(Date.class))).thenAnswer(i -> "2022-12-31T23:59:59Z");
    when(localFormatMock.parse("2022-12-31T23:59:59Z")).thenReturn(new Date(1671343999000L));

    dateAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormatMock, localFormatMock);
    jsonSerializationContext = new GsonBuilder().setLenient().create().getAdapter(JsonSerializationContext.class);
    gson = new GsonBuilder().setLenient().create();
  }

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, defaultDateTypeAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), defaultDateTypeAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), defaultDateTypeAdapter.localFormat);
  }

  @Test
  public void testStringDateConstructor() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter("2022");
    assertEquals(Date.class, defaultDateTypeAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), defaultDateTypeAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), defaultDateTypeAdapter.localFormat);
  }

  @Test
  public void testIntDateStyleConstructor() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT);
    assertEquals(Date.class, defaultDateTypeAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT, Locale.US), defaultDateTypeAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT), defaultDateTypeAdapter.localFormat);
  }

  @Test
  public void testIntDateStyleTimeStyleConstructor() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, defaultDateTypeAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT, Locale.US), defaultDateTypeAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT), defaultDateTypeAdapter.localFormat);
  }

  @Test
  public void testNullConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter(null));
  }

  @Test
  public void testInvalidConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter(Integer.class));
  }

  @Test
  public void testWrite() throws IOException {
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    Date date = new Date(1671343999000L);
    dateAdapter.write(jsonWriter, date);
    assertEquals("\"2022-12-31T23:59:59Z\"", jsonWriter.toString());
  }

  @Test
  public void testRead() throws IOException {
    String jsonString = "{\"value\":\"2022-12-31T23:59:59Z\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonObject();
    Date date = dateAdapter.read(jsonReaderMock(jsonElement));
    assertEquals(new Date(1671343999000L), date);
  }

  @Test
  public void testReadNull() throws IOException {
    JsonReader jsonReader = jsonReaderMock(null);
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test
  public void testReadInvalidJson() throws IOException {
    String jsonString = "{\"value\":2022-12-31T23:59:59Z}";
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonObject();
    JsonReader jsonReader = jsonReaderMock(jsonElement);
    assertThrows(JsonParseException.class, () -> dateAdapter.read(jsonReader));
  }

  @Test
  public void testReadEmptyJson() throws IOException {
    JsonReader jsonReader = jsonReaderMock(JsonParser.parseString("").getAsJsonObject());
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test
  public void testReadEmptyString() throws IOException {
    JsonReader jsonReader = jsonReaderMock(JsonParser.parseString("").createReader());
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test
  public void testReadUnsupportedJsonToken() throws IOException {
    JsonReader jsonReader = jsonReaderMock(JsonParser.parseString("{\"value\":5}").createReader());
    assertThrows(IllegalStateException.class, () -> dateAdapter.read(jsonReader));
  }

  @Test
  public void testReadInvalidDateFormat() throws IOException, ParseException {
    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, format, format);
    String jsonString = "{\"value\":\"31-12-2022 23:59:59\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonObject();
    JsonReader jsonReader = jsonReaderMock(jsonElement);
    assertThrows(JsonParseException.class, () -> defaultDateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testReadUnsupportedDateType() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(String.class, localFormatMock, localFormatMock);
    String jsonString = "{\"value\":\"2022-12-31T23:59:59Z\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonObject();
    JsonReader jsonReader = jsonReaderMock(jsonElement);
    assertThrows(AssertionError.class, () -> defaultDateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testReadDeserializeToDate() throws IOException, ParseException {
    assertEquals(new Date(1671343999000L), dateAdapter.deserializeToDate("2022-12-31T23:59:59Z"));
  }

  @Test
  public void testReadDeserializeToDateWithInvalidFormat() {
    assertThrows(JsonSyntaxException.class, () -> dateAdapter.deserializeToDate("2022-12-31T23:59:59abc"));
  }

  @Test
  public void testWriteToString() {
    assertEquals("DefaultDateTypeAdapter(java.text.DateFormat@4e96d9b8, java.text.DateFormat@3f4a828b)", dateAdapter.toString());
  }

  @Test
  public void testWriteToJson() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("adapter", jsonSerializationContext.serialize(dateAdapter));
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    gson.toJson(jsonObject, jsonWriter);
    assertEquals("{\"adapter\":{\"className\":\"" + DefaultDateTypeAdapter.class.getSimpleName() + "\",\"enUsFormat\":{\"class\":\"java.text.DateFormat\",\"format\":\"2022-12-31T23:59:59Z\"},\"localFormat\":{\"class\":\"java.text.DateFormat\",\"format\":\"2022-12-31T23:59:59Z\"}}}", jsonWriter.toString());
  }

  private JsonReader jsonReaderMock(JsonElement jsonElement) {
    return mock(JsonReader.class, mockito -> {
      when(mock.isLenient()).thenReturn(true);
      when(mock.peek()).thenReturn(JsonToken.STRING);
      when(mock.nextString()).thenReturn("2022-12-31T23:59:59Z");
    });
  }
}