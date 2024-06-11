import static org.junit.Assert.*;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    // Extracted and listed public methods:
    // 1. public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor)
    // 2. public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType)
    // 3. static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> fieldType, JsonAdapter annotation)

    @Test
    public void testCreate_WithAnnotationNull_ShouldReturnNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);
        TypeAdapter<String> result = factory.create(gson, targetType);
        assertNull(result);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterValue_ShouldReturnTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterFactoryValue_ShouldReturnTypeAdapterFactory() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = JsonAdapterAnnotationTypeAdapterFactory.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidValue_ShouldThrowIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = Integer.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
    }

}