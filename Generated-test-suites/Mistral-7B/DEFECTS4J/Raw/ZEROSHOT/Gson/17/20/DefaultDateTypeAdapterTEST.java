import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  @org.junit.Test
  public void testDefaultDateTypeAdapter() {
    // Test empty constructor
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    // Test constructor with DateFormat
    DateFormat dateFormat = mock(DateFormat.class);
    when(dateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT)).thenReturn(dateFormat);
    DefaultDateTypeAdapter dateTimeInstanceDefaultDateDefaultLocaleAdapter = new DefaultDateTypeAdapter(Date.class, dateFormat);

    // Test constructor with String datePattern
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    DefaultDateTypeAdapter datePatternAdapter = new DefaultDateTypeAdapter(Date.class, simpleDateFormat.toPattern());

    // Test constructor with int dateStyle and int timeStyle
    DefaultDateTypeAdapter dateStyleTimeStyleAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY);

    // Test constructor with int dateStyle, int timeStyle and Locale
    DefaultDateTypeAdapter dateStyleTimeStyleLocaleAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY, Locale.US);

    // Test constructor with Class<? extends Date> dateType, DateFormat enUsFormat and DateFormat localFormat
    DateFormat enUsFormat = mock(DateFormat.class);
    when(enUsFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US)).thenReturn(enUsFormat);
    DateFormat localFormat = mock(DateFormat.class);
    when(localFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT)).thenReturn(localFormat);
    DefaultDateTypeAdapter constructorWithParamsAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
  }

  @org.junit.Test
  public void testWrite() throws IOException {
    Gson gson = new Gson();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    Date date = new Date();
    defaultDateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    String jsonString = System.out.getBytes().toString();
    assertEquals("\"[date]\"" + System.lineSeparator(), jsonString);

    jsonWriter = new JsonWriter(new OutputStreamWriter(System.out));
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    date = new Date();
    defaultDateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    jsonString = System.out.getBytes().toString();
    assertEquals("\"2023-02-28T19:37:18.012Z\"" + System.lineSeparator(), jsonString);
  }

  @org.junit.Test
  public void testRead() throws IOException {
    Gson gson = new Gson();
    JsonReader jsonReader = new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/testData.json")));
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    JsonElement jsonElement = jsonReader.peek();

    Date date = defaultDateTypeAdapter.read(jsonReader);
    assertNotNull(date);
    assertEquals(new Date(), date);

    jsonElement = jsonReader.peek();
    jsonReader.nextString(); // skip "date":
    jsonElement = jsonReader.peek();
    String jsonDateString = jsonElement.getAsString();
    Date parsedDate = defaultDateTypeAdapter.read(jsonReader);
    assertEquals(new Date(System.currentTimeMillis()), parsedDate);

    jsonElement = jsonReader.peek();
    jsonReader.nextString(); // skip "dateTime":
    jsonElement = jsonReader.peek();
    jsonDateString = jsonElement.getAsString();
    Date parsedDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(jsonDateString);
    Date parsedParsedDateTime = defaultDateTypeAdapter.read(jsonReader);
    assertEquals(parsedDateTime, parsedParsedDateTime);
  }

  @org.junit.Test(expected = JsonParseException.class)
  public void testReadInvalidString() throws IOException {
    Gson gson = new Gson();
    JsonReader jsonReader = new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/testDataInvalidString.json")));
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    defaultDateTypeAdapter.read(jsonReader);
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testReadInvalidJsonString() throws IOException {
    Gson gson = new Gson();
    JsonReader jsonReader = new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/testDataInvalidJsonString.json")));
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    defaultDateTypeAdapter.read(jsonReader);
  }

  @org.junit.Test(expected = JsonParseException.class)
  public void testReadNullString() throws IOException {
    Gson gson = new Gson();
    JsonReader jsonReader = new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/testDataNullString.json")));
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    defaultDateTypeAdapter.read(jsonReader);
  }

  @org.junit.Test
  public void testToString() {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(SIMPLE_NAME + "(class java.util.Date, java.text.DateFormat[US/en/dd/MM/yyyy HH:mm:ss zzz]@7b77f334, java.text.DateFormat[US/en/dd/MM/yyyy HH:mm:ss zzz]@6d1f67d)", defaultDateTypeAdapter.toString());

    DefaultDateTypeAdapter datePatternAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    assertEquals(SIMPLE_NAME + "(class java.util.Date, java.text.SimpleDateFormat[yyyy-MM-dd'T'HH:mm:ss.SSS'Z']@6185c50d)", datePatternAdapter.toString());

    DefaultDateTypeAdapter dateStyleTimeStyleAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY);
    assertEquals(SIMPLE_NAME + "(class java.util.Date, java.text.DateFormat[Wed Jan 25 12:00:00 CST 2023]@6a76831d, java.text.DateFormat[Wed Jan 25 12:00:00 CST 2023]@6a76831d)", dateStyleTimeStyleAdapter.toString());
  }
}