import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson = new Gson();
    private ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    private JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    @Test
    public void testCreate_TypeAdapterReturned() {
        TypeToken<String> stringTypeToken = TypeToken.get(String.class);
        TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeToken);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        TypeToken<Integer> integerTypeToken = TypeToken.get(Integer.class);
        TypeAdapter<Integer> typeAdapter = factory.create(gson, integerTypeToken);
        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterValue() {
        JsonAdapter annotation = JsonAdapter.class.getAnnotation(JsonAdapter.class);
        TypeToken<String> stringTypeToken = TypeToken.get(String.class);
        TypeAdapter<String> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, annotation);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryValue() {
        TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
        JsonAdapter annotation = listTypeToken.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<List<String>> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, listTypeToken, annotation);
        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidValue_ThrowsException() {
        JsonAdapter annotation = JsonAdapterAnnotationTypeAdapterFactory.class.getAnnotation(JsonAdapter.class);
        TypeToken<JsonAdapterAnnotationTypeAdapterFactory> factoryTypeToken = TypeToken.get(JsonAdapterAnnotationTypeAdapterFactory.class);
        factory.getTypeAdapter(constructorConstructor, gson, factoryTypeToken, annotation);
    }

}