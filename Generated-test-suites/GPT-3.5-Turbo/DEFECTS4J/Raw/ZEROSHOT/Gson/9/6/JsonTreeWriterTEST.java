import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter writer;
  
  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
  }
  
  @Test
  public void testBeginArray() {
    try {
      writer.beginArray();
      JsonElement element = writer.get();
      assertTrue(element.isJsonArray());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testEndArray() {
    try {
      writer.beginArray();
      writer.endArray();
      JsonElement element = writer.get();
      assertTrue(element.isJsonNull());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testBeginObject() {
    try {
      writer.beginObject();
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testEndObject() {
    try {
      writer.beginObject();
      writer.endObject();
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testName() {
    try {
      writer.beginObject();
      writer.name("name");
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testValueString() {
    try {
      writer.beginObject();
      writer.value("test");
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testNullValue() {
    try {
      writer.beginObject();
      writer.nullValue();
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testValueBoolean() {
    try {
      writer.beginObject();
      writer.value(true);
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testValueDouble() {
    try {
      writer.beginObject();
      writer.value(3.14);
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testValueLong() {
    try {
      writer.beginObject();
      writer.value(100L);
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testValueNumber() {
    try {
      writer.beginObject();
      writer.value(42);
      JsonElement element = writer.get();
      assertTrue(element.isJsonObject());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testFlush() {
    try {
      writer.flush();
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testClose() {
    try {
      writer.close();
      JsonElement element = writer.get();
      assertTrue(element.isJsonPrimitive());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testGet() {
    try {
      writer.beginObject();
      JsonElement element = writer.get();
      assertNotNull(element);
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
}