
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    
    @Test
    public void testCreate_WithNoAnnotation_ReturnsNull() {
        // Setup
        Gson gson = mock(Gson.class);
        TypeToken<String> targetType = new TypeToken<String>() {};

        // Execute
        TypeAdapter<String> result = new JsonAdapterAnnotationTypeAdapterFactory(
                new ConstructorConstructor()).create(gson, targetType);

        // Verify
        assertNull(result);
    }

    @Test
    public void testCreate_TypeAdapter_ReturnsTypeAdapter() {
        // Setup
        Gson gson = mock(Gson.class);
        TypeToken<TypeAdapter<String>> targetType = new TypeToken<TypeAdapter<String>>() {
        };
        JsonAdapter annotation = mock(JsonAdapter.class);

        // Execute
        TypeAdapter<TypeAdapter<String>> result = new JsonAdapterAnnotationTypeAdapterFactory(
                new ConstructorConstructor()).create(gson, targetType);

        // Verify
        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test
    public void testCreate_TypeAdapterFactory_ReturnsTypeAdapter() {
        // Setup
        Gson gson = mock(Gson.class);
        TypeToken<TypeAdapter<String>> fieldType = new TypeToken<TypeAdapter<String>>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);

        // Execute
        TypeAdapter<TypeAdapter<String>> result = new JsonAdapterAnnotationTypeAdapterFactory(
                new ConstructorConstructor()).create(gson, fieldType);

        // Verify
        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_TypeAdapter_ReturnsTypeAdapter() {
        // Setup
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<TypeAdapter<String>> fieldType = new TypeToken<TypeAdapter<String>>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);

        // Execute
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
                constructorConstructor, gson, fieldType, annotation);

        // Verify
        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactory_ReturnsTypeAdapter() {
        // Setup
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = mock(Gson.class);
        TypeToken<TypeAdapter<String>> fieldType = new TypeToken<TypeAdapter<String>>() {};
        JsonAdapter annotation = mock(JsonAdapter.class);

        // Execute
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
                constructorConstructor, gson, fieldType, annotation);

        // Verify
        assertEquals(TypeAdapter.class, result.getClass());
    }
}
