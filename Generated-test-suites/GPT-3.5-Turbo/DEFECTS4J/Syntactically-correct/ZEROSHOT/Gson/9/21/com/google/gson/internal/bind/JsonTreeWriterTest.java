import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {
  
  private static JsonTreeWriter jsonTreeWriter;
  
  @BeforeClass
  public static void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }
  
  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonArray());
  }
  
  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonNull());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonObject());
  }
  
  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonNull());
  }
  
  @Test
  public void testName() throws IOException {
    jsonTreeWriter.name("testName");
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonObject());
  }
  
  @Test
  public void testValueString() throws IOException {
    jsonTreeWriter.value("testString");
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
  }
  
  @Test
  public void testNullValue() throws IOException {
    jsonTreeWriter.nullValue();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonNull());
  }
  
  @Test
  public void testValueBoolean() throws IOException {
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
  }
  
  @Test
  public void testValueDouble() throws IOException {
    jsonTreeWriter.value(3.14);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
  }
  
  @Test
  public void testValueLong() throws IOException {
    jsonTreeWriter.value(100L);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
  }
  
  @Test
  public void testValueNumber() throws IOException {
    jsonTreeWriter.value(123);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
  }
  
  @Test(expected=IllegalStateException.class)
  public void testIncompleteDocument() throws IOException {
    jsonTreeWriter.close();
  }
  
  @Test
  public void testGet() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonObject());
  }
  
}