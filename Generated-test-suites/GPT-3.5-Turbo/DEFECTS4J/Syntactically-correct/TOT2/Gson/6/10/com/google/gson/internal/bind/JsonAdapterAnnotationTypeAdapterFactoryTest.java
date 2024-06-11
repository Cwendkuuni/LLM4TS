import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        TypeAdapter<String> typeAdapter = factory.create(new Gson(), TypeToken.get(String.class));

        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);

        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterWithTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        JsonAdapter annotation = JsonAdapterAnnotationTypeAdapterFactory.class.getAnnotation(JsonAdapter.class);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);
    }
}