import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

  @Before
  public void setup() {
    gson = new Gson();
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void teardown() {
    gson = null;
    constructorConstructor = null;
    fieldNamingPolicy = null;
    excluder = null;
    reflectiveTypeAdapterFactory = null;
  }

  @Test
  public void testCreate_GivenNullTypeToken_ReturnsNull() {
    assertNull(reflectiveTypeAdapterFactory.create(gson, null));
  }

  @Test
  public void testCreate_GivenNonObjectType_ReturnsNull() {
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    assertNull(reflectiveTypeAdapterFactory.create(gson, intTypeToken));
  }

  @Test
  public void testGetBoundFields_GivenNullGson_ThrowsNullPointerException() {
    try {
      reflectiveTypeAdapterFactory.getBoundFields(null, new TypeToken<Object>() {}, Object.class);
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBoundFields_GivenInterfaceType_ReturnsEmptyMap() {
    TypeToken<MyInterface> typeToken = new TypeToken<MyInterface>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, MyInterface.class);
    assertEmptyMap(boundFields);
  }

  @Test
  public void testGetBoundFields_GivenClassWithFields_ReturnsMapWithBoundFields() {
    MyClass myClass = new MyClass();
    TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};

    when(constructorConstructor.get(typeToken)).thenReturn(MyClassConstructor.INSTANCE);
    when(fieldNamingPolicy.translateName(myClass.getDeclaredField1())).thenReturn("field1");
    when(fieldNamingPolicy.translateName(myClass.getDeclaredField2())).thenReturn("field2");

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, MyClass.class);

    assertSize(2, boundFields);
    assertFieldBound(boundFields.get("field1"), myClass.getDeclaredField1(), "field1");
    assertFieldBound(boundFields.get("field2"), myClass.getDeclaredField2(), "field2");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_GivenNullConstructorConstructor_ThrowsNullPointerException() {
    try {
      new ReflectiveTypeAdapterFactory(null, fieldNamingPolicy, excluder);
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void testCreate_GivenNullFieldNamingPolicy_ThrowsNullPointerException() {
    try {
      new ReflectiveTypeAdapterFactory(constructorConstructor, null, excluder);
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void testCreate_GivenNullExcluder_ThrowsNullPointerException() {
    try {
      new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, null);
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testAdapter_GivenJsonReaderAndObject_WritesFieldsToJsonWriter() throws IOException {
    MyClass myClass = new MyClass();
    MyClass expectedJson = new MyClass(1, "foo");

    when(constructorConstructor.get(new TypeToken<MyClass>() {})).thenReturn(MyClassConstructor.INSTANCE);

    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, myClass);
    jsonWriter.flush();

    String jsonString = jsonWriter.toString();
    JsonElement jsonElement = JsonParser.parseString(jsonString);

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("field1", jsonObject.name("field1").getAsJsonPrimitive().getName());
    assertEquals(1, jsonObject.get("field1").getAsInt());

    assertEquals("field2", jsonObject.name("field2").getAsJsonPrimitive().getName());
    assertEquals("foo", jsonObject.get("field2").getAsString());

    assertEquals(JsonToken.END_OBJECT, jsonElement.getAsJsonObject().get(JsonToken.NULL).getAsJsonObject()
        .isJsonNull());
  }

  @Test
  public void testAdapter_GivenJsonReaderAndNullObject_WritesNullToJsonWriter() throws IOException {
    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, null);
    jsonWriter.flush();

    String jsonString = jsonWriter.toString();
    JsonElement jsonElement = JsonParser.parseString(jsonString);

    JsonNull jsonNull = jsonElement.getAsJsonNull();

    assertEquals(JsonToken.NULL, jsonNull.isJsonNull());
  }

  @Test
  public void testAdapter_GivenJsonElementAndObject_ReadsFieldsFromJsonReader() throws IOException {
    MyClass myClass = new MyClass(1, "foo");
    JsonElement jsonElement = new JsonObject()
        .add("field1", new JsonPrimitive(1))
        .add("field2", new JsonPrimitive("foo"))
        .getAsJsonElement();

    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

    MyClass readObject = adapter.read(new JsonReader(new StringReader(jsonElement.toString()))).getValue();

    assertEquals(myClass.getDeclaredField1().get(myClass), readObject.getDeclaredField1().get(readObject));
    assertEquals(myClass.getDeclaredField2().get(myClass), readObject.getDeclaredField2().get(readObject));
  }

  @Test(expected = JsonIOException.class)
  public void testAdapter_GivenJsonElementAndNullObject_ThrowsJsonIOException() throws IOException {
    TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

    JsonReader jsonReader = new JsonReader(new StringReader("{}"));
    jsonReader.setLenient(true);

    try {
      adapter.read(jsonReader).getValue();
    } catch (IllegalStateException e) {
      throw new JsonIOException(e);
    }
  }

  private static void assertEmptyMap(Map<?, ?> map) {
    assertEquals(0, map.size());
  }

  private static void assertSize(int expectedSize, Map<?, ?> map) {
    assertEquals(expectedSize, map.size());
  }

  private static void assertFieldBound(ReflectiveTypeAdapterFactory.BoundField boundField,
      Field field, String name) {
    assertEquals(name, boundField.name);
    assertEquals(true, boundField.serialized);
    assertEquals(true, boundField.deserialized);
  }

  private static class MyClass {
    private int field1;
    private String field2;

    public MyClass() {
      this(0, null);
    }

    public MyClass(int field1, String field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    @SerializedName("field1")
    public Field getDeclaredField1() {
      try {
        return this.getClass().getDeclaredField("field1");
      } catch (NoSuchFieldException e) {
        throw new AssertionError(e);
      }
    }

    @SerializedName("field2")
    public Field getDeclaredField2() {
      try {
        return this.getClass().getDeclaredField("field2");
      } catch (NoSuchFieldException e) {
        throw new AssertionError(e);
      }
    }
  }

  private static class MyClassConstructor {
    public static MyClass INSTANCE = new MyClass();

    private MyClassConstructor() {
      // private constructor
    }

    public MyClass construct() {
      return new MyClass();
    }
  }

  private static class MyInterface {}
}