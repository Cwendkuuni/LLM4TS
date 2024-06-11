import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_NullAnnotation_ReturnsNull() {
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);

        TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

        assertNull(typeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<SampleClass> targetType = TypeToken.get(SampleClass.class);

        TypeAdapter<SampleClass> typeAdapter = factory.create(gson, targetType);

        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterValue_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = SampleClass.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidValue_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = InvalidClass.class.getAnnotation(JsonAdapter.class);

        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    private static class SampleClass {}

    private static class InvalidClass {}

}