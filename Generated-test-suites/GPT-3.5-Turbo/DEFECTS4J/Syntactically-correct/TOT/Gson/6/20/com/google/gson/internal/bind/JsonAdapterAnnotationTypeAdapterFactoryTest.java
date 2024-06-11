import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson;
    private ConstructorConstructor constructorConstructor;
    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

    @Before
    public void setup() {
        gson = mock(Gson.class);
        constructorConstructor = mock(ConstructorConstructor.class);
        jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_withNullAnnotation_shouldReturnNull() {
        TypeToken targetType = mock(TypeToken.class);
        when(targetType.getRawType()).thenReturn(Object.class);
        TypeAdapter result = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
        assertNull(result);
    }

    @Test
    public void testCreate_withNonNullAnnotation_shouldReturnNonNullTypeAdapter() {
        TypeToken targetType = mock(TypeToken.class);
        Class clazz = mock(Class.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(targetType.getRawType()).thenReturn(clazz);
        when(clazz.getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(mock(ConstructorConstructor.class));
        TypeAdapter result = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeAdapter_withTypeAdapterValue_shouldReturnNonNullTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class valueClass = mock(Class.class);
        when(annotation.value()).thenReturn(valueClass);
        TypeAdapter typeAdapter = mock(TypeAdapter.class);
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(typeAdapter);
        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeAdapter_withTypeAdapterFactoryValue_shouldReturnNonNullTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class valueClass = mock(Class.class);
        List<Method> methods = new ArrayList<>();
        when(annotation.value()).thenReturn(valueClass);
        when(valueClass.isAssignableFrom(TypeAdapter.class)).thenReturn(false);
        when(valueClass.isAssignableFrom(TypeAdapterFactory.class)).thenReturn(true);
        when(valueClass.getMethods()).thenReturn(methods.toArray(new Method[0]));
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(typeAdapterFactory);
        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_withInvalidValue_shouldThrowException() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class valueClass = mock(Class.class);
        when(annotation.value()).thenReturn(valueClass);
        when(valueClass.isAssignableFrom(TypeAdapter.class)).thenReturn(false);
        when(valueClass.isAssignableFrom(TypeAdapterFactory.class)).thenReturn(false);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

}