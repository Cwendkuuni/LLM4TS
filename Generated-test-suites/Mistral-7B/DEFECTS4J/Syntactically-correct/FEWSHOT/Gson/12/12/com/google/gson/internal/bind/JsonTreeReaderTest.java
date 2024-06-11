package com.google.gson.internal.bind;

import static com.google.gson.stream.JsonReader.JsonToken.*;
import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonArray.class.getDeclaredConstructor().newInstance(new Object[]{}));
    assertTrue(reader.hasNext());
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name1 = reader.nextName();
    assertEquals("0", name1);
    JsonElement element1 = reader.peek();
    assertEquals(BEGIN_ARRAY, element1.getAsJson().getType());

    assertTrue(reader.hasNext());
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(NUMBER, reader.peek());
    double number1 = reader.nextDouble();
    assertEquals(0.0, number1, 0.001);

    assertTrue(reader.hasNext());
    assertEquals(END_ARRAY, reader.peek());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(END_ARRAY, reader.peek());
    reader.endArray();

    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    assertTrue(reader.hasNext());
    assertEquals(BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name1 = reader.nextName();
    assertEquals("name1", name1);
    JsonElement element1 = reader.peek();
    assertEquals(BEGIN_OBJECT, element1.getAsJson().getType());

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name2 = reader.nextName();
    assertEquals("name2", name2);
    JsonElement element2 = reader.peek();
    assertEquals(STRING, element2.getAsJson().getType());

    String value2 = reader.nextString();
    assertEquals("value2", value2);

    assertTrue(reader.hasNext());
    assertEquals(END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(END_OBJECT, reader.peek());
    reader.endObject();

    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testNestedJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonArray.class.getDeclaredConstructor().newInstance(new Object[]{JsonArray.class.getDeclaredConstructor().newInstance(new Object[]{JsonPrimitive.valueOf("1"), JsonPrimitive.valueOf("2")})}));
    assertTrue(reader.hasNext());
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(NUMBER, reader.peek());
    double number1 = reader.nextDouble();
    assertEquals(1.0, number1, 0.001);

    assertTrue(reader.hasNext());
    assertEquals(NUMBER, reader.peek());
    double number2 = reader.nextDouble();
    assertEquals(2.0, number2, 0.001);

    assertTrue(reader.hasNext());
    assertEquals(END_ARRAY, reader.peek());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(END_ARRAY, reader.peek());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(END_ARRAY, reader.peek());
    reader.endArray();

    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testNestedJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());

    reader.beginObject();
    reader.name("obj1").beginObject();
    reader.name("obj2").beginObject();
    reader.name("name1").value("value1");
    reader.name("name2").value("value2");
    reader.endObject();
    reader.endObject();
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name1 = reader.nextName();
    assertEquals("obj1", name1);
    JsonElement element1 = reader.peek();
    assertEquals(BEGIN_OBJECT, element1.getAsJson().getType());

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name2 = reader.nextName();
    assertEquals("obj2", name2);
    JsonElement element2 = reader.peek();
    assertEquals(BEGIN_OBJECT, element2.getAsJson().getType());

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name3 = reader.nextName();
    assertEquals("name1", name3);
    assertEquals("value1", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals(NAME, reader.peek());
    String name4 = reader.nextName();
    assertEquals("name2", name4);
    assertEquals("value2", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals(END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(END_OBJECT, reader.peek());
    reader.endObject();

    assertFalse(reader.hasNext());
    reader.close();
  }
}