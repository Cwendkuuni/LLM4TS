import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonTreeReaderTest {

 private JsonTreeReader jsonTreeReader;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 JsonElement jsonElement = mock(JsonElement.class);
 when(jsonElement.isJsonArray()).thenReturn(true);
 when(jsonElement.getAsJsonArray()).thenReturn(createJsonArray());
 jsonTreeReader = new JsonTreeReader(jsonElement);
 }

 @Test
 public void testBeginArray() throws IOException {
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.endArray();
 assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
 }

 @Test
 public void testEndArray() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.endArray();
 assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
 }

 @Test
 public void testBeginObject() throws IOException {
 jsonTreeReader.beginObject();
 assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
 }

 @Test
 public void testEndObject() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.endObject();
 assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
 }

 @Test
 public void testHasNext() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element1"));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element2"));
 jsonTreeReader.push(createJsonPrimitive(2));
 jsonTreeReader.endArray();

 assertTrue(jsonTreeReader.hasNext());
 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());

 jsonTreeReader.skipValue();
 assertTrue(jsonTreeReader.hasNext());
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("element1", jsonTreeReader.nextName());

 jsonTreeReader.skipValue();
 assertFalse(jsonTreeReader.hasNext());
 assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());

 jsonTreeReader.nextName(); // consume name
 jsonTreeReader.nextString();
 jsonTreeReader.nextBoolean();
 jsonTreeReader.nextNull();
 jsonTreeReader.nextDouble();
 jsonTreeReader.nextLong();
 jsonTreeReader.nextInt();

 jsonTreeReader.close();
 jsonTreeReader = new JsonTreeReader(createJsonElement());

 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testPeek() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element1"));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element2"));
 jsonTreeReader.push(createJsonPrimitive(2));
 jsonTreeReader.endArray();

 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.skipValue();

 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("element1", jsonTreeReader.nextName());

 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("key1", jsonTreeReader.nextName());

 assertEquals(JsonToken.STRING, jsonTreeReader.peek());
 assertEquals("value1", jsonTreeReader.nextString());

 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
 }

 @Test
 public void testClose() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element1"));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element2"));
 jsonTreeReader.push(createJsonPrimitive(2));
 jsonTreeReader.endArray();
 jsonTreeReader.close();

 assertSame(SENTINEL_CLOSED, jsonTreeReader.peekStack());
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testPopStack() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element1"));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element2"));
 jsonTreeReader.push(createJsonPrimitive(2));
 jsonTreeReader.endArray();

 Object topOfStack = jsonTreeReader.popStack();
 assertEquals(JsonArray.class, topOfStack.getClass());
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.popStack();
 jsonTreeReader.close();

 assertNull(jsonTreeReader.peek());
 }

 @Test
 public void testExpect() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element1"));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("element2"));
 jsonTreeReader.push(createJsonPrimitive(2));
 jsonTreeReader.endArray();

 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.expect(JsonToken.STRING);
 jsonTreeReader.expect(JsonToken.NAME);
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.expect(JsonToken.END_OBJECT);
 jsonTreeReader.expect(JsonToken.END_ARRAY);

 jsonTreeReader.close();
 }

 @Test
 public void testNextName() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();

 assertEquals("key1", jsonTreeReader.nextName());
 }

 @Test
 public void testNextString() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive("\"string1\""));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();

 assertEquals("string1", jsonTreeReader.nextString());
 }

 @Test
 public void testNextBoolean() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive(true));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();

 assertTrue(jsonTreeReader.nextBoolean());
 }

 @Test
 public void testNextNull() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive(null));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();

 assertNull(jsonTreeReader.nextNull());
 }

 @Test
 public void testNextDouble() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive(1.1));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();

 assertEquals(1.1, jsonTreeReader.nextDouble(), 0.0);
 }

 @Test
 public void testNextLong() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive(1L));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();

 assertEquals(1L, jsonTreeReader.nextLong());
 }

 @Test
 public void testNextInt() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.push(createJsonPrimitive(1));
 jsonTreeReader.endArray();

 assertEquals(1, jsonTreeReader.nextInt());
 }

 @Test
 public void testSkipValue() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();

 jsonTreeReader.skipValue();
 jsonTreeReader.nextName();

 assertEquals("null", jsonTreeReader.nextName());
 }

 @Test
 public void testPromoteNameToValue() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.push(createJsonPrimitive("key1"));
 jsonTreeReader.push(createJsonPrimitive("value1"));
 jsonTreeReader.endObject();

 jsonTreeReader.promoteNameToValue();
 assertEquals(JsonPrimitive.class, jsonTreeReader.peekStack().getClass());
 jsonTreeReader.nextString();
 jsonTreeReader.promoteNameToValue();
 assertEquals(JsonObject.class, jsonTreeReader.peekStack().getClass());
 }

 private JsonElement createJsonElement() {
 return new JsonPrimitive("[]");
 }

 private JsonArray createJsonArray() {
 return new JsonArray();
 }

 private JsonPrimitive createJsonPrimitive(Object value) {
 return new JsonPrimitive(value);
 }
}