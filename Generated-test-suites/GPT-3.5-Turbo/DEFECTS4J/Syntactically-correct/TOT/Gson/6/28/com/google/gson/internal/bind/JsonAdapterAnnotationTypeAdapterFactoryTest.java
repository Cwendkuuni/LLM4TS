import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<TestModel> targetType = new TypeToken<TestModel>() {};

        // Act
        TypeAdapter<TestModel> result = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor()).create(gson, targetType);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testCreate_WithoutJsonAdapterAnnotation_ReturnsNull() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<TestClass> targetType = new TypeToken<TestClass>() {};

        // Act
        TypeAdapter<TestClass> result = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor()).create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetTypeAdapter_WithValidTypeAdapterValue_ReturnsTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidTypeAdapterValue_ThrowsIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass2> fieldType = new TypeToken<TestClass2>() {};
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        // Act
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert (Expected Exception)
    }

    @Test
    public void testGetTypeAdapter_WithValidTypeAdapterFactoryValue_ReturnsTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidTypeAdapterFactoryValue_ThrowsIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass3> fieldType = new TypeToken<TestClass3>() {};
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        // Act
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert (Expected Exception)
    }

    @Test
    public void testGetTypeAdapter_WithValidType_ReturnsNullSafeTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertNotNull(result.nullSafe());
    }

}

class TestModel {}

@JsonAdapter(TestModelAdapter.class)
class TestClass {}

class TestClass2 {}

@JsonAdapter(TestClass2AdapterFactory.class)
class TestClass3 {}

class TestModelAdapter extends TypeAdapter<TestModel> {
    @Override
    public void write(JsonWriter out, TestModel value) throws IOException {
        // Implementation irrelevant for test cases
    }

    @Override
    public TestModel read(JsonReader in) throws IOException {
        // Implementation irrelevant for test cases
        return null;
    }
}

class TestClass2AdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        // Implementation irrelevant for test cases
        return null;
    }
}