import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateAdapter;
  private DefaultDateTypeAdapter dateAdapterWithUsFormat;
  private DefaultDateTypeAdapter dateAdapterWithCustomFormat;
  private DefaultDateTypeAdapter dateAdapterWithDateStyleAndTimeStyle;

  @Before
  public void setUp() {
    dateAdapter = new DefaultDateTypeAdapter(Date.class);
    dateAdapterWithUsFormat = new DefaultDateTypeAdapter(Date.class, Locale.US);
    dateAdapterWithCustomFormat = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd HH:mm:ss");
    dateAdapterWithDateStyleAndTimeStyle = new DefaultDateTypeAdapter(Date.class, Date.DATE_FIELD, Date.HOUR_OF_DAY);
  }

  @Test
  public void testWrite() throws IOException {
    JsonWriter jsonWriter = new JsonWriter(System.out);
    Date date = new Date();
    dateAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    Assert.assertEquals("Expected a string output from write method", dateAdapter.toString(), System.out.getBytes());
  }

  @Test
  public void testReadDate() throws IOException, ParseException {
    JsonReader jsonReader = new JsonReader(new StringReader("2022-01-01"));
    Date date = dateAdapter.read(jsonReader);

    Assert.assertNotNull("Expected a Date object from read method", date);
    Assert.assertEquals("Expected the date read from the string to be the same as the original date", new Date(2022, 0, 1), date);
  }

  @Test
  public void testReadTimestamp() throws IOException, ParseException {
    JsonReader jsonReader = new JsonReader(new StringReader("2022-01-01 12:00:00"));
    Timestamp timestamp = new Timestamp(new Date(2022, 0, 1).getTime());
    Date date = dateAdapter.read(jsonReader);

    Assert.assertNotNull("Expected a Date object from read method", date);
    Assert.assertEquals("Expected the date read from the string to be the same as the original Timestamp", timestamp, new Timestamp(date.getTime()));
  }

  @Test
  public void testReadSqlDate() throws IOException, ParseException {
    JsonReader jsonReader = new JsonReader(new StringReader("2022-01-01"));
    java.sql.Date sqlDate = new java.sql.Date(new Date(2022, 0, 1).getTime());
    Date date = dateAdapter.read(jsonReader);

    Assert.assertNotNull("Expected a Date object from read method", date);
    Assert.assertEquals("Expected the date read from the string to be the same as the original sqlDate", sqlDate, new java.sql.Date(date.getTime()));
  }

  @Test(expected = AssertionError.class)
  public void testReadInvalidType() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("2022-01-01"));
    Integer integer = dateAdapter.read(jsonReader);
  }

  @Test
  public void testDeserializeToDate() throws ParseException {
    String dateString = "2022-01-01";
    Date date = dateAdapter.deserializeToDate(dateString);

    Assert.assertNotNull("Expected a Date object from deserializeToDate method", date);
    Assert.assertEquals("Expected the date deserialized from the string to be the same as the original date", new Date(2022, 0, 1), date);
  }

  @Test
  public void testDeserializeToDateUsFormat() throws ParseException {
    String dateString = "Jan 1, 2022";
    Date date = dateAdapterWithUsFormat.deserializeToDate(dateString);

    Assert.assertNotNull("Expected a Date object from deserializeToDate method", date);
    Assert.assertEquals("Expected the date deserialized from the string to be the same as the original date", new Date(2022, 0, 1), date);
  }

  @Test
  public void testDeserializeToDateCustomFormat() throws ParseException {
    String dateString = "2022-01-01 12:00:00";
    Date date = dateAdapterWithCustomFormat.deserializeToDate(dateString);

    Assert.assertNotNull("Expected a Date object from deserializeToDate method", date);
    Assert.assertEquals("Expected the date deserialized from the string to be the same as the original date", new Date(2022, 0, 1, 12, 0, 0), date);
  }

  @Test
  public void testDeserializeToDateISO8601Utils() throws ParseException {
    String dateString = "2022-01-01T12:00:00.000Z";
    Date date = dateAdapter.deserializeToDate(dateString);

    Assert.assertNotNull("Expected a Date object from deserializeToDate method", date);
    Assert.assertEquals("Expected the date deserialized from the string to be the same as the original date", new Date(2022, 0, 1, 12, 0, 0), date);
  }
}