import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test null value
    JsonWriter writer = new JsonWriter();
    adapter.write(writer, null);
    assertNull(writer.getValue());
    
    // Test non-null value
    Date date = new Date();
    writer = new JsonWriter();
    adapter.write(writer, date);
    assertEquals(adapter.enUsFormat.format(date), writer.getValue());
  }

  @Test
  public void testRead() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    // Test valid string value
    String dateString = "2020-01-01";
    JsonReader reader = new JsonReader(dateString);
    Date expectedDate = adapter.deserializeToDate(dateString);
    Date actualDate = adapter.read(reader);
    assertEquals(expectedDate, actualDate);
    
    // Test non-string value
    reader = new JsonReader(true);
    try {
      adapter.read(reader);
      fail("Expected JsonParseException to be thrown");
    } catch (JsonParseException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testDeserializeToDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    // Test valid date string
    String dateString = "2020-01-01";
    Date expectedDate = adapter.localFormat.parse(dateString);
    Date actualDate = adapter.deserializeToDate(dateString);
    assertEquals(expectedDate, actualDate);
    
    // Test invalid date string
    dateString = "2020-13-01";
    try {
      adapter.deserializeToDate(dateString);
      fail("Expected JsonSyntaxException to be thrown");
    } catch (JsonSyntaxException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String actualString = adapter.toString();
    assertEquals(expectedString, actualString);
  }

}