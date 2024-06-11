import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory typeAdapterFactory;

 @Before
 public void setUp() {
 gson = new GsonBuilder().create();
 typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor(gson));
 }

 @Test
 public void testNullType() {
 assertNull(typeAdapterFactory.create(gson, null));
 }

 @Test
 public void testStringTypeWithJsonAdapterAnnotation() {
 String jsonString = "{\"name\": \"John\", \"age\": 30}";
 Type StringType = String.class.getRawType();
 TypeToken<String> stringToken = new TypeToken<String>() {};
 JsonAdapter annotation = StringType.getAnnotation(JsonAdapter.class);
 TypeAdapter<String> stringAdapter = getTypeAdapter(typeAdapterFactory, gson, stringToken, annotation);

 assertEquals("John", stringAdapter.fromJson(jsonString));
 assertEquals("John", stringAdapter.toJson(new String("John")));
 }

 @Test
 public void testIntegerTypeWithJsonAdapterAnnotation() {
 String jsonString = "{\"number\": 42}";
 Type IntegerType = Integer.class;
 TypeToken<Integer> integerToken = new TypeToken<Integer>() {};
 JsonAdapter annotation = IntegerType.getAnnotation(JsonAdapter.class);
 TypeAdapter<Integer> integerAdapter = getTypeAdapter(typeAdapterFactory, gson, integerToken, annotation);

 assertEquals(42, integerAdapter.fromJson(jsonString));
 assertEquals(42, integerAdapter.toJson(42));
 }

 @Test
 public void testCustomTypeAdapter() {
 class CustomTypeAdapter implements TypeAdapter<MyClass> {
 @Override
 public MyClass fromJson(JsonElement jsonElement) throws JsonParseException {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement toJson(MyClass myClass) throws JsonSerializationException {
 throw new UnsupportedOperationException();
 }
 }

 class MyClass {
 String name;
 }

 Type MyClassType = MyClass.class;
 TypeToken<MyClass> myClassToken = new TypeToken<MyClass>() {};
 JsonAdapter myClassAdapterAnnotation = MyClassType.getAnnotation(JsonAdapter.class);
 TypeAdapter<MyClass> myClassAdapter = new CustomTypeAdapter();
 TypeAdapterFactory wrapper = new TypeAdapterFactory() {
 @SuppressWarnings("unchecked")
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 return (TypeAdapter<T>) myClassAdapter;
 }
 };
 TypeAdapterFactory factoryWithCustomAdapter = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor(gson)) {
 @Override
 public TypeAdapter<MyClass> create(Gson gson, TypeToken<MyClass> targetType) {
 return wrapper.create(gson, targetType);
 }
 };
 TypeAdapter<MyClass> myClassAdapterFromFactory = factoryWithCustomAdapter.create(gson, myClassToken);

 assertSame(myClassAdapter, myClassAdapterFromFactory);
 assertNull(myClassAdapterFromFactory.fromJson(jsonString));
 assertNull(myClassAdapterFromFactory.toJson(new MyClass()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapterAnnotation() {
 String jsonString = "{\"name\": \"John\", \"age\": 30}";
 TypeStringType = String.class;
 TypeToken<String> stringToken = new TypeToken<String>() {};
 JsonAdapter annotation = TypeStringType.getAnnotation(JsonAdapter.class);
 annotation.value() = new Object() {};
 TypeAdapter<String> stringAdapter = getTypeAdapter(typeAdapterFactory, gson, stringToken, annotation);
 }
}