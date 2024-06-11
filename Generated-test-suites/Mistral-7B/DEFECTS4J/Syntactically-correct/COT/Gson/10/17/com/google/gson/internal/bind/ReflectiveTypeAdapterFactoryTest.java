import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.Excluder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Types;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter.Builder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ReflectiveTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;

  @Before
  public void setup() {
    gson = new Gson();
    constructorConstructor = new ConstructorConstructor(gson);
    fieldNamingPolicy = new FieldNamingPolicy();
    excluder = new Excluder();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  @Test
  public void testCreate() {
    Class<MyClass> type = MyClass.class;
    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeField() {
    Class<MyClass> type = MyClass.class;
    Field excludedField = MyClass.class.getDeclaredField("excludedField");
    assertFalse(factory.excludeField(excludedField, true));
    assertFalse(factory.excludeField(excludedField, false));

    excludedField.setAccessible(true);
    excludedField.set(new MyClass(), "excludedValue");

    MyClass instance = new MyClass();
    assertFalse(factory.excludeField(excludedField, true).writeField(instance));
    assertNull(excludedField.get(instance));

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(new byte[0]));
    MyClass deserializedInstance = factory.create(gson, new TypeToken<MyClass>() {}).read(jsonReader);
    assertNull(excludedField.get(deserializedInstance));
  }

  @Test
  public void testGetBoundFields() {
    Class<MyClass> type = MyClass.class;
    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});

    Map<String, BoundField> boundFields = adapter.getClass().getDeclaredField("boundFields")
        .get(adapter);
    assertEquals(3, boundFields.size());

    BoundField field1 = boundFields.get("name1");
    BoundField field2 = boundFields.get("name2");
    BoundField field3 = boundFields.get("name3");

    assertTrue(field1.writeField(new MyClass()));
    assertTrue(field2.writeField(new MyClass()));
    assertFalse(field3.writeField(new MyClass()));

    assertTrue(field1.serialized);
    assertTrue(field1.deserialized);
    assertFalse(field2.serialized);
    assertFalse(field2.deserialized);
    assertFalse(field3.serialized);
    assertFalse(field3.deserialized);
  }

  @Test
  public void testAdapterSerialization() throws IOException {
    MyClass instance = new MyClass();
    instance.name1 = "name1";
    instance.name2 = "name2";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(outputStream);
    adapter.write(writer, instance);
    writer.flush();

    JsonReader reader = new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()));
    MyClass deserializedInstance = factory.create(gson, new TypeToken<MyClass>() {}).read(reader);
    assertEquals(instance.name1, deserializedInstance.name1);
    assertEquals(instance.name2, deserializedInstance.name2);
  }

  @Test
  public void testAdapterDeserialization() throws IOException {
    String json = "{\"name1\":\"name1\", \"name3\":\"name3\"}";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());
    JsonReader jsonReader = new JsonReader(inputStream);

    MyClass deserializedInstance = factory.create(gson, new TypeToken<MyClass>() {}).read(jsonReader);
    assertNull(deserializedInstance.name2);
    assertEquals("name1", deserializedInstance.name1);
    assertEquals("name3", deserializedInstance.name3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateFieldName() throws IOException {
    Class<MyClassWithDuplicateFields> type = MyClassWithDuplicateFields.class;
    TypeAdapter<MyClassWithDuplicateFields> adapter = factory.create(gson, new TypeToken<MyClassWithDuplicateFields>() {});
  }

  @Test
  public void testJsonAdapter() throws IOException {
    Class<MyClassWithJsonAdapter> type = MyClassWithJsonAdapter.class;
    TypeAdapter<MyClassWithJsonAdapter> adapter = factory.create(gson, new TypeToken<MyClassWithJsonAdapter>() {});

    assertInstanceOf(JsonAdapter.class, adapter.getClass().getDeclaredField("jsonAdapter")
        .get(adapter));

    MyClassWithJsonAdapter instance = new MyClassWithJsonAdapter();
    instance.number = 123;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(outputStream);
    adapter.write(writer, instance);
    writer.flush();

    JsonReader reader = new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()));
    MyClassWithJsonAdapter deserializedInstance = factory.create(gson, new TypeToken<MyClassWithJsonAdapter>() {})
        .read(reader);
    assertEquals(instance.number, deserializedInstance.number);
  }

  @Test
  public void testAdapterFactoryWithAnnotations() {
    Class<MyClassWithAnnotations> type = MyClassWithAnnotations.class;
    TypeAdapter<MyClassWithAnnotations> adapter = factory.create(gson, new TypeToken<MyClassWithAnnotations>() {});

    assertEquals(MyClassWithAnnotationsAdapter.class, adapter.getClass());
  }

  static class MyClass {
    String name1;
    String name2;
    @JsonAdapter(MyClassStringAdapter.class)
    String name3;

    public MyClass() {}

    public String getName1() {
      return name1;
    }

    public void setName1(String name1) {
      this.name1 = name1;
    }

    public String getName2() {
      return name2;
    }

    public void setName2(String name2) {
      this.name2 = name2;
    }

    public String getName3() {
      return name3;
    }

    public void setName3(String name3) {
      this.name3 = name3;
    }

    public static class MyClassStringAdapter extends TypeAdapter<String> {
      @Override public String read(JsonReader in) throws IOException {
        return in.nextString();
      }

      @Override public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
      }
    }
  }

  static class MyClassWithDuplicateFields {
    String name1;
    String name1;
  }

  static class MyClassWithJsonAdapter {
    int number;
  }

  static class MyClassWithAnnotations {
    @JsonAdapter(MyClassStringAdapter.class)
    String name;
  }

  static class FieldNamingPolicy extends FieldNamingStrategy {
    @Override public String translateName(Field f) {
      return f.getName().toUpperCase();
    }
  }

  static class Excluder implements Excluder {
    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }

    @Override public boolean excludeField(Field f, boolean serialize) {
      return false;
    }
  }
}