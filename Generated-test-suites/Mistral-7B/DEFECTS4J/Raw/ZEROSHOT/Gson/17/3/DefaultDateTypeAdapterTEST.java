import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader.CloseQuietly;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() {
    // Test default constructor
    TypeAdapter<Date> defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, defaultDateTypeAdapter.getType());

    // Test constructor with DateFormat
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    TypeAdapter<Date> dateTypeAdapterWithDateFormat = new DefaultDateTypeAdapter(Date.class, dateFormat);
    assertEquals(Date.class, dateTypeAdapterWithDateFormat.getType());
    assertEquals(dateFormat, dateTypeAdapterWithDateFormat.enUsFormat);
    assertEquals(dateFormat, dateTypeAdapterWithDateFormat.localFormat);

    // Test constructor with DatePattern
    String datePattern = "yyyy-MM-dd HH:mm:ss";
    TypeAdapter<Date> dateTypeAdapterWithDatePattern = new DefaultDateTypeAdapter(Date.class, datePattern);
    assertEquals(Date.class, dateTypeAdapterWithDatePattern.getType());

    // Test constructor with dateStyle and timeStyle
    TypeAdapter<Date> dateTypeAdapterWithDateStyleAndTimeStyle = new DefaultDateTypeAdapter(Date.class, Date.ABBREVIATED, Date.ABBREVIATED);
    assertEquals(Date.class, dateTypeAdapterWithDateStyleAndTimeStyle.getType());

    // Test constructor with int dateStyle and int timeStyle
    TypeAdapter<Date> dateTypeAdapterWithDateStyleAndTimeStyleInt = new DefaultDateTypeAdapter(Date.class, Date.ABBREVIATED, Date.ABBREVIATED);
    assertEquals(Date.class, dateTypeAdapterWithDateStyleAndTimeStyleInt.getType());

    // Test constructor with Class<? extends Date> and DateFormat
    TypeAdapter<Timestamp> timestampTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class, dateFormat);
    assertEquals(Timestamp.class, timestampTypeAdapter.getType());
    assertEquals(dateFormat, timestampTypeAdapter.enUsFormat);
    assertEquals(dateFormat, timestampTypeAdapter.localFormat);

    // Test constructor with Class<? extends Date> and int dateStyle and int timeStyle
    TypeAdapter<java.sql.Date> sqlDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class, Date.ABBREVIATED, Date.ABBREVIATED);
    assertEquals(java.sql.Date.class, sqlDateTypeAdapter.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateTypeThrowsIllegalArgumentException() {
    new DefaultDateTypeAdapter(String.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
  }

  @Test
  public void testSerialization() throws IOException {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(new Date(), new DefaultDateTypeAdapter(Date.class));
    JsonReader jsonReader = new JsonReader(jsonElement.getAsStream());
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    Date result = adapter.read(jsonReader);
    assertEquals(new Date(), result);
  }

  @Test(expected = JsonParseException.class)
  public void testInvalidDateFormatThrowsJsonParseException() throws IOException {
    Gson gson = new Gson();
    JsonReader jsonReader = new JsonReader(new CloseQuietly(new StringReader("Invalid date format")));
    new DefaultDateTypeAdapter(Date.class).read(jsonReader);
  }

  @Test
  public void testDeserializeToDate() throws ParseException {
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    assertEquals(new Date(), DefaultDateTypeAdapter.class. newInstance().deserializeToDate(dateFormat.format(new Date())));
    assertEquals(new Date(), DefaultDateTypeAdapter.class. newInstance().deserializetoDate("2022-12-31 23:59:59"));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateWithInvalidFormatThrowsJsonSyntaxException() {
    new DefaultDateTypeAdapter(Date.class).deserializetoDate("InvalidFormat");
  }

  @Test
  public void testToString() {
    TypeAdapter<Date> dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(java.text.SimpleDateFormat)", dateTypeAdapter.toString());
  }
}