import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReflectiveTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);

    gson = new GsonBuilder()
        .setExcluder(excluder)
        .setFieldNamingStrategy(fieldNamingPolicy)
        .setConstructorConstructor(constructorConstructor)
        .create();

    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  @Test
  public void testCreate_nullTypeToken_returnsNull() {
    assertNull(reflectiveTypeAdapterFactory.create(gson, null));
  }

  @Test
  public void testCreate_notObjectType_returnsNull() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    assertNull(reflectiveTypeAdapterFactory.create(gson, typeToken));
  }

  @Test
  public void testCreate_reflectiveTypeAdapterFactory_returnsInstanceOfAdapter() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, typeToken);
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeField_fieldAnnotatedWithJsonAdapter_excludesField() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Field field = TestClass.class.getDeclaredField("fieldAnnotatedWithJsonAdapter");
    assertFalse(reflectiveTypeAdapterFactory.excludeField(field, true));

    field.setAccessible(true);
    field.set(new TestClass(), new TestJsonAdapter());

    assertTrue(reflectiveTypeAdapterFactory.excludeField(field, true));
  }

  @Test
  public void testExcludeField_fieldNotAnnotatedWithJsonAdapter_includesField() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Field field = TestClass.class.getDeclaredField("fieldNotAnnotatedWithJsonAdapter");

    assertTrue(reflectiveTypeAdapterFactory.excludeField(field, true));
  }

  @Test
  public void testExcludeField_excludesFieldWhenSerializeIsFalse() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Field field = TestClass.class.getDeclaredField("fieldToExcludeWhenSerializeIsFalse");

    assertTrue(reflectiveTypeAdapterFactory.excludeField(field, true));
    assertFalse(reflectiveTypeAdapterFactory.excludeField(field, false));
  }

  @Test
  public void testGetBoundFields_emptyClass_returnsEmptyMap() {
    TypeToken<Object> typeToken = new TypeToken<Object>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, Object.class);

    assertEquals(0, boundFields.size());
  }

  @Test
  public void testGetBoundFields_interface_returnsEmptyMap() {
    TypeToken<TestInterface> typeToken = new TypeToken<TestInterface>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestInterface.class);

    assertEquals(0, boundFields.size());
  }

  @Test
  public void testGetBoundFields_simpleClass_returnsMapWithBoundFields() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestClass.class);

    assertEquals(3, boundFields.size());

    for (ReflectiveTypeAdapterFactory.BoundField boundField : boundFields.values()) {
      assertEquals("name1", boundField.name);
      assertEquals(true, boundField.serialized);
      assertEquals(true, boundField.deserialized);
    }
  }

  @Test
  public void testWriteRead_primitiveField_writesAndReadsCorrectly() throws IOException {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestClass.class);

    byte[] json = createJsonForTestClass(new TestClass(1, "name1", 'c'));

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(json));
    JsonWriter jsonWriter = new JsonWriter(new ByteArrayOutputStream());

    Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, typeToken);

    TestClass testClass = adapter.read(jsonReader);
    adapter.write(jsonWriter, testClass);

    byte[] json2 = jsonWriter.toString().getBytes();

    Assert.assertArrayEquals(json, json2);
  }

  @Test
  public void testWriteRead_objectFieldWithJsonAdapter_writesAndReadsCorrectly() throws IOException {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestClass.class);

    TestJsonAdapter testJsonAdapter = new TestJsonAdapter();
    TypeToken<TestObject> testObjectTypeToken = new TypeToken<TestObject>() {};

    Field field = TestClass.class.getDeclaredField("fieldAnnotatedWithJsonAdapter");
    field.setAccessible(true);

    TestClass testClass = new TestClass(1, "name1", 'c');
    field.set(testClass, testJsonAdapter);

    byte[] json = createJsonForTestClass(testClass);

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(json));
    JsonWriter jsonWriter = new JsonWriter(new ByteArrayOutputStream());

    Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, typeToken);

    TestClass readTestClass = adapter.read(jsonReader);
    adapter.write(jsonWriter, readTestClass);

    byte[] json2 = jsonWriter.toString().getBytes();

    TestObject testObject = testJsonAdapter.read(new JsonReader(new ByteArrayInputStream(json)));
    TestObject testObject2 = testJsonAdapter.read(new JsonReader(new ByteArrayInputStream(json2)));

    Assert.assertEquals(testObject, testObject2);
  }

  @Test
  public void testWriteRead_objectFieldWithoutJsonAdapter_writesAndReadsCorrectly() throws IOException {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestClass.class);

    byte[] json = createJsonForTestClass(new TestClass(1, "name1", new TestObject()));

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(json));
    JsonWriter jsonWriter = new JsonWriter(new ByteArrayOutputStream());

    Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, typeToken);

    TestClass testClass = adapter.read(jsonReader);
    adapter.write(jsonWriter, testClass);

    byte[] json2 = jsonWriter.toString().getBytes();

    TestObject testObject = testClass.getObjectField();
    TestObject testObject2 = testClass.getObjectField();

    Assert.assertEquals(testObject, testObject2);
  }

  @Test
  public void testWriteField_writesCorrectly() throws IllegalAccessException, IOException {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestClass.class);

    TestClass testClass = new TestClass(1, "name1", 'c');

    Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, typeToken);

    Field field = TestClass.class.getDeclaredField("name2");
    field.setAccessible(true);
    field.set(testClass, "test");

    boolean written = adapter.writeField(testClass);

    assertTrue(written);
  }

  @Test
  public void testWriteField_doesNotWriteWhenSerializedIsFalse() throws IllegalAccessException {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestClass.class);

    TestClass testClass = new TestClass(1, "name1", 'c');

    Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, typeToken);

    Field field = TestClass.class.getDeclaredField("fieldToExcludeWhenSerializeIsFalse");
    field.setAccessible(true);
    field.set(testClass, "test");

    boolean written = adapter.writeField(testClass);

    assertFalse(written);
  }

  private byte[] createJsonForTestClass(Object testClass) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter jsonWriter = new JsonWriter(outputStream);

    ((Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<TestClass>() {})).write(jsonWriter, testClass);
    jsonWriter.flush();

    return outputStream.toByteArray();
  }

  private static class TestClass {
    int intField;
    @SerializedName("name1")
    String name1;
    @SerializedName("name2")
    String name2;
    char charField;
    @JsonAdapter(TestJsonAdapter.class)
    TestObject objectField;

    boolean fieldToExcludeWhenSerializeIsFalse = false;

    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof TestClass)) return false;

      TestClass other = (TestClass) o;
      return intField == other.intField &&
             name1.equals(other.name1) &&
             name2.equals(other.name2) &&
             charField == other.charField &&
             objectField.equals(other.objectField);
    }

    @Override
    public String toString() {
      return "TestClass{" +
             "intField=" + intField +
             ", name1='" + name1 + '\'' +
             ", name2='" + name2 + '\'' +
             ", charField=" + charField +
             ", objectField=" + objectField +
             '}';
    }

    public TestClass(int intField, String name1, String name2) {
      this.intField = intField;
      this.name1 = name1;
      this.name2 = name2;
      this.charField = 'c';
      this.objectField = new TestObject();
    }

    public TestObject getObjectField() {
      return objectField;
    }

    public static class TestJsonAdapter extends TypeAdapter<TestObject> {
      @Override public TestObject read(JsonReader in) throws IOException {
        return new TestObject();
      }

      @Override public void write(JsonWriter out, TestObject value) throws IOException {
      }
    }
  }

  private static class TestInterface {}

  private static class TestObject {}
}