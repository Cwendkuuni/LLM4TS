import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() {
    // Test normal array
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    try {
      reader.beginArray();
      assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testEndArray() {
    // Test empty array
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    try {
      reader.endArray();
      assertEquals(JsonToken.END_ARRAY, reader.peek());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  // Other test cases for public methods in JsonTreeReader...

}