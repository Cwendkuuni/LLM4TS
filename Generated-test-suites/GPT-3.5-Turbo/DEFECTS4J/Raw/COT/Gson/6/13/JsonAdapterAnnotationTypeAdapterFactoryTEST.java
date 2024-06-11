import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
    
    @Test
    public void testCreate_JsonAdapterAnnotationNull_ReturnsNull() {
        Gson gson = new Gson();
        TypeToken<String> targetType = new TypeToken<String>() {};
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        
        TypeAdapter<String> result = factory.create(gson, targetType);
        
        assertNull(result);
    }
    
    @Test
    public void testCreate_JsonAdapterAnnotationTypeAdapter_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<TestClass> targetType = new TypeToken<TestClass>() {};
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        
        TypeAdapter<TestClass> result = factory.create(gson, targetType);
        
        assertNotNull(result);
        assertTrue(result instanceof TestAdapter);
    }
    
    @Test
    public void testCreate_JsonAdapterAnnotationTypeAdapterFactory_ReturnsTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<AnotherTestClass> targetType = new TypeToken<AnotherTestClass>() {};
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        
        TypeAdapter<AnotherTestClass> result = factory.create(gson, targetType);
        
        assertNotNull(result);
        assertTrue(result instanceof TestAdapterFactory.TestAdapter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        Gson gson = new Gson();
        TypeToken<InvalidTestClass> targetType = new TypeToken<InvalidTestClass>() {};
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        
        factory.create(gson, targetType);
    }
    
    @Test
    public void testGetTypeAdapter_TypeAdapter_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return TestAdapter.class;
            }
        };
        
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertNotNull(result);
        assertTrue(result instanceof TestAdapter);
    }
    
    @Test
    public void testGetTypeAdapter_TypeAdapterFactory_ReturnsTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return TestAdapterFactory.class;
            }
        };
        
        TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        assertNotNull(result);
        assertTrue(result instanceof TestAdapterFactory.TestAdapter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Boolean> fieldType = new TypeToken<Boolean>() {};
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return Void.class;
            }
        };
        
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}