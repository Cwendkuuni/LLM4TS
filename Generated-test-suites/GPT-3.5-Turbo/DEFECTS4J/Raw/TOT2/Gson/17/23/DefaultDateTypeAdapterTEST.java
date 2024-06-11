import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.DefaultDateTypeAdapter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test writing null value
    // Expected behavior: null value should be written without exceptions
    adapter.write(null, null);
  }

  @Test
  public void testWrite_NotNullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test writing a non-null value
    // Expected behavior: the date should be formatted and written without exceptions
    Date date = new Date();
    adapter.write(null, date);
  }

  @Test
  public void testRead_StringValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test reading a valid string value
    // Expected behavior: the string should be parsed to a Date object without exceptions
    String dateString = "2021-09-01T12:00:00";
    Date date = adapter.deserializeToDate(dateString);
    assertNotNull(date);
  }

  @Test
  public void testRead_InvalidStringValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test reading an invalid string value
    // Expected behavior: the method should throw a JsonParseException
    String invalidDateString = "invalidDate";
    assertThrows(JsonParseException.class, () -> adapter.deserializeToDate(invalidDateString));
  }

  @Test
  public void testRead_NullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test reading a null value
    // Expected behavior: the method should throw a JsonParseException
    assertThrows(JsonParseException.class, () -> adapter.read(null));
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test toString method
    // Expected behavior: the toString should return the class name and format class name enclosed in parentheses
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}