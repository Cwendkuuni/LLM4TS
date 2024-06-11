import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setup() {
        constructorConstructor = new ConstructorConstructor();
        gson = new Gson();
    }

    @Test
    public void create_TypeAdapterAnnotationPresent_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<String> targetType = TypeToken.get(String.class);

        // Act
        TypeAdapter<?> typeAdapter = factory.create(gson, targetType);

        // Assert
        assertNotNull(typeAdapter);
    }

    @Test
    public void create_TypeAdapterAnnotationNotPresent_ReturnsNull() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);

        // Act
        TypeAdapter<?> typeAdapter = factory.create(gson, targetType);

        // Assert
        assertNull(typeAdapter);
    }

    @Test
    public void getTypeAdapter_TypeAdapterClass_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        Class<?> value = annotation.value();

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(typeAdapter);
        assertTrue(TypeAdapter.class.isAssignableFrom(value));
    }

    @Test
    public void getTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        Class<?> value = annotation.value();

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(typeAdapter);
        assertTrue(TypeAdapterFactory.class.isAssignableFrom(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        Class<?> value = annotation.value();

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_NullValue_ReturnsNullSafeTypeAdapter() {
        // Arrange
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        Class<?> value = annotation.value();

        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(typeAdapter);
        assertNotNull(typeAdapter.nullSafe());
    }

}