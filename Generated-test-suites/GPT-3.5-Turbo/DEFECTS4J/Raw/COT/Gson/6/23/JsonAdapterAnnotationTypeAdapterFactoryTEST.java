
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);

        TypeAdapter<String> adapter = factory.create(gson, targetType);

        assertNull(adapter);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TestObject> targetType = TypeToken.get(TestObject.class);

        TypeAdapter<TestObject> adapter = factory.create(gson, targetType);

        assertNotNull(adapter);
    }

    @Test
    public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<TestObject> targetType = TypeToken.get(TestObject.class);

        TypeAdapter<TestObject> adapter = factory.create(gson, targetType);

        assertNotNull(adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidAnnotation_ThrowsIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        Gson gson = new Gson();
        TypeToken<InvalidTestObject> targetType = TypeToken.get(InvalidTestObject.class);

        factory.create(gson, targetType);
    }

    private class TestObject {
        // Test class
    }

    private class InvalidTestObject {
        // Test class
    }
}
