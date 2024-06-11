package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonStreamParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder.ExcludedField;
import com.google.gson.internal.Excluder.ExcludedClass;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.TypeToken;
import com.google.gson.reflect.TypeTokenBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonStreamParser.State;
import com.google.gson.stream.JsonWriter;

public class ReflectiveTypeAdapterFactoryTest {

  interface TestClass {
    @JsonAdapter(TestAdapter.class)
    @SerializedName("test")
    int testField = 0;

    @SerializedName("foo")
    String otherField = "bar";

    TestClass otherInstance = new OtherTestClass();
  }

  static class OtherTestClass {
    String field1 = "test1";
    int field2 = 1;

    @SerializedName("test")
    int testField = 2;
  }

  static class TestAdapter extends TypeAdapter<TestClass> {
    @Override public TestClass read(JsonReader in) throws IOException {
      TestClass instance = new TestClass();
      in.beginObject();
      String name = null;
      while (in.hasNext()) {
        name = in.nextName();
        if (name.equals("test")) {
          instance.testField = in.nextInt();
        } else if (name.equals("otherField")) {
          instance.otherField = in.nextString();
        } else if (name.equals("otherInstance")) {
          in.beginArray();
          instance.otherInstance = readOtherInstance(in);
          in.endArray();
        } else {
          in.skipValue();
        }
      }
      in.endObject();
      return instance;
    }

    @Override public void write(JsonWriter out, TestClass value) throws IOException {
      out.beginObject();
      out.name("test").value(value.testField);
      out.name("otherField").value(value.otherField);
      writeOtherInstance(out, value.otherInstance);
      out.endObject();
    }

    static TestClass readOtherInstance(JsonReader in) throws IOException {
      in.beginObject();
      int testField = in.nextInt();
      in.endObject();
      return new OtherTestClass();
    }

    static void writeOtherInstance(JsonWriter out, OtherTestClass value) throws IOException {
      out.beginObject();
      out.name("test").value(value.testField);
      out.endObject();
    }
  }

  static class TestFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingPolicy fieldNamingPolicy;
    private final Excluder excluder;

    TestFactory(ConstructorConstructor constructorConstructor,
        FieldNamingPolicy fieldNamingPolicy, Excluder excluder) {
      this.constructorConstructor = constructorConstructor;
      this.fieldNamingPolicy = fieldNamingPolicy;
      this.excluder = excluder;
    }

