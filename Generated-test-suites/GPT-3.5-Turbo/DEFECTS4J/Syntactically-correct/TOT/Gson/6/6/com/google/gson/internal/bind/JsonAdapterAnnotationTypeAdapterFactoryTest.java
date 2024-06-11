import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private ConstructorConstructor constructorConstructor = new ConstructorConstructor();

    @Test
    public void create_WhenAnnotationPresent_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<MyClass> targetType = TypeToken.get(MyClass.class);

        // Act
        TypeAdapter<MyClass> typeAdapter = factory.create(gson, targetType);

        // Assert
        assertNotNull(typeAdapter);
    }

    @Test
    public void create_WhenAnnotationNotPresent_ReturnsNull() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<NoAnnotationClass> targetType = TypeToken.get(NoAnnotationClass.class);

        // Act
        TypeAdapter<NoAnnotationClass> typeAdapter = factory.create(gson, targetType);

        // Assert
        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_WhenValueIsNeitherTypeAdapterNorTypeAdapterFactory_ThrowsIllegalArgumentException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = MyInvalidAdapter.class.getAnnotation(JsonAdapter.class);

        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapter_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = MyTypeAdapterAdapter.class.getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MyTypeAdapter);
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapterFactory_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = MyTypeAdapterFactoryAdapter.class.getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MyTypeAdapterFactory);
    }

    @Test
    public void getTypeAdapter_WhenValueIsNull_ReturnsNull() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = MyNullAdapter.class.getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNull(typeAdapter);
    }

    private static class MyClass {
        // Class for testing purposes
    }

    private static class NoAnnotationClass {
        // Class for testing purposes
    }

    @JsonAdapter(MyTypeAdapter.class)
    private static class MyTypeAdapterAdapter {
        // Class for testing purposes
    }

    private static class MyTypeAdapter extends TypeAdapter<MyClass> {
        // Class for testing purposes
    }

    @JsonAdapter(MyTypeAdapterFactory.class)
    private static class MyTypeAdapterFactoryAdapter {
        // Class for testing purposes
    }

    private static class MyTypeAdapterFactory implements TypeAdapterFactory {
        // Class for testing purposes
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    @JsonAdapter(null)
    private static class MyNullAdapter {
        // Class for testing purposes
    }

    @JsonAdapter(MyInvalidAdapter.class)
    private static class MyInvalidAnnotationAdapter {
        // Class for testing purposes
    }

    private static class MyInvalidAdapter {
        // Class for testing purposes
    }

}