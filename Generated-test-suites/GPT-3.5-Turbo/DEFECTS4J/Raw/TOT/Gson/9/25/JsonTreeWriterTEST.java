import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;
  
  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }
  
  @Test
  public void testGet() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John");
    assertEquals(expected, jsonTreeWriter.get());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGet_whenStackNotEmpty_throwIllegalStateException() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.get();
  }
  
  @Test
  public void testBeginArray() {
    jsonTreeWriter.beginArray();
    JsonArray expected = new JsonArray();
    assertEquals(expected, jsonTreeWriter.get());
  }
  
  @Test
  public void testEndArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("Value1");
    jsonTreeWriter.value("Value2");
    jsonTreeWriter.endArray();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("Value1"));
    expected.add(new JsonPrimitive("Value2"));
    assertEquals(expected, jsonTreeWriter.get());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArray_whenStackIsEmpty_throwIllegalStateException() {
    jsonTreeWriter.endArray();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArray_whenPendingNameIsNotNull_throwIllegalStateException() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.endArray();
  }
  
  // ... (continue with the other test cases)
  
  @Test(expected = IOException.class)
  public void testClose_whenStackIsNotEmpty_throwIOException() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.close();
  }
  
  @Test
  public void testClose() throws IOException {
    jsonTreeWriter.close();
    JsonElement expected = new JsonPrimitive("closed");
    assertEquals(expected, jsonTreeWriter.get());
  }
  
}