
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_WhenAnnotationIsNull_ReturnsNull() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<Object> targetType = TypeToken.get(Object.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());

        // Act
        TypeAdapter<Object> result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test
    public void testCreate_WhenAnnotationIsNotNull_ReturnsTypeAdapter() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<TestType> targetType = TypeToken.get(TestType.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());

        // Act
        TypeAdapter<TestType> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetTypeAdapter_WhenValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
        // Arrange
        JsonAdapter annotation = getAnnotation(TestTypeAdapter.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestType.class);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestTypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_WhenValueIsTypeAdapterFactory_ReturnsTypeAdapterInstance() {
        // Arrange
        JsonAdapter annotation = getAnnotation(TestTypeAdapterFactory.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestType.class);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestTypeAdapterFactory.TestTypeAdapterFactoryImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WhenValueIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        JsonAdapter annotation = getAnnotation(String.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(TestType.class);

        // Act & Assert
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @SuppressWarnings("unchecked")
    private <T extends TypeAdapterFactory> JsonAdapter getAnnotation(Class<T> value) {
        return new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter<?>> value() {
                return (Class<? extends TypeAdapter<?>>) value;
            }

            @Override
            public Class<?>[] classes() {
                return new Class<?>[] {};
            }

            @Override
            public Class<?> expected() {
                return JsonAdapter.DEFAULT.class;
            }
        };
    }

    private static class TestType {}

    private static class TestTypeAdapter extends TypeAdapter<TestType> {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, TestType value) throws java.io.IOException {
        }

        @Override
        public TestType read(com.google.gson.stream.JsonReader in) throws java.io.IOException {
            return null;
        }
    }

    private static class TestTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            return new TestTypeAdapterFactoryImpl();
        }

        private static class TestTypeAdapterFactoryImpl extends TypeAdapter<TestType> {
            @Override
            public void write(com.google.gson.stream.JsonWriter out, TestType value) throws java.io.IOException {
            }

            @Override
            public TestType read(com.google.gson.stream.JsonReader in) throws java.io.IOException {
                return null;
            }
        }
    }
}
