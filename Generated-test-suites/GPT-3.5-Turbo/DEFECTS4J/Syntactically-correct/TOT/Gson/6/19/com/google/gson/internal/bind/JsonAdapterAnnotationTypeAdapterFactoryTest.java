import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void create_WhenJsonAdapterAnnotationExists_ReturnsTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<TargetClass> targetType = new TypeToken<TargetClass>(){};

        // Act
        TypeAdapter<TargetClass> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
        assertEquals(TypeAdapter.class, result.getClass().getSuperclass());
    }

    @Test
    public void create_WhenJsonAdapterAnnotationDoesNotExist_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<NoAnnotationClass> targetType = new TypeToken<NoAnnotationClass>(){};

        // Act
        TypeAdapter<NoAnnotationClass> result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test
    public void create_WhenJsonAdapterValueIsTypeAdapter_ReturnsNotNullTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<TypeAdapterAnnotationClass> targetType = new TypeToken<TypeAdapterAnnotationClass>(){};

        // Act
        TypeAdapter<TypeAdapterAnnotationClass> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
        assertEquals(TypeAdapter.class, result.getClass().getSuperclass());
    }

    @Test
    public void create_WhenJsonAdapterValueIsTypeAdapterFactory_ReturnsNotNullTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<TypeAdapterFactoryAnnotationClass> targetType = new TypeToken<TypeAdapterFactoryAnnotationClass>(){};

        // Act
        TypeAdapter<TypeAdapterFactoryAnnotationClass> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
        assertEquals(TypeAdapter.class, result.getClass().getSuperclass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_WhenJsonAdapterValueIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<InvalidTypeAnnotationClass> targetType = new TypeToken<InvalidTypeAnnotationClass>(){};

        // Act
        factory.create(gson, targetType);

        // Assert (Exception expected)
    }
}