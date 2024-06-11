import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
       
    @Test
    public void create_Test1() {
        // Typical Use Case
        Gson gson = new Gson();
        TypeToken<String> targetType = new TypeToken<String>() {};
        TypeAdapter<String> expectedTypeAdapter = gson.getAdapter(targetType);
        JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);
        
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
        
        assertNotNull(actualTypeAdapter);
        assertEquals(expectedTypeAdapter.getClass(), actualTypeAdapter.getClass());
    }
    
    @Test
    public void create_Test2() {
        // Edge Case: Missing @JsonAdapter annotation
        Gson gson = new Gson();
        TypeToken<Integer> targetType = new TypeToken<Integer>() {};
        
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        TypeAdapter<Integer> actualTypeAdapter = factory.create(gson, targetType);
        
        assertNull(actualTypeAdapter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_Test1() {
        // Error Scenario: Invalid @JsonAdapter value, must be TypeAdapter or TypeAdapterFactory reference
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
        JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
        
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

}