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
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;
    private ConstructorConstructor constructorConstructor;
    private Gson gson;
    private TypeToken<TestType> typeToken;

    @Before
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = mock(Gson.class);
        typeToken = TypeToken.get(TestType.class);
    }

    @Test
    public void testCreate_WithAnnotation_ReturnsTypeAdapter() {
        JsonAdapter annotation = TestType.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<TestType> expectedTypeAdapter = mock(TypeAdapter.class);

        Mockito.when(constructorConstructor.get(TypeToken.get(TestTypeAdapter.class))).thenReturn(expectedTypeAdapter);

        TypeAdapter<TestType> result = jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken);

        assertEquals(expectedTypeAdapter, result);
    }

    @Test
    public void testCreate_WithoutAnnotation_ReturnsNull() {
        TypeAdapter<TestType> result = jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_ThrowsIllegalArgumentException() {
        JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
        Mockito.when(annotation.value()).thenReturn(TypeAdapter.class);

        jsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);
    }
}