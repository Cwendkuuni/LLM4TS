import com.google.gson.*;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testSerializationDeserialization() throws IOException {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());

    // When
    MyClass original = new MyClass("John Doe", 25);
    String json = gson.toJson(original, MyClass.class);
    MyClass deserialized = gson.fromJson(json, MyClass.class);

    // Then
    assertEquals(original, deserialized);
  }

  @Test
  public void testSerializationDeserializationWithJsonAdapter() throws IOException {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    MyClassAdapter myClassAdapter = new MyClassAdapter();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());

    // When
    MyClass original = new MyClass("John Doe", 25);
    String json = gson.toJson(original, MyClass.class, myClassAdapter);
    MyClass deserialized = gson.fromJson(json, MyClass.class, myClassAdapter);

    // Then
    assertEquals(original, deserialized);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testSerializationDeserializationWithMissingField() throws IOException {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    MyClassWithoutName original = new MyClassWithoutName(25);
    String json = gson.toJson(original, MyClassWithoutName.class);

    // When & Then
    try {
      MyClassWithoutName deserialized = gson.fromJson(json, MyClassWithoutName.class);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testSerializationDeserializationWithExcludedField() throws IOException {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder() {
          @Override
          public boolean excludeField(Field f, boolean serialize) {
            return f.getName().equals("age");
          }
        })
        .create();

    MyClassWithExcludedField original = new MyClassWithExcludedField("John Doe", 25);
    String json = gson.toJson(original, MyClassWithExcludedField.class);
    MyClassWithExcludedField deserialized = gson.fromJson(json, MyClassWithExcludedField.class);

    // Then
    assertEquals(original.getName(), deserialized.getName());
    assertNull(deserialized.getAge());
  }

  static class MyClass {
    private final String name;
    private final int age;

    public MyClass(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }
  }

  static class MyClassAdapter extends TypeAdapter<MyClass> {
    @Override public MyClass read(JsonReader in) throws IOException {
      String name = in.nextString();
      int age = in.nextInt();
      return new MyClass(name, age);
    }

    @Override public void write(JsonWriter out, MyClass value) throws IOException {
      out.value(value.getName());
      out.value(value.getAge());
    }
  }

  static class MyClassWithoutName {
    private final int age;

    public MyClassWithoutName(int age) {
      this.age = age;
    }

    public int getAge() {
      return age;
    }
  }

  static class MyClassWithExcludedField {
    private final String name;
    private final Integer age; // note: Java 8 or later required for int to Integer

    public MyClassWithExcludedField(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public Integer getAge() {
      return age;
    }
  }
}