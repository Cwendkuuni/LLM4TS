package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = mock(Gson.class);
    }

    @Test
    public void testCreate_withJsonAdapterAnnotation() {
        TypeToken targetType = mock(TypeToken.class);
        Class rawType = mock(Class.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        TypeAdapter typeAdapter = mock(TypeAdapter.class);

        when(targetType.getRawType()).thenReturn(rawType);
        when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(TypeAdapter.class);
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(typeAdapter);

        TypeAdapter result = factory.create(gson, targetType);

        assertEquals(typeAdapter, result);
    }

    @Test
    public void testCreate_withoutJsonAdapterAnnotation() {
        TypeToken targetType = mock(TypeToken.class);
        Class rawType = mock(Class.class);

        when(targetType.getRawType()).thenReturn(rawType);
        when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(null);

        TypeAdapter result = factory.create(gson, targetType);

        assertNull(result);
    }

    @Test
    public void testGetTypeAdapter_withTypeAdapterValue() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class value = TypeAdapter.class;
        TypeAdapter typeAdapter = mock(TypeAdapter.class);

        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(typeAdapter);

        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertEquals(typeAdapter, result);
    }

    @Test
    public void testGetTypeAdapter_withTypeAdapterFactoryValue() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class value = TypeAdapterFactory.class;
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        TypeAdapter typeAdapter = mock(TypeAdapter.class);

        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.construct()).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.create(gson, fieldType)).thenReturn(typeAdapter);

        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertEquals(typeAdapter, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_withInvalidValue() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class value = String.class;

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}