import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
  @SuppressWarnings("unchecked")
  private static final Gson gson = new GsonBuilder().create();

  

  // Test create() method
  @org.junit.Test
  public void testCreate() {
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy(),
        new Excluder()
    );

    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertNotNull(adapter);
  }

  // Test excludeField() method
  @org.junit.Test
  public void testExcludeField() {
    Class myClass = MyClass.class;
    Field field = myClass.getDeclaredField("fieldToExclude");
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(field, true));
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(field, false));

    MyClass obj = new MyClass();
    assertFalse(obj.writeField(field));
  }

  // Test getFieldNames() method
  @org.junit.Test
  public void testGetFieldNames() throws NoSuchFieldException, IllegalAccessException {
    Class myClass = MyClass.class;
    Field field = myClass.getDeclaredField("fieldWithAnnotation");

    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(2, fieldNames.size());
    assertEquals("fieldName1", fieldNames.get(0));
    assertEquals("fieldName2", fieldNames.get(1));
  }

  // Test createBoundField() method
  @org.junit.Test
  public void testCreateBoundField() throws NoSuchFieldException, IllegalAccessException {
    Class myClass = MyClass.class;
    Field field = myClass.getDeclaredField("fieldWithAnnotation");

    String[] alternates = {"alternate1", "alternate2"};
    List<String> fieldNames = new ArrayList<>();
    fieldNames.add("originalName");
    fieldNames.addAll(List.of(alternates));

    BoundField boundField = ReflectiveTypeAdapterFactory.createBoundField(
        gson, field, "originalName", new TypeToken<String>() {});

    assertEquals("originalName", boundField.name);
    assertTrue(boundField.serialized);
    assertTrue(boundField.deserialized);
  }

  // Test getBoundFields() method
  @org.junit.Test
  public void testGetBoundFields() throws NoSuchFieldException, IllegalAccessException {
    Class myClass = MyClass.class;

    Map<String, BoundField> boundFields = ReflectiveTypeAdapterFactory.getBoundFields(
        gson, new TypeToken<MyClass>() {}, myClass
    );

    assertEquals(3, boundFields.size());
    assertNotNull(boundFields.get("fieldName1"));
    assertNotNull(boundFields.get("fieldName2"));
    assertNotNull(boundFields.get("fieldWithAnnotation"));
  }

  // Test BoundField writeField() method
  @org.junit.Test
  public void testBoundFieldWriteField() throws NoSuchFieldException, IllegalAccessException, IOException {
    Class myClass = MyClass.class;
    Field field = myClass.getDeclaredField("fieldWithAnnotation");

    MyClass obj = new MyClass();
    boolean written = ReflectiveTypeAdapterFactory.getBoundFields(
        gson, new TypeToken<MyClass>() {}, myClass
    ).get("fieldWithAnnotation").writeField(obj);

    assertFalse(written);
  }

  // Test BoundField write() method
  @org.junit.Test
  public void testBoundFieldWrite() throws NoSuchFieldException, IllegalAccessException, IOException {
    Class myClass = MyClass.class;
    Field field = myClass.getDeclaredField("fieldWithAnnotation");

    MyClass obj = new MyClass();
    ObjectAdapter fieldAdapter = new ObjectAdapter(gson, new TypeToken<MyClass>() {});
    TypeAdapter<String> stringAdapter = new StringAdapter();

    ReflectiveTypeAdapterFactory.BoundField boundField = ReflectiveTypeAdapterFactory.getBoundFields(
        gson, new TypeToken<MyClass>() {}, myClass
    ).get("fieldWithAnnotation");

    String json = "{\"fieldWithAnnotation\": \"value\"}";
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

    try (JsonReader reader = new JsonReader(new StringReader(json))) {
      boundField.read(reader, obj);
    }

    assertEquals("value", field.get(obj));

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    boundField.write(jsonWriter, obj);
    assertEquals("{\"fieldWithAnnotation\": \"value\"}", jsonWriter.toString());
  }

  // Test Adapter write() method
  @org.junit.Test
  public void testAdapterWrite() throws NoSuchFieldException, IllegalAccessException, IOException {
    Class myClass = MyClass.class;

    Map<String, BoundField> boundFields = ReflectiveTypeAdapterFactory.getBoundFields(
        gson, new TypeToken<MyClass>() {}, myClass
    );

    Adapter<MyClass> adapter = (Adapter<MyClass>) ReflectiveTypeAdapterFactory.create(
        gson, new TypeToken<MyClass>() {}
    );

    MyClass obj = new MyClass();

    String json = "{\"fieldName1\": 1, \"fieldName2\": \"2\", \"fieldWithAnnotation\": 3}";
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

    try (JsonReader reader = new JsonReader(new StringReader(json))) {
      adapter.read(reader, obj);
    }

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, obj);

    assertEquals("{\"fieldName1\": 1, \"fieldName2\": \"2\", \"fieldWithAnnotation\": 3}", jsonWriter.toString());
  }

  

  static class MyClass {
    int fieldName1;
    String fieldName2;
    @JsonAdapter(StringAdapter.class)
    transient String fieldWithAnnotation;
  }

  static class StringAdapter extends TypeAdapter<String> {
    @Override public String read(JsonReader reader) throws IOException {
      return reader.nextString();
    }

    @Override public void write(JsonWriter writer, String value) throws IOException {
      writer.value(value);
    }
  }
}