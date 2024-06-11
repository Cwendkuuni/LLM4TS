import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReader_Constructor_JsonElement() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement());
    assertNotNull(jsonTreeReader);
  }

  @Test
  public void testBeginArray_NullInput_ThrowsIOException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());
    jsonTreeReader.beginArray();

    expect(JsonToken.BEGIN_ARRAY).atLeastOnce(jsonTreeReader);

    try {
      jsonTreeReader.beginArray();
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testEndArray_NullInput_ThrowsIOException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());
    jsonTreeReader.endArray();

    expect(JsonToken.END_ARRAY).atLeastOnce(jsonTreeReader);

    jsonTreeReader.endArray();

    try {
      jsonTreeReader.endArray();
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testBeginObject_NullInput_ThrowsIOException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());
    jsonTreeReader.beginObject();

    expect(JsonToken.BEGIN_OBJECT).atLeastOnce(jsonTreeReader);

    try {
      jsonTreeReader.beginObject();
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testEndObject_NullInput_ThrowsIOException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());
    jsonTreeReader.endObject();

    expect(JsonToken.END_OBJECT).atLeastOnce(jsonTreeReader);

    jsonTreeReader.endObject();

    try {
      jsonTreeReader.endObject();
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testHasNext_EmptyStack_ReturnsFalse() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testPeek_EndDocument_ReturnsEndDocument() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonNull());
    jsonTreeReader.close();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testPeek_InvalidToken_ThrowsAssertionError() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());
    jsonTreeReader.popStack();

    try {
      jsonTreeReader.peek();
      fail("Expected AssertionError to be thrown");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testPopStack_EmptyStack_ThrowsAssertionError() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    try {
      jsonTreeReader.popStack();
      fail("Expected AssertionError to be thrown");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testExpect_InvalidToken_ThrowsIllegalStateException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());
    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);

    try {
      jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testNextName_EmptyArray_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    assertEquals(null, jsonTreeReader.nextName());
  }

  @Test
  public void testNextName_EmptyObject_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    assertEquals(null, jsonTreeReader.nextName());
  }

  @Test
  public void testNextName_InvalidToken_ThrowsIllegalStateException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    try {
      jsonTreeReader.nextName();
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testNextString_EmptyArray_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.popStack();

    assertEquals(null, jsonTreeReader.nextString());
  }

  @Test
  public void testNextString_EmptyObject_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    assertEquals(null, jsonTreeReader.nextString());
  }

  @Test
  public void testNextString_InvalidToken_ThrowsIllegalStateException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    try {
      jsonTreeReader.nextString();
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testNextBoolean_EmptyArray_ReturnsFalse() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.popStack();

    assertEquals(false, jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextBoolean_EmptyObject_ReturnsFalse() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    assertEquals(false, jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextBoolean_TrueValue_ReturnsTrue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonBooleanTrue());

    jsonTreeReader.popStack();

    assertEquals(true, jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextBoolean_FalseValue_ReturnsFalse() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonBooleanFalse());

    jsonTreeReader.popStack();

    assertEquals(false, jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextNull_EmptyArray_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.popStack();

    jsonTreeReader.nextNull();

    assertEquals(null, jsonTreeReader.nextString());
    assertEquals(0, jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testNextNull_EmptyObject_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    jsonTreeReader.nextNull();

    assertEquals(null, jsonTreeReader.nextString());
    assertEquals(0, jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testNextNull_NullValue_ReturnsNull() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonNull());

    jsonTreeReader.popStack();

    jsonTreeReader.nextNull();

    assertEquals(null, jsonTreeReader.nextString());
    assertEquals(0, jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testNextDouble_EmptyArray_ReturnsNaN() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.popStack();

    assertEquals(Double.NaN, jsonTreeReader.nextDouble(), 0.0);
  }

  @Test
  public void testNextDouble_EmptyObject_ReturnsNaN() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    assertEquals(Double.NaN, jsonTreeReader.nextDouble(), 0.0);
  }

  @Test
  public void testNextDouble_InvalidString_ThrowsNumberFormatException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonStringInvalidDouble());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    try {
      jsonTreeReader.nextDouble();
      fail("Expected NumberFormatException to be thrown");
    } catch (NumberFormatException e) {
      // Expected
    }
  }

  @Test
  public void testNextDouble_ValidString_ReturnsDoubleValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonStringDouble());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    assertEquals(123.45, jsonTreeReader.nextDouble(), 0.0);
  }

  @Test
  public void testNextLong_EmptyArray_ReturnsMinValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.popStack();

    assertEquals(-9223372036854775807L, jsonTreeReader.nextLong());
  }

  @Test
  public void testNextLong_EmptyObject_ReturnsMinValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    assertEquals(-9223372036854775807L, jsonTreeReader.nextLong());
  }

  @Test
  public void testNextLong_InvalidString_ThrowsNumberFormatException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonStringInvalidLong());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    try {
      jsonTreeReader.nextLong();
      fail("Expected NumberFormatException to be thrown");
    } catch (NumberFormatException e) {
      // Expected
    }
  }

  @Test
  public void testNextLong_ValidString_ReturnsLongValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonStringLong());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    assertEquals(1234567890L, jsonTreeReader.nextLong());
  }

  @Test
  public void testNextInt_EmptyArray_ReturnsMinValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.popStack();

    assertEquals(-2147483648, jsonTreeReader.nextInt());
  }

  @Test
  public void testNextInt_EmptyObject_ReturnsMinValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.popStack();

    assertEquals(-2147483648, jsonTreeReader.nextInt());
  }

  @Test
  public void testNextInt_InvalidString_ThrowsNumberFormatException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonStringInvalidInt());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    try {
      jsonTreeReader.nextInt();
      fail("Expected NumberFormatException to be thrown");
    } catch (NumberFormatException e) {
      // Expected
    }
  }

  @Test
  public void testNextInt_ValidString_ReturnsIntValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonStringInt());

    jsonTreeReader.popStack();
    jsonTreeReader.popStack();

    assertEquals(12345, jsonTreeReader.nextInt());
  }

  @Test
  public void testClose_NullInput_ThrowsAssertionError() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    try {
      jsonTreeReader.close();
      fail("Expected AssertionError to be thrown");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testClose_ValidInput_DoesNotThrowException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.close();
  }

  @Test
  public void testSkipValue_NullInput_DoesNotThrowException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.skipValue();

    assertEquals(null, jsonTreeReader.nextString());
    assertEquals(0, jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testPromoteNameToValue_EmptyArray_DoesNotThrowException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArray());

    jsonTreeReader.promoteNameToValue();

    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }

  @Test
  public void testPromoteNameToValue_EmptyObject_DoesNotThrowException() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObject());

    jsonTreeReader.promoteNameToValue();

    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testPromoteNameToValue_ValidArray_ReturnsValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonArrayWithValue());

    jsonTreeReader.promoteNameToValue();

    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonTreeReader.peekStack();
    assertEquals(123, jsonPrimitive.getAsInt());
  }

  @Test
  public void testPromoteNameToValue_ValidObject_ReturnsValue() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonObjectWithValue());

    jsonTreeReader.promoteNameToValue();

    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonTreeReader.peekStack();
    assertEquals("value", jsonPrimitive.getAsString());
  }

  private JsonElement createJsonArray() {
    return new JsonArray();
  }

  private JsonElement createJsonArrayWithValue() {
    return new JsonArray().add(new JsonPrimitive(123));
  }

  private JsonElement createJsonBooleanFalse() {
    return new JsonPrimitive(false);
  }

  private JsonElement createJsonBooleanTrue() {
    return new JsonPrimitive(true);
  }

  private JsonElement createJsonElement() {
    return null;
  }

  private JsonElement createJsonObject() {
    return new JsonObject();
  }

  private JsonElement createJsonObjectWithValue() {
    return new JsonObject().addProperty("value", new JsonPrimitive("value"));
  }

  private JsonElement createJsonNull() {
    return JsonNull.INSTANCE;
  }

  private JsonElement createJsonStringDouble() {
    return new JsonPrimitive("123.45");
  }

  private JsonElement createJsonStringInt() {
    return new JsonPrimitive("12345");
  }

  private JsonElement createJsonStringInvalidDouble() {
    return new JsonPrimitive("not a number");
  }

  private JsonElement createJsonStringInvalidInt() {
    return new JsonPrimitive("not an integer");
  }

  private JsonElement createJsonStringInvalidLong() {
    return new JsonPrimitive("not a long");
  }

}