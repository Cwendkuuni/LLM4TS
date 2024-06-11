import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreateWithJsonAdapterAnnotation() {
        // Given
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();

        // When
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeAdapter<TestObject> typeAdapter = factory.create(gson, TypeToken.get(TestObject.class));

        // Then
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateWithoutJsonAdapterAnnotation() {
        // Given
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();

        // When
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        TypeAdapter<TestObject> typeAdapter = factory.create(gson, TypeToken.get(NoAnnotationObject.class));

        // Then
        assertNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapterWithUnsupportedValue() {
        // Given
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return String.class;
            }

            @Override
            public Class<?> getValue() {
                return null;
            }

            @Override
            public Class<? extends TypeAdapter<?>> type() {
                return null;
            }
        };

        // When
        try {
            JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference."));
        }
    }

}