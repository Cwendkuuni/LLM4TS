import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateAdapter;
  private JsonReader jsonReaderMock;
  private JsonWriter jsonWriterMock;

  @Before
  public void setup() {
    jsonReaderMock = mock(JsonReader.class);
    jsonWriterMock = mock(JsonWriter.class);

    dateAdapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testDefaultConstructor() {
    // Test default constructor
    DefaultDateTypeAdapter defaultAdapter = new DefaultDateTypeAdapter(Date.class);
    Assert.assertNotNull(defaultAdapter.getEnUsFormat());
    Assert.assertNotNull(defaultAdapter.getLocalFormat());
  }

  @Test
  public void testStringDateFormatConstructor() {
    // Test constructor with date pattern
    String datePattern = "yyyy-MM-dd HH:mm:ss";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
    DateFormat enUsFormat = adapter.getEnUsFormat();
    DateFormat localFormat = adapter.getLocalFormat();
    Assert.assertEquals(datePattern, enUsFormat.toPattern());
    Assert.assertEquals(datePattern, localFormat.toPattern());
  }

  @Test
  public void testIntConstructor() {
    // Test constructor with date styles
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    DateFormat enUsFormat = adapter.getEnUsFormat();
    DateFormat localFormat = adapter.getLocalFormat();
    Assert.assertEquals(DateFormat.SHORT, enUsFormat.getDateFormatSymbols().getShortMonths()[Calendar.JANUARY]);
    Assert.assertEquals(DateFormat.SHORT, localFormat.getDateFormatSymbols().getShortMonths()[Calendar.JANUARY]);
  }

  @Test
  public void testWrite() throws IOException {
    // Test write method
    Date testDate = new Date();
    when(jsonWriterMock.value(String.class)).thenCallRealMethod();

    dateAdapter.write(jsonWriterMock, testDate);

    SimpleDateFormat expectedFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    String testOutput = (String) jsonWriterMock.getBuffer().toString();
    String expectedOutput = expectedFormat.format(testDate);

    Assert.assertEquals(expectedOutput, testOutput);
  }

  @Test
  public void testReadDate() throws IOException, ParseException {
    // Test read method with Date
    String testString = "2023-02-13 15:30:45";
    Date expectedDate = new Date(1676352645000L);

    when(jsonReaderMock.nextString()).thenReturn(testString);

    Date actualDate = dateAdapter.read(jsonReaderMock);

    Assert.assertEquals(expectedDate, actualDate);
  }

  @Test(expected = JsonParseException.class)
  public void testReadDateInvalidFormat() throws IOException, ParseException {
    // Test read method with invalid format
    String testString = "InvalidFormat";

    when(jsonReaderMock.nextString()).thenReturn(testString);

    dateAdapter.read(jsonReaderMock);
  }

  @Test
  public void testReadTimestamp() throws IOException, ParseException {
    // Test read method with Timestamp
    String testString = "2023-02-13 15:30:45.123";
    Timestamp expectedTimestamp = new Timestamp(1676352745123L);

    when(jsonReaderMock.nextString()).thenReturn(testString);

    Date actualDate = dateAdapter.read(jsonReaderMock);

    Assert.assertEquals(expectedTimestamp, new Timestamp(actualDate.getTime()));
  }

  @Test
  public void testReadSqlDate() throws IOException, ParseException {
    // Test read method with java.sql.Date
    String testString = "2023-02-13";
    java.sql.Date expectedSqlDate = new java.sql.Date(1676352645000L);

    when(jsonReaderMock.nextString()).thenReturn(testString);

    Date actualDate = dateAdapter.read(jsonReaderMock);

    Assert.assertEquals(expectedSqlDate, (java.sql.Date) actualDate);
  }
}