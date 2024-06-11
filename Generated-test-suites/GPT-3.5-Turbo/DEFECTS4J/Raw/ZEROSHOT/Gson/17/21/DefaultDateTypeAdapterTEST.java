import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullValue() {
    // Test writing null value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    try {
      adapter.write(writer, null);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    assertEquals("null", writer.toString());
  }

  @Test
  public void testWrite_DateValue() {
    // Test writing Date value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    Date date = new Date();
    try {
      adapter.write(writer, date);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    assertEquals(date.toString(), writer.toString());
  }

  @Test
  public void testWrite_TimestampValue() {
    // Test writing Timestamp value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter writer = new JsonWriter();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    try {
      adapter.write(writer, timestamp);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    assertEquals(timestamp.toString(), writer.toString());
  }

  @Test
  public void testWrite_SqlDateValue() {
    // Test writing java.sql.Date value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter writer = new JsonWriter();
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    try {
      adapter.write(writer, sqlDate);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    assertEquals(sqlDate.toString(), writer.toString());
  }

  @Test
  public void testRead_InvalidJson() {
    // Test reading invalid JSON
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    try {
      adapter.read(reader);
      fail("Expected JsonParseException to be thrown");
    } catch (JsonParseException e) {
      // expected
    }
    reader.endObject();
  }

  @Test
  public void testRead_DateValue() {
    // Test reading Date value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.nextName();
    reader.nextString("2021-01-01");
    Date expected = new Date();
    expected.setYear(121);
    expected.setMonth(0);
    expected.setDate(1);
    try {
      Date result = adapter.read(reader);
      assertEquals(expected, result);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    reader.endObject();
  }

  @Test
  public void testRead_TimestampValue() {
    // Test reading Timestamp value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.nextName();
    reader.nextString("2021-01-01 00:00:00");
    Timestamp expected = new Timestamp(121, 0, 1, 0, 0, 0, 0);
    try {
      Timestamp result = adapter.read(reader);
      assertEquals(expected, result);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    reader.endObject();
  }

  @Test
  public void testRead_SqlDateValue() {
    // Test reading java.sql.Date value
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.nextName();
    reader.nextString("2021-01-01");
    java.sql.Date expected = java.sql.Date.valueOf("2021-01-01");
    try {
      java.sql.Date result = adapter.read(reader);
      assertEquals(expected, result);
    } catch (IOException e) {
      fail("IOException encountered: " + e.getMessage());
    }
    reader.endObject();
  }
}