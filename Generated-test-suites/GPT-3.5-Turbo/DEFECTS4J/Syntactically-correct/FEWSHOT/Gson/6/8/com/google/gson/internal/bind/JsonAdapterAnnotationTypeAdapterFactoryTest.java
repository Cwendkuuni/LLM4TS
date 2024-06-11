package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@JsonAdapter(MockTypeAdapterFactory.class)
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = new Gson();
    }

    @Test
    public void testCreate_TypeAdapterReturned() {
        TypeToken<MockClass> targetType = TypeToken.get(MockClass.class);
        TypeAdapter<MockClass> typeAdapter = factory.create(gson, targetType);
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MockTypeAdapter);
    }

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);
        TypeAdapter<TestClass> typeAdapter = factory.create(gson, targetType);
        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationType_ThrowsException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapter annotation = TestClass.class.getAnnotation(JsonAdapter.class);
        factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapter annotation = MockClass.TypeAdapter.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MockClass.TypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapterInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapter annotation = MockClass.TypeAdapterFactory.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MockClass.TypeAdapterFactory);
    }

    @Test
    public void testGetTypeAdapter_NullSafe_ReturnsNullSafeTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapter annotation = MockClass.class.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof MockTypeAdapter);
        assertTrue(typeAdapter.nullSafe());
    }
}

class TestClass {

}

class MockClass {

    static final class TypeAdapter extends TypeAdapter<MockClass> {
        @Override
        public void write(JsonWriter out, MockClass value) throws IOException {
        }

        @Override
        public MockClass read(JsonReader in) throws IOException {
            return null;
        }
    }

    static final class TypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }
}

class MockTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        return new MockTypeAdapter<>();
    }
}

class MockTypeAdapter<T> extends TypeAdapter<T> {
    @Override
    public void write(JsonWriter out, T value) throws IOException {
    }

    @Override
    public T read(JsonReader in) throws IOException {
        return null;
    }
}