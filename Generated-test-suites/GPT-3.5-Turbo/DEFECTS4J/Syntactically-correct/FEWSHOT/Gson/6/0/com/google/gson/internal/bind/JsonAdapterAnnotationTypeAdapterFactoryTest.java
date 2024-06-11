import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    @Test
    public void testCreate() {
        // Test case when JsonAdapter annotation is not present
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);
        assertNull(JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType));

        // Test case when JsonAdapter annotation is present
        targetType = TypeToken.get(MyClass.class);
        TypeAdapter<MyClass> expectedAdapter = new MyTypeAdapter();
        TypeAdapter<MyClass> actualAdapter = JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
        assertEquals(expectedAdapter.getClass(), actualAdapter.getClass());
    }

    @Test
    public void testGetTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();

        // Test case when value is TypeAdapter
        Gson gson = new Gson();
        TypeToken<?> fieldType = TypeToken.get(String.class);
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        TypeAdapter<?> expectedAdapter = new MyTypeAdapter();
        TypeAdapter<?> actualAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertEquals(expectedAdapter.getClass(), actualAdapter.getClass());

        // Test case when value is TypeAdapterFactory
        fieldType = TypeToken.get(MyClass.class);
        annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        expectedAdapter = new MyTypeAdapterFactory().create(gson, fieldType);
        actualAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertEquals(expectedAdapter.getClass(), actualAdapter.getClass());

        // Test case when value is neither TypeAdapter nor TypeAdapterFactory
        fieldType = TypeToken.get(Double.class);
        annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        try {
            JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
        }
    }
}

class MyClass {
    // Test class
}

class MyTypeAdapter extends TypeAdapter<MyClass> {
    // Test TypeAdapter implementation
}

class MyTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        return null;
    }
}