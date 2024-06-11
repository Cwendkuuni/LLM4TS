
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = new Gson();
    }

    @Test
    public void testCreate_WhenNoAnnotation_ReturnsNull() {
        // Arrange
        TypeToken<String> targetType = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test
    public void testCreate_WhenAnnotationIsPresentAndValueTypeAdapter_ReturnsTypeAdapterInstance() {
        // Arrange
        TypeToken<CustomTypeAdapter> targetType = TypeToken.get(CustomTypeAdapter.class);

        // Act
        TypeAdapter<CustomTypeAdapter> result = factory.create(gson, targetType);

        // Assert
        assertEquals(CustomTypeAdapter.class, result.getClass());
    }

    @Test
    public void testCreate_WhenAnnotationIsPresentAndValueTypeAdapterFactory_ReturnsTypeAdapterInstance() {
        // Arrange
        TypeToken<CustomTypeAdapterFactory> targetType = TypeToken.get(CustomTypeAdapterFactory.class);

        // Act
        TypeAdapter<CustomTypeAdapterFactory> result = factory.create(gson, targetType);

        // Assert
        assertEquals(CustomTypeAdapterFactory.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_WhenAnnotationIsPresentButInvalidValue_ThrowsException() {
        // Arrange
        TypeToken<InvalidType> targetType = TypeToken.get(InvalidType.class);

        // Act
        factory.create(gson, targetType);

        // Assert
        // Expecting exception
    }

    private static class CustomTypeAdapter extends TypeAdapter<String> {

        @Override
        public void write(com.google.gson.stream.JsonWriter out, String value) {
            // Custom implementation
        }

        @Override
        public String read(com.google.gson.stream.JsonReader in) {
            // Custom implementation
            return null;
        }
    }

    private static class CustomTypeAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            // Custom implementation
            return null;
        }
    }

    private static class InvalidType {
        // Custom implementation
    }
}
