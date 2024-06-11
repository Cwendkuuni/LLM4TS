package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    @Test
    public void testCreate() {
        // Test case when annotation is null
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
        assertNull(typeAdapter);

        // Test case when annotation is not null, and value is TypeAdapter
        Class<TypeAdapter<String>> typeAdapterClass = getTestTypeAdapterClass();
        targetType = TypeToken.get(String.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        typeAdapter = factory.create(gson, targetType);
        assertEquals(typeAdapterClass, typeAdapter.getClass());

        // Test case when annotation is not null, and value is TypeAdapterFactory
        Class<TypeAdapterFactory> typeAdapterFactory = getTestTypeAdapterFactoryClass();
        targetType = new TypeToken<TestClass>() {};
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        typeAdapter = factory.create(gson, targetType);
        assertEquals(typeAdapterFactory, typeAdapter.getClass());
    }

    private Class<TypeAdapter<String>> getTestTypeAdapterClass() {
        return TestTypeAdapter.class;
    }

    private Class<TypeAdapterFactory> getTestTypeAdapterFactoryClass() {
        return TestTypeAdapterFactory.class;
    }

    private class TestTypeAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            
        }

        @Override
        public String read(JsonReader in) throws IOException {
            return null;
        }
    }

    private class TestTypeAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

    private class TestClass {

    }
}