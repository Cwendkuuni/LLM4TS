package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    @Mock
    private Gson gson;

    @Mock
    private ConstructorConstructor constructorConstructor;

    @Mock
    private TypeToken targetType;

    @Mock
    private JsonAdapter annotation;

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        when(targetType.getRawType()).thenReturn(TestClass.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);

        TypeAdapter result = factory.create(gson, targetType);

        assertNull(result);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        when(targetType.getRawType()).thenReturn(TestClass.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(TypeAdapter.class);

        TypeAdapter result = factory.create(gson, targetType);

        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test
    public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        when(targetType.getRawType()).thenReturn(TestClass.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(TypeAdapterFactory.class);
        TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
        when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);
        when(typeAdapterFactory.create(gson, targetType)).thenReturn(mock(TypeAdapter.class));

        TypeAdapter result = factory.create(gson, targetType);

        verify(typeAdapterFactory, times(1)).create(gson, targetType);
        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidAnnotationValue_ThrowsException() {
        when(targetType.getRawType()).thenReturn(TestClass.class);
        when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn(String.class);

        TypeAdapter result = factory.create(gson, targetType);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
        when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(mock(ConstructorConstructor.class));

        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

        assertEquals(TypeAdapter.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationValue_ThrowsException() {
        when(annotation.value()).thenReturn(String.class);

        TypeAdapter result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
    }

    private static class TestClass {}

}