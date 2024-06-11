import com.google.gson.Gson;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectiveTypeAdapterFactoryTest {
  
  private ReflectiveTypeAdapterFactory factory;
  
  @BeforeEach
  public void setup() {
      factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), FieldNamingPolicy.IDENTITY, new Excluder());
  }
  
  @Test
  public void testExcludeField() {
      Field field = getField("field1");
      assertTrue(factory.excludeField(field, true));
      assertFalse(factory.excludeField(field, false));
  }
  
  @Test
  public void testGetFieldNames() {
      Field field = getField("field2");
      List<String> fieldNames = factory.getFieldNames(field);
      assertEquals(2, fieldNames.size());
      assertEquals("field2", fieldNames.get(0));
      assertEquals("alternateField2", fieldNames.get(1));
  }
  
  @Test
  public void testCreate() {
      Gson gson = new GsonBuilder().create();
      TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
      TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
      assertNotNull(adapter);
  }
  
  @Test
  public void testCreateWithPrimitiveType() {
      Gson gson = new GsonBuilder().create();
      TypeToken<int[]> typeToken = TypeToken.get(int[].class);
      TypeAdapter<int[]> adapter = factory.create(gson, typeToken);
      assertNull(adapter);
  }
  
  @Test
  public void testCreateBoundField() throws NoSuchFieldException {
      Gson gson = new GsonBuilder().create();
      Field field = getField("field3");
      ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(gson, field, "field3", TypeToken.get(field.getGenericType()), true, true);
      
      assertNotNull(boundField);
      assertEquals("field3", boundField.name);
      assertTrue(boundField.serialized);
      assertTrue(boundField.deserialized);
  }
  
  @Test
  public void testGetBoundFields() {
      Gson gson = new GsonBuilder().create();
      TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
      Class<?> raw = typeToken.getRawType();
      Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, typeToken, raw);
      
      assertEquals(3, boundFields.size());
      assertTrue(boundFields.containsKey("field1"));
      assertTrue(boundFields.containsKey("field2"));
      assertTrue(boundFields.containsKey("field3"));
  }
  
  @Test
  public void testAdapterReadNull() throws Exception {
      Gson gson = new GsonBuilder().create();
      ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter = createAdapter(gson);
      TestClass result = adapter.read(createJsonReader("null"));
      assertNull(result);
  }
  
  @Test
  public void testAdapterWriteNull() throws Exception {
      Gson gson = new GsonBuilder().create();
      ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter = createAdapter(gson);
      String result = adapter.write(null);
      assertEquals("null", result);
  }
  
  @Test
  public void testAdapterReadObject() throws Exception {
      Gson gson = new GsonBuilder().create();
      ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter = createAdapter(gson);
      TestClass result = adapter.read(createJsonReader("{\"field1\": 123, \"field2\": \"test\", \"field3\": 123.45}"));
      assertNotNull(result);
      assertEquals(123, result.field1);
      assertEquals("test", result.field2);
      assertEquals(123.45, result.field3);
  }
  
  @Test
  public void testAdapterWriteObject() throws Exception {
      Gson gson = new GsonBuilder().create();
      ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter = createAdapter(gson);
      TestClass testObject = new TestClass();
      testObject.field1 = 123;
      testObject.field2 = "test";
      testObject.field3 = 123.45;
      String result = adapter.write(testObject);
      assertEquals("{\"field1\":123,\"field2\":\"test\",\"field3\":123.45}", result);
  }
  
  private Field getField(String fieldName) {
      try {
          return TestClass.class.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
          fail("Field not found: " + fieldName);
      }
      return null;
  }
  
  private ReflectiveTypeAdapterFactory.Adapter<TestClass> createAdapter(Gson gson) {
      TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
      Class<? super TestClass> raw = typeToken.getRawType();
      ObjectConstructor<TestClass> constructor = new ConstructorConstructor().get(typeToken);
      Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, typeToken, raw);
      return new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
  }
  
  private JsonReader createJsonReader(String json) {
      return new JsonReader(null);
  }
  
  private static class TestClass {
      int field1;
      String field2;
      double field3;
      
      public TestClass() {}
  }
}