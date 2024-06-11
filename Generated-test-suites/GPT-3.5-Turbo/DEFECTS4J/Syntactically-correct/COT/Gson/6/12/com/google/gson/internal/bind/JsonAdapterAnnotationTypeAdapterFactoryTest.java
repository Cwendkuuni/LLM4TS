
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void create_withTypeAdapterAnnotation_shouldReturnTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<MyClass> targetType = TypeToken.get(MyClass.class);

    // Act
    TypeAdapter<MyClass> result = factory.create(gson, targetType);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void create_withoutTypeAdapterAnnotation_shouldReturnNull() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<OtherClass> targetType = TypeToken.get(OtherClass.class);

    // Act
    TypeAdapter<OtherClass> result = factory.create(gson, targetType);

    // Assert
    assertNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getTypeAdapter_withInvalidAnnotationValue_shouldThrowIllegalArgumentException() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    // Act & Assert (Exception)
  }

}
