import static org.junit.Assert.*;
import static com.google.gson.stream.JsonReader.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{}";
      }
    });

    reader.close();
    try {
      reader.beginArray();
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "[ ]";
      }
    });

    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testArrayWithOneElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray() {
          @Override public Iterator<JsonElement> iterator() {
            return new Iterator<JsonElement>() {
              JsonElement element = new JsonPrimitive("element");

              @Override public boolean hasNext() {
                return element != null;
              }

              @Override public JsonElement next() {
                JsonElement result = element;
                element = null;
                return result;
              }
            };
          }
        };
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "[\"element\"]";
      }
    });

    reader.beginArray();
    assertEquals("element", reader.nextString());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testArrayWithMultipleElements() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray() {
          @Override public Iterator<JsonElement> iterator() {
            return new Iterator<JsonElement>() {
              JsonElement[] elements = { new JsonPrimitive("element1"), new JsonPrimitive("element2") };
              int index = 0;

              @Override public boolean hasNext() {
                return index < elements.length;
              }

              @Override public JsonElement next() {
                JsonElement result = elements[index++];
                return result;
              }
            };
          }
        };
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "[\"element1\", \"element2\"]";
      }
    });

    reader.beginArray();
    assertEquals("element1", reader.nextString());
    assertEquals("element2", reader.nextString());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testObjectWithEmptyNameArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{}";
      }
    });

    reader.beginObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testObjectWithOneName() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject() {
          @Override public Iterator<Map.Entry<String, JsonElement>> entryIterator() {
            return new Iterator<Map.Entry<String, JsonElement>>() {
              Map.Entry<String, JsonElement> entry = new AbstractMap.SimpleEntry<>("name", new JsonPrimitive("value"));

              @Override public boolean hasNext() {
                return entry != null;
              }

              @Override public Map.Entry<String, JsonElement> next() {
                Map.Entry<String, JsonElement> result = entry;
                entry = null;
                return result;
              }
            };
          }
        };
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{\"name\":\"value\"}";
      }
    });

    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("value", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testObjectWithMultipleNames() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject() {
          @Override public Iterator<Map.Entry<String, JsonElement>> entryIterator() {
            return new Iterator<Map.Entry<String, JsonElement>>() {
              Map.Entry<String, JsonElement>[] entries = {
                new AbstractMap.SimpleEntry<>("name1", new JsonPrimitive("value1")),
                new AbstractMap.SimpleEntry<>("name2", new JsonPrimitive("value2"))
              };
              int index = 0;

              @Override public boolean hasNext() {
                return index < entries.length;
              }

              @Override public Map.Entry<String, JsonElement> next() {
                Map.Entry<String, JsonElement> result = entries[index++];
                return result;
              }
            };
          }
        };
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{\"name1\":\"value1\", \"name2\":\"value2\"}";
      }
    });

    reader.beginObject();
    assertEquals("name1", reader.nextName());
    assertEquals("value1", reader.nextString());
    assertEquals("name2", reader.nextName());
    assertEquals("value2", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testObjectWithNullValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject() {
          @Override public Iterator<Map.Entry<String, JsonElement>> entryIterator() {
            return new Iterator<Map.Entry<String, JsonElement>>() {
              Map.Entry<String, JsonElement> entry = new AbstractMap.SimpleEntry<>("name", JsonNull.INSTANCE);

              @Override public boolean hasNext() {
                return entry != null;
              }

              @Override public Map.Entry<String, JsonElement> next() {
                Map.Entry<String, JsonElement> result = entry;
                entry = null;
                return result;
              }
            };
          }
        };
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return JsonNull.INSTANCE;
      }

      @Override public String getAsString() {
        return "{\"name\":null}";
      }
    });

    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertNull(reader.nextNull());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testArrayAndObjectNesting() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray() {
          @Override public Iterator<JsonElement> iterator() {
            return new Iterator<JsonElement>() {
              JsonArray array = new JsonArray() {
                @Override public Iterator<JsonElement> iterator() {
                  return new Iterator<JsonElement>() {
                    JsonElement[] arrayElements = {
                      new JsonPrimitive("arrayElement1"),
                      new JsonPrimitive("arrayElement2"),
                      new JsonObject() {
                        @Override public Iterator<Map.Entry<String, JsonElement>> entryIterator() {
                          return new Iterator<Map.Entry<String, JsonElement>>() {
                            Map.Entry<String, JsonElement>[] objectEntries = {
                              new AbstractMap.SimpleEntry<>("name1", new JsonPrimitive("objectElement1")),
                              new AbstractMap.SimpleEntry<>("name2", new JsonPrimitive("objectElement2"))
                            };
                            int index = 0;

                            @Override public boolean hasNext() {
                              return index < objectEntries.length;
                            }

                            @Override public Map.Entry<String, JsonElement> next() {
                              Map.Entry<String, JsonElement> result = objectEntries[index++];
                              return result;
                            }
                          };
                        }
                      }
                    };
                    int index = 0;

                    @Override public boolean hasNext() {
                      return index < arrayElements.length;
                    }

                    @Override public JsonElement next() {
                      JsonElement result = arrayElements[index++];
                      return result;
                    }
                  };
                }
              };
              int index = 0;

              @Override public boolean hasNext() {
                return index < 1;
              }

              @Override public JsonElement next() {
                if (index == 0) {
                  JsonElement result = array;
                  index++;
                  return result;
                } else {
                  return arrayElements[0];
                }
              }
            };
          }
        };
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "[[" +
               "  {\"name1\":\"objectElement1\", \"name2\":\"objectElement2\"}," +
               "  \"arrayElement1\"," +
               "  \"arrayElement2\"" +
               "]]";
      }
    });

    reader.beginArray();
    reader.beginArray();
    assertEquals("objectElement1", reader.nextName());
    assertEquals("objectElement1", reader.nextString());
    assertEquals("name1", reader.nextName());
    assertEquals("objectElement1", reader.nextString());
    assertEquals("name2", reader.nextName());
    assertEquals("objectElement2", reader.nextString());
    assertEquals("arrayElement1", reader.nextString());
    assertEquals("arrayElement1", reader.nextString());
    assertEquals("arrayElement2", reader.nextString());
    assertEquals("arrayElement2", reader.nextString());
    reader.endArray();
    reader.endArray();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{}";
      }
    });

    reader.close();
    reader.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReaderWithConsumedData() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{}";
      }
    });

    reader.close();
    reader.nextString();
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidToken() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonNull getAsJsonNull() {
        return null;
      }

      @Override public String getAsString() {
        return "{\"invalidToken\":\"value\"}";
      }
    });

    reader.beginObject();
    reader.nextName(); // Expecting "invalidToken"
    reader.nextString(); // Expecting "value"
    reader.endObject();
  }
}