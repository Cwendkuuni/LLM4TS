import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private Gson gson;
    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        gson = new Gson();
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    public void create_ShouldReturnNull_WhenNoAnnotationPresent() {
        TypeToken<Object> targetType = TypeToken.get(Object.class);
        TypeAdapter<Object> result = factory.create(gson, targetType);
        Assert.assertNull(result);
    }

    @Test
    public void create_ShouldReturnCorrectTypeAdapter_WhenAnnotationPresentAndValueIsTypeAdapter() {
        TypeToken<TypeAdapterTestClass> targetType = TypeToken.get(TypeAdapterTestClass.class);
        TypeAdapter<TypeAdapterTestClass> result = factory.create(gson, targetType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TypeAdapterTestClass.TestTypeAdapter);
    }

    @Test
    public void create_ShouldReturnCorrectTypeAdapter_WhenAnnotationPresentAndValueIsTypeAdapterFactory() {
        TypeToken<TypeAdapterFactoryTestClass> targetType = TypeToken.get(TypeAdapterFactoryTestClass.class);
        TypeAdapter<TypeAdapterFactoryTestClass> result = factory.create(gson, targetType);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TypeAdapterFactoryTestClass.TestTypeAdapterFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_ShouldThrowException_WhenAnnotationPresentAndValueIsNotTypeAdapterOrTypeAdapterFactory() {
        TypeToken<InvalidAnnotationTestClass> targetType = TypeToken.get(InvalidAnnotationTestClass.class);
        factory.create(gson, targetType);
    }

    @JsonAdapter(TypeAdapterTestClass.TestTypeAdapter.class)
    private static class TypeAdapterTestClass {
        private String name;

        private static class TestTypeAdapter extends TypeAdapter<TypeAdapterTestClass> {
            // Implementation of TypeAdapter
        }
    }

    @JsonAdapter(TypeAdapterFactoryTestClass.TestTypeAdapterFactory.class)
    private static class TypeAdapterFactoryTestClass {
        private String name;

        private static class TestTypeAdapterFactory implements TypeAdapterFactory {
            // Implementation of TypeAdapterFactory
        }
    }

    @JsonAdapter(String.class)
    private static class InvalidAnnotationTestClass {
        private String name;
    }

}