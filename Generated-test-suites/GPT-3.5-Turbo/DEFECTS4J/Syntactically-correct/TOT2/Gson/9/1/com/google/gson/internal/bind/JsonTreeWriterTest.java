import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.*;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonNull);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonWriter jsonWriter = writer.beginArray();
    assertNotNull(jsonWriter);
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonWriter jsonWriter = writer.beginObject();
    assertNotNull(jsonWriter);
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> {
      writer.name("key");
    });
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> {
      writer.value("value");
    });
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> {
      writer.nullValue();
    });
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> {
      writer.value(true);
    });
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Double.NaN);
    });
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> {
      writer.value(123L);
    });
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Double.POSITIVE_INFINITY);
    });
  }

  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertDoesNotThrow(() -> {
      writer.flush();
    });
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> {
      writer.close();
    });
  }
}