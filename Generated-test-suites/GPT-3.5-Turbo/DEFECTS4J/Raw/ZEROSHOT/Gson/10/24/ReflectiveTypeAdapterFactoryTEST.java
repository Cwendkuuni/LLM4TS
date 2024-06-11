import org.junit.Assert;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testExcludeField() {
    // Given
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
    Field f = new Field();

    // When
    boolean result = reflectiveTypeAdapterFactory.excludeField(f, true);

    // Then
    Assert.assertFalse(result);
  }

  @Test
  public void testCreate() {
    // Given
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
    Gson gson = new Gson();
    TypeToken<ReflectiveTypeAdapterFactory> type = new TypeToken<ReflectiveTypeAdapterFactory>() {};

    // When
    TypeAdapter<ReflectiveTypeAdapterFactory> result = reflectiveTypeAdapterFactory.create(gson, type);

    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void testCreateBoundField() {
    // Given
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
    Gson context = new Gson();
    Field field = new Field();
    String name = "Test";
    TypeToken<?> fieldType = new TypeToken<ReflectiveTypeAdapterFactory>() {};
    boolean serialize = true;
    boolean deserialize = false;

    // When
    ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, name, fieldType, serialize, deserialize);

    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void testGetBoundFields() {
    // Given
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
    Gson context = new Gson();
    TypeToken<?> type = new TypeToken<ReflectiveTypeAdapterFactory>() {};
    Class<?> raw = ReflectiveTypeAdapterFactory.class;

    // When
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);

    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void testCreateAdapter() {
    // Given
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
    ObjectConstructor<ReflectiveTypeAdapterFactory> constructor = new ObjectConstructor<ReflectiveTypeAdapterFactory>() {
      @Override
      public ReflectiveTypeAdapterFactory construct() {
        return new ReflectiveTypeAdapterFactory(null, null, null);
      }
    };
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();

    // When
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> result = reflectiveTypeAdapterFactory.createAdapter(constructor, boundFields);

    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void testRead() {
    // Given
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory>(null, null);
    JsonReader in = new JsonReader();

    // When
    ReflectiveTypeAdapterFactory result = adapter.read(in);

    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void testWrite() {
    // Given
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory>(null, null);
    JsonWriter out = new JsonWriter();
    ReflectiveTypeAdapterFactory value = new ReflectiveTypeAdapterFactory(null, null, null);

    // When
    adapter.write(out, value);

    // Then
    // No assertion, just checking for any exceptions
  }

}