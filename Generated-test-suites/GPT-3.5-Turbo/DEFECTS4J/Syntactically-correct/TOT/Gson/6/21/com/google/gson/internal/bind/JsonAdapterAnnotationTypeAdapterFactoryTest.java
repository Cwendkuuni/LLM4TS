import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setup() {
        constructorConstructor = new ConstructorConstructor();
        gson = new Gson();
    }

    @Test
    public void create_WhenNoJsonAdapterAnnotation_ReturnsNull() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<String> targetType = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result = factory.create(gson, targetType);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void create_WhenJsonAdapterAnnotationExistsAndValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);

        // Act
        TypeAdapter<TestClass> result = factory.create(gson, targetType);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void create_WhenJsonAdapterAnnotationExistsAndValueIsTypeAdapterFactory_ReturnsTypeAdapterInstance() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestInterface> targetType = TypeToken.get(TestInterface.class);

        // Act
        TypeAdapter<TestInterface> result = factory.create(gson, targetType);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_WhenJsonAdapterAnnotationExistsAndValueIsNotTypeAdapterOrTypeAdapterFactory_ThrowsIllegalArgumentException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<InvalidClass> targetType = TypeToken.get(InvalidClass.class);

        // Act
        factory.create(gson, targetType);

        // Assert
        // Expects IllegalArgumentException to be thrown
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestTypeAdapterClass> fieldType = TypeToken.get(TestTypeAdapterClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapterFactory_ReturnsTypeAdapterInstance() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestTypeAdapterFactory> fieldType = TypeToken.get(TestTypeAdapterFactory.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_WhenValueIsNotTypeAdapterOrTypeAdapterFactory_ThrowsIllegalArgumentException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<InvalidType> fieldType = TypeToken.get(InvalidType.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        // Expects IllegalArgumentException to be thrown
    }

    @Test
    public void getTypeAdapter_WhenValueIsValidTypeAdapter_ReturnsTypeAdapterInstanceWithNotNullSafety() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestTypeAdapterClass> fieldType = TypeToken.get(TestTypeAdapterClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.nullSafe());
    }

    @Test
    public void getTypeAdapter_WhenValueIsValidTypeAdapterFactory_ReturnsTypeAdapterInstanceWithNotNullSafety() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestTypeAdapterFactory> fieldType = TypeToken.get(TestTypeAdapterFactory.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.nullSafe());
    }

    @Test
    public void getTypeAdapter_WhenValueIsValidTypeAdapter_ReturnsTypeAdapterInstanceWithNotNullSafetyAfterNullSafetyApplied() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestTypeAdapterClass> fieldType = TypeToken.get(TestTypeAdapterClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> expectedTypeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        TypeAdapter<?> nullSafeAdapter = result.nullSafe();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.nullSafe());
        Assert.assertEquals(expectedTypeAdapter.nullSafe(), nullSafeAdapter);
    }

    @Test
    public void getTypeAdapter_WhenValueIsValidTypeAdapterFactory_ReturnsTypeAdapterInstanceWithNotNullSafetyAfterNullSafetyApplied() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestTypeAdapterFactory> fieldType = TypeToken.get(TestTypeAdapterFactory.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> expectedTypeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        TypeAdapter<?> nullSafeAdapter = result.nullSafe();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.nullSafe());
        Assert.assertEquals(expectedTypeAdapter.nullSafe(), nullSafeAdapter);
    }

    @Test(expected = JsonParseException.class)
    public void getTypeAdapter_WhenAnnotationValueIsInvalidTypeAdapterClass_ThrowsJsonParseException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<InvalidTypeAdapterClass> fieldType = TypeToken.get(InvalidTypeAdapterClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        // Expects JsonParseException to be thrown
    }

    @Test(expected = JsonParseException.class)
    public void getTypeAdapter_WhenAnnotationValueIsInvalidTypeAdapterFactory_ThrowsJsonParseException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<InvalidTypeAdapterFactory> fieldType = TypeToken.get(InvalidTypeAdapterFactory.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        // Expects JsonParseException to be thrown
    }

}

class TestClass {}

interface TestInterface {}

class InvalidClass {}

@JsonAdapter(value = TestTypeAdapterClass.class)
class TestTypeAdapterClass {}

@JsonAdapter(value = TestTypeAdapterFactory.class)
class TestTypeAdapterFactory {}

@JsonAdapter(value = InvalidTypeAdapterClass.class)
class InvalidTypeAdapterClass {}

@JsonAdapter(value = InvalidTypeAdapterFactory.class)
class InvalidTypeAdapterFactory {}