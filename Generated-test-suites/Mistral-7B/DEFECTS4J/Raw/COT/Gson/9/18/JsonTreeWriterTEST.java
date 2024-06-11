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

 private JsonTreeWriter createJsonTreeWriter() {
 return new JsonTreeWriter();
 }

 @Test
 public void testGet() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertNull(jsonElement);

 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value", jsonObject.get("name").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testGet_when_no_element_present() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.get();
 }

 @Test
 public void testPeek() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonElement = jsonTreeWriter.peek();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(1, jsonArray.size());
 assertEquals("element1", jsonArray.get(0).getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeek_when_stack_is_empty() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.peek();
 }

 @Test
 public void testPut() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.put(new JsonPrimitive("value"));
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals("value", jsonElement.getAsString());
 }

 @Test
 public void testPut_with_pending_name() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.put(new JsonPrimitive("value"));
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value", jsonObject.get("name").getAsString());
 }

 @Test
 public void testBeginArray() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.value("element2");
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(2, jsonArray.size());
 assertEquals("element1", jsonArray.get(0).getAsString());
 assertEquals("element2", jsonArray.get(1).getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginArray_when_stack_is_not_empty() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.beginArray();
 }

 @Test
 public void testEndArray() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(1, jsonArray.size());
 assertEquals("element1", jsonArray.get(0).getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArray_when_stack_is_empty() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.endArray();
 }

 @Test
 public void testBeginObject() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value", jsonObject.get("name").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObject_when_stack_is_not_empty_and_top_is_not_object() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.beginObject();
 }

 @Test
 public void testEndObject() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value", jsonObject.get("name").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObject_when_stack_is_empty() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.endObject();
 }

 @Test
 public void testName() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value", jsonObject.get("name").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testName_when_stack_is_not_object() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.name("name");
 }

 @Test
 public void testValueString() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value("value");
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals("value", jsonElement.getAsString());
 }

 @Test
 public void testValueBoolean() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value(true);
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(true, jsonElement.getAsBoolean());
 }

 @Test
 public void testValueDouble() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value(1.0);
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(1.0, jsonElement.getAsDouble(), 0);
 }

 @Test
 public void testValueLong() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value(1);
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(1L, jsonElement.getAsLong());
 }

 @Test
 public void testValueNumber() {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.value(new Double(1.0));
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(1.0, jsonElement.getAsDouble(), 0);
 }

 @Test
 public void testFlush() throws IOException {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 jsonTreeWriter.setWriter(writer);
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endObject();
 String jsonString = outputStream.toString();
 assertEquals("{\"name\":\"value\"}", jsonString);
 jsonTreeWriter.flush();
 }

 @Test
 public void testClose() throws IOException {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 jsonTreeWriter.setWriter(writer);
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endObject();
 jsonTreeWriter.close();
 String jsonString = outputStream.toString();
 assertEquals("{\"name\":\"value\"}", jsonString);
 }

 @Test(expected = IOException.class)
 public void testClose_when_incomplete_document() throws IOException {
 JsonTreeWriter jsonTreeWriter = createJsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.close();
 jsonTreeWriter.value("element");
 }
}