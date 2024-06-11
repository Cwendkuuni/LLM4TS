import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
  @Test
  public void testExcludeField() {
    Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
    boolean serialize = true;
    boolean expected = false;
    boolean actual = ReflectiveTypeAdapterFactory.excludeField(field, serialize);
    assertEquals(expected, actual);
    serialize = false;
    expected = false;
    actual = ReflectiveTypeAdapterFactory.excludeField(field, serialize);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetFieldNames() {
    Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
    List<String> expected = List.of("EMPTY_STRING_ARRAY");
    List<String> actual = new ReflectiveTypeAdapterFactory().getFieldNames(field);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeToken<List<String>> type = new TypeToken<List<String>>(){};
    TypeAdapter<List<String>> expected = null;
    TypeAdapter<List<String>> actual = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingStrategy.UPPER_CAMEL_CASE,
        new Excluder()
    ).create(gson, type);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateBoundField() throws NoSuchFieldException {
    Gson gson = new Gson();
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    String name = "constructorConstructor";
    TypeToken<?> fieldType = TypeToken.get(ConstructorConstructor.class);
    boolean serialize = false;
    boolean deserialize = true;
    ReflectiveTypeAdapterFactory.BoundField expected =
        new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {
          @Override
          boolean writeField(Object value) throws IOException, IllegalAccessException {
            return false;
          }
          @Override
          void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
          }
          @Override
          void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
          }
        };
    ReflectiveTypeAdapterFactory.BoundField actual = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingStrategy.UPPER_CAMEL_CASE,
        new Excluder()
    ).createBoundField(gson, field, name, fieldType, serialize, deserialize);
    assertEquals(expected.name, actual.name);
    assertEquals(expected.serialized, actual.serialized);
    assertEquals(expected.deserialized, actual.deserialized);
  }

  @Test
  public void testGetBoundFields() {
    Gson gson = new Gson();
    TypeToken<?> type = TypeToken.get(ReflectiveTypeAdapterFactory.class);
    Class<?> raw = ReflectiveTypeAdapterFactory.class;
    Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = null;
    Map<String, ReflectiveTypeAdapterFactory.BoundField> actual = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingStrategy.UPPER_CAMEL_CASE,
        new Excluder()
    ).getBoundFields(gson, type, raw);
    assertEquals(expected, actual);
  }

  @Test
  public void testBoundFieldWrite() throws NoSuchFieldException, IOException, IllegalAccessException {
    Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
    String name = "EMPTY_STRING_ARRAY";
    boolean serialized = false;
    boolean deserialized = true;
    ReflectiveTypeAdapterFactory.BoundField boundField =
        new ReflectiveTypeAdapterFactory.BoundField(name, serialized, deserialized) {
          @Override
          boolean writeField(Object value) throws IOException, IllegalAccessException {
            return false;
          }
          @Override
          void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
          }
          @Override
          void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
          }
        };
    Object value = new Object();
    boolean expected = false;
    boolean actual = boundField.writeField(value);
    assertEquals(expected, actual);
  }

  @Test
  public void testAdapterRead() throws IOException {
    JsonReader in = null;
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter =
        new ReflectiveTypeAdapterFactory.Adapter<>(new ObjectConstructor<Object>() {
          @Override
          public Object construct() {
            return null;
          }
        }, null);
    Object expected = null;
    Object actual = adapter.read(in);
    assertEquals(expected, actual);
  }

  @Test
  public void testAdapterWrite() throws IOException {
    JsonWriter out = null;
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter =
        new ReflectiveTypeAdapterFactory.Adapter<>(new ObjectConstructor<Object>() {
          @Override
          public Object construct() {
            return null;
          }
        }, null);
    Object value = null;
    adapter.write(out, value);
  }
}