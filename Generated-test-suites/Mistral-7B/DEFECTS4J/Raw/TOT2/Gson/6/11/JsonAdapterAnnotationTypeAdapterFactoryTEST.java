import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate() {
    // Given
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> expectedType = new TypeToken<String>() {};

    // When
    TypeAdapter<String> returnedTypeAdapter = factory.create(gson, expectedType);

    // Then
    assert(returnedTypeAdapter != null);
  }
}