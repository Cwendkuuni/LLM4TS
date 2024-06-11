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
import org.mockito.MockitoAnnotations;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        gson = mock(Gson.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate_noJsonAdapterAnnotation_returnsNull() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(null);
        TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        assertNull(factory.create(gson, typeToken));
    }

    @Test
    public void testCreate_JsonAdapterAnnotationWithTypeAdapter_returnsTypeAdapterInstance() {
        TypeToken<String> stringTypeToken = new TypeToken<String>() {};
        Class<TypeAdapter<String>> stringTypeAdapterClass = mock(TypeAdapter.class);
        when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(stringTypeAdapterClass);

        JsonAdapter annotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeToken);
        assertThat(typeAdapter).isEqualTo(stringTypeAdapterClass);
    }

    @Test
    public void testCreate_JsonAdapterAnnotationWithTypeAdapterFactory_returnsTypeAdapterInstance() {
        TypeToken<String> stringTypeToken = new TypeToken<String>() {};
        TypeAdapterFactory stringTypeAdapterFactory = mock(TypeAdapterFactory.class);
        when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(stringTypeAdapterFactory);

        JsonAdapter annotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeToken);
        when(stringTypeAdapterFactory.create(gson, stringTypeToken)).thenReturn(mock(TypeAdapter.class));
        TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);
        when(stringTypeAdapterFactory.create(gson, stringTypeToken)).thenReturn(expectedTypeAdapter);
        assertThat(typeAdapter).isEqualTo(expectedTypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_JsonAdapterAnnotationWithInvalidValue_throwsIllegalArgumentException() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        JsonAdapter annotation = typeToken.getRawType().getAnnotation(JsonAdapter.class);
        annotation.value().getClass(); // Trigger the cast
        TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }
}