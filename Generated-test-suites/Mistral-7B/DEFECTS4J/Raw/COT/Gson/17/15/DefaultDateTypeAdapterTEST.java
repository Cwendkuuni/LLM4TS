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
  private SimpleDateFormat simpleDateFormat;

  @Before
  public void setup() {
    dateAdapter = new DefaultDateTypeAdapter(Date.class);
    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  @Test
  public void testDefaultConstructor() {
    Assert.assertEquals(Date.class, dateAdapter.DATE_TYPE);
  }

  @Test
  public void testWrite() throws IOException {
    Date date = new Date();
    String dateString = dateAdapter.toString();
    dateAdapter.write(new JsonWriter(System.out), date);
    Assert.assertEquals(dateString, System.out.toString());
  }

  @Test
  public void testReadDate() throws IOException, ParseException {
    String dateString = "2022-01-01 12:00:00";
    Date date = dateAdapter.read(new JsonReader(new StringReader(dateString)));
    Assert.assertEquals(simpleDateFormat.parse(dateString), date);
  }

  @Test
  public void testReadTimestamp() throws IOException, ParseException {
    String timestampString = "2022-01-01 12:00:00.123";
    Timestamp timestamp = new Timestamp(simpleDateFormat.parse(timestampString).getTime());
    Assert.assertEquals(timestamp, dateAdapter.read(new JsonReader(new StringReader(timestampString))));
  }

  @Test
  public void testReadSqlDate() throws IOException, ParseException {
    String sqlDateString = "2022-01-01";
    Date sqlDate = Date.valueOf(sqlDateString);
    Assert.assertEquals(sqlDate, dateAdapter.read(new JsonReader(new StringReader(sqlDateString))));
  }

  @Test(expected = JsonParseException.class)
  public void testInvalidJson() throws IOException {
    dateAdapter.read(new JsonReader(new StringReader("invalidJson")));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testInvalidDateFormat() throws IOException, ParseException {
    SimpleDateFormat invalidFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    String invalidDateString = "2022-01-01 12:00:00.123.456";
    dateAdapter = new DefaultDateTypeAdapter(Date.class, invalidFormat, invalidFormat);
    dateAdapter.read(new JsonReader(new StringReader(invalidDateString)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateType() {
    dateAdapter = new DefaultDateTypeAdapter(Integer.class, null, null);
  }
}