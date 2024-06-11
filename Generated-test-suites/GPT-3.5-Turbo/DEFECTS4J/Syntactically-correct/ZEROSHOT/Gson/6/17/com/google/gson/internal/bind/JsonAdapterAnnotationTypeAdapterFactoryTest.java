import com.google.gson.Gson;
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
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void create_shouldReturnNull_whenAnnotationIsNull() {
        Gson gson = new Gson();
        TypeToken<String> targetType = new TypeToken<String>() {};
        TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
        assertNull(typeAdapter);
    }

    @Test
    public void create_shouldReturnTypeAdapter_whenAnnotationIsNotNull() {
        Gson gson = new Gson();
        TypeToken<TestClass> targetType = new TypeToken<TestClass>() {};
        TypeAdapter<TestClass> typeAdapter = factory.create(gson, targetType);
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
    }

    @Test
    public void getTypeAdapter_shouldReturnTypeAdapter_whenValueIsTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = TestClass.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
    }

    @Test
    public void getTypeAdapter_shouldReturnTypeAdapter_whenValueIsTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestAdapterFactory> fieldType = new TypeToken<TestAdapterFactory>() {};
        JsonAdapter annotation = TestAdapterFactory.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_shouldThrowException_whenValueIsNeitherTypeAdapterNorTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestInvalidClass> fieldType = new TypeToken<TestInvalidClass>() {};
        JsonAdapter annotation = TestInvalidClass.class.getAnnotation(JsonAdapter.class);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_shouldReturnNullSafeTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestClass> fieldType = new TypeToken<TestClass>() {};
        JsonAdapter annotation = TestClass.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
        assertTrue(typeAdapter.nullSafe());
    }

    private static class TestClass {}

    private static class TestAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    private static class TestInvalidClass {}
}