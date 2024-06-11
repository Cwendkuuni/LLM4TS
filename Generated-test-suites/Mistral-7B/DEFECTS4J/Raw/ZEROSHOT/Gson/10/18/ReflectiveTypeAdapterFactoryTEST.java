import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.excludeField;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

  private Excluder excluder;
  private FieldNamingPolicy fieldNamingPolicy;
  private ConstructorConstructor constructorConstructor;
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;

  @Before
  public void setUp() {
    excluder = mock(Excluder.class);
    fieldNamingPolicy = new FieldNamingPolicy() {
      @Override public String translateName(Field field) {
        return field.getName();
      }
    };
    constructorConstructor = new ConstructorConstructor(new ObjectConstructor[0]);
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    gson = new GsonBuilder().setExcluder(excluder).create();
  }

  @Test
  public void testExcludeField() {
    Class<?> clazz = MyClass.class;
    Field field = clazz.getDeclaredField("excludedField");
    Assert.assertTrue(excludeField(field, true));
    Assert.assertFalse(excludeField(field, false));

    field.setAccessible(true);
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(clazz));
    MyClass instance = adapter.read(new JsonReader(new ByteArrayInputStream(new byte[0])));
    Assert.assertNull(instance.getExcludedField());
  }

  @Test
  public void testCreateTypeAdapter() {
    Class<?> clazz = MyClass.class;
    Field field1 = clazz.getDeclaredField("field1");
    Field field2 = clazz.getDeclaredField("field2");

    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(clazz));

    MyClass instance = adapter.read(new JsonReader(new ByteArrayInputStream(new byte[0])));

    Assert.assertEquals(0, instance.getField1());
    Assert.assertEquals("Value2", instance.getField2());
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    Class<?> clazz = MyClass.class;
    Field field1 = clazz.getDeclaredField("field1");
    Field field2 = clazz.getDeclaredField("field2");

    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(clazz));

    MyClass instance = new MyClass(1, "Value2");

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(output);
    adapter.write(writer, instance);
    writer.close();
    JsonElement jsonElement = new JsonObject(new String(output.toByteArray()));

    MyClass deserializedInstance = adapter.read(new JsonReader(new ByteArrayInputStream(jsonElement.getAsBytes())));

    Assert.assertEquals(instance.getField1(), deserializedInstance.getField1());
    Assert.assertEquals(instance.getField2(), deserializedInstance.getField2());
  }

  @Test(expected = IllegalStateException.class)
  public void testMultipleFieldsWithSameName() {
    Class<?> clazz = MyClassWithMultipleFields.class;
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<MyClassWithMultipleFields> adapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(clazz));
    adapter.read(new JsonReader(new ByteArrayInputStream(new byte[0])));
  }

  @Test
  public void testJsonAdapter() {
    Class<?> clazz = MyClassWithJsonAdapter.class;
    Field field = clazz.getDeclaredField("field");

    JsonAdapter jsonAdapter = mock(JsonAdapter.class);
    when(jsonAdapter.read(Mockito.any(JsonReader.class))).thenReturn("Json value");
    when(jsonAdapter.write(Mockito.any(JsonWriter.class), Mockito.any(String.class))).thenReturn(null);

    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<MyClassWithJsonAdapter> adapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(clazz));

    MyClassWithJsonAdapter instance = adapter.read(new JsonReader(new ByteArrayInputStream(new byte[0])));
    Assert.assertEquals("Json value", instance.getField());
  }

  static class MyClass {
    @SerializedName("field1")
    int field1;
    @SerializedName("field2")
    String field2;

    MyClass(int field1, String field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    int getField1() {
      return field1;
    }

    String getField2() {
      return field2;
    }

    private int excludedField;
  }

  static class MyClassWithMultipleFields {
    @SerializedName("field1")
    int field1;
    @SerializedName("field1")
    String field2;
  }

  static class MyClassWithJsonAdapter {
    @JsonAdapter(MyJsonAdapter.class)
    String field;
  }

  static class MyJsonAdapter extends TypeAdapter<String> {
    @Override public String read(JsonReader in) throws IOException {
      return null;
    }

    @Override public void write(JsonWriter out, String value) throws IOException {
      out.value("Value");
    }
  }
}