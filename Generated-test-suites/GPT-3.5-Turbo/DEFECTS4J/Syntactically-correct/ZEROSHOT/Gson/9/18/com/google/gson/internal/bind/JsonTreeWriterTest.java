import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = writer.get();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
    } catch (Exception e) {
      fail("beginArray threw an exception");
    }
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.endArray();
    } catch (Exception e) {
      fail("endArray threw an exception");
    }
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
    } catch (Exception e) {
      fail("beginObject threw an exception");
    }
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.endObject();
    } catch (Exception e) {
      fail("endObject threw an exception");
    }
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.name("test");
    } catch (Exception e) {
      fail("name threw an exception");
    }
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value("test");
    } catch (Exception e) {
      fail("value (string) threw an exception");
    }
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.nullValue();
    } catch (Exception e) {
      fail("nullValue threw an exception");
    }
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(true);
    } catch (Exception e) {
      fail("value (boolean) threw an exception");
    }
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(3.14);
    } catch (Exception e) {
      fail("value (double) threw an exception");
    }
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(42L);
    } catch (Exception e) {
      fail("value (long) threw an exception");
    }
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(123);
    } catch (Exception e) {
      fail("value (number) threw an exception");
    }
  }

  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.flush();
    } catch (Exception e) {
      fail("flush threw an exception");
    }
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.close();
    } catch (Exception e) {
      fail("close threw an exception");
    }
  }
  
}