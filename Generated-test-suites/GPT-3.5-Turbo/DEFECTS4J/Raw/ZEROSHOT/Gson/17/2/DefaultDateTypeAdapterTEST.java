import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNull() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, null);
    Assert.assertEquals("null", jsonWriter.toString());
  }

  @Test
  public void testWriteDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = format.parse("2021-01-01 12:00:00");
    String expectedOutput = "\"2021-01-01 12:00:00\"";
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    adapter.write(jsonWriter, date);
    Assert.assertEquals(expectedOutput, writer.toString());
  }

  @Test
  public void testWriteTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = format.parse("2021-01-01 12:00:00");
    String expectedOutput = "\"2021-01-01 12:00:00\"";
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    adapter.write(jsonWriter, new Timestamp(date.getTime()));
    Assert.assertEquals(expectedOutput, writer.toString());
  }

  @Test
  public void testWriteSqlDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date = format.parse("2021-01-01");
    String expectedOutput = "\"2021-01-01\"";
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    adapter.write(jsonWriter, new java.sql.Date(date.getTime()));
    Assert.assertEquals(expectedOutput, writer.toString());
  }

  @Test
  public void testReadDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String json = "\"2021-01-01 12:00:00\"";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    Date date = adapter.read(jsonReader);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date expectedDate = format.parse("2021-01-01 12:00:00");
    Assert.assertEquals(expectedDate, date);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    String json = "\"2021-01-01 12:00:00\"";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    Timestamp timestamp = adapter.read(jsonReader);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date expectedDate = format.parse("2021-01-01 12:00:00");
    Assert.assertEquals(new Timestamp(expectedDate.getTime()), timestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    String json = "\"2021-01-01\"";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    java.sql.Date sqlDate = adapter.read(jsonReader);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date expectedDate = format.parse("2021-01-01");
    Assert.assertEquals(new java.sql.Date(expectedDate.getTime()), sqlDate);
  }
}