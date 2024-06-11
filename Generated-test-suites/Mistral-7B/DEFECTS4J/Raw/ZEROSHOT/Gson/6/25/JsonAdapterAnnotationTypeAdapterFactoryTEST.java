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
 private JsonAdapterAnnotationTypeAdapterFactory factory;

 @Before
 public void setup() {
 gson = new Gson();
 constructorConstructor = gson.getFactory().constructorConstructor;
 factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testNullTypeToken() {
 TypeAdapter<?> adapter = factory.create(gson, null);
 Assert.assertNull(adapter);
 }

 @Test
 public void testNoJsonAdapterAnnotation() {
 Class<Integer> intType = Integer.TYPE;
 TypeAdapter<?> adapter = factory.create(gson, new TypeToken<Integer>() {}.where(new TypeToken<Class<Integer>>() {}).getType());
 Assert.assertNull(adapter);
 }

 @Test
 public void testJsonAdapterAsTypeAdapter() {
 class JsonAdapterAsTypeAdapter implements TypeAdapter<String> {
 @Override
 public String read(JsonReader jsonReader) throws IOException {
 return jsonReader.nextString();
 }

 @Override
 public void write(JsonWriter jsonWriter, String s) throws IOException {
 jsonWriter.value(s);
 }
 }

 JsonAdapter annotation = new JsonAdapterAsTypeAdapter().getClass().getAnnotation(JsonAdapter.class);
 Class<String> stringType = String.class;
 TypeAdapter<String> stringAdapter = factory.create(gson, new TypeToken<String>() {}.getType());
 Assert.assertEquals(JsonAdapterAsTypeAdapter.class, stringAdapter.getClass());
 }

 @Test
 public void testJsonAdapterAsTypeAdapterFactory() {
 class JsonAdapterAsTypeAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 return new TypeAdapter<T>() {
 @Override
 public T read(JsonReader jsonReader) throws IOException {
 return null;
 }

 @Override
 public void write(JsonWriter jsonWriter, T t) throws IOException {
 jsonWriter.value(null);
 }
 };
 }
 }

 JsonAdapter annotation = new JsonAdapterAsTypeAdapterFactory().getClass().getAnnotation(JsonAdapter.class);
 Class<String> stringType = String.class;
 TypeAdapter<String> stringAdapter = factory.create(gson, new TypeToken<String>() {}.getType());
 Assert.assertTrue(stringAdapter.getClass().getSuperclass() == JsonAdapterAsTypeAdapterFactory.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapter() {
 class InvalidJsonAdapterAnnotatedType {}
 JsonAdapter annotation = InvalidJsonAdapterAnnotatedType.class.getAnnotation(JsonAdapter.class);
 TypeAdapter<?> adapter = factory.create(gson, new TypeToken<InvalidJsonAdapterAnnotatedType>() {}.getType());
 }
}