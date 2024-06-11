import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullDate_ShouldWriteNullValue() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(new StringWriter());
    
    try {
      adapter.write(writer, null);
      writer.flush();
    } catch(IOException e) {
      fail("IOException should not be thrown");
    }
    
    assertEquals("null", writer.toString());
  }

  @Test
  public void testRead_InvalidJson_ShouldThrowJsonParseException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(new StringReader("invalid json"));
    
    try {
      adapter.read(reader);
      fail("JsonParseException should be thrown");
    } catch(JsonParseException e) {
      // success
    } catch(IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testDeserializeToDate_InvalidDateString_ShouldThrowJsonSyntaxException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "invalid date";
    
    try {
      adapter.deserializeToDate(dateString);
      fail("JsonSyntaxException should be thrown");
    } catch(JsonSyntaxException e) {
      // success
    }
  }

  // Add more test cases for the remaining methods

}