import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDateTypeAdapterTest {
  
  DefaultDateTypeAdapter defaultDateTypeAdapter;

  @Before
  public void setUp() {
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullDate_WritesNullValue() throws Exception {
    JsonWriter mockJsonWriter = new JsonWriter(null); // Replace with your actual JsonWriter implementation
    mockJsonWriter.setSerializeNulls(true);

    defaultDateTypeAdapter.write(mockJsonWriter, null);

    assertEquals(JsonToken.NULL, mockJsonWriter.peek());
  }

  @Test
  public void testWrite_NonNullDate_WritesFormattedDateString() throws Exception {
    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2022-01-01");

    JsonWriter mockJsonWriter = new JsonWriter(null); // Replace with your actual JsonWriter implementation
    mockJsonWriter.setSerializeNulls(true);

    defaultDateTypeAdapter.write(mockJsonWriter, date);

    assertEquals("\"2022-01-01\"", mockJsonWriter.peek());
  }

  @Test
  public void testRead_NonStringToken_ThrowsJsonParseException() throws Exception {
    JsonReader mockJsonReader = new JsonReader(null); // Replace with your actual JsonReader implementation
    mockJsonReader.beginObject();
    mockJsonReader.name("date");
    mockJsonReader.value(true);
    mockJsonReader.endObject();

    try {
      defaultDateTypeAdapter.read(mockJsonReader);
    } catch (JsonParseException e) {
      assertEquals("The date should be a string value", e.getMessage());
    }
  }

  @Test
  public void testRead_ValidDateString_ReturnsCorrectDate() throws Exception {
    String dateString = "2022-01-01";

    JsonReader mockJsonReader = new JsonReader(null); // Replace with your actual JsonReader implementation
    mockJsonReader.value(dateString);

    Date result = defaultDateTypeAdapter.read(mockJsonReader);

    Date expected = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);
    assertEquals(expected, result);
  }

  @Test
  public void testToString_ReturnsExpectedString() {
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    assertEquals(expected, defaultDateTypeAdapter.toString());
  }
}