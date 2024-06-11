import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import static com.google.gson.internal.ConstructorConstructor.ObjectConstructor;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

 @Test
 public void testGetInstanceWithValidTypeToken() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 ObjectConstructor<String> constructor = constructorConstructor.get(stringTypeToken);
 String instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertEquals("", instance);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullTypeToken() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<String> constructor = constructorConstructor.get(null);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceWithUnknownTypeToken() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};
 ObjectConstructor<Integer> constructor = constructorConstructor.get(integerTypeToken);
 }

 @Test
 public void testGetInstanceWithType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type intType = Integer.TYPE;
 ObjectConstructor<Integer> constructor = constructorConstructor.get(intType);
 Integer instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertEquals(0, instance.intValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Integer> constructor = constructorConstructor.get(null);
 }

 @Test
 public void testGetInstanceWithRawType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Integer> constructor = constructorConstructor.get(Integer.class);
 Integer instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertEquals(0, instance.intValue());
 }

 @Test
 public void testGetInstanceWithInstanceCreator() {
 InstanceCreator<String> stringInstanceCreator = new StringInstanceCreator();
 instanceCreators.put(String.class, stringInstanceCreator);
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 String instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertEquals("instance creator", instance);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceWithNoConstructorFound() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type unknownType = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> constructor = constructorConstructor.get(unknownType);
 }

 @Test
 public void testNewDefaultConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type intType = Integer.TYPE;
 ObjectConstructor<Integer> constructor = constructorConstructor.newDefaultConstructor(intType.getClass());
 Integer instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertEquals(0, instance.intValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNewDefaultConstructorWithNullType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Integer> constructor = constructorConstructor.newDefaultConstructor(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewDefaultConstructorWithInvalidType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Object> constructor = constructorConstructor.newDefaultConstructor(String.class);
 }

 @Test
 public void testNewDefaultImplementationConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type collectionType = new TypeToken<Collection<String>>() {}.getType();
 ObjectConstructor<Collection<String>> constructor = constructorConstructor.newDefaultImplementationConstructor(collectionType, Collection.class);
 Collection<String> instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertTrue(instance instanceof ArrayList);
 }

 @Test
 public void testNewDefaultImplementationConstructorForMap() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
 ObjectConstructor<Map<String, Integer>> constructor = constructorConstructor.newDefaultImplementationConstructor(mapType, Map.class);
 Map<String, Integer> instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertTrue(instance instanceof LinkedHashMap);
 }

 @Test
 public void testNewUnsafeAllocator() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type stringType = String.class;
 ObjectConstructor<String> constructor = constructorConstructor.newUnsafeAllocator(stringType, String.class);
 String instance = constructor.construct();
 Assert.assertNotNull(instance);
 Assert.assertEquals("", instance);
 }

 @Test(expected = NullPointerException.class)
 public void testNewUnsafeAllocatorWithNullType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<String> constructor = constructorConstructor.newUnsafeAllocator(null, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewUnsafeAllocatorWithInvalidType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Object> constructor = constructorConstructor.newUnsafeAllocator(Integer.TYPE, String.class);
 }

 @Test
 public void testToString() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 String result = constructorConstructor.toString();
 System.out.println(result);
 }
}

class StringInstanceCreator implements InstanceCreator<String> {
 @Override public String createInstance(Type type) {
 return "instance creator";
 }
}