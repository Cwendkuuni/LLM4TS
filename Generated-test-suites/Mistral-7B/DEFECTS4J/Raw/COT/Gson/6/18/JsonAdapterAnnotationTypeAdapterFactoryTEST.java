import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.create;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setup() {
 constructorConstructor = new ConstructorConstructor();
 gson = new GsonBuilder().setPrettyPrinting().create();
 }

 @Test
 public void testCreate_NoJsonAdapterAnnotation() {
 TypeAdapter<Integer> integerTypeAdapter = create(gson, new TypeToken<Integer>() {});
 Assert.assertNull(integerTypeAdapter);
 }

 @Test
 public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
 Integer[] ints = new Integer[] {1, 2, 3};
 JsonAdapterIntegerAdapter integerAdapter = new JsonAdapterIntegerAdapter();
 JsonAdapter<Integer> jsonAdapterAnnotation = integerAdapter.getClass().getAnnotation(JsonAdapter.class);
 TypeAdapter<Integer> integerTypeAdapter = create(gson, new TypeToken<Integer>() {});
 Assert.assertNotNull(integerTypeAdapter);
 Assert.assertEquals(integerAdapter.getClass(), integerTypeAdapter.getClass());
 }

 @Test
 public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
 Integer[] ints = new Integer[] {1, 2, 3};
 JsonAdapterIntegerFactory integerFactory = new JsonAdapterIntegerFactory();
 JsonAdapter<Integer> jsonAdapterAnnotation = integerFactory.getClass().getAnnotation(JsonAdapter.class);
 TypeAdapter<Integer> integerTypeAdapter = create(gson, new TypeToken<Integer>() {});
 Assert.assertNotNull(integerTypeAdapter);
 Assert.assertTrue(integerTypeAdapter instanceof JsonAdapterIntTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_JsonAdapterAnnotation_InvalidValue() {
 Integer[] ints = new Integer[] {1, 2, 3};
 JsonAdapterStringAdapter stringAdapter = new JsonAdapterStringAdapter();
 JsonAdapter<Integer> jsonAdapterAnnotation = stringAdapter.getClass().getAnnotation(JsonAdapter.class);
 create(gson, new TypeToken<Integer>() {});
 }

 @Test
 public void testGetTypeAdapter_TypeAdapter() {
 TypeAdapter<Integer> integerTypeAdapter = constructorConstructor.get(TypeAdapter.class);
 TypeAdapter<Integer> integerTypeAdapterFromAnnotation = getTypeAdapter(constructorConstructor, gson, new TypeToken<Integer>() {}, new JsonAdapter<Integer>() {});
 Assert.assertEquals(integerTypeAdapter, integerTypeAdapterFromAnnotation);
 }

 @Test
 public void testGetTypeAdapter_TypeAdapterFactory() {
 TypeAdapter<Integer> integerTypeAdapter = constructorConstructor.get(JsonAdapterIntegerFactory.class);
 TypeAdapter<Integer> integerTypeAdapterFromAnnotation = getTypeAdapter(constructorConstructor, gson, new TypeToken<Integer>() {}, new JsonAdapter<Integer>() {});
 Assert.assertNotSame(integerTypeAdapter, integerTypeAdapterFromAnnotation);
 Assert.assertTrue(integerTypeAdapter instanceof JsonAdapterIntTypeAdapter);
 Assert.assertTrue(integerTypeAdapterFromAnnotation instanceof JsonAdapterIntTypeAdapter);
 }
}

class JsonAdapterIntegerAdapter implements TypeAdapter<Integer> {
 // Empty implementation for testing.
}

class JsonAdapterIntegerFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 return new JsonAdapterIntTypeAdapter();
 }
}

class JsonAdapterIntTypeAdapter extends JsonAdapter<Integer> implements TypeAdapter<Integer> {
 // Empty implementation for testing.
}

class JsonAdapterStringAdapter implements TypeAdapter<String> {
 // Empty implementation for testing.
}