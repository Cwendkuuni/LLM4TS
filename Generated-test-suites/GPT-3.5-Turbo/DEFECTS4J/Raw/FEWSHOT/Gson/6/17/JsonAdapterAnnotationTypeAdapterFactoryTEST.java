package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        gson = mock(Gson.class);
    }

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
        TypeAdapter<Foo> expectedTypeAdapter = mock(TypeAdapter.class);
        TypeToken<Foo> targetType = TypeToken.get(Foo.class);

        when(targetType.getRawType()).thenReturn(Foo.class);
        when(Foo.class.getAnnotation(JsonAdapter.class)).thenReturn(mock(JsonAdapter.class));
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(mock(ConstructorConstructor.class));
        when(constructorConstructor.construct()).thenReturn(expectedTypeAdapter);

        TypeAdapter<Foo> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor).create(gson, targetType);

        assertEquals(expectedTypeAdapter, typeAdapter);
    }

    @Test
    public void testCreate_WithNoJsonAdapterAnnotation_ReturnsNull() {
        TypeToken<Foo> targetType = TypeToken.get(Foo.class);

        when(targetType.getRawType()).thenReturn(Foo.class);
        when(Foo.class.getAnnotation(JsonAdapter.class)).thenReturn(null);

        TypeAdapter<Foo> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor).create(gson, targetType);

        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        JsonAdapter annotation = mock(JsonAdapter.class);

        when(annotation.value()).thenReturn(Object.class);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(Foo.class), annotation);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterValue_ReturnsTypeAdapter() {
        TypeAdapter<Foo> expectedTypeAdapter = mock(TypeAdapter.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<TypeAdapter<Foo>> typeAdapterClass = (Class<TypeAdapter<Foo>>) (Class) FooTypeAdapter.class;
        TypeToken<Foo> fieldType = TypeToken.get(Foo.class);

        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(eq(TypeToken.get(typeAdapterClass)))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(expectedTypeAdapter);

        TypeAdapter<Foo> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertEquals(expectedTypeAdapter, typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterFactoryValue_ReturnsTypeAdapter() {
        TypeAdapter<Foo> expectedTypeAdapter = mock(TypeAdapter.class);
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<TypeAdapterFactory> typeAdapterFactoryClass = (Class<TypeAdapterFactory>) (Class) FooTypeAdapterFactory.class;
        TypeToken<Foo> fieldType = TypeToken.get(Foo.class);

        when(annotation.value()).thenReturn(typeAdapterFactoryClass);
        when(constructorConstructor.get(eq(TypeToken.get(typeAdapterFactoryClass)))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.create(eq(gson), eq(fieldType))).thenReturn(expectedTypeAdapter);

        TypeAdapter<Foo> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertEquals(expectedTypeAdapter, typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_WithNullSafeAnnotation_ReturnsNullSafeTypeAdapter() {
        TypeAdapter<Foo> expectedTypeAdapter = mock(TypeAdapter.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<TypeAdapter<Foo>> typeAdapterClass = (Class<TypeAdapter<Foo>>) (Class) FooTypeAdapter.class;
        TypeToken<Foo> fieldType = TypeToken.get(Foo.class);

        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(eq(TypeToken.get(typeAdapterClass)))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(expectedTypeAdapter);
        when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);

        TypeAdapter<Foo> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertEquals(expectedTypeAdapter, typeAdapter);
    }

    public static class Foo {
    }

    public static class FooTypeAdapter extends TypeAdapter<Foo> {
        @Override
        public void write(JsonWriter out, Foo value) throws IOException {
        }

        @Override
        public Foo read(JsonReader in) throws IOException {
            return null;
        }
    }

    public static class FooTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }
}