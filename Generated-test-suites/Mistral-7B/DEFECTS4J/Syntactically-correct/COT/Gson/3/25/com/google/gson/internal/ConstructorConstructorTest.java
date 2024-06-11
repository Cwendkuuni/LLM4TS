import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.UnsafeAllocator;

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @Before
 public void setup() {
 Gson gson = new Gson();
 Map<Type, InstanceCreator<?>> instanceCreators = gson.getDelegateAdapterFactory().getRegistry().getAdapter(Map.class);
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGet_SimpleType() {
 Type typeToken = new TypeToken<Integer>() {};
 ObjectConstructor<Integer> constructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(constructor);
 Assert.assertEquals(Integer.class, constructor.construct().getClass());
 }

 @Test
 public void testGet_ClassType() {
 ObjectConstructor<String> constructor = constructorConstructor.get(String.class.getTypeName());
 Assert.assertNotNull(constructor);
 Assert.assertEquals(String.class, constructor.construct().getClass());
 }

 @Test
 public void testGet_CollectionType() {
 Type collectionType = new TypeToken<Collection<Integer>>() {}.getType();
 ObjectConstructor<Collection<Integer>> constructor = constructorConstructor.get(collectionType);
 Assert.assertNotNull(constructor);
 Assert.assertTrue(constructor.construct() instanceof Collection);
 }

 @Test
 public void testGet_MapType() {
 Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
 ObjectConstructor<Map<String, Integer>> constructor = constructorConstructor.get(mapType);
 Assert.assertNotNull(constructor);
 Assert.assertTrue(constructor.construct() instanceof Map);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_UnknownType() {
 Type unknownType = new TypeToken<Foo>() {}.getType();
 ObjectConstructor<Foo> constructor = constructorConstructor.get(unknownType);
 Assert.assertNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_NoConstructorFound() {
 Type typeToken = new TypeToken<FooWithNoDefaultConstructor>() {}.getType();
 ObjectConstructor<FooWithNoDefaultConstructor> constructor = constructorConstructor.get(typeToken);
 Assert.assertNull(constructor);
 }

 @Test
 public void testGet_CustomInstanceCreator() {
 InstanceCreator<FooWithCustomCreator> customCreator = new InstanceCreator<FooWithCustomCreator>() {
 @Override
 public FooWithCustomCreator createInstance(Type type) {
 return new FooWithCustomCreator();
 }
 };
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 instanceCreators.put(FooWithCustomCreator.class, customCreator);
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<FooWithCustomCreator> constructor = constructorConstructor.get(new TypeToken<FooWithCustomCreator>() {}.getType());
 Assert.assertNotNull(constructor);
 Assert.assertEquals(FooWithCustomCreator.class, constructor.construct().getClass());
 }

 @Test
 public void testGet_RawType() {
 Type typeToken = new TypeToken<List<Integer>>() {}.getType();
 ObjectConstructor<List<Integer>> constructor = constructorConstructor.get(typeToken);
 Class<? super List<Integer>> rawType = (Class<? super List<Integer>>) typeToken.getRawType();
 ObjectConstructor<List<Integer>> rawTypeConstructor = constructorConstructor.get(rawType);
 Assert.assertNotNull(rawTypeConstructor);
 Assert.assertEquals(List.class, rawTypeConstructor.construct().getClass().getComponentType());
 Assert.assertEquals(Integer.class, rawTypeConstructor.construct().getClass().getGenericSuperclass());
 Assert.assertEquals(List.class, constructor.construct().getClass());
 Assert.assertEquals(constructor.construct().getClass().getComponentType(), Integer.class);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_UnsupportedRawType() {
 Type typeToken = new TypeToken<String>() {}.getType();
 ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);
 Class<? super String> rawType = String[].class;
 ObjectConstructor<String> rawTypeConstructor = constructorConstructor.get(rawType);
 Assert.assertNull(rawTypeConstructor);
 }

 @Test
 public void testToString() {
 Map<Type, InstanceCreator<?>> instanceCreators = constructorConstructor.getInstanceCreators();
 Assert.assertNotNull(instanceCreators);
 Assert.assertEquals(constructorConstructor.toString(), instanceCreators.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testGet_UnsafeAllocatorFailure() {
 Type type = Unsafe.class.getTypeName();
 ObjectConstructor<Unsafe> constructor = constructorConstructor.get(type);
 Assert.assertNull(constructor);
 }
}

public class Foo {
}

public class FooWithNoDefaultConstructor {
}

public class FooWithCustomCreator {
}

class UnsafeAllocator {
 // Unsafe allocation logic here
}