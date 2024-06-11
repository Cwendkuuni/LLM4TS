import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertNotNull(writer.get());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
    } catch (Exception e) {
      fail("Exception thrown during beginArray");
    }
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
      writer.endArray();
    } catch (Exception e) {
      fail("Exception thrown during endArray");
    }
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
    } catch (Exception e) {
      fail("Exception thrown during beginObject");
    }
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
      writer.endObject();
    } catch (Exception e) {
      fail("Exception thrown during endObject");
    }
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
      writer.name("key");
    } catch (Exception e) {
      fail("Exception thrown during name");
    }
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value("value");
    } catch (Exception e) {
      fail("Exception thrown during value(String)");
    }
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.nullValue();
    } catch (Exception e) {
      fail("Exception thrown during nullValue");
    }
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(true);
    } catch (Exception e) {
      fail("Exception thrown during value(boolean)");
    }
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(10.5);
    } catch (Exception e) {
      fail("Exception thrown during value(double)");
    }
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(10L);
    } catch (Exception e) {
      fail("Exception thrown during value(long)");
    }
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(10);
    } catch (Exception e) {
      fail("Exception thrown during value(Number)");
    }
  }
}