    @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return new ReflectiveTypeAdapterFactory.Adapter<T>(
          constructorConstructor.get(type),
          getBoundFields(gson, type, TestClass.class)
      ).nullSafe();
    }

    private Map<String, ReflectiveTypeAdapterFactory.BoundField> getBoundFields(
        Gson context, TypeToken<?> type, Class<?> raw) {
      Map<String, ReflectiveTypeAdapterFactory.BoundField> result = new LinkedHashMap<>();
      Field[] fields = raw.getDeclaredFields();
      for (Field field : fields) {
        boolean serialize = excludeField(field, true);
        boolean deserialize = excludeField(field, false);
        if (!serialize && !deserialize) continue;
        field.setAccessible(true);
        Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
        List<String> fieldNames = getFieldNames(field);
        for (int i = 0; i < fieldNames.size(); ++i) {
          String name = fieldNames.get(i);
          if (i != 0) serialize = false; // only serialize the default name
          ReflectiveTypeAdapterFactory.BoundField boundField = createBoundField(
              context, field, name, fieldType, serialize, deserialize);
          ReflectiveTypeAdapterFactory.BoundField replaced = result.put(name, boundField);
          if (replaced != null) {
            throw new IllegalArgumentException(TestClass.class.getName() +
                " declares multiple JSON fields named " + replaced.name);
          }
        }
      }
      return result;
    }

    private ReflectiveTypeAdapterFactory.BoundField createBoundField(
        final Gson context, final Field field, final String name,
        final Type fieldType, boolean serialize, boolean deserialize) {
      final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
      JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
      TypeAdapter<?> mapped = null;
      if (annotation != null) {
        mapped = getTypeAdapter(constructorConstructor, context, fieldType, annotation);
      }
      final boolean jsonAdapterPresent = mapped != null;
      if (mapped == null) mapped = context.getAdapter(fieldType);

      final TypeAdapter<?> typeAdapter = mapped;
      return new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {
        @Override void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
          Object fieldValue = field.get(value);
          TypeAdapter t =
            new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
          t.write(writer, fieldValue);
        }
        @Override void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
          Object fieldValue = typeAdapter.read(reader);
          if (fieldValue != null || !isPrimitive) {
            field.set(value, fieldValue);
          }
        }
        @Override public boolean writeField(Object value) throws IOException, IllegalAccessException {
          if (!serialize) return false;
          Object fieldValue = field.get(value);
          return fieldValue != value; // avoid recursion for example for Throwable.cause
        }
      };
    }
  }

  @Test
  public void testReflection() throws IOException {
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder())
        .create();

    TestFactory testFactory = new TestFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override public String translateName(Field f) {
            return f.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return clazz.isInterface();
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return f.isSynthetic() || f.isTransient();
          }
        }
    );

    TypeAdapter<TestClass> testAdapter = testFactory.create(gson, new TypeToken<TestClass>() {});

    TestClass testInstance = new TestClass();
    testInstance.testField = 1;
    testInstance.otherField = "test";

    JsonObject json = new JsonObject();
    json.add("test", testAdapter.toJsonTree(testInstance));
    json.add("otherField", JsonElement.parse("test"));
    json.add("otherInstance", testAdapter.toJsonTree(testInstance.otherInstance));

    JsonReader jsonReader = new JsonReader(new StringReader(json.toString()));
    TestClass deserializedInstance = testAdapter.read(jsonReader);

    assertNotNull(deserializedInstance);
    assertEquals(1, deserializedInstance.testField);
    assertEquals("test", deserializedInstance.otherField);
    assertNull(deserializedInstance.otherInstance);
    assertNotNull(deserializedInstance.otherInstance); // it gets initialized during deserialization

    JsonStreamParser parser = new JsonParser().parse(json.toString()).getParser();
    testAdapter.read(parser);

    TestClass deserializedInstance2 = testAdapter.read(jsonReader);
    assertEquals(deserializedInstance, deserializedInstance2);

    JsonObject json2 = new JsonObject();
    json2.add("test", JsonElement.parse(String.valueOf(1)));
    json2.add("otherField", JsonElement.parse("test"));
    json2.add("otherInstance", JsonElement.parse("{}"));

    testAdapter = testFactory.create(gson, new TypeToken<TestClass>() {});
    TestClass deserializedInstance3 = testAdapter.read(new JsonReader(new StringReader(json2.toString())));

    Field testField = TestClass.class.getDeclaredField("testField");
    ExcludedField fieldAnnotation = testField.getAnnotation(ExcludedField.class);

    assertNotNull(fieldAnnotation);
    assertFalse(deserializedInstance3.testField == 1); // the field is excluded

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = getBoundFields(gson, new TypeToken<TestClass>() {}, TestClass.class);
    ReflectiveTypeAdapterFactory.BoundField testFieldBoundField = boundFields.get("testField");

    assertNotNull(testFieldBoundField);
    assertNull(testFieldBoundField.deserialized);

    TestClass testInstance2 = new TestClass();
    testInstance2.testField = 2;
    testAdapter.write(new JsonWriter(System.out), testInstance2);

    JsonObject json3 = new JsonObject();
    json3.add("test", JsonElement.parse(String.valueOf(2)));
    json3.add("otherField", JsonElement.parse("test"));

    JsonReader jsonReader2 = new JsonReader(new StringReader(json3.toString()));
    TestClass deserializedInstance4 = testAdapter.read(jsonReader2);

    assertEquals(testInstance2, deserializedInstance4);
  }

  private Map<String, ReflectiveTypeAdapterFactory.BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = new LinkedHashMap<>();
    // ... same code as in the factory ...
  }

  private static boolean excludeField(Field f, boolean serialize, Excluder excluder) {
    // ... same code as in the factory ...
  }

  private static List<String> getFieldNames(Field f) {
    // ... same code as in the factory ...
  }
}