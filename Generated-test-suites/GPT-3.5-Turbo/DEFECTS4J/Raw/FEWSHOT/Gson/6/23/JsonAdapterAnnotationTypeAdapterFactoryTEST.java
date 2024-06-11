import static org.junit.Assert.*;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate_WithAnnotation() {
        // create a Gson instance
        Gson gson = new Gson();
        
        // create a JsonAdapterAnnotationTypeAdapterFactory instance
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        
        // create a TypeToken with annotation
        TypeToken<TestObject> targetType = new TypeToken<TestObject>() {
        };
        
        // invoke the create method
        TypeAdapter<TestObject> adapter = factory.create(gson, targetType);
        
        // assert that the returned adapter is not null
        assertNotNull(adapter);
    }

    @Test
    public void testCreate_WithoutAnnotation() {
        // create a Gson instance
        Gson gson = new Gson();
        
        // create a JsonAdapterAnnotationTypeAdapterFactory instance
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        
        // create a TypeToken without annotation
        TypeToken<String> targetType = new TypeToken<String>() {
        };
        
        // invoke the create method
        TypeAdapter<String> adapter = factory.create(gson, targetType);
        
        // assert that the returned adapter is null
        assertNull(adapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass() {
        // create a ConstructorConstructor instance
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        // create a Gson instance
        Gson gson = new Gson();
        
        // create a fieldType TypeToken
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {
        };
        
        // create a JsonAdapter annotation
        JsonAdapter annotation = TestObject.class.getAnnotation(JsonAdapter.class);
        
        // invoke the getTypeAdapter method
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        // assert that the returned adapter is not null
        assertNotNull(adapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactory() {
        // create a ConstructorConstructor instance
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        // create a Gson instance
        Gson gson = new Gson();
        
        // create a fieldType TypeToken
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {
        };
        
        // create a JsonAdapter annotation
        JsonAdapter annotation = TestObjectFactory.class.getAnnotation(JsonAdapter.class);
        
        // invoke the getTypeAdapter method
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        // assert that the returned adapter is not null
        assertNotNull(adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotation() {
        // create a ConstructorConstructor instance
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        // create a Gson instance
        Gson gson = new Gson();
        
        // create a fieldType TypeToken
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {
        };
        
        // create an invalid JsonAdapter annotation
        JsonAdapter annotation = InvalidTestObject.class.getAnnotation(JsonAdapter.class);
        
        // invoke the getTypeAdapter method
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        // an exception should be thrown due to the invalid annotation
    }

    @Test
    public void testTypeAdapterNullSafe() {
        // create a ConstructorConstructor instance
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        // create a Gson instance
        Gson gson = new Gson();
        
        // create a fieldType TypeToken
        TypeToken<TestObject> fieldType = new TypeToken<TestObject>() {
        };
        
        // create a JsonAdapter annotation
        JsonAdapter annotation = TestObject.class.getAnnotation(JsonAdapter.class);
        
        // invoke the getTypeAdapter method
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        
        // assert that the returned adapter is not null
        assertNotNull(adapter);
        
        // invoke the nullSafe method
        TypeAdapter<?> nullSafeAdapter = adapter.nullSafe();
        
        // assert that the nullSafe method returns a new adapter
        assertNotSame(adapter, nullSafeAdapter);
    }
}