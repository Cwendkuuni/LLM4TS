import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Mock
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ShouldReturnCorrectTypeAdapter() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<TestClass> targetType = new TypeToken<TestClass>() {};

        // Act
        TypeAdapter<TestClass> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
        assertSame(TestAdapter.class, result.getClass());
    }

    @Test
    public void testCreate_WithoutJsonAdapterAnnotation_ShouldReturnNull() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<TestClassWithoutAnnotation> targetType = new TypeToken<TestClassWithoutAnnotation>() {};

        // Act
        TypeAdapter<TestClassWithoutAnnotation> result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidJsonAdapterValue_ShouldThrowIllegalArgumentException() {
        // Arrange
        ConstructorConstructor mockConstructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(String.class);

        // Act
        factory.getTypeAdapter(mockConstructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterValue_ShouldReturnTypeAdapter() {
        // Arrange
        ConstructorConstructor mockConstructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(TestAdapter.class);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(mockConstructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertSame(TestAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterFactoryValue_ShouldReturnTypeAdapter() {
        // Arrange
        ConstructorConstructor mockConstructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(TestAdapterFactory.class);
        TypeAdapterFactory mockedTypeAdapterFactory = mock(TestAdapterFactory.class);
        when(mockConstructorConstructor.get(TypeToken.get(TestAdapterFactory.class))).thenReturn(mockedTypeAdapterFactory);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(mockConstructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertSame(TestAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_WithUnsupportedValue_ShouldThrowIllegalArgumentException() {
        // Arrange
        ConstructorConstructor mockConstructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(Integer.class);

        // Act
        try {
            factory.getTypeAdapter(mockConstructorConstructor, gson, fieldType, annotation);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
        }
    }

    private static class TestAdapter extends TypeAdapter<TestClass> {
        // Test Adapter implementation
    }

    private static class TestAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    @JsonAdapter(TestAdapter.class)
    private static class TestClass {
        // Test Class implementation
    }

    private static class TestClassWithoutAnnotation {
        // Test Class without annotation
    }
}