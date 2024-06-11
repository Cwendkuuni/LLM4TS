import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators;
 private ConstructorConstructor constructorConstructor;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 instanceCreators = new LinkedHashMap<>();
 Gson gson = new Gson();
 constructorConstructor = new ConstructorConstructor(gson.getAdapter(new TypeAdapterFactory() {
 @SuppressWarnings("unchecked")
 @Override public <T> TypeAdapter<T> create(Type type) {
 return (TypeAdapter<T>) null;
 }
 })).init();
 }

 @Test
 public void testGet_TypeToken_ReturnsObjectConstructor() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 ObjectConstructor<String> stringObjectConstructor = constructorConstructor.get(stringTypeToken);
 Assert.assertNotNull(stringObjectConstructor);
 }

 @Test(expected = NullPointerException.class)
 public void testGet_NullTypeToken_ThrowsNullPointerException() {
 ObjectConstructor<String> stringObjectConstructor = constructorConstructor.get(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidTypeToken_ThrowsIllegalArgumentException() {
 ObjectConstructor<?> invalidObjectConstructor = constructorConstructor.get(int.class);
 Assert.assertNull(invalidObjectConstructor);
 }

 @Test
 public void testNewDefaultConstructor_ClassWithDefaultConstructor_ReturnsObjectConstructor() {
 Class<String> stringClass = String.class;
 ObjectConstructor<String> stringObjectConstructor = constructorConstructor.newDefaultConstructor(stringClass);
 Assert.assertNotNull(stringObjectConstructor);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testNewDefaultConstructor_ClassWithoutDefaultConstructor_ThrowsNoSuchMethodException() {
 Class<MyClass> myClass = new MyClass();
 ObjectConstructor<MyClass> myObjectConstructor = constructorConstructor.newDefaultConstructor(myClass);
 Assert.assertNull(myObjectConstructor);
 }

 @Test
 public void testNewDefaultImplementationConstructor_Collection_ReturnsObjectConstructor() {
 TypeCollectionType collectionType = new TypeToken<Collection<String>>() {};
 ObjectConstructor<Collection<String>> collectionObjectConstructor = constructorConstructor.newDefaultImplementationConstructor(
 collectionType.getType(), Collection.class);
 Assert.assertNotNull(collectionObjectConstructor);
 }

 @Test
 public void testNewDefaultImplementationConstructor_Map_ReturnsObjectConstructor() {
 TypeMapType mapType = new TypeToken<Map<String, String>>() {};
 ObjectConstructor<Map<String, String>> mapObjectConstructor = constructorConstructor.newDefaultImplementationConstructor(
 mapType.getType(), Map.class);
 Assert.assertNotNull(mapObjectConstructor);
 }

 @Test(expected = JsonIOException.class)
 public void testNewDefaultImplementationConstructor_InvalidMapType_ThrowsJsonIOException() {
 TypeMapType mapType = new TypeToken<Map<Integer, String>>() {};
 ObjectConstructor<Map<Integer, String>> mapObjectConstructor = constructorConstructor.newDefaultImplementationConstructor(
 mapType.getType(), Map.class);
 Assert.assertNull(mapObjectConstructor);
 }

 @Test
 public void testNewUnsafeAllocator_ClassWithNoArgsConstructor_ReturnsObjectConstructor() {
 Class<String> stringClass = String.class;
 ObjectConstructor<String> stringObjectConstructor = constructorConstructor.newUnsafeAllocator(stringClass.getType(), stringClass);
 Assert.assertNotNull(stringObjectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testNewUnsafeAllocator_ClassWithoutNoArgsConstructor_ThrowsRuntimeException() {
 Class<MyClass> myClass = new MyClass();
 ObjectConstructor<MyClass> myObjectConstructor = constructorConstructor.newUnsafeAllocator(myClass.getType(), myClass);
 }
}

class MyClass {
 // empty class
}