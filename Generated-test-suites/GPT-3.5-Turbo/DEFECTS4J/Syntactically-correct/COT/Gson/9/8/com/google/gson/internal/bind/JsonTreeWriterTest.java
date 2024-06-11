import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter writer;
  private StringWriter stringWriter;
  
  @Before
  public void setUp() {
    stringWriter = new StringWriter();
    writer = new JsonTreeWriter(new JsonWriter(stringWriter));
  }
  
  @Test
  public void testBeginArray() throws IOException {
    writer.beginArray();
    writer.endArray();
    assertEquals("[]", stringWriter.toString());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    writer.beginObject();
    writer.endObject();
    assertEquals("{}", stringWriter.toString());
  }
  
  @Test
  public void testBooleanValue() throws IOException {
    writer.value(true);
    assertEquals("true", stringWriter.toString());
  }
  
  @Test
  public void testNullValue() throws IOException {
    writer.nullValue();
    assertEquals("null", stringWriter.toString());
  }
  
  @Test
  public void testStringValue() throws IOException {
    writer.value("test");
    assertEquals("\"test\"", stringWriter.toString());
  }
  
  @Test
  public void testNumberValue() throws IOException {
    writer.value(123);
    assertEquals("123", stringWriter.toString());
  }
  
  @Test
  public void testFlush() throws IOException {
    writer.flush();
    // No assertion as it only flushes the writer
  }
  
  @Test(expected = IOException.class)
  public void testClose_IncompleteDocument() throws IOException {
    writer.beginArray();
    writer.close();
  }
  
  @Test
  public void testClose_CompleteDocument() throws IOException {
    writer.beginArray();
    writer.endArray();
    writer.close();
    assertTrue(writer.isClosed());
  }
  
  @Test
  public void testSerializeNulls() throws IOException {
    writer.setSerializeNulls(true);
    writer.nullValue();
    assertEquals("null", stringWriter.toString());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValue_NaN() throws IOException {
    writer.value(Double.NaN);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValue_Infinity() throws IOException {
    writer.value(Double.POSITIVE_INFINITY);
  }
  
  @Test
  public void testValue_Long() throws IOException {
    writer.value(123L);
    assertEquals("123", stringWriter.toString());
  }
  
  @Test
  public void testValue_Double() throws IOException {
    writer.value(1.23);
    assertEquals("1.23", stringWriter.toString());
  }
  
  @Test
  public void testValue_Number() throws IOException {
    writer.value(123);
    assertEquals("123", stringWriter.toString());
  }
  
  @Test
  public void testName() throws IOException {
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    assertEquals("{\"key\":\"value\"}", stringWriter.toString());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArray_Unexpected() throws IOException {
    writer.endArray();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndObject_Unexpected() throws IOException {
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testName_NoObject() throws IOException {
    writer.name("key");
  }

  @Test(expected = IllegalStateException.class)
  public void testNullValue_NoObject() throws IOException {
    writer.nullValue();
  }

}