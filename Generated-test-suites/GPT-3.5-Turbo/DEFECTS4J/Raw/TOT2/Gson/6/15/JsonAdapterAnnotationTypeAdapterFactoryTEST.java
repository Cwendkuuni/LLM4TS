import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Test
    public void testCreate_TypeAdapterNotNull() {
        Gson gson = new Gson();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

        TypeAdapter<TestClass> typeAdapter = factory.create(gson, typeToken);

        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterNull() {
        Gson gson = new Gson();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<NoJsonAdapterTestClass> typeToken = TypeToken.get(NoJsonAdapterTestClass.class);

        TypeAdapter<NoJsonAdapterTestClass> typeAdapter = factory.create(gson, typeToken);

        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidJsonAdapterValue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter<?>> value() {
                return String.class;
            }
        };

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);
    }

    @JsonAdapter(TestTypeAdapterFactory.class)
    private static class TestClass {
    }

    private static class NoJsonAdapterTestClass {
    }

    private static class TestTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }
}