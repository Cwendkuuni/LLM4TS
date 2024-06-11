import static org.junit.Assert.*;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNullDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test writing null date value
    // Expected output is null value
    assertEquals("null", adapter.write(null));
  }
  
  @Test
  public void testWriteValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test writing valid date value
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Expected output is formatted date string
    assertEquals(format.format(date), adapter.write(date));
  }
  
  @Test
  public void testReadValidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test reading valid date string
    Date expectedDate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = format.format(expectedDate);
    // Expected output is the Date object from the string
    assertEquals(expectedDate, adapter.read(dateString));
  }
  
  @Test
  public void testReadInvalidDateString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test reading invalid date string
    String invalidDateString = "invalid_date";
    // Expected output is a JsonSyntaxException
    assertThrows(JsonSyntaxException.class, () -> adapter.read(invalidDateString));
  }
  
  @Test
  public void testReadNullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test reading null value
    // Expected output is a JsonParseException
    assertThrows(JsonParseException.class, () -> adapter.read(null));
  }

}