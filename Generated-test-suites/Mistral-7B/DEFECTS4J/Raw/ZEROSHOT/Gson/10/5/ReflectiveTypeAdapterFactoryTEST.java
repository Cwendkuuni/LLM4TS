import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.excludeField;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    TestClass testClass = new TestClass();

    // Test serialization
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    JsonWriter writer = new JsonWriter(System.out);
    gson.toJson(testClass, writer);
    writer.flush();

    // Test deserialization
    JsonReader reader = new JsonReader(System.in);
    JsonElement jsonElement = reader.readTree();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    TestClass deserializedClass = gson.fromJson(jsonObject, TestClass.class);

    // Test reflection based type adapter factory
    FieldNamingPolicy fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
    Excluder excluder = new Excluder() {
      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }

      @Override public boolean excludeField(Field field, boolean serialize) {
        return !serialize && field.getName().equals("excludedField");
      }
    };
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(TestClass.class);
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory =
        new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<TestClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<TestClass>() {}.getType());

    // Test serialization using reflection based type adapter
    JsonWriter writer2 = new JsonWriter(System.out);
    adapter.write(writer2, testClass);
    writer2.flush();

    // Test deserialization using reflection based type adapter
    JsonReader reader2 = new JsonReader(System.in);
    TestClass deserializedClass2 = adapter.read(reader2);

    // Test equality
    if (!testClass.equals(deserializedClass) || !testClass.equals(deserializedClass2)) {
      throw new RuntimeException("Serialization and deserialization are not equal");
    }

    // Test excluded field
    TestClassWithoutExcludedField testClassWithoutExcludedField = new TestClassWithoutExcludedField();
    TestClassWithoutExcludedField deserializedClassWithoutExcludedField = adapter.read(reader2);
    if (deserializedClassWithoutExcludedField.excludedField != null) {
      throw new RuntimeException("Excluded field was not excluded during deserialization");
    }

    // Test writing and reading primitive types
    Gson gsonPrimitive = new GsonBuilder().create();
    TestClassWithPrimitiveTypes testClassWithPrimitiveTypes = new TestClassWithPrimitiveTypes();
    TypeAdapter<TestClassWithPrimitiveTypes> adapterPrimitive =
        reflectiveTypeAdapterFactory.create(gsonPrimitive, new TypeToken<TestClassWithPrimitiveTypes>() {}.getType());

    JsonWriter writer3 = new JsonWriter(System.out);
    adapterPrimitive.write(writer3, testClassWithPrimitiveTypes);
    writer3.flush();

    JsonReader reader3 = new JsonReader(System.in);
    TestClassWithPrimitiveTypes deserializedClassWithPrimitiveTypes = adapterPrimitive.read(reader3);

    if (!testClassWithPrimitiveTypes.equals(deserializedClassWithPrimitiveTypes)) {
      throw new RuntimeException("Serialization and deserialization of primitive types are not equal");
    }

    System.out.println("All tests passed.");
  }

  static class TestClass {
    @SerializedName("name")
    private String name;
    private int integer;
    private boolean booleanValue;
    private List<String> list;

    @JsonAdapter(StringAdapter.class)
    private String excludedField;

    public TestClass() {
      list = new ArrayList<>();
    }

    public TestClass(String name, int integer, boolean booleanValue, List<String> list) {
      this();
      this.name = name;
      this.integer = integer;
      this.booleanValue = booleanValue;
      this.list.addAll(list);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getInteger() {
      return integer;
    }

    public void setInteger(int integer) {
      this.integer = integer;
    }

    public boolean isBooleanValue() {
      return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
      this.booleanValue = booleanValue;
    }

    public List<String> getList() {
      return list;
    }

    public void setList(List<String> list) {
      this.list = list;
    }

    public void setExcludedField(String excludedField) {
      this.excludedField = excludedField;
    }

    public String getExcludedField() {
      return excludedField;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TestClass that = (TestClass) o;

      if (booleanValue != that.booleanValue) return false;
      if (!list.equals(that.list)) return false;
      if (name != null ? !name.equals(that.name) : that.name != null) return false;
      if (integer != that.integer) return false;
      return excludedField == null ? that.excludedField == null : excludedField.equals(that.excludedField);
    }

    @Override public String toString() {
      return "TestClass{"
          + "name='" + name + '\''
          + ", integer=" + integer
          + ", booleanValue=" + booleanValue
          + ", list=" + list
          + ", excludedField=" + excludedField
          + '}';
    }
  }

  static class TestClassWithoutExcludedField {
    private String name;
    private int integer;
    private List<String> list;

    public TestClassWithoutExcludedField() {
      list = new ArrayList<>();
    }

    public TestClassWithoutExcludedField(String name, int integer, List<String> list) {
      this();
      this.name = name;
      this.integer = integer;
      this.list.addAll(list);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getInteger() {
      return integer;
    }

    public void setInteger(int integer) {
      this.integer = integer;
    }

    public List<String> getList() {
      return list;
    }

    public void setList(List<String> list) {
      this.list = list;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TestClassWithoutExcludedField that = (TestClassWithoutExcludedField) o;

      if (integer != that.integer) return false;
      if (!list.equals(that.list)) return false;
      if (name != null ? !name.equals(that.name) : that.name != null) return false;

      return true;
    }

    @Override public String toString() {
      return "TestClassWithoutExcludedField{"
          + "name='" + name + '\''
          + ", integer=" + integer
          + ", list=" + list
          + '}';
    }
  }

  static class TestClassWithPrimitiveTypes {
    private String name;
    private int integer;
    private boolean booleanValue;
    @SerializedName("floatValue")
    private float floatValue;

    public TestClassWithPrimitiveTypes() {
    }

    public TestClassWithPrimitiveTypes(String name, int integer, boolean booleanValue, float floatValue) {
      this();
      this.name = name;
      this.integer = integer;
      this.booleanValue = booleanValue;
      this.floatValue = floatValue;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getInteger() {
      return integer;
    }

    public void setInteger(int integer) {
      this.integer = integer;
    }

    public boolean isBooleanValue() {
      return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
      this.booleanValue = booleanValue;
    }

    public float getFloatValue() {
      return floatValue;
    }

    public void setFloatValue(float floatValue) {
      this.floatValue = floatValue;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TestClassWithPrimitiveTypes that = (TestClassWithPrimitiveTypes) o;

      if (booleanValue != that.booleanValue) return false;
      if (floatValue != that.floatValue) return false;
      if (integer != that.integer) return false;
      if (name != null ? !name.equals(that.name) : that.name != null) return false;

      return true;
    }

    @Override public String toString() {
      return "TestClassWithPrimitiveTypes{"
          + "name='" + name + '\''
          + ", integer=" + integer
          + ", booleanValue=" + booleanValue
          + ", floatValue=" + floatValue
          + '}';
    }
  }

  static class StringAdapter extends TypeAdapter<String> {
    @Override public String read(JsonReader in) throws IOException {
      return in.nextString();
    }

    @Override public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }
  }
}

class ReflectiveTypeAdapterFactory {
  // ... (the rest of the code remains the same)
}