import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.*;


public class JsonTreeWriterTest {

  @Test
  public void testBeginArray() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the beginArray() method
      writer.beginArray();

      // Check if the top element in the stack is a JsonArray
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonArray);
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testEndArray() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the beginArray() method
      writer.beginArray();

      // Call the endArray() method
      writer.endArray();

      // Check if the stack is empty
      assertTrue(writer.stack.isEmpty());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testBeginObject() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the beginObject() method
      writer.beginObject();

      // Check if the top element in the stack is a JsonObject
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonObject);
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testEndObject() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the beginObject() method
      writer.beginObject();

      // Call the endObject() method
      writer.endObject();

      // Check if the stack is empty
      assertTrue(writer.stack.isEmpty());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testName() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the beginObject() method
      writer.beginObject();

      // Call the name() method
      writer.name("name");

      // Check if the pendingName field is set
      assertEquals("name", writer.pendingName);
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testValueString() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the value() method with a String argument
      writer.value("value");

      // Check if the top element in the stack is a JsonPrimitive with the correct value
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonPrimitive);
      assertEquals("value", element.getAsString());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testNullValue() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the nullValue() method
      writer.nullValue();

      // Check if the top element in the stack is a JsonNull
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonNull);
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testValueBoolean() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the value() method with a boolean argument
      writer.value(true);

      // Check if the top element in the stack is a JsonPrimitive with the correct value
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonPrimitive);
      assertEquals(true, element.getAsBoolean());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testValueDouble() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the value() method with a double argument
      writer.value(1.23);

      // Check if the top element in the stack is a JsonPrimitive with the correct value
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonPrimitive);
      assertEquals(1.23, element.getAsDouble(), 0);
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testValueLong() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the value() method with a long argument
      writer.value(123L);

      // Check if the top element in the stack is a JsonPrimitive with the correct value
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonPrimitive);
      assertEquals(123L, element.getAsLong());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testValueNumber() {
    // Create a JsonTreeWriter instance
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      // Call the value() method with a Number argument
      writer.value(1);

      // Check if the top element in the stack is a JsonPrimitive with the correct value
      JsonElement element = writer.get();
      assertTrue(element instanceof JsonPrimitive);
      assertEquals(1, element.getAsInt());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }


}