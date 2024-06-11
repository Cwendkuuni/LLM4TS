package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonTypeAdapter;
import com.google.gson.JsonWriter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
  private static final class DummyClass {
    @SerializedName("name1")
    String field1;

    @JsonAdapter(DummyJsonAdapter.class)
    String field2;
    int field3;

    public DummyClass(String name1, String name2, int num) {
      this.field1 = name1;
      this.field2 = name2;
      this.field3 = num;
    }
  }

  private static class DummyJsonAdapter extends JsonTypeAdapter<DummyClass> {
    @Override public DummyClass read(JsonReader in) throws IOException {
      DummyClass result = new DummyClass("defaultName1", "defaultName2", 0);
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      in.beginObject();
      String name1 = null;
      String name2 = null;
      int num = 0;
      while (in.hasNext()) {
        String name = in.nextName();
        switch (name) {
          case "name1":
            name1 = in.nextString();
            break;
          case "name2":
            name2 = in.nextString();
            break;
          case "num":
            num = in.nextInt();
            break;
          default:
            in.skipValue();
        }
      }
      in.endObject();
      result.field1 = name1;
      result.field2 = name2;
      result.field3 = num;
      return result;
    }

    @Override public void write(JsonWriter out, DummyClass value) throws IOException {
      out.beginObject();
      out.name("name1").value(value.field1);
      out.name("name2").value(value.field2);
      out.name("num").value(value.field3);
      out.endObject();
    }
  }

  @Test
  public void testCreateTypeAdapter() {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeToken<DummyClass> type = new TypeToken<DummyClass>() {};
    TypeAdapter<DummyClass> adapter = factory.create(gson, type);
    assertEquals(DummyJsonAdapter.class, adapter.getClass());
  }

  @Test
  public void testGetBoundFields_noFields() {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeToken<Object> type = new TypeToken<Object>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, Object.class);
    assertNull(boundFields);
  }

  @Test
  public void testGetBoundFields_interface() {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeToken<MyInterface> type = new TypeToken<MyInterface>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, MyInterface.class);
    assertNull(boundFields);
  }

  @Test
  public void testGetBoundFields_simpleClass() throws Exception {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    Class<?> raw = DummyClass.class;
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeAdapter<DummyClass> adapter = factory.create(gson, new TypeToken<DummyClass>() {});

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<DummyClass>() {}, raw);

    List<ReflectiveTypeAdapterFactory.BoundField> fields = new ArrayList<>();
    fields.add(
        spy(new ReflectiveTypeAdapterFactory.BoundField("field1", true, true) {
          @Override public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            super.write(writer, value);
            writer.name("name1").value(String.valueOf(((DummyClass) value).field1));
          }
        }));
    fields.add(
        new ReflectiveTypeAdapterFactory.BoundField("field2", true, true) {
          @Override public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            super.write(writer, value);
            writer.name("name2").value(String.valueOf(((DummyClass) value).field2));
          }

          @Override public boolean writeField(Object value) throws IOException, IllegalAccessException {
            boolean result = super.writeField(value);
            if (result) {
              assertEquals("defaultName2", ((DummyClass) value).field2);
            }
            return result;
          }
        });
    fields.add(
        new ReflectiveTypeAdapterFactory.BoundField("field3", true, true) {
          @Override public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            super.write(writer, value);
            writer.name("num").value(((DummyClass) value).field3);
          }
        });

    Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = new LinkedHashMap<>();
    expected.putAll(fields);

    assertEquals(expected, boundFields);
  }

  @Test
  public void testGetBoundFields_getDeclaredFields() throws Exception {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    Class<?> raw = DummyClass.class;

    Type field3Type = $Gson$Types.getSuperclassType(TypeToken.get(int.class), raw);
    Field field3 = raw.getDeclaredField("field3");
    field3.setAccessible(true);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeAdapter<DummyClass> adapter = factory.create(gson, new TypeToken<DummyClass>() {});

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<DummyClass>() {}, raw);

    List<ReflectiveTypeAdapterFactory.BoundField> fields = new ArrayList<>();
    fields.addAll(getBoundFieldsForField("field1", true, true, fields, gson, factory, raw));
    fields.addAll(getBoundFieldsForField("field2", true, true, fields, gson, factory, raw));
    fields.addAll(getBoundFieldsForField("field3", true, true, fields, gson, factory, raw));

    Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = new LinkedHashMap<>();
    expected.putAll(fields);

    assertEquals(expected, boundFields);
  }

  @Test
  public void testGetBoundFields_excludedField() throws Exception {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    Class<?> raw = DummyClass.class;

    Field field3 = raw.getDeclaredField("field3");
    field3.setAccessible(true);

    when(excluder.excludeClass(field3.getType(), true)).thenReturn(false);
    when(excluder.excludeField(field3, true)).thenReturn(false);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeAdapter<DummyClass> adapter = factory.create(gson, new TypeToken<DummyClass>() {});

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<DummyClass>() {}, raw);

    List<ReflectiveTypeAdapterFactory.BoundField> fields = new ArrayList<>();
    fields.addAll(getBoundFieldsForField("field1", true, true, fields, gson, factory, raw));
    fields.addAll(getBoundFieldsForField("field2", true, true, fields, gson, factory, raw));

    assertNull(getBoundFieldsForField("field3", true, true, fields, gson, factory, raw));

    Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = new LinkedHashMap<>();
    expected.putAll(fields);

    assertEquals(expected, boundFields);
  }

  @Test
  public void testGetBoundFields_excludedClass() throws Exception {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    Class<?> raw = DummyClass.class;

    when(excluder.excludeClass(raw.getSuperclass(), true)).thenReturn(true);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeAdapter<DummyClass> adapter = factory.create(gson, new TypeToken<DummyClass>() {});

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<DummyClass>() {}, raw);

    List<ReflectiveTypeAdapterFactory.BoundField> fields = new ArrayList<>();
    fields.addAll(getBoundFieldsForField("field1", true, true, fields, gson, factory, raw));
    fields.addAll(getBoundFieldsForField("field2", true, true, fields, gson, factory, raw));

    assertNull(boundFields);
  }

  @Test
  public void testGetBoundFields_getSuperclass() throws Exception {
    FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field f) {
        return f.getName().replace("_", "");
      }
    };

    Excluder excluder = mock(Excluder.class);
    Gson gson = mock(Gson.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    Class<?> raw = DummyClass.class;

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);

    TypeAdapter<DummyClass> adapter = factory.create(gson, new TypeToken<DummyClass>() {});

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<DummyClass>() {}, raw);

    List<ReflectiveTypeAdapterFactory.BoundField> fields = new ArrayList<>();
    fields.addAll(getBoundFieldsForField("field1", true, true, fields, gson, factory, raw));
    fields.addAll(getBoundFieldsForField("field2", true, true, fields, gson, factory, raw));

    Class<?> superclass = DummyClass.class.getSuperclass();
    Map<String, ReflectiveTypeAdapterFactory.BoundField> superclassFields = factory.getBoundFields(gson, new TypeToken<DummyClass>() {}, superclass);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = new LinkedHashMap<>();
    expected.putAll(fields);
    expected.putAll(superclassFields);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = new LinkedHashMap<>();
    result.putAll(boundFields);
    result.putAll(superclassFields);

    assertEquals(expected, result);
  }

  private List<ReflectiveTypeAdapterFactory.BoundField> getBoundFieldsForField(
      String name, boolean serialize, boolean deserialize,
      List<ReflectiveTypeAdapterFactory.BoundField> result,
      Gson gson, ReflectiveTypeAdapterFactory factory, Class<?> raw) throws Exception {
    Field field = raw.getDeclaredField(name);
    field.setAccessible(true);
    Type fieldType = $Gson$Types.getSuperclassType(TypeToken.get(field.getType()), raw);

    ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(
        gson, field, name, new TypeToken<Field>() {}, serialize, deserialize);

    result.add(boundField);

    return result;
  }
}