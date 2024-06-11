import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        // Test when targetType does not have JsonAdapter annotation
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter() {
        // Test when annotation value is TypeAdapter class
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        JsonAdapter annotation = IntegerAdapter.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(typeAdapter);
    }

    // Edge case: Test when annotation value is invalid
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterInvalidValue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = InvalidAdapter.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    // Edge case: Test when TypeAdapterFactory class is used as annotation value
    @Test
    public void testGetTypeAdapterTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<List<String>> fieldType = new TypeToken<List<String>>() {};
        JsonAdapter annotation = ListAdapterFactory.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(typeAdapter);
    }

    // Error scenario: Test when TypeAdapter is null safe
    @Test
    public void testGetTypeAdapterNullSafe() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Double> fieldType = TypeToken.get(Double.class);
        JsonAdapter annotation = DoubleAdapter.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        // Since DoubleAdapter is annotated as nullSafe, the returned type adapter should not be null
        assertNotNull(typeAdapter);
    }
}