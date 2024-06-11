import static org.junit.Assert.*;

import org.junit.Before;
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
  
  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void get_WhenStackIsNotEmpty_ThrowsIllegalStateException() {
    jsonTreeWriter.beginArray();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.get());
  }

  @Test
  public void get_WhenStackIsEmpty_ReturnsProduct() {
    JsonElement expectedElement = JsonNull.INSTANCE;
    assertEquals(expectedElement, jsonTreeWriter.get());
  }

  @Test
  public void beginArray_CreatesNewJsonArrayAndAddsToStack() throws IOException {
    jsonTreeWriter.beginArray();
    JsonElement expectedElement = new JsonArray();
    assertEquals(expectedElement, jsonTreeWriter.peek());
  }

  @Test
  public void endArray_WhenStackIsEmpty_ThrowsIllegalStateException() throws IOException {
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
  public void endArray_WhenPendingNameIsNotNull_ThrowsIllegalStateException() throws IOException {
    jsonTreeWriter.beginObject().name("name");
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
  public void endArray_WhenTopElementInStackIsJsonArray_RemovesTopElementFromStack() throws IOException {
    jsonTreeWriter.beginArray().endArray();
    assertTrue(jsonTreeWriter.stack.isEmpty());
  }

  @Test
  public void endArray_WhenTopElementInStackIsNotJsonArray_ThrowsIllegalStateException() throws IOException {
    jsonTreeWriter.beginObject();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
  public void beginObject_CreatesNewJsonObjectAndAddsToStack() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement expectedElement = new JsonObject();
    assertEquals(expectedElement, jsonTreeWriter.peek());
  }

  @Test
  public void endObject_WhenStackIsEmpty_ThrowsIllegalStateException() throws IOException {
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
  }

  @Test
  public void endObject_WhenPendingNameIsNotNull_ThrowsIllegalStateException() throws IOException {
    jsonTreeWriter.beginArray().name("name");
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
  }

  @Test
  public void endObject_WhenTopElementInStackIsJsonObject_RemovesTopElementFromStack() throws IOException {
    jsonTreeWriter.beginObject().endObject();
    assertTrue(jsonTreeWriter.stack.isEmpty());
  }

  @Test
  public void endObject_WhenTopElementInStackIsNotJsonObject_ThrowsIllegalStateException() throws IOException {
    jsonTreeWriter.beginArray();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
  }

  @Test
  public void name_WhenStackIsEmpty_ThrowsIllegalStateException() throws IOException {
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.name("name"));
  }

  @Test
  public void name_WhenPendingNameIsNotNull_ThrowsIllegalStateException() throws IOException {
    jsonTreeWriter.beginObject().name("name");
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.name("name"));
  }

  @Test
  public void name_WhenTopElementInStackIsJsonObject_SetsPendingName() throws IOException {
    jsonTreeWriter.beginObject().name("name");
    assertEquals("name", jsonTreeWriter.pendingName);
  }

  @Test
  public void name_WhenTopElementInStackIsNotJsonObject_ThrowsIllegalStateException() throws IOException {
    jsonTreeWriter.beginArray();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.name("name"));
  }

  @Test
  public void value_WhenValueIsNull_CallsPutWithJsonNullInstance() throws IOException {
    jsonTreeWriter.value(null);
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsNotNull_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.value("value");
    assertEquals(new JsonPrimitive("value"), jsonTreeWriter.peek());
  }

  @Test
  public void nullValue_CallsPutWithJsonNullInstance() throws IOException {
    jsonTreeWriter.nullValue();
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsBoolean_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.value(true);
    assertEquals(new JsonPrimitive(true), jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsDoubleAndStrictModeIsEnabled_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(0.0);
    assertEquals(new JsonPrimitive(0.0), jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsDoubleAndStrictModeIsDisabled_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.setLenient(true);
    jsonTreeWriter.value(Double.NaN);
    assertEquals(new JsonPrimitive(Double.NaN), jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsLong_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.value(100L);
    assertEquals(new JsonPrimitive(100L), jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsNumberAndNull_CallsNullValue() throws IOException {
    jsonTreeWriter.value(null);
    assertNull(jsonTreeWriter.value((Number) null));
  }

  @Test
  public void value_WhenValueIsNumberAndStrictModeIsEnabled_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(123.456f);
    assertEquals(new JsonPrimitive(123.456f), jsonTreeWriter.peek());
  }

  @Test
  public void value_WhenValueIsNumberAndStrictModeIsDisabled_CallsPutWithJsonPrimitiveInstance() throws IOException {
    jsonTreeWriter.setLenient(true);
    jsonTreeWriter.value(Float.POSITIVE_INFINITY);
    assertEquals(new JsonPrimitive(Float.POSITIVE_INFINITY), jsonTreeWriter.peek());
  }
  
  @Test
  public void flush_DoesNotThrow() throws IOException {
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
  public void close_WhenStackIsNotEmpty_ThrowsIOException() throws IOException {
    assertThrows(IOException.class, () -> jsonTreeWriter.close());
  }

  @Test
  public void close_WhenStackIsEmpty_AddsSentinelClosedToJsonTreeWriter() throws IOException {
    jsonTreeWriter.close();
    JsonElement expectedElement = new JsonPrimitive("closed");
    assertEquals(expectedElement, jsonTreeWriter.stack.get(0));
  }
  
}