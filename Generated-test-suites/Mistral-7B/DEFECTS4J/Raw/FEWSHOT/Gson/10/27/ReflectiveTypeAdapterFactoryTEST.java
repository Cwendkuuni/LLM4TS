package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ReflectiveTypeAdapterFactoryTest {

  private static class DummyClass {
    private String field1;
    private String field2;
    private int field3;

    public String getField1() {
      return field1;
    }

    public void setField1(String field1) {
      this.field1 = field1;
    }

    public String getField2() {
      return field2;
    }

    public void setField2(String field2) {
      this.field2 = field2;
    }

    public int getField3() {
      return field3;
    }

    public void setField3(int field3) {
      this.field3 = field3;
    }

    @JsonAdapter(Adapter.class)
    public static class InnerClass {
      private int innerField;

      public int getInnerField() {
        return innerField;
      }

      public void setInnerField(int innerField) {
        this.innerField = innerField;
      }
    }

    private static class Adapter extends TypeAdapter<DummyClass> {
      @Override public DummyClass read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }

        DummyClass instance = new DummyClass();

        try {
          in.beginObject();
          while (in.hasNext()) {
            String name = in.nextName();
            Field field = DummyClass.class.getDeclaredField(name);
            if (field == null) {
              in.skipValue();
            } else {
              field.setAccessible(true);
              field.set(instance, in.nextString());
            }
          }
        } catch (IllegalStateException e) {
          throw new JsonSyntaxException(e);
        } catch (IllegalAccessException e) {
          throw new AssertionError(e);
        }
        in.endObject();
        return instance;
      }

      @Override public void write(JsonWriter out, DummyClass value) throws IOException {
        if (value == null) {
          out.nullValue();
          return;
        }

        out.beginObject();
        try {
          for (Field field : DummyClass.class.getDeclaredFields()) {
            if (!field.isSynthetic()) {
              String name = fieldNamingPolicy.translateName(field);
              Object value2 = field.get(value);
              if (value2 != null) {
                out.name(name);
                out.value(value2.toString());
              }
            }
          }
        } catch (IllegalAccessException e) {
          throw new AssertionError(e);
        }
        out.endObject();
      }
    }
  }

  private static FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
    @Override public String translateName(Field field) {
      return field.getName().toLowerCase();
    }
  };

  private static Excluder excluder = new Excluder() {
    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }

    @Override public boolean excludeField(Field field, boolean serialize) {
      return field.isSynthetic();
    }
  };

  private static Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(fieldNamingPolicy)
      .setExcluder(excluder)
      .create();

  @Test
  public void testReflectiveTypeAdapterFactory() {
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), fieldNamingPolicy, excluder);

    TypeToken<DummyClass> type = new TypeToken<DummyClass>() {};

    TypeAdapter<DummyClass> adapter = factory.create(gson, type);

    assertTrue(adapter instanceof ReflectiveTypeAdapterFactory.Adapter);

    DummyClass instance = new DummyClass();
    JsonObject json = new JsonParser().parse("{" +
        "\"field1\":\"foo\"," +
        "\"field2\":\"bar\"" +
        "}").getAsJsonObject();

    adapter.read(new JsonReader(new StringReader(json.toString()))).assertEquals(instance);

    JsonWriter writer = new JsonWriter(new StringWriter());
    adapter.write(writer, instance);
    JsonElement json2 = JsonParser.parseReader(writer);
    JsonObject json3 = json2.getAsJsonObject();

    List<Map.Entry<String, Object>> entries = json3.entrySet();
    List<Map.Entry<String, Object>> expectedEntries = new ArrayList<>();
    expectedEntries.add(Map.entry("field1", "foo"));
    expectedEntries.add(Map.entry("field2", "bar"));
    assertArrayEquals(expectedEntries.toArray(), entries.toArray());

    TypeToken<DummyClass.InnerClass> innerType = new TypeToken<DummyClass.InnerClass>() {};
    TypeAdapter<DummyClass.InnerClass> innerAdapter = factory.create(gson, innerType);

    DummyClass.InnerClass innerInstance = new DummyClass.InnerClass();
    JsonObject innerJson = new JsonParser().parse("{" +
        "\"innerField\":123" +
        "}").getAsJsonObject();

    innerAdapter.read(new JsonReader(new StringReader(innerJson.toString()))).assertEquals(innerInstance);

    JsonWriter innerWriter = new JsonWriter(new StringWriter());
    innerAdapter.write(innerWriter, innerInstance);
    JsonElement innerJson2 = JsonParser.parseReader(innerWriter);
    JsonObject innerJson3 = innerJson2.getAsJsonObject();

    List<Map.Entry<String, Object>> innerEntries = innerJson3.entrySet();
    List<Map.Entry<String, Object>> expectedInnerEntries = new ArrayList<>();
    expectedInnerEntries.add(Map.entry("innerField", 123));
    assertArrayEquals(expectedInnerEntries.toArray(), innerEntries.toArray());
  }

  private static class StringReader implements java.io.Reader {
    private final String string;

    public StringReader(String string) {
      this.string = string;
    }

    @Override public int read(char[] cbuf, int off, int length) throws IOException {
      int len = string.length();
      if (off + length > len) {
        throw new IOException("End of stream");
      }
      int numCharsToRead = Math.min(len - off, length);
      string.getChars(off, off + numCharsToRead, cbuf, 0);
      return numCharsToRead;
    }
  }

  private static class StringWriter extends java.io.StringWriter {
    @Override public void write(String s, int offset, int length) throws IOException {
      super.write(s, offset, length);
    }
  }
}