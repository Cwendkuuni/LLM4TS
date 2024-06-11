import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);

        TypeAdapter<String> adapter = factory.create(gson, targetType);

        assertNull(adapter);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);
        class CustomTypeAdapter extends TypeAdapter<Integer> {}
        @JsonAdapter(CustomTypeAdapter.class) class TestObject {}

        TypeAdapter<Integer> adapter = factory.create(gson, targetType);

        assertNotNull(adapter);
    }

    @Test
    public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<Double> targetType = TypeToken.get(Double.class);
        class CustomTypeAdapterFactory implements TypeAdapterFactory {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
                return null;
            }
        }
        @JsonAdapter(CustomTypeAdapterFactory.class) class TestObject {}

        TypeAdapter<Double> adapter = factory.create(gson, targetType);

        assertNotNull(adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationType_ThrowsIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<Character> fieldType = TypeToken.get(Character.class);
        @JsonAdapter(Character.class) class TestObject {}

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, TestObject.class.getAnnotation(JsonAdapter.class));
    }
}