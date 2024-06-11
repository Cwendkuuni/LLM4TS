import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertEquals("Begin Array", writer.peek(), writer.get());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.endArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Pass
    }
    writer.beginArray();
    writer.endArray();
    assertEquals("End Array", writer.peek(), writer.get());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    assertEquals("Begin Object", writer.peek(), writer.get());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.endObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Pass
    }
    writer.beginObject();
    writer.endObject();
    assertEquals("End Object", writer.peek(), writer.get());
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.name("key");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Pass
    }
    writer.beginObject();
    writer.name("key");
    assertEquals("Name", writer.pendingName, "key");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    assertEquals("Value String", writer.peek(), writer.get());
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    assertEquals("Null Value", writer.peek(), writer.get());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    assertEquals("Value Boolean", writer.peek(), writer.get());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.0);
    assertEquals("Value Double", writer.peek(), writer.get());
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1);
    assertEquals("Value Long", writer.peek(), writer.get());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.0);
    assertEquals("Value Number", writer.peek(), writer.get());
  }

  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    // Since flush does nothing, we cannot test its functionality
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.close();
      fail("Expected IOException");
    } catch (IOException e) {
      // Pass
    }
    writer.beginObject();
    try {
      writer.close();
      fail("Expected IOException");
    } catch (IOException e) {
      // Pass
    }
    writer.endObject();
    try {
      writer.close();
      fail("Expected IOException");
    } catch (IOException e) {
      // Pass
    }
  }

}