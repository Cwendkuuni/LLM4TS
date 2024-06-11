import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {
  
  private DefaultDateTypeAdapter adapter;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullDate() {
    JsonWriter writer = new JsonWriter();
    try {
      adapter.write(writer, null);
      assertEquals(JsonToken.NULL, writer.peek());
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testWrite_ValidDate() {
    JsonWriter writer = new JsonWriter();
    Date date = new Date();
    try {
      adapter.write(writer, date);
      assertEquals(JsonToken.STRING, writer.peek());
      assertEquals(adapter.serializeToDate(writer.nextString()), date);
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidToken() throws IOException {
    JsonReader reader = new JsonReader().value(123);
    adapter.read(reader);
  }

  @Test
  public void testRead_ValidDate() {
    JsonReader reader = new JsonReader().value("2021-01-01 12:00:00");
    try {
      Date expected = adapter.deserializeToDate("2021-01-01 12:00:00");
      Date actual = adapter.read(reader);
      assertEquals(expected, actual);
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testToString() {
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    assertEquals(expected, adapter.toString());
  }

}