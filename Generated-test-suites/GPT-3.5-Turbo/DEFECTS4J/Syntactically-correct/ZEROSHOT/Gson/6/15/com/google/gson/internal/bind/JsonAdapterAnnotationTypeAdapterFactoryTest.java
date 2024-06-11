import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void create_NullAnnotation_ReturnsNull() {
        TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(JsonAdapterAnnotationTypeAdapterFactory.class));
        assertNull(adapter);
    }

    @Test
    public void create_ValidAnnotation_ReturnsTypeAdapter() {
        TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(MockClass.class));
        assertNotNull(adapter);
    }

    @Test
    public void getTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(MockClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertTrue(result instanceof TypeAdapter);
    }

    @Test
    public void getTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapterInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(MockClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        annotation.value = MockTypeAdapterFactory.class;

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertTrue(result instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(MockClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        annotation.value = String.class;

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_ReturnsNullSafeTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(MockClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(result);
        assertTrue(result.isLenient());
    }

    @JsonAdapter(MockTypeAdapter.class)
    private static class MockClass {
    }

    private static class MockTypeAdapter extends TypeAdapter<MockClass> {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, MockClass value) {
        }

        @Override
        public MockClass read(com.google.gson.stream.JsonReader in) {
            return null;
        }
    }

    private static class MockTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }
}