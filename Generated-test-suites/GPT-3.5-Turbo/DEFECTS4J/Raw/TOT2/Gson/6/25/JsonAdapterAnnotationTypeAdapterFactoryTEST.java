import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson;
    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = new GsonBuilder().registerTypeAdapterFactory(factory).create();
    }

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        TypeAdapter typeAdapter = factory.create(gson, TypeToken.get(String.class));
        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
        TypeAdapter typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), createMockAnnotation(TypeAdapterMock.class));
        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapterInstance() {
        TypeAdapter typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), createMockAnnotation(TypeAdapterFactoryMock.class));
        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), createMockAnnotation(InvalidAnnotationMock.class));
    }

    private JsonAdapter createMockAnnotation(Class<? extends TypeAdapter> value) {
        return new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter> value() {
                return value;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }
        };
    }
}