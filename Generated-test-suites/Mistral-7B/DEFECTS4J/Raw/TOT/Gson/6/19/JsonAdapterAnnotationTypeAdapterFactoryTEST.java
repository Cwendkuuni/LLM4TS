import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private static final Gson gson = new GsonBuilder().create();

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_TypeAdapter() {
 TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeAdapter<String> stringTypeAdapter = factory.create(gson, new TypeToken<String>() {});
 assertNotNull(stringTypeAdapter);
 }

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_TypeAdapterFactory() {
 TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeAdapter<String> stringTypeAdapter = factory.create(gson, new TypeToken<String>() {});
 assertNotNull(stringTypeAdapter);

 TypeAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor()) {
 @SuppressWarnings("unchecked")
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 if (targetType.getType().equals(String.class)) {
 return (TypeAdapter<T>) new StringSerializerAdapter();
 }
 return null;
 }
 };
 TypeAdapter<String> stringTypeAdapter2 = factory.create(gson, new TypeToken<String>() {});
 assertNotNull(stringTypeAdapter2);
 assertNotSame(stringTypeAdapter, stringTypeAdapter2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterAnnotationTypeAdapterFactory_InvalidValue() {
 TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeAdapter<Integer> intTypeAdapter = factory.create(gson, new TypeToken<Integer>() {});
 fail();
 }

 @JsonAdapter(MyJsonAdapter.class)
 static class MyClass {}

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_MyClass() {
 TypeAdapter<MyClass> myClassTypeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor())
 .create(gson, new TypeToken<MyClass>() {});
 assertNotNull(myClassTypeAdapter);
 }

 static class MyJsonAdapter extends TypeAdapter<MyClass> {
 @Override
 public MyClass read(JsonElement jsonElement) throws JsonParseException {
 fail();
 }

 @Override
 public JsonElement write(JsonElement jsonElement, MyClass myClass) throws JsonSerializationException {
 fail();
 }
 }
}