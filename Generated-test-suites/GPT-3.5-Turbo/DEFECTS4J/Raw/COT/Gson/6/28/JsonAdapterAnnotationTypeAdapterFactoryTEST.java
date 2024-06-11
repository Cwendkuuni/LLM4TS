import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<String> adapter = factory.create(new Gson(), TypeToken.get(String.class));
        assertNull(adapter);
    }

    @Test
    public void testCreate_ValidJsonAdapterAnnotation_ReturnsTypeAdapterInstance() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<Integer> adapter = factory.create(new Gson(), TypeToken.get(Integer.class));
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test
    public void testGetTypeNameAdapter_TypeAdapter_ReturnsTypeAdapterInstance() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), TypeToken.get(String.class), new JsonAdapter() {
            @Override
            public Class<?> value() {
                return TypeAdapter.class;
            }
        });
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test
    public void testGetTypeNameAdapter_TypeAdapterFactory_ReturnsTypeAdapterInstance() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), TypeToken.get(String.class), new JsonAdapter() {
            @Override
            public Class<?> value() {
                return TypeAdapterFactory.class;
            }
        });
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeNameAdapter_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), TypeToken.get(String.class), new JsonAdapter() {
            @Override
            public Class<?> value() {
                return Object.class;
            }
        });
    }

    @Test
    public void testGetTypeNameAdapter_NullSafe_ReturnsNullSafeTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), TypeToken.get(String.class), new JsonAdapter() {
            @Override
            public Class<?> value() {
                return TypeAdapter.class;
            }
        });
        assertTrue(adapter.nullSafe());
    }
}