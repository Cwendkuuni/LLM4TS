import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setUp() {
        factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    }

    @Test
    public void create_WithJsonAdapterAnnotationAndTypeAdapterValue_ReturnsTypeAdapterInstance() {
        Gson gson = new Gson();
        TypeToken<TestModel> targetType = new TypeToken<TestModel>(){};
        TypeAdapter<TestModel> expectedAdapter = new CustomTypeAdapter();

        TypeAdapter<TestModel> actualAdapter = factory.create(gson, targetType);

        Assert.assertEquals(expectedAdapter, actualAdapter);
    }
    
    @Test
    public void create_WithJsonAdapterAnnotationAndTypeAdapterFactoryValue_ReturnsTypeAdapterInstance() {
        Gson gson = new Gson();
        TypeToken<TestModel> targetType = new TypeToken<TestModel>(){};
        TypeAdapter<TestModel> expectedAdapter = new CustomTypeAdapterFactory().create(gson, targetType);

        TypeAdapter<TestModel> actualAdapter = factory.create(gson, targetType);

        Assert.assertEquals(expectedAdapter, actualAdapter);
    }

    @Test
    public void getTypeAdapter_WithTypeAdapterValue_ReturnsTypeAdapterInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestModel> fieldType= new TypeToken<TestModel>(){};
        JsonAdapter annotation = new JsonAdapter(value = CustomTypeAdapter.class);
        TypeAdapter<TestModel> expectedAdapter = new CustomTypeAdapter().nullSafe();

        TypeAdapter<TestModel> actualAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        Assert.assertEquals(expectedAdapter, actualAdapter);
    }
    
    @Test
    public void getTypeAdapter_WithTypeAdapterFactoryValue_ReturnsTypeAdapterInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestModel> fieldType= new TypeToken<TestModel>(){};
        JsonAdapter annotation = new JsonAdapter(value = CustomTypeAdapterFactory.class);
        TypeAdapter<TestModel> expectedAdapter = new CustomTypeAdapterFactory().create(gson, fieldType).nullSafe();

        TypeAdapter<TestModel> actualAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        Assert.assertEquals(expectedAdapter, actualAdapter);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getTypeAdapter_WithUnknownValueType_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestModel> fieldType= new TypeToken<TestModel>(){};
        JsonAdapter annotation = new JsonAdapter(value = UnknownType.class);

        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    static class TestModel {
        // Test model class for testing purposes
    }

    static class CustomTypeAdapter extends TypeAdapter<TestModel> {
        // Custom type adapter implementation for testing purposes
    }

    static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            // Custom type adapter factory implementation for testing purposes
            return null;
        }
    }
}