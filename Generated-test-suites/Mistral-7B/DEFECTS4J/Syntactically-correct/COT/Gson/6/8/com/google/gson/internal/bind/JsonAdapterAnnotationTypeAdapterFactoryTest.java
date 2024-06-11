import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setup() {
 constructorConstructor = new ConstructorConstructor();
 gson = new Gson();
 }

 @Test
 public void testCreate_NullTypeToken() {
 Assert.assertNull(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
     .create(gson, null));
 }

 @Test
 public void testCreate_NoJsonAdapterAnnotation() {
 TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};
 Assert.assertNull(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
     .create(gson, integerTypeToken));
 }

 @Test
 public void testCreate_JsonAdapterAnnotationWithTypeAdapter() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 String expected = "test";
 TypeAdapter<String> typeAdapter = new TypeAdapter<String>() {
 @Override
 public String read(JsonReader in) throws IOException {
 return expected;
 }

 @Override
 public void write(JsonWriter out, String value) throws IOException {
 out.value(value);
 }
 };
 JsonAdapter annotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
 TypeAdapter<?> actual = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
     .create(gson, stringTypeToken);
 Assert.assertEquals(typeAdapter, actual);
 }

 @Test
 public void testCreate_JsonAdapterAnnotationWithTypeAdapterFactory() {
 TypeToken<MyClass> myClassTypeToken = new TypeToken<MyClass>() {};
 MyClassTypeAdapterFactory myTypeAdapterFactory = new MyClassTypeAdapterFactory();
 JsonAdapter annotation = myClassTypeToken.getRawType().getAnnotation(JsonAdapter.class);
 TypeAdapter<MyClass> actual = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
     .create(gson, myClassTypeToken);
 TypeAdapter<MyClass> expected = myTypeAdapterFactory.create(gson, myClassTypeToken);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_JsonAdapterAnnotationWithInvalidValue() {
 TypeToken<Double> doubleTypeToken = new TypeToken<Double>() {};
 JsonAdapter annotation = doubleTypeToken.getRawType().getAnnotation(JsonAdapter.class);
 annotation.value().getClass(); // Trigger exception
 }

 @Test
 public void testNullSafe() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
     .create(gson, stringTypeToken).nullSafe();
 Assert.assertNotNull(typeAdapter);
 }
}

class MyClassTypeAdapterFactory implements TypeAdapterFactory {
 @SuppressWarnings("unchecked")
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 return (TypeAdapter<T>) new MyClassTypeAdapter<>();
 }
}

class MyClassTypeAdapter implements TypeAdapter<MyClass> {
 // Implement MyClass type adapter logic here
}