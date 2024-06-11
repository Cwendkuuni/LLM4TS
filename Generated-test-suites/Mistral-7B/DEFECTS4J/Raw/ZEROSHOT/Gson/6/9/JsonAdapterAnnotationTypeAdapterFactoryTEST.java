import com.google.gson.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private ConstructorConstructor constructorConstructor;

 @Before
 public void setup() {
 gson = new GsonBuilder().create();
 constructorConstructor = gson.getDelegateAdapterFactory().getConstructorCreator();
 }

 @Test
 public void testNullType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Object> adapter = factory.create(gson, new TypeToken<Object>() {});
 assertNull(adapter);
 }

 @Test
 public void testJsonAdapterAnnotationClassType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 Class<?> jsonAdapterClass = MyJsonAdapter.class;
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapter<String> stringAdapter = factory.create(gson, stringTypeToken);
 assertEquals(MyJsonAdapter.class, stringAdapter.getClass());
 }

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 Class<JsonAdapterFactory> jsonAdapterFactoryClass = JsonAdapterFactory.class;
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapterFactory stringAdapterFactory = factory.create(gson, stringTypeToken.getType());
 assertEquals(jsonAdapterFactoryClass, stringAdapterFactory.getClass());

 TypeAdapter<String> stringAdapter = stringAdapterFactory.create(gson, stringTypeToken);
 assertEquals(MyJsonStringAdapter.class, stringAdapter.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterAnnotationInvalidType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeToken<Object> objectTypeToken = new TypeToken<Object>() {};
 JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, objectTypeToken, null);
 }

 @Test
 public void testJsonAdapterAnnotationNullSafe() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 Class<MyNullSafeJsonAdapter> jsonAdapterClass = MyNullSafeJsonAdapter.class;
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 TypeAdapter<String> stringAdapter = factory.create(gson, stringTypeToken);
 assertNotNull(stringAdapter.isNullSafe());

 MyNullSafeJsonAdapter myNullSafeJsonAdapter = new MyNullSafeJsonAdapter();
 assertNotNull(stringAdapter.nullSafe());
 assertEquals(myNullSafeJsonAdapter, stringAdapter.nullSafe());
 }
}

class MyJsonAdapter {
 @JsonAdapter(MyJsonStringAdapter.class)
 public static class MyClass {
 String name;
 }
}

class MyJsonStringAdapter extends TypeAdapter<String> {
 @Override
 public String read(JsonReader in) throws IOException {
 return null;
 }

 @Override
 public void write(JsonWriter out, String value) throws IOException {
 out.value(value);
 }
}

class JsonAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
 return null;
 }
}

class MyNullSafeJsonAdapter extends TypeAdapter<String> {
 @Override
 public String read(JsonReader in) throws IOException {
 return null;
 }

 @Override
 public void write(JsonWriter out, String value) throws IOException {
 out.value(value);
 }

 @Override
 public Boolean isNullSafe() {
 return true;
 }
}