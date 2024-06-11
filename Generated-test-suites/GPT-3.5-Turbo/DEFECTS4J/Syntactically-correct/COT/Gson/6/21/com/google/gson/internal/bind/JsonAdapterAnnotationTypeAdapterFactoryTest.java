import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_NoAnnotation_ReturnsNull() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        Gson gson = new Gson();
        TypeToken<String> targetType = TypeToken.get(String.class);

        TypeAdapter<String> adapter = factory.create(gson, targetType);

        assertNull(adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidValueInAnnotation_ThrowsIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        Gson gson = new Gson();
        TypeToken<Integer> targetType = TypeToken.get(Integer.class);

        factory.create(gson, targetType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidAdapterClass_ThrowsIllegalArgumentException() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        Gson gson = new Gson();
        TypeToken<Double> targetType = TypeToken.get(Double.class);

        factory.create(gson, targetType);
    }

    @Test
    public void testCreate_ValidAnnotation_ReturnsTypeAdapter() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        Gson gson = new Gson();
        TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);

        TypeAdapter<TestClass> adapter = factory.create(gson, targetType);

        assertNotNull(adapter);
    }

    // Test class used for the last test case
    @JsonAdapter(TestClassAdapter.class)
    private static class TestClass {
        // test fields
    }

    // Test adapter class used for the last test case
    private static class TestClassAdapter extends TypeAdapter<TestClass> {
        // test methods
    }
}