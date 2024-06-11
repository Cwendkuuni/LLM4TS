import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 private JsonTreeReader createJsonTreeReader(JsonElement jsonElement) {
 return new JsonTreeReader(jsonElement);
 }

 @Test
 public void testBeginArray() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonElement() {
 @Override public JsonPrimitive getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonArray getAsJsonArray() {
 return new JsonArray();
 }

 @Override public JsonObject getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonNull getAsJsonNull() {
 throw new UnsupportedOperationException();
 }

 @Override public String getAsString() {
 throw new UnsupportedOperationException();
 }

 @Override public double getAsDouble() {
 throw new UnsupportedOperationException();
 }

 @Override public float getAsFloat() {
 throw new UnsupportedOperationException();
 }

 @Override public long getAsLong() {
 throw new UnsupportedOperationException();
 }

 @Override public int getAsInt() {
 throw new UnsupportedOperationException();
 }

 @Override public boolean getAsBoolean() {
 throw new UnsupportedOperationException();
 }

 @Override public byte[] getAsBytes() {
 throw new UnsupportedOperationException();
 }

 @Override public char getAsCharacter() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonObject getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonArray getAsJsonArray() {
 return new JsonArray();
 }

 @Override public JsonNull getAsJsonNull() {
 throw new UnsupportedOperationException();
 }

 @Override public void beginObject() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void endObject() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public boolean hasNext() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public JsonToken peek() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void close() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public String nextName() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public String nextString() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public boolean nextBoolean() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void nextNull() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public double nextDouble() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public long nextLong() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public int nextInt() throws IOException {
 throw new UnsupportedOperationException();
 }
 });
 jsonTreeReader.beginArray();
 assertTrue(jsonTreeReader.hasNext());
 jsonTreeReader.endArray();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testBeginObject() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject());
 jsonTreeReader.beginObject();
 assertTrue(jsonTreeReader.hasNext());
 assertEquals("name1", jsonTreeReader.nextName());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBeginObjectWithNull() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(null);
 jsonTreeReader.beginObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectAfterEndDocument() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonElement() {
 @Override public JsonPrimitive getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonArray getAsJsonArray() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonObject getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonNull getAsJsonNull() {
 throw new UnsupportedOperationException();
 }

 @Override public String getAsString() {
 throw new UnsupportedOperationException();
 }

 @Override public double getAsDouble() {
 throw new UnsupportedOperationException();
 }

 @Override public float getAsFloat() {
 throw new UnsupportedOperationException();
 }

 @Override public long getAsLong() {
 throw new UnsupportedOperationException();
 }

 @Override public int getAsInt() {
 throw new UnsupportedOperationException();
 }

 @Override public boolean getAsBoolean() {
 throw new UnsupportedOperationException();
 }

 @Override public byte[] getAsBytes() {
 throw new UnsupportedOperationException();
 }

 @Override public char getAsCharacter() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonObject getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonArray getAsJsonArray() {
 throw new UnsupportedOperationException();
 }

 @Override public JsonNull getAsJsonNull() {
 throw new UnsupportedOperationException();
 }

 @Override public void beginObject() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void endObject() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public boolean hasNext() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public JsonToken peek() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void close() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public String nextName() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public String nextString() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public boolean nextBoolean() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void nextNull() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public double nextDouble() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public long nextLong() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public int nextInt() throws IOException {
 throw new UnsupportedOperationException();
 }
 });
 jsonTreeReader.close();
 jsonTreeReader.beginObject();
 }

 @Test
 public void testNextName() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("name1", new JsonPrimitive("value1"));
 addProperty("name2", new JsonPrimitive("value2"));
 addProperty("name3", new JsonPrimitive("value3"));
 }});
 jsonTreeReader.beginObject();
 assertEquals("name1", jsonTreeReader.nextName());
 assertEquals("value1", jsonTreeReader.nextString());
 jsonTreeReader.promoteNameToValue();
 assertEquals("value1", jsonTreeReader.nextString());
 assertEquals("name2", jsonTreeReader.nextName());
 assertEquals("value2", jsonTreeReader.nextString());
 jsonTreeReader.promoteNameToValue();
 assertEquals("value2", jsonTreeReader.nextString());
 assertEquals("name3", jsonTreeReader.nextName());
 assertEquals("value3", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testBeginArrayWithNull() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(null);
 jsonTreeReader.beginArray();
 }

 @Test
 public void testEndArrayWithNull() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonArray());
 jsonTreeReader.endArray();
 jsonTreeReader.beginArray();
 }

 @Test
 public void testEndObjectWithNull() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject());
 jsonTreeReader.endObject();
 jsonTreeReader.beginObject();
 }

 @Test
 public void testHasNext() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("name1", new JsonPrimitive("value1"));
 addProperty("name2", new JsonPrimitive("value2"));
 addProperty("name3", new JsonPrimitive("value3"));
 }});
 jsonTreeReader.beginObject();
 assertTrue(jsonTreeReader.hasNext());
 assertEquals("name1", jsonTreeReader.nextName());
 assertTrue(jsonTreeReader.hasNext());
 assertEquals("name2", jsonTreeReader.nextName());
 assertTrue(jsonTreeReader.hasNext());
 assertEquals("name3", jsonTreeReader.nextName());
 assertFalse(jsonTreeReader.hasNext());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testPeek() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("name1", new JsonPrimitive("value1"));
 addProperty("name2", new JsonPrimitive("value2"));
 addProperty("name3", new JsonPrimitive("value3"));
 }});
 jsonTreeReader.beginObject();
 assertEquals("name1", jsonTreeReader.nextName());
 assertEquals("value1", jsonTreeReader.nextString());
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("name2", jsonTreeReader.nextName());
 assertEquals("value2", jsonTreeReader.nextString());
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("name3", jsonTreeReader.nextName());
 assertEquals("value3", jsonTreeReader.nextString());
 assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextLong() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("number1", new JsonPrimitive(1L));
 addProperty("number2", new JsonPrimitive(2L));
 addProperty("number3", new JsonPrimitive(3L));
 }});
 jsonTreeReader.beginObject();
 assertEquals(1L, jsonTreeReader.nextLong());
 jsonTreeReader.promoteNameToValue();
 assertEquals(1L, jsonTreeReader.nextLong());
 assertEquals(2L, jsonTreeReader.nextLong());
 jsonTreeReader.promoteNameToValue();
 assertEquals(2L, jsonTreeReader.nextLong());
 assertEquals(3L, jsonTreeReader.nextLong());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextInt() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("number1", new JsonPrimitive(1));
 addProperty("number2", new JsonPrimitive(2));
 addProperty("number3", new JsonPrimitive(3));
 }});
 jsonTreeReader.beginObject();
 assertEquals(1, jsonTreeReader.nextInt());
 jsonTreeReader.promoteNameToValue();
 assertEquals(1, jsonTreeReader.nextInt());
 assertEquals(2, jsonTreeReader.nextInt());
 jsonTreeReader.promoteNameToValue();
 assertEquals(2, jsonTreeReader.nextInt());
 assertEquals(3, jsonTreeReader.nextInt());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextDouble() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("number1", new JsonPrimitive(1.0));
 addProperty("number2", new JsonPrimitive(2.0));
 addProperty("number3", new JsonPrimitive(3.0));
 }});
 jsonTreeReader.beginObject();
 assertEquals(1.0, jsonTreeReader.nextDouble(), 0.0);
 jsonTreeReader.promoteNameToValue();
 assertEquals(1.0, jsonTreeReader.nextDouble(), 0.0);
 assertEquals(2.0, jsonTreeReader.nextDouble(), 0.0);
 jsonTreeReader.promoteNameToValue();
 assertEquals(2.0, jsonTreeReader.nextDouble(), 0.0);
 assertEquals(3.0, jsonTreeReader.nextDouble(), 0.0);
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextBoolean() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("boolean1", new JsonPrimitive(true));
 addProperty("boolean2", new JsonPrimitive(false));
 }});
 jsonTreeReader.beginObject();
 assertTrue(jsonTreeReader.nextBoolean());
 jsonTreeReader.promoteNameToValue();
 assertTrue(jsonTreeReader.nextBoolean());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextString() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("string1", new JsonPrimitive("value1"));
 addProperty("string2", new JsonPrimitive("value2"));
 }});
 jsonTreeReader.beginObject();
 assertEquals("value1", jsonTreeReader.nextString());
 jsonTreeReader.promoteNameToValue();
 assertEquals("value1", jsonTreeReader.nextString());
 assertEquals("value2", jsonTreeReader.nextString());
 jsonTreeReader.promoteNameToValue();
 assertEquals("value2", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextNull() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("null1", JsonNull.INSTANCE);
 addProperty("null2", JsonNull.INSTANCE);
 }});
 jsonTreeReader.beginObject();
 jsonTreeReader.nextNull();
 jsonTreeReader.promoteNameToValue();
 jsonTreeReader.nextNull();
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextNullWithJsonArray() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonArray() {{
 add(JsonNull.INSTANCE);
 add(JsonNull.INSTANCE);
 }});
 jsonTreeReader.nextNull();
 jsonTreeReader.promoteNameToValue();
 jsonTreeReader.nextNull();
 jsonTreeReader.endArray();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextNullWithJsonObject() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("null1", JsonNull.INSTANCE);
 addProperty("null2", JsonNull.INSTANCE);
 }});
 jsonTreeReader.nextNull();
 jsonTreeReader.promoteNameToValue();
 jsonTreeReader.nextNull();
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testNextNullWithJsonPrimitive() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonPrimitive(null));
 jsonTreeReader.nextNull();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testClose() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject());
 jsonTreeReader.close();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testSkipValue() throws IOException {
 JsonTreeReader jsonTreeReader = createJsonTreeReader(new JsonObject() {{
 addProperty("name1", new JsonPrimitive("value1"));
 addProperty("name2", new JsonPrimitive("value2"));
 addProperty("name3", new JsonPrimitive("value3"));
 }});
 jsonTreeReader.beginObject();
 jsonTreeReader.skipValue();
 jsonTreeReader.promoteNameToValue();
 assertEquals("null", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertFalse(jsonTreeReader.hasNext());
 }
}