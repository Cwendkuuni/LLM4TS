
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Mock
    private Gson gson;

    @Mock
    private TypeToken<String> targetType;

    @Mock
    private ConstructorConstructor constructorConstructor;

    private JsonAdapterAnnotationTypeAdapterFactory adapterFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreateAnnotationNull() {
        TypeAdapter<String> result = adapterFactory.create(gson, targetType);
        assertNull(result);
    }

    @Test
    public void testCreateAnnotationTypeAdapter() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(targetType.getRawType()).thenReturn(String.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(TypeAdapter.class);

        TypeAdapter<String> result = adapterFactory.create(gson, targetType);
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter);
    }

    @Test
    public void testCreateAnnotationTypeAdapterFactory() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(targetType.getRawType()).thenReturn(String.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(TypeAdapterFactory.class);
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.create(gson, targetType)).thenReturn(mock(TypeAdapter.class));

        TypeAdapter<String> result = adapterFactory.create(gson, targetType);
        assertNotNull(result);
        verify(constructorConstructor).get(TypeToken.get(TypeAdapterFactory.class));
        verify(typeAdapterFactory).create(gson, targetType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAnnotationInvalidValueType() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(targetType.getRawType()).thenReturn(String.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(Object.class);

        adapterFactory.create(gson, targetType);
    }
  
    @Test
    public void testGetTypeAdapterTypeAdapter() {
        TypeAdapter<?> result = adapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, mock(JsonAdapter.class));
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter);
        assertTrue(result.isNullSafe());
    }

    @Test
    public void testGetTypeAdapterTypeAdapterFactory() {
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.create(gson, targetType)).thenReturn(mock(TypeAdapter.class));

        TypeAdapter<?> result = adapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, mock(JsonAdapter.class));
        assertNotNull(result);
        verify(constructorConstructor).get(TypeToken.get(TypeAdapterFactory.class));
        verify(typeAdapterFactory).create(gson, targetType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterInvalidValueType() {
        adapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, mock(JsonAdapter.class, Mockito.RETURNS_DEFAULTS));
    }

}
