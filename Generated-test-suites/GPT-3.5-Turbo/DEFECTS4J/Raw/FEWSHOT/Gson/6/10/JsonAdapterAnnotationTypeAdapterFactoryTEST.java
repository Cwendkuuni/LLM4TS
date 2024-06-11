package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = new Gson();
    }

    @Test
    public void testCreate_NoJsonAdapterAnnotation_ShouldReturnNull() {
        TypeToken<String> targetType = TypeToken.get(String.class);
        assertNull(factory.create(gson, targetType));
    }

    @Test
    public void testCreate_TypeAdapterJsonAdapterAnnotation_ShouldReturnTypeAdapter() {
        TypeToken<TypeAdapterJsonAdapterAnnotation> targetType = TypeToken.get(TypeAdapterJsonAdapterAnnotation.class);
        TypeAdapter<TypeAdapterJsonAdapterAnnotation> typeAdapter = factory.create(gson, targetType);
        assertEquals(TypeAdapterJsonAdapterAnnotation.class, typeAdapter.getClass());
    }

    @Test
    public void testCreate_TypeAdapterFactoryJsonAdapterAnnotation_ShouldReturnTypeAdapter() {
        TypeToken<TypeAdapterFactoryJsonAdapterAnnotation> targetType = TypeToken.get(TypeAdapterFactoryJsonAdapterAnnotation.class);
        TypeAdapter<TypeAdapterFactoryJsonAdapterAnnotation> typeAdapter = factory.create(gson, targetType);
        assertEquals(TypeAdapterFactoryJsonAdapterAnnotation.class, typeAdapter.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidJsonAdapterValue_ShouldThrowException() {
        TypeToken<InvalidJsonAdapterValue> targetType = TypeToken.get(InvalidJsonAdapterValue.class);
        factory.create(gson, targetType);
    }

    // Helper classes for testing

    private static class TypeAdapterJsonAdapterAnnotation {
        @JsonAdapter(TestTypeAdapter.class)
        private String field;
    }

    private static class TypeAdapterFactoryJsonAdapterAnnotation {
        @JsonAdapter(TestTypeAdapterFactory.class)
        private String field;
    }

    private static class TestTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            return in.nextString();
        }
    }

    private static class TestTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    private static class InvalidJsonAdapterValue {
        @JsonAdapter(Object.class)
        private String field;
    }
}