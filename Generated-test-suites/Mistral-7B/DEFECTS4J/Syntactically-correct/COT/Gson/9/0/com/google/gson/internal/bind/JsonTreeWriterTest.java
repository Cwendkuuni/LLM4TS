import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

 private JsonTreeWriter createJsonTreeWriter() {
 return new JsonTreeWriter();
 }

 @Test
 public void testGetReturnsEmptyJsonElementWhenNoElementAdded() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testGetReturnsLastAddedJsonElement() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value("test");
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(new JsonPrimitive("test"), jsonElement);
 }

 @Test
 public void testBeginArrayAddsJsonArrayToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertTrue(jsonElement instanceof JsonArray);
 }

 @Test
 public void testEndArrayRemovesJsonArrayFromStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertNull(jsonElement);
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayWhenNotAnArrayIsOnTopOfStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.endArray();
 }

 @Test
 public void testBeginObjectAddsJsonObjectToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertTrue(jsonElement instanceof JsonObject);
 }

 @Test
 public void testEndObjectRemovesJsonObjectFromStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertNull(jsonElement);
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectWhenNotAnObjectIsOnTopOfStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endObject();
 }

 @Test
 public void testNameSetsPendingNameForNextJsonObject() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.name("test");
 jsonTreeWriter.beginObject();
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.peek();
 String name = jsonObject.name();
 assertEquals("test", name);
 }

 @Test
 public void testValueAddsJsonPrimitiveToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value("test");
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertTrue(jsonElement instanceof JsonPrimitive);
 }

 @Test
 public void testNullValueAddsJsonNullToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.nullValue();
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testValueAddsBooleanToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value(true);
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertTrue(jsonElement instanceof JsonPrimitive);
 assertTrue(((JsonPrimitive) jsonElement).isBoolean());
 assertTrue(((JsonPrimitive) jsonElement).getAsBoolean());
 }

 @Test
 public void testValueAddsNumberToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value(1);
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertTrue(jsonElement instanceof JsonPrimitive);
 assertTrue(jsonElement.isNumberType());
 assertEquals(1.0, jsonElement.getAsDouble(), 0.0);
 }

 @Test
 public void testValueAddsStringToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value("test");
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertTrue(jsonElement instanceof JsonPrimitive);
 assertEquals("test", jsonElement.getAsString());
 }

 @Test
 public void testFlushDoesNothing() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.flush();
 }

 @Test
 public void testCloseThrowsIOExceptionWhenNotClosed() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 try {
 jsonTreeWriter.close();
 } catch (IOException e) {
 assertFalse(e.getMessage().contains("Incomplete document"));
 }
 }

 @Test(expected = IOException.class)
 public void testCloseThrowsIOExceptionWhenClosedAlready() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.close();
 try {
 jsonTreeWriter.close();
 } finally {
 jsonTreeWriter.close(); // this one should throw
 }
 }

 @Test
 public void testCloseAddsSentinalToStack() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.close();
 JsonElement jsonElement = jsonTreeWriter.peek();
 assertSame(SENTINEL_CLOSED, jsonElement);
 }
}