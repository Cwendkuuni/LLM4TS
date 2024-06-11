import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_NullAnnotation_ReturnsNull() {
        Gson gson = new Gson();
        TypeToken<Object> targetType = TypeToken.get(Object.class);

        TypeAdapter<Object> typeAdapter = factory.create(gson, targetType);

        assertEquals(null, typeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);

        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);

        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);
        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationType_ThrowsException() {
        TypeToken<Object> fieldType = TypeToken.get(Object.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        factory.getTypeAdapter(constructorConstructor, new Gson(), fieldType, annotation);
    }

    @Test
    public void testGetTypeAdapter_ValidAnnotationType_ReturnsTypeAdapter() {
        TypeToken<String> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, new Gson(), fieldType, annotation);
        assertNotNull(typeAdapter);
    }
}