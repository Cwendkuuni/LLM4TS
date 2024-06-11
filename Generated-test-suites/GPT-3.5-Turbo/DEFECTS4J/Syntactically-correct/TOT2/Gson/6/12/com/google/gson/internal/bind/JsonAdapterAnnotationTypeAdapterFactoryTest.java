import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Gson gson = new Gson();

        TypeAdapter<String> typeAdapter = factory.create(gson, TypeToken.get(String.class));

        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetAdapter_TypeAdapter_ReturnsNonNull() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);

        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAdapter_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return Object.class; // Invalid JsonAdapter value
            }
        };

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}