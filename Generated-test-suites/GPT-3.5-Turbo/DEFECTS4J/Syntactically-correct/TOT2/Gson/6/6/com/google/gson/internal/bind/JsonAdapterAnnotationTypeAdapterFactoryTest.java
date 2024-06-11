import static org.junit.Assert.*;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

    @Test
    public void testCreate_NullAnnotation() {
        // Given
        Gson gson = new Gson();
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);

        // When
        TypeAdapter<TestClass> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);

        // Then
        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass() {
        // Given
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestTypeAdapter> fieldType = TypeToken.get(TestTypeAdapter.class);
        JsonAdapter annotation = TestTypeAdapter.class.getAnnotation(JsonAdapter.class);

        // When
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Then
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TestTypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryClass() {
        // Given
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestTypeAdapterFactory> fieldType = TypeToken.get(TestTypeAdapterFactory.class);
        JsonAdapter annotation = TestTypeAdapterFactory.class.getAnnotation(JsonAdapter.class);

        // When
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Then
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TestTypeAdapterFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotation() {
        // Given
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestInvalidAnnotation> fieldType = TypeToken.get(TestInvalidAnnotation.class);
        JsonAdapter annotation = TestInvalidAnnotation.class.getAnnotation(JsonAdapter.class);

        // When
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    // Edge cases and error scenarios can be added here...

}