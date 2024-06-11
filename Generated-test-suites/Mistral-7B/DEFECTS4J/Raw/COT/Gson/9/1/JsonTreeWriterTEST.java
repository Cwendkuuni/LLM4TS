import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

 private JsonTreeWriter createWriter() {
 return new JsonTreeWriter();
 }

 @Test
 public void testGet() {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name");
 writer.value("value");
 JsonElement result = writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive("value"));
 assertEquals(expected, result);
 }

 @Test
 public void testPeek() {
 JsonTreeWriter writer = createWriter();
 writer.beginArray();
 writer.beginObject();
 writer.name("name");
 writer.value("value");
 writer.endObject();
 writer.value("value2");
 JsonElement expectedArray = new JsonArray();
 expectedArray.add(new JsonObject());
 expectedArray.add(new JsonPrimitive("value2"));
 JsonElement actual = writer.peek();
 assertEquals(expectedArray, actual.getAsJsonArray());
 }

 @Test
 public void testPut() {
 JsonTreeWriter writer = createWriter();
 JsonElement expectedArray = new JsonArray();
 JsonElement expectedObjects = new JsonObject();
 JsonPrimitive expectedClosed = new JsonPrimitive("closed");
 writer.beginArray();
 writer.beginObject();
 writer.name("name1").value("value1");
 writer.name("name2").value("value2");
 writer.endObject();
 writer.name("name3").value("value3");
 writer.endArray();
 writer.close();
 List<JsonElement> actualStack = writer.stack;
 assertEquals(expectedArray, actualStack.get(0));
 assertEquals(expectedObjects, actualStack.get(1));
 assertEquals(expectedClosed, actualStack.get(2));
 }

 @Test
 public void testBeginArray() throws IOException {
 JsonTreeWriter writer = createWriter();
 JsonArray expected = new JsonArray();
 writer.beginArray();
 writer.endArray();
 List<JsonElement> actualStack = writer.stack;
 assertEquals(expected, actualStack.get(0));
 assertTrue(actualStack.size() == 1);
 }

 @Test
 public void testEndArray() throws IOException {
 JsonTreeWriter writer = createWriter();
 JsonArray expected = new JsonArray();
 writer.beginArray();
 writer.endArray();
 JsonElement actual = writer.peek();
 JsonArray actualArray = actual.getAsJsonArray();
 assertEquals(expected, actualArray);
 assertTrue(actualStack.size() == 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayBeforeBeginArray() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectBeforeBeginObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.endObject();
 }

 @Test
 public void testBeginObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 JsonObject expected = new JsonObject();
 writer.beginObject();
 writer.name("name1").value("value1");
 writer.name("name2").value("value2");
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 assertEquals(expected, actual);
 }

 @Test
 public void testEndObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 JsonObject expected = new JsonObject();
 writer.beginObject();
 writer.name("name1").value("value1");
 writer.name("name2").value("value2");
 writer.endObject();
 JsonElement actual = writer.peek();
 JsonObject actualObject = actual.getAsJsonObject();
 assertEquals(expected, actualObject);
 assertTrue(writer.stack.size() == 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectBeforeBeginObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.endObject();
 }

 @Test
 public void testName() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value("value");
 writer.name("name2").value("value2");
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive("value"));
 expected.add("name2", new JsonPrimitive("value2"));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testNameAfterEndObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.endObject();
 writer.name("name");
 }

 @Test
 public void testValueString() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value("value");
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive("value"));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testValueStringAfterEndObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.endObject();
 writer.value("value");
 }

 @Test
 public void testNullValue() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(null);
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", JsonNull.INSTANCE);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testNullValueAfterEndObject() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.endObject();
 writer.value(null);
 }

 @Test
 public void testValueBoolean() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(true);
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive(true));
 assertEquals(expected, actual);
 }

 @Test
 public void testValueDouble() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(123.45);
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive(123.45));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueDoubleNaN() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(Double.NaN);
 writer.endObject();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueDoubleInfinity() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(Double.POSITIVE_INFINITY);
 writer.endObject();
 }

 @Test
 public void testValueLong() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(123);
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive(123L));
 assertEquals(expected, actual);
 }

 @Test
 public void testValueNumber() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.beginObject();
 writer.name("name").value(new Long(123));
 writer.endObject();
 JsonObject actual = (JsonObject) writer.get();
 JsonObject expected = new JsonObject();
 expected.add("name", new JsonPrimitive(123L));
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testFlush() throws IOException {
 JsonTreeWriter writer = createWriter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writerStream = new OutputStreamWriter(outputStream);
 writer.writeTo(writerStream);
 writer.flush();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCloseTwice() throws IOException {
 JsonTreeWriter writer = createWriter();
 writer.close();
 writer.close();
 }
}