import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private ConstructorConstructor constructorConstructor;
 private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

 @Before
 public void setup() {
 gson = new GsonBuilder().setPrePreProcessor(new TypeAdapterFactory() {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
 return new IdentityTypeAdapter<T>() {};
 }
 }).create();
 constructorConstructor = gson.getDelegateAdapterFactory().getConstructorAdapter();
 jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testNullType() {
 TypeToken<Object> nullType = new TypeToken<Object>() {};
 TypeAdapter<Object> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, nullType);
 assertNull(typeAdapter);
 }

 @Test
 public void testNoJsonAdapterAnnotation() {
 int type = 0;
 TypeToken<Integer> intType = new TypeToken<Integer>() {}.where(new TypeNameFilter() {
 @Override
 public boolean filter(TypeToken<?> typeToken, Type type, Class<?> clazz) {
 return clazz.isPrimitive() && typeToken.getType() == type;
 }
 });
 TypeAdapter<Integer> intTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, intType);
 assertNull(intTypeAdapter);
 }

 @Test
 public void testJsonAdapterAnnotationOnClass() {
 @JsonAdapter(MyIntJsonAdapter.class)
 class MyIntClass {}
 TypeToken<MyIntClass> myIntClassType = new TypeToken<MyIntClass>() {};
 TypeAdapter<MyIntClass> myIntClassTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, myIntClassType);
 assertEquals(MyIntJsonAdapter.class, myIntClassTypeAdapter.getClass());
 }

 @Test
 public void testJsonAdapterAnnotationOnFactory() {
 class MyIntJsonAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
 return new IdentityTypeAdapter<T>() {};
 }
 }
 @JsonAdapter(MyIntJsonAdapterFactory.class)
 class MyIntClass {}
 TypeToken<MyIntClass> myIntClassType = new TypeToken<MyIntClass>() {};
 TypeAdapter<MyIntClass> myIntClassTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, myIntClassType);
 assertNotNull(myIntClassTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterAnnotationValueNotTypeAdapterOrFactory() {
 class MyIntClass {}
 @JsonAdapter(String.class)
 class MyIntClassWithInvalidJsonAdapterAnnotation {}
 TypeToken<MyIntClass> myIntClassType = new TypeToken<MyIntClass>() {};
 jsonAdapterAnnotationTypeAdapterFactory.create(gson, myIntClassType);
 }
}

class MyIntJsonAdapter implements TypeAdapter<Integer> {}
 class IdentityTypeAdapter<T> implements TypeAdapter<T> {}