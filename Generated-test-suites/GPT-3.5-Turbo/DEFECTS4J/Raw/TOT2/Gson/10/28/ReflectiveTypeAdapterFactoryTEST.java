import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testExcludeField() {
    // Test excludeField method with serialize parameter as true
    // Verify the result is as expected
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(TestClass.class.getDeclaredField("testField"), true));
  }

  @Test
  public void testGetFieldNames() {
    // Test getFieldNames method with a field that has SerializedName annotation
    // Verify the list of field names is correct
    assertEquals(1, new ReflectiveTypeAdapterFactory().getFieldNames(TestClass.class.getDeclaredField("testField")).size());

    // Test getFieldNames method with a field that does not have SerializedName annotation
    // Verify the default field name is used
    assertEquals("testField", new ReflectiveTypeAdapterFactory().getFieldNames(TestClass.class.getDeclaredField("testField")).get(0));
  }

  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeAdapter typeAdapter = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
      @Override
      public String translateName(Field f) {
        return null;
      }
    }, new Excluder()).create(gson, TypeToken.get(TestClass.class));

    assertNotNull(typeAdapter);
  }

  private static class TestClass {
    @SerializedName("testField")
    String testField;
  }
}