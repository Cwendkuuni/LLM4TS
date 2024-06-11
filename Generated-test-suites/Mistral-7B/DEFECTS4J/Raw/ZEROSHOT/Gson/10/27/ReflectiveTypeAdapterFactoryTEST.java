import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.LinkedHashMap;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final class TestClass {
    @SerializedName("name1") private String name1;
    @SerializedName("name2") private int name2;
    @SerializedName("name3") private boolean name3;

    @JsonAdapter(JsonAdapterForTestClass.class)
    private TestInner inner;

    private static class TestInner {
      @SerializedName("innerName1") private String innerName1;
      @SerializedName("innerName2") private int innerName2;
      @SerializedName("innerName3") private boolean innerName3;
    }
  }

  private static final class JsonAdapterForTestClass extends TypeAdapter<TestClass> {
    @Override public TestClass read(JsonReader in) throws IOException {
      TestClass result = new TestClass();
      in.beginObject();
      while (in.hasNext()) {
        String name = in.nextName();
        if ("name1".equals(name)) {
          result.name1 = in.nextString();
        } else if ("name2".equals(name)) {
          result.name2 = in.nextInt();
        } else if ("name3".equals(name)) {
          result.name3 = in.nextBoolean();
        } else if ("inner".equals(name)) {
          in.beginObject();
          result.inner = new TestClass.TestInner();
          while (in.hasNext()) {
            String innerName = in.nextName();
            if ("innerName1".equals(innerName)) {
              result.inner.innerName1 = in.nextString();
            } else if ("innerName2".equals(innerName)) {
              result.inner.innerName2 = in.nextInt();
            } else if ("innerName3".equals(innerName)) {
              result.inner.innerName3 = in.nextBoolean();
            }
          }
          in.endObject();
        } else {
          in.skipValue();
        }
      }
      in.endObject();
      return result;
    }

    @Override public void write(JsonWriter out, TestClass value) throws IOException {
      out.beginObject();
      out.name("name1").value(value.name1);
      out.name("name2").value(value.name2);
      out.name("name3").value(value.name3);
      out.name("inner").beginObject();
      out.name("innerName1").value(value.inner.innerName1);
      out.name("innerName2").value(value.inner.innerName2);
      out.name("innerName3").value(value.inner.innerName3);
      out.endObject();
      out.endObject();
    }
  }

  private static final class ConstructorConstructor {
    private ConstructorConstructor() {}

    private <T> ObjectConstructor<T> get(TypeToken<T> type) {
      try {
        Class<T> clazz = type.getRawType().asSubclass(Object.class);
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            try {
              return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
              throw new RuntimeException(e);
            }
          }
        };
      } catch (NoSuchMethodException e) {
        throw new AssertionError(e);
      }
    }
  }

  private static final class FieldNamingStrategyDefault implements FieldNamingStrategy {
    @Override public String translateName(Field field) {
      return field.getName();
    }
  }

  private static final class ExcluderDefault implements Excluder {
    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }

    @Override public boolean excludeField(Field field, boolean serialize) {
      return Modifier.isFinal(field.getModifiers()) || field.isSynthetic();
    }
  }

  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;

  @Before public void setup() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategyDefault();
    Excluder excluder = new ExcluderDefault();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    gson = new GsonBuilder().setExclusionStrategies(new Excluder[] {excluder}).create();
  }

  @Test public void testCreate() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);
  }

  @Test public void testCreateWithInterface() {
    TypeToken<Object> typeToken = new TypeToken<Object>() {};
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test public void testWriteReadFields() throws IOException {
    TestClass original = new TestClass();
    original.name1 = "Test";
    original.name2 = 123;
    original.name3 = true;
    original.inner = new TestClass.TestInner();
    original.inner.innerName1 = "InnerTest";
    original.inner.innerName2 = 456;
    original.inner.innerName3 = false;

    JsonElement jsonElement = gson.toJsonTree(original, TestClass.class);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    TestClass deserialized = factory.create(gson, typeToken).read(new JsonReader(new StringReader(jsonObject.toString())));

    assertEquals(original.name1, deserialized.name1);
    assertEquals(original.name2, deserialized.name2);
    assertEquals(original.name3, deserialized.name3);
    assertEquals(original.inner.innerName1, deserialized.inner.innerName1);
    assertEquals(original.inner.innerName2, deserialized.inner.innerName2);
    assertEquals(original.inner.innerName3, deserialized.inner.innerName3);
  }

  @Test public void testWriteReadFieldWithJsonAdapter() throws IOException {
    TestClass original = new TestClass();
    original.name1 = "Test";
    original.name2 = 123;
    original.name3 = true;

    TestClass deserialized = factory.create(gson, typeToken).read(new JsonReader(new StringReader(
        "{\"name1\":\"Test\",\"name3\":true,\"inner\":{\"innerName3\":false}}")));

    assertEquals(original.name1, deserialized.name1);
    assertEquals(original.name2, deserialized.name2);
    assertFalse(original.name3);
    assertNull(original.inner);
    assertNull(deserialized.inner);
  }

  @Test(expected = JsonSyntaxException.class) public void testReadWithNullValue() throws IOException {
    TestClass original = new TestClass();

    JsonReader jsonReader = new JsonReader(new StringReader("{\"name1\":null,\"name2\":123}"));
    TestClass deserialized = factory.create(gson, typeToken).read(jsonReader);

    assertNull(deserialized);
  }

  @Test public void testWriteField() throws IOException, IllegalAccessException {
    TestClass original = new TestClass();
    original.name1 = "Test";
    original.name2 = 123;
    original.name3 = true;
    original.inner = new TestClass.TestInner();
    original.inner.innerName1 = "InnerTest";
    original.inner.innerName2 = 456;
    original.inner.innerName3 = false;

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);

    adapter.write(jsonWriter, original);
    JsonObject jsonObject = JsonParser.parseObject(jsonWriter.toString());

    TestClass deserialized = factory.create(gson, typeToken).read(new JsonReader(new StringReader(jsonObject.toString())));

    TestClass modified = new TestClass();
    Field name1Field = TestClass.class.getDeclaredField("name1");
    name1Field.setAccessible(true);
    name1Field.set(modified, "ModifiedTest");

    adapter.write(jsonWriter, modified);
    JsonObject jsonObject2 = JsonParser.parseObject(jsonWriter.toString());

    assertEquals(deserialized.name1, original.name1);
    assertEquals(deserialized.name1, jsonObject.get("name1").getAsString());
    assertEquals(modified.name1, jsonObject2.get("name1").getAsString());
  }

  @Test public void testWriteFieldWithJsonAdapter() throws IOException, IllegalAccessException {
    TestClass original = new TestClass();
    original.name1 = "Test";
    original.name2 = 123;
    original.name3 = true;
    original.inner = new TestClass.TestInner();
    original.inner.innerName1 = "InnerTest";
    original.inner.innerName2 = 456;
    original.inner.innerName3 = false;

    ReflectiveTypeAdapterFactory.BoundField boundField = factory.getBoundFields(gson, typeToken, TestClass.class).get("name3");
    TypeAdapterRuntimeTypeWrapper typeAdapterRuntimeTypeWrapper = new TypeAdapterRuntimeTypeWrapper(gson, boundField.typeAdapter, TestClass.class);

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    TestClass modified = new TestClass();
    Modifier.setFinal(Modifier.FINAL, false);
    Field name3Field = TestClass.class.getDeclaredField("name3");
    name3Field.setAccessible(true);
    name3Field.set(modified, false);

    typeAdapterRuntimeTypeWrapper.write(jsonWriter, modified);
    JsonObject jsonObject = JsonParser.parseObject(jsonWriter.toString());

    assertEquals(original.name1, jsonObject.get("name1").getAsString());
    assertFalse(original.name3);
    assertFalse(jsonObject.get("name3").getAsBoolean());
    assertTrue(modified.name3);
    assertFalse(modified.name3);
  }
}