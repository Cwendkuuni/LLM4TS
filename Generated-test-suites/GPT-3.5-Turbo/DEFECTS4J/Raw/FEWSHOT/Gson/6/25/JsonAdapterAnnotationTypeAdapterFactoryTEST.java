import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setUp() {
        constructorConstructor = Mockito.mock(ConstructorConstructor.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = Mockito.mock(Gson.class);
    }

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
        TypeToken<String> targetType = TypeToken.get(String.class);
        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<String> expectedResult = Mockito.mock(TypeAdapter.class);
        Mockito.when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(expectedResult);

        TypeAdapter<String> result = factory.create(gson, targetType);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testCreate_WithoutJsonAdapterAnnotation_ReturnsNull() {
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);
        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);

        TypeAdapter<Integer> result = factory.create(gson, targetType);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidAnnotationValue_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);
        Gson gson = Mockito.mock(Gson.class);
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
        Mockito.when(annotation.value()).thenReturn(Integer.class);

        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

}