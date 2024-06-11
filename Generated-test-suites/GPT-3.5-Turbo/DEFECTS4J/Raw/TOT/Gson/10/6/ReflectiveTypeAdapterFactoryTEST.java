import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  // Test for excludeField method
  @Test
  public void testExcludeField() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory();
    
    // Test for excluding a field during serialization
    boolean excludeSerialize = factory.excludeField(field, true);
    assertTrue(excludeSerialize);
    
    // Test for excluding a field during deserialization
    boolean excludeDeserialize = factory.excludeField(field, false);
    assertFalse(excludeDeserialize);
  }

  // Test for getFieldNames method
  @Test
  public void testGetFieldNames() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory();
    Field field = new Field();
    
    // Test for field without SerializedName annotation
    List<String> fieldNames = factory.getFieldNames(field);
    assertEquals(1, fieldNames.size());
    assertEquals("fieldName", fieldNames.get(0));
    
    // Test for field with SerializedName annotation
    field.setAnnotation(new SerializedName("altName"));
    fieldNames = factory.getFieldNames(field);
    assertEquals(2, fieldNames.size());
    assertEquals("fieldName", fieldNames.get(0));
    assertEquals("altName", fieldNames.get(1));
  }

  // Test for create method
  @Test
  public void testCreate() {
    Gson gson = new Gson();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory();
    
    // Test for creating type adapter for primitive type
    TypeAdapter<Integer> adapter = factory.create(gson, new TypeToken<Integer>() {});
    assertNull(adapter);
    
    // Test for creating type adapter for object type
    TypeAdapter<?> adapter = factory.create(gson, new TypeToken<Object>() {});
    assertNotNull(adapter);
  }

  // Test for createBoundField method
  @Test
  public void testCreateBoundField() {
    Gson gson = new Gson();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory();
    Field field = new Field();
    TypeToken<?> fieldType = new TypeToken<String>() {};
    
    // Test for JsonAdapter annotation present on field
    field.setAnnotation(new JsonAdapter());
    ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(gson, field, "fieldName", fieldType, true, true);
    assertNotNull(boundField);
    
    // Test for JsonAdapter annotation not present on field
    ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(gson, field, "fieldName", fieldType, true, true);
    assertNotNull(boundField);
  }

  // Test for getBoundFields method
  @Test
  public void testGetBoundFields() {
    Gson gson = new Gson();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory();
    TypeToken<?> type = new TypeToken<Object>() {};
    Class<?> raw = Object.class;

    // Test for interface type
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, raw);
    assertTrue(boundFields.isEmpty());
    
    // Test for object type
    type = new TypeToken<Object>() {};
    raw = Object.class;
    boundFields = factory.getBoundFields(gson, type, raw);
    assertFalse(boundFields.isEmpty());
  }

  // Test for Adapter class
  @Test
  public void testAdapter() {
    Gson gson = new Gson();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory();
    ObjectConstructor<Object> constructor = new ObjectConstructor<Object>() {
      public Object construct() {
        return new Object();
      }
    };
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = factory.new Adapter<Object>(constructor, boundFields);
    
    // Test for reading JSON
    JsonReader reader = new JsonReader(new StringReader(jsonString));
    Object value = adapter.read(reader);
    assertNotNull(value);
    
    // Test for writing JSON
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    adapter.write(writer, value);
    assertEquals(jsonString, stringWriter.toString());
  }
}