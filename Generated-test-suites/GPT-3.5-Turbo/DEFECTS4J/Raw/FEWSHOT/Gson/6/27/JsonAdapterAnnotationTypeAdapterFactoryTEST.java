package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.BeforeClass;
import org.junit.Test;

@JsonAdapter(TestTypeAdapter.class)
class TestClass {}

class TestTypeAdapter extends TypeAdapter<TestClass> {}

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    private static Gson gson;
    private static ConstructorConstructor constructorConstructor;
    private static JsonAdapterAnnotationTypeAdapterFactory factory;

    @BeforeClass
    public static void setUp() {
        gson = new Gson();
        constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void testCreate_ReturnsNull_WhenAnnotationIsNull() {
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);
        TypeAdapter<?> result = factory.create(gson, targetType);
        assertNull(result);
    }

    @Test
    public void testCreate_ReturnsTypeAdapter_WhenAnnotationHasTypeAdapterValue() {
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = factory.create(gson, targetType);
        assertEquals(TestTypeAdapter.class, result.getClass());
    }

    @Test
    public void testCreate_ReturnsTypeAdapter_WhenAnnotationHasTypeAdapterFactoryValue() {
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = factory.create(gson, targetType);
        assertEquals(TestTypeAdapter.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_ThrowsIllegalArgumentException_WhenAnnotationHasInvalidValue() {
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
        factory.create(gson, targetType);
    }

    @Test
    public void testGetTypeAdapter_ReturnsTypeAdapter_WhenTypeAdapterValueIsAssignableFromTypeAdapterClass() {
        TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertEquals(TestTypeAdapter.class, result.getClass());
    }

    @Test
    public void testGetTypeAdapter_ReturnsTypeAdapter_WhenTypeAdapterFactoryValueIsAssignableFromTypeAdapterFactoryClass() {
        TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertEquals(TestTypeAdapter.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_ThrowsIllegalArgumentException_WhenValueIsInvalid() {
        TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void testGetTypeAdapter_ReturnsNullSafeTypeAdapter() {
        TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertEquals(TestTypeAdapter.class, result.getClass());
    }

}