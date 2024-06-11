
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    try {
      adapter.write(writer, null);
      assertEquals(JsonToken.NULL, writer.peek());
    } catch (IOException e) {
      fail("IOException thrown");
    }
  }
  
  @Test
  public void testWrite_ValidValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    try {
      adapter.write(writer, date);
      assertEquals(date.toString(), writer.peek().asString());
    } catch (IOException e) {
      fail("IOException thrown");
    }
  }
  
  @Test
  public void testRead_ValidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    String dateString = "2022-01-01 12:00:00";
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
    try {
      reader.setNextValue(JsonToken.STRING, dateString);
      Date date = adapter.read(reader);
      assertEquals(expectedDate, date);
    } catch (IOException | ParseException e) {
      fail("Exception thrown");
    }
  }
  
  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDataType() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    reader.setNextValue(JsonToken.NUMBER, "42");
    adapter.read(reader);
  }
  
  @Test
  public void testDeserializeToDate_ValidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-01-01 12:00:00";
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
    Date date = adapter.deserializeToDate(dateString);
    assertEquals(expectedDate, date);
  }
  
  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDate_InvalidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-13-32 25:00:00";
    adapter.deserializeToDate(dateString);
  }
}
