import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private ConstructorConstructor constructorConstructor;

 @Before
 public void setup() {
 gson = new Gson();
 constructorConstructor = gson.getDelegateAdapterFactory().getConstructorConstructor();
 }

 @Test
 public void testCreate_NullTypeToken() {
 Assert.assertNull(JsonAdapterAnnotationTypeAdapterFactory.create(gson, null));
 }

 @Test
 public void testCreate_NoJsonAdapterAnnotation() {
 TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
 Assert.assertNull(JsonAdapterAnnotationTypeAdapterFactory.create(gson, intTypeToken));
 }

 @Test
 public void testCreate_SimpleJsonAdapterClass() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 StringJsonAdapter stringJsonAdapter = new StringJsonAdapter();
 JsonAdapter stringJsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
 Assert.assertEquals(stringJsonAdapter, JsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken));
 }

 @Test
 public void testCreate_TypeAdapterFactory() {
 TypeToken<MyCustomClass> myCustomClassTypeToken = new TypeToken<MyCustomClass>() {};
 MyCustomTypeAdapterFactory myCustomTypeAdapterFactory = new MyCustomTypeAdapterFactory();
 JsonAdapter myCustomJsonAdapterAnnotation = myCustomClassTypeToken.getRawType().getAnnotation(JsonAdapter.class);
 TypeAdapter<MyCustomClass> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.create(gson, myCustomClassTypeToken);
 Assert.assertEquals(myCustomTypeAdapterFactory, typeAdapter.getClass().getGenericInterfaces()[0].getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_InvalidJsonAdapterValue() {
 TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
 JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, intTypeToken, null);
 }

 @Test
 public void testGetTypeAdapter_TypeAdapter() {
 Class<StringJsonAdapter> stringJsonAdapterClass = StringJsonAdapter.class;
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 StringJsonAdapter stringJsonAdapter = new StringJsonAdapter();
 Assert.assertEquals(stringJsonAdapter, JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, stringJsonAdapterAnnotation));
 }

 @Test
 public void testGetTypeAdapter_TypeAdapterFactory() {
 Class<MyCustomTypeAdapterFactory> myCustomTypeAdapterFactoryClass = MyCustomTypeAdapterFactory.class;
 TypeToken<MyCustomClass> myCustomClassTypeToken = new TypeToken<MyCustomClass>() {};
 MyCustomTypeAdapterFactory myCustomTypeAdapterFactory = new MyCustomTypeAdapterFactory();
 Assert.assertEquals(myCustomTypeAdapterFactory, JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, myCustomClassTypeToken, new JsonAdapter() {
 @JsonAdapter(MyCustomTypeAdapterFactory.class)
 public Class<? extends TypeAdapter<?>> value() {
 return myCustomTypeAdapterFactoryClass;
 }
 }).getClass());
 }
}

class StringJsonAdapter extends TypeAdapter<String> {}
class MyCustomClass {}
class MyCustomTypeAdapterFactory implements TypeAdapterFactory {}