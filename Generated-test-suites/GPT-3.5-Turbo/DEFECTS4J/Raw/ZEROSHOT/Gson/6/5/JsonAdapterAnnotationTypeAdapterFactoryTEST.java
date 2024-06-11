import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private ConstructorConstructor constructorConstructor;
    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ExpectTypeAdapterCreatedAndReturned() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<Foo> targetType = TypeToken.get(Foo.class);

        // Act
        TypeAdapter<Foo> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
        assertEquals(FooTypeAdapter.class, result.getClass());
    }

    @Test
    public void testCreate_WithoutJsonAdapterAnnotation_ExpectNull() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<Bar> targetType = TypeToken.get(Bar.class);

        // Act
        TypeAdapter<Bar> result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterValue_ExpectTypeAdapterCreatedAndReturned() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Qux> fieldType = TypeToken.get(Qux.class);
        JsonAdapter annotation = Qux.class.getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertEquals(QuxTypeAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterFactoryValue_ExpectTypeAdapterCreatedAndReturned() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Baz> fieldType = TypeToken.get(Baz.class);
        JsonAdapter annotation = Baz.class.getAnnotation(JsonAdapter.class);

        // Act
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertEquals(BazTypeAdapter.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidValue_ExpectIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Invalid> fieldType = TypeToken.get(Invalid.class);
        JsonAdapter annotation = Invalid.class.getAnnotation(JsonAdapter.class);

        // Act
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    private static class Foo {
    }

    private static class Bar {
    }

    @JsonAdapter(FooTypeAdapter.class)
    private static class FooTypeAdapter extends TypeAdapter<Foo> {
    }

    @JsonAdapter(QuxTypeAdapterFactory.class)
    private static class Qux {
    }

    private static class QuxTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    @JsonAdapter(BazTypeAdapterFactory.class)
    private static class Baz {
    }

    private static class BazTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    @JsonAdapter(InvalidTypeAdapter.class)
    private static class Invalid {
    }

    private static class InvalidTypeAdapter {
    }
}