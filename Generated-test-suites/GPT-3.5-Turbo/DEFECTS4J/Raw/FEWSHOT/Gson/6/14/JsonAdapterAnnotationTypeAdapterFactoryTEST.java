package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson;
    private ConstructorConstructor constructorConstructor;
    private TypeToken<String> targetType;

    @Before
    public void setup() {
        gson = mock(Gson.class);
        constructorConstructor = mock(ConstructorConstructor.class);
        targetType = TypeToken.get(String.class);
    }

    @Test
    public void testCreate_ValidAnnotation_ReturnsTypeAdapter() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class typeAdapterClass = TypeAdapter.class;
        TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);

        when(targetType.getRawType()).thenReturn(typeAdapterClass);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(expectedTypeAdapter);

        TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);

        assertEquals(expectedTypeAdapter, result);
    }

    @Test
    public void testCreate_InvalidAnnotation_ReturnsNull() {
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);

        TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotation_ThrowsIllegalArgumentException() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class typeAdapterClass = Object.class;

        when(annotation.value()).thenReturn(typeAdapterClass);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class typeAdapterClass = TypeAdapter.class;
        TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);

        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(expectedTypeAdapter);

        TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        assertEquals(expectedTypeAdapter, result);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class typeAdapterFactory = TypeAdapterFactory.class;
        TypeAdapter<?> expectedTypeAdapter = mock(TypeAdapter.class);

        when(annotation.value()).thenReturn(typeAdapterFactory);
        when(constructorConstructor.get(TypeToken.get(typeAdapterFactory))).thenReturn(expectedTypeAdapter);
        when(expectedTypeAdapter.create(gson, targetType)).thenReturn(expectedTypeAdapter);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        assertEquals(expectedTypeAdapter, result);
    }

    @Test
    public void testGetTypeAdapter_NullSafe() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class typeAdapterFactory = TypeAdapter.class;
        TypeAdapter<?> expectedTypeAdapter = mock(TypeAdapter.class);

        when(annotation.value()).thenReturn(typeAdapterFactory);
        when(constructorConstructor.get(TypeToken.get(typeAdapterFactory))).thenReturn(expectedTypeAdapter);
        when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        assertEquals(expectedTypeAdapter, result);
    }
}