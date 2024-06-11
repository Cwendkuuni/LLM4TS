import org.junit.Assert;
import org.junit.Test;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;

 @Test
 public void testCreate_NoJsonAdapterAnnotation() {
 Gson gson = new Gson();
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> integerTypeAdapter = adapterFactory.create(gson, new TypeToken<Integer>() {}.getType());
 Assert.assertNull(integerTypeAdapter);
 }

 @Test
 public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<MyJsonAdapter> jsonAdapterClass = MyJsonAdapter.class;
 JsonAdapter annotation = jsonAdapterClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.create(gson, new TypeToken<String>() {}.getType());
 Assert.assertNotNull(stringTypeAdapter);
 Assert.assertTrue(stringTypeAdapter instanceof MyJsonAdapter);
 }

 @Test
 public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<MyJsonAdapterFactory> jsonAdapterFactoryClass = MyJsonAdapterFactory.class;
 JsonAdapter annotation = jsonAdapterFactoryClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.create(gson, new TypeToken<String>() {}.getType());
 Assert.assertNotNull(stringTypeAdapter);
 TypeAdapter<Number> numberTypeAdapter = adapterFactory.create(gson, new TypeToken<Number>() {}.getType());
 Assert.assertNotNull(numberTypeAdapter);
 Assert.assertTrue(numberTypeAdapter instanceof MyJsonAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_JsonAdapterAnnotation_InvalidType() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<String> stringClass = String.class;
 JsonAdapter annotation = stringClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.create(gson, new TypeToken<String>() {}.getType());
 Assert.assertNotNull(stringTypeAdapter);
 TypeAdapter<Integer> integerTypeAdapter = adapterFactory.create(gson, new TypeToken<Integer>() {}.getType());
 Assert.assertNull(integerTypeAdapter);
 TypeAdapter<?> invalidTypeAdapter = adapterFactory.create(gson, new TypeToken<Object>() {}.getType());
 }

 @Test
 public void testGetTypeAdapter_TypeAdapter() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<MyJsonAdapter> jsonAdapterClass = MyJsonAdapter.class;
 JsonAdapter annotation = jsonAdapterClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}.getType(), annotation);
 Assert.assertNotNull(stringTypeAdapter);
 Assert.assertTrue(stringTypeAdapter instanceof MyJsonAdapter);
 }

 @Test
 public void testGetTypeAdapter_TypeAdapterFactory() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<MyJsonAdapterFactory> jsonAdapterFactoryClass = MyJsonAdapterFactory.class;
 JsonAdapter annotation = jsonAdapterFactoryClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}.getType(), annotation);
 Assert.assertNotNull(stringTypeAdapter);
 TypeAdapter<Number> numberTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Number>() {}.getType(), annotation);
 Assert.assertNotNull(numberTypeAdapter);
 Assert.assertTrue(numberTypeAdapter instanceof MyJsonAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetTypeAdapter_InvalidJsonAdapterAnnotation() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<String> stringClass = String.class;
 JsonAdapter annotation = stringClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<?> invalidTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Object>() {}.getType(), annotation);
 }

 @Test
 public void testNullSafe_TypeAdapter() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<MyJsonAdapter> jsonAdapterClass = MyJsonAdapter.class;
 JsonAdapter annotation = jsonAdapterClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}.getType(), annotation);
 TypeAdapter<String> nullSafeStringTypeAdapter = stringTypeAdapter.nullSafe();
 Assert.assertNotNull(nullSafeStringTypeAdapter);
 }

 @Test
 public void testNullSafe_TypeAdapterFactory() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> factoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<MyJsonAdapterFactory> jsonAdapterFactoryClass = MyJsonAdapterFactory.class;
 JsonAdapter annotation = jsonAdapterFactoryClass.getAnnotation(JsonAdapter.class);
 Gson gson = new Gson();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson.getAdapterFactory());
 JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> stringTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}.getType(), annotation);
 TypeAdapter<String> nullSafeStringTypeAdapter = stringTypeAdapter.nullSafe();
 Assert.assertNotNull(nullSafeStringTypeAdapter);
 TypeAdapter<Number> numberTypeAdapter = adapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Number>() {}.getType(), annotation);
 TypeAdapter<Number> nullSafeNumberTypeAdapter = numberTypeAdapter.nullSafe();
 Assert.assertNotNull(nullSafeNumberTypeAdapter);
 }
}

class MyJsonAdapter {}
class MyJsonAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 return null;
 }
}