
import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testExcludeField() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Field field = null;
    boolean actual = factory.excludeField(field, true);
    assertFalse(actual);
  }

  @Test
  public void testExcludeFieldWithType() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Field field = null;
    boolean actual = factory.excludeField(field, true, new Excluder());
    assertFalse(actual);
  }

  @Test
  public void testGetFieldNames() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Field field = null;
    List<String> actual = factory.getFieldNames(field);
    assertNotNull(actual);
  }

  @Test
  public void testCreate() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Gson gson = new Gson();
    TypeToken<T> type = null;
    TypeAdapter<T> actual = factory.create(gson, type);
    assertNull(actual);
  }

  @Test
  public void testCreateAdapter() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Gson gson = new Gson();
    ConstructorConstructor constructor = new ConstructorConstructor();
    TypeToken<?> fieldType = null;
    JsonAdapter annotation = null;
    TypeAdapter<?> actual = factory.createAdapter(constructor, gson, fieldType, annotation);
    assertNull(actual);
  }

  @Test
  public void testCreateBoundField() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Gson context = new Gson();
    Field field = null;
    String name = null;
    TypeToken<?> fieldType = null;
    boolean serialize = false;
    boolean deserialize = false;
    ReflectiveTypeAdapterFactory.BoundField actual = factory.createBoundField(context, field, name, fieldType, serialize, deserialize);
    assertNull(actual);
  }

  @Test
  public void testGetBoundFields() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());
    Gson context = new Gson();
    TypeToken<?> type = null;
    Class<?> raw = null;
    Map<String, BoundField> actual = factory.getBoundFields(context, type, raw);
    assertNotNull(actual);
  }

  @Test
  public void testBoundFieldWrite() {
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField(null, false, false) {
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
    Object value = null;
    boolean actual = boundField.writeField(value);
    assertFalse(actual);
  }

  @Test
  public void testBoundFieldWriteWithSerialized() {
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField(null, true, false) {
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
    Object value = null;
    boolean actual = boundField.writeField(value);
    assertFalse(actual);
  }

  @Test
  public void testAdapterRead() {
    ReflectiveTypeAdapterFactory.Adapter<T> adapter = new ReflectiveTypeAdapterFactory.Adapter<T>(null, null);
    JsonReader in = null;
    T actual = adapter.read(in);
    assertNull(actual);
  }

  @Test
  public void testAdapterWrite() {
    ReflectiveTypeAdapterFactory.Adapter<T> adapter = new ReflectiveTypeAdapterFactory.Adapter<T>(null, null);
    JsonWriter out = null;
    T value = null;
    adapter.write(out, value);
  }

}
