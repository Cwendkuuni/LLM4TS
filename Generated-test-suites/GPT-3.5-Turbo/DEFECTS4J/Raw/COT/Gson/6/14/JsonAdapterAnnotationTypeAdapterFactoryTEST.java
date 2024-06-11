
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;

    @Before
    public void setUp() {
        jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    }

    @Test
    public void testCreate_NoAnnotation_ReturnsNullTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<Object> targetType = TypeToken.get(Object.class);

        TypeAdapter<Object> result = jsonAdapterFactory.create(gson, targetType);

        assertNull(result);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<MockClass1> targetType = TypeToken.get(MockClass1.class);

        TypeAdapter<MockClass1> result = jsonAdapterFactory.create(gson, targetType);

        assertNotNull(result);
        assertTrue(result instanceof MockTypeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<MockClass2> targetType = TypeToken.get(MockClass2.class);

        TypeAdapter<MockClass2> result = jsonAdapterFactory.create(gson, targetType);

        assertNotNull(result);
        assertTrue(result instanceof MockTypeAdapterFactory.MockClass2TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Object> fieldType = TypeToken.get(Object.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter> value() {
                return InvalidTypeAdapter.class;
            }
            @Override
            public Class<?>[] classes() {
                return new Class<?>[0];
            }
        };

        jsonAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}
