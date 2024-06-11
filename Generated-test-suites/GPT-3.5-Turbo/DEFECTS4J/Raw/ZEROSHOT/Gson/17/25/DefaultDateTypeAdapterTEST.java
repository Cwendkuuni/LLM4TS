import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultDateTypeAdapterTest {
  
  private DefaultDateTypeAdapter defaultDateTypeAdapter;
  private DateFormat enUsFormat;
  private DateFormat localFormat;

  @Before
  public void setUp() {
    enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
  }

  @Test
  public void testWrite_NullValue() throws IOException {
    JsonWriter jsonWriter = new JsonWriter();
    defaultDateTypeAdapter.write(jsonWriter, null);
    assertNull(jsonWriter.toString());
  }
  
  @Test
  public void testWrite_ValidValue() throws IOException {
    Date date = new Date();
    JsonWriter jsonWriter = new JsonWriter();
    defaultDateTypeAdapter.write(jsonWriter, date);
    assertEquals(enUsFormat.format(date), jsonWriter.toString());
  }
  
  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDate() throws IOException {
    JsonReader jsonReader = new JsonReader();
    jsonReader.setValue(JsonToken.STRING, "Invalid Date");
    defaultDateTypeAdapter.read(jsonReader);
  }
  
  @Test
  public void testRead_ValidDate() throws IOException {
    Date expected = new Date();
    String dateString = enUsFormat.format(expected);
    JsonReader jsonReader = new JsonReader();
    jsonReader.setValue(JsonToken.STRING, dateString);
    Date actual = defaultDateTypeAdapter.read(jsonReader);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testRead_TimestampDate() throws IOException {
    Timestamp expected = new Timestamp(new Date().getTime());
    String dateString = enUsFormat.format(expected);
    JsonReader jsonReader = new JsonReader();
    jsonReader.setValue(JsonToken.STRING, dateString);
    Date actual = defaultDateTypeAdapter.read(jsonReader);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testRead_SqlDate() throws IOException {
    java.sql.Date expected = new java.sql.Date(new Date().getTime());
    String dateString = enUsFormat.format(expected);
    JsonReader jsonReader = new JsonReader();
    jsonReader.setValue(JsonToken.STRING, dateString);
    Date actual = defaultDateTypeAdapter.read(jsonReader);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }
  
  @Test(expected = AssertionError.class)
  public void testRead_InvalidDateType() throws IOException {
    JsonReader jsonReader = new JsonReader();
    jsonReader.setValue(JsonToken.STRING, "23-05-2022");
    defaultDateTypeAdapter.read(jsonReader);
  }
  
  @Test
  public void testDeserializeToDate_InvalidDate() {
    String date = "Invalid Date";
    Date result = defaultDateTypeAdapter.deserializeToDate(date);
    assertNull(result);
  }
  
  @Test
  public void testDeserializeToDate_ValidDate() {
    Date expected = new Date();
    String dateString = enUsFormat.format(expected);
    Date actual = defaultDateTypeAdapter.deserializeToDate(dateString);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }
  
  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDate_InvalidISO8601Date() {
    String date = "2022-05-23T10:22:33Z";
    defaultDateTypeAdapter.deserializeToDate(date);
  }
  
  @Test
  public void testToString() {
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String actual = defaultDateTypeAdapter.toString();
    assertEquals(expected, actual);
  }
}