import static org.junit.Assert.*;

import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNullDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter out = new JsonWriter();
    Date value = null;
    try {
      adapter.write(out, value);
      assertEquals(JsonToken.NULL, out.peek());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWriteNonNullDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter out = new JsonWriter();
    Date value = new Date();
    try {
      adapter.write(out, value);
      assertEquals(JsonToken.STRING, out.peek());
      String expected = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(value);
      assertEquals(expected, out.nextString());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWriteNullTimestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter out = new JsonWriter();
    Date value = null;
    try {
      adapter.write(out, value);
      assertEquals(JsonToken.NULL, out.peek());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWriteNonNullTimestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter out = new JsonWriter();
    Date value = new Date();
    try {
      adapter.write(out, value);
      assertEquals(JsonToken.STRING, out.peek());
      String expected = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(value);
      assertEquals(expected, out.nextString());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWriteNullSqlDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter out = new JsonWriter();
    Date value = null;
    try {
      adapter.write(out, value);
      assertEquals(JsonToken.NULL, out.peek());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWriteNonNullSqlDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter out = new JsonWriter();
    Date value = new Date();
    try {
      adapter.write(out, value);
      assertEquals(JsonToken.STRING, out.peek());
      String expected = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(value);
      assertEquals(expected, out.nextString());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testReadInvalidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader();
    in.beginObject();
    in.name("date");
    in.value("invalid_date");
    in.endObject();
    try {
      Date date = adapter.read(in);
      fail("JsonParseException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (JsonParseException e) {
      // This is the expected exception
    }
  }

  @Test
  public void testReadValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader();
    in.beginObject();
    in.name("date");
    in.value("2021-01-01");
    in.endObject();
    try {
      Date date = adapter.read(in);
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Date expectedDate = format.parse("2021-01-01");
      assertEquals(expectedDate, date);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (JsonParseException e) {
      fail("JsonParseException should not be thrown");
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }

  @Test
  public void testReadValidTimestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader in = new JsonReader();
    in.beginObject();
    in.name("date");
    in.value("2021-01-01 00:00:00");
    in.endObject();
    try {
      Date date = adapter.read(in);
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date expectedDate = format.parse("2021-01-01 00:00:00");
      Timestamp expectedTimestamp = new Timestamp(expectedDate.getTime());
      assertEquals(expectedTimestamp, date);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (JsonParseException e) {
      fail("JsonParseException should not be thrown");
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }

  @Test
  public void testReadValidSqlDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader in = new JsonReader();
    in.beginObject();
    in.name("date");
    in.value("2021-01-01");
    in.endObject();
    try {
      Date date = adapter.read(in);
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Date expectedDate = format.parse("2021-01-01");
      java.sql.Date expectedSqlDate = new java.sql.Date(expectedDate.getTime());
      assertEquals(expectedSqlDate, date);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (JsonParseException e) {
      fail("JsonParseException should not be thrown");
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }

}