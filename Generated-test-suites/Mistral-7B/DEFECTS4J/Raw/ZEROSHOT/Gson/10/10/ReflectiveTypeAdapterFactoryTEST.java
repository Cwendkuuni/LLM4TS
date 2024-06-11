import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() throws IOException {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingStrategy(FieldNamingStrategy.IDENTITY)
        .setExcluder(new Excluder())
        .setPrePreprocessor(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            FieldNamingStrategy.IDENTITY,
            new Excluder()))
        .create();

    MyClass myClass = new MyClass("Test", 42, 3.14);

    String json = gson.toJson(myClass);

    // When
    ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());
    JsonObject jsonObject = gson.fromJson(inputStream, JsonObject.class);

    // Then
    assertNotNull(jsonObject);
    assertEquals(3, jsonObject.entrySet().size());

    Map<String, JsonElement> jsonEntries = jsonObject.entrySet();
    List<Map.Entry<String, JsonElement>> jsonEntryList = new ArrayList<>(jsonEntries);

    // Test serialization order
    assertEquals("name", jsonEntryList.get(0).getKey());
    assertEquals("intField", jsonEntryList.get(1).getKey());
    assertEquals("doubleField", jsonEntryList.get(2).getKey());

    // Test deserialization
    MyClass deserializedMyClass = gson.fromJson(json, MyClass.class);
    assertEquals("Test", deserializedMyClass.name);
    assertEquals(42, deserializedMyClass.intField);
    assertEquals(3.14, deserializedMyClass.doubleField, 0.001);

    // Test JsonAdapter
    MyJsonClass jsonMyClass = new MyJsonClass("TestJson", 42.42);
    MyJsonClass jsonAdapterMyClass = new MyJsonClass("TestJsonJson", 42.43);

    MyJsonClass jsonAdapterMyClassWithJsonAdapter = new MyJsonClassWithJsonAdapter();

    MyJsonClass jsonMyClassWithJsonAdapter = new MyJsonClassWithJsonAdapter("TestJsonWithJsonAdapter", 42.42);

    JsonObject jsonObjectWithJsonAdapter = new JsonObject();
    jsonObjectWithJsonAdapter.add("name", gson.toJsonTree(jsonMyClassWithJsonAdapter.name));
    jsonObjectWithJsonAdapter.add("doubleField", gson.toJsonTree(jsonMyClassWithJsonAdapter.doubleField));

    jsonObjectWithJsonAdapter.addProperty("intField", 42);

    byte[] jsonWithJsonAdapterBytes = jsonObjectWithJsonAdapter.toString().getBytes();

    ByteArrayInputStream jsonWithJsonAdapterInputStream = new ByteArrayInputStream(jsonWithJsonAdapterBytes);

    MyJsonClass deserializedJsonAdapterMyClass = gson.fromJson(jsonWithJsonAdapterInputStream, MyJsonClass.class);

    assertEquals("TestJsonWithJsonAdapter", deserializedJsonAdapterMyClass.name);
    assertEquals(42.43, deserializedJsonAdapterMyClass.doubleField, 0.001);
  }

  // MyClass and MyJsonClass are used as examples for tested class ReflectiveTypeAdapterFactory
  // You should define these classes according to your needs

  public static class MyClass {
    @SerializedName("name")
    private final String name;
    @SerializedName("intField")
    private final int intField;
    @SerializedName("doubleField")
    private final double doubleField;

    public MyClass(String name, int intField, double doubleField) {
      this.name = name;
      this.intField = intField;
      this.doubleField = doubleField;
    }

    public String getName() {
      return name;
    }

    public int getIntField() {
      return intField;
    }

    public double getDoubleField() {
      return doubleField;
    }
  }

  public static class MyJsonClass {
    @SerializedName("name")
    private String name;
    @SerializedName("doubleField")
    private double doubleField;

    public MyJsonClass(String name, double doubleField) {
      this.name = name;
      this.doubleField = doubleField;
    }

    public String getName() {
      return name;
    }

    public double getDoubleField() {
      return doubleField;
    }
  }

  public static class MyJsonClassWithJsonAdapter {
    @SerializedName("name")
    private String name;
    @SerializedName("doubleField")
    private double doubleField;

    @JsonAdapter(MyJsonClassAdapter.class)
    private MyJsonClass jsonField;

    public MyJsonClassWithJsonAdapter() {
    }

    public MyJsonClassWithJsonAdapter(String name, double doubleField) {
      this.name = name;
      this.doubleField = doubleField;
      this.jsonField = new MyJsonClass(name, doubleField);
    }

    public String getName() {
      return name;
    }

    public double getDoubleField() {
      return doubleField;
    }

    public MyJsonClass getJsonField() {
      return jsonField;
    }
  }

  public static class MyJsonClassAdapter extends TypeAdapter<MyJsonClass> {
    @Override public MyJsonClass read(JsonReader in) throws IOException {
      JsonElement jsonElement = in.nextTree();
      assert jsonElement.isJsonObject();

      String name = jsonElement.getAsJsonObject().get("name").getAsString();
      double doubleField = jsonElement.getAsJsonObject().get("doubleField").getAsDouble();

      return new MyJsonClass(name, doubleField);
    }

    @Override public void write(JsonWriter out, MyJsonClass value) throws IOException {
      out.beginObject();
      out.name("name").value(value.getName());
      out.name("doubleField").value(value.getDoubleField());
      out.endObject();
    }
  }
}