import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite() {
    JsonWriter writer = new JsonWriter(new StringWriter());
    Date date = new Date();

    try {
      adapter.write(writer, date);
      String expected = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(date);
      String actual = writer.toString().replace("\"", "");
      assertEquals(expected, actual);
    } catch (IOException e) {
      fail("IOException occurred while writing");
    }
  }

  @Test
  public void testWrite_NullDate() {
    JsonWriter writer = new JsonWriter(new StringWriter());
    Date date = null;

    try {
      adapter.write(writer, date);
      String expected = "null";
      String actual = writer.toString();
      assertEquals(expected, actual);
    } catch (IOException e) {
      fail("IOException occurred while writing");
    }
  }

  @Test
  public void testRead_ValidDate() {
    JsonReader reader = new JsonReader(new StringReader("'2022-12-31'"));
    Date expected = null;

    try {
      expected = new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-31");
    } catch (ParseException e) {
      fail("ParseException occurred while parsing expected date");
    }

    try {
      Date actual = adapter.read(reader);
      assertNotNull(actual);
      assertEquals(expected, actual);
    } catch (IOException e) {
      fail("IOException occurred while reading");
    }
  }

  @Test
  public void testRead_NullValue() {
    JsonReader reader = new JsonReader(new StringReader("null"));
    Date expected = null;

    try {
      Date actual = adapter.read(reader);
      assertNull(actual);
    } catch (IOException e) {
      fail("IOException occurred while reading");
    }
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidValue() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("'invalid-date'"));
    adapter.read(reader); // Expecting JsonParseException
  }

  // Add more test cases as needed

}