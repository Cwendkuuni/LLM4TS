import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();
        TypeToken<String> stringTypeToken = TypeToken.get(String.class);
        TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeToken);
        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> stringTypeToken = TypeToken.get(String.class);
        JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);
        try {
            JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
        }
    }
}