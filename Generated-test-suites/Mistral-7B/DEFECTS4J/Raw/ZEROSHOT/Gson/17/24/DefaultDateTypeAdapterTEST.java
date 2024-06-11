import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonToken;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;
  private DateFormat enUsFormat;
  private DateFormat localFormat;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;
  private Gson gson;

  @Before
  public void setUp() {
    enUsFormat = mock(DateFormat.class);
    localFormat = mock(DateFormat.class);
    when(enUsFormat.format(new Date())).thenReturn("2022-12-31 12:00:00");
    when(localFormat.parse("2022-12-31 12:00:00")).thenReturn(new Date());
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
    gson = new Gson();

    adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
  }

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter defaultAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, defaultAdapter.dateType);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), defaultAdapter.enUsFormat);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), defaultAdapter.localFormat);
  }

  @Test
  public void testStringConstructor() {
    DefaultDateTypeAdapter defaultAdapter = new DefaultDateTypeAdapter("DefaultDateTypeAdapter", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    assertEquals("DefaultDateTypeAdapter", defaultAdapter.getClass().getSimpleName());
    assertEquals(Date.class, defaultAdapter.dateType);
    assertSame(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), defaultAdapter.enUsFormat);
    assertSame(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), defaultAdapter.localFormat);
  }

  @Test
  public void testDateStyleIntConstructor() {
    DefaultDateTypeAdapter defaultAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVIATED, DateFormat.ABBREVIATED);
    assertEquals(Date.class, defaultAdapter.dateType);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.ABBREVIATED, DateFormat.ABBREVIATED, Locale.US), defaultAdapter.enUsFormat);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.ABBREVIATED, DateFormat.ABBREVIATED), defaultAdapter.localFormat);
  }

  @Test
  public void testDateStyleIntConstructorWithLocale() {
    DefaultDateTypeAdapter defaultAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVIATED, DateFormat.ABBREVIATED, Locale.US);
    assertEquals(Date.class, defaultAdapter.dateType);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.ABBREVIATED, DateFormat.ABBREVIATED, Locale.US), defaultAdapter.enUsFormat);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.ABBREVIATED, DateFormat.ABBREVIATED), defaultAdapter.localFormat);
  }

  @Test
  public void testWriteMethod() throws IOException {
    Date date = new Date();
    when(jsonWriter.value(anyString())).thenAnswer(invocation -> {});

    adapter.write(jsonWriter, date);

    ArgumentCaptor<String> dateFormatAsStringArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(jsonWriter).value(dateFormatAsStringArgumentCaptor.capture());
    assertEquals("2022-12-31 12:00:00", dateFormatAsStringArgumentCaptor.getValue());
  }

  @Test
  public void testReadMethodWithNullInput() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);
    assertNull(adapter.read(jsonReader));
  }

  @Test
  public void testReadMethodWithStringInput() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING).thenReturn(JsonToken.NULL);
    when(jsonReader.nextString()).thenReturn("2022-12-31 12:00:00");

    Date date = adapter.read(jsonReader);

    assertEquals(new Date(), date);
  }

  @Test(expected = JsonParseException.class)
  public void testReadMethodWithInvalidStringInput() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING).thenReturn(JsonToken.NULL);
    when(jsonReader.nextString()).thenReturn("InvalidDateString");

    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadMethodWithInvalidDateFormat() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING).thenReturn(JsonToken.NULL);
    when(jsonReader.nextString()).thenReturn("2022-12-32 12:00:00");

    adapter.read(jsonReader);
  }

  @Test
  public void testReadMethodWithJsonElementInput() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("\"2022-12-31 12:00:00\"");

    Date date = adapter.read(new JsonReader(new StringReader(jsonElement.getAsString())) {
      @Override
      public JsonElement peek() {
        return jsonElement;
      }
    });

    assertEquals(new Date(), date);
  }

  @Test
  public void testReadMethodWithTimestampInput() throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    when(jsonReader.nextInt()).thenReturn(2);
    when(jsonReader.nextLong()).thenReturn(timestamp.getTime());

    Date date = adapter.read(jsonReader);

    assertEquals(new Timestamp(timestamp.getTime()), date);
  }

  @Test
  public void testReadMethodWithSqlDateInput() throws IOException {
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

    when(jsonReader.nextInt()).thenReturn(3);
    when(jsonReader.nextLong()).thenReturn(sqlDate.getTime());

    Date date = adapter.read(jsonReader);

    assertEquals(new java.sql.Date(sqlDate.getTime()), date);
  }

  @Test
  public void testDeserializeToDateMethodWithNullInput() {
    assertNull(adapter.deserializeToDate(null));
  }

  @Test
  public void testDeserializeToDateMethodWithStringInputEnUsFormat() {
    String dateString = "2022-12-31 12:00:00";

    Date date = adapter.deserializeToDate(dateString);

    assertEquals(new Date(), date);
  }

  @Test
  public void testDeserializeToDateMethodWithStringInputLocalFormat() {
    String dateString = "2022-12-31 12:00:00";

    Date date = adapter.deserializeToDate(dateString);

    assertEquals(new Date(), date);
  }

  @Test
  public void testDeserializeToDateMethodWithStringInputISO8601Utils() {
    String dateString = "2022-12-31T12:00:00.123Z";

    Date date = adapter.deserializeToDate(dateString);

    assertEquals(new Date(1671635201123L), date);
  }
}