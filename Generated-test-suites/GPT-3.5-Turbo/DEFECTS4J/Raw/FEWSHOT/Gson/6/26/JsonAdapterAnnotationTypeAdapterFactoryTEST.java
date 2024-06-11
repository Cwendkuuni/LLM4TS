package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;
    private TypeToken<Object> targetType;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = mock(Gson.class);
        targetType = TypeToken.get(Object.class);
    }

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        TypeAdapter<Object> adapter = factory.create(gson, targetType);
        assertNull(adapter);
    }

    @Test
    public void testCreate_WithAnnotation_ReturnsTypeAdapter() {
        TypeAdapter<Object> adapter = factory.create(gson, TypeToken.get(JsonAdapterAnnotationTypeAdapterFactoryTest.class));
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidValue_ThrowsException() {
        JsonAdapter annotation = mock(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(null, null, null, annotation);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterValue_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = TypeToken.get(Object.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        annotation.value(TypeAdapter.class);
        
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryValue_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = TypeToken.get(Object.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        annotation.value(TypeAdapterFactory.class);
        
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_Default_ReturnsNullSafeTypeAdapter() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<?> fieldType = TypeToken.get(Object.class);
        JsonAdapter annotation = mock(JsonAdapter.class);
        annotation.value(Object.class);
        
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertTrue(adapter instanceof TypeAdapter);
    }
}