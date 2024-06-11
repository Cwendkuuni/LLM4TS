package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertNotNull(array);
    assertEquals(0, array.size());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    assertNotNull(object);
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    assertEquals("name", object.name());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive value = object.get("name");
    assertEquals("value", value.getAsString());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonNull nullValue = object.get("name");
    assertSame(JsonNull.INSTANCE, nullValue);
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(true);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive booleanValue = object.get("name");
    assertTrue(booleanValue.isBoolean());
    assertTrue(booleanValue.getAsBoolean());
  }

  @Test
  public void testValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(42);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive integerValue = object.get("name");
    assertEquals(42, integerValue.getAsInt());
  }

  @Test
  public void testValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(42L);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive longValue = object.get("name");
    assertEquals(42L, longValue.getAsLong());
  }

  @Test
  public void testValueNumberDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(3.14);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive doubleValue = object.get("name");
    assertEquals(3.14, doubleValue.getAsDouble(), 0.0);
  }

  @Test
  public void testBeginArrayAddValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value");
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonPrimitive value = array.get(0);
    assertEquals("value", value.getAsString());
  }

  @Test
  public void testBeginArrayAddValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.nullValue();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonNull nullValue = array.get(0);
    assertSame(JsonNull.INSTANCE, nullValue);
  }

  @Test
  public void testBeginArrayAddValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(true);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonPrimitive booleanValue = array.get(0);
    assertTrue(booleanValue.isBoolean());
    assertTrue(booleanValue.getAsBoolean());
  }

  @Test
  public void testBeginArrayAddValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(42);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonPrimitive integerValue = array.get(0);
    assertEquals(42, integerValue.getAsInt());
  }

  @Test
  public void testBeginArrayAddValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(42L);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonPrimitive longValue = array.get(0);
    assertEquals(42L, longValue.getAsLong());
  }

  @Test
  public void testBeginArrayAddValueNumberDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(3.14);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonPrimitive doubleValue = array.get(0);
    assertEquals(3.14, doubleValue.getAsDouble(), 0.0);
  }

  @Test
  public void testBeginArrayAddValueArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonTreeWriter subWriter = new JsonTreeWriter();
    writer.value(subWriter.beginArray().value("value").endArray().get());
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonArray subArray = array.get(0).getAsJsonArray();
    assertEquals(1, subArray.size());
    JsonPrimitive value = subArray.get(0);
    assertEquals("value", value.getAsString());
  }

  @Test
  public void testBeginObjectAddNameValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive name = object.get("name");
    assertEquals("value", name.getAsString());
  }

  @Test
  public void testBeginObjectAddNameValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonNull name = object.get("name");
    assertSame(JsonNull.INSTANCE, name);
  }

  @Test
  public void testBeginObjectAddNameValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(true);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive name = object.get("name");
    assertTrue(name.isBoolean());
    assertTrue(name.getAsBoolean());
  }

  @Test
  public void testBeginObjectAddNameValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(42);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive name = object.get("name");
    assertEquals(42, name.getAsInt());
  }

  @Test
  public void testBeginObjectAddNameValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(42L);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive name = object.get("name");
    assertEquals(42L, name.getAsLong());
  }

  @Test
  public void testBeginObjectAddNameValueNumberDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(3.14);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonPrimitive name = object.get("name");
    assertEquals(3.14, name.getAsDouble(), 0.0);
  }

  @Test
  public void testBeginObjectAddNameValueObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonTreeWriter subWriter = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(subWriter.beginObject().name("subName").value("subValue").endObject().get());
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = (JsonObject) result;
    JsonObject name = object.get("name");
    JsonObject subObject = name.getAsJsonObject();
    assertEquals("subValue", subObject.get("subName").getAsString());
  }
}