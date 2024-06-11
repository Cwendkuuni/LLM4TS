import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = new Gson();
    }

    @Test
    public void create_TypeAdapterExists_ReturnsCorrectTypeAdapter() {
        // Arrange
        TypeToken<MyClass> targetType = new TypeToken<MyClass>() {};
        // Act
        TypeAdapter<MyClass> typeAdapter = factory.create(gson, targetType);
        // Assert
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MyTypeAdapter);
    }

    @Test
    public void create_NoTypeAdapter_ReturnsNull() {
        // Arrange
        TypeToken<OtherClass> targetType = new TypeToken<OtherClass>() {};
        // Act
        TypeAdapter<OtherClass> typeAdapter = factory.create(gson, targetType);
        // Assert
        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = new TypeToken<MyClass>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return String.class;
            }
        };
        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_TypeAdapterValue_ReturnsCorrectTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = new TypeToken<MyClass>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return MyTypeAdapter.class;
            }
        };
        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        // Assert
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MyTypeAdapter);
    }

    @Test
    public void getTypeAdapter_TypeAdapterFactoryValue_ReturnsCorrectTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = new TypeToken<MyClass>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return MyTypeAdapterFactory.class;
            }
        };
        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        // Assert
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MyTypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_InvalidTypeAdapterValue_ThrowsIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = new TypeToken<MyClass>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return InvalidTypeAdapter.class;
            }
        };
        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_NullSafeAnnotation_ReturnsNullSafeTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<MyClass> fieldType = new TypeToken<MyClass>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return MyTypeAdapter.class;
            }
            @Override
            public boolean nullSafe() {
                return true;
            }
        };
        // Act
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        // Assert
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MyTypeAdapter);
        assertTrue(typeAdapter.nullSafe());
    }
}

class MyClass {
}

class OtherClass {
}

class MyTypeAdapter extends TypeAdapter<MyClass> {
    // Implementation not necessary for test purposes
}

class MyTypeAdapterFactory implements TypeAdapterFactory {
    // Implementation not necessary for test purposes
}

class InvalidTypeAdapter {
    // Implementation not necessary for test purposes
}