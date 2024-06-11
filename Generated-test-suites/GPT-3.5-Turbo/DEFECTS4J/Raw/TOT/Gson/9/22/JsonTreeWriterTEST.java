import static org.junit.Assert.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testGet_EmptyWriter_ReturnsJsonNull() {
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result instanceof JsonNull);
  }

  @Test
  public void testGet_AfterWritingJsonObject_ReturnsJsonObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement result = jsonTreeWriter.get();

    assertTrue(result instanceof JsonObject);
  }

  @Test(expected = IllegalStateException.class)
  public void testGet_AfterIncompleteDocument_ThrowsIllegalStateException() {
    jsonTreeWriter.beginObject();

    jsonTreeWriter.get();
  }

  @Test
  public void testBeginArray_ValidInput_CreatesJsonArray() {
    jsonTreeWriter.beginArray();

    JsonElement result = peekStack();

    assertTrue(result.isJsonArray());
  }

  @Test
  public void testEndArray_ValidInput_RemovesJsonArrayFromStack() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    JsonElement result = peekStack();

    assertFalse(result.isJsonArray());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_WhenNoArrayLeftOnStack_ThrowsIllegalStateException() {
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject_ValidInput_CreatesJsonObject() {
    jsonTreeWriter.beginObject();

    JsonElement result = peekStack();

    assertTrue(result.isJsonObject());
  }

  @Test
  public void testEndObject_ValidInput_RemovesJsonObjectFromStack() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement result = peekStack();

    assertFalse(result.isJsonObject());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_WhenNoObjectLeftOnStack_ThrowsIllegalStateException() {
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_WhenInObject_CreatesJsonPrimitive() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");

    JsonElement result = getLastChildFromObject();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testName_WhenNotInObject_ThrowsIllegalStateException() {
    try {
      jsonTreeWriter.name("key");

      fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // No need to do anything, the test passed
    }
  }

  @Test
  public void testName_WhenInObject_SetsCorrectKey() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");

    JsonElement result = getLastChildFromObject();
    String key = result.getAsString();

    assertEquals("key", key);
  }

  @Test
  public void testValue_StringInput_CreatesJsonPrimitive() {
    String value = "test";
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testValue_StringInput_SetsCorrectValue() {
    String value = "test";
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();
    String resultValue = result.getAsString();

    assertEquals(value, resultValue);
  }

  @Test(expected = NullPointerException.class)
  public void testValue_NullInput_ThrowsNullPointerException() {
    jsonTreeWriter.value((String) null);
  }

  @Test
  public void testNullValue_CreatesJsonNull() {
    jsonTreeWriter.nullValue();

    JsonElement result = peekStack();

    assertTrue(result.isJsonNull());
  }

  @Test
  public void testValue_BooleanInput_CreatesJsonPrimitive() {
    boolean value = true;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testValue_BooleanInput_SetsCorrectValue() {
    boolean value = true;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();
    boolean resultValue = result.getAsBoolean();

    assertEquals(value, resultValue);
  }

  @Test
  public void testValue_DoubleInput_CreatesJsonPrimitive() {
    double value = 3.14;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_DoubleInput_WithNaN_ThrowsIllegalArgumentException() {
    double value = Double.NaN;
    jsonTreeWriter.value(value);
  }

  @Test
  public void testValue_DoubleInput_WithInfinity_CreatesJsonPrimitive() {
    double value = Double.POSITIVE_INFINITY;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testValue_DoubleInput_WithInfinity_SetsCorrectValue() {
    double value = Double.POSITIVE_INFINITY;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();
    double resultValue = result.getAsDouble();

    assertTrue(Double.isInfinite(resultValue));
    assertEquals(value, resultValue, 0.0);
  }

  @Test
  public void testValue_FloatInput_CreatesJsonPrimitive() {
    float value = 3.14f;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testValue_LongInput_CreatesJsonPrimitive() {
    long value = 1234567890L;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testValue_LongInput_SetsCorrectValue() {
    long value = 1234567890L;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();
    long resultValue = result.getAsLong();

    assertEquals(value, resultValue);
  }

  @Test
  public void testValue_IntegerInput_CreatesJsonPrimitive() {
    int value = 12345;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();

    assertTrue(result.isJsonPrimitive());
  }

  @Test
  public void testValue_IntegerInput_SetsCorrectValue() {
    int value = 12345;
    jsonTreeWriter.value(value);

    JsonElement result = peekStack();
    int resultValue = result.getAsInt();

    assertEquals(value, resultValue);
  }

  private JsonElement peekStack() {
    List<JsonElement> stack = getStackFromJsonTreeWriter();
    return stack.get(stack.size() - 1);
  }

  private JsonElement getLastChildFromObject() {
    JsonObject jsonObject = (JsonObject) peekStack();
    int lastChildIndex = jsonObject.size() - 1;
    List<String> keys = new ArrayList<>(jsonObject.keySet());
    String lastKey = keys.get(lastChildIndex);
    return jsonObject.get(lastKey);
  }

  private List<JsonElement> getStackFromJsonTreeWriter() {
    try {
      jsonTreeWriter.getClass().getDeclaredField("stack").setAccessible(true);
      return (List<JsonElement>) jsonTreeWriter.getClass().getDeclaredField("stack")
          .get(jsonTreeWriter);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }
}