import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.ConstructorConstructor;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate() {
    // Given
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);

    // When
    TypeAdapter<String> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    // Then
    assertNotNull(result);
  }

  @Test
  public void testCreate_NullGson() {
    // Given
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    TypeToken<String> targetType = TypeToken.get(String.class);
    Gson gson = null;

    // When - Assertion
    assertNull(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType));
  }

  @Test
  public void testCreate_NullTargetType() {
    // Given
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> targetType = null;

    // When - Assertion
    assertNull(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType));
  }

  // Add test cases for edge cases 3, 4, and 5 here
}