import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;

  @Before
  public void setup() {
    this.gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();
  }

  @Test
  public void testCreate_NullTypeToken() {
    try {
      ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
          new ConstructorConstructor(),
          FieldNamingPolicy.IDENTITY,
          new Excluder());
      factory.create(gson, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testCreate_NonObjectType() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingPolicy.IDENTITY,
        new Excluder());
    assertNull(factory.create(gson, typeToken));
  }

  @Test
  public void testCreate_CustomJsonAdapterPresent() {
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter() {};
    MyClass myClass = new MyClass();
    Class<?> clazz = MyClass.class;
    Field field = clazz.getDeclaredField("fieldWithCustomJsonAdapter");
    field.setAccessible(true);
    Type fieldType = $Gson$Types.getPrimitiveType(int.class);
    List<String> fieldNames = Collections.singletonList("customName");
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingPolicy.IDENTITY,
        new Excluder());
    TypeAdapter<MyClass> typeAdapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertEquals(MyClass.class, typeAdapter.getClass());
    JsonReader reader = new JsonReader(new StringReader("{\"customName\": 42}"));
    reader.setLenient(true);
    MyClass deserialized = typeAdapter.read(reader);
    assertEquals(myClass, deserialized);
    JsonWriter writer = new JsonWriter(new StringWriter());
    typeAdapter.write(writer, myClass);
    String json = writer.toString();
    assertEquals("{\"customName\": 42}", json);
  }

  @Test
  public void testCreate_JsonAdapterAbsent() {
    MyClass myClass = new MyClass();
    Class<?> clazz = MyClass.class;
    Field field = clazz.getDeclaredField("fieldWithoutCustomJsonAdapter");
    field.setAccessible(true);
    Type fieldType = $Gson$Types.getPrimitiveType(int.class);
    List<String> fieldNames = Collections.singletonList("defaultName");
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingPolicy.IDENTITY,
        new Excluder());
    TypeAdapter<MyClass> typeAdapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertEquals(MyClass.class, typeAdapter.getClass());
    JsonReader reader = new JsonReader(new StringReader("{\"defaultName\": 42}"));
    reader.setLenient(true);
    MyClass deserialized = typeAdapter.read(reader);
    assertEquals(myClass, deserialized);
    JsonWriter writer = new JsonWriter(new StringWriter());
    typeAdapter.write(writer, myClass);
    String json = writer.toString();
    assertEquals("{\"defaultName\": 42}", json);
  }

  @Test
  public void testExcludeField() {
    MyClass myClass = new MyClass();
    Class<?> clazz = MyClass.class;
    Field excludedField = clazz.getDeclaredField("excludedField");
    excludedField.setAccessible(true);
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingPolicy.IDENTITY,
        new Excluder());
    TypeAdapter<MyClass> typeAdapter = factory.create(gson, new TypeToken<MyClass>() {});
    MyClass deserialized = typeAdapter.read(new JsonReader(new StringReader("{excludedField: 42}")));
    assertNull(deserialized);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateFieldName() {
    MyClass myClass = new MyClass();
    Class<?> clazz = MyClass.class;
    Field field1 = clazz.getDeclaredField("field1");
    field1.setAccessible(true);
    Field field2 = clazz.getDeclaredField("field1");
    field2.setAccessible(true);
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingPolicy.IDENTITY,
        new Excluder());
    TypeAdapter<MyClass> typeAdapter = factory.create(gson, new TypeToken<MyClass>() {});
    fail("Expected IllegalArgumentException");
  }

  static class MyClass {
    int fieldWithCustomJsonAdapter;
    @JsonAdapter(CustomJsonAdapter.class)
    int fieldWithCustomJsonAdapterAnnotation;
    int fieldWithoutCustomJsonAdapter;
    @JsonAdapter(CustomJsonAdapter.class)
    transient int excludedField;

    public MyClass() {
      // empty constructor
    }
  }

  static class CustomJsonAdapter extends JsonAdapter<Integer> {
    // empty adapter for testing purposes
  }
}