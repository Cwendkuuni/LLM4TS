import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreateWithAnnotation() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);
        TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateWithoutAnnotation() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);
        TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);

        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapterWithTypeAdapterValue() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = StringFieldTypeAdapter.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapterWithTypeAdapterFactoryValue() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = DummyAdapterFactory.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterWithInvalidValue() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = InvalidTypeAdapter.class.getAnnotation(JsonAdapter.class);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
    }
}