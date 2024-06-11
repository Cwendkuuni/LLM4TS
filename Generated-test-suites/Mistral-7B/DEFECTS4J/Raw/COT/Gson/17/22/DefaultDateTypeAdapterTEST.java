import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateTypeAdapter;
  private SimpleDateFormat enUsFormat;
  private SimpleDateFormat localFormat;

  @Before
  public void setup() {
    enUsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
  }

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.enUsFormat);
    assertSame(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.localFormat);
  }

  @Test
  public void testStringConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("DefaultDateTypeAdapter", new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM-dd"));
    assertEquals("DefaultDateTypeAdapter", adapter.getClass().getSimpleName());
    assertEquals(Date.class, adapter.dateType);
    assertSame(new SimpleDateFormat("yyyy-MM-dd"), adapter.enUsFormat);
    assertSame(new SimpleDateFormat("yyyy-MM-dd"), adapter.localFormat);
  }

  @Test
  public void testDateStyleIntConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, adapter.dateType);
    assertSame(DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT, Locale.US), adapter.enUsFormat);
    assertSame(DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT), adapter.localFormat);
  }

  @Test
  public void testWrite() throws IOException {
    String dateString = "2022-12-31 23:59:59.999";
    String expectedJson = "\"2022-12-31 23:59:59.999\"";

    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateTypeAdapter.write(jsonWriter, new Date(enUsFormat.parse(dateString).getTime()));
    jsonWriter.flush();

    assertEquals(expectedJson, System.out.getBytes());
  }

  @Test
  public void testReadDate() throws IOException {
    String dateString = "\"2022-12-31 23:59:59.999\"";

    JsonReader jsonReader = new JsonReader(new StringReader(dateString));
    Date expectedDate = new Date(enUsFormat.parse("2022-12-31 23:59:59.999").getTime());

    Date actualDate = dateTypeAdapter.read(jsonReader);

    assertEquals(expectedDate, actualDate);
  }

  @Test(expected = JsonParseException.class)
  public void testReadNullJson() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    dateTypeAdapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidJson() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"invalidJson\":\"abc\"}"));
    dateTypeAdapter.read(jsonReader);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    String timestampString = "2022-12-31 23:59:59.999";

    JsonReader jsonReader = new JsonReader(new StringReader(String.format("\"%s\"", timestampString)));
    Timestamp expectedTimestamp = new Timestamp(enUsFormat.parse(timestampString).getTime());

    Timestamp actualTimestamp = dateTypeAdapter.read(jsonReader);

    assertEquals(expectedTimestamp, actualTimestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException, ParseException {
    String sqlDateString = "2022-12-31";

    JsonReader jsonReader = new JsonReader(new StringReader(String.format("\"%s\"", sqlDateString)));
    java.sql.Date expectedSqlDate = new java.sql.Date(enUsFormat.parse(sqlDateString).getTime());

    java.sql.Date actualSqlDate = dateTypeAdapter.read(jsonReader);

    assertEquals(expectedSqlDate, actualSqlDate);
  }

  @Test
  public void testParseWithUSFormat() throws ParseException {
    String dateString = "2022-12-31 23:59:59.999";
    Date date = dateTypeAdapter.deserializeToDate(dateString);

    assertEquals(new Date(enUsFormat.parse(dateString).getTime()), date);
  }

  @Test
  public void testParseWithLocalFormat() throws ParseException {
    String dateString = "2022-12-31 23:59:59.999";
    Date date = dateTypeAdapter.deserializeToDate(dateString);

    assertEquals(new Date(localFormat.parse(dateString).getTime()), date);
  }

  @Test
  public void testParseWithISO8601Utils() throws ParseException {
    String dateString = "2022-12-31T23:59:59.999Z";
    Date date = dateTypeAdapter.deserializeToDate(dateString);

    assertEquals(new Date(ISO8601Utils.parse(dateString, new ParsePosition(0)).getTime()), date);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testParseInvalidString() throws ParseException {
    String invalidString = "2022-12-31AAA";
    dateTypeAdapter.deserializeToDate(invalidString);
  }

  @Test
  public void testToString() {
    Date date = new Date();
    String expectedString = DefaultDateTypeAdapter.SIMPLE_NAME + "(java.util.Date, java.text.SimpleDateFormat, java.text.SimpleDateFormat)";

    String actualString = dateTypeAdapter.toString();

    assertEquals(expectedString, actualString);
  }
}