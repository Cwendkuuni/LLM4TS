import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

    @Before
    public void setUp() {
        jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    }

    @Test
    public void testCreate_NullAnnotation_ReturnsNull() {
        Gson gson = new Gson();
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);
        TypeAdapter<Integer> result = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
        assertNull(result);
    }

    @Test
    public void testGetAdapter_TypeAdapter_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        JsonAdapter annotation = Integer.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertTrue(result instanceof TypeAdapter);
    }

    @Test
    public void testGetAdapter_TypeAdapterFactory_ReturnsTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        JsonAdapter annotation = Gson.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertTrue(result instanceof TypeAdapterFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAdapter_InvalidAnnotation_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        JsonAdapter annotation = JsonAdapterAnnotationTypeAdapterFactory.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}