import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );

    // When
    TypeAdapter<MyClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

    // Then
    MyClass myClass = new MyClass();
    myClass.setName("John Doe");
    myClass.setAge(30);

    // Test serialization
    String json = gson.toJson(myClass);
    Assert.assertEquals("{\"name\":\"John Doe\",\"age\":30}", json);

    // Test deserialization
    MyClass deserializedMyClass = gson.fromJson(json, MyClass.class);
    Assert.assertEquals(myClass, deserializedMyClass);

    // Test edge cases
    MyClass nullMyClass = null;
    String emptyJson = "{}";

    // Test serialization of null object
    String jsonNullMyClass = gson.toJson(nullMyClass);
    Assert.assertEquals("null", jsonNullMyClass);

    // Test deserialization of empty JSON
    MyClass deserializedEmptyMyClass = gson.fromJson(emptyJson, MyClass.class);
    Assert.assertNull(deserializedEmptyMyClass);

    // Test exception handling
    try {
      gson.fromJson("{\"name\":\"John Doe\",\"age\":30,\"invalidField\":\"value\"}", MyClass.class);
      Assert.fail();
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  static class MyClass {
    private String name;
    private int age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
}