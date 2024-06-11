import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = new Gson();
    }

    @Test
    public void create_WithAnnotation_ReturnsTypeAdapter() {
        TypeToken<MockClass> targetType = TypeToken.get(MockClass.class);
        TypeAdapter<?> typeAdapter = factory.create(gson, targetType);
        
        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
    }

    @Test
    public void create_WithoutAnnotation_ReturnsNull() {
        TypeToken<NoAnnotationClass> targetType = TypeToken.get(NoAnnotationClass.class);
        TypeAdapter<?> typeAdapter = factory.create(gson, targetType);

        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_WithInvalidAnnotationValue_ThrowsIllegalArgumentException() {
        TypeToken<InvalidAnnotationValueClass> fieldType = TypeToken.get(InvalidAnnotationValueClass.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter> value() {
                return String.class; // Invalid type
            }

            @Override
            public Class<?>[] classes() { return new Class[0]; }

            @Override
            public Class<? extends TypeAdapterFactory> type() { return null; }

            @Override
            public Class<?>[] classes2() { return new Class[0]; }

            @Override
            public Class<?>[] classes3() { return new Class[0]; }

            @Override
            public Class<?>[] classes4() { return new Class[0]; }

            @Override
            public Class<?>[] classes5() { return new Class[0]; }

            @Override
            public Class<?>[] classes6() { return new Class[0]; }

            @Override
            public Class<?>[] classes7() { return new Class[0]; }
        };

        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void getTypeAdapter_WithTypeAdapterClass_ReturnsTypeAdapter() {
        TypeToken<TypeAdapterClassClass> fieldType = TypeToken.get(TypeAdapterClassClass.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter> value() {
                return MockTypeAdapter.class;
            }

            @Override
            public Class<?>[] classes() { return new Class[0]; }

            @Override
            public Class<? extends TypeAdapterFactory> type() { return null; }

            @Override
            public Class<?>[] classes2() { return new Class[0]; }

            @Override
            public Class<?>[] classes3() { return new Class[0]; }

            @Override
            public Class<?>[] classes4() { return new Class[0]; }

            @Override
            public Class<?>[] classes5() { return new Class[0]; }

            @Override
            public Class<?>[] classes6() { return new Class[0]; }

            @Override
            public Class<?>[] classes7() { return new Class[0]; }
        };

        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
        assertEquals(MockTypeAdapter.class, typeAdapter.getClass());
    }

    @Test
    public void getTypeAdapter_WithTypeAdapterFactoryClass_ReturnsTypeAdapter() {
        TypeToken<TypeAdapterFactoryClass> fieldType = TypeToken.get(TypeAdapterFactoryClass.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter> value() { return null; }

            @Override
            public Class<?>[] classes() { return new Class[0]; }

            @Override
            public Class<? extends TypeAdapterFactory> type() {
                return MockTypeAdapterFactory.class;
            }

            @Override
            public Class<?>[] classes2() { return new Class[0]; }

            @Override
            public Class<?>[] classes3() { return new Class[0]; }

            @Override
            public Class<?>[] classes4() { return new Class[0]; }

            @Override
            public Class<?>[] classes5() { return new Class[0]; }

            @Override
            public Class<?>[] classes6() { return new Class[0]; }

            @Override
            public Class<?>[] classes7() { return new Class[0]; }
        };

        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        assertNotNull(typeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapter);
        assertTrue(typeAdapter instanceof TypeAdapterFactory);
        assertEquals(MockTypeAdapterFactory.class, typeAdapter.getClass());
    }

    @Test
    public void getTypeAdapter_WithUnsupportedType_ThrowsIllegalArgumentException() {
        TypeToken<UnsupportedTypeClass> fieldType = TypeToken.get(UnsupportedTypeClass.class);
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends TypeAdapter> value() { return null; }

            @Override
            public Class<?>[] classes() { return new Class[0]; }

            @Override
            public Class<? extends TypeAdapterFactory> type() { return null; }

            @Override
            public Class<?>[] classes2() { return new Class[0]; }

            @Override
            public Class<?>[] classes3() { return new Class[0]; }

            @Override
            public Class<?>[] classes4() { return new Class[0]; }

            @Override
            public Class<?>[] classes5() { return new Class[0]; }

            @Override
            public Class<?>[] classes6() { return new Class[0]; }

            @Override
            public Class<?>[] classes7() { return new Class[0]; }
        };

        try {
            factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
        }
    }
}

class MockClass {}
class NoAnnotationClass {}
class InvalidAnnotationValueClass {}
class TypeAdapterClassClass {}
class TypeAdapterFactoryClass {}
class UnsupportedTypeClass {}

class MockTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, String value) { }

    @Override
    public String read(com.google.gson.stream.JsonReader in) { return ""; }
}

class MockTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        return null;
    }
}