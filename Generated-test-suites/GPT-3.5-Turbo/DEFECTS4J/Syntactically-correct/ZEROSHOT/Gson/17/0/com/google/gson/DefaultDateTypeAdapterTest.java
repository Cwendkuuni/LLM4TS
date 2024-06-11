import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter defaultDateTypeAdapter;
  private DateFormat enUsFormat;
  private DateFormat localFormat;

  @Before
  public void setUp() {
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
  }

  @Test
  public void testWrite_NullDate() throws IOException {
    JsonWriter out = new JsonWriter(new StringWriter());
    Date value = null;

    defaultDateTypeAdapter.write(out, value);

    assertEquals("null", out.toString());
  }

  @Test
  public void testWrite_NonNullDate() throws IOException {
    JsonWriter out = new JsonWriter(new StringWriter());
    Date value = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");

    defaultDateTypeAdapter.write(out, value);

    assertEquals("\"2022-01-01\"", out.toString());
  }

  @Test
  public void testRead_InvalidType() throws IOException {
    JsonReader in = new JsonReader(new StringReader("123"));

    try {
      defaultDateTypeAdapter.read(in);
      fail("Expected JsonParseException to be thrown");
    } catch (JsonParseException e) {
      assertEquals("The date should be a string value", e.getMessage());
    }
  }

  @Test
  public void testRead_ValidDate() throws IOException, ParseException {
    JsonReader in = new JsonReader(new StringReader("\"2022-01-01\""));

    Date result = defaultDateTypeAdapter.read(in);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expected = sdf.parse("2022-01-01");
    assertEquals(expected, result);
  }

  @Test
  public void testRead_InvalidDate() throws IOException {
    JsonReader in = new JsonReader(new StringReader("\"2022-99-99\""));

    try {
      defaultDateTypeAdapter.read(in);
      fail("Expected JsonSyntaxException to be thrown");
    } catch (JsonSyntaxException e) {
      assertEquals("2022-99-99", e.getMessage());
    }
  }

  @Test
  public void testToString() {
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String result = defaultDateTypeAdapter.toString();
    assertEquals(expected, result);
  }
}