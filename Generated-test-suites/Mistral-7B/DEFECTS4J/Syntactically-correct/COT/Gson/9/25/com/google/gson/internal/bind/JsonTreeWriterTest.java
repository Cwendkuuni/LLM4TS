import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setup() {
    writer = new JsonTreeWriter();
  }

  @Test
  public void testGetReturnsEmptyWhenNoElementAdded() {
    assertNull(writer.get());
  }

  @Test
  public void testGetReturnsAddedElement() {
    writer.value("hello");
    assertEquals("hello", writer.get().getAsString());
  }

  @Test
  public void testPeekReturnsAddedElement() {
    writer.value("hello");
    JsonElement element = writer.peek();
    assertEquals("hello", element.getAsString());
  }

  @Test
  public void testStackIsEmptyBeforeFirstAddition() {
    assertFalse(writer.stack.isEmpty());
  }

  @Test
  public void testStackContainsAddedElement() {
    writer.value("hello");
    assertTrue(writer.stack.contains(writer.product));
  }

  @Test
  public void testBeginArrayAddsArrayToStack() {
    writer.beginArray();
    JsonElement array = writer.peek();
    assertTrue(array instanceof JsonArray);
  }

  @Test
  public void testBeginArrayReturnsWriterInstance() {
    JsonWriter arrayWriter = writer.beginArray();
    assertSame(writer, arrayWriter);
  }

  @Test
  public void testEndArrayRemovesArrayFromStack() {
    writer.beginArray();
    writer.endArray();
    assertFalse(writer.stack.isEmpty());
    assertFalse(writer.stack.contains(writer.product));
  }

  @Test
  public void testEndArrayThrowsIOExceptionWhenNoArrayPresent() {
    writer.value("hello");
    try {
      writer.endArray();
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testBeginObjectAddsObjectToStack() {
    writer.beginObject();
    JsonElement object = writer.peek();
    assertTrue(object instanceof JsonObject);
  }

  @Test
  public void testBeginObjectReturnsWriterInstance() {
    JsonWriter objectWriter = writer.beginObject();
    assertSame(writer, objectWriter);
  }

  @Test
  public void testEndObjectRemovesObjectFromStack() {
    writer.beginObject();
    writer.endObject();
    assertFalse(writer.stack.isEmpty());
    assertFalse(writer.stack.contains(writer.product));
  }

  @Test
  public void testEndObjectThrowsIOExceptionWhenNoObjectPresent() {
    writer.value("hello");
    try {
      writer.endObject();
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testNameSetsPendingName() {
    writer.name("name");
    assertEquals("name", writer.pendingName);
  }

  @Test
  public void testNullValueAddsNullToStack() {
    writer.nullValue();
    assertEquals(JsonNull.INSTANCE, writer.peek());
  }

  @Test
  public void testNullValueReturnsWriterInstance() {
    JsonWriter nullWriter = writer.nullValue();
    assertSame(writer, nullWriter);
  }

  @Test
  public void testValueAddsStringToStack() {
    writer.value("hello");
    assertEquals("hello", writer.peek().getAsString());
  }

  @Test
  public void testValueReturnsWriterInstance() {
    JsonWriter stringWriter = writer.value("hello");
    assertSame(writer, stringWriter);
  }

  @Test
  public void testValueAddsBooleanToStack() {
    writer.value(true);
    JsonPrimitive booleanPrimitive = (JsonPrimitive) writer.peek();
    assertEquals(true, booleanPrimitive.getAsBoolean());
  }

  @Test
  public void testValueReturnsWriterInstanceWhenWritingBoolean() {
    JsonWriter booleanWriter = writer.value(true);
    assertSame(writer, booleanWriter);
  }

  @Test
  public void testValueAddsNumberToStack() {
    writer.value(123);
    JsonPrimitive numberPrimitive = (JsonPrimitive) writer.peek();
    assertEquals(123, numberPrimitive.getAsInt());
  }

  @Test
  public void testValueReturnsWriterInstanceWhenWritingNumber() {
    JsonWriter numberWriter = writer.value(123);
    assertSame(writer, numberWriter);
  }

  @Test
  public void testValueAddsDoubleToStack() {
    writer.value(123.45);
    JsonPrimitive doublePrimitive = (JsonPrimitive) writer.peek();
    assertEquals(123.45, doublePrimitive.getAsDouble(), 0.001);
  }

  @Test
  public void testValueReturnsWriterInstanceWhenWritingDouble() {
    JsonWriter doubleWriter = writer.value(123.45);
    assertSame(writer, doubleWriter);
  }

  @Test
  public void testFlushHasNoEffect() {
    writer.value("hello");
    writer.flush();
    assertEquals("hello", writer.get().getAsString());
  }

  @Test(expected = IOException.class)
  public void testCloseThrowsIOExceptionWhenNoElementAdded() {
    writer.close();
  }

  @Test
  public void testCloseAddsSentinalToStack() {
    writer.close();
    assertEquals(JsonTreeWriter.SENTINEL_CLOSED, writer.peek());
  }

  @Test
  public void testCloseReturnsWriterInstance() {
    JsonWriter closedWriter = writer.close();
    assertSame(writer, closedWriter);
  }
}