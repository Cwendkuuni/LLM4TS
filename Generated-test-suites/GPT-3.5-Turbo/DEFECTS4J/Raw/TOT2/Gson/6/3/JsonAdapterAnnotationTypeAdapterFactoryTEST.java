import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @Before
    public void setUp() {
        factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    }

    @Test
    public void testCreateWithTypeAdapterAnnotation() {
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        TypeAdapter<String> typeAdapter = factory.create(gson, typeToken);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateWithTypeAdapterFactoryAnnotation() {
        Gson gson = new Gson();
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        TypeAdapter<Integer> typeAdapter = factory.create(gson, typeToken);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateWithNoAnnotation() {
        Gson gson = new Gson();
        TypeToken<Double> typeToken = TypeToken.get(Double.class);
        TypeAdapter<Double> typeAdapter = factory.create(gson, typeToken);
        assertNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidAnnotation() {
        Gson gson = new Gson();
        TypeToken<Boolean> typeToken = TypeToken.get(Boolean.class);
        factory.create(gson, typeToken);
    }

    @Test
    public void testGetTypeAdapterWithTypeAdapterAnnotation() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        com.google.gson.annotations.JsonAdapter annotation = String.class.getAnnotation(com.google.gson.annotations.JsonAdapter.class);
        TypeAdapter<String> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapterWithTypeAdapterFactoryAnnotation() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        com.google.gson.annotations.JsonAdapter annotation = Integer.class.getAnnotation(com.google.gson.annotations.JsonAdapter.class);
        TypeAdapter<Integer> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);
        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterWithInvalidAnnotation() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<Boolean> typeToken = TypeToken.get(Boolean.class);
        com.google.gson.annotations.JsonAdapter annotation = Boolean.class.getAnnotation(com.google.gson.annotations.JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation);
    }
}