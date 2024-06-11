
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
  public void testBeginAndEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("value1");
    jsonTreeWriter.value("value2");
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(new JsonPrimitive("value2"));
    
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testBeginAndEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1").value("value1");
    jsonTreeWriter.name("name2").value("value2");
    jsonTreeWriter.endObject();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("name1", new JsonPrimitive("value1"));
    jsonObject.add("name2", new JsonPrimitive("value2"));
    
    assertEquals(jsonObject, jsonElement);
  }

  @Test
  public void testBeginObjectBeforeValue() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("value");
    jsonTreeWriter.endObject();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("name", new JsonPrimitive("value"));
    
    assertEquals(jsonObject, jsonElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() throws IOException {
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameCalledTwice() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1");
    jsonTreeWriter.name("name2");
  }

  @Test
  public void testValue_String() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("value1");
    jsonTreeWriter.value((String) null);
    jsonTreeWriter.value("value2");
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(JsonNull.INSTANCE);
    jsonArray.add(new JsonPrimitive("value2"));
    
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testNullValue() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value((String) null);
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(JsonNull.INSTANCE);
    
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testValue_Boolean() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(true);
    jsonTreeWriter.value(false);
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(true));
    jsonArray.add(new JsonPrimitive(false));
    
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testValue_Double() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1.23);
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
    jsonTreeWriter.value(Double.NaN);
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1.23));
    jsonArray.add(new JsonPrimitive(Double.POSITIVE_INFINITY));
    jsonArray.add(new JsonPrimitive(Double.NEGATIVE_INFINITY));
    jsonArray.add(new JsonPrimitive(Double.NaN));
    
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testValue_Long() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(123L);
    jsonTreeWriter.value(0L);
    jsonTreeWriter.value(-123L);
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(123L));
    jsonArray.add(new JsonPrimitive(0L));
    jsonArray.add(new JsonPrimitive(-123L));
    
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testValue_Number() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(123);
    jsonTreeWriter.value(0);
    jsonTreeWriter.value(-123);
    jsonTreeWriter.value(1.23);
    jsonTreeWriter.endArray();
    
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(123));
    jsonArray.add(new JsonPrimitive(0));
    jsonArray.add(new JsonPrimitive(-123));
    jsonArray.add(new JsonPrimitive(1.23));
    
    assertEquals(jsonArray, jsonElement);
  }
}
