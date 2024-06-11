import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

  @Before
  public void setUp() {
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new MyFieldNamingStrategy(), new Excluder());
  }

  @Test
  public void testExcludeField_true() throws Exception {
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
    assertTrue(result);
  }

  @Test
  public void testExcludeField_false() throws Exception {
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("fieldNamingPolicy");
    boolean result = reflectiveTypeAdapterFactory.excludeField(field, false);
    assertFalse(result);
  }

  @Test
  public void testCreate_null() throws Exception {
    Gson gson = new Gson();
    TypeToken<String> type = new TypeToken<String>() {};
    TypeAdapter<String> result = reflectiveTypeAdapterFactory.create(gson, type);
    assertNull(result);
  }

  @Test
  public void testCreate_notNull() throws Exception {
    Gson gson = new Gson();
    TypeToken<ReflectiveTypeAdapterFactory> type = new TypeToken<ReflectiveTypeAdapterFactory>() {};
    TypeAdapter<ReflectiveTypeAdapterFactory> result = reflectiveTypeAdapterFactory.create(gson, type);
    assertNotNull(result);
  }

  @Test
  public void testCreate_primitive() throws Exception {
    Gson gson = new Gson();
    TypeToken<Integer> type = new TypeToken<Integer>() {};
    TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, type);
    assertNull(result);
  }

  @Test
  public void testCreate_boundFields() throws Exception {
    Gson gson = new Gson();
    TypeToken<ReflectiveTypeAdapterFactory> type = new TypeToken<ReflectiveTypeAdapterFactory>() {};
    TypeAdapter<ReflectiveTypeAdapterFactory> adapter = reflectiveTypeAdapterFactory.create(gson, type);
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> reflectiveAdapter = (ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory>)adapter;
    assertNotNull(reflectiveAdapter.boundFields);
    assertEquals(51, reflectiveAdapter.boundFields.size());
  }

  @Test
  public void testCreate_read() throws Exception {
    Gson gson = new Gson();
    TypeToken<ReflectiveTypeAdapterFactory> type = new TypeToken<ReflectiveTypeAdapterFactory>() {};
    TypeAdapter<ReflectiveTypeAdapterFactory> adapter = reflectiveTypeAdapterFactory.create(gson, type);
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> reflectiveAdapter = (ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory>)adapter;
    JsonReader reader = new JsonReader(new StringReader("{\"name\":\"Test\"}"));
    ReflectiveTypeAdapterFactory result = reflectiveAdapter.read(reader);
    assertNotNull(result);
    assertEquals("Test", result.getName());
  }

  @Test
  public void testCreate_write() throws Exception {
    Gson gson = new Gson();
    TypeToken<ReflectiveTypeAdapterFactory> type = new TypeToken<ReflectiveTypeAdapterFactory>() {};
    TypeAdapter<ReflectiveTypeAdapterFactory> adapter = reflectiveTypeAdapterFactory.create(gson, type);
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> reflectiveAdapter = (ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory>)adapter;
    ReflectiveTypeAdapterFactory object = new ReflectiveTypeAdapterFactory();
    object.setName("Test");
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    reflectiveAdapter.write(jsonWriter, object);
    jsonWriter.close();
    String result = writer.toString();
    assertEquals("{\"name\":\"Test\"}", result);
  }

  private static class MyFieldNamingStrategy implements FieldNamingStrategy {

    @Override
    public String translateName(Field field) {
      return field.getName();
    }
  }
}