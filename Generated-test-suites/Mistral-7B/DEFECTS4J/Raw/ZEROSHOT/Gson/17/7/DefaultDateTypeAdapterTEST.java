import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonElement;
import com.google.gson.stream.JsonParser;

public class DefaultDateTypeAdapterTest {

  private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss z";
  private static final String ISO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  private DefaultDateTypeAdapter dateAdapter;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;
  private JsonParser jsonParser;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    dateAdapter = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat(DATE_PATTERN, Locale.US), new SimpleDateFormat(DATE_PATTERN));
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
    jsonParser = new JsonParser();
  }

  @Test
  public void testWriteDate() throws IOException {
    Date date = new Date();
    dateAdapter.write(jsonWriter, date);
    verify(jsonWriter).value(dateAdapter.enUsFormat.format(date));
  }

  @Test
  public void testReadDate() throws IOException {
    String dateString = "25/12/2021 10:30:00 EST";
    when(jsonReader.nextString()).thenReturn(dateString);

    Date readDate = dateAdapter.read(jsonReader);
    Date expectedDate = localDateFormat.parse(dateString);

    assertEquals(expectedDate, readDate);
  }

  @Test
  public void testReadDate_InvalidFormat() throws IOException {
    when(jsonReader.nextString()).thenReturn("invalid date");

    try {
      dateAdapter.read(jsonReader);
      fail();
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testReadDate_ISOFormat() throws IOException {
    String isoDateString = "2021-12-25T10:30:00.000Z";
    when(jsonReader.nextString()).thenReturn(isoDateString);

    Date readDate = dateAdapter.read(jsonReader);
    Date expectedDate = new Date(ISO8601Utils.parse(isoDateString, new ParsePosition(0)).getTime());

    assertEquals(expectedDate, readDate);
  }

  @Test
  public void testWriteTimestamp() throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    dateAdapter.write(jsonWriter, timestamp);
    verify(jsonWriter).value(dateAdapter.enUsFormat.format(new Date(timestamp.getTime())));
  }

  @Test
  public void testReadTimestamp() throws IOException {
    String dateString = "25/12/2021 10:30:00 EST";
    when(jsonReader.nextString()).thenReturn(dateString);

    Date readDate = dateAdapter.read(jsonReader);
    Date expectedDate = localDateFormat.parse(dateString);

    assertEquals(new Timestamp(expectedDate.getTime()).getTime(), new Timestamp(readDate.getTime()).getTime());
  }

  @Test
  public void testReadSqlDate() throws IOException {
    String dateString = "25-12-2021";
    when(jsonReader.nextString()).thenReturn(dateString);

    Date readDate = dateAdapter.read(jsonReader);
    Date expectedDate = new Date(0);
    expectedDate.setYear(2021);
    expectedDate.setMonth(11);
    expectedDate.setDate(25);

    assertEquals(expectedDate, readDate);
  }

  @Test
  public void testReadInvalidDateType() throws IOException {
    when(jsonReader.nextString()).thenReturn("invalid type");

    try {
      dateAdapter.read(jsonReader);
      fail();
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  private DateFormat localDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);
}