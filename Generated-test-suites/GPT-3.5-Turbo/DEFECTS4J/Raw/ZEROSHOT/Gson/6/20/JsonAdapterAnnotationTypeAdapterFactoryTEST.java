
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson;
    private ConstructorConstructor constructorConstructor;
    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setUp() {
        gson = mock(Gson.class);
        constructorConstructor = mock(ConstructorConstructor.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_WithNoAnnotation_ReturnsNull() {
        TypeToken<String> targetType = TypeToken.get(String.class);
        TypeAdapter<String> result = factory.create(gson, targetType);
        assert result == null;
    }

    @Test
    public void testCreate_WithAnnotation_ReturnsTypeAdapter() {
        TypeToken<MyClass> targetType = TypeToken.get(MyClass.class);

        JsonAdapter annotation = mock(JsonAdapter.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);

        Class<TypeAdapter<?>> typeAdapterClass = (Class<TypeAdapter<?>>)MyTypeAdapter.class;
        TypeAdapter<MyClass> typeAdapter = mock(TypeAdapter.class);
        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(typeAdapter);

        TypeAdapter<MyClass> result = factory.create(gson, targetType);
        assert result == typeAdapter.nullSafe();
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<TypeAdapter<?>> typeAdapterClass = (Class<TypeAdapter<?>>)MyTypeAdapter.class;

        when(constructorConstructor.get(TypeToken.get(typeAdapterClass)))
                .thenReturn(mock(TypeAdapter.class));

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory
                .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assert result != null;
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<TypeAdapterFactory> typeAdapterFactory = (Class<TypeAdapterFactory>)MyTypeAdapterFactory.class;

        TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
        when(factory.create(gson, fieldType)).thenReturn(typeAdapter);
        when(constructorConstructor.get(TypeToken.get(typeAdapterFactory)))
                .thenReturn(factory);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory
                .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assert result != null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotation_ThrowsException() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<MyClass> fieldType = TypeToken.get(MyClass.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<?> invalidValue = MyClass.class;

        when(annotation.value()).thenReturn(invalidValue);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory
                .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}

class MyClass {}

class MyTypeAdapter extends TypeAdapter<MyClass> {

    @Override
    public void write(JsonWriter out, MyClass value) throws IOException {

    }

    @Override
    public MyClass read(JsonReader in) throws IOException {
        return null;
    }
}

class MyTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        return null;
    }
}
