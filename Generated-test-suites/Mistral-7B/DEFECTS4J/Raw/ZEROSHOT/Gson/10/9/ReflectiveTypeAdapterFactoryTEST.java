import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.excludeField;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest extends TestCase {

  private ReflectiveTypeAdapterFactory factory;

  @Override
  protected void setUp() throws Exception {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
    Excluder excluder = new Excluder();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  public void testCreateTypeAdapter() {
    Gson gson = new GsonBuilder().create();
    TypeToken<DummyClass> type = new TypeToken<DummyClass>() {};
    TypeAdapter<DummyClass> adapter = factory.create(gson, type);
    assertNotNull(adapter);
  }

  public void testExcludeField() {
    Class<DummyClass> dummyClass = DummyClass.class;
    Field field = dummyClass.getDeclaredField("excludedField");
    assertFalse(excludeField(field, true));
    assertFalse(excludeField(field, false));

    field.setAccessible(true);
    field.set(new DummyClass(), "excludedValue");

    TypeToken<DummyClass> type = new TypeToken<DummyClass>() {};
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder() {
      @Override
      public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }

      @Override
      public boolean excludeField(Field field, boolean serialize) {
        return field.getName().equals("excludedField");
      }
    });
    TypeAdapter<DummyClass> adapter = factory.create(new Gson(), type);

    DummyClass dummy = new DummyClass();
    JsonObject json = new JsonObject();
    json.addProperty("includedField", "includedValue");
    adapter.write(new JsonWriter(System.out), dummy);

    assertFalse(adapter.writeField(dummy));
  }

  public void testGetFieldNames() {
    Class<DummyClass> dummyClass = DummyClass.class;
    Field field = dummyClass.getDeclaredField("serializedNameField");

    List<String> fieldNames = factory.getFieldNames(field);
    assertEquals(1, fieldNames.size());
    assertEquals("jsonName", fieldNames.get(0));
  }

  public void testCreateBoundField() {
    Class<DummyClass> dummyClass = DummyClass.class;
    Field field = dummyClass.getDeclaredField("primitiveField");

    TypeAdapter<String> stringAdapter = new StringTypeAdapter();
    JsonAdapter<String> jsonAdapter = new JsonAdapter<String>() {};

    TypeToken<DummyClass> type = new TypeToken<DummyClass>() {};
    TypeAdapter<DummyClass> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
        new ObjectConstructor<DummyClass>() {},
        new HashMap<String, ReflectiveTypeAdapterFactory.BoundField>() {{
          put("primitiveField", new ReflectiveTypeAdapterFactory.BoundField("primitiveField", true, false) {
            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
              String valueStr = (String) value;
              stringAdapter.write(writer, valueStr);
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
              JsonElement jsonElement = reader.nextElement();
              String valueStr = jsonElement.getAsString();
              field.set(value, valueStr);
            }

            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
              if (!serialized) return false;
              String valueStr = (String) field.get(value);
              return valueStr != null && !valueStr.equals(field.get(value).toString());
            }
          });
        }}
    );

    Object dummy = new DummyClass();
    JsonObject json = new JsonObject();
    json.addProperty("primitiveField", "value");
    adapter.write(new JsonWriter(System.out), dummy);

    assertFalse(adapter.writeField(dummy));
  }

  public void testCreateBoundFields() {
    Class<DummyClass> dummyClass = DummyClass.class;
    Field field1 = dummyClass.getDeclaredField("serializedNameField");
    Field field2 = dummyClass.getDeclaredField("primitiveField");
    Field field3 = dummyClass.getDeclaredField("jsonAdapterField");

    TypeAdapter<String> stringAdapter = new StringTypeAdapter();
    JsonAdapter<Integer> intAdapter = new JsonAdapter<Integer>() {};

    TypeToken<DummyClass> type = new TypeToken<DummyClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>() {{
      put("serializedNameField", new ReflectiveTypeAdapterFactory.BoundField("serializedNameField", true, false) {
        @Override
        void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
          String valueStr = (String) value;
          stringAdapter.write(writer, valueStr);
        }

        @Override
        void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
          JsonElement jsonElement = reader.nextElement();
          String valueStr = jsonElement.getAsString();
          field1.set(value, valueStr);
        }
      });

      put("primitiveField", new ReflectiveTypeAdapterFactory.BoundField("primitiveField", true, false) {
        @Override
        void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
          int valueInt = (int) value;
          intAdapter.write(writer, valueInt);
        }

        @Override
        void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
          JsonElement jsonElement = reader.nextElement();
          int valueInt = jsonElement.getAsInt();
          field2.set(value, valueInt);
        }

        @Override
        boolean writeField(Object value) throws IOException, IllegalAccessException {
          if (!serialized) return false;
          int valueInt = (int) field2.get(value);
          return valueInt != field2.get(value);
        }
      });

      put("jsonAdapterField", new ReflectiveTypeAdapterFactory.BoundField("jsonAdapterField", true, false) {
        @Override
        void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
          JsonAdapter jsonAdapter = (JsonAdapter) field3.get(value);
          jsonAdapter.write(writer, jsonAdapter.getObject());
        }

        @Override
        void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
          JsonAdapter jsonAdapter = getTypeAdapter(new ConstructorConstructor(), new Gson(), field3.getType(), new JsonAdapter<JsonAdapter>() {
            @Override
            JsonAdapter getObject() {
              return null;
            }
          });
          jsonAdapter.read(reader);
          field3.set(value, jsonAdapter);
        }
      });
    }};

    Object dummy = new DummyClass();
    JsonObject json = new JsonObject();
    json.addProperty("serializedNameField", "value1");
    json.addProperty("primitiveField", 42);
    json.add("jsonAdapterField", new JsonObject() {{
      add("class", "com.example.JsonAdapter");
    }});

    TypeAdapter<DummyClass> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
        new ObjectConstructor<DummyClass>() {},
        boundFields
    );

    adapter.write(new JsonWriter(System.out), dummy);

    assertEquals("{\"serializedNameField\":\"value1\",\"primitiveField\":42,\"jsonAdapterField\":{}}", json.toString());
  }

  public static class DummyClass {
    @SerializedName("jsonName")
    String serializedNameField;
    int primitiveField;
    @JsonAdapter(JsonAdapter.class)
    JsonAdapter jsonAdapterField;
  }

  public static class JsonAdapter {
    @JsonAdapter(StringTypeAdapter.class)
    String object;
  }

  public static class StringTypeAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }
  }
}

class FieldNamingPolicy extends FieldNamingStrategy {
  @Override
  public String translateName(Field field) {
    return field.getName();
  }
}