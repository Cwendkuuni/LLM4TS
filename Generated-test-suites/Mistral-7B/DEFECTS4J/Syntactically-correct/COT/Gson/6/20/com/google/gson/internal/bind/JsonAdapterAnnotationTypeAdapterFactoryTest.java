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
 private ConstructorConstructor constructorConstructor;
 private JsonAdapterAnnotationTypeAdapterFactory typeAdapterFactory;

 @Before
 public void setUp() {
 gson = new Gson();
 constructorConstructor = gson.getDelegateAdapterFactory().getConstructorConstructor();
 typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testCreateNullTargetType() {
 Assert.assertNull(typeAdapterFactory.create(gson, null));
 }

 @Test
 public void testCreateNoJsonAdapterAnnotation() {
 TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
 Assert.assertNull(typeAdapterFactory.create(gson, intTypeToken));
 }

 @Test
 public void testCreateTypeAdapter() {
 Class<TypeAdapter<String>> stringTypeAdapterClass = TypeAdapter.class;
 JsonAdapter stringJsonAdapterAnnotation = stringTypeAdapterClass.getAnnotation(JsonAdapter.class);
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapter<String> expectedTypeAdapter = gson.getDelegateAdapterFactory().getAdapter(String.class);
 TypeAdapter<String> actualTypeAdapter = typeAdapterFactory.create(gson, stringTypeToken);
 Assert.assertEquals(expectedTypeAdapter, actualTypeAdapter);
 }

 @Test
 public void testCreateTypeAdapterFactory() {
 Class<TypeAdapterFactory> stringTypeAdapterFactoryClass = TypeAdapterFactory.class;
 JsonAdapter stringJsonAdapterAnnotation = stringTypeAdapterClass.getAnnotation(JsonAdapter.class);
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapterFactory stringTypeAdapterFactory = new GsonBuilder().registerTypeAdapterFactory(new StringTypeAdapterFactory()).create();
 TypeAdapter<String> expectedTypeAdapter = gson.getDelegateAdapterFactory().getAdapter(String.class);
 TypeAdapter<String> actualTypeAdapter = typeAdapterFactory.create(gson, stringTypeToken);
 Assert.assertEquals(expectedTypeAdapter, actualTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateInvalidJsonAdapterValue() {
 Class<String> stringClass = String.class;
 JsonAdapter stringJsonAdapterAnnotation = new JsonAdapter() {};
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 typeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, stringJsonAdapterAnnotation);
 }

 @Test
 public void testCreateNullSafeTypeAdapter() {
 Class<TypeAdapter<String>> stringTypeAdapterClass = TypeAdapter.class;
 JsonAdapter stringJsonAdapterAnnotation = stringTypeAdapterClass.getAnnotation(JsonAdapter.class);
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapter<String> typeAdapter = typeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, stringJsonAdapterAnnotation);
 TypeAdapter<String> nullSafeTypeAdapter = typeAdapter.nullSafe();
 Assert.assertNotNull(nullSafeTypeAdapter);
 }
}