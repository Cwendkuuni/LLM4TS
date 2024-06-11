import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {
  private Gson gson;
  private ReflectiveTypeAdapterFactory factory;

  @Before
  public void setup() {
    gson = new GsonBuilder().create();
    factory = new ReflectiveTypeAdapterFactory(gson.getConstructorConstructor(),
        gson.getFieldNamingPolicy(), gson.getExcluder());
  }

  @Test
  public void testCreate_ObjectType_ReturnsTypeAdapter() {
    TypeToken<Foo> type = new TypeToken<Foo>() {};
    TypeAdapter<Foo> adapter = factory.create(gson, type);
    assertNotNull(adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_NonObjectType_ReturnsNull() {
    int type = 1;
    TypeAdapter<?> adapter = factory.create(gson, new TypeToken<Object>() {}.where(new TypeToken<Integer>() {}
        .getType(), new TypeToken<Object>() {}, new TypeToken<Object>() {}, type));
    assertNull(adapter);
  }

  @Test
  public void testExcludeField_ExcludesFieldFromSerialization() {
    Class<?> clazz = Foo.class;
    Field excludedField = clazz.getDeclaredField("excludedField");
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(excludedField, true));
  }

  @Test
  public void testExcludeField_DoesNotExcludeFieldFromDeserialization() {
    Class<?> clazz = Foo.class;
    Field includedField = clazz.getDeclaredField("includedField");
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(includedField, false));
  }

  @Test
  public void testGetFieldNames_FieldWithDefaultName() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("field");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(1, fieldNames.size());
    assertEquals("field", fieldNames.get(0));
  }

  @Test
  public void testGetFieldNames_FieldWithCustomName() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("customField");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(1, fieldNames.size());
    assertEquals("customField", fieldNames.get(0));
  }

  @Test
  public void testGetFieldNames_FieldWithMultipleNames() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("alternateField");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(2, fieldNames.size());
    assertTrue(fieldNames.contains("alternateField"));
    assertTrue(fieldNames.contains("otherName"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateBoundField_NullContext() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("field");
    String name = "name";
    Type fieldType = new TypeToken<String>() {};
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField(name, true, false) {
      @Override void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
        fail();
      }
      @Override void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        fail();
      }
    };
    try {
      ReflectiveTypeAdapterFactory.createBoundField(null, field, name, new TypeToken<Object>() {}, true, false);
      fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateBoundField_NullType() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("field");
    String name = "name";
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField(name, true, false) {
      @Override void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
        fail();
      }
      @Override void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        fail();
      }
    };
    try {
      ReflectiveTypeAdapterFactory.createBoundField(gson, field, name, null, true, false);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testCreateBoundField_PrimitiveType() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("intField");
    String name = "name";
    Type fieldType = TypeToken.get(int.class).getType();
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField(name, true, false) {
      @Override void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
        fail();
      }
      @Override void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        fail();
      }
    };
    ReflectiveTypeAdapterFactory.BoundField boundFieldObj = ReflectiveTypeAdapterFactory.createBoundField(gson, field, name, new TypeToken<Object>() {}, true, false);
    assertInstanceOf(ReflectiveTypeAdapterFactory.BoundField.class, boundFieldObj);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateBoundField_NoJsonAdapterAnnotation() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("intField");
    String name = "name";
    Type fieldType = TypeToken.get(int.class).getType();
    try {
      ReflectiveTypeAdapterFactory.createBoundField(gson, field, name, new TypeToken<Object>() {}, true, false);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testCreateBoundField_JsonAdapterAnnotation() {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("jsonAdaptedField");
    String name = "name";
    Type fieldType = TypeToken.get(String.class).getType();
    ReflectiveTypeAdapterFactory.createBoundField(gson, field, name, new TypeToken<Object>() {}, true, false);
  }

  @Test
  public void testGetBoundFields_EmptyInterface() {
    Class<?> clazz = FooInterface.class;
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<Object>() {}, clazz);
    assertEquals(0, boundFields.size());
  }

  @Test
  public void testGetBoundFields_NonInterface() {
    Class<?> clazz = Foo.class;
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<Object>() {}, clazz);
    assertEquals(5, boundFields.size());
  }

  @Test
  public void testAdapter_WritesValueToJson() throws IOException {
    String json = "{\"field\":1,\"customField\":\"customValue\",\"includedField\":\"includedValue\",\"excludedField\":null,\"intField\":42}";
    Foo foo = new Foo("value", "customValue", "includedValue", null, 42);
    TypeAdapter<Foo> adapter = factory.create(gson, new TypeToken<Foo>() {});
    assertEquals(json, adapter.write(new JsonWriter(System.out), foo).toString());
  }

  @Test
  public void testAdapter_ReadsValueFromJson() throws IOException {
    String json = "{\"field\":\"value\",\"customField\":\"customValue\",\"includedField\":\"includedValue\",\"excludedField\":null,\"intField\":42}";
    TypeAdapter<Foo> adapter = factory.create(gson, new TypeToken<Foo>() {});
    Foo foo = adapter.read(new JsonReader(new StringReader(json)));
    assertEquals("value", foo.value);
    assertEquals("customValue", foo.customField);
    assertEquals("includedValue", foo.includedField);
    assertNull(foo.excludedField);
    assertEquals(42, foo.intField);
  }

  @Test
  public void testAdapter_HandlesNullValues() throws IOException {
    String json = "{\"field\":null,\"intField\":null}";
    Foo foo = new Foo("value", null, null, null, null);
    TypeAdapter<Foo> adapter = factory.create(gson, new TypeToken<Foo>() {});
    Foo readFoo = adapter.read(new JsonReader(new StringReader(json)));
    assertEquals(null, readFoo.value);
    assertEquals(null, readFoo.customField);
    assertEquals(null, readFoo.includedField);
    assertNull(readFoo.excludedField);
    assertEquals(null, readFoo.intField);
  }

  @Test
  public void testAdapter_HandlesPrimitiveTypeValues() throws IOException {
    String json = "{\"intField\":42}";
    Foo foo = new Foo("value", null, null, null, 42);
    TypeAdapter<Foo> adapter = factory.create(gson, new TypeToken<Foo>() {});
    Foo readFoo = adapter.read(new JsonReader(new StringReader(json)));
    assertEquals(42, readFoo.intField);
  }

  @Test
  public void testAdapter_HandlesNullObject() throws IOException {
    String json = "{}";
    TypeAdapter<Foo> adapter = factory.create(gson, new TypeToken<Foo>() {});
    assertNull(adapter.read(new JsonReader(new StringReader(json))));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testAdapter_InvalidJson() throws IOException {
    String json = "{\"field\":\"value\",\"invalidField\":{}}";
    TypeAdapter<Foo> adapter = factory.create(gson, new TypeToken<Foo>() {});
    adapter.read(new JsonReader(new StringReader(json)));
    fail();
  }
}

class Foo {
  @SerializedName("value")
  String value;
  @SerializedName("customField")
  String customField;
  @SerializedName("includedField")
  String includedField;
  @SerializedName("excludedField")
  transient String excludedField;
  int intField;

  public Foo(String value, String customField, String includedField, String excludedField, int intField) {
    this.value = value;
    this.customField = customField;
    this.includedField = includedField;
    this.excludedField = excludedField;
    this.intField = intField;
  }
}

interface FooInterface {
}