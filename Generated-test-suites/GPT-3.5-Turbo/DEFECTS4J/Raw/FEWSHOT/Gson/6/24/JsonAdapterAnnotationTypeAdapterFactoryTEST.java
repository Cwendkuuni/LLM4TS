package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private TypeToken<?> fieldType;
    private JsonAdapter annotation;

    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        gson = mock(Gson.class);
        fieldType = mock(TypeToken.class);
        annotation = mock(JsonAdapter.class);
        when(fieldType.getRawType()).thenReturn(Object.class);
    }

    @Test
    public void testCreate() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);

        when(fieldType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);
        assertNull(factory.create(gson, fieldType));

        when(fieldType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(typeAdapter);
        assertEquals(typeAdapter, factory.create(gson, fieldType));
    }

    @Test
    public void testGetTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
        TypeAdapter<?> nullSafeTypeAdapter = mock(TypeAdapter.class);
        Class<? extends TypeAdapter> typeAdapterClass = TypeAdapter.class;
        Class<? extends TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;

        when(annotation.value()).thenReturn(typeAdapterClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct()).thenReturn(typeAdapter);
        when(typeAdapter.nullSafe()).thenReturn(nullSafeTypeAdapter);
        assertEquals(nullSafeTypeAdapter, factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation));

        when(annotation.value()).thenReturn(typeAdapterFactoryClass);
        when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(constructorConstructor);
        when(constructorConstructor.construct().create(gson, fieldType)).thenReturn(typeAdapter);
        assertEquals(nullSafeTypeAdapter, factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation));

        when(annotation.value()).thenReturn(Object.class);
        assertEquals(nullSafeTypeAdapter, factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation));
    }

}