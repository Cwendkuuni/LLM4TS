import com.google.gson.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Mock
    private ConstructorConstructor constructorConstructor;

    @InjectMocks
    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

    @Test
    public void testCreate_TypeAdapter() {
        Gson gson = mock(Gson.class);
        TypeToken<String> targetType = TypeToken.get(String.class);
        JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
        JsonAdapter valueAnnotation = mock(JsonAdapter.class);
        when(jsonAdapterAnnotation.value()).thenReturn(StringAdapter.class);

        TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);

        when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(expectedTypeAdapter);

        TypeAdapter<String> actualTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
        assertTypeAdapterEquals(expectedTypeAdapter, actualTypeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterFactory() {
        Gson gson = mock(Gson.class);
        TypeToken<String> targetType = TypeToken.get(String.class);
        JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
        JsonAdapter valueAnnotation = mock(JsonAdapter.class);
        when(jsonAdapterAnnotation.value()).thenReturn(CustomStringAdapterFactory.class);

        TypeAdapterFactory customStringAdapterFactory = mock(TypeAdapterFactory.class);
        when(customStringAdapterFactory.create(gson, targetType)).thenReturn(mock(TypeAdapter.class));

        when(constructorConstructor.get(TypeToken.get(CustomStringAdapterFactory.class))).thenReturn(customStringAdapterFactory);

        TypeAdapter<String> actualTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
        assertTypeAdapterEquals(mock(TypeAdapter.class), actualTypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidValue() {
        Gson gson = mock(Gson.class);
        TypeToken<String> targetType = TypeToken.get(String.class);
        JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
        when(jsonAdapterAnnotation.value()).thenReturn(Object.class);

        jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
    }

    private void assertTypeAdapterEquals(TypeAdapter<?> expectedTypeAdapter, TypeAdapter<?> actualTypeAdapter) {
        assertEquals(expectedTypeAdapter.getClass(), actualTypeAdapter.getClass());
        assertEquals(expectedTypeAdapter.isNullSafe(), actualTypeAdapter.isNullSafe());
    }

    private static class StringAdapter extends TypeAdapter<String> {
    }

    private static class CustomStringAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            return null;
        }
    }
}