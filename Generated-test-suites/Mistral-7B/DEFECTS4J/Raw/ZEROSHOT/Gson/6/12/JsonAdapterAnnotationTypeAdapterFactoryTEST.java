import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Type;
import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private ConstructorConstructor constructorConstructor;

 @Before
 public void setUp() {
 gson = new GsonBuilder().setPrePreProcessor(new JsonPrePreProcessor()).create();
 constructorConstructor = gson.getDelegateAdapterFactory().getAdapter(ConstructorConstructor.class);
 }

 @Test
 public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapterInstance() {
 Class<Integer> intType = Integer.TYPE;
 Class<JsonAdapterAnnotationTypeAdapterFactoryTest.MyIntAdapter> intAdapter = JsonAdapterAnnotationTypeAdapterFactoryTest.MyIntAdapter.class;

 JsonAdapter jsonAdapterAnnotation = intType.getAnnotation(JsonAdapter.class);
 assertNotNull(jsonAdapterAnnotation);

 TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<Integer>() {}.getType());
 assertNotNull(typeAdapter);

 TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> typeAdapterFromFactory = typeAdapterFactory.create(gson, new TypeToken<Integer>() {}.getType());
 assertNotNull(typeAdapterFromFactory);

 Class<? extends TypeAdapter<?>> intAdapterClass = jsonAdapterAnnotation.value();
 TypeAdapter<Integer> typeAdapterFromAnnotation = (TypeAdapter<Integer>) typeAdapterFromFactory
 .create(gson, new TypeToken<Integer>() {}.getType());

 assertNotNull(typeAdapterFromAnnotation);
 assertNotNull(typeAdapterFromAnnotation.getClass().getSuperclass());
 assertNotNull(typeAdapterFromAnnotation.getClass().getGenericSuperclass());
 assertTypeEquals(intAdapterClass, typeAdapterFromAnnotation.getClass());
 assertSame(typeAdapter, typeAdapterFromFactory);
 assertSame(typeAdapter, typeAdapterFromAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_JsonAdapterAnnotationMissing_ReturnsNull() {
 Class<String> stringType = String.class;
 TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<String>() {}.getType());
 assertNull(typeAdapter);
 }

 @Test
 public void testCreate_JsonAdapterValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
 Class<Integer> intType = Integer.TYPE;
 Class<JsonAdapterAnnotationTypeAdapterFactoryTest.MyIntAdapter> intAdapter = JsonAdapterAnnotationTypeAdapterFactoryTest.MyIntAdapter.class;

 JsonAdapter jsonAdapterAnnotation = intType.getAnnotation(JsonAdapter.class);
 assertNotNull(jsonAdapterAnnotation);

 TypeAdapter<Integer> intAdapterInstance = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, intType);
 assertNotNull(intAdapterInstance);

 assertSame(intAdapter, intAdapterInstance.getClass().getDeclaredConstructor().newInstance());
 }

 @Test
 public void testCreate_JsonAdapterValueIsTypeAdapterFactory_ReturnsTypeAdapterInstance() {
 Class<Integer> intType = Integer.TYPE;
 Class<JsonAdapterAnnotationTypeAdapterFactoryTest.MyIntAdapterFactory> intAdapterFactory = JsonAdapterAnnotationTypeAdapterFactoryTest.MyIntAdapterFactory.class;

 JsonAdapter jsonAdapterAnnotation = intType.getAnnotation(JsonAdapter.class);
 assertNotNull(jsonAdapterAnnotation);

 TypeAdapterFactory intAdapterFactoryInstance = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, intType);
 TypeAdapter<Integer> intAdapter = intAdapterFactoryInstance.create(gson, intType);
 assertNotNull(intAdapter);

 assertSame(intAdapterFactory, intAdapterFactoryInstance.getClass().getDeclaredConstructor().newInstance());
 assertSame(intAdapter, intAdapterInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_JsonAdapterValueNotTypeAdapterOrTypeAdapterFactory_ThrowsException() {
 Class<Integer> intType = Integer.TYPE;
 Class<String> stringType = String.class;

 JsonAdapter jsonAdapterAnnotation = intType.getAnnotation(JsonAdapter.class);
 assertNotNull(jsonAdapterAnnotation);

 TypeAdapter<Integer> intAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, intType);
 assertNotNull(intAdapter);

 try {
 TypeAdapter<Integer> intAdapterFromString = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, stringType);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 private static void assertTypeEquals(Type expectedType, Type actualType) {
 if (!expectedType.getTypeName().equals(actualType.getTypeName())) {
 throw new AssertionError("Expected type " + expectedType + " but was " + actualType);
 }
 }

 private static void assertSame(Object expected, Object actual) {
 if (expected != actual) {
 throw new AssertionError("Expected " + expected + " but was " + actual);
 }
 }

 static class MyIntAdapter implements TypeAdapter<Integer> {
 // Empty implementation
 }

 static class MyIntAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
 return MyIntAdapter.class.newInstance();
 }
 }
}