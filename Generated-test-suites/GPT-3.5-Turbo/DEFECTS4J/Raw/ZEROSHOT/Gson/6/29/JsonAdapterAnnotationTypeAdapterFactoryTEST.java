import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    
    private ConstructorConstructor constructorConstructor;
    private Gson gson;
    private TypeToken targetType;
    private JsonAdapter annotation;
    private JsonAdapterAnnotationTypeAdapterFactory factory;
    
    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        gson = mock(Gson.class);
        targetType = mock(TypeToken.class);
        annotation = mock(JsonAdapter.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }
    
    @Test
    public void create_shouldReturnNullWhenAnnotationIsNull() {
        // Arrange
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);

        // Act
        TypeAdapter result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }
    
    @Test
    public void create_shouldReturnTypeAdapterWhenAnnotationIsNotNull() {
        // Arrange
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(TypeAdapter.class);

        // Act
        TypeAdapter result = factory.create(gson, targetType);

        // Assert
        assertTrue(result instanceof TypeAdapter);
    }
    
    @Test
    public void getTypeAdapter_shouldReturnTypeAdapterWhenValueIsTypeAdapter() {
        // Arrange
        Class<?> value = TypeAdapter.class;
        when(annotation.value()).thenReturn(value);

        // Act
        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        // Assert
        assertTrue(result instanceof TypeAdapter);
    }
    
    @Test
    public void getTypeAdapter_shouldReturnTypeAdapterWhenValueIsTypeAdapterFactory() {
        // Arrange
        Class<?> value = TypeAdapterFactory.class;
        TypeAdapterFactory typeAdapterFactoryMock = mock(TypeAdapterFactory.class);
        TypeAdapter typeAdapterMock = mock(TypeAdapter.class);
        when(annotation.value()).thenReturn(value);
        when(constructorConstructor.get(TypeToken.get(value))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(typeAdapterFactoryMock);
        when(typeAdapterFactoryMock.create(gson, targetType)).thenReturn(typeAdapterMock);

        // Act
        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        // Assert
        assertTrue(result instanceof TypeAdapter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_shouldThrowIllegalArgumentExceptionWhenValueIsInvalid() {
        // Arrange
        Class<?> value = String.class;
        when(annotation.value()).thenReturn(value);

        // Act
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        // Assert
        // Expecting exception
    }
}