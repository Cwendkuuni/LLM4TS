import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import static com.google.gson.internal.ConstructorConstructor.ObjectConstructor;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

 @Test
 public void testGetMethod() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type typeToken = new TypeToken<ArrayList<Integer>>() {};
 ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(typeToken);
 ArrayList<Integer> instance = constructor.construct();
 Assert.assertNotNull(instance);
 }

 @Test(expected = RuntimeException.class)
 public void testGetMethodNoCreator() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);
 }

 @Test
 public void testGetMethodRawType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type typeToken = new TypeToken<ArrayList<Integer>>() {};
 ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(typeToken.getType());
 ArrayList<Integer> instance = constructor.construct();
 Assert.assertNotNull(instance);
 }

 @Test
 public void testNewDefaultConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type rawType = Integer.TYPE;
 ObjectConstructor<Integer> constructor = constructorConstructor.newDefaultConstructor(rawType.getClass());
 Integer instance = constructor.construct();
 Assert.assertNotNull(instance);
 }

 @Test(expected = RuntimeException.class)
 public void testNewDefaultConstructorNoConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type rawType = String.class;
 ObjectConstructor<String> constructor = constructorConstructor.newDefaultConstructor(rawType);
 }

 @Test
 public void testNewDefaultImplementationConstructorMap() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type mapType = new TypeToken<Map<String, ArrayList<Integer>>>() {}.getType();
 ObjectConstructor<Map<String, ArrayList<Integer>>> constructor = constructorConstructor.newDefaultImplementationConstructor(mapType, Map.class);
 Map<String, ArrayList<Integer>> instance = constructor.construct();
 Assert.assertNotNull(instance);
 }

 @Test
 public void testNewDefaultImplementationConstructorCollection() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type collectionType = new TypeToken<SortedSet<TreeSet<Integer>>>() {}.getType();
 ObjectConstructor<SortedSet<TreeSet<Integer>>> constructor = constructorConstructor.newDefaultImplementationConstructor(collectionType, SortedSet.class);
 SortedSet<TreeSet<Integer>> instance = constructor.construct();
 Assert.assertNotNull(instance);
 }

 @Test(expected = JsonIOException.class)
 public void testNewDefaultImplementationConstructorInvalidEnumSetType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type enumSetType = new TypeToken<EnumSet<String>>() {}.getType();
 ObjectConstructor<EnumSet<String>> constructor = constructorConstructor.newDefaultImplementationConstructor(enumSetType, EnumSet.class);
 }

 @Test
 public void testNewUnsafeAllocator() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
 ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.newUnsafeAllocator(type, ArrayList.class);
 ArrayList<Integer> instance = constructor.construct();
 Assert.assertNotNull(instance);
 }

 @Test(expected = RuntimeException.class)
 public void testNewUnsafeAllocatorNoConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type type = new TypeToken<String>() {}.getType();
 ObjectConstructor<String> constructor = constructorConstructor.newUnsafeAllocator(type, String.class);
 }
}