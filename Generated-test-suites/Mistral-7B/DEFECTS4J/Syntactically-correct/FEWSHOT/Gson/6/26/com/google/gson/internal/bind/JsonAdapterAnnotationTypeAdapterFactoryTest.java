package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
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
import org.mockito.ArgumentMatchers;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    private ConstructorConstructor constructorConstructor;
    private Gson gson;
    private JsonAdapterAnnotationTypeAdapterFactory typeAdapterFactory;

    @Before
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        gson = mock(Gson.class);
        typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullInput() {
        assertNull(typeAdapterFactory.create(gson, null));
    }

    @Test
    public void testNullFieldType() {
        TypeToken<?> nullTypeToken = null;
        assertNull(typeAdapterFactory.create(gson, nullTypeToken));
    }

    @Test
    public void testNoJsonAdapterAnnotation() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        assertNull(typeAdapterFactory.create(gson, typeToken));
    }

    @Test
    public void testTypeAdapter() {
        JsonAdapter jsonAdapterAnnotation = new JsonAdapter(TypeAdapter.class);
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        TypeAdapter<Integer> typeAdapter = mock(TypeAdapter.class);
        when(constructorConstructor.get(ArgumentMatchers.eq(TypeToken.get(TypeAdapter.class)))).thenReturn(typeAdapter);

        TypeAdapter<Integer> result = typeAdapterFactory.create(gson, typeToken);

        assertThat(result).isSameInstanceAs(typeAdapter);
    }

    @Test
    public void testTypeAdapterFactory() {
        JsonAdapter jsonAdapterAnnotation = new JsonAdapter(TypeAdapterFactory.class);
        TypeToken<String> typeToken = new TypeToken<String>() {};
        TypeAdapterFactory typeAdapterFactoryMock = mock(TypeAdapterFactory.class);
        when(constructorConstructor.get(ArgumentMatchers.eq(TypeToken.get(TypeAdapterFactory.class)))).thenReturn(typeAdapterFactoryMock);

        TypeAdapter<String> result = typeAdapterFactory.create(gson, typeToken);

        TypeAdapter<String> expected = typeAdapterFactoryMock.create(gson, typeToken);
        assertThat(result).isSameInstanceAs(expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonAdapterAnnotationValue() {
        JsonAdapter jsonAdapterAnnotation = new JsonAdapter(int[].class);
        TypeToken<String> typeToken = new TypeToken<String>() {};

        typeAdapterFactory.create(gson, typeToken);
    }
}