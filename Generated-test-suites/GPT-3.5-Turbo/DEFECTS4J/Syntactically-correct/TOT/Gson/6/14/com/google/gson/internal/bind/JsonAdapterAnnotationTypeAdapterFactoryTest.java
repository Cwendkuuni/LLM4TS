import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_ValidJsonAdapterAnnotation_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TargetType> targetTypeToken = TypeToken.get(TargetType.class);

        // Act
        TypeAdapter<TargetType> typeAdapter = factory.create(gson, targetTypeToken);

        // Assert
        Assert.assertNotNull(typeAdapter);
    }

    @Test
    public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TargetTypeWithoutAnnotation> targetTypeToken = TypeToken.get(TargetTypeWithoutAnnotation.class);

        // Act
        TypeAdapter<TargetTypeWithoutAnnotation> typeAdapter = factory.create(gson, targetTypeToken);

        // Assert
        Assert.assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TargetType> fieldTypeToken = TypeToken.get(TargetType.class);
        TypeAdapter<?> typeAdapter = null;

        // Act
        try {
            typeAdapter = factory.getTypeAdapter(factory.constructorConstructor, gson, fieldTypeToken, new JsonAdapter(ValidTypeAdapterClass.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNotNull(typeAdapter);
        Assert.assertTrue(typeAdapter instanceof ValidTypeAdapterClass);
        Assert.assertTrue(typeAdapter.isNullSafe());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TargetType> fieldTypeToken = TypeToken.get(TargetType.class);

        // Act
        factory.getTypeAdapter(factory.constructorConstructor, gson, fieldTypeToken, new JsonAdapter(InvalidTypeAdapterValue.class));
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TargetType> fieldTypeToken = TypeToken.get(TargetType.class);
        TypeAdapter<?> typeAdapter = null;

        // Act
        try {
            typeAdapter = factory.getTypeAdapter(factory.constructorConstructor, gson, fieldTypeToken, new JsonAdapter(ValidTypeAdapterFactoryClass.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNotNull(typeAdapter);
        Assert.assertTrue(typeAdapter instanceof ValidTypeAdapterFactoryClass);
        Assert.assertTrue(typeAdapter.isNullSafe());
    }

    @Test
    public void testGetTypeAdapter_NullSafeAnnotation_ReturnsNullSafeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TargetType> fieldTypeToken = TypeToken.get(TargetType.class);
        TypeAdapter<?> typeAdapter = null;

        // Act
        try {
            typeAdapter = factory.getTypeAdapter(factory.constructorConstructor, gson, fieldTypeToken, new JsonAdapter(ValidTypeAdapterClass.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNotNull(typeAdapter);
        Assert.assertTrue(typeAdapter instanceof ValidTypeAdapterClass);
        Assert.assertTrue(typeAdapter.isNullSafe());
    }

}