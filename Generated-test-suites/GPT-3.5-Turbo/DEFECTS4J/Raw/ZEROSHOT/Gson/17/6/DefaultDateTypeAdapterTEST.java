import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    adapter.write(writer, null);
    Assert.assertTrue(writer.isNull());
  }

  @Test
  public void testWrite() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    Date date = new Date();
    adapter.write(writer, date);
    Assert.assertEquals(adapter.enUsFormat.format(date), writer.getValue());
  }

  @Test
  public void testReadInvalidType() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date");
    reader.value("2021-01-01");
    reader.endObject();
    try {
      adapter.read(reader);
      Assert.fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testReadNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.nullValue();
    Date date = adapter.read(reader);
    Assert.assertNull(date);
  }

  @Test
  public void testReadDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.value("2021-01-01");
    Date date = adapter.read(reader);
    DateFormat expectedFormat = adapter.localFormat;
    Date expectedDate = expectedFormat.parse("2021-01-01");
    Assert.assertEquals(expectedDate, date);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader();
    reader.value("2021-01-01");
    Timestamp timestamp = adapter.read(reader);
    DateFormat expectedFormat = adapter.localFormat;
    Date expectedDate = expectedFormat.parse("2021-01-01");
    Timestamp expectedTimestamp = new Timestamp(expectedDate.getTime());
    Assert.assertEquals(expectedTimestamp, timestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader();
    reader.value("2021-01-01");
    java.sql.Date sqlDate = adapter.read(reader);
    DateFormat expectedFormat = adapter.localFormat;
    Date expectedDate = expectedFormat.parse("2021-01-01");
    java.sql.Date expectedSqlDate = new java.sql.Date(expectedDate.getTime());
    Assert.assertEquals(expectedSqlDate, sqlDate);
  }

}