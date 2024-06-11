import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
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
  public void get_WhenEmptyStack_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.get();
    });
  }

  @Test
  public void get_WhenNonEmptyStack_ReturnsTopLevelElement() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("value");
    jsonTreeWriter.endArray();

    assertEquals("value", jsonTreeWriter.get().getAsJsonArray().get(0).getAsString());
  }

  @Test
  public void beginArray_ShouldAddNewJsonArrayToStack() {
    jsonTreeWriter.beginArray();

    assertEquals(JsonArray.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void endArray_WhenStackIsEmpty_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endArray();
    });
  }

  @Test
  public void endArray_WhenPendingNameIsNotNull_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginObject();
      jsonTreeWriter.name("name");
      jsonTreeWriter.endArray();
    });
  }

  @Test
  public void endArray_WhenStackTopIsJsonArray_RemovesElementFromStack() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    assertTrue(jsonTreeWriter.stack.isEmpty());
  }

  @Test
  public void endArray_WhenStackTopIsNotJsonArray_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginObject();
      jsonTreeWriter.endArray();
    });
  }

  @Test
  public void beginObject_ShouldAddNewJsonObjectToStack() {
    jsonTreeWriter.beginObject();

    assertEquals(JsonObject.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void endObject_WhenStackIsEmpty_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
  }

  @Test
  public void endObject_WhenPendingNameIsNotNull_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginObject();
      jsonTreeWriter.name("name");
      jsonTreeWriter.endObject();
    });
  }

  @Test
  public void endObject_WhenStackTopIsJsonObject_RemovesElementFromStack() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    assertTrue(jsonTreeWriter.stack.isEmpty());
  }

  @Test
  public void endObject_WhenStackTopIsNotJsonObject_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginArray();
      jsonTreeWriter.endObject();
    });
  }

  @Test
  public void name_WhenStackIsEmpty_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.name("name");
    });
  }

  @Test
  public void name_WhenPendingNameIsNotNull_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginObject();
      jsonTreeWriter.name("name1");
      jsonTreeWriter.name("name2");
    });
  }

  @Test
  public void name_WhenStackTopIsJsonObject_SetsPendingName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");

    assertEquals("name", jsonTreeWriter.pendingName);
  }

  @Test
  public void name_WhenStackTopIsNotJsonObject_ThrowsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginArray();
      jsonTreeWriter.name("name");
    });
  }

  @Test
  public void value_WhenValueIsNull_AddsJsonNullToStack() {
    jsonTreeWriter.value(null);

    assertTrue(jsonTreeWriter.peek().isJsonNull());
  }

  @Test
  public void value_WhenStackTopIsJsonObject_AddsValueToJsonObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("value");

    assertEquals("value", jsonTreeWriter.peek().getAsJsonObject().get("name").getAsString());
  }

  @Test
  public void value_WhenStackTopIsNotJsonObject_AddsValueToJsonArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("value");

    assertEquals("value", jsonTreeWriter.peek().getAsJsonArray().get(0).getAsString());
  }

  @Test
  public void nullValue_Always_AddsJsonNullToStack() {
    jsonTreeWriter.nullValue();

    assertTrue(jsonTreeWriter.peek().isJsonNull());
  }

  @Test
  public void value_WhenStackTopIsJsonObjectAndValueIsBoolean_AddsValueToJsonObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(true);

    assertEquals(true, jsonTreeWriter.peek().getAsJsonObject().get("name").getAsBoolean());
  }

  @Test
  public void value_WhenValueIsNaNAndLenientIsFalse_ThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.NaN);
    });
  }

  @Test
  public void value_WhenValueIsInfinityAndLenientIsFalse_ThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    });
  }

  @Test
  public void value_WhenValueIsNegativeInfinityAndLenientIsFalse_ThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
    });
  }

  @Test
  public void value_WhenStackTopIsJsonObjectAndValueIsDouble_AddsValueToJsonObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(10.5);

    assertEquals(10.5, jsonTreeWriter.peek().getAsJsonObject().get("name").getAsDouble(), 0.00001);
  }

  @Test
  public void value_WhenStackTopIsJsonObjectAndValueIsLong_AddsValueToJsonObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(100L);

    assertEquals(100L, jsonTreeWriter.peek().getAsJsonObject().get("name").getAsLong());
  }

  @Test
  public void value_WhenStackTopIsJsonObjectAndValueIsNumber_AddsValueToJsonObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(20);

    assertEquals(20, jsonTreeWriter.peek().getAsJsonObject().get("name").getAsInt());
  }

  @Test
  public void flush_Always_DoesNotThrowException() {
    assertDoesNotThrow(() -> {
      jsonTreeWriter.flush();
    });
  }

  @Test
  public void close_WhenStackNotEmpty_ThrowsIOException() {
    assertThrows(IOException.class, () -> {
      jsonTreeWriter.beginArray();
      jsonTreeWriter.close();
    });
  }

  @Test
  public void close_WhenStackIsEmpty_MarksStackWithSentinelClosed() throws IOException {
    jsonTreeWriter.close();

    assertEquals(JsonPrimitive.class, jsonTreeWriter.stack.get(0).getClass());
    assertEquals("closed", jsonTreeWriter.stack.get(0).getAsString());
  }
}