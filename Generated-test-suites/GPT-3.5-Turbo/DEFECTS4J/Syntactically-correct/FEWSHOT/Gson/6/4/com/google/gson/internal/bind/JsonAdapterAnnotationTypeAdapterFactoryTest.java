package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson;
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        gson = mock(Gson.class);
        constructorConstructor = mock(ConstructorConstructor.class);
    }

    @Test
    public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestObject> targetType = new TypeToken<TestObject>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(targetType.getRawType()).thenReturn(TestObject.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        Class<TypeAdapter<?>> typeAdapterClass = (Class<TypeAdapter<?>>) (TypeAdapter.class);
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(typeAdapter);
        
        TypeAdapter<TestObject> result = factory.create(gson, targetType);
        
        assertEquals(typeAdapter, result);
    }

    @Test
    public void testCreate_WithoutJsonAdapterAnnotation_ReturnsNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeToken<TestObject> targetType = new TypeToken<TestObject>() {};
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);
        
        TypeAdapter<TestObject> result = factory.create(gson, targetType);
        
        assertNull(result);
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterValue_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Class<TypeAdapter<?>> typeAdapterClass = (Class<TypeAdapter<?>>) (TypeAdapter.class);
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(typeAdapter);
        
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertEquals(typeAdapter, result);
        verify(typeAdapter, times(1)).nullSafe();
    }

    @Test
    public void testGetTypeAdapter_WithTypeAdapterFactoryValue_ReturnsTypeAdapterFromTypeAdapterFactory() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Class<TypeAdapterFactory> typeAdapterFactoryClass = (Class<TypeAdapterFactory>) (TypeAdapterFactory.class);
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
        when(annotation.value()).thenReturn(typeAdapterFactoryClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.create(gson, fieldType)).thenReturn(typeAdapter);
        
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertEquals(typeAdapter, result);
        verify(typeAdapter, times(1)).nullSafe();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_WithInvalidJsonAdapterValue_ThrowsException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        Class<?> invalidValueClass = String.class;
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);
        when(annotation.value()).thenReturn(invalidValueClass);
        
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    private static class TestObject {

    }
}