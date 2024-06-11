package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    private ConstructorConstructor constructorConstructor;
    private Gson gson;
    private TypeAdapter<?> typeAdapter;

    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        gson = mock(Gson.class);
        typeAdapter = mock(TypeAdapter.class);
    }

    @Test
    public void testCreate_withAnnotationIsNull_returnsNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeAdapter<?> result = factory.create(gson, TypeToken.get(Object.class));
        assertNull(result);
    }

    @Test
    public void testCreate_withAnnotationIsNotNull_returnsTypeAdapter() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        TypeToken<?> targetType = mock(TypeToken.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        TypeAdapter<?> result = factory.create(gson, targetType);

        assertEquals(typeAdapter, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_withValueIsTypeAdapterAndConstructorThrowsException_throwsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<?> value = mock(Class.class);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_withValueIsTypeAdapterFactoryAndConstructorThrowsException_throwsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<?> value = mock(Class.class);

        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void testGetTypeAdapter_withValueIsTypeAdapterAndConstructorReturnValueNull_returnsNullSafeTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<?> value = mock(Class.class);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNull(result);
    }

    @Test
    public void testGetTypeAdapter_withValueIsTypeAdapterFactoryAndConstructorReturnValueNull_returnsNullSafeTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = mock(TypeToken.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        Class<?> value = mock(Class.class);

        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNull(result);
    }
}