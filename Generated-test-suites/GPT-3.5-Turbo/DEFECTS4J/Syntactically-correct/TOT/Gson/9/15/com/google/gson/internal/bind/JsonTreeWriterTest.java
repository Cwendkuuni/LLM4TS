import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {

  // Test for beginArray()
  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
      JsonElement product = writer.get();
      assertTrue(product instanceof JsonArray);
      assertEquals(0, ((JsonArray) product).size());
    } catch (IOException e) {
      fail("IOException thrown when not expected");
    }
  }

  // Test for endArray()
  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray().endArray();
      JsonElement product = writer.get();
      assertTrue(product instanceof JsonArray);
      assertEquals(0, ((JsonArray) product).size());
    } catch (IOException e) {
      fail("IOException thrown when not expected");
    }
  }

  // Test for beginObject()
  @Test
  public void testBeginObject() {
    // TODO: Implement
  }

  // Test for endObject()
  @Test
  public void testEndObject() {
    // TODO: Implement
  }

  // Test for name(String name)
  @Test
  public void testName() {
    // TODO: Implement
  }

  // Test for value(String value)
  @Test
  public void testValueString() {
    // TODO: Implement
  }

  // Test for nullValue()
  @Test
  public void testNullValue() {
    // TODO: Implement
  }

  // Test for value(boolean value)
  @Test
  public void testValueBoolean() {
    // TODO: Implement
  }

  // Test for value(double value)
  @Test
  public void testValueDouble() {
    // TODO: Implement
  }

  // Test for value(long value)
  @Test
  public void testValueLong() {
    // TODO: Implement
  }

  // Test for value(Number value)
  @Test
  public void testValueNumber() {
    // TODO: Implement
  }

  // Test for flush()
  @Test
  public void testFlush() {
    // TODO: Implement
  }

  // Test for close()
  @Test
  public void testClose() {
    // TODO: Implement
  }

}