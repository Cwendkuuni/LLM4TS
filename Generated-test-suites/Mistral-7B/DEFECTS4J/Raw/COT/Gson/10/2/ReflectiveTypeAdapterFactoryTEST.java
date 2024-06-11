import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final String CLASS_NAME = ReflectiveTypeAdapterFactory.class.getName();
  private Gson gson;
  private ReflectiveTypeAdapterFactory factory;

  @Before
  public void setup() {
    this.gson = new GsonBuilder().create();
    this.factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
        new FieldNamingStrategyOfRuntime().SET_FIELD_NAMES, Excluder.getInstance());
  }

  @Test
  public void testCreateMethod() {
    TypeAdapter<Foo> adapter = factory.create(gson, TypeToken.get(Foo.class));
    Assert.assertNotNull(adapter);
  }

  @Test
  public void testExcludeFieldMethod() {
    Class<Bar> clazz = Bar.class;
    Field field = null;
    try {
      field = clazz.getDeclaredField("excludedField");
    } catch (NoSuchFieldException e) {
      Assert.fail("Expected field 'excludedField' not found.");
    }

    boolean result = factory.excludeField(field, true);
    Assert.assertTrue(result);
  }

  @Test
  public void testExcludeFieldMethodWithFalseArgument() {
    Class<Bar> clazz = Bar.class;
    Field field = null;
    try {
      field = clazz.getDeclaredField("includedField");
    } catch (NoSuchFieldException e) {
      Assert.fail("Expected field 'includedField' not found.");
    }

    boolean result = factory.excludeField(field, true);
    Assert.assertFalse(result);
  }

  @Test
  public void testGetFieldNamesMethod() throws NoSuchFieldException {
    Class<Bar> clazz = Bar.class;
    Field field = clazz.getDeclaredField("fieldWithAnnotation");

    List<String> fieldNames = factory.getFieldNames(field);
    Assert.assertEquals(1, fieldNames.size());
    Assert.assertEquals("name1", fieldNames.get(0));
  }

  @Test
  public void testGetFieldNamesMethodWithMultipleAlternates() throws NoSuchFieldException {
    Class<Bar> clazz = Bar.class;
    Field field = clazz.getDeclaredField("fieldWithMultipleAlternates");

    List<String> fieldNames = factory.getFieldNames(field);
    Assert.assertEquals(2, fieldNames.size());
    Assert.assertEquals("name1", fieldNames.get(0));
    Assert.assertEquals("name2", fieldNames.get(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetFieldNamesMethodWithDuplicateNames() throws NoSuchFieldException {
    Class<Bar> clazz = Bar.class;
    Field field1 = clazz.getDeclaredField("field1");
    Field field2 = clazz.getDeclaredField("field1");

    factory.getFieldNames(field1);
  }

  @Test
  public void testCreateBoundFieldMethod() throws NoSuchFieldException, IllegalAccessException {
    Class<Bar> clazz = Bar.class;
    Field field = clazz.getDeclaredField("includedField");

    String name = "name";
    TypeToken<?> fieldType = TypeToken.get(int.class);
    boolean serialize = true;
    boolean deserialize = true;

    ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(
        gson, field, name, fieldType, serialize, deserialize
    );

    Assert.assertEquals(name, boundField.name);
    Assert.assertEquals(serialize, boundField.serialized);
    Assert.assertEquals(deserialize, boundField.deserialized);
  }

  @Test
  public void testCreateBoundFieldMethodWithJsonAdapter() throws NoSuchFieldException, IllegalAccessException {
    Class<Bar> clazz = Bar.class;
    Field field = clazz.getDeclaredField("fieldWithJsonAdapter");

    String name = "name";
    TypeToken<?> fieldType = TypeToken.get(String.class);
    boolean serialize = true;
    boolean deserialize = true;

    JsonAdapter jsonAdapter = new JsonAdapter() {
      @Override public String toString(JsonElement jsonElement) {
        return jsonElement.getAsJsonArray().get(0).getAsString();
      }

      @Override public JsonElement fromJson(JsonParsePosition jsonParsePosition, String json) {
        return new JsonPrimitive(json);
      }
    };

    ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(
        gson, field, name, fieldType, serialize, deserialize
    );

    TypeAdapter<?> typeAdapter = boundField.typeAdapter;
    Assert.assertEquals(JsonAdapter.class, typeAdapter.getClass());
    Assert.assertEquals(jsonAdapter, typeAdapter);
  }

  @Test
  public void testGetBoundFieldsMethod() throws NoSuchFieldException, IllegalAccessException {
    Class<Bar> clazz = Bar.class;

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory
        .getBoundFields(gson, TypeToken.get(clazz), clazz);

    Assert.assertEquals(4, boundFields.size());

    ReflectiveTypeAdapterFactory.BoundField boundField1 = boundFields.get("name1");
    Assert.assertEquals("name1", boundField1.name);
    Assert.assertEquals(true, boundField1.serialized);
    Assert.assertEquals(true, boundField1.deserialized);

    ReflectiveTypeAdapterFactory.BoundField boundField2 = boundFields.get("name2");
    Assert.assertEquals("name2", boundField2.name);
    Assert.assertEquals(true, boundField2.serialized);
    Assert.assertEquals(true, boundField2.deserialized);

    ReflectiveTypeAdapterFactory.BoundField boundField3 = boundFields.get("excludedField");
    Assert.assertNull(boundField3);

    ReflectiveTypeAdapterFactory.BoundField boundField4 = boundFields.get("includedField");
    Assert.assertEquals("includedField", boundField4.name);
    Assert.assertEquals(true, boundField4.serialized);
    Assert.assertEquals(true, boundField4.deserialized);
  }

  @Test
  public void testAdapterMethod() throws IOException, IllegalAccessException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));
    adapter.write(new JsonWriter(System.out), barInstance);
    System.out.flush();
    System.out.close();

    JsonReader jsonReader = new JsonReader(new StringReader(System.in));
    Bar bar = adapter.read(jsonReader);

    Assert.assertEquals(barInstance, bar);
  }

  @Test
  public void testAdapterMethodWithJsonSyntaxException() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    JsonReader jsonReader = new JsonReader(new StringReader("{\"excludedField\": 42}"));;

    try {
      adapter.read(jsonReader);
      Assert.fail("Expected JsonSyntaxException.");
    } catch (JsonSyntaxException e) {
      // expected
    }

    Assert.assertNotEquals(barInstance, adapter.read(jsonReader));
  }

  @Test
  public void testAdapterMethodWithIllegalAccessException() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    JsonReader jsonReader = new JsonReader(new StringReader("{\"name1\": 42,\"name2\": 43,\"excludedField\": 44}"));

    try {
      adapter.read(jsonReader);
      Assert.fail("Expected IllegalAccessException.");
    } catch (IllegalAccessException e) {
      // expected
    }

    Assert.assertNotEquals(barInstance, adapter.read(jsonReader));
  }

  @Test
  public void testAdapterMethodWithNullValue() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    JsonReader jsonReader = new JsonReader(new StringReader("{\"name1\": null}"));

    Bar nullBar = adapter.read(jsonReader);
    Assert.assertNull(nullBar);
  }

  @Test
  public void testAdapterMethodWithEmptyJsonObject() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    JsonReader jsonReader = new JsonReader(new StringReader("{}"));

    Bar emptyBar = adapter.read(jsonReader);
    Assert.assertEquals(barInstance, emptyBar);
  }

  @Test
  public void testAdapterMethodWithJsonObjectWithOneField() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    String json = "{ \"name1\": 1, \"name2\": 2 }";

    Bar bar = adapter.read(new JsonParser().parse(new StringReader(json)).getAsJsonObject());

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }

  @Test
  public void testAdapterMethodWithJsonObjectWithMultipleFields() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    String json = "{ \"name1\": 1, \"name2\": 2, \"name3\": 3, \"name4\": 4 }";

    Bar bar = adapter.read(new JsonParser().parse(new StringReader(json)).getAsJsonObject());

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }

  @Test
  public void testAdapterMethodWithJsonArray() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    String json = "[ 1, 2, 3, 4 ]";

    JsonElement jsonElement = new JsonParser().parse(new StringReader(json)).getAsJsonArray();

    Bar bar = adapter.read(jsonElement);

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }

  @Test
  public void testAdapterMethodWithJsonArrayWithSizeZero() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    String json = "[ ]";

    Bar bar = adapter.read(new JsonParser().parse(new StringReader(json)).getAsJsonArray());

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }

  @Test
  public void testAdapterMethodWithJsonArrayWithSizeOne() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    JsonElement jsonElement = new JsonParser().parse(new StringReader("[\n  1\n]")).getAsJsonArray();

    Bar bar = adapter.read(jsonElement);

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }

  @Test
  public void testAdapterMethodWithJsonArrayWithSizeMoreThanOne() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    String json = "[ 1, 2, 3, 4 ]";

    Bar bar = adapter.read(new JsonParser().parse(new StringReader(json)).getAsJsonArray());

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }

  private static class Bar {
    private int name1;
    private int name2;
    private int name3;
    private int name4;
    private int excludedField;

    private Bar() {}

    @JsonAdapter(BarJsonAdapter.class)
    private int fieldWithJsonAdapter = 0;
  }

  private static class BarJsonAdapter extends TypeAdapter<Bar> {
    @Override public Bar read(JsonReader in) throws IOException {
      return new Bar();
    }

    @Override public void write(JsonWriter out, Bar value) throws IOException {
      out.value(value.fieldWithJsonAdapter);
    }
  }

  @Test
  public void testAdapterMethodWithJsonAdapterClass() throws IOException, InterruptedException {
    Class<Bar> clazz = Bar.class;
    Object barInstance = factory.constructorConstructor.get(clazz).construct();

    TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(clazz));

    String json = "{\"fieldWithJsonAdapter\": [1, 2, 3, 4]}";

    Bar bar = adapter.read(new JsonParser().parse(new StringReader(json)).getAsJsonObject());

    Assert.assertEquals(barInstance.hashCode(), bar.hashCode());
  }
}