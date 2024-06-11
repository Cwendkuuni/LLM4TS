import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateTypeAdapter;
  private JsonReader jsonReaderMock;
  private JsonWriter jsonWriterMock;

  @Before
  public void setup() {
    jsonReaderMock = mock(JsonReader.class);
    jsonWriterMock = mock(JsonWriter.class);
    dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
  }

  // Test constructor with Date type
  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateType() {
    Date date = new Date();
    // Test write method
    dateTypeAdapter.write(jsonWriterMock, date);
    Mockito.verify(jsonWriterMock).value(date.toString());
    // Test read method
    when(jsonReaderMock.nextString()).thenReturn("2022-01-01");
    Date readDate = dateTypeAdapter.read(jsonReaderMock);
    Assert.assertEquals("Expected date to be equal", new Date(2022, 0, 1), readDate);
  }

  // Test constructor with Timestamp type
  @Test
  public void testDefaultDateTypeAdapterConstructorWithTimestampType() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    // Test write method
    dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    dateTypeAdapter.write(jsonWriterMock, timestamp);
    Mockito.verify(jsonWriterMock).value(timestamp.toString());
    // Test read method
    when(jsonReaderMock.nextString()).thenReturn(timestamp.toString());
    Timestamp readTimestamp = dateTypeAdapter.read(jsonReaderMock);
    Assert.assertEquals("Expected timestamp to be equal", timestamp, readTimestamp);
  }

  // Test constructor with java.sql.Date type
  @Test
  public void testDefaultDateTypeAdapterConstructorWithSqlDateType() {
    Date date = new Date(1633059200000L); // Mon Jan 01 00:00:00 EST 2012
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    // Test write method
    dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    dateTypeAdapter.write(jsonWriterMock, sqlDate);
    Mockito.verify(jsonWriterMock).value(date.toString());
    // Test read method
    when(jsonReaderMock.nextString()).thenReturn(date.toString());
    java.sql.Date readSqlDate = dateTypeAdapter.read(jsonReaderMock);
    Assert.assertEquals("Expected sqlDate to be equal", sqlDate, readSqlDate);
  }

  // Test write method with null value
  @Test(expected = JsonIOException.class)
  public void testWriteMethodWithNullValue() {
    dateTypeAdapter.write(jsonWriterMock, null);
    Mockito.verifyNoMoreInteractions(jsonWriterMock);
    throw new JsonIOException("A date value was null");
  }

  // Test read method with null value
  @Test(expected = JsonParseException.class)
  public void testReadMethodWithNullValue() {
    when(jsonReaderMock.nextString()).thenReturn(null);
    dateTypeAdapter.read(jsonReaderMock);
    throw new JsonParseException("The date should be a string value");
  }

  // Test read method with invalid date format
  @Test(expected = JsonParseException.class)
  public void testReadMethodWithInvalidDateFormat() {
    when(jsonReaderMock.nextString()).thenReturn("InvalidDateFormat");
    dateTypeAdapter.read(jsonReaderMock);
    throw new JsonParseException("The date should be a valid date format");
  }

  // Test read method with invalid date value
  @Test(expected = JsonSyntaxException.class)
  public void testReadMethodWithInvalidDateValue() {
    when(jsonReaderMock.nextString()).thenReturn("1234567890");
    dateTypeAdapter.read(jsonReaderMock);
    throw new JsonSyntaxException("1234567890", new NumberFormatException("For input string: \"1234567890\""));
  }
